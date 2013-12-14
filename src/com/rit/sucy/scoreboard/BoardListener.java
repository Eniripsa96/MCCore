package com.rit.sucy.scoreboard;

import com.rit.sucy.player.TargetHelper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.lang.annotation.Target;

/**
 * Listener for players quitting
 */
public class BoardListener implements Listener {

    /**
     * Constructor
     *
     * @param plugin plugin reference
     */
    public BoardListener(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Clear data on quit
     *
     * @param event event details
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        BoardManager.clearPlayer(event.getPlayer().getName());
    }

    /**
     * Clear data on kick
     *
     * @param event event details
     */
    @EventHandler
    public void onKick(PlayerKickEvent event) {
        BoardManager.clearPlayer(event.getPlayer().getName());
    }
}
