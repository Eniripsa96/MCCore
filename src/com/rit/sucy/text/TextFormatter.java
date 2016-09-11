/**
 * MCCore
 * com.rit.sucy.text.TextFormatter
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

import org.bukkit.ChatColor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Formats strings into various forms
 */
public class TextFormatter
{
    /**
     * Regex string for finding color patterns
     */
    private static final String  COLOR_REGEX   = "([0-9a-fl-orA-FL-OR])";
    private static final Pattern COLOR_PATTERN = Pattern.compile(COLOR_REGEX);

    /**
     * Formats text into individual words
     * (e.g. This Would Be A Result)
     *
     * @param string string to format
     *
     * @return formatted string
     */
    public static String format(String string)
    {
        if (string == null || string.length() == 0)
            return string;

        String[] pieces = split(string);
        String result = pieces[0].substring(0, 1).toUpperCase() + pieces[0].substring(1).toLowerCase();
        for (int i = 1; i < pieces.length; i++)
        {
            result += " " + pieces[i].substring(0, 1).toUpperCase() + pieces[i].substring(1).toLowerCase();
        }
        return result;
    }

    /**
     * Formats text into lower camel case form
     * (e.g. thisWouldBeAResult
     *
     * @param string string to be formatted
     *
     * @return formatted string
     */
    public static String formatLowerCamel(String string)
    {
        if (string == null || string.length() == 0)
            return string;

        String[] pieces = split(string);
        String result = pieces[0].toLowerCase();
        for (int i = 1; i < pieces.length; i++)
            result += pieces[i].substring(0, 1).toUpperCase() + pieces[i].substring(1).toLowerCase();
        return result;
    }

    /**
     * Formats the string into upper camel case form
     * (e.g. ThisWouldBeAResult)
     *
     * @param string string to be formatted
     *
     * @return formatted string
     */
    public static String formatUpperCamel(String string)
    {
        if (string == null || string.length() == 0)
            return string;

        String[] pieces = split(string);
        String result = "";
        for (String piece : pieces)
            result += piece.substring(0, 1).toUpperCase() + piece.substring(1).toLowerCase();
        return result;
    }

    /**
     * Formats a decimal number
     *
     * @param number   number to format
     * @param decimals how many decimal places should be used
     * @param commas   whether or not to add commas (e.g. 1,210,321)
     *
     * @return formatted number
     */
    public static String formatNumber(double number, int decimals, boolean commas)
    {
        String formatString = commas ? "#,###,###,##0" : "#########0";
        if (decimals >= 1) formatString += ".0";
        for (int i = 1; i < decimals; i++) formatString += "0";
        return new DecimalFormat(formatString).format(number);
    }

    /**
     * Colors a string using & as the color indicator
     *
     * @param string string to color
     *
     * @return colored string
     */
    public static String colorString(String string)
    {
        return colorString(string, '&');
    }

    /**
     * Colors a string using the given color indicator
     *
     * @param string string to color
     * @param token  color indicator
     *
     * @return colored string
     */
    public static String colorString(String string, char token)
    {
        if (string == null) return null;
        return string.replaceAll(token + COLOR_REGEX, ChatColor.COLOR_CHAR + "$1");
    }

    /**
     * Colors a string builder using & as the color indicator
     *
     * @param sb string builder to color
     */
    public static void colorString(StringBuilder sb)
    {
        colorString(sb, '&');
    }

    /**
     * Colors a string builder using the given color indicator
     *
     * @param sb    string builder to color
     * @param token color indicator
     */
    public static void colorString(StringBuilder sb, char token)
    {
        if (sb == null) return;
        String t = token + "";
        int index = sb.indexOf(t);
        while (index >= 0 && index < sb.length() - 1)
        {
            ChatColor color = ChatColor.getByChar(sb.charAt(index + 1));
            if (color != null)
            {
                sb.setCharAt(index, ChatColor.COLOR_CHAR);
            }
            index = sb.indexOf(t);
        }
    }

    /**
     * Colors a list of strings using & as the color indicator
     *
     * @param list string list
     *
     * @return colored string list
     */
    public static List<String> colorStringList(List<String> list)
    {
        return colorStringList(list, '&');
    }

    /**
     * Colors a list of strings with the given color indicator
     *
     * @param list  string list
     * @param token color indicator
     *
     * @return colored string list
     */
    public static List<String> colorStringList(List<String> list, char token)
    {
        ArrayList<String> copy = new ArrayList<String>();
        for (String string : list)
        {
            copy.add(colorString(string, token));
        }
        return copy;
    }

    /**
     * Splits a string
     *
     * @param string string to split
     *
     * @return split string
     */
    private static String[] split(String string)
    {
        if (string == null) return null;
        if (string.contains(" ") || string.contains("_"))
            return string.split("[ _]");
        else return new String[] { string };
    }
}
