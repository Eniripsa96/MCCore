package com.rit.sucy.version;

import com.rit.sucy.player.PlayerUUIDs;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

/**
 * <p>Manages compatibilities between different versions and provides
 * ways to detect what version is currently active.</p>
 */
public class VersionManager {

    public static int V1_5_2 = 10502;
    public static int V1_6_2 = 10602;
    public static int V1_6_4 = 10604;
    public static int V1_7_2 = 10702;
    public static int V1_7_5 = 10705;
    public static int V1_7_8 = 10708;
    public static int V1_7_9 = 10709;
    public static int V1_8_0 = 10800;

    /**
     * The build number for the first 1.5.2 version
     * @deprecated change to exact versions rather than ranges
     */
    public static int MC_1_5_2_MIN = 10502;

    /**
     * The build number for the first 1.6.2 version
     * @deprecated change to exact versions rather than ranges
     */
    public static int MC_1_6_2_MIN = 10602;

    /**
     * The build number for the first 1.6.4 version
     * @deprecated change to exact versions rather than ranges
     */
    public static int MC_1_6_4_MIN = 10604;

    /**
     * The build number for the first 1.7.2 version
     * @deprecated change to exact versions rather than ranges
     */
    public static int MC_1_7_2_MIN = 10702;

    /**
     * The build number for the first 1.7.5 version
     * @deprecated change to exact versions rather than ranges
     */
    public static int MC_1_7_5_MIN = 10705;

    /**
     * The build number for the first 1.7.8 version
     * @deprecated change to exact versions rather than ranges
     */
    public static int MC_1_7_8_MIN = 10708;

    /**
     * The build number for the first 1.7.9 version
     * @deprecated change to exact versions rather than ranges
     */
    public static int MC_1_7_9_MIN = 10709;

    /**
     * The build number for the last 1.5.2 version
     * @deprecated change to exact versions rather than ranges
     */
    public static int MC_1_5_2_MAX = 10502;

    /**
     * The build number for the last 1.6.2 version
     * @deprecated change to exact versions rather than ranges
     */
    public static int MC_1_6_2_MAX = 10602;

    /**
     * The build number for the last 1.6.4 version
     * @deprecated change to exact versions rather than ranges
     */
    public static int MC_1_6_4_MAX = 10604;

    /**
     * The build number for the last 1.7.2 version
     * @deprecated change to exact versions rather than ranges
     */
    public static int MC_1_7_2_MAX = 10702;

    /**
     * The build number for the last 1.7.5 version
     * @deprecated change to exact versions rather than ranges
     */
    public static int MC_1_7_5_MAX = 10705;

    /**
     * The build number for the last 1.7.8 version
     * @deprecated change to exact versions rather than ranges
     */
    public static int MC_1_7_8_MAX = 10708;

    private static ServerType server;
    private static int version = -1;

    /**
     * <p>Initializes the version data</p>
     * <p>MCCore already calls this by default and
     * this is called in case it wasn't called before
     * data is started to be accessed so you shouldn't
     * ever need to call this method.</p>
     */
    public static void initialize(String vs) {
        try
        {
            int i = vs.indexOf("MC:") + 4;
            int j = vs.indexOf(")", i);
            String v = vs.substring(i, j);
            Bukkit.getLogger().info(v);
            String[] pieces = v.split("\\.");
            version = Integer.parseInt(pieces[0]) * 10000 + Integer.parseInt(pieces[1]) * 100 + Integer.parseInt(pieces[2]);
        }

        // Some error occurred, assume an up to date server with all features
        catch (Exception ex) {
            ex.printStackTrace();
            server = ServerType.UNKNOWN;
            version = 99999;
        }
        Bukkit.getLogger().info("Version: " + version);
    }

    /**
     * Checks whether or not UUIDs are used by the server
     *
     * @return true if used, false otherwise
     */
    public static boolean isUUID() {
        return isVersionAtLeast(V1_7_5);
    }

    /**
     * Checks whether or not damage dealt can be a double
     *
     * @return true if can be a double, false otherwise
     */
    public static boolean isDamageDouble() {
        return isVersionAtLeast(V1_6_2);
    }

    /**
     * Checks whether or not the tellraw command is active
     *
     * @return true if active, false otherwise
     */
    public static boolean isTellRaw() {
        return isVersionAtLeast(V1_7_9);
    }

    /**
     * <p>Checks whether or not the server's version is at most the
     * provided version.</p>
     * <p>You should be passing in one of the MAX constants from
     * this class (eg. VersionManager.MC_1_7_2_MAX)</p>
     * <p>Passing in MC_1_7_2_MAX would mean a server version
     * of 1.7.2 or earlier</p>
     *
     * @param v version to check
     * @return  true if the actual version is at least the provided one
     */
    public static boolean isVersionAtMost(int v) {
        return version <= v;
    }

    /**
     * <p>Checks whether or not the server's version is at least the
     * provided version.</p>
     * <p>You should be passing in one of the MIN constants from
     * this class (eg. VersionManager.MC_1_7_2_MIN)</p>
     * <p>Passing in MC_1_7_2_MIN would mean a server version of
     * 1.7.2 or later</p>
     *
     * @param v version to check
     * @return  true if the actual version is at most the provided one
     */
    public static boolean isVersionAtLeast(int v) {
        return version >= v;
    }

    /**
     * <p>Retrieves the kind of server that is being run.</p>
     *
     * @return the type of server that is running
     */
    public static ServerType getServerType() {
        return server;
    }

    /**
     * <p>Damages an entity the given amount while giving credit
     * to a damager</p>
     * <p>If the server version is 1.5.2 or earlier, this will
     * convert the damage to an integer since those versions
     * did not support double values for damage.</p>
     * <p>This also ignores invincibility ticks so that the damage
     * will go through regardless of whether or not the target
     * has been recently damaged</p>
     *
     * @param target  target to damage
     * @param damager entity dealing the damage
     * @param damage  damage to deal
     */
    public static void damage(LivingEntity target, LivingEntity damager, double damage) {

        // Allow damage to occur
        int ticks = target.getNoDamageTicks();
        target.setNoDamageTicks(0);

        // 1.5.2 and earlier used integer values
        if (isVersionAtMost(MC_1_5_2_MAX)) {
            target.damage((int)damage, damager);
        }

        // 1.6.2 and beyond use double values
        else target.damage(damage, damager);

        // Reset damage timer to before the damage was applied
        target.setNoDamageTicks(ticks);
    }

    /**
     * <p>Damages an entity the given amount</p>
     * <p>If the server version is 1.5.2 or earlier, this will
     * convert the damage to an integer since those versions
     * did not support double values for damage.</p>
     * <p>This also ignores invincibility ticks so that the damage
     * will go through regardless of whether or not the target
     * has been recently damaged</p>
     *
     * @param target  target to damage
     * @param damage  damage to deal
     */
    public static void damage(LivingEntity target, double damage) {

        // Allow damage to occur
        int ticks = target.getNoDamageTicks();
        target.setNoDamageTicks(0);

        // 1.5.2 and earlier used integer values
        if (isVersionAtMost(MC_1_5_2_MAX)) {
            target.damage((int)damage);
        }

        // 1.6.2 and beyond use double values
        else target.damage(damage);

        // Reset damage timer to before the damage was applied
        target.setNoDamageTicks(ticks);
    }

    /**
     * <p>Sets the maximum health of an entity</p>
     * <p>If the server version is 1.5.2 or earlier, this will
     * convert the health amount to an integer since those versions
     * did not support double values for health.</p>
     *
     * @param entity entity to set the health for
     * @param amount amount to set the max health to
     */
    public static void setMaxHealth(LivingEntity entity, double amount) {
        double prevMax = entity.getMaxHealth();
        double prevHealth = entity.getHealth();

        // 1.5.2 and earlier used integer values
        if (isVersionAtMost(MC_1_5_2_MAX)) {
            entity.setMaxHealth((int) amount);
            entity.setHealth((int) Math.min(Math.max(1, prevHealth + amount - prevMax), amount));
        }

        // 1.6.2 and beyond use double values
        else {
            entity.setMaxHealth(amount);
            entity.setHealth(Math.min(Math.max(1, prevHealth + amount - prevMax), amount));
        }
    }

    /**
     * <p>Heals the target entity the given amount</p>
     * <p>If the server version is 1.5.2 or earlier, this will
     * convert the heal amount to an integer since those versions
     * did not support double values for health.</p>
     *
     * @param entity entity to heal
     * @param amount amount to heal
     */
    public static void heal(LivingEntity entity, double amount) {

        // Cannot go above the enemy health
        double health = entity.getHealth() + amount;
        health = Math.min(entity.getMaxHealth(), health);

        // 1.5.2 and earlier used integer values
        if (isVersionAtMost(MC_1_5_2_MAX)) {
            entity.setHealth((int)health);
        }

        // 1.6.2 and later use double values
        else entity.setHealth(health);
    }

    /**
     * <p>Sets the damage of the event to the given amount</p>
     * <p>If the server version is 1.5.2 or earlier, this will
     * convert the damage to an integer since those versions
     * did not support double values for damage.</p>
     *
     * @param event  event details
     * @param damage damage to set
     */
    public static void setDamage(EntityDamageEvent event, double damage) {

        // 1.5.2 and earlier used integer values
        if (isVersionAtMost(MC_1_5_2_MAX)) {
            event.setDamage((int)damage);
        }

        // 1.6.2 and later used double values
        else event.setDamage(damage);
    }

    /**
     * <p>Retrieves a player by name, converting it to a
     * UUID in case the server is at least version 1.7.5.</p>
     * <p>If the player is not online, this will return null.</p>
     * <p>This method doesn't require querying the Minecraft
     * server as it uses the PlayerUUIDs class so it is very
     * efficient for the later server versions.</p>
     *
     * @param name name of the player
     * @return     player or null if not online
     */
    public static Player getPlayer(String name) {
        if (isVersionAtLeast(MC_1_7_5_MIN)) {
            UUID id = PlayerUUIDs.getUUID(name);
            if (id == null) return null;
            else return Bukkit.getPlayer(id);
        }
        else return Bukkit.getPlayer(name);
    }

    /**
     * <p>Retrieves an offline player by name trying to be efficient by
     * version. If the player hasn't played since MCCore was
     * installed and it is a newer server, then this will query the Minecraft
     * server for the UUID which can cause lag. To avoid this, use
     * getOfflinePlayer(name, false)</p>
     *
     * @param name name of the player
     * @return     the offline player
     */
    public static OfflinePlayer getOfflinePlayer(String name) {
        return getOfflinePlayer(name, true);
    }

    /**
     * <p>Retrieves an offline player by name while trying to be efficient
     * by version.</p>
     * <p>If querying is allowed, the server is at least 1.7.5, and the
     * player has not played before, then this will query the Minecraft
     * server for the UUID which can cause lag.</p>
     * <p>If querying is not allowed, this will return null when the
     * player has not played before and the server version is at least
     * 1.7.5.</p>
     * <p>If you want to allow querying but avoid lag, you should
     * call this method on an asynchronous task so that it doesn't
     * stall the main game loop.</p>
     *
     * @param name       name of the player
     * @param allowQuery whether or not to allow server queries
     * @return           offline player or null if unable to query the player data
     */
    public static OfflinePlayer getOfflinePlayer(String name, boolean allowQuery) {
        if (isVersionAtLeast(MC_1_7_5_MIN)) {
            UUID id = PlayerUUIDs.getUUID(name);
            if (id == null) {
                if (allowQuery) return Bukkit.getOfflinePlayer(name);
                else return null;
            }
            else return Bukkit.getOfflinePlayer(id);
        }
        else return Bukkit.getOfflinePlayer(name);
    }
}
