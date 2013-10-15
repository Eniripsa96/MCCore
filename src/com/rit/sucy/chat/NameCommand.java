package com.rit.sucy.chat;

import com.rit.sucy.commands.CommandHandler;
import com.rit.sucy.commands.ICommand;
import com.rit.sucy.commands.SenderType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Changes a player's display name
 */
class NameCommand implements ICommand {

    /**
     * Executs the command
     *
     * @param handler command handler
     * @param plugin  plugin reference
     * @param sender  sender of the command
     * @param args    command arguments
     */
    @Override
    public void execute(CommandHandler handler, Plugin plugin, CommandSender sender, String[] args) {
        ChatData data = Chat.getPlayerData(sender.getName());
        if (data != null && args.length > 0) {
            String name = "";
            for (String piece : args)
                name += piece.replace('&', ChatColor.COLOR_CHAR) + " ";
            name = name.substring(0, name.length() - 2);
            data.setDisplayName(name);
            sender.sendMessage(ChatColor.DARK_GREEN + "Your name has been set");
        }
        else handler.displayUsage(sender);
    }

    /**
     * @return permission needed for this command
     */
    public String getPermissionNode() {
        return ChatNodes.NAME.getNode();
    }

    /**
     * @return args string
     */
    @Override
    public String getArgsString() {
        return "<name>";
    }

    /**
     * @return description
     */
    @Override
    public String getDescription() {
        return "Sets your display name";
    }

    /**
     * Sender required for the command
     */
    @Override
    public SenderType getSenderType() {
        return SenderType.PLAYER_ONLY;
    }
}
