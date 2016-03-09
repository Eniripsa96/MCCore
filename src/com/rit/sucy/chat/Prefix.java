/**
 * MCCore
 * com.rit.sucy.chat.Prefix
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
package com.rit.sucy.chat;

import org.bukkit.ChatColor;

/**
 * Unlockable Prefix Data
 */
public class Prefix
{

    ChatColor braceColor;
    String    pluginName;
    String    textWithoutColor;
    String    text;

    /**
     * Constructor for prefixes with white square braces
     *
     * @param pluginName name of the plugin that gives the prefix
     * @param prefix     prefix text (with colors)
     */
    public Prefix(String pluginName, String prefix)
    {
        this(pluginName, prefix, ChatColor.WHITE);
    }

    /**
     * Constructor for prefixes with colored square braces
     *
     * @param pluginName name of the plugin that gives the prefix
     * @param prefix     prefix text (with colors)
     * @param braceColor color of the braces containing the prefix
     */
    public Prefix(String pluginName, String prefix, ChatColor braceColor)
    {
        this.pluginName = pluginName;
        text = prefix;
        textWithoutColor = ChatColor.stripColor(text);
        this.braceColor = braceColor;
    }

    /**
     * Constructor for loading data from config
     *
     * @param data config data
     */
    Prefix(String data)
    {
        String[] values = data.split(",");
        pluginName = values[0];
        text = values[1];
        textWithoutColor = ChatColor.stripColor(text);
        braceColor = ChatColor.valueOf(values[2]);
    }

    /**
     * @return tag for the prefix
     */
    public String tag()
    {
        return braceColor + "[" + text + braceColor + "]";
    }

    /**
     * @return the name of the plugin that gives this prefix
     */
    public String pluginName()
    {
        return pluginName;
    }

    /**
     * @return the text of the prefix including colors
     */
    public String text()
    {
        return text;
    }

    /**
     * @return the text of the prefix without colors
     */
    public String textWithoutColor()
    {
        return textWithoutColor;
    }

    /**
     * Represents the prefix as a string for config saving
     *
     * @return prefix data represented as a string
     */
    @Override
    public String toString()
    {
        return pluginName + "," + text + "," + braceColor.name();
    }
}
