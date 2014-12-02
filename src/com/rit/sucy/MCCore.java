package com.rit.sucy;

import com.rit.sucy.chat.ChatCommander;
import com.rit.sucy.chat.ChatListener;
import com.rit.sucy.commands.CommandListener;
import com.rit.sucy.commands.CommandLog;
import com.rit.sucy.commands.CommandManager;
import com.rit.sucy.commands.LogFunction;
import com.rit.sucy.config.Config;
import com.rit.sucy.economy.Economy;
import com.rit.sucy.economy.EconomyPlugin;
import com.rit.sucy.event.EquipListener;
import com.rit.sucy.items.DurabilityListener;
import com.rit.sucy.player.PlayerUUIDs;
import com.rit.sucy.reflect.Reflection;
import com.rit.sucy.scoreboard.BoardListener;
import com.rit.sucy.scoreboard.CycleTask;
import com.rit.sucy.scoreboard.ScoreboardCommander;
import com.rit.sucy.scoreboard.UpdateTask;
import com.rit.sucy.version.VersionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Ref;
import java.util.Hashtable;
import java.util.Map;

public class MCCore extends JavaPlugin {

    private static Hashtable<String, Config> configs = new Hashtable<String, Config>();

    private Economy economy;
    private CycleTask cTask;
    private UpdateTask uTask;
    private PlayerUUIDs idManager;

    // Settings
    private boolean chatEnabled;
    private boolean scoreboardsEnabled;
    private boolean equipEventsEnabled;
    private boolean durabilityEnabled;
    private boolean durabilityMessageEnabled;
    private String durabilityMessage;

    /**
     * Sets up commands and listeners
     */
    @Override
    public void onEnable() {

        CommandLog.callback = new LogFunction()
        {
            @Override
            public void execute(String msg)
            {
                getLogger().info("Logger => " + msg);
                VersionManager.initialize(msg);
            }
        };
        getServer().dispatchCommand(new CommandLog(), "version");

        // Initialize libraries
        if (VersionManager.isUUID()) {
            idManager = new PlayerUUIDs(this);
        }

        // Load settings
        saveDefaultConfig();
        Config.trim(getConfig());
        Config.setDefaults(getConfig());
        chatEnabled = getConfig().getBoolean("Features.chat-enabled", true);
        scoreboardsEnabled = getConfig().getBoolean("Features.scoreboards-enabled", true);
        equipEventsEnabled = getConfig().getBoolean("Features.equip-events-enabled", true);
        durabilityEnabled = getConfig().getBoolean("Features.durability-enabled", true);

        durabilityMessageEnabled = getConfig().getBoolean("Settings.durability-message-enabled", true);
        durabilityMessage = getConfig().getString("Settings.durability-message", "&6{current}&7/&6{max} &2Durability left on your &r{item}&r");

        if (chatEnabled) {
            new ChatCommander(this);
            new ChatListener(this);
        }
        if (scoreboardsEnabled) {
            new ScoreboardCommander(this);
            new BoardListener(this);
            cTask = new CycleTask(this);
            uTask = new UpdateTask(this);
        }
        if (equipEventsEnabled) {
            new EquipListener(this);
        }
        if (durabilityEnabled) {
            new DurabilityListener(this);
        }
        new CommandListener(this);

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
        if (isScoreboardsEnabled()) {
            cTask.cancel();
            uTask.cancel();
        }
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
     * Checks whether or not MCCore's chat management is enabled
     *
     * @return true if enabled, false otherwise
     */
    public boolean isChatEnabled() {
        return chatEnabled;
    }

    /**
     * Checks whether or not MCCore's scoreboard management is enabled
     *
     * @return true if enabled, false otherwise
     */
    public boolean isScoreboardsEnabled() {
        return scoreboardsEnabled;
    }

    /**
     * Checks whether or not MCCore's equip/unequip events are enabled
     *
     * @return true if enabled, false otherwise
     */
    public boolean isEquipEventsEnabled() {
        return equipEventsEnabled;
    }

    /**
     * Checks whether or not MCCore's durability events are enabled
     *
     * @return true if enabled, false otherwise
     */
    public boolean isDurabilityEnabled() {
        return durabilityEnabled;
    }

    /**
     * <p>Checks whether or not messages are to be displayed when
     * the durability of an item  changes.</p>
     *
     * @return true if enabled, false otherwise
     */
    public boolean isDurabilityMessageEnabled() {
        return durabilityMessageEnabled;
    }

    /**
     * <p>Retrieves the message to be shown when an item's durability changes</p>
     *
     * @return durability message
     */
    public String getDurabilityMessage() {
        return durabilityMessage;
    }

    /**
     * <p>Retrieves the configuration from a file for a plugin</p>
     * <p>If the config file hasn't been loaded yet, this will
     * load the file first.</p>
     * <p>Configs retrieved via this method are handled by MCCore
     * and automatically saved when MCCore disables.</p>
     * <p>This should not be used for settings configs
     * that admins may want to edit while the server is running
     * as the auto save will overwrite any changes they make.</p>
     *
     * @param file          file name
     * @return              config for the file
     */
    public ConfigurationSection getConfig(JavaPlugin plugin, String file) {
        return getConfigFile(plugin,file).getConfig();
    }

    /**
     * <p>Retrieves the configuration file for a plugin</p>
     * <p>If the config file hasn't been loaded yet, this will
     * load the file first.</p>
     * <p>Configs retrieved via this method are handled by MCCore
     * and automatically saved when MCCore disables.</p>
     * <p>This should not be used for settings configs
     * that admins may want to edit while the server is running
     * as the auto save will overwrite any changes they make.</p>
     *
     * @param file file name
     * @return     config manager for the file
     */
    public Config getConfigFile(JavaPlugin plugin, String file) {
        if (!configs.containsKey(file.toLowerCase() + plugin.getName())) {
            Config config = new Config(plugin, file);
            registerConfig(config);
            return config;
        }
        return configs.get(file.toLowerCase() + plugin.getName());
    }

    /**
     * <p>Registers the Config with MCCore for auto saving.</p>
     * <p>If the Config was already registered, this method will
     * not do anything.</p>
     *
     * @param config config to register
     */
    public void registerConfig(Config config) {
        configs.put(config.getFile().toLowerCase() + config.getPlugin().getName(), config);
    }
}
