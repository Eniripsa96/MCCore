package com.rit.sucy.config;

import com.rit.sucy.MCCore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Handles configs for files other than the default config.yml
 *
 * Slightly modified version of the one from the bukkit tutorial
 * Source: http://wiki.bukkit.org/Configuration_API_Reference
 */
public class Config {

    private final HashMap<ISavable, String> savables = new HashMap<ISavable, String>();
    private final String fileName;
    private final JavaPlugin plugin;

    private File configFile;
    private FileConfiguration fileConfiguration;

    /**
     * Constructor
     *
     * @param plugin plugin reference
     * @param name   file name
     */
    public Config(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.fileName = name + ".yml";

        // Setup the path
        this.configFile = new File(plugin.getDataFolder().getAbsolutePath() + "/" + fileName);
        try {
            String path = configFile.getAbsolutePath();
            if (new File(path.substring(0, path.lastIndexOf(File.separator))).mkdirs())
                plugin.getLogger().info("Created a new folder for config files");
        }
        catch (Exception e) { /* */ }

        // Register for auto-saving
        ((MCCore)plugin.getServer().getPluginManager().getPlugin("MCCore")).registerConfig(this);
    }

    /**
     * @return plugin owning this config file
     */
    public JavaPlugin getPlugin() {
        return plugin;
    }

    /**
     * @return name of the file this config saves to
     */
    public String getFile() {
        return fileName.replace(".yml", "");
    }

    /**
     * Saves if there are savables added
     */
    public void save() {
        if (savables.size() == 0)
            return;

        for (Map.Entry<ISavable, String> entry : savables.entrySet()) {
            entry.getKey().save(getConfig(), entry.getValue());
        }
        saveConfig();
    }

    /**
     * Adds a savable object to the config for automatic saving
     *
     * @param savable  savable object
     * @param basePath base path for it
     */
    public void addSavable(ISavable savable, String basePath) {
        this.savables.put(savable, basePath);
    }

    /**
     * Deletes the savable from the config
     *
     * @param savable savable to delete
     */
    public void deleteSavable(ISavable savable) {
        if (savables.containsKey(savable)) {
            plugin.getLogger().info("Deleting!");
            String base = savables.get(savable);
            if (base.length() > 0 && base.charAt(base.length() - 1) == '.')
                base = base.substring(0, base.length() - 1);
            getConfig().set(base, null);
            savables.remove(savable);
        }
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
