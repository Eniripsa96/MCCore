package com.rit.sucy.scoreboard;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Map;

/**
 * Stat board for displaying various statistics
 */
public class StatBoard extends Board {

    private final ArrayList<StatHolder> holders = new ArrayList<StatHolder>();

    /**
     * Constructor
     *
     * @param title         scoreboard title
     */
    public StatBoard(String title, String plugin) {
        super(title, plugin);
    }

    /**
     * Add stats to the scoreboard
     *
     * @param holder holder of the stats
     */
    public void addStats(StatHolder holder) {
        holders.add(holder);
        update();
    }

    /**
     * Clears the stats from a stat holder
     *
     * @param holder stat holder
     */
    public void clearStats(StatHolder holder) {
        for (String statName : holder.getStats().keySet())
            scoreboard.resetScores(Bukkit.getOfflinePlayer(statName));
    }

    /**
     * Updates the stats for this scoreboard
     */
    public void update() {
        for (StatHolder holder : holders) {
            for (Map.Entry<String, Integer> entry : holder.getStats().entrySet()) {
                obj.getScore(Bukkit.getOfflinePlayer(entry.getKey())).setScore(entry.getValue());
            }
        }
    }
}
