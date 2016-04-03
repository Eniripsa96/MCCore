/**
 * MCCore
 * com.rit.sucy.config.parse.DataArray
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

import java.util.*;

/**
 * Represents a section of a config
 */
public class DataArray
{
    // Values attached to each node
    private ArrayList<Object> data = new ArrayList<Object>();

    /**
     * Clears all data and comments from the data section
     */
    public void clear()
    {
        data.clear();
    }

    /**
     * @return number of key/value pairs
     */
    public int size()
    {
        return data.size();
    }

    /**
     * Retrieves the values contained in the keys for this section
     *
     * @return values contained in the keys for this section
     */
    public ArrayList<Object> values()
    {
        return new ArrayList<Object>(data);
    }

    /**
     * Adds a value to the array
     *
     * @param value value to add
     */
    public void add(Object value)
    {
        data.add(value);
    }

    /**
     * Fetches a value using an index
     *
     * @param index index to use
     * @return fetched value
     */
    public Object get(int index)
    {
        return data.get(index);
    }

    /**
     * Gets the array as a string list
     *
     * @return string list representation
     */
    public List<String> asStringList()
    {
        ArrayList<String> list = new ArrayList<String>();
        for (Object obj : data)
            list.add(obj.toString());
        return list;
    }

    /**
     * Gets the array as a data section list
     *
     * @return data list representation
     */
    public List<DataSection> asDataList()
    {
        ArrayList<DataSection> list = new ArrayList<DataSection>();
        for (Object obj : data)
            list.add((DataSection)obj);
        return list;
    }
}
