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
}
