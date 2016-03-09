/**
 * MCCore
 * com.rit.sucy.text.TypeChecker
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
package com.rit.sucy.text;

/**
 * Checks strings if they match other primitive type formats
 */
public class TypeChecker
{

    /**
     * Regex string for matching integer values
     */
    public static final String INT_REGEX = "[+-]?[0-9]+";

    /**
     * Regex string for matching boolean values
     */
    public static final String BOOL_REGEX = "(true)|(false)";

    /**
     * Regex string for matching double values
     */
    public static final String DOUBLE_REGEX = "[+-]?[0-9]+(\\.[0-9]+)?";

    /**
     * Checks if a string can be parsed as an integer
     *
     * @param arg string to check
     *
     * @return true if can be parsed as an integer, false otherwise
     */
    public static boolean isInteger(String arg)
    {
        return arg.matches(INT_REGEX);
    }

    /**
     * Checks if a string can be parsed as a boolean
     *
     * @param arg string to check
     *
     * @return true if can be parsed as a boolean value, false otherwise
     */
    public static boolean isBoolean(String arg)
    {
        return arg.toLowerCase().matches(BOOL_REGEX);
    }

    /**
     * Checks if a string can be parsed as a double
     *
     * @param arg string to check
     *
     * @return true if can be parsed as a double, false otherwise
     */
    public static boolean isDouble(String arg)
    {
        return arg.matches(DOUBLE_REGEX);
    }
}
