package com.rit.sucy.chat;

import com.rit.sucy.commands.CommandHandler;
import com.rit.sucy.commands.ICommand;
import com.rit.sucy.commands.SenderType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Sets a prefix for a player
 */
class PrefixCommand implements ICommand
{

    /**
     * Executes the command
     *
     * @param handler command handler
     * @param plugin  plugin reference
     * @param sender  sender of the command
     * @param args    command arguments
     */
    @Override
    public void execute(CommandHandler handler, Plugin plugin, CommandSender sender, String[] args)
    {
        ChatData data = Chat.getPlayerData(sender.getName());
        if (data != null && args.length == 1)
        {
            if (data.hasPrefix(args[0]))
            {
                data.setPrefix(args[0]);
                sender.sendMessage(ChatColor.DARK_GREEN + "The prefix has been set!");
            }
            else sender.sendMessage(ChatColor.DARK_RED + "You do not have that prefix!");
        }
        else handler.displayUsage(sender);
    }

    /**
     * @return permission needed for this command
     */
    public String getPermissionNode()
    {
        return ChatNodes.PREFIX.getNode();
    }

    /**
     * @return args string
     */
    @Override
    public String getArgsString()
    {
        return "<prefix>";
    }

    /**
     * @return description
     */
    @Override
    public String getDescription()
    {
        return "Sets your prefix";
    }

    /**
     * Sender required for the command
     */
    @Override
    public SenderType getSenderType()
    {
        return SenderType.PLAYER_ONLY;
    }
}
