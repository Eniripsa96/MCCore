package com.rit.sucy.economy;

/**
 * Manager for accessing banks and player accounts
 */
public interface Economy {

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
     * @return     the bank
     */
    public Bank getBank(String name);

    /**
     * Retrieves a bank for a given world
     *
     * @param name  bank name
     * @param world world name
     * @return      the bane
     */
    public Bank getBank(String name, String world);

    /**
     * Checks if the bank exists
     *
     * @param name bank name
     * @return     true if exists, false otherwise
     */
    public boolean hasBank(String name);

    /**
     * Checks if the bank exists in the given world
     *
     * @param name  bank name
     * @param world world name
     * @return      true if exists, false otherwise
     */
    public boolean hasBank(String name, String world);

    /**
     * Creates a new bank
     *
     * @param name  bank name
     * @param funds initial funds
     * @return      the bank
     */
    public Bank createBank(String name, double funds);

    /**
     * Creates a bank in a world
     *
     * @param name  bank name
     * @param world world name
     * @param funds initial funds
     * @return      the bank
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
     * @return     player account
     */
    public Account getAccount(String name);

    /**
     * Retrieves a player account for a specific world
     *
     * @param name  player name
     * @param world world name
     * @return      player account
     */
    public Account getAccount(String name, String world);

    /**
     * Checks if the player account exists
     *
     * @param name player name
     * @return     true if exists, false otherwise
     */
    public boolean hasAccount(String name);

    /**
     * Checks if the player account exists in the given world
     *
     * @param name  player name
     * @param world world name
     * @return      true if exists, false otherwise
     */
    public boolean hasAccount(String name, String world);

    /**
     * Creates a new player account
     *
     * @param name  player name
     * @param funds initial funds
     * @return      player account
     */
    public Account createAccount(String name, double funds);

    /**
     * Creates a new player account in a world
     *
     * @param name  player name
     * @param world world name
     * @param funds initial funds
     * @return      player account
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
}
