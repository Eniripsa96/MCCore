/**
 * MCCore
 * com.rit.sucy.scoreboard.BoardManager
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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.HashMap;

/**
 * Main accessor for player scoreboards
 */
public class BoardManager
{
    private static final HashMap<String, PlayerBoards> players = new HashMap<String, PlayerBoards>();

    private static Scoreboard scoreboard;

    private static boolean scoreboardUsed = false;

    static void init(Player player)
    {
        if (scoreboardUsed && player != null)
            player.setScoreboard(scoreboard);
    }

    /**
     * Initializes the scoreboard utility
     */
    public static void init()
    {
        if (scoreboard == null)
        {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            Board.init(scoreboard);
        }
    }

    /**
     * Retrieves scoreboard data for a player
     *
     * @param player player
     *
     * @return player's scoreboard data
     */
    public static PlayerBoards getPlayerBoards(String player)
    {
        if (!players.containsKey(player.toLowerCase()))
            players.put(player.toLowerCase(), new PlayerBoards(player));
        return players.get(player.toLowerCase());
    }

    /**
     * <p>Registers a new team with all player boards</p>
     *
     * @param team team to register
     */
    public static void registerTeam(Team team)
    {
        org.bukkit.scoreboard.Team sbTeam = scoreboard.getTeam(team.getName());
        if (sbTeam == null)
        {
            sbTeam = scoreboard.registerNewTeam(team.getName());
            sbTeam.setAllowFriendlyFire(true);
            sbTeam.setCanSeeFriendlyInvisibles(false);
        }
        updateTeam(team);
    }

    /**
     * Updates a team with all players
     *
     * @param team team to update
     */
    public static void updateTeam(Team team)
    {
        org.bukkit.scoreboard.Team sbTeam = scoreboard.getTeam(team.getName());
        sbTeam.setPrefix(team.getPrefix());
        sbTeam.setSuffix(team.getSuffix());
    }

    /**
     * <p>Sets the team for a player</p>
     * <p>If the team doesn't exist, it will be created
     * with a prefix matching its name</p>
     *
     * @param player player to set to the team
     * @param team   team to add the player to
     */
    public static void setTeam(String player, String team)
    {
        enableScoreboard();
        scoreboard.getTeam(team).addEntry(player);
    }

    /**
     * Clears the team for the player with the provided name
     *
     * @param player player name
     */
    public static void clearTeam(String player)
    {
        org.bukkit.scoreboard.Team sbTeam = scoreboard.getEntryTeam(player);
        if (sbTeam != null) sbTeam.removeEntry(player);
    }

    /**
     * Sets the text below player names
     *
     * @param t text to show
     */
    public static void setTextBelowNames(String t)
    {
        enableScoreboard();
        Objective objective = scoreboard.getObjective(DisplaySlot.BELOW_NAME);
        if (objective == null)
        {
            objective = scoreboard.registerNewObjective("dummy", "dummy");
            objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        }
        objective.setDisplayName(t);
    }

    /**
     * Enables the scoreboard, overriding all players' scoreboards
     */
    private static void enableScoreboard()
    {
        if (!scoreboardUsed)
        {
            for (Player player : Bukkit.getOnlinePlayers())
                player.setScoreboard(scoreboard);
            scoreboardUsed = true;
        }
    }

    /**
     * Sets the score for a player for below name text
     *
     * @param player player to set for
     * @param score  score to set
     */
    public static void setBelowNameScore(String player, int score)
    {
        if (!scoreboardUsed)
            throw new IllegalStateException("Cannot set below name score before text");

        scoreboard.getObjective(DisplaySlot.BELOW_NAME)
            .getScore(player)
            .setScore(score);
    }

    /**
     * Clears the score for a player
     *
     * @param player player to clear for
     */
    public static void clearScore(String player)
    {
        scoreboard.resetScores(player);
    }

    /**
     * Updates a scoreboard with the text below the player's name, if any
     *
     * @param board board to update
     */
    @Deprecated
    public static void updateBoard(Board board) { }

    /**
     * @return collection of player data
     */
    public static Collection<PlayerBoards> getAllPlayerBoards()
    {
        return players.values();
    }

    /**
     * Sets a label for a health bar under each player's name
     *
     * @param label label to set
     * @deprecated use setTextBelowNames instead
     */
    @Deprecated
    public static void setGlobalHealthBar(String label)
    {
        setTextBelowNames(label);
    }

    /**
     * Gives the scoreboard to every player
     *
     * @param board scoreboard to add
     */
    public static void addGlobalScoreboard(Board board)
    {
        for (PlayerBoards player : players.values())
            player.addBoard(board);
    }

    /**
     * Clears a board for all players
     *
     * @param plugin plugin to clear
     */
    public static void clearPluginBoards(String plugin)
    {
        for (PlayerBoards player : players.values())
            player.removeBoards(plugin);
    }

    /**
     * Clears data for a player with a given name
     *
     * @param name player name
     */
    public static void clearPlayer(String name)
    {
        if (players.containsKey(name.toLowerCase()))
            players.remove(name.toLowerCase());
    }
}
