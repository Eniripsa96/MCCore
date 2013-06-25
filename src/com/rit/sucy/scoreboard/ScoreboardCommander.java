package com.rit.sucy.scoreboard;

import com.rit.sucy.commands.CommandHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Handles commands for scoreboards
 */
public class ScoreboardCommander extends CommandHandler {

    /**
     * Constructor
     *
     * @param plugin plugin reference
     */
    public ScoreboardCommander(Plugin plugin) {
        super(plugin, "Scoreboard");
    }

    /**
     * Registers commands
     */
    @Override
    protected void registerCommands() {
        registerCommand("cycle", new CycleCommand());
        registerCommand("list", new ListCommand());
        registerCommand("show", new ShowCommand());
        registerCommand("stop", new StopCommand());
    }

    /**
     * Displays the usage for chat commands
     *
     * @param sender sender of the command
     */
    @Override
    public void displayUsage(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "Scoreboard commands are for players only!");
        }
        else super.displayUsage(sender);
    }
}
