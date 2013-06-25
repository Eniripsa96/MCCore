package com.rit.sucy.scoreboard;

import com.rit.sucy.commands.CommandHandler;
import com.rit.sucy.commands.ICommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Displays a list of all active scoreboards for a player
 */
public class ListCommand implements ICommand {

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
            String message = ChatColor.DARK_GREEN + "Active Scoreboards: ";
            PlayerBoards boards = BoardManager.getPlayerBoards(sender.getName());
            for (Board board : boards.boards.values()) {
                message += ChatColor.GOLD + ChatColor.stripColor(board.getName()) + ChatColor.GRAY + ", ";
            }
            sender.sendMessage(message);
        }
        else handler.displayUsage(sender);
    }

    @Override
    public String getPermissionNode() {
        return ScoreboardNodes.LIST.getNode();
    }

    @Override
    public String getArgsString() {
        return "";
    }

    @Override
    public String getDescription() {
        return "Displays a list of active scoreboards";
    }
}
