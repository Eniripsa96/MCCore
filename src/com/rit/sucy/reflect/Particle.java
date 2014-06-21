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

    private static Object packet;

    private static void initialize() {

        // Try to get the packet instance for 1.6.4 and earlier
        packet = Reflection.getInstance(Reflection.getNMSClass("Packet63WorldParticles"));

        // Otherwise get the instance for 1.7.2 and later
        if (packet == null) {
            packet = Reflection.getInstance(Reflection.getNMSClass("PacketPlayOutWorldParticles"));
        }

        // Set common values
        Reflection.setValue(packet, "e", 0.0f);
        Reflection.setValue(packet, "f", 0.0f);
        Reflection.setValue(packet, "g", 0.0f);
        Reflection.setValue(packet, "h", 1.0f);
        Reflection.setValue(packet, "i", 1);
    }

    /**
     * Sends the particle to all players within a radius of the location
     *
     * @param particle type of particle to play
     * @param loc      location to play at
     * @param radius   radius of the effect
     */
    public static void play(ParticleType particle, Location loc, int radius) {
        play(particle.getPacketString(), loc, radius);
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
        play("blockcrack_" + mat.getId() + "_" + data, loc, radius);
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
    public static void playBlockCrack(int mat, short data, Location loc, int radius) {
        play("blockcrack_" + mat + "_" + data, loc, radius);
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
        play("iconcrack_" + mat.getId() + "_" + data, loc, radius);
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
    public static void playIconCrack(int mat, short data, Location loc, int radius) {
        play("iconcrack_" + mat + "_" + data, loc, radius);
    }

    private static void play(String particle, Location loc, int radius) {
        if (packet == null) {
            Bukkit.getLogger().severe("Tried to play a particle before the helper was initialized!");
            return;
        }
        Reflection.setValue(packet, "a", particle);
        Reflection.setValue(packet, "b", (float) loc.getX());
        Reflection.setValue(packet, "c", (float) loc.getY());
        Reflection.setValue(packet, "d", (float) loc.getZ());
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getWorld() == loc.getWorld() && player.getLocation().distanceSquared(loc) < radius * radius) {
                Reflection.sendPacket(player, packet);
            }
        }
    }
}
