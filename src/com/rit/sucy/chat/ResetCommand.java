package com.rit.sucy.chat;

import com.rit.sucy.commands.CommandHandler;
import com.rit.sucy.commands.ICommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Resets the player's display name
 */
class ResetCommand implements ICommand {

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
        ChatData data = Chat.getPlayerData(sender.getName());
        if (data != null) {
            data.setDisplayName(sender.getName());
            sender.sendMessage(ChatColor.DARK_GREEN + "Your name has been reset to its default");
        }
        else handler.displayUsage(sender);
    }

    /**
     * @return permission needed for this command
     */
    public String getPermissionNode() {
        return ChatNodes.RESET.getNode();
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
        return "Resets your display name";
    }
}
