/**
 * MCCore
 * com.rit.sucy.gui.MapListener
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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

/**
 * Handles controls events for map menus
 */
public class MapListener implements Listener {
    private static final Vector UP = new Vector(0, 1, 0);
    private static final Vector ZERO = new Vector(0, 0, 0);

    /* at */ long last = 0;

    /**
     * Sets up the map listener. Other plugins should
     * not initialize this.
     *
     * @param mcCore MCCore reference
     */
    public MapListener(MCCore mcCore) {
        mcCore.getServer().getPluginManager().registerEvents(this, mcCore);
    }

    /**
     * Movement controls in a map menu
     *
     * @param event event details
     */
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        // If not holding onto a map, ignore it
        MapData data = MapMenuManager.getActiveMenuData(event.getPlayer());
        if (data == null) return;

        // If not moving and only turning, ignore it
        Vector moving = event.getTo().clone().subtract(event.getFrom()).toVector();
        moving.setY(0);
        if (moving.lengthSquared() < 1e-8) return;

        // Prevent movement so they don't fall off cliffs or something
        Location loc = new Location(
                event.getFrom().getWorld(),
                event.getFrom().getX(),
                event.getTo().getY(),
                event.getFrom().getZ(),
                event.getTo().getYaw(),
                event.getTo().getPitch()
        );
        event.getPlayer().teleport(loc);

        moving.normalize();

        // Ignore repeated updates, only want when they press down
        if (System.currentTimeMillis() - last < 300) {
            last = System.currentTimeMillis();
            return;
        }
        last = System.currentTimeMillis();

        // Get the actual menu for the player
        MapMenu menu = data.getMenu(event.getPlayer());

        // Get the facing direction again ignoring y-direction
        Vector facing = event.getTo().getDirection();
        facing.setY(0);
        facing.normalize();

        // Dot product tells us the direction
        double dot = moving.dot(facing);

        // Forwards is a value of 1, so get close to that for up
        if (dot > 0.5) menu.onUp(event.getPlayer());

            // Backwards is -1, so close to that will be down
        else if (dot < -0.5) menu.onDown(event.getPlayer());

            // Otherwise, check left and right
        else {

            // Change the forward to face to the right
            facing.crossProduct(UP);
            dot = moving.dot(facing);

            // Positive would face to the right since that's the new forward
            if (dot > 0) menu.onRight(event.getPlayer());

                // Otherwise it was left
            else menu.onLeft(event.getPlayer());
        }
    }

    /**
     * Handles selecting the current item in the menu
     *
     * @param event event details
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        // If not holding onto a map, ignore it
        MapData data = MapMenuManager.getActiveMenuData(event.getPlayer());
        if (data == null) return;

        // Left clicking selects
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            event.setCancelled(true);
            data.getMenu(event.getPlayer()).onSelect(event.getPlayer());
        }
    }

    /**
     * Handles selecting the current item in the menu
     *
     * @param event event details
     */
    @EventHandler
    public void onInteract(PlayerToggleSneakEvent event) {
        // If not holding onto a map, ignore it
        MapData data = MapMenuManager.getActiveMenuData(event.getPlayer());
        if (data == null) return;

        // Starting to sneak goes back
        if (event.isSneaking()) {
            data.getMenu(event.getPlayer()).onBack(event.getPlayer());
            data.back(event.getPlayer());
        }
    }

    /**
     * Clears player menu data on quit
     *
     * @param event event details
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        MapData data = MapMenuManager.getActiveMenuData(event.getPlayer());
        if (data != null) {
            MapMenu menu = data.getMenu(event.getPlayer());
            menu.onExit(event.getPlayer());
            data.clear(event.getPlayer());
        }
    }
}
