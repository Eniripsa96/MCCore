/**
 * MCCore
 * com.rit.sucy.event.PlayerMoneyChangedEvent
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Steven Sucy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software") to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
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

/**
 * <p>Event for when someone gains or loses money</p>
 * <p>Note: This event is not automatically fired. It is to
 * be implemented by economy plugins.</p>
 */
public class PlayerMoneyChangedEvent extends Event
{

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
    public PlayerMoneyChangedEvent(Player player, double change, double balance)
    {
        this.player = player;
        this.change = change;
        this.balance = balance;
    }

    /**
     * @return plyer that equipped the item
     */
    public Player getPlayer()
    {
        return player;
    }

    /**
     * @return amount the player's balance changed by
     */
    public double getAmount()
    {
        return change;
    }

    /**
     * @return new balance of the player
     */
    public double getBalance()
    {
        return balance;
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
