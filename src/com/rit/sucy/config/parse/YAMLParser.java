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

import com.sun.xml.internal.fastinfoset.Encoder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Custom parser for YAML that preserves comments with
 * the key they preceed
 */
public class YAMLParser
{
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
            ex.printStackTrace();
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
        while (i < lines.length && ((spaces = countSpaces(lines[i])) >= indent || lines[i].length() == 0 || lines[i].charAt(spaces) == '#'))
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
            else if (i < lines.length - 1
                     && lines[i + 1].length() > indent + 1
                     && lines[i + 1].charAt(indent) == '-'
                     && lines[i + 1].charAt(indent + 1) == ' '
                     && countSpaces(lines[i + 1]) == indent)
            {
                ArrayList<String> stringList = new ArrayList<String>();
                while (++i < lines.length
                       && lines[i].length() > indent
                       && lines[i].charAt(indent) == '-'
                       && lines[i].charAt(indent + 1) == ' ')
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

    /**
     * Saves config data to a file
     *
     * @param path path to the file
     */
    public static void save(DataSection data, String path)
    {
        save(data, new File(path));
    }

    /**
     * Dumps the data contents to a file to the given file
     *
     * @param file file to dump to
     */
    public static void save(DataSection data, File file)
    {
        try
        {
            FileOutputStream out = new FileOutputStream(file);
            BufferedWriter write = new BufferedWriter(new OutputStreamWriter(out, Encoder.UTF_8));

            save(data, write);

            write.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Dumps the data contents into the stream
     *
     * @param write  stream to dump to
     *
     * @throws IOException
     */
    public static void save(DataSection data, BufferedWriter write) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        dump(data, sb, 0, '\'');
        write.write(sb.toString());
    }

    /**
     * Dumps config data to a string in YAML format
     *
     * @param data    data to dump
     * @param builder string builder to use
     * @param indent  starting indent
     * @param quote   character to use for containing strings
     */
    public static void dump(DataSection data, StringBuilder builder, int indent, char quote)
    {
        // Create spacing to use
        String spacing = "";
        for (int i = 0; i < indent; i++)
        {
            spacing += ' ';
        }

        for (String key : data.keys())
        {
            // Comments first
            if (data.hasComment(key))
            {
                List<String> lines = data.getComments(key);
                for (String line : lines)
                {
                    if (line.length() == 0)
                    {
                        builder.append('\n');
                        continue;
                    }
                    builder.append(spacing);
                    builder.append('#');
                    builder.append(line);
                    builder.append('\n');
                }
            }

            // Write the key
            builder.append(spacing);
            builder.append(key);
            builder.append(": ");

            Object value = data.get(key);

            // Empty section
            if (value == null)
            {
                builder.append(" {}\n");
            }

            // Section with content
            else if (value instanceof DataSection)
            {
                DataSection child = (DataSection) value;
                if (child.size() == 0)
                {
                    builder.append(" {}\n");
                }
                else
                {
                    builder.append('\n');
                    dump(child, builder, indent + 2, quote);
                }
            }

            // List value
            else if (value instanceof List)
            {
                List list = (List) value;
                if (list.size() == 0)
                {
                    builder.append(" []");
                    builder.append('\n');
                }
                else
                {
                    builder.append('\n');
                    for (Object item : list)
                    {
                        builder.append(spacing);
                        builder.append("- ");
                        writeValue(builder, item, quote);
                        builder.append('\n');
                    }
                }
            }

            // Single value
            else
            {
                writeValue(builder, value, quote);
                builder.append('\n');
            }
        }
    }

    private static void writeValue(StringBuilder builder, Object value, char quote)
    {
        if (value instanceof Number)
        {
            builder.append(value.toString());
        }
        else if (value.toString().contains("" + quote))
        {
            builder.append('"');
            builder.append(value.toString());
            builder.append('"');
        }
        else
        {
            builder.append(quote);
            builder.append(value.toString());
            builder.append(quote);
        }
    }
}
