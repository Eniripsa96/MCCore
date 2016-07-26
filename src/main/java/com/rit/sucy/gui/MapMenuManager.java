/**
 * MCCore
 * com.rit.sucy.gui.MapMenuManager
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
package com.rit.sucy.gui;

import com.rit.sucy.MCCore;
import com.rit.sucy.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.util.HashMap;

/**
 * Handles setting up
 */
public class MapMenuManager {
    private static HashMap<String, MapData> data = new HashMap<String, MapData>();
    private static HashMap<Short, MapData> idMap = new HashMap<Short, MapData>();
    private static Config config;

    /**q
     * Creates a map item for the given map menu
     *
     * @param menuKey key of the menu the map should show
     */
    public static MapData getData(String menuKey) {
        return data.get(menuKey);
    }

    /**
     * Creates a new menu view using the given menu as the root.
     * The root menu cannot backtrack to any other menus, only
     * progress into child menus.
     *
     * @param key  key to use for the menu group
     * @param root root of the menu chain
     */
    public static void registerMenu(String key, MapMenu root) {
        // Duplicate keys are not allowed
        if (data.containsKey(key)) {
            Bukkit.getLogger().severe("Duplicate map menu key: " + key);
            return;
        }

        init();

        // Load the MapView for the
        MapView view = null;
        int id = config.getConfig().getInt(key, -1);
        if (id >= 0) {
            view = Bukkit.getMap((short) id);
            if (view == null) id = -1;
        }
        if (id == -1) {
            view = Bukkit.createMap(Bukkit.getWorlds().get(0));
        }
        if (view == null) return;
        config.getConfig().set(key, view.getId());
        config.saveConfig();

        // Add the data
        MapData mapData = new MapData(root, view);
        data.put(key, mapData);
        idMap.put(view.getId(), mapData);
    }

    /**
     * Retrieves the map menu a player is looking at, if any.
     * When a player is not holding a map menu, this will
     * instead return null.
     *
     * @param player player to get the menu for
     * @return active map menu or null if not found
     */
    public static MapData getActiveMenuData(Player player) {
        if (player == null || idMap.size() == 0) return null;
        ItemStack map = player.getItemInHand();
        if (map != null && map.getType() == Material.MAP) {
            short id = map.getDurability();
            if (idMap.containsKey(id)) {
                return idMap.get(id);
            }
        }
        return null;
    }

    /**
     * Sends a player back one level in their current menu chain.
     *
     * @param player player to send back to the previous menu
     */
    public static void sendBack(Player player) {
        MapData data = getActiveMenuData(player);
        if (data != null) {
            data.back(player);
        }
    }

    /**
     * Sends a player forward one level to the given menu. If the player
     * is not currently at the parent menu of the new one, this will
     * not do anything. You must follow the chain of menus for
     * a given hierarchy.
     *
     * @param player player to send back to the previous menu
     */
    public static void sendNext(Player player, MapMenu menu) {
        MapData data = getActiveMenuData(player);
        if (data != null) {
            data.next(player, menu);
        }
    }

    /**
     * Initializes needed data if not already initialized
     */
    private static void init() {
        if (config == null) {
            config = new Config((MCCore) Bukkit.getPluginManager().getPlugin("MCCore"), "maps");
        }
    }
}
