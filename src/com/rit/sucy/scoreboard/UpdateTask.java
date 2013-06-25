package com.rit.sucy.scoreboard;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Updates stat boards periodically
 */
public class UpdateTask extends BukkitRunnable {

    /**
     * Constructor
     *
     * @param plugin plugin reference
     */
    public UpdateTask(Plugin plugin) {
        runTaskTimer(plugin, 10, 10);
    }

    /**
     * Updates stat boards
     */
    public void run() {
        for (PlayerBoards player : BoardManager.getAllPlayerBoards()) {
            for (Board board : player.boards.values()) {
                if (board instanceof StatBoard) {
                    ((StatBoard) board).update();
                }
            }
        }
    }
}
