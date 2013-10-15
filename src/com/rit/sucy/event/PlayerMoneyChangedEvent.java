package com.rit.sucy.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * <p>Event for when someone gains or loses money</p>
 * <p>Note: This event is not automatically fired. It is to
 * be implemented by economy plugins.</p>
 *
 */
public class PlayerMoneyChangedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final double change;
    private final double balance;

    /**
     * Constructor
     *
     * @param player  player equipping an item
     * @param change  amount the balance changed by
     * @param balance new balance of the player
     */
    public PlayerMoneyChangedEvent(Player player, double change, double balance) {
        this.player = player;
        this.change = change;
        this.balance = balance;
    }

    /**
     * @return plyer that equipped the item
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return amount the player's balance changed by
     */
    public double getAmount() {
        return change;
    }

    /**
     * @return new balance of the player
     */
    public double getBalance() {
        return balance;
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
