package com.rit.sucy.enchant;

import org.bukkit.enchantments.Enchantment;

import java.util.Hashtable;
import java.util.Map;

/**
 * Provides tools for converting names of enchantments
 */
public class Enchants {

    /**
     * Gets the vanilla enchantment name from the bukkit name
     *
     * @param bukkitName bukkit enchantment name
     * @return           vanilla enchantment name
     */
    public static String getVanillaName(String bukkitName) {
        return table.get(bukkitName.toUpperCase());
    }

    /**
     * Gets the vanilla enchantment name for an enchantment
     *
     * @param enchant enchantment
     * @return        vanilla name of enchantment
     */
    public static String getVanillaName(Enchantment enchant) {
        return getVanillaName(enchant.getName());
    }

    /**
     * Gets the bukkit name of an enchantment from the vanilla name
     *
     * @param vanillaName vanilla enchantment name
     * @return            bukkit enchantment name
     */
    public static String getBukkitName(String vanillaName) {
        for (Map.Entry<String, String> entry : table.entrySet())
            if (entry.getValue().equalsIgnoreCase(vanillaName))
                return entry.getKey();
        return null;
    }

    private static final Hashtable<String, String> table = new Hashtable<java.lang.String, java.lang.String>(){{
        put("ARROW_INFINITE",           "Infinity");
        put("ARROW_FIRE",               "Flame");
        put("ARROW_KNOCKBACK",          "Punch");
        put("ARROW_DAMAGE",             "Power");
        put("DAMAGE_ALL",               "Sharpness");
        put("DAMAGE_ARTHROPODS",        "Bane of Arthropods");
        put("DAMAGE_UNDEAD",            "Smite");
        put("DIG_SPEED",                "Efficiency");
        put("DURABILITY",               "Unbreaking");
        put("FIRE_ASPECT",              "Fire Aspect");
        put("KNOCKBACK",                "Knockback");
        put("LOOT_BONUS_BLOCKS",        "Fortune");
        put("LOOT_BONUS_MOBS",          "Looting");
        put("OXYGEN",                   "Respiration");
        put("PROTECTION_ENVIRONMENTAL", "Protection");
        put("PROTECTION_EXPLOSIONS",    "Blast Protection");
        put("PROTECTION_FALL",          "Feather Falling");
        put("PROTECTION_FIRE",          "Fire Protection");
        put("PROTECTION_PROJECTILE",    "Projectile Protection");
        put("SILK_TOUCH",               "Silk Touch");
        put("THORNS",                   "Thorns");
        put("WATER_WORKER",             "Aqua Affinity");
    }};
}
