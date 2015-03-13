package com.rit.sucy.economy;

import org.bukkit.entity.Player;

/**
 * A player account - personal or a bank account
 */
public interface Account
{

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
