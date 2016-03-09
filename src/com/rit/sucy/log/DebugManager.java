/**
 * MCCore
 * com.rit.sucy.log.DebugManager
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
package com.rit.sucy.log;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * <p>Manager for debug messages at different levels</p>
 * <p>You can leave in debug messages with this that players
 * can use to help you find bugs without having to always have
 * them active. Simply adding a config option and using that
 * for the debugging level can remove any unwanted messages</p>
 */
public class DebugManager
{

    private JavaPlugin plugin;
    private int        activeLevel;

    /**
     * Constructor
     *
     * @param activeLevel debugging message level
     */
    public DebugManager(JavaPlugin plugin, int activeLevel)
    {
        this.plugin = plugin;
        this.activeLevel = activeLevel;
    }

    /**
     * Sends an info message
     *
     * @param message message to send
     * @param level   debugging level of the message
     */
    public void info(String message, int level)
    {
        if (activeLevel >= level)
        {
            plugin.getLogger().info(message);
        }
    }

    /**
     * Sends a severe message
     *
     * @param message message to send
     * @param level   debugging level for the message
     */
    public void severe(String message, int level)
    {
        if (activeLevel >= level)
        {
            plugin.getLogger().severe(message);
        }
    }

    /**
     * Sends a warning message
     *
     * @param message message to send
     * @param level   debugging level for the message
     */
    public void warning(String message, int level)
    {
        if (activeLevel >= level)
        {
            plugin.getLogger().warning(message);
        }
    }
}
