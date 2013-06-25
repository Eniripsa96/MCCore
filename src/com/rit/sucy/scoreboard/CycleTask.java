package com.rit.sucy.scoreboard;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Cycles through boards periodically
 */
public class CycleTask extends BukkitRunnable {

    /**
     * Constructor
     *
     * @param plugin plugin reference
     */
    public CycleTask(Plugin plugin) {
        runTaskTimer(plugin, 200, 200);
    }

    /**
     * Cycles through scoreboards
     */
    public void run() {
        for (PlayerBoards player : BoardManager.getAllPlayerBoards())
            if (player.isCycling())
                player.showNextBoard();
    }
}
