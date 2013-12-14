package com.rit.sucy.log;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * <p>Manager for debug messages at different levels</p>
 * <p>You can leave in debug messages with this that players
 * can use to help you find bugs without having to always have
 * them active. Simply adding a config option and using that
 * for the debugging level can remove any unwanted messages</p>
 */
public class DebugManager {

    private JavaPlugin plugin;
    private int activeLevel;

    /**
     * Constructor
     *
     * @param activeLevel debugging message level
     */
    public DebugManager(JavaPlugin plugin, int activeLevel) {
        this.plugin = plugin;
        this.activeLevel = activeLevel;
    }

    /**
     * Sends an info message
     *
     * @param message message to send
     * @param level   debugging level of the message
     */
    public void info(String message, int level) {
        if (activeLevel >= level) {
            plugin.getLogger().info(message);
        }
    }

    /**
     * Sends a severe message
     *
     * @param message message to send
     * @param level   debugging level for the message
     */
    public void severe(String message, int level) {
        if (activeLevel >= level) {
            plugin.getLogger().severe(message);
        }
    }

    /**
     * Sends a warning message
     *
     * @param message message to send
     * @param level   debugging level for the message
     */
    public void warning(String message, int level) {
        if (activeLevel >= level) {
            plugin.getLogger().warning(message);
        }
    }
}
