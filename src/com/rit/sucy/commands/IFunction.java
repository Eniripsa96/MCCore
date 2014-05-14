package com.rit.sucy.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Interface for handlers for commands
 */
public interface IFunction {

    /**
     * Executes a function
     *
     * @param command owning command
     * @param plugin  plugin reference
     * @param sender  sender of the command
     * @param args    command arguments
     */
    public void execute(ConfigurableCommand command, Plugin plugin, CommandSender sender, String[] args);
}
