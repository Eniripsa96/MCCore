/**
 * MCCore
 * com.rit.sucy.items.InventoryManager
 * <p/>
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2014 Steven Sucy
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software") to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
public class InventoryManager {
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
    public static Inventory createInventory(String key, int rows, String title) {
        if (!holders.containsKey(key)) {
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
    public static boolean isMatching(Inventory inventory, String key) {
        return inventory.getHolder() instanceof Holder && ((Holder) inventory.getHolder()).getKey().equals(key);
    }
}

class Holder implements InventoryHolder {
    private String key;

    public Holder(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
