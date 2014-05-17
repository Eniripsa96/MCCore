package com.rit.sucy.chat;

import com.rit.sucy.commands.CommandHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Controls commands for the chat API
 */
public class ChatCommander extends CommandHandler {

    /**
     * Constructor
     *
     * @param plugin plugin reference
     */
    public ChatCommander(Plugin plugin) {
        super(plugin, "Chat", "chat");
    }

    /**
     * Registers the sub-commands
     */
    @Override
    protected void registerCommands() {
        registerCommand("list", new ListCommand());
        registerCommand("name", new NameCommand());
        registerCommand("prefix", new PrefixCommand());
        registerCommand("reset", new ResetCommand());
    }

    /**
     * Displays the usage for chat commands
     *
     * @param sender sender of the command
     */
    @Override
    public void displayUsage(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "Chat commands are for players only!");
        }
        else super.displayUsage(sender);
    }
}
