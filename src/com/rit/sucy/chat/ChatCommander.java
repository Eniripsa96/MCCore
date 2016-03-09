/**
 * MCCore
 * com.rit.sucy.chat.ChatCommander
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
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Controls commands for the chat API
 */
public class ChatCommander extends CommandHandler
{

    /**
     * Constructor
     *
     * @param plugin plugin reference
     */
    public ChatCommander(Plugin plugin)
    {
        super(plugin, "Chat", "chat");
    }

    /**
     * Registers the sub-commands
     */
    @Override
    protected void registerCommands()
    {
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
    public void displayUsage(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.DARK_RED + "Chat commands are for players only!");
        }
        else super.displayUsage(sender);
    }
}
