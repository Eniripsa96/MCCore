package com.rit.sucy;

import com.rit.sucy.chat.ChatListener;
import com.rit.sucy.chat.ChatCommander;
import com.rit.sucy.commands.CommandListener;
import com.rit.sucy.commands.CommandManager;
import com.rit.sucy.config.Config;
import com.rit.sucy.economy.Economy;

import com.rit.sucy.economy.EconomyPlugin;
import com.rit.sucy.event.EquipListener;
import com.rit.sucy.player.PlayerUUIDs;
import com.rit.sucy.scoreboard.BoardListener;
import com.rit.sucy.scoreboard.CycleTask;
import com.rit.sucy.scoreboard.ScoreboardCommander;
import com.rit.sucy.scoreboard.UpdateTask;
import com.rit.sucy.text.TextFormatter;
import com.rit.sucy.version.VersionManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Hashtable;

public class MCCore extends JavaPlugin {

    private static Hashtable<String, Config> configs = new Hashtable<String, Config>();

    private Economy economy;
    private CycleTask cTask;
    private UpdateTask uTask;
    private PlayerUUIDs idManager;

    /**
     * Sets up commands and listeners
     */
    @Override
    public void onEnable() {

        VersionManager.initialize();
        if (VersionManager.isVersionAtLeast(VersionManager.MC_1_7_5_MIN)) {
            idManager = new PlayerUUIDs(this);
        }

        new ChatCommander(this);
        new ScoreboardCommander(this);
        new ChatListener(this);
        new BoardListener(this);
        new EquipListener(this);
        new CommandListener(this);

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
        if (idManager != null) idManager.save();
        for (Config config : configs.values())
            config.save();
        configs.clear();
        cTask.cancel();
        uTask.cancel();
        CommandManager.unregisterAll();
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
