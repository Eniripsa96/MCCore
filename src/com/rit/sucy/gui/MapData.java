package com.rit.sucy.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.util.HashMap;

/**
 * Represents the data for a map hierarchy. These should only
 * be set up by the MapMenuManager. Use that class to set up
 * and manage menus instead.
 */
public class MapData extends MapRenderer
{
    private HashMap<String, MapMenu> current = new HashMap<String, MapMenu>();

    private MapBuffer buffer;
    private MapMenu   root;
    private MapView   view;
    private ItemStack map;
    private boolean   firstPass = true;

    /**
     * Initializes a new renderer and data set for a map view using
     * the given menu as the root of the hierarchy.
     *
     * @param root root map menu
     * @param view view to set up renderer for
     */
    public MapData(MapMenu root, MapView view)
    {
        this.root = root;
        this.view = view;
        this.buffer = new MapBuffer(view);
        this.map = new ItemStack(Material.MAP, 1, view.getId());

        for (MapRenderer r : view.getRenderers())
        {
            view.removeRenderer(r);
        }
        view.addRenderer(this);
    }

    /**
     * Renders to the map canvas each frame
     *
     * @param mapView   the map view owning the canvas
     * @param mapCanvas the canvas to draw to
     * @param player    the player looking at the map
     */
    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player)
    {
        // Ignore if not holding this map
        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() != Material.MAP || item.getDurability() != view.getId())
        {
            return;
        }

        // Enable instant drawing rather than over time
        if (firstPass)
        {
            firstPass = false;
            player.sendMap(mapView);
            return;
        }
        firstPass = true;

        // Draw the menu
        getMenu(player).render(buffer, player);

        // Draw to the canvas
        buffer.drawTo(mapCanvas);
    }

    /**
     * Retrieves the map ItemStack representing this menu
     * hierarchy.
     *
     * @return map ItemStack for the menu
     */
    public ItemStack getMapItem()
    {
        return map;
    }

    /**
     * Retrieves the current menu being seen by a specific player
     *
     * @param player player to retrieve the current map for
     * @return the current map the player is viewing
     */
    public MapMenu getMenu(Player player)
    {
        if (!current.containsKey(player.getName())) {
            current.put(player.getName(), root);
            setup(root, player);
            return root;
        }
        return current.get(player.getName());
    }

    /***
     * Returns a player back to the previous map menu in the
     * hierarchy if they are not already at the root level.
     *
     * @param player player to return back to the previous menu
     */
    public void back(Player player)
    {
        if (!current.containsKey(player.getName())) return;

        MapMenu c = current.get(player.getName());
        if (c == root) return;
        current.put(player.getName(), c.getParent());
        setup(c.getParent(), player);
    }

    /**
     * Sends a player to the next menu in the hierarchy if
     * they are currently at the parent menu.
     *
     * @param menu menu to transition to
     */
    public void next(Player player, MapMenu menu)
    {
        if (getMenu(player) == menu.getParent())
        {
            current.put(player.getName(), menu);
            setup(menu, player);
        }
    }

    /**
     * Sets up the player for the given menu
     *
     * @param menu   menu to set up for
     * @param player player to set up
     */
    private void setup(MapMenu menu, Player player)
    {
        MapMenu.setSelection(player, 0);
        MapMenu.getScene(player).clear();
        menu.setup(player);
    }

    /**
     * Removes the menu data for a player
     *
     * @param player player to remove for
     */
    public void clear(Player player)
    {
        current.remove(player.getName());
    }
}