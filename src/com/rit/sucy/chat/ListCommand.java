/**
 * MCCore
 * com.rit.sucy.chat.ListCommand
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Steven Sucy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software") to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.rit.sucy.chat;

import com.rit.sucy.commands.CommandHandler;
import com.rit.sucy.commands.ICommand;
import com.rit.sucy.commands.SenderType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Displays a list of unlocked prefixes
 */
class ListCommand implements ICommand
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
        if (data != null)
        {
            String message = ChatColor.DARK_GREEN + "Unlocked prefixes: ";
            if (data.unlockedPrefixes.size() > 0)
            {
                for (Prefix prefix : data.unlockedPrefixes)
                {
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
    public String getPermissionNode()
    {
        return ChatNodes.LIST.getNode();
    }

    /**
     * @return args string
     */
    @Override
    public String getArgsString()
    {
        return "";
    }

    /**
     * @return description
     */
    @Override
    public String getDescription()
    {
        return "Displays unlocked prefixes";
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
