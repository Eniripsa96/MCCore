package com.rit.sucy.scoreboard;

import com.rit.sucy.commands.CommandHandler;
import com.rit.sucy.commands.ICommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Shows a desired scoreboard for the player
 */
public class ShowCommand implements ICommand {

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
        if (sender instanceof Player && args.length > 0) {
            String name = args[0];
            for (int i = 1; i < args.length; i++) {
                name += " " + args[i];
            }
            PlayerBoards board = BoardManager.getPlayerBoards(sender.getName());
            if (board.showBoard(name))
                sender.sendMessage(ChatColor.DARK_GREEN + "Your scoreboard has been changed");
            else
                sender.sendMessage(ChatColor.DARK_RED + "You do not have a scoreboard with that name");
        }
        else handler.displayUsage(sender);
    }

    /**
     * @return permission needed for this command
     */
    @Override
    public String getPermissionNode() {
        return ScoreboardNodes.SHOW.getNode();
    }

    /**
     * @return args string
     */
    @Override
    public String getArgsString() {
        return "<boardName>";
    }

    /**
     * @return description
     */
    @Override
    public String getDescription() {
        return "Shows the scoreboard";
    }
}
