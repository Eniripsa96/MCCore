package com.rit.sucy.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * <p>A generic help function that can be plugged into any other command</p>
 */
public class HelpFunction implements IFunction {

    private ConfigurableCommand command;

    /**
     * <p>Creates a help function that displays the usage of the command</p>
     *
     * @param command command to display the sub command usage for
     */
    public HelpFunction(ConfigurableCommand command) {
        this.command = command;
    }

    /**
     * <p>Executes the function, displaying the usage for the command</p>
     *
     * @param command owning command
     * @param plugin  plugin reference
     * @param sender  sender of the command
     * @param args    command arguments
     */
    @Override
    public void execute(ConfigurableCommand command, Plugin plugin, CommandSender sender, String[] args) {
        this.command.displayHelp(sender, args);
    }
}
