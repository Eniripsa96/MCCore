package com.rit.sucy.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class Protection {

    public static boolean canPVP(Player attacker, Player target) {
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(attacker, target, EntityDamageEvent.DamageCause.CUSTOM, 1);
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled() && event.getDamage() > 0;
    }
}
