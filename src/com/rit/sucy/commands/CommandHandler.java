package com.rit.sucy.commands;

import com.rit.sucy.text.TextSizer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Manages organizing commands into sub-commands
 */
public abstract class CommandHandler implements CommandExecutor {

    /**
     * Table of registered sub-commands
     */
    protected final Map<String, ICommand> commands = new HashMap<String, ICommand>();
    /**
     * Plugin reference
     */
    protected final Plugin plugin;

    /**
     * Command label
     */
    protected final String label;

    /**
     * Constructor
     *
     * @param plugin plugin reference
     * @param label  command label
     */
    public CommandHandler(Plugin plugin, String label) {
        this.plugin = plugin;
        this.label = label;
        registerCommands();
    }

    /**
     * @return plugin reference
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * @return command label
     */
    public String getLabel() {
        return label.toLowerCase();
    }

    /**
     * Registers a new sub-command
     *
     * @param command  command prefix
     * @param executor handler for the command
     */
    protected void registerCommand(String command, ICommand executor) {
        commands.put(command, executor);
    }

    /**
     * Called on a command
     *
     * @param sender sender of the command
     * @param cmd    command executed
     * @param label  command label
     * @param args   command arguments
     * @return       true
     */
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // No arguments simply shows the command usage
        if (args.length == 0) displayUsage(sender);

        // If a sub-command is found, execute it
        else if (commands.containsKey(args[0].toLowerCase())) {
            ICommand command = commands.get(args[0].toLowerCase());
            if (sender.hasPermission(command.getPermissionNode()))
                command.execute(this, plugin, sender, trimArgs(args));
            else
                sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that");
        }
        else {

            // Try to get a page number from the args
            try {
                int page = Integer.parseInt(args[0]);
                displayUsage(sender, page);
            }

            // If it wasn't a number, just display the first page
            catch (Exception e) {
                displayUsage(sender);
            }
        }

        // Use custom command usage
        return true;
    }

    /**
     * Trims the first element off of args
     *
     * @param args initial args
     * @return     trimmed args
     */
    protected String[] trimArgs(String[] args) {

        // Can't trim a zero-length array
        if (args.length == 0) return args;

        // Make a new array that is one smaller in size
        String[] trimmed = new String[args.length - 1];

        // Copy the array over if there are elements left
        if (trimmed.length > 0)
            System.arraycopy(args, 1, trimmed, 0, trimmed.length);

        // Return the new array
        return trimmed;
    }

    /**
     * Displays the command usage
     * - If you want custom displays, override the method with the page argument -
     *
     * @param sender sender of the command
     */
    public void displayUsage(CommandSender sender) {
        displayUsage(sender, 1);
    }

    /**
     * Displays the command usage
     * Can be overridden for custom displays
     *
     * @param sender sender of the command
     * @param page   page number
     */
    public void displayUsage (CommandSender sender, int page) {
        if (page < 1) page = 1;
        if (page > (commands.size() - 1) / 9 + 1) page = (commands.size() - 1) / 9 + 1;

        sender.sendMessage(ChatColor.DARK_GREEN + label + " - Command Usage (Page " + page + "/" + ((commands.size() - 1) / 9 + 1) + ")");

        // Get the maximum length
        int maxSize = 0;
        int index = 0;
        for (Map.Entry<String, ICommand> entry : commands.entrySet()) {
            if (!sender.hasPermission(entry.getValue().getPermissionNode()))
                continue;
            index++;
            if (index <= (page - 1) * 9 || index > page * 9) continue;
            int size = TextSizer.measureString(entry.getKey() + " " + entry.getValue().getArgsString());
            if (size > maxSize) maxSize = size;
        }
        maxSize += 4;

        // Display usage, squaring everything up nicely
        index = 0;
        for (Map.Entry<String, ICommand> command : commands.entrySet()) {
            if (!sender.hasPermission(command.getValue().getPermissionNode()))
                continue;
            index++;
            if (index <= (page - 1) * 9 || index > page * 9) continue;
            sender.sendMessage(ChatColor.GOLD + "   /" + TextSizer.expand(command.getKey() + " "
                    + ChatColor.LIGHT_PURPLE + command.getValue().getArgsString() + ChatColor.GRAY, maxSize, false)
                    + ChatColor.GRAY + "- " + command.getValue().getDescription());
        }
    }

    /**
     * Registers all sub-commands
     */
    protected abstract void registerCommands();
}
