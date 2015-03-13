package com.rit.sucy.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/**
 * A manager for a scoreboard
 */
public abstract class Board
{

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
    public Board(String title, String plugin)
    {
        this(title, "dummy", plugin);
    }

    /**
     * Constructs a new scoreboard manager with a desired type
     *
     * @param title title for the scoreboard
     * @param type  type of the scoreboard
     */
    public Board(String title, String type, String plugin)
    {
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
    public String getName()
    {
        return obj.getName();
    }

    /**
     * @return scoreboard that this boad manages
     */
    public Scoreboard getScoreboard()
    {
        return scoreboard;
    }

    /**
     * Shows a player the scoreboard
     *
     * @param player player to show
     */
    public void showPlayer(Player player)
    {
        player.setScoreboard(scoreboard);
    }

    /**
     * Sets the health objective for the scoreboard
     * - Recommended not to use this method -
     * - Use PlayerBoard or BoardManager instead -
     *
     * @param label scoreboard label
     */
    public void setHealthLabel(String label)
    {
        Objective obj = scoreboard.getObjective(DisplaySlot.BELOW_NAME) != null ?
                scoreboard.getObjective(DisplaySlot.BELOW_NAME)
                : scoreboard.registerNewObjective("hpBelowName", "health");
        if (obj.getDisplaySlot() != DisplaySlot.BELOW_NAME) obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
        obj.setDisplayName(label);
    }
}
