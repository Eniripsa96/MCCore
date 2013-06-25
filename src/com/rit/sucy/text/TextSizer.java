package com.rit.sucy.text;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Handles measuring the size of strings and squaring them up
 * Units for sizes are based off of the space between characters
 * (e.g. size 4 = the size of 4 of the spaces between characters)
 */
public class TextSizer {

    /**
     * Gets the size of the message
     *
     * @param message message to measure
     * @return        message size
     */
    public static int measureString(String message) {

        // Make sure the string isn't null
        if (message == null)
            throw new IllegalArgumentException("Invalid string - null");

        byte boldBonus = 0;
        boolean skip = false;

        int index = 0;
        int size = 0;
        for (char c : message.toCharArray()) {
            if (skip) {
                skip = false;
                continue;
            }

            // Chat colors don't add to the size, but bold adds 1 to all characters affected
            if (c == ChatColor.COLOR_CHAR) {
                skip = true;
                if (message.length() > index + 1) {
                    if (message.charAt(index + 1) == 'l')
                        boldBonus = 1;
                    else if (message.charAt(index + 1) == 'r')
                        boldBonus = 0;
                }
            }

            // Add the size of each character other than what's used for chat colors
            else {
                if (lengths.containsKey(c)) size += lengths.get(c) + boldBonus;
                else size += 6 + boldBonus;
            }
        }

        return size;
    }

    /**
     * Expands a string to meet the desired size
     *
     * @param message message to expand
     * @param size    desired size
     * @param front   whether or not to add to the front of the string
     * @return        the resulting message
     * @throws IllegalArgumentException when the string is either too large or just one pixel too small
     */
    public static String expand(String message, int size, boolean front) {

        // Make sure the message isn't null
        if (message == null)
            throw new IllegalArgumentException("Invalid string - null");

        // Get the length of the message
        int currentSize = measureString(message);

        // Already the correct size
        if (currentSize == size) return message;

        // Too large of a string
        if (currentSize > size)
            throw new IllegalArgumentException("Invalid string - larger than desired size");

        // Can't match the size when it is one pixel away
        if (currentSize == size - 1)
            throw new IllegalArgumentException("Invalid string - one pixel off and unable to match desired size");

        // Expand the string
        if (front) return expandFront(message, currentSize, size);
        else return expandBack(message, currentSize, size);
    }

    /**
     * Expands the messages so that they are all the same size
     * Note: this adds a space to the end of the longest string to ensure it can square them all
     *
     * @param messages messages to expand
     * @param front    whether or not to add to the front
     * @return         list of expanded strings
     */
    public static List<String> expand(List<String> messages, boolean front) {

        // Don't worry about empty lists
        if (messages.size() == 0)
            return messages;

        // Get the maximum size
        int maxSize = 0;
        for (String message : messages) {
            int size = measureString(message);
            if (size > maxSize) maxSize = size;
        }

        // Add a space to make sure it doesn't break
        maxSize += 4;

        // Expand each of the strings
        ArrayList<String> result = new ArrayList<String>();
        for (String message : messages) {
            result.add(expand(message, maxSize, front));
        }

        return result;
    }

    /**
     * Expands a string by adding to the end of the string
     *
     * @param message     message to expand
     * @param currentSize current size of the message
     * @param size        desired size of the message
     * @return            expanded message
     */
    private static String expandBack(String message, int currentSize, int size) {

        while (currentSize < size - 3 && currentSize != size - 5) {
            message += ' ';
            currentSize += 4;
        }
        if (currentSize < size - 2) {
            message += ChatColor.BLACK + "'";
            currentSize += 3;
        }
        if (currentSize < size - 1) {
            message += ChatColor.BLACK + "`";
        }

        return message + ChatColor.RESET;
    }

    /**
     * Expands a string by adding to the front of the string
     *
     * @param message     message to expand
     * @param currentSize current size of the message
     * @param size        desired size of the message
     * @return            expanded message
     */
    private static String expandFront(String message, int currentSize, int size) {

        while (currentSize < size - 3 && currentSize != size - 5) {
            message = " " + message;
            currentSize += 4;
        }
        if ((size - currentSize) % 2 == 1) {
            message = ChatColor.BLACK + "'" + ChatColor.RESET + message;
            currentSize += 3;
        }
        if ((size - currentSize) % 4 == 2) {
            message = ChatColor.BLACK + "`" + ChatColor.RESET + message;
            currentSize += 2;
        }

        return message;
    }

    /**
     * The lengths for each character
     */
    private static final HashMap<Character, Byte> lengths = new HashMap<Character, Byte>() {{

        // Length 6 + 1
        put('~', (byte)7);
        put('@', (byte)7);

        // All not-included characters are size 5 + 1

        // Length 4 + 1
        put('f', (byte)5);
        put('k', (byte)5);
        put('"', (byte)5);
        put('<', (byte)5);
        put('>', (byte)5);
        put('{', (byte)5);
        put('}', (byte)5);
        put('(', (byte)5);
        put(')', (byte)5);
        put('*', (byte)5);

        // length 3 + 1
        put('I', (byte)4);
        put('t', (byte)4);
        put(' ', (byte)4);
        put('[', (byte)4);
        put(']', (byte)4);

        // length 2 + 1
        put('l', (byte)3);
        put('\'', (byte)3);

        // length 1 + 1
        put('|', (byte)2);
        put('.', (byte)2);
        put(';', (byte)2);
        put(':', (byte)2);
        put('!', (byte)2);
        put('`', (byte)2);
        put(',', (byte)2);
        put('i', (byte)2);
    }};
}
