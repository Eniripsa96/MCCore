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
