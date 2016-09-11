/**
 * MCCore
 * com.rit.sucy.scoreboard.PlayerBoards
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
package com.rit.sucy.scoreboard;

import com.rit.sucy.version.VersionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

/**
 * Scoreboard data for a player
 */
public class PlayerBoards
{
    public static final Scoreboard EMPTY = Bukkit.getScoreboardManager().getNewScoreboard();

    private final List<Board> boards = new ArrayList<Board>();

    private final String player;

    private Board  currentBoard;

    private int current = 0;

    /**
     * Whether or not the player's scoreboard is cycling
     */
    protected boolean cycling;

    /**
     * Constructor
     *
     * @param playerName name of the player
     */
    public PlayerBoards(String playerName)
    {
        this.player = playerName;
        cycling = true;
    }

    /**
     * @return owning player reference
     */
    public Player getPlayer()
    {
        return VersionManager.getPlayer(player);
    }

    /**
     * @return name of the owning player
     */
    public String getPlayerName()
    {
        return player;
    }

    /**
     * Adds a scoreboard for the player
     *
     * @param board board to add
     */
    public void addBoard(Board board)
    {
        if (getPlayer() == null)
            throw new IllegalStateException("Cannot add boards when no player is present");

        board.setPlayer(getPlayer());
        boards.add(board);
        if (currentBoard == null)
            showNextBoard();
    }

    /**
     * Removes a board from a player
     *
     * @param board board to remove
     */
    public void removeBoard(Board board)
    {
        if (!boards.contains(board))
            return;

        boards.remove(board);
        if (currentBoard == board)
        {
            clear();
            showNextBoard();
        }
    }

    /**
     * Removes all boards from a plugin
     *
     * @param plugin plugin name
     */
    public void removeBoards(String plugin)
    {
        for (int i = 0; i < boards.size(); i++)
            if (boards.get(i).plugin.equalsIgnoreCase(plugin))
                boards.remove(i);

        clear();
    }

    /**
     * Clears current board data
     */
    private void clear()
    {
        if (currentBoard != null)
            currentBoard.clearDisplay();
        current = -1;
        currentBoard = null;
    }

    /**
     * Shows a scoreboard for the player
     *
     * @param name name of the scoreboard
     *
     * @return true if successful, false otherwise
     */
    public boolean showBoard(String name)
    {
        for (Board board : boards)
            if (format(board.getName()).equals(name))
                return board.showPlayer();

        return false;
    }

    /**
     * Shows the next scoreboard
     */
    public void showNextBoard()
    {
        if (boards.size() == 0)
            return;
        int next = (current + 1) % boards.size();
        if (next != current)
            boards.get(next).showPlayer();
        current = next;
    }

    /**
     * Formats the name for the hash table
     *
     * @param name board name
     *
     * @return formatted board name
     */
    private String format(String name)
    {
        return ChatColor.stripColor(name.toLowerCase());
    }

    /**
     * Retrieves a board manager
     *
     * @param name scoreboard name
     *
     * @return board manager
     */
    public Board getBoard(String name)
    {
        for (Board board : boards)
            if (format(board.getName()).equals(name))
                return board;
        return null;
    }

    /**
     * Gets the active board for the player
     *
     * @return active board
     */
    public Board getActiveBoard()
    {
        return currentBoard;
    }

    /**
     * Checks whether or not the player has an active scoreboard
     *
     * @return true if has an active scoreboard, false otherwise
     */
    public boolean hasActiveBoard()
    {
        return boards.size() > 0;
    }

    /**
     * @return the boards attached to the player
     */
    public List<Board> getBoards()
    {
        return boards;
    }

    /**
     * @return true if cycling, false otherwise
     */
    public boolean isCycling()
    {
        return cycling;
    }

    /**
     * Makes the player's scoreboard cycle
     */
    public void startCycling()
    {
        cycling = true;
    }

    /**
     * Makes the player's scoreboard stop cycling
     */
    public void stopCycling()
    {
        cycling = false;
    }

    /**
     * Sets the health label for this player
     *
     * @param label health label
     * @deprecated use BoardManager.setTextBelowNames instead
     */
    @Deprecated
    public void setHealthLabel(String label)
    {
        BoardManager.setTextBelowNames(label);
    }
}
