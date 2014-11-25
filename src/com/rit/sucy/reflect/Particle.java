package com.rit.sucy.reflect;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * <p>A utility class for playing particle effects using reflection to
 * allow for particles not normally supported by Bukkit.</p>
 */
public class Particle {

    private static Class<?> packetClass;
    private static boolean initialized = false;

    private static void initialize() {

        initialized = true;

        // Try to get the packet instance for 1.6.4 and earlier
        packetClass = Reflection.getNMSClass("Packet63WorldParticles");

        // Otherwise get the instance for 1.7.2 and later
        if (packetClass == null) {
            packetClass = Reflection.getNMSClass("PacketPlayOutWorldParticles");
        }
    }

    /**
     * Checks whether or not the reflection particles are supported for this server
     *
     * @return true if supported, false otherwise
     */
    public boolean isSupported() {
        if (!initialized) {
            initialize();
        }
        return packetClass != null;
    }

    /**
     * Sends the particle to all players within a radius of the location
     *
     * @param particle type of particle to play
     * @param loc      location to play at
     * @param radius   radius of the effect
     */
    public static void play(ParticleType particle, Location loc, int radius) {
        play(particle.getPacketString(), loc, radius, 1.0f);
    }

    /**
     * Sends the particle to all players within a radius of the location
     *
     * @param particle type of particle to play
     * @param loc      location to play at
     * @param radius   radius of the effect
     * @param speed    speed of the particle
     */
    public static void play(ParticleType particle, Location loc, int radius, float speed) {
        play(particle.getPacketString(), loc, radius, speed);
    }

    /**
     * Sends a block crack particle for the material to all players within
     * a radius of the location
     *
     * @param mat    material to show the block crack of
     * @param data   data of the material to show the block crack of
     * @param loc    location to play at
     * @param radius radius of the effect
     */
    public static void playBlockCrack(Material mat, short data, Location loc, int radius) {
        if (mat == null) return;
        playBlockCrack(mat.getId(), data, loc, radius, 1.0f);
    }

    /**
     * Sends a block crack particle for the material to all players within
     * a radius of the location
     *
     * @param mat    material to show the block crack of
     * @param data   data of the material to show the block crack of
     * @param loc    location to play at
     * @param radius radius of the effect
     * @param speed  speed of the particle
     */
    public static void playBlockCrack(Material mat, short data, Location loc, int radius, float speed) {
        if (mat == null) return;
        playBlockCrack(mat.getId(), data, loc, radius, speed);
    }

    /**
     * Sends a block crack particle for the material to all players within
     * a radius of the location
     *
     * @param mat    ID of the material to show the block crack of
     * @param data   data of the material to show the block crack of
     * @param loc    location to play at
     * @param radius radius of the effect
     */
    public static void playBlockCrack(int mat, short data, Location loc, int radius)
    {
        playBlockCrack(mat, data, loc, radius, 1.0f);
    }

    /**
     * Sends a block crack particle for the material to all players within
     * a radius of the location
     *
     * @param mat    ID of the material to show the block crack of
     * @param data   data of the material to show the block crack of
     * @param loc    location to play at
     * @param radius radius of the effect
     * @param speed  speed of the particle
     */
    public static void playBlockCrack(int mat, short data, Location loc, int radius, float speed)
    {
        play("blockcrack_" + mat + "_" + data, loc, radius, speed);
    }

    /**
     * Sends a icon crack particle for the material to all players within
     * a radius of the location
     *
     * @param mat    material to show the icon crack of
     * @param data   data of the material to show the icon crack of
     * @param loc    location to play at
     * @param radius radius of the effect
     */
    public static void playIconCrack(Material mat, short data, Location loc, int radius) {
        if (mat == null) return;
        playIconCrack(mat.getId(), data, loc, radius, 1.0f);
    }

    /**
     * Sends a icon crack particle for the material to all players within
     * a radius of the location
     *
     * @param mat    material to show the icon crack of
     * @param data   data of the material to show the icon crack of
     * @param loc    location to play at
     * @param radius radius of the effect
     * @param speed  the speed of the particle
     */
    public static void playIconCrack(Material mat, short data, Location loc, int radius, float speed) {
        if (mat == null) return;
        playIconCrack(mat.getId(), data, loc, radius, speed);
    }

    /**
     * Sends a icon crack particle for the material to all players within
     * a radius of the location
     *
     * @param mat    ID of the material to show the icon crack of
     * @param data   data of the material to show the icon crack of
     * @param loc    location to play at
     * @param radius radius of the effect
     */
    public static void playIconCrack(int mat, short data, Location loc, int radius)
    {
        playIconCrack(mat, data, loc, radius, 1.0f);
    }

    /**
     * Sends a icon crack particle for the material to all players within
     * a radius of the location
     *
     * @param mat    ID of the material to show the icon crack of
     * @param data   data of the material to show the icon crack of
     * @param loc    location to play at
     * @param radius radius of the effect
     * @param speed  speed of the particle
     */
    public static void playIconCrack(int mat, short data, Location loc, int radius, float speed){

        play("iconcrack_" + mat + "_" + data, loc, radius, speed);
    }

    /**
     * Sends a particle packet using the given string. This should only be used
     * when you know what you are doing. Otherwise, use the other helper methods
     * that provide valid particle strings and settings.
     *
     * @param particle packet string of the particle
     * @param loc      location to play the particle at
     * @param radius   radius in which to show the effect
     * @param dx       particle x range
     * @param dy       particle y range
     * @param dz       particle z range
     * @param speed    particle speed
     * @param count    number of particles
     */
    public static void play(String particle, Location loc, int radius, float dx, float dy, float dz, float speed, int count) {
        if (!initialized) {
            initialize();
        }
        if (packetClass == null) {
            return;
        }
        Object packet = Reflection.getInstance(packetClass);

        Reflection.setValue(packet, "a", particle);
        Reflection.setValue(packet, "b", (float) loc.getX());
        Reflection.setValue(packet, "c", (float) loc.getY());
        Reflection.setValue(packet, "d", (float) loc.getZ());
        Reflection.setValue(packet, "e", dx);
        Reflection.setValue(packet, "f", dy);
        Reflection.setValue(packet, "g", dz);
        Reflection.setValue(packet, "h", speed);
        Reflection.setValue(packet, "i", count);
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getWorld() == loc.getWorld() && player.getLocation().distanceSquared(loc) < radius * radius) {
                Reflection.sendPacket(player, packet);
            }
        }
    }

    private static void play(String particle, Location loc, int radius)
    {
        play(particle, loc, radius, 1.0f);
    }

    private static void play(String particle, Location loc, int radius, float speed) {
        play(particle, loc, radius, 0, 0, 0, speed, 0);
    }
}
