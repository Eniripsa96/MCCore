/**
 * MCCore
 * com.rit.sucy.config.parse.YAMLParser
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Steven Sucy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software") to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.rit.sucy.config.parse;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Custom parser for YAML that preserves comments with
 * the key they preceed
 */
public class YAMLParser
{
    private static final Pattern INT    = Pattern.compile("-?[0-9]+");
    private static final Pattern DOUBLE = Pattern.compile("-?[0-9]+\\.[0-9]+");

    private static int               i        = 0;
    private static ArrayList<String> comments = new ArrayList<String>();

    /**
     * Reads and then parses data from an embedded plugin resource. If
     * the resource does not exist or doesn't contain any data, this
     * will return an empty DataSection object.
     *
     * @param plugin plugin containing the embedded resource
     * @param path   path to the resource (not including the beginning slash)
     *
     * @return loaded data
     */
    public static DataSection parseResource(Plugin plugin, String path)
    {
        try
        {
            InputStream read = plugin.getClass().getResourceAsStream("/" + path);
            StringBuilder builder = new StringBuilder();
            byte[] data = new byte[1024];
            int bytes;
            do
            {
                bytes = read.read(data);
                builder.append(new String(data, 0, bytes, "UTF-8"));
            }
            while (bytes == 1024);
            read.close();
            return parseText(builder.toString());
        }
        catch (Exception ex)
        {
            // Do nothing
            Bukkit.getLogger().info("Failed to parse resource (" + path + ") - " + ex.getMessage());
        }
        return new DataSection();
    }

    /**
     * Reads and then parses data from the file at the given path. If
     * the file does not exist or doesn't contain any data, this
     * will return an empty DataSection object.
     *
     * @param path path to the file load from
     *
     * @return loaded data
     */
    public static DataSection parseFile(String path)
    {
        return parseFile(new File(path));
    }

    /**
     * Reads and then parses data from the file. If
     * the file does not exist or doesn't contain any data, this
     * will return an empty DataSection object.
     *
     * @param file the file load from
     *
     * @return loaded data
     */
    public static DataSection parseFile(File file)
    {
        try
        {
            if (file.exists())
            {
                FileInputStream read = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                read.read(data);
                read.close();
                return parseText(new String(data, "UTF-8"));
            }
        }
        catch (Exception ex)
        {
            // Do nothing
            ex.printStackTrace();
        }
        return new DataSection();
    }

    /**
     * Parses the text read in from a file. If a null string
     * is passed in, this will return an empty data section.
     *
     * @param text text to parse
     *
     * @return parsed data
     */
    public static DataSection parseText(String text)
    {
        return parseText(text, '\'');
    }

    /**
     * Parses the text read in from a file. If a null string
     * is passed in, this will return an empty data section.
     *
     * @param text  text to parse
     * @param quote character strings are wrapped in
     *
     * @return parsed data
     */
    public static DataSection parseText(String text, char quote)
    {
        if (text == null) return new DataSection();
        comments.clear();
        text = text.replaceAll("\r\n", "\n").replaceAll("\n *\n", "\n").replaceAll(" +\n", "\n");
        String[] lines = text.split("\n");
        i = 0;
        return parse(lines, 0, quote);
    }

    /**
     * Parses YAML data into DataSection objects
     *
     * @param lines  lines to parse
     * @param indent current indent
     * @param quote character strings are wrapped in
     *
     * @return parsed data
     */
    private static DataSection parse(String[] lines, int indent, char quote)
    {
        DataSection data = new DataSection();
        int spaces;
        while (i < lines.length && ((spaces = countSpaces(lines[i])) >= indent || lines[i].charAt(spaces) == '#'))
        {
            // When the entire line is just spaces, continue
            if (lines[i].length() == spaces) {
                i++;
                continue;
            }

            // Comments
            if (lines[i].charAt(spaces) == '#')
            {
                comments.add(lines[i].substring(spaces + 1));
                i++;
                continue;
            }

            while (i < lines.length && (spaces != indent))
            {
                i++;
            }
            if (i == lines.length) return data;

            String key = lines[i].substring(indent, lines[i].indexOf(':'));
            data.setComments(key, comments);
            comments.clear();

            // New empty section
            if (lines[i].indexOf(": {}") == lines[i].length() - 4 && lines[i].length() >= 4)
            {
                data.createSection(key);
            }

            // String list
            else if (i < lines.length - 1 && lines[i + 1].length() > indent && lines[i + 1].charAt(indent) == '-' && countSpaces(lines[i + 1]) == indent)
            {
                ArrayList<String> stringList = new ArrayList<String>();
                while (++i < lines.length && lines[i].length() > indent && lines[i].charAt(indent) == '-')
                {
                    String str = lines[i].substring(indent + 2);
                    if (str.length() > 0 && str.charAt(0) == quote)
                        while (str.length() > 0 && str.charAt(0) == quote) str = str.substring(1, str.length() - 1);
                    else if (str.length() > 0 && str.charAt(0) == '"')
                        while (str.length() > 0 && str.charAt(0) == '"') str = str.substring(1, str.length() - 1);
                    else if (str.length() > 0 && str.charAt(0) == '\'')
                        while (str.length() > 0 && str.charAt(0) == '\'') str = str.substring(1, str.length() - 1);

                    stringList.add(str);
                }
                data.set(key, stringList);
                i--;
            }

            // New section with content
            else if (i < lines.length - 1 && countSpaces(lines[i + 1]) > indent)
            {
                i++;
                int newIndent = countSpaces(lines[i]);
                DataSection node = parse(lines, newIndent, quote);
                data.set(key, node);
                continue;
            }

            // New empty section
            else if (lines[i].indexOf(':') == lines[i].length() - 1)
            {
                data.set(key, new DataSection());
            }

            // Regular value
            else
            {
                String str = lines[i].substring(lines[i].indexOf(':') + 2);
                Object value;
                if (str.charAt(0) == quote) value = str.substring(1, str.length() - 1);
                else if (str.charAt(0) == '\'') value = str.substring(1, str.length() - 1);
                else if (str.charAt(0) == '"') value = str.substring(1, str.length() - 1);
                else if (INT.matcher(str).matches()) value = Integer.parseInt(str);
                else if (DOUBLE.matcher(str).matches()) value = Double.parseDouble(str);
                else value = str;
                data.set(key, value);
            }

            i++;
        }
        return data;
    }

    /**
     * Counts the number of leading spaces in the string
     *
     * @param line line to count the leading spaces for
     *
     * @return the number of leading spaces
     */
    private static int countSpaces(String line)
    {
        int c = 0;
        while (line.length() > c && line.charAt(c) == ' ') c++;
        return c;
    }
}
