/**
 * MCCore
 * com.rit.sucy.config.parse.JSONParser
 * <p/>
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2014 Steven Sucy
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software") to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.rit.sucy.config.parse;

import com.sun.xml.fastinfoset.Encoder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.Map;

/**
 * Custom parser for JSON that doesn't trim whitespace or newlines
 * as it is meant for storing data, not for super readable files.
 */
public class JSONParser {
    private static int i = 0;

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
    public static DataSection parseResource(Plugin plugin, String path) {
        try {
            InputStream read = plugin.getClass().getResourceAsStream("/" + path);
            StringBuilder builder = new StringBuilder();
            byte[] data = new byte[1024];
            int bytes;
            do {
                bytes = read.read(data);
                builder.append(new String(data, 0, bytes, "UTF-8"));
            }
            while (bytes == 1024);
            read.close();
            return parseText(builder.toString());
        } catch (Exception ex) {
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
    public static DataSection parseFile(String path) {
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
    public static DataSection parseFile(File file) {
        try {
            if (file.exists()) {
                FileInputStream read = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                read.read(data);
                read.close();
                return parseText(new String(data, "UTF-8"));
            }
        } catch (Exception ex) {
            // Do nothing
            ex.printStackTrace();
        }
        return new DataSection();
    }

    /**
     * Parses the text read in from a file. If a null string
     * is passed in, this will return an empty data section.
     *
     * @param text  text to parse
     *
     * @return parsed data
     */
    public static DataSection parseText(String text) {
        if (text == null) return new DataSection();
        i = 0;
        return parse(text);
    }

    /**
     * Parses JSON data into DataSection objects
     *
     * @param text data to parse
     *
     * @return parsed data
     */
    private static DataSection parse(String text) {
        DataSection data = new DataSection();

        if (text.charAt(i) != '{') return data;
        if (text.charAt(i + 1) == '}') {
            i += 2;
            return data;
        }

        Object value;
        String key;
        int next, end;
        while (text.charAt(i) != '}') {
            i++;

            next = text.indexOf(':', i);

            // Grab key
            if (text.charAt(i) == '"')
                key = text.substring(i + 1, next - 1);
            else
                key = text.substring(i, next);

            // Grab value
            switch (text.charAt(next + 1)) {
                case '{':
                    i = next + 1;
                    value = parse(text);
                    break;
                case '[':
                    i = next + 1;
                    value = parseArray(text);
                    break;
                case '"':
                    end = text.indexOf('"', next + 2);
                    value = text.substring(next + 2, end);
                    i = end + 1;
                    break;
                default:
                    end = next(text, next + 1);
                    value = text.substring(next + 1, end);
                    i = end;
                    break;
            }

            data.set(key, value);
        }

        i++;
        return data;
    }

    /**
     * Parses JSON data into DataSection objects
     *
     * @param text data to parse
     *
     * @return parsed data
     */
    private static DataArray parseArray(String text) {
        DataArray array = new DataArray();

        if (text.charAt(i) != '[') return array;
        if (text.charAt(i) == ']') {
            i += 2;
            return array;
        }

        Object value;
        int end;
        while (text.charAt(i) != ']') {
            i++;

            // Grab value
            switch (text.charAt(i)) {
                case '{':
                    value = parse(text);
                    break;
                case '[':
                    value = parseArray(text);
                    break;
                case '"':
                    end = text.indexOf('"', i + 1);
                    value = text.substring(i + 1, end);
                    i = end + 1;
                    break;
                default:
                    end = next(text, i);
                    value = text.substring(i, end);
                    i = end;
                    break;
            }

            array.add(value);
        }

        i++;
        return array;
    }

    /**
     * Grabs the next index of a terminating character
     *
     * @param text    text to search through
     * @param index   index to start searching at
     * @return terminating character index
     */
    private static int next(String text, int index) {
        while (true) {
            switch (text.charAt(index)) {
                case ',':
                    return index;
                case ']':
                case '}':
                    return index;
                default:
                    index++;
            }
        }
    }

    /**
     * Saves config data to a file
     *
     * @param path path to the file
     */
    public static void save(DataSection data, String path) {
        save(data, new File(path));
    }

    /**
     * Dumps the data contents to a file to the given file
     *
     * @param file file to dump to
     */
    public static void save(DataSection data, File file) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            BufferedWriter write = new BufferedWriter(new OutputStreamWriter(out, Encoder.UTF_8));

            save(data, write);

            write.close();
        } catch (Exception ex) {
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
    public static void save(DataSection data, BufferedWriter write) throws IOException {
        StringBuilder sb = new StringBuilder();
        dump(data, sb);
        write.write(sb.toString());
    }

    /**
     * Dumps config data to a string in JSON format
     *
     * @param data    data to dump
     * @param builder string builder to use
     */
    public static void dump(DataSection data, StringBuilder builder) {
        builder.append('{');
        boolean first = true;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (first) first = false;
            else builder.append(',');
            builder.append(entry.getKey());
            builder.append(':');
            writeValue(builder, entry.getValue());
        }
        builder.append('}');
    }

    /**
     * Writes a value to the string builder
     *
     * @param builder builder to write to
     * @param value   value to write
     */
    private static void writeValue(StringBuilder builder, Object value) {
        // Sections
        if (value instanceof DataSection) {
            dump((DataSection) value, builder);
        }

        // Arrays
        else if (value instanceof DataArray) {
            builder.append('[');
            boolean first = true;
            DataArray array = (DataArray) value;
            for (int i = 0; i < array.size(); i++) {
                if (first) first = false;
                else builder.append(',');
                writeValue(builder, array.get(i));
            }
            builder.append(']');
        }

        // Strings that need quotes
        else if (value.toString().contains(":")) {
            builder.append('"');
            builder.append(value.toString());
            builder.append('"');
        }

        // Strings and numbers that don't need quotes
        else {
            builder.append(value.toString());
        }
    }
}
