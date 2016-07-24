/**
 * MCCore
 * com.rit.sucy.config.DataFile
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
import com.rit.sucy.config.parse.JSONParser;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

/**
 * Handles saving/loading data in a more condensed JSON format
 * with UTF-8 support in order to save disk space compared to
 * regular YAML configurations.
 */
public class DataFile {

    private final String fileName;
    private final JavaPlugin plugin;

    private File configFile;
    private DataSection data;

    /**
     * Constructor
     *
     * @param plugin plugin reference
     * @param name   file name
     */
    public DataFile(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.fileName = name + ".json";

        // Setup the path
        this.configFile = new File(plugin.getDataFolder().getAbsolutePath() + "/" + fileName);
        try {
            String path = configFile.getAbsolutePath();
            if (new File(path.substring(0, path.lastIndexOf(File.separator))).mkdirs())
                plugin.getLogger().info("Created a new folder for data files");
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
        return fileName.replace(".json", "");
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
        data = JSONParser.parseFile(configFile);
    }

    /**
     * @return config file
     */
    public DataSection getData() {
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
    public File getFile() {
        return configFile;
    }

    /**
     * Saves the config
     */
    public void save() {
        if (data != null && configFile != null) {
            try {
                data.dump(configFile);
            } catch (Exception ex) {
                plugin.getLogger().log(Level.SEVERE, "Could not save data to " + configFile, ex);
            }
        }
    }
}
