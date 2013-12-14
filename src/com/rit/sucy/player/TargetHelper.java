package com.rit.sucy.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for finding the target of a player
 */
public class TargetHelper {

    /**
     * <p>Number of pixels that end up displaying about 1 degree of vision in the client window</p>
     * <p>Not really useful since you can't get the client's window size, but I added it in case
     * it becomes useful sometime</p>
     */
    private static final int PIXELS_PER_DEGREE = 35;

    /**
     * <p>Gets all entities the player is looking at within the range</p>
     * <p>Has a little bit of tolerance to make targeting easier</p>
     *
     * @param player player to check
     * @param range  maximum range to check
     * @return       all entities in the player's vision line
     */
    public static List<LivingEntity> getLivingTargets(Player player, int range) {
        List<Entity> list = player.getNearbyEntities(range, range, range);
        List<LivingEntity> targets = new ArrayList<LivingEntity>();

        Vector facing = player.getLocation().getDirection();
        double fLengthSq = facing.lengthSquared();

        for (Entity entity : list) {
            if (!isInFront(player, entity) || !(entity instanceof LivingEntity)) continue;

            Vector relative = entity.getLocation().subtract(player.getLocation()).toVector();
            double dot = relative.dot(facing);
            double rLengthSq = relative.lengthSquared();
            double cosSquared = (dot * dot) / (rLengthSq + fLengthSq);
            double sinSquared = 1 - cosSquared;

            relative = player.getLocation().subtract(entity.getLocation()).toVector();
            double dSquared = relative.lengthSquared() * sinSquared;

            // If close enough to vision line, return the entity
            if (dSquared < 4) targets.add((LivingEntity)entity);
        }

        return targets;
    }

    /**
     * <p>Gets the entity the player is looking at</p>
     * <p>Has a little bit of tolerance to make targeting easier</p>
     *
     * @param player player to check
     * @param range  maximum range to check
     * @return       entity player is looing at or null if not found
     */
    public static LivingEntity getLivingTarget(Player player, int range) {
        List<LivingEntity> targets = getLivingTargets(player, range);
        if (targets.size() == 0) return null;
        LivingEntity target = targets.get(0);
        double minDistance = target.getLocation().distanceSquared(player.getLocation());
        for (LivingEntity entity : targets) {
            double distance = entity.getLocation().distanceSquared(player.getLocation());
            if (distance < minDistance) {
                minDistance = distance;
                target = entity;
            }
        }
        return target;
    }

    /**
     * Checks if the entity is in front of the player
     *
     * @param player player to check for
     * @param target target to check against
     * @return       true if the target is in front of the player
     */
    public static boolean isInFront(Player player, Entity target) {

        // Get the necessary vectors
        Vector facing = player.getLocation().getDirection();
        Vector relative = target.getLocation().subtract(player.getLocation()).toVector();

        // If the dot product is positive, the target is in front
        return facing.dot(relative) >= 0;
    }

    /**
     * Checks if the entity is in front of the player restricted to the given angle
     *
     * @param player player to check for
     * @param target target to check against
     * @param angle  angle to restrict it to (0-360)
     * @return       true if the target is in front of the player
     */
    public static boolean isInFront(Player player, Entity target, double angle) {
        if (angle <= 0) return false;
        if (angle >= 360) return true;

        // Get the necessary data
        double dotTarget = Math.cos(angle);
        Vector facing = player.getLocation().getDirection().normalize();
        Vector relative = target.getLocation().subtract(player.getLocation()).toVector().normalize();

        // Compare the target dot product with the actual result
        return facing.dot(relative) >= dotTarget;
    }

    /**
     * Checks if the entity is behind the player
     *
     * @param player player to check for
     * @param target target to check against
     * @return       true if the target is behind the player
     */
    public static boolean isBehind(Player player, Entity target) {
        return !isInFront(player, target);
    }

    /**
     * Checks if the entity is behind the player restricted to the given angle
     *
     * @param player player to check for
     * @param target target to check against
     * @param angle  angle to restrict it to (0-360)
     * @return       true if the target is behind the player
     */
    public static boolean isBehind(Player player, Entity target, double angle) {
        if (angle <= 0) return false;
        if (angle >= 360) return true;

        // Get the necessary data
        double dotTarget = Math.cos(angle);
        Vector facing = player.getLocation().getDirection().normalize();
        Vector relative = player.getLocation().subtract(target.getLocation()).toVector().normalize();

        // Compare the target dot product and the actual result
        return facing.dot(relative) >= dotTarget;
    }
}
