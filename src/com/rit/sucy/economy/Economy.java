/**
 * MCCore
 * com.rit.sucy.economy.Economy
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
package com.rit.sucy.economy;

/**
 * Manager for accessing banks and player accounts
 */
public interface Economy
{

    /**
     * @return true if the economy supports multiple worlds, false otherwise
     */
    public boolean supportsMultiWorld();

    /**
     * @return true if the economy supports banks, false otherwise
     */
    public boolean supportsBanks();

    /**
     * Retrieves the bank with the given name
     *
     * @param name bank name
     *
     * @return the bank
     */
    public Bank getBank(String name);

    /**
     * Retrieves a bank for a given world
     *
     * @param name  bank name
     * @param world world name
     *
     * @return the bane
     */
    public Bank getBank(String name, String world);

    /**
     * Checks if the bank exists
     *
     * @param name bank name
     *
     * @return true if exists, false otherwise
     */
    public boolean hasBank(String name);

    /**
     * Checks if the bank exists in the given world
     *
     * @param name  bank name
     * @param world world name
     *
     * @return true if exists, false otherwise
     */
    public boolean hasBank(String name, String world);

    /**
     * Creates a new bank
     *
     * @param name  bank name
     * @param funds initial funds
     *
     * @return the bank
     */
    public Bank createBank(String name, double funds);

    /**
     * Creates a bank in a world
     *
     * @param name  bank name
     * @param world world name
     * @param funds initial funds
     *
     * @return the bank
     */
    public Bank createBank(String name, String world, double funds);

    /**
     * Deletes a bank
     *
     * @param name bank name
     */
    public void deleteBank(String name);

    /**
     * Deletes a bank in a specific world
     *
     * @param name  bank name
     * @param world world name
     */
    public void deleteBank(String name, String world);

    /**
     * Retrieves a player account
     *
     * @param name player name
     *
     * @return player account
     */
    public Account getAccount(String name);

    /**
     * Retrieves a player account for a specific world
     *
     * @param name  player name
     * @param world world name
     *
     * @return player account
     */
    public Account getAccount(String name, String world);

    /**
     * Checks if the player account exists
     *
     * @param name player name
     *
     * @return true if exists, false otherwise
     */
    public boolean hasAccount(String name);

    /**
     * Checks if the player account exists in the given world
     *
     * @param name  player name
     * @param world world name
     *
     * @return true if exists, false otherwise
     */
    public boolean hasAccount(String name, String world);

    /**
     * Creates a new player account
     *
     * @param name  player name
     * @param funds initial funds
     *
     * @return player account
     */
    public Account createAccount(String name, double funds);

    /**
     * Creates a new player account in a world
     *
     * @param name  player name
     * @param world world name
     * @param funds initial funds
     *
     * @return player account
     */
    public Account createAccount(String name, String world, double funds);

    /**
     * Deletes a player account
     *
     * @param name player name
     */
    public void deleteAccount(String name);

    /**
     * Deletes a player account for a specific world
     *
     * @param name  player name
     * @param world world name
     */
    public void deleteAccount(String name, String world);

    /**
     * Formats the money into a string
     *
     * @param money money to format
     *
     * @return money string
     */
    public String format(double money);

    /**
     * Returns the currency name as a singular word
     *
     * @return singular form of currency name
     */
    public String getCurrencySingular();

    /**
     * Returns the currency name as a plural word
     *
     * @return plural form of currency name
     */
    public String getCurrencyPlural();
}
