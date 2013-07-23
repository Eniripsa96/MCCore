package com.rit.sucy;

import com.rit.sucy.chat.ChatListener;
import com.rit.sucy.chat.ChatCommander;
import com.rit.sucy.config.Config;
import com.rit.sucy.economy.Economy;

import com.rit.sucy.economy.EconomyPlugin;
import com.rit.sucy.event.EquipListener;
import com.rit.sucy.scoreboard.BoardListener;
import com.rit.sucy.scoreboard.CycleTask;
import com.rit.sucy.scoreboard.ScoreboardCommander;
import com.rit.sucy.scoreboard.UpdateTask;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

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

        new ChatCommander(this);
        new ScoreboardCommander(this);
        new ChatListener(this);
        new BoardListener(this);
        new EquipListener(this);

        cTask = new CycleTask(this);
        uTask = new UpdateTask(this);

        for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
            if (plugin instanceof EconomyPlugin) {
                this.economy = ((EconomyPlugin) plugin).getEconomy();
                break;
            }
        }
    }

    /**
     * Disables commands and listeners and saves applicable configs
     */
    @Override
    public void onDisable() {

        HandlerList.unregisterAll(this);
        for (Config config : configs.values())
            config.save();
        configs.clear();
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
     * Gets a config for a file
     *
     * @param file          file name
     * @return              config for the file
     */
    public ConfigurationSection getConfig(JavaPlugin plugin, String file) {
        return getConfigFile(plugin,file).getConfig();
    }

    /**
     * Gets the config manager for a file
     *
     * @param file file name
     * @return     config manager for the file
     */
    public Config getConfigFile(JavaPlugin plugin, String file) {
        if (!configs.containsKey(file.toLowerCase() + plugin.getName())) {
            return new Config(plugin, file);
        }
        return configs.get(file.toLowerCase() + plugin.getName());
    }

    /**
     * Registers the config for auto-saving
     *
     * @param config config to register
     */
    public void registerConfig(Config config) {
        configs.put(config.getFile().toLowerCase() + config.getPlugin().getName(), config);
    }
}
