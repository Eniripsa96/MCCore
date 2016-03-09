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

import com.rit.sucy.version.VersionManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Main accessor for player scoreboards
 */
public class BoardManager
{

    public static final  String                        TEAM_KEY    = "mcct";
    private static final HashMap<String, PlayerBoards> players     = new HashMap<String, PlayerBoards>();
    private static final HashMap<String, Team>         teams       = new HashMap<String, Team>();
    private static final HashMap<String, Integer>      scores      = new HashMap<String, Integer>();
    private static final HashMap<String, String>       playerTeams = new HashMap<String, String>();

    private static String text;

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
        if (getTeam(team.getName()) == null)
        {
            teams.put(team.getName().toLowerCase(), team);
        }
        String key = TEAM_KEY + team.getId();
        if (PlayerBoards.EMPTY.getTeam(key) == null)
        {
            PlayerBoards.EMPTY.registerNewTeam(key);
        }
        for (PlayerBoards player : players.values())
        {
            for (Board board : player.getBoards().values())
            {
                if (board.getScoreboard().getTeam(key) == null)
                {
                    board.getScoreboard().registerNewTeam(key);
                }
            }
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
        String key = TEAM_KEY + team.getId();
        org.bukkit.scoreboard.Team te = PlayerBoards.EMPTY.getTeam(key);
        te.setPrefix(team.getPrefix() == null ? "" : team.getPrefix());
        te.setSuffix(team.getSuffix() == null ? "" : team.getSuffix());
        for (PlayerBoards player : players.values())
        {
            for (Board board : player.getBoards().values())
            {
                org.bukkit.scoreboard.Team t = board.getScoreboard().getTeam(key);
                t.setPrefix(team.getPrefix() == null ? "" : team.getPrefix());
                t.setSuffix(team.getSuffix() == null ? "" : team.getSuffix());
            }
        }
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
        if (team == null || player == null) return;
        if (getTeam(team) == null) registerTeam(new Team(team, team, null));
        String key = TEAM_KEY + getTeam(team).getId();
        OfflinePlayer p = Bukkit.getOfflinePlayer(player);
        playerTeams.put(player.toLowerCase(), key);
        PlayerBoards.EMPTY.getTeam(key).addPlayer(p);
        for (PlayerBoards pb : players.values())
        {
            for (Board board : pb.getBoards().values())
            {
                board.getScoreboard().getTeam(key).addPlayer(p);
            }
        }
    }

    /**
     * Clears the team for the player with the provided name
     *
     * @param player player name
     */
    public static void clearTeam(String player)
    {
        if (player == null) return;
        OfflinePlayer p = Bukkit.getOfflinePlayer(player);
        org.bukkit.scoreboard.Team te = PlayerBoards.EMPTY.getPlayerTeam(p);
        if (te != null)
        {
            te.removePlayer(p);
            for (PlayerBoards pb : players.values())
            {
                for (Board board : pb.getBoards().values())
                {
                    org.bukkit.scoreboard.Team t = board.getScoreboard().getPlayerTeam(p);
                    if (t != null) t.removePlayer(p);
                }
            }
        }
    }

    /**
     * Sets the text below player names
     *
     * @param t text to show
     */
    public static void setTextBelowNames(String t)
    {
        text = t;
        Objective obj = PlayerBoards.EMPTY.getObjective(DisplaySlot.BELOW_NAME);
        if (obj == null)
        {
            obj = PlayerBoards.EMPTY.registerNewObjective("below", "dummy");
            obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
        }
        obj.setDisplayName(t);
        for (PlayerBoards pb : players.values())
        {
            for (Board board : pb.getBoards().values())
            {
                updateBoard(board);
            }
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
        scores.put(player.toLowerCase(), score);
        OfflinePlayer p = Bukkit.getOfflinePlayer(player);
        if (text != null)
        {
            PlayerBoards.EMPTY.getObjective(DisplaySlot.BELOW_NAME).getScore(p).setScore(score);
        }
        for (PlayerBoards pb : players.values())
        {
            for (Board board : pb.getBoards().values())
            {
                updateBoard(board);
            }
        }
    }

    /**
     * Clears the score for a player
     *
     * @param player player to clear for
     */
    public static void clearScore(String player)
    {
        scores.remove(player.toLowerCase());
        OfflinePlayer p = Bukkit.getOfflinePlayer(player);
        if (text != null)
        {
            PlayerBoards.EMPTY.resetScores(p);
        }
        for (PlayerBoards pb : players.values())
        {
            for (Board board : pb.getBoards().values())
            {
                board.getScoreboard().resetScores(p);
            }
        }
    }

    /**
     * Updates a scoreboard with the text below the player's name, if any
     *
     * @param board board to update
     */
    public static void updateBoard(Board board)
    {

        // Apply registered teams
        for (Team team : teams.values())
        {
            String key = BoardManager.TEAM_KEY + team.getId();
            if (board.getScoreboard().getTeam(key) == null)
            {
                org.bukkit.scoreboard.Team t = board.getScoreboard().registerNewTeam(key);
                t.setPrefix(team.getPrefix() == null ? "" : team.getPrefix());
                t.setSuffix(team.getSuffix() == null ? "" : team.getSuffix());
                t.setCanSeeFriendlyInvisibles(false);
                t.setAllowFriendlyFire(true);
            }
        }

        // Assign players to teams
        for (Map.Entry<String, String> teams : playerTeams.entrySet())
        {
            OfflinePlayer p = VersionManager.getOfflinePlayer(teams.getKey());
            if (board.getScoreboard().getPlayerTeam(p) != null)
            {
                board.getScoreboard().getPlayerTeam(p).removePlayer(p);
            }
            board.getScoreboard().getTeam(teams.getValue()).addPlayer(p);
        }

        // Apply text below the player names
        if (text == null)
        {
            if (board.getScoreboard().getObjective(DisplaySlot.BELOW_NAME) != null)
            {
                board.getScoreboard().clearSlot(DisplaySlot.BELOW_NAME);
            }
        }
        else
        {
            Objective obj = board.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);
            if (obj == null)
            {
                obj = board.getScoreboard().registerNewObjective("below", "dummy");
                obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
            }
            obj.setDisplayName(text);
            for (Map.Entry<String, Integer> score : scores.entrySet())
            {
                obj.getScore(Bukkit.getOfflinePlayer(score.getKey())).setScore(score.getValue());
            }
        }
    }

    /**
     * Retrieves a team by name
     *
     * @param name team name
     *
     * @return team reference
     */
    public static Team getTeam(String name)
    {
        return teams.get(name.toLowerCase());
    }

    /**
     * @return team data
     */
    public static HashMap<String, Team> getTeams()
    {
        return teams;
    }

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
     */
    public static void setGlobalHealthBar(String label)
    {
        for (PlayerBoards board : players.values())
            board.setHealthLabel(label);
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
        {
            player.removeBoards(plugin);
        }
    }

    /**
     * Clears data for a player with a given name
     *
     * @param name player name
     */
    public static void clearPlayer(String name)
    {
        if (players.containsKey(name.toLowerCase()))
        {
            players.remove(name.toLowerCase());
        }
    }
}
