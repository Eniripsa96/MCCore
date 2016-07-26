/**
 * MCCore
 * com.rit.sucy.economy.Account
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
package com.rit.sucy.economy;

import org.bukkit.entity.Player;

/**
 * A player account - personal or a bank account
 */
public interface Account {

    /**
     * @return money in this account
     */
    public double getBalance();

    /**
     * Checks if the account has at least the given amount
     *
     * @param amount amount
     *
     * @return true if the account has it, false otherwise
     */
    public boolean has(double amount);

    /**
     * @param amount withdraws money from the account
     *
     * @return amount of money left in the account
     */
    public double withdraw(double amount);

    /**
     * @param amount adds money to the account
     *
     * @return amount of money left in the account
     */
    public double deposit(double amount);

    /**
     * Transfers money into the target player account
     *
     * @param account target account
     * @param amount  amount to transfer
     *
     * @return remaining balance
     */
    public double transfer(Account account, double amount);

    /**
     * Sets the amount of money in the account
     *
     * @param amount new amount
     */
    public void setBalance(double amount);

    /**
     * @return name of the player who owns the account
     */
    public String getOwnerName();

    /**
     * @return the player who owns the account
     */
    public Player getOwner();
}
