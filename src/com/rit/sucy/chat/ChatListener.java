package com.rit.sucy.chat;

import com.rit.sucy.MCCore;
import com.rit.sucy.items.SplashPotion;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;

/**
 * Listens for players joining/quitting to load and clear data appropriately
 */
public class ChatListener implements Listener {

    /**
     * Constructor
     *
     * @param plugin plugin hosting this listener
     */
    public ChatListener(MCCore plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Loads player data when they join
     *
     * @param event event details
     */
    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        Chat.getPlayerData(event.getPlayer().getName());
    }

    /**
     * Clears player data when they quit
     *
     * @param event event details
     */
    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {
        Chat.players.remove(event.getPlayer().getName());
    }
}
