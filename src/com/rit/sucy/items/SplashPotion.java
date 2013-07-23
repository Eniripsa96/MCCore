package com.rit.sucy.items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Bat;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

/**
 * Spawns thrown splash potions that break upon hitting the ground
 */
public class SplashPotion {

    /**
     * Spawns a splash potion from an entity
     *
     * @param type     potion type
     * @param level    potion level
     * @param extended whether or not the potion has an extended duration
     * @param source   entity to spawn from
     * @return         the potion that was spawned
     */
    public static ThrownPotion spawnPotion(PotionType type, int level, boolean extended, LivingEntity source) {

        // Create the splash potion
        Potion potion = new Potion(type, level);
        potion.setSplash(true);
        potion.setHasExtendedDuration(extended);

        // Create and item stack for it
        ItemStack itemStack = new ItemStack(Material.POTION);
        potion.apply(itemStack);

        // Spawn the potion
        ThrownPotion thrownPotion = source.launchProjectile(ThrownPotion.class);
        thrownPotion.setItem(itemStack);
        thrownPotion.setVelocity(new Vector(0, 0, 0));
        return thrownPotion;
    }

    /**
     * Drops a splash potion at a target location
     *
     * @param type     potion type
     * @param level    potion level
     * @param extended whether or not the potion has an extended duration
     * @param loc      target location
     * @return         the potion that was spawned
     */
    public static ThrownPotion spawnPotion(PotionType type, int level, boolean extended, Location loc) {
        Bat bat = loc.getWorld().spawn(loc, Bat.class);
        ThrownPotion potion = spawnPotion(type, level, extended, bat);
        bat.remove();
        return potion;
    }
}
