package com.rit.sucy.chat;

import com.rit.sucy.MCCore;
import com.rit.sucy.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Hashtable;

/**
 * Main helper method for the chat resources including accessing
 * player data and sending messages to target groups
 */
public class Chat
{

    static Hashtable<String, ChatData> players = new Hashtable<String, ChatData>();

    /**
     * Retrieves the player with the given name
     *
     * @param playerName name of the player
     *
     * @return chat data associated with the player
     */
    public static ChatData getPlayerData(String playerName)
    {

        playerName = playerName.toLowerCase();

        // Initialize data if it doesn't exist
        if (!players.containsKey(playerName))
        {
            MCCore core = (MCCore) Bukkit.getPluginManager().getPlugin("MCCore");
            Config configFile = core.getConfigFile(core, "data");
            ChatData data = new ChatData(configFile.getConfig(), playerName);
            configFile.addSavable(data, playerName + ".");
            players.put(playerName, data);
        }

        return players.get(playerName);
    }

    /**
     * Unlocks the prefix for all players
     *
     * @param prefix the prefix to unlock
     * @param apply  whether or not to automatically apply it
     */
    public static void unlockPrefix(Prefix prefix, boolean apply)
    {
        for (ChatData data : players.values()) data.unlockPrefix(prefix, apply);
    }

    /**
     * Removes the prefix for all players
     *
     * @param pluginName name of the plugin with the prefix
     * @param prefix     the text of the prefix (with or without color)
     */
    public static void removePrefix(String pluginName, String prefix)
    {
        for (ChatData data : players.values()) data.removePrefix(pluginName, prefix);
    }

    /**
     * Clears the plugin prefix for all players
     *
     * @param pluginName name of the plugin with the prefix
     */
    public static void clearPluginPrefixes(String pluginName)
    {
        for (ChatData data : players.values()) data.clearPluginPrefix(pluginName);
    }

    /**
     * Sends a message to all players with the given permission
     *
     * @param permission permission required to get the message
     * @param message    message to send
     */
    public static void sendMessage(String permission, String message)
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (player.hasPermission(permission)) player.sendMessage(message);
        }
    }

    /**
     * Sends a message to all players within the defined cuboid
     *
     * @param point1  the first point
     * @param point2  the second point
     * @param message the message to be sent
     */
    public static void sendMessage(Location point1, Location point2, String message)
    {
        if (point1.getWorld() != point2.getWorld()) return;
        sendMessage(point1, point2.getBlockX() - point1.getBlockX(), point2.getBlockY() - point1.getBlockY(),
                    point2.getBlockZ() - point1.getBlockZ(), message);
    }

    /**
     * Sends a message to all players within the defined cuboid
     *
     * @param point   the initial point
     * @param width   width of the cuboid (x direction)
     * @param height  height of the cuboid (y direction)
     * @param depth   depth of the cuboid (z direction)
     * @param message message to be sent
     */
    public static void sendMessage(Location point, int width, int height, int depth, String message)
    {
        if (width < 0) point.setX(point.getX() + width);
        if (height < 0) point.setY(point.getY() + height);
        if (depth < 0) point.setZ(point.getZ() + depth);
        for (Player player : Bukkit.getOnlinePlayers())
        {
            Location loc = player.getLocation();
            if (loc.getX() >= point.getX() && loc.getY() >= point.getY() && loc.getZ() >= point.getZ()
                && loc.getX() <= point.getX() + Math.abs(width) && loc.getY() <= point.getY() + Math.abs(height)
                && loc.getZ() <= point.getZ() + Math.abs(depth))
                player.sendMessage(message);
        }
    }

    /**
     * Sends a message to all players within the defined sphere or cylinder
     *
     * @param center the center of the region
     * @param radius radius of the sphere/cylinder
     * @param sphere sphere if true, cylinder if false (cylinder contains all y within the defined circle)
     */
    public static void sendMessage(Location center, int radius, boolean sphere, String message)
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            Location loc = player.getLocation();
            if (!sphere) loc.setY(center.getY());
            if (loc.distanceSquared(center) < radius * radius) player.sendMessage(message);
        }
    }
}
