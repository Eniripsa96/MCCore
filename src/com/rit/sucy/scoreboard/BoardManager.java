package com.rit.sucy.scoreboard;

import java.util.Collection;
import java.util.Hashtable;

/**
 * Main accessor for player scoreboards
 */
public class BoardManager {

    static final Hashtable<String, PlayerBoards> players = new Hashtable<String, PlayerBoards>();

    /**
     * Retrieves scoreboard data for a player
     *
     * @param player player
     * @return       player's scoreboard data
     */
    public static PlayerBoards getPlayerBoards(String player) {
        if (!players.containsKey(player.toLowerCase()))
            players.put(player.toLowerCase(), new PlayerBoards(player));
        return players.get(player.toLowerCase());
    }

    /**
     * @return collection of player data
     */
    public static Collection<PlayerBoards> getAllPlayerBoards() {
        return players.values();
    }

    /**
     * Sets a label for a health bar under each player's name
     *
     * @param label label to set
     */
    public static void setGlobalHealthBar(String label) {
        for (PlayerBoards board : players.values())
            board.setHealthLabel(label);
    }

    /**
     * Gives the scoreboard to every player
     *
     * @param board scoreboard to add
     */
    public static void addGlobalScoreboard(Board board) {
        for (PlayerBoards player : players.values())
            player.addBoard(board);
    }

    /**
     * Clears a board for all players
     *
     * @param plugin plugin to clear
     */
    public static void clearPluginBoards(String plugin) {
        for (PlayerBoards player : players.values()) {
            player.removeBoards(plugin);
        }
    }

    /**
     * Clears data for a player with a given name
     *
     * @param name player name
     */
    public static void clearPlayer(String name) {
        if (players.containsKey(name.toLowerCase())) {
            players.remove(name.toLowerCase());
        }
    }
}
