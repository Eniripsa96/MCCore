/**
 * MCCore
 * com.rit.sucy.scoreboard.Board
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
package com.rit.sucy.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/**
 * A manager for a scoreboard
 */
public abstract class Board {

    /**
     * Scoreboard controlled by this board
     */
    protected final Scoreboard scoreboard;

    /**
     * Sidebar objective to manage
     */
    protected final Objective obj;

    /**
     * Name of plugin
     */
    protected final String plugin;

    /**
     * Constructs a new scoreboard manager
     *
     * @param title title for the scoreboard
     */
    public Board(String title, String plugin) {
        this(title, "dummy", plugin);
    }

    /**
     * Constructs a new scoreboard manager with a desired type
     *
     * @param title title for the scoreboard
     * @param type  type of the scoreboard
     */
    public Board(String title, String type, String plugin) {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        obj = scoreboard.registerNewObjective(title, type);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.plugin = plugin;
    }

    /**
     * Gets the name of the scoreboard
     *
     * @return scoreboard name
     */
    public String getName() {
        return obj.getName();
    }

    /**
     * @return scoreboard that this boad manages
     */
    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    /**
     * Shows a player the scoreboard
     *
     * @param player player to show
     */
    public void showPlayer(Player player) {
        if (player != null)
            player.setScoreboard(scoreboard);
    }

    /**
     * Sets the health objective for the scoreboard
     * - Recommended not to use this method -
     * - Use PlayerBoard or BoardManager instead -
     *
     * @param label scoreboard label
     */
    public void setHealthLabel(String label) {
        Objective obj = scoreboard.getObjective(DisplaySlot.BELOW_NAME) != null ?
                scoreboard.getObjective(DisplaySlot.BELOW_NAME)
                : scoreboard.registerNewObjective("hpBelowName", "health");
        if (obj.getDisplaySlot() != DisplaySlot.BELOW_NAME) obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
        obj.setDisplayName(label);
    }
}
