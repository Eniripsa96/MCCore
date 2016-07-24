/**
 * MCCore
 * com.rit.sucy.config.CommentedConfig
 * <p/>
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2014 Steven Sucy
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software") to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.rit.sucy.config;

import com.rit.sucy.config.parse.DataSection;
import com.rit.sucy.config.parse.YAMLParser;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

/**
 * Handles configs with comment and UTF-8 support. Can be used
 * to handle config.yml to preserve/manage comments as well.
 */
public class CommentedConfig {

    private final String fileName;
    private final JavaPlugin plugin;

    private File configFile;
    private DataSection data;
    private DataSection defaults;

    /**
     * Constructor
     *
     * @param plugin plugin reference
     * @param name   file name
     */
    public CommentedConfig(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.fileName = name + ".yml";

        // Setup the path
        this.configFile = new File(plugin.getDataFolder().getAbsolutePath() + "/" + fileName);
        try {
            String path = configFile.getAbsolutePath();
            if (new File(path.substring(0, path.lastIndexOf(File.separator))).mkdirs())
                plugin.getLogger().info("Created a new folder for config files");
        } catch (Exception e) { /* */ }
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
    public String getFileName() {
        return fileName.replace(".yml", "");
    }

    /**
     * <p>Clears all of the data in the config</p>
     * <p>This doesn't save the config so if  you want
     * the changes to be reflected in the actual file,
     * call the save() method after doing this.</p>
     */
    public void clear() {
        if (data == null) {
            this.reload();
        }
        data.clear();
    }

    /**
     * Reloads the config data
     */
    public void reload() {
        data = YAMLParser.parseFile(configFile);
    }

    /**
     * @return config file
     */
    public DataSection getConfig() {
        if (data == null) {
            this.reload();
        }
        return data;
    }

    /**
     * <p>Retrieves the file of the configuration</p>
     *
     * @return the file of the configuration
     */
    public File getConfigFile() {
        return configFile;
    }

    /**
     * Saves the config
     */
    public void save() {
        if (data != null) {
            try {
                data.dump(configFile);
            } catch (Exception ex) {
                plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
            }
        }
    }

    /**
     * Saves the default config if no file exists yet
     */
    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            if (defaults == null) defaults = YAMLParser.parseResource(plugin, fileName);
            defaults.dump(configFile);
        }
    }

    /**
     * <p>Checks the configuration for default values, copying
     * default values over if they are not set. Once finished,
     * the config is saved so the user can see the changes.</p>
     * <p>This acts differently than saveDefaultConfig() as
     * the config can already exist for this method. This is
     * more for making sure users do not erase needed values
     * from settings configs.</p>
     */
    public void checkDefaults() {
        if (defaults == null) defaults = YAMLParser.parseResource(plugin, fileName);
        if (data == null) {
            this.reload();
        }
        data.applyDefaults(defaults);
    }

    /**
     * <p>Trims excess (non-default) values from the configuration</p>
     * <p>Any values that weren't in the default configuration are removed</p>
     * <p>This is primarily used for settings configs </p>
     */
    public void trim() {
        if (defaults == null) defaults = YAMLParser.parseResource(plugin, fileName);
        if (data == null) {
            this.reload();
        }
        data.trim(defaults);
    }
}
