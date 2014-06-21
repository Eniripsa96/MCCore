package com.rit.sucy.reflect;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <p>Utility class for performing reflection operations. Only use
 * this class if you know what you're doing.</p>
 */
public class Reflection {

    private static String CRAFT;
    private static String NMS;
    private static Class<?> packetClass = getNMSClass("Packet");

    /**
     * Retrieves a class by name
     *
     * @param name name of the class including packages
     * @return     class object or null if invalid
     */
    public static Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        }
        catch (Exception ex) {
            return null;
        }
    }

    /**
     * Retrieves an NMS class by name
     *
     * @param name name of the class including packages
     * @return     class object or null if invalid
     */
    public static Class<?> getNMSClass(String name) {
        if (NMS == null) {
            String[] pieces = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",");
            if (pieces.length >= 4) {
                NMS = "net.minecraft.server." + pieces[3] + ".";
            }
            else {
                NMS = "net.minecraft.server.";
            }
        }
        return getClass(NMS + name);
    }

    /**
     * Retrieves a CraftBukkit class by name
     *
     * @param name name of the class including packages
     * @return     class object or null if invalid
     */
    public static Class<?> getCraftClass(String name) {
        if (CRAFT == null) {
            String[] pieces = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",");
            if (pieces.length >= 4) {
                CRAFT = "org.bukkit.craftbukkit." + pieces[3] + ".";
            }
            else {
                CRAFT = "org.bukkit.craftbukkit.";
            }
        }
        return getClass(CRAFT + name);
    }

    /**
     * Gets an instance of the class
     *
     * @param c    class to get an instance of
     * @param args constructor arguments
     * @return     instance of the class or null if unable to create the object
     */
    public static Object getInstance(Class<?> c, Object ... args) {
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
    public static void setValue(Object o, String fieldName, Object value) {
        try {
            Field field = o.getClass().getDeclaredField(fieldName);
            if (!field.isAccessible()) field.setAccessible(true);
            field.set(o, value);
        }
        catch (Exception ex) { /* Do Nothing */ }
    }

    /**
     * Tries to get a value from the object
     *
     * @param o         object reference
     * @param fieldName name of the field to retrieve the value from
     * @return          the value of the field or null if not found
     */
    public static Object getValue(Object o, String fieldName) {
        try {
            Field field = o.getClass().getDeclaredField(fieldName);
            if (!field.isAccessible()) field.setAccessible(true);
            return field.get(o);
        }
        catch (Exception ex) { /* Do nothing */ }
        return null;
    }

    /**
     * Tries to get a method from the object
     *
     * @param o          object reference
     * @param methodName name of the field to retrieve the value from
     * @return           the value of the field or null if not found
     */
    public static Method getMethod(Object o, String methodName) {
        try {
            Method method = o.getClass().getMethod(methodName);
            if (!method.isAccessible()) method.setAccessible(true);
            return method;
        }
        catch (Exception ex) { /* Do nothing */ }
        return null;
    }

    /**
     * Tries to send a packet to the player
     *
     * @param player player to send to
     * @param packet packet to send
     */
    public static void sendPacket(Player player, Object packet) {
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
