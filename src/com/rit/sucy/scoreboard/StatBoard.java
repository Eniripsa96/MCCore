package com.rit.sucy.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Map;

/**
 * Stat board for displaying various statistics
 */
public class StatBoard extends Board {

    private final ArrayList<StatHolder> holders = new ArrayList<StatHolder>();
    private final ArrayList<OfflinePlayer> stats = new ArrayList<OfflinePlayer>();

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
        for (OfflinePlayer stat : holder.getStats()) {
            stats.add(stat);
        }
        update();
    }

    /**
     * Clears the stats from a stat holder
     *
     * @param holder stat holder
     */
    public void clearStats(StatHolder holder) {
        for (OfflinePlayer stat : stats) {
            scoreboard.resetScores(stat);
        }
    }

    /**
     * Updates the stats for this scoreboard
     */
    public void update() {
        for (StatHolder holder : holders) {
            int index = 0;
            for (Integer value : holder.getValues()) {
                obj.getScore(stats.get(index++)).setScore(value);
            }
        };
    }
}
