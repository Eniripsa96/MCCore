package com.rit.sucy.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

/**
 * Handles configs for files other than the default config.yml
 *
 * Slightly modified version of the one from the bukkit tutorial
 * Source: http://wiki.bukkit.org/Configuration_API_Reference
 */
public class Config {

    private final String fileName;
    private final JavaPlugin plugin;
    private boolean saveOnDisable;

    private File configFile;
    private FileConfiguration fileConfiguration;

    /**
     * Constructor
     *
     * @param plugin plugin reference
     */
    public Config(JavaPlugin plugin, String name, boolean saveOnDisable) {
        this.plugin = plugin;
        this.saveOnDisable = saveOnDisable;
        this.fileName = name + ".yml";
        this.configFile = new File(plugin.getDataFolder().getAbsolutePath() + "/" + fileName);
        try {
            String path = configFile.getAbsolutePath();
            if (new File(path.substring(0, path.lastIndexOf(File.separator))).mkdirs())
                plugin.getLogger().info("Created a new folder for config files");
        }
        catch (Exception e) { /* */ }
    }

    /**
     * @return true if should save on disable, false otherwise
     */
    public boolean saveOnDisable() {
        return saveOnDisable;
    }

    /**
     * Reloads the config
     */
    public void reloadConfig() {
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
        InputStream defConfigStream = plugin.getResource(fileName);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            fileConfiguration.setDefaults(defConfig);
        }
    }

    /**
     * @return config file
     */
    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            this.reloadConfig();
        }
        return fileConfiguration;
    }

    /**
     * Saves the config
     */
    public void saveConfig() {
        if (fileConfiguration != null || configFile != null) {
            try {
                getConfig().save(configFile);
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
            }
        }
    }

    /**
     * Saves the default config if no file exists yet
     */
    public void saveDefaultConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder().getAbsolutePath() + "/" + fileName);
        }
        if (!configFile.exists()) {
            this.plugin.saveResource(fileName, false);
        }
    }
}
