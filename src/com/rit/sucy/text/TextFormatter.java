package com.rit.sucy.text;

import java.text.DecimalFormat;

/**
 * Formats strings into various forms
 */
public class TextFormatter {

    /**
     * Formats text to be separate words with each word capitalized
     * (e.g. This Would Be A Result)
     * @param string string to format
     * @return       formatted string
     */
    public static String format(String string) {
        if (string == null || string.length() == 0)
            return string;

        String[] pieces = split(string);
        String result = "";
        for (String part : pieces) {
            result += " " + part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
        }
        return result.substring(1);
    }

    /**
     * Formats text into lower camel case form
     * (e.g. thisWouldBeAResult
     *
     * @param string string to be formatted
     * @return       formatted string
     */
    public static String formatLowerCamel(String string) {
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
     * @return       formatted string
     */
    public static String formatUpperCamel(String string) {
        if (string == null || string.length() == 0)
            return string;

        String[] pieces = split(string);
        String result = "";
        for (String piece : pieces)
            result += " " + piece.substring(0, 1).toUpperCase() + piece.substring(1).toLowerCase();
        return result.substring(1);
    }

    /**
     * Formats a decimal number
     *
     * @param number   number to format
     * @param decimals how many decimal places should be used
     * @param commas   whether or not to add commas (e.g. 1,210,321)
     * @return         formatted number
     */
    public static String formatNumber(double number, int decimals, boolean commas) {
        String formatString = commas ? "#,###,###,##0" : "#########0";
        if (decimals >= 1) formatString += ".0";
        for (int i = 1; i < decimals; i++) formatString += "0";
        return new DecimalFormat(formatString).format(number);
    }

    /**
     * Splits a string
     *
     * @param string string to split
     * @return       split string
     */
    private static String[] split(String string) {
        if (string.contains(" "))
            return string.split(" ");
        if (string.contains("_"))
            return string.split("_");
        else return new String[] { string };
    }
}
