package com.rit.sucy.mobs;

import com.rit.sucy.text.TextFormatter;
import org.bukkit.entity.*;

import java.util.Hashtable;

/**
 * Contains tools for converting names of mobs
 */
public class MobManager {

    /**
     * Gets the simple name of a mob
     *
     * @param entity entity to get the name of
     * @return       basic name
     */
    public static String getName(LivingEntity entity) {
        String name = entity.getType().getName();

        if (table.contains(name.toLowerCase()))
            return table.get(name.toLowerCase());

        if (entity instanceof Skeleton) {
            if (((Skeleton) entity).getSkeletonType() == Skeleton.SkeletonType.WITHER)
                name = "wither_" + name;
        }

        return TextFormatter.format(name);
    }

    /**
     * Gets the vanilla entity name along with various attributes such as:
     * - Baby
     * - Sheep color
     * - Villager profession
     * - Slime size
     * - Wolf/Ocelot tamed status
     * - Zombie type
     *
     * @param entity entity to get the name of
     * @return       detailed name
     */
    public static String getDetailedName(LivingEntity entity) {
        String basic = getName(entity);
        if (entity instanceof Tameable)
            basic = ((Tameable)entity).isTamed() ? "Tamed " + basic : "Wild " + basic;
        else if (entity instanceof Slime) {
            switch (((Slime)entity).getSize()) {
                case 4:
                    basic = "Big " + basic;
                    break;
                case 2:
                    basic = "Small " + basic;
                    break;
                case 1:
                    basic = "Tiny " + basic;
                    break;
            }
        }
        else if (entity instanceof Sheep) {
            String color = ((Sheep) entity).getColor().name().toLowerCase();
            basic = TextFormatter.format(color) + " " + basic;
        }
        else if (entity instanceof Villager) {
            String profession = ((Villager) entity).getProfession().name();
            basic = TextFormatter.format(profession) + " " + basic;
        }
        else if (entity instanceof Zombie) {
            String extra = ((Zombie) entity).isBaby() ? "Baby " : "";
            extra += ((Zombie) entity).isVillager() ? "Villager " : "";
            basic = extra + basic;
        }

        if (entity instanceof Ageable) {
            basic = ((Ageable) entity).isAdult() ? basic : "Baby " + basic;
        }

        return basic;
    }

    private static final Hashtable<String, String> table = new Hashtable<String, String>(){{
        put("mushroom_cow", "Mooshroom");
        put("pig_zombie",   "Zombie Pigman");
        put("snowman",      "Snow Golem");
    }};
}
