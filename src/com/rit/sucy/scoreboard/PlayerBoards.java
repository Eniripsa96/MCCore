package com.rit.sucy.scoreboard;

import com.rit.sucy.version.VersionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Scoreboard data for a player
 */
public class PlayerBoards
{

    public static final Scoreboard EMPTY = Bukkit.getScoreboardManager().getNewScoreboard();

    final Hashtable<String, Board> boards = new Hashtable<String, Board>();
    private final String player;
    private       String currentBoard;

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
        return Bukkit.getPlayer(player);
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
        String fName = format(board.getName());
        boards.put(fName, board);
        BoardManager.updateBoard(board);
        if (currentBoard == null)
        {
            board.showPlayer(VersionManager.getPlayer(player));
            currentBoard = fName;
        }
    }

    /**
     * Removes a board from a player
     *
     * @param board board to remove
     */
    public void removeBoard(Board board)
    {

        String fName = format(board.getName());
        if (!boards.containsKey(fName))
            return;

        boards.remove(fName);
        validateBoard();
    }

    /**
     * Removes all boards from a plugin
     *
     * @param plugin plugin name
     */
    public void removeBoards(String plugin)
    {
        ArrayList<Board> list = new ArrayList<Board>(boards.values());
        for (Board board : list)
        {
            if (board.plugin.equalsIgnoreCase(plugin))
            {
                boards.remove(format(board.getName()));
            }
        }
        validateBoard();
    }

    /**
     * Validates that the current board is active
     */
    private void validateBoard()
    {
        if (currentBoard == null)
            return;
        if (!boards.contains(currentBoard))
        {
            if (boards.size() > 0)
                showNextBoard();
            else
            {
                currentBoard = null;
                Bukkit.getPlayer(player).setScoreboard(EMPTY);
            }
        }
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

        name = format(name);
        if (boards.containsKey(name))
        {
            Bukkit.getPlayer(player).setScoreboard(boards.get(name).getScoreboard());
            currentBoard = name;
            return true;
        }

        return false;
    }

    /**
     * Shows the next scoreboard
     */
    public void showNextBoard()
    {
        if (boards.size() == 0)
            return;

        ArrayList<Board> boards = new ArrayList<Board>(this.boards.values());

        if (boards.size() == 1 || currentBoard == null)
        {
            if (currentBoard == null)
            {
                showBoard(boards.get(0).getName());
            }
            return;
        }

        for (int i = 0; i < boards.size(); i++)
        {
            if (format(boards.get(i).getName()).equalsIgnoreCase(currentBoard))
            {
                showBoard(boards.get((i + 1) % boards.size()).getName());
                return;
            }
        }
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
        return boards.get(name.toLowerCase());
    }

    /**
     * Gets the active board for the player
     *
     * @return active board
     */
    public Board getActiveBoard()
    {
        return boards.get(currentBoard);
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
    public Hashtable<String, Board> getBoards()
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
     */
    public void setHealthLabel(String label)
    {
        for (Board board : boards.values())
        {
            board.setHealthLabel(label);
            Player player = Bukkit.getPlayer(this.player);
            player.setHealth(player.getHealth());
        }
    }
}
