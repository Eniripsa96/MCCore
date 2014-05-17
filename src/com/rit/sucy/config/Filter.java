package com.rit.sucy.config;

/**
 * <p>A collection of commonly used filters to avoid needing to instantiate them
 * repeatedly. To set the replacement, just do .setReplacement(String). This method
 * also returns the filter back so you can put it straight in the sendMessage
 * or getMessage calls.</p>
 *
 * <p>Note: you are not limited to this list. You can create your own filters using
 * just the CustomFilter constructor. This list is merely for slight optimizations
 * by avoiding creating filters constantly. You can make your own global filters
 * as well to make your own optimizations as well.</p>
 */
public class Filter {

    /**
     * <p>Filter for player names</p>
     * <p>Token: {player}</p>
     */
    public static final CustomFilter PLAYER = new CustomFilter("{player}", "unknown");

    /**
     * <p>Filter for a target's name</p>
     * <p>Token: {target}</p>
     */
    public static final CustomFilter TARGET = new CustomFilter("{target}", "unknown");

    /**
     * <p>Filter for a description</p>
     * <p>Token: {description}</p>
     */
    public static final CustomFilter DESCRIPTION = new CustomFilter("{description}", "no description");

    /**
     * <p>Filter for a generic message</p>
     * <p>Token: {message}</p>
     */
    public static final CustomFilter MESSAGE = new CustomFilter("{message}", "no message");

    /**
     * <p>Filter for a numerical amount</p>
     * <p>Token: {amount}</p>
     */
    public static final CustomFilter AMOUNT = new CustomFilter("{amount}", "0");

    /**
     * <p>Filter for a generic value</p>
     * <p>Token: {value}</p>
     */
    public static final CustomFilter VALUE = new CustomFilter("{value}", "0");
}
