package com.rit.sucy.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Provides checks for protection plugins
 */
public class Protection {

    /**
     * Checks if a player can be PvPed
     *
     * @param attacker player attacking the other
     * @param target   player being attacked
     * @return         true if the attack is allowed
     */
    public static boolean canPVP(Player attacker, Player target) {
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(attacker, target, EntityDamageEvent.DamageCause.CUSTOM, 1);
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled() && event.getDamage() > 0;
    }
}
