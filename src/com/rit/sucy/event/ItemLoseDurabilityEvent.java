package com.rit.sucy.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Event for when a player's item loses durability
 */
public class ItemLoseDurabilityEvent extends Event implements Cancellable
{

    private static final HandlerList handlers = new HandlerList();

    private final Player    player;
    private final ItemStack item;
    private       boolean   cancelled;
    private       int       amount;

    /**
     * Constructor
     *
     * @param player player with the item
     * @param item   item that lost durability
     */
    public ItemLoseDurabilityEvent(Player player, ItemStack item, int amount)
    {
        this.player = player;
        this.item = item;
        this.amount = amount;
        cancelled = false;
    }

    /**
     * @return plyer that equipped the item
     */
    public Player getPlayer()
    {
        return player;
    }

    /**
     * @return item that was equipped
     */
    public ItemStack getItem()
    {
        return item;
    }

    /**
     * @return amount of durability lost
     */
    public int getAmount()
    {
        return amount;
    }

    /**
     * Sets the amount of durability to lose
     *
     * @param amount amount of durability to lose
     */
    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    /**
     * Sets whether or not to cancel the event
     *
     * @param cancelled whether or not to cancel the event
     */
    @Override
    public void setCancelled(boolean cancelled)
    {
        this.cancelled = cancelled;
    }

    /**
     * Checks whether or not this event has been cancelled
     *
     * @return true if cancelled, false otherwise
     */
    @Override
    public boolean isCancelled()
    {
        return cancelled;
    }

    /**
     * @return handlers for this event
     */
    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    /**
     * @return handlers for this event
     */
    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}
