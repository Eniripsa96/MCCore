package com.rit.sucy;

import com.rit.sucy.chat.Chat;
import com.rit.sucy.chat.ChatListener;
import com.rit.sucy.chat.ChatCommander;
import com.rit.sucy.config.Config;
import com.rit.sucy.economy.Economy;

import com.rit.sucy.scoreboard.BoardListener;
import com.rit.sucy.scoreboard.CycleTask;
import com.rit.sucy.scoreboard.ScoreboardCommander;
import com.rit.sucy.scoreboard.UpdateTask;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.Hashtable;

public class MCCore extends JavaPlugin {

    /**
     * Config files in use
     */
    private static Hashtable<String, Config> configs = new Hashtable<String, Config>();

    /**
     * The active economy
     */
    private Economy economy;

    /**
     * Board cycling task
     */
    private CycleTask cTask;

    /**
     * Stat board update task
     */
    private UpdateTask uTask;

    /**
     * Sets up commands and listeners
     */
    @Override
    public void onEnable() {

        getCommand("chat").setExecutor(new ChatCommander(this));
        getCommand("board").setExecutor(new ScoreboardCommander(this));
        new ChatListener(this);
        new BoardListener(this);

        cTask = new CycleTask(this);
        uTask = new UpdateTask(this);

        getLogger().info(Bukkit.getScoreboardManager().getNewScoreboard().getObjective(DisplaySlot.BELOW_NAME) + " is it");
    }

    /**
     * Disables commands and listeners and saves applicable configs
     */
    @Override
    public void onDisable() {

        HandlerList.unregisterAll(this);
        Chat.save();
        cTask.cancel();
        uTask.cancel();
    }

    /**
     * Gets the active economy
     *
     * @return active economy
     */
    public Economy getEconomy() {
        return economy;
    }

    /**
     * Sets the economy to be accessed
     *
     * @param economy economy class
     * @return        true if successful, false otherwise
     */
    public boolean setEconomy(Economy economy) {
        if (this.economy == null || this.economy == economy) {
            this.economy = economy;
            return true;
        }
        else {
            getLogger().severe("Multiple economies detected! - " + economy.getClass().getName() + " was rejected!");
            return false;
        }
    }

    /**
     * Gets a config for a file
     *
     * @param file          file name
     * @return              config for the file
     */
    public ConfigurationSection getConfig(String file) {
        if (!configs.containsKey(file.toLowerCase())) {
            configs.put(file.toLowerCase(), new Config(this, file));
        }
        return configs.get(file.toLowerCase()).getConfig();
    }
}
