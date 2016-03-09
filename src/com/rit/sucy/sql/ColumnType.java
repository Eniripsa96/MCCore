/**
 * MCCore
 * com.rit.sucy.sql.ColumnType
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
package com.rit.sucy.sql;

/**
 * Types of columns supported
 */
public enum ColumnType
{
    /**
     * <p>A string that can be up to 16 characters long</p>
     */
    STRING_16("VARCHAR(16)"),

    /**
     * A string that can be up to 32 characters long
     */
    STRING_32("VARCHAR(32)"),

    /**
     * <p>A string that can be up to 64 characters long</p>
     */
    STRING_64("VARCHAR(64)"),

    /**
     * <p>A string that can be up to 128 characters long</p>
     */
    STRING_128("VARCHAR(128)"),

    /**
     * <p>A string that can be up to 256 characters long</p>
     */
    STRING_255("VARCHAR(255)"),

    /**
     * <p>A string that can be up to 65535 characters long</p>
     */
    TEXT("TEXT"),

    /**
     * A standard integer
     */
    INT("INT"),

    /**
     * A standard long
     */
    LONG("BIGINT"),

    /**
     * <p>A standard 4-byte float value</p>
     */
    FLOAT("FLOAT(24)"),

    /**
     * <p>A standard 8-byte double value</p>
     */
    DOUBLE("FLOAT(53)"),

    /**
     * <p>A integer increment to put a number ID on each entry</p>
     */
    INCREMENT("INT KEY AUTO_INCREMENT"),

    /**
     * <p>A date/time value in the format YYYY-MM-DD HH:MM:SS</p>
     */
    DATE_TIME("DATETIME"),;

    private final String key;

    /**
     * Enum constructor
     *
     * @param key type key
     */
    private ColumnType(String key)
    {
        this.key = key;
    }

    /**
     * @return type key
     */
    @Override
    public String toString()
    {
        return this.key;
    }

    /**
     * <p>Retrieves a column type by name and size values.</p>
     * <p>If no supported type matches the data, this will return
     * null instead.</p>
     *
     * @param type type of the column
     * @param size size of the column
     *
     * @return column type representation
     */
    public static ColumnType getByValues(String type, int size)
    {
        if (type.equalsIgnoreCase("VARCHAR"))
        {
            switch (size)
            {
                case 16:
                    return STRING_16;
                case 32:
                    return STRING_32;
                case 64:
                    return STRING_64;
                case 128:
                    return STRING_128;
                case 255:
                    return STRING_255;
                case 65535:
                    return TEXT;
                default:
                    if (size > 255) return TEXT;
                    if (size > 128) return STRING_255;
                    if (size > 64) return STRING_128;
                    if (size > 32) return STRING_64;
                    if (size > 16) return STRING_32;
                    else return STRING_16;
            }
        }
        else if (type.equals("TEXT"))
        {
            return TEXT;
        }
        else if (type.equalsIgnoreCase("INT"))
        {
            return INT;
        }
        else if (type.equals("FLOAT"))
        {
            return FLOAT;
        }
        else if (type.equals("DOUBLE"))
        {
            return DOUBLE;
        }
        else if (type.equals("DATETIME"))
        {
            return DATE_TIME;
        }
        else if (type.equals("BIGINT"))
        {
            return LONG;
        }

        return null;
    }
}
