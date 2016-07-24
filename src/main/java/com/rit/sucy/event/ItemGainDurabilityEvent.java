/**
 * MCCore
 * com.rit.sucy.event.ItemGainDurabilityEvent
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
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Event for when a player's item gains durability
 */
public class ItemGainDurabilityEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final ItemStack item;
    private boolean cancelled;
    private int amount;

    /**
     * Constructor
     *
     * @param player player with the item
     * @param item   item that lost durability
     */
    public ItemGainDurabilityEvent(Player player, ItemStack item, int amount) {
        this.player = player;
        this.item = item;
        this.amount = amount;
        cancelled = false;
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
     * @return amount of durability gained
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the amount of durability to gain
     *
     * @param amount amount of durability to lose
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Sets whether or not to cancel the event
     *
     * @param cancelled whether or not to cancel the event
     */
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Checks whether or not this event has been cancelled
     *
     * @return true if cancelled, false otherwise
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
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
