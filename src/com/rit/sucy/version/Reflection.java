package com.rit.sucy.version;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * <p>A utility method for using reflection. If you don't know
 * about reflection, you probably shouldn't be using this class.</p>
 */
public class Reflection {

    private static final String VERSION = "net.minecraft.server."
            + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";

    /**
     * Retrieves a class by name
     *
     * @param name name of the class
     * @return     class object or null if invalid
     */
    private static Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        }
        catch (Exception ex) {
            return null;
        }
    }

    /**
     * <p>Retrieves a NMS class by name</p>
     * <p>This handles getting the correct package for the
     * NMS classes due to Bukkit changing the packages constantly.</p>
     *
     * @param name name of the NMS class
     * @return     class object or null if invalid
     */
    private static Class<?> getNMSClass(String name) {
        try {
            return Class.forName(VERSION + name);
        }
        catch (Exception ex) {
            return null;
        }
    }

    /**
     * Gets an instance of the class
     *
     * @param c    class to get an instance of
     * @param args constructor arguments
     * @return     instance of the class or null if unable to create the object
     */
    private static Object getInstance(Class<?> c, Object ... args) {
        if (c == null) return null;
        try {
            for (Constructor<?> constructor : c.getDeclaredConstructors()) {
                if (constructor.getGenericParameterTypes().length == args.length) {
                    return constructor.newInstance(args);
                }
            }
        }
        catch (Exception ex) { /* */ }
        return null;
    }

    /**
     * Tries to set a value for the object
     *
     * @param o         object reference
     * @param fieldName name of the field to set
     * @param value     value to set
     */
    private static void setValue(Object o, String fieldName, Object value) {
        try {
            Field field = o.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(o, value);
        }
        catch (Exception ex) { /* Do Nothing */ }
    }

    /**
     * Tries to send a packet to the player
     *
     * @param player      player to send to
     * @param packet      packet to send
     * @param packetClass class of the packet to send
     */
    private static void sendPacket(Player player, Object packet, Class<?> packetClass) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = handle.getClass().getField("playerConnection").get(handle);
            connection.getClass().getMethod("sendPacket", packetClass).invoke(connection, packet);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
