/**
 * MCCore
 * com.rit.sucy.reflect.Particle
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Steven Sucy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software") to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.rit.sucy.reflect;

import com.rit.sucy.version.VersionManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * <p>A utility class for playing particle effects using reflection to
 * allow for particles not normally supported by Bukkit.</p>
 */
public class Particle
{

    private static Class<?> packetClass;
    private static Class<?> particleEnum;
    private static boolean initialized = false;

    private static void initialize()
    {
        initialized = true;

        // Try to get the packet instance for 1.6.4 and earlier
        particleEnum = Reflection.getNMSClass("EnumParticle");
        packetClass = Reflection.getNMSClass("Packet63WorldParticles");

        // Otherwise get the instance for 1.7.2 and later
        if (packetClass == null)
        {
            packetClass = Reflection.getNMSClass("PacketPlayOutWorldParticles");
        }
    }

    /**
     * Checks whether or not the reflection particles are supported for this server
     *
     * @return true if supported, false otherwise
     */
    public static boolean isSupported()
    {
        if (!initialized)
        {
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
    public static void play(ParticleType particle, Location loc, int radius)
    {
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
    public static void play(ParticleType particle, Location loc, int radius, float speed)
    {
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
    public static void playBlockCrack(Material mat, short data, Location loc, int radius)
    {
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
    public static void playBlockCrack(Material mat, short data, Location loc, int radius, float speed)
    {
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
        if (VersionManager.isVersionAtLeast(VersionManager.V1_8_0))
            play("blockcrack_", loc, radius, 0, 0, 0, speed, 1, new int[] { mat, data });
        else
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
    public static void playIconCrack(Material mat, short data, Location loc, int radius)
    {
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
    public static void playIconCrack(Material mat, short data, Location loc, int radius, float speed)
    {
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
    public static void playIconCrack(int mat, short data, Location loc, int radius, float speed)
    {
        if (VersionManager.isVersionAtLeast(VersionManager.V1_8_0))
            play("iconcrack_", loc, radius, 0, 0, 0, speed, 1, new int[] { mat, data });
        else
            play("iconcrack_" + mat + "_" + data, loc, radius, speed);
    }

    /**
     * Sends a block dust particle for the material to all players within
     * a radius of the location
     *
     * @param mat    material to show the icon crack of
     * @param data   data of the material to show the icon crack of
     * @param loc    location to play at
     * @param radius radius of the effect
     */
    public static void playBlockDust(Material mat, short data, Location loc, int radius)
    {
        if (mat == null) return;
        playBlockDust(mat.getId(), data, loc, radius, 1.0f);
    }

    /**
     * Sends a block dust particle for the material to all players within
     * a radius of the location
     *
     * @param mat    material to show the icon crack of
     * @param data   data of the material to show the icon crack of
     * @param loc    location to play at
     * @param radius radius of the effect
     * @param speed  the speed of the particle
     */
    public static void playBlockDust(Material mat, short data, Location loc, int radius, float speed)
    {
        if (mat == null) return;
        playBlockDust(mat.getId(), data, loc, radius, speed);
    }

    /**
     * Sends a block dust particle for the material to all players within
     * a radius of the location
     *
     * @param mat    ID of the material to show the icon crack of
     * @param data   data of the material to show the icon crack of
     * @param loc    location to play at
     * @param radius radius of the effect
     */
    public static void playBlockDust(int mat, short data, Location loc, int radius)
    {
        playBlockDust(mat, data, loc, radius, 1.0f);
    }

    /**
     * Sends a block dust particle for the material to all players within
     * a radius of the location
     *
     * @param mat    ID of the material to show the icon crack of
     * @param data   data of the material to show the icon crack of
     * @param loc    location to play at
     * @param radius radius of the effect
     * @param speed  speed of the particle
     */
    public static void playBlockDust(int mat, short data, Location loc, int radius, float speed)
    {
        if (VersionManager.isVersionAtLeast(VersionManager.V1_8_0))
            play("blockdust_", loc, radius, 0, 0, 0, speed, 1, new int[] { mat, data });
        else
            play("blockdust_" + mat + "_" + data, loc, radius, speed);
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
    public static void play(String particle, Location loc, int radius, float dx, float dy, float dz, float speed, int count)
    {
        play(particle, loc, radius, dx, dy, dz, speed, count, new int[0]);
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
     * @param extra    extra data for 1.8+
     */
    public static void play(String particle, Location loc, int radius, float dx, float dy, float dz, float speed, int count, int[] extra)
    {
        if (!initialized)
        {
            initialize();
        }
        if (packetClass == null)
        {
            return;
        }
        if (VersionManager.isVersionAtLeast(VersionManager.V1_8_0))
        {
            if (CONVERSION.containsKey(particle))
            {
                particle = CONVERSION.get(particle);
            }
            else particle = particle.toUpperCase().replace(" ", "_");
            Object[] values = particleEnum.getEnumConstants();
            Object enumValue = null;
            for (Object value : values)
            {
                if (value.toString().equals(particle))
                {
                    enumValue = value;
                }
            }
            if (enumValue != null)
            {
                try
                {
                    Object packet = packetClass.getConstructor(particleEnum, Boolean.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Integer.TYPE, int[].class)
                            .newInstance(enumValue, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), dx, dy, dz, speed, count, extra);

                    for (Player player : VersionManager.getOnlinePlayers())
                    {
                        if (player.getWorld() == loc.getWorld() && player.getLocation().distanceSquared(loc) < radius * radius)
                        {
                            Reflection.sendPacket(player, packet);
                        }
                    }
                }
                catch (Exception ex)
                {
                    // Do nothing
                }
            }
        }
        else
        {
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
            for (Player player : VersionManager.getOnlinePlayers())
            {
                if (player.getWorld() == loc.getWorld() && player.getLocation().distanceSquared(loc) < radius * radius)
                {
                    Reflection.sendPacket(player, packet);
                }
            }
        }
    }

    private static void play(String particle, Location loc, int radius)
    {
        play(particle, loc, radius, 1.0f);
    }

    private static void play(String particle, Location loc, int radius, float speed)
    {
        play(particle, loc, radius, 0, 0, 0, speed, 0);
    }

    public static final HashMap<String, String> CONVERSION = new HashMap<String, String>()
    {{
            put("angryVillager", "VILLAGER_ANGRY");
            put("bubble", "WATER_BUBBLE");
            put("blockcrack_", "BLOCK_CRACK");
            put("blockdust_", "BLOCK_DUST");
            put("cloud", "CLOUD");
            put("crit", "CRIT");
            put("depthSuspend", "SUSPENDED_DEPTH");
            put("dripLava", "DRIP_LAVA");
            put("dripWater", "DRIP_WATER");
            put("enchantmenttable", "ENCHANTMENT_TABLE");
            put("explode", "EXPLOSION_NORMAL");
            put("fireworksSpark", "FIREWORKS_SPARK");
            put("flame", "FLAME");
            put("footstep", "FOOTSTEP");
            put("happyVillager", "VILLAGER_HAPPY");
            put("heart", "HEART");
            put("hugeexplosion", "EXPLOSION_HUGE");
            put("iconcrack_", "ITEM_CRACK");
            put("instantSpell", "SPELL_INSTANT");
            put("largeexplode", "EXPLOSION_LARGE");
            put("largesmoke", "SMOKE_LARGE");
            put("lava", "LAVA");
            put("magicCrit", "CRIT_MAGIC");
            put("mobSpell", "SPELL_MOB");
            put("mobSpellAmbient", "SPELL_MOB_AMBIENT");
            put("note", "NOTE");
            put("portal", "PORTAL");
            put("reddust", "REDSTONE");
            put("slime", "SLIME");
            put("snowballpoof", "SNOWBALL");
            put("snowshovel", "SNOW_SHOVEL");
            put("spell", "SPELL");
            put("splash", "WATER_SPLASH");
            put("suspend", "SUSPENDED");
            put("townaura", "TOWN_AURA");
            put("witchMagic", "SPELL_WITCH");
        }};
}
