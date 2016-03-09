/**
 * MCCore
 * com.rit.sucy.config.LanguageConfig
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
package com.rit.sucy.config;

import com.rit.sucy.text.TextFormatter;
import com.rit.sucy.text.TextSizer;
import com.rit.sucy.version.VersionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>A special kind of configuration for handling language files</p>
 * <p>Provides methods for sending and retrieving messages from the configuration
 * for both players and the console using various kinds of filters.</p>
 * <p>The language configuration works off of the defaults you include in the
 * file when you build your plugin. At least one default is required to
 * instantiate this class.</p>
 */
public class LanguageConfig extends Config
{

    private static final String  EXPAND_FONT_REGEX = "\\{expandFront\\((.+),([0-9]+),([0-9]+)\\)\\}";
    private static final Pattern EXPAND_FRONT      = Pattern.compile(EXPAND_FONT_REGEX);
    private static final String  EXPAND_BACK_REGEX = "\\{expandBack\\((.+),([0-9]+),([0-9]+)\\)\\}";
    private static final Pattern EXPAND_BACK       = Pattern.compile(EXPAND_BACK_REGEX);

    /**
     * <p>Constructs a language config from the defaults in the file</p>
     * <p>If no default exists, an exception will be thrown because no
     * messages would be available.</p>
     *
     * @param plugin plugin reference
     * @param file   path to the language file
     */
    public LanguageConfig(JavaPlugin plugin, String file)
    {
        super(plugin, file);

        // The configuration must have a default section
        ConfigurationSection config = getConfig();
        if (config.getDefaultSection() == null)
            throw new IllegalArgumentException("Invalid language configuration - no defaults found");

        // Update the config, making sure all defaults
        // // are present and trimming incorrect values
        saveDefaultConfig();
        trim();
        checkDefaults();
    }

    /**
     * <p>Gets a message without any filters</p>
     * <p>You can use this method instead of the sendMessage methods
     * if you want to manipulate the string outside of the provided
     * filters before sending the message.</p>
     *
     * @param key key for the language message
     *
     * @return unfiltered message or null if an invalid key
     */
    public List<String> getMessage(String key)
    {
        return getMessage(key, false, FilterType.NONE);
    }

    /**
     * <p>Gets a message using the provided filters</p>
     * <p>You can use this method instead of the sendMessage methods
     * if you want to manipulate the string outside of the provided
     * filters before sending the message.</p>
     * <p>If the key is not in the language configuration, null will be returned</p>
     *
     * @param key        key for the language message
     * @param player     whether or not it is for a player
     * @param filterType type of built-in filter to use
     * @param filters    custom filters to use
     *
     * @return filtered message or null if an invalid key
     */
    public List<String> getMessage(String key, boolean player, FilterType filterType, CustomFilter... filters)
    {

        List<String> lines;
        if (!getConfig().contains(key)) return null;
        else if (getConfig().isList(key)) lines = getConfig().getStringList(key);
        else
        {
            lines = new ArrayList<String>();
            lines.add(getConfig().getString(key));
        }
        List<String> result = new ArrayList<String>();

        // Filter each line
        for (String line : lines)
        {
            StringBuilder sb = new StringBuilder(line);

            // Apply custom filters
            for (CustomFilter filter : filters)
            {
                filter.apply(sb);
            }

            // Filter colors
            if (filterType == FilterType.COLOR || filterType == FilterType.ALL)
            {
                TextFormatter.colorString(sb);
            }

            // Filter specials
            if (filterType == FilterType.SPECIAL || filterType == FilterType.ALL)
            {
                filterSizer(sb, true, player);
                filterSizer(sb, true, player);
                filterBreak(sb);
            }

            sb.append(ChatColor.RESET);

            result.add(sb.toString());
        }

        return result;
    }

    /**
     * Applies the size filter
     *
     * @param sb     message to filter
     * @param front  true if apply front sizer, false for back sizer
     * @param player whether or not it is for a player
     */
    private void filterSizer(StringBuilder sb, boolean front, boolean player)
    {
        Pattern regex = front ? EXPAND_FRONT : EXPAND_BACK;
        Matcher match = regex.matcher(sb);
        int size = sb.length();
        while (match.find())
        {
            int playerSize = Integer.parseInt(match.group(2));
            int consoleSize = Integer.parseInt(match.group(3));
            String string = match.group(1);
            if (player)
            {
                sb.replace(match.start() + sb.length() - size, match.end(),
                           (TextSizer.measureString(string) > playerSize - 2 ? string : TextSizer.expand(string, playerSize, front)));
            }
            else
            {
                sb.replace(match.start() + sb.length() - size, match.end(),
                           (string.length() > consoleSize ? string : TextSizer.expandConsole(string, consoleSize, front)));
            }
        }
    }

    /**
     * Applies the break filter
     *
     * @param sb message to filter
     */
    private void filterBreak(StringBuilder sb)
    {
        int index = sb.indexOf("{break}");
        if (index >= 0)
        {
            sb.delete(index, index + 7);
            String without = sb.toString();
            int size = TextSizer.measureString(without);
            for (int i = 0; i < (320 - size) / 6; i++)
            {
                sb.insert(index, '-');
            }
        }
    }

    /**
     * Sends a message without any filters
     *
     * @param key    key for the language message
     * @param target recipient of the message
     */
    public void sendMessage(String key, CommandSender target)
    {
        sendMessage(key, target, FilterType.NONE);
    }

    /**
     * Sends a message using the provided filters
     *
     * @param key        key for the language message
     * @param target     recipient of the message
     * @param filterType type of built-in filter to use
     * @param filters    custom filters to use
     */
    public void sendMessage(String key, CommandSender target, FilterType filterType, CustomFilter... filters)
    {
        List<String> lines = getMessage(key, target instanceof Player, filterType, filters);
        for (String line : lines)
        {
            target.sendMessage(line);
        }
    }

    /**
     * Sends a message to an area without any filters
     *
     * @param key    key for the language message
     * @param loc    location to send the message from
     * @param radius radius to send the message across
     */
    public void sendMessage(String key, Location loc, double radius)
    {
        sendMessage(key, loc, radius, FilterType.NONE);
    }

    /**
     * Sends a message to an area using the provided filters
     *
     * @param key        key for the language message
     * @param loc        location to send the message from
     * @param radius     radius to send the message across
     * @param filterType type of built-in filter to use
     * @param filters    custom filters to use
     */
    public void sendMessage(String key, Location loc, double radius, FilterType filterType, CustomFilter... filters)
    {
        radius *= radius;
        List<String> lines = getMessage(key, true, filterType, filters);
        for (Player player : loc.getWorld().getPlayers())
        {
            if (player.getLocation().distanceSquared(loc) < radius)
            {
                for (String line : lines)
                {
                    player.sendMessage(line);
                }
            }
        }
    }

    /**
     * Sends a message without any filters to a list of players
     * represented by their IDs
     *
     * @param key       key for the language message
     * @param targetIds ids of the recipients of the message
     */
    public void sendMessage(String key, Collection<UUID> targetIds)
    {
        sendMessage(key, targetIds, FilterType.NONE);
    }

    /**
     * Sends a message using the provided filters to a list of players
     * represented by their IDs
     *
     * @param key        key for the language message
     * @param targetIds  ids of the recipients of the message
     * @param filterType type of built-in filter to use
     * @param filters    custom filters to use
     */
    public void sendMessage(String key, Collection<UUID> targetIds, FilterType filterType, CustomFilter... filters)
    {
        List<String> lines = getMessage(key, true, filterType, filters);
        if (lines == null || !VersionManager.isUUID()) return;
        for (UUID id : targetIds)
        {
            Player target = Bukkit.getPlayer(id);
            if (target != null)
            {
                for (String line : lines)
                {
                    target.sendMessage(line);
                }
            }
        }
    }
}
