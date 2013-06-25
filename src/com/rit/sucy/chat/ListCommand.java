package com.rit.sucy.chat;

import com.rit.sucy.commands.CommandHandler;
import com.rit.sucy.commands.ICommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Displays a list of unlocked prefixes
 */
class ListCommand implements ICommand {

    /**
     * Executes the command
     * @param handler command handler
     * @param plugin  plugin reference
     * @param sender  sender of the command
     * @param args    command arguments
     */
    @Override
    public void execute(CommandHandler handler, Plugin plugin, CommandSender sender, String[] args) {
        ChatData data = Chat.getPlayerData(sender.getName());
        if (data != null) {
            String message = ChatColor.DARK_GREEN + "Unlocked prefixes: ";
            if (data.unlockedPrefixes.size() > 0) {
                for (Prefix prefix : data.unlockedPrefixes) {
                    message += prefix.text + ChatColor.GRAY + ", ";
                }
                message = message.substring(0, message.length() - 2);
            }
            sender.sendMessage(message);
        }
        else handler.displayUsage(sender);
    }

    /**
     * @return permission needed for this command
     */
    public String getPermissionNode() {
        return ChatNodes.LIST.getNode();
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
        return "Displays unlocked prefixes";
    }
}
