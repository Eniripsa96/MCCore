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
