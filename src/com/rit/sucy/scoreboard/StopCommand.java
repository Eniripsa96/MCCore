package com.rit.sucy.scoreboard;

import com.rit.sucy.commands.CommandHandler;
import com.rit.sucy.commands.ICommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Stops a player's scoreboard from cycling
 */
public class StopCommand implements ICommand {

    /**
     * Executes the command
     *
     * @param handler command handler
     * @param plugin  plugin reference
     * @param sender  sender of the command
     * @param args    command arguments
     */
    @Override
    public void execute(CommandHandler handler, Plugin plugin, CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            PlayerBoards board = BoardManager.getPlayerBoards(sender.getName());
            if (!board.isCycling())
                sender.sendMessage(ChatColor.DARK_RED + "Your scoreboard is already stopped");
            else {
                board.stopCycling();
                sender.sendMessage(ChatColor.DARK_GREEN + "Your scoreboard is no longer cycling");
            }
        }
        else handler.displayUsage(sender);
    }

    /**
     * @return permission needed for this command
     */
    @Override
    public String getPermissionNode() {
        return ScoreboardNodes.STOP.getNode();
    }

    /**
     * @return args string
     */
    @Override
    public String getArgsString() {
        return "";
    }

    /**
     * @return description
     */
    @Override
    public String getDescription() {
        return "Stops cycling the scoreboard";
    }
}
