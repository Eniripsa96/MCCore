/**
 * MCCore
 * com.rit.sucy.config.parse.DataSection
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

import java.io.*;
import java.util.*;

/**
 * Represents a section of a config
 */
public class DataSection {
    // Comments attached to each node
    private HashMap<String, List<String>> comments = new HashMap<String, List<String>>();

    // Values attached to each node
    private HashMap<String, Object> data = new HashMap<String, Object>();

    // All of the available keys
    private ArrayList<String> keys = new ArrayList<String>();

    /**
     * Clears all data and comments from the data section
     */
    public void clear() {
        keys.clear();
        comments.clear();
        data.clear();
    }

    /**
     * Retrieves the keys within the section
     *
     * @return keys in the data section
     */
    public List<String> keys() {
        return new ArrayList<String>(keys);
    }

    /**
     * @return number of key/value pairs
     */
    public int size() {
        return keys.size();
    }

    /**
     * Retrieves the set of entries in this data section
     *
     * @return entry set of the data section
     */
    public Set<Map.Entry<String, Object>> entrySet() {
        return data.entrySet();
    }

    /**
     * Retrieves the values contained in the keys for this section
     *
     * @return values contained in the keys for this section
     */
    public Collection<Object> values() {
        return data.values();
    }

    /**
     * Adds a comment in front of the value with the key
     *
     * @param key     key of the value the comment is in front of
     * @param comment comment to add
     */
    public void addComment(String key, String comment) {
        if (!comments.containsKey(key)) comments.put(key, new ArrayList<String>());
        comments.get(key).add(comment);
        if (!keys.contains(key)) keys.add(key);
    }

    /**
     * Checks whether or not the data has a comment for it
     *
     * @param key data key
     * @return true if has comments
     */
    public boolean hasComment(String key) {
        return comments.containsKey(key);
    }

    /**
     * Gets the comments for a data value
     *
     * @param key data key
     * @return list of comments
     */
    public List<String> getComments(String key) {
        return comments.get(key);
    }

    /**
     * Sets the comments to have above the specified node
     *
     * @param comments comments above a given node
     */
    public void setComments(String key, List<String> comments) {
        ArrayList<String> list = new ArrayList<String>(comments.size());
        list.addAll(comments);
        this.comments.put(key, list);
        if (!keys.contains(key)) keys.add(key);
    }

    /**
     * Clears the comments for the given node
     *
     * @param key key of the node to clear for
     */
    public void clearComments(String key) {
        this.comments.remove(key);
    }

    /**
     * Clears all comments
     *
     * @param deep true if to clear comments in all child sections as well
     */
    public void clearAllComments(boolean deep) {
        this.comments.clear();
        if (deep) {
            for (Object value : data.values()) {
                if (value instanceof DataSection) {
                    ((DataSection) value).clearAllComments(true);
                }
            }
        }
    }

    /**
     * Sets a value to the config
     *
     * @param key   key to represent the value
     * @param value value to set
     */
    public void set(String key, Object value) {
        if (value == null) remove(key);
        else if (value instanceof Map<?, ?>) {
            DataSection section = createSection(key);
            Map<?, ?> map = (Map<?, ?>) value;
            for (Map.Entry<?, ?> k : map.entrySet()) {
                section.set(k.getKey().toString(), k.getValue().toString());
            }
        } else {
            data.put(key, value);
            if (!keys.contains(key)) keys.add(key);
        }
    }

    /**
     * Checks for a default value in the data. If the value is
     * not there, the provided default value will be applied.
     *
     * @param key          key of the default value
     * @param defaultValue value to apply if no value is present
     */
    public void checkDefault(String key, Object defaultValue) {
        if (!has(key)) {
            set(key, defaultValue);
        }
    }

    /**
     * Creates a new data section at the given key
     *
     * @param key key to create the section at
     *
     * @return the created section
     */
    public DataSection createSection(String key) {
        DataSection section = new DataSection();
        data.put(key, section);
        if (!keys.contains(key)) keys.add(key);
        return section;
    }

    /**
     * Checks for a data section at the given key. If it is
     * not a section, a new one will be created and returned.
     * Otherwise, the existing section will be returned.
     *
     * @param key key to check for a section at
     *
     * @return current section at the key, new or existing
     */
    public DataSection defaultSection(String key) {
        if (isSection(key)) return getSection(key);
        return createSection(key);
    }

    /**
     * Removes a value at the given key along with its comments.
     *
     * @param key key to remove a value for
     *
     * @return the removed value or null of no value was present
     */
    public Object remove(String key) {
        if (key.contains(".")) {
            String[] pieces = key.split("\\.", 2);
            DataSection section = getSection(pieces[0]);
            if (section != null) return section.remove(pieces[1]);
            return null;
        }
        keys.remove(key);
        comments.remove(key);
        return data.remove(key);
    }

    /**
     * Checks whether or not the value at the
     * given key is a data section
     *
     * @param key key of the value
     *
     * @return true if DataSection, false otherwise
     */
    public boolean isSection(String key) {
        return getSection(key) != null;
    }

    /**
     * Checks whether or not the data contains a value at the given key
     *
     * @param key key to check
     *
     * @return true if contains a value, false otherwise
     */
    public boolean has(String key) {
        if (key.contains(".")) {
            String[] pieces = key.split("\\.", 2);
            DataSection section = getSection(pieces[0]);
            return section != null && section.has(pieces[1]);
        }
        return data.containsKey(key);
    }

    /**
     * Checks whether or not the data contains a list value
     * at the given key
     *
     * @param key key to check for a list value at
     *
     * @return true if a list value is set at the key, false otherwise
     */
    public boolean isList(String key) {
        return getList(key, null) != null;
    }

    /**
     * Checks whether or not the value at the key is a number
     *
     * @param key key to check for a number value at
     *
     * @return true if a number is at the key, false otherwise
     */
    public boolean isNumber(String key) {
        return getDouble(key, Double.MAX_VALUE) != Double.MAX_VALUE;
    }

    /**
     * Retrieves a data section from the given location
     *
     * @param key key of the section to retrieve
     *
     * @return found section or null if not found
     */
    public DataSection getSection(String key) {
        if (key.contains(".")) {
            String[] pieces = key.split("\\.", 2);
            DataSection section = getSection(pieces[0]);
            return section == null ? null : section.getSection(pieces[1]);
        }

        if (data.containsKey(key)) {
            Object obj = data.get(key);
            if (obj instanceof DataSection) {
                return (DataSection) obj;
            }
        }
        return null;
    }

    /**
     * Retrieves a character value from the data. If more than
     * one characters are found, this will only return the first.
     *
     * @param key key of the value to retrieve
     *
     * @return the character from the config or '\0' if not found
     */
    public char getChar(String key) {
        return getChar(key, '\0');
    }

    /**
     * Retrieves a string value from the data
     *
     * @param key key of the value to retrieve
     *
     * @return the string value from the config or null if not found
     */
    public String getString(String key) {
        return getString(key, null);
    }

    /**
     * Retrieves a boolean value from the data
     *
     * @param key key of the value to retrieve
     *
     * @return the boolean value from the config or false if not found
     */
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * Retrieves a byte value from the data
     *
     * @param key key of the value to retrieve
     *
     * @return the byte value or 0 if not found or not a number
     */
    public byte getByte(String key) {
        return getByte(key, (byte) 0);
    }

    /**
     * Retrieves a short value from the data
     *
     * @param key key of the value to retrieve
     *
     * @return the short value or 0 if not found or not a number
     */
    public short getShort(String key) {
        return getShort(key, (short) 0);
    }

    /**
     * Retrieves an integer value from the data
     *
     * @param key key of the value to retrieve
     *
     * @return the integer value or 0 if not found or not a number
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * Retrieves a float value from the data
     *
     * @param key key of the value to retrieve
     *
     * @return the float value or 0 if not found or not a number
     */
    public float getFloat(String key) {
        return getFloat(key, 0);
    }

    /**
     * Retrieves a double value from the data
     *
     * @param key key of the value to retrieve
     *
     * @return the double value or fallback if not found or not a number
     */
    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    /**
     * Retrieves a string list value from the config
     *
     * @param key key of the value to retrieve
     *
     * @return the list value or an empty list if not found
     */
    public List<String> getList(String key) {
        return getList(key, new ArrayList<String>());
    }

    /**
     * Retrieves a character value from the data. If more than
     * one characters are found, this will only return the first.
     *
     * @param key      key of the value to retrieve
     * @param fallback value to return if not found
     *
     * @return the character from the config or fallback if not found
     */
    public char getChar(String key, char fallback) {
        if (key.contains(".")) {
            String[] pieces = key.split("\\.", 2);
            DataSection section = getSection(pieces[0]);
            return section == null ? fallback : section.getChar(pieces[1], fallback);
        }

        String str = getString(key);
        if (str == null || str.length() == 0) return fallback;
        return str.charAt(0);
    }

    /**
     * Retrieves a string value from the data
     *
     * @param key      key of the value to retrieve
     * @param fallback value to return if not found
     *
     * @return the string value from the config or fallback if not found
     */
    public String getString(String key, String fallback) {
        if (key.contains(".")) {
            String[] pieces = key.split("\\.", 2);
            DataSection section = getSection(pieces[0]);
            return section == null ? fallback : section.getString(pieces[1], fallback);
        }

        if (!data.containsKey(key)) return fallback;
        return data.get(key).toString();
    }

    /**
     * Retrieves a boolean value from the data
     *
     * @param key      key of the value to retrieve
     * @param fallback value to return if not found
     *
     * @return the boolean value from the config or fallback if not found
     */
    public boolean getBoolean(String key, boolean fallback) {
        if (key.contains(".")) {
            String[] pieces = key.split("\\.", 2);
            DataSection section = getSection(pieces[0]);
            return section == null ? fallback : section.getBoolean(pieces[1], fallback);
        }

        if (!data.containsKey(key)) return fallback;
        String str = getString(key).toLowerCase();
        return str.equals("true") || str.equals("yes") || str.equals("t") || str.equals("y")
                || (fallback && !str.equals("false") && !str.equals("no") && !str.equals("f") && !str.equals("n"));
    }

    /**
     * Retrieves a byte value from the data
     *
     * @param key      key of the value to retrieve
     * @param fallback value to return if not found
     *
     * @return the byte value or fallback if not found or not a number
     */
    public byte getByte(String key, byte fallback) {
        if (key.contains(".")) {
            String[] pieces = key.split("\\.", 2);
            DataSection section = getSection(pieces[0]);
            return section == null ? fallback : section.getByte(pieces[1], fallback);
        }

        if (!data.containsKey(key)) return fallback;
        Object obj = data.get(key);
        try {
            return (byte) NumberParser.parseInt(obj.toString());
        } catch (Exception ex) {
            return fallback;
        }
    }

    /**
     * Retrieves a short value from the data
     *
     * @param key      key of the value to retrieve
     * @param fallback value to return if not found
     *
     * @return the short value or fallback if not found or not a number
     */
    public short getShort(String key, short fallback) {
        if (key.contains(".")) {
            String[] pieces = key.split("\\.", 2);
            DataSection section = getSection(pieces[0]);
            return section == null ? fallback : section.getShort(pieces[1], fallback);
        }

        if (!data.containsKey(key)) return fallback;
        Object obj = data.get(key);
        try {
            return (short) NumberParser.parseInt(obj.toString());
        } catch (Exception ex) {
            return fallback;
        }
    }

    /**
     * Retrieves an integer value from the data
     *
     * @param key      key of the value to retrieve
     * @param fallback value to return if not found
     *
     * @return the integer value or fallback if not found or not a number
     */
    public int getInt(String key, int fallback) {
        if (key.contains(".")) {
            String[] pieces = key.split("\\.", 2);
            DataSection section = getSection(pieces[0]);
            return section == null ? fallback : section.getInt(pieces[1], fallback);
        }

        if (!data.containsKey(key)) return fallback;
        Object obj = data.get(key);
        try {
            return NumberParser.parseInt(obj.toString());
        } catch (Exception ex) {
            return fallback;
        }
    }

    /**
     * Retrieves a float value from the data
     *
     * @param key      key of the value to retrieve
     * @param fallback value to return if not found
     *
     * @return the float value or fallback if not found or not a number
     */
    public float getFloat(String key, float fallback) {
        if (key.contains(".")) {
            String[] pieces = key.split("\\.", 2);
            DataSection section = getSection(pieces[0]);
            return section == null ? fallback : section.getFloat(pieces[1], fallback);
        }

        if (!data.containsKey(key)) return -1;
        Object obj = data.get(key);
        try {
            return (float) NumberParser.parseDouble(obj.toString());
        } catch (Exception ex) {
            return fallback;
        }
    }

    /**
     * Retrieves a double value from the data
     *
     * @param key      key of the value to retrieve
     * @param fallback value to return if not found
     *
     * @return the double value or fallback if not found or not a number
     */
    public double getDouble(String key, double fallback) {
        if (key.contains(".")) {
            String[] pieces = key.split("\\.", 2);
            DataSection section = getSection(pieces[0]);
            return section == null ? fallback : section.getDouble(pieces[1], fallback);
        }

        if (!data.containsKey(key)) return fallback;
        Object obj = data.get(key);
        try {
            return NumberParser.parseDouble(obj.toString());
        } catch (Exception ex) {
            return fallback;
        }
    }

    /**
     * Retrieves a string list value from the config
     *
     * @param key      key of the value to retrieve
     * @param fallback value to return if not found
     *
     * @return the list value or fallback if not found
     */
    public List<String> getList(String key, List<String> fallback) {
        if (key.contains(".")) {
            String[] pieces = key.split("\\.", 2);
            DataSection section = getSection(pieces[0]);
            return section == null ? fallback : section.getList(pieces[1], fallback);
        }

        if (!data.containsKey(key)) {
            return fallback;
        }
        Object obj = data.get(key);
        if (obj instanceof List) {
            List l = (List) obj;
            ArrayList<String> list = new ArrayList<String>(l.size());
            list.addAll(l);
            return list;
        } else return fallback;
    }

    /**
     * Retrieves a generic value at the given key
     *
     * @param key key to get the value for
     *
     * @return the value at the given key
     */
    public Object get(String key) {
        if (key.contains(".")) {
            String[] pieces = key.split("\\.", 2);
            DataSection section = getSection(pieces[0]);
            return section == null ? null : section.get(pieces[1]);
        }
        return data.get(key);
    }

    /**
     * Retrieves a generic value at the given key
     *
     * @param key      key to get the value for
     * @param fallback value to return if not found
     *
     * @return the value at the given key or fallback if not found
     */
    public Object get(String key, Object fallback) {
        if (key.contains(".")) {
            String[] pieces = key.split("\\.", 2);
            DataSection section = getSection(pieces[0]);
            return section == null ? fallback : section.get(pieces[1]);
        }

        if (data.containsKey(key)) return data.get(key);
        return fallback;
    }

    /**
     * Applies defaults to this data section
     *
     * @param defaults defaults to apply
     */
    public void applyDefaults(DataSection defaults) {
        for (String key : defaults.keys) {
            if (defaults.comments.containsKey(key)) {
                setComments(key, defaults.comments.get(key));
            }
            if (defaults.isSection(key)) {
                DataSection section = defaultSection(key);
                section.applyDefaults(defaults.getSection(key));
            } else checkDefault(key, defaults.get(key));
        }
    }

    /**
     * Trims the data, only keeping what is also in the default section
     *
     * @param defaults default section to trim to
     */
    public void trim(DataSection defaults) {
        ArrayList<String> copy = new ArrayList<String>(keys);
        for (String key : copy) {
            if (!defaults.has(key)) {
                remove(key);
            } else if (defaults.isSection(key)) {
                if (isSection(key)) getSection(key).trim(defaults.getSection(key));
                else remove(key);
            }
        }
    }

    /**
     * Dumps the data contents to a file at the given path
     *
     * @param path path to the file
     */
    public void dump(String path) {
        YAMLParser.save(this, path);
    }

    /**
     * Dumps the data contents to a file to the given file
     *
     * @param file file to dump to
     */
    public void dump(File file) {
        YAMLParser.save(this, file);
    }

    /**
     * Dumps the data contents into the stream
     *
     * @param write  stream to dump to
     *
     * @throws IOException
     */
    public void dump(BufferedWriter write) throws IOException {
        YAMLParser.save(this, write);
    }

    /**
     * Returns the YAML string for the config data
     *
     * @return YAML data string
     */
    @Override
    public String toString() {
        return toString('\'');
    }

    /**
     * Returns the YAML string for the config data
     *
     * @param quote the character to wrap strings in
     * @return YAML data string
     */
    public String toString(char quote) {
        StringBuilder builder = new StringBuilder();
        YAMLParser.dump(this, builder, 0, quote);
        return builder.toString();
    }
}
