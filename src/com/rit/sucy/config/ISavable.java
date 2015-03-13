package com.rit.sucy.config;

import org.bukkit.configuration.ConfigurationSection;

/**
 * A savable object for automatic config saving
 */
public interface ISavable
{

    /**
     * Saves the object to the config using the base path
     *
     * @param config config to save to
     * @param path   base path
     */
    public void save(ConfigurationSection config, String path);
}
