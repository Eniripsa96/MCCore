package com.rit.sucy.items;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;

/**
 * Manages creating inventories and checking if the inventory is
 * for a particular purpose using keys attached to the inventory holder.
 * An example usage of this would be a custom shop inventory where
 * functionality is attached to that specific inventory. Creating the
 * inventory with a key, then checking on events for the matching key
 * lets you easily apply functionality to only the shop inventories.
 */
public class InventoryManager
{
    private static final HashMap<String, Holder> holders = new HashMap<String, Holder>();

    /**
     * Creates a new inventory marked with the given key
     *
     * @param key   key to mark the inventory with
     * @param rows  number of rows in the inventory
     * @param title title for the inventory
     *
     * @return the created inventory
     */
    public static Inventory createInventory(String key, int rows, String title)
    {
        if (!holders.containsKey(key))
        {
            holders.put(key, new Holder(key));
        }

        return Bukkit.createInventory(holders.get(key), rows * 9, title);
    }

    /**
     * Checks whether the given inventory matches the given key
     *
     * @param inventory inventory to check
     * @param key       key to check against
     *
     * @return true if matches, false otherwise
     */
    public static boolean isMatching(Inventory inventory, String key)
    {
        return inventory.getHolder() instanceof Holder && ((Holder) inventory.getHolder()).getKey().equals(key);
    }
}

class Holder implements InventoryHolder
{
    private String key;

    public Holder(String key)
    {
        this.key = key;
    }

    public String getKey()
    {
        return key;
    }

    @Override
    public Inventory getInventory()
    {
        return null;
    }
}
