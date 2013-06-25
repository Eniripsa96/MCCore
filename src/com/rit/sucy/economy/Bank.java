package com.rit.sucy.economy;

/**
 * A bank containing members and optionally having interest rates
 */
public interface Bank extends Account {

    /**
     * Gets an account of a member of the bank
     *
     * @param name member name
     * @return     member account
     */
    public Account getAccount(String name);

    /**
     * Checks if the player is a member of the bank
     *
     * @param name player name
     * @return     true if a member, false otherwise
     */
    public boolean hasAccount(String name);

    /**
     * Creates a new member account for the bank
     *
     * @param name  member name
     * @param funds initial funds
     * @return      the new account
     */
    public Account createAccount(String name, double funds);

    /**
     * Removes an account from the bank
     *
     * @param name   account to delete
     * @param absorb whether or not to add the money to the bank
     * @return       the removed account
     */
    public Account removeAccount(String name, boolean absorb);

    /**
     * Transfers an account to the target bank
     *
     * @param name account name
     * @param bank bank to transfer to
     * @return     the transferred account
     */
    public Account transferAccount(String name, Bank bank);

    /**
     * @return true if the bank supports interest, false otherwise
     */
    public boolean supportsInterest();

    /**
     * @return the rate of interest on loans as a percentage
     */
    public double getLoanInterestRate();

    /**
     * @return the rate of interest on deposits as a percentage
     */
    public double getDepositInterestRate();

    /**
     * Sets the interest on loans for the bank
     *
     * @param rate new rate
     */
    public void setLoanInterestRate(double rate);

    /**
     * Sets the interest on deposits for the bank
     *
     * @param rate new rate
     */
    public void setDepositInterestRate(double rate);
}
