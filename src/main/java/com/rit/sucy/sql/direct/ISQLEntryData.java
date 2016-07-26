/**
 * MCCore
 * com.rit.sucy.sql.direct.ISQLEntryData
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
package com.rit.sucy.sql.direct;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>A container for the data from an SQL table entry.</p>
 * <p>This is to be implemented by other plugins to match their
 * data structures.</p>
 */
public interface ISQLEntryData {

    /**
     * <p>Loads the needed data from the result set.</p>
     * <p>Get the values and don't store the result set
     * anywhere as it will be closed to prevent leaks.</p>
     *
     * @param set set to load from
     *
     * @throws java.sql.SQLException when retrieving a value from the result set goes wrong
     */
    public void loadData(ResultSet set) throws SQLException;
}
