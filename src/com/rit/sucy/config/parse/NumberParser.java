/**
 * MCCore
 * com.rit.sucy.config.parse.NumberParser
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Steven Sucy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
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

/**
 * Handles number parsing for various locales
 */
public class NumberParser
{
    /**
     * Parses an integer value from a string
     *
     * @param value string to parse
     * @return integer value
     */
    public static int parseInt(String value)
    {
        try {
            return Integer.parseInt(value);
        }
        catch (Exception ex) {
            return 0;
        }
    }

    /**
     * Parses a double value from a string
     *
     * @param value string to parse
     * @return double value
     */
    public static double parseDouble(String value)
    {
        try {
            return Double.parseDouble(value);
        }
        catch (Exception ex) {
            return 0;
        }
    }
}

