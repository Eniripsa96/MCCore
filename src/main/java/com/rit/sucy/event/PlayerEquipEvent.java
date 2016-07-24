/**
 * MCCore
 * com.rit.sucy.event.PlayerEquipEvent
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
package com.rit.sucy.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Event for when a player equips an item
 */
public class PlayerEquipEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final ItemStack item;
    private final int slot;

    /**
     * Constructor
     *
     * @param player player equipping an item
     * @param item   item that was equipped
     * @param slot   the slot the item was unequipped from
     */
    public PlayerEquipEvent(Player player, ItemStack item, int slot) {
        this.player = player;
        this.item = item;
        this.slot = slot;
    }

    /**
     * @return plyer that equipped the item
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return item that was equipped
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * @return armor slot that was equipped
     */
    public int getSlot() {
        return slot;
    }

    /**
     * @return handlers for this event
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @return handlers for this event
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
