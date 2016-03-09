/**
 * MCCore
 * com.rit.sucy.config.Filter
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
package com.rit.sucy.config;

/**
 * <p>A collection of commonly used filters to avoid needing to instantiate them
 * repeatedly. To set the replacement, just do .setReplacement(String). This method
 * also returns the filter back so you can put it straight in the sendMessage
 * or getMessage calls.</p>
 * <p/>
 * <p>Note: you are not limited to this list. You can create your own filters using
 * just the CustomFilter constructor. This list is merely for slight optimizations
 * by avoiding creating filters constantly. You can make your own global filters
 * as well to make your own optimizations as well.</p>
 */
public class Filter
{

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
