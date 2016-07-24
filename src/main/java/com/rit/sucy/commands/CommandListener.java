/**
 * MCCore
 * com.rit.sucy.commands.CommandListener
 * <p/>
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2014 Steven Sucy
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software") to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.rit.sucy.commands;

import com.rit.sucy.MCCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.ServerCommandEvent;

/**
 * <p>Listener for cleaning up configurable commands
 * automatically for other plugins when they are
 * disabled.</p>
 */
public class CommandListener implements Listener {

    /**
     * Creates the listener for configurable commands
     *
     * @param plugin plugin reference
     */
    public CommandListener(MCCore plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * <p>Unregisters commands for plugins when they are disabled</p>
     *
     * @param event event details
     */
    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        CommandManager.unregisterCommands(event.getPlugin());
    }


    /**
     * Handles player commands
     *
     * @param event event details
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (CommandManager.isInvalidRegistration()) {
            String[] args = event.getMessage().contains(" ") ? event.getMessage().split(" ") : new String[]{event.getMessage()};
            if (args[0].startsWith("/")) args[0] = args[0].substring(1);
            ConfigurableCommand cmd = CommandManager.getCommand(args[0]);
            if (cmd != null) {
                args = CommandManager.trimArgs(args);
                cmd.execute(event.getPlayer(), args);
                event.setCancelled(true);
            }
        }
    }

    /**
     * Handles server commands
     *
     * @param event event details
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommand(ServerCommandEvent event) {
        if (CommandManager.isInvalidRegistration()) {
            String[] args = event.getCommand().contains(" ") ? event.getCommand().split(" ") : new String[]{event.getCommand()};
            if (args[0].startsWith("/")) args[0] = args[0].substring(1);
            ConfigurableCommand cmd = CommandManager.getCommand(args[0]);
            if (cmd != null) {
                args = CommandManager.trimArgs(args);
                cmd.execute(event.getSender(), args);
            }
        }
    }
}
