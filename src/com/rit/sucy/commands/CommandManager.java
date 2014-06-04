package com.rit.sucy.commands;

import com.rit.sucy.config.Config;
import com.rit.sucy.text.TextFormatter;
import com.rit.sucy.text.TextSizer;
import com.rit.sucy.text.TextSplitter;
import com.rit.sucy.version.VersionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * <p>The handler for configurable commands</p>
 * <p>Only configurable commands registered using this class will function.</p>
 * <p>It is recommended that you use "ConfigurableCommand" instead as it provides
 * a more flexible structure for your commands and makes them automatically
 * configurable as well.</p>
 */
public class CommandManager {

    private static final HashMap<String, ConfigurableCommand> commands = new HashMap<String, ConfigurableCommand>();
    private static final HashMap<Plugin, List<String>> plugins = new HashMap<Plugin, List<String>>();
    private static final HashMap<Plugin, Config> configs = new HashMap<Plugin, Config>();

    // Configuration keys for the usage settings
    private static final String
            HELP_BUTTON = "Format.help-button",
            HELP_NO_BUTTON = "Format.help-no-button",
            PAGE = "Format.page",
            NO_DESCRIPTION = "Format.no-description",
            COMMAND_USAGE = "Format.command-usage",
            NO_COMMANDS = "Format.no-commands",
            NEXT_PAGE = "Format.next-button",
            NEXT_PAGE_HOVER = "Format.next-button-hover",
            PREV_PAGE = "Format.prev-button",
            PREV_PAGE_HOVER = "Foramt.prev-button-hover",
            COMMAND = "Colors.command",
            REQUIRED = "Colors.required-args",
            OPTIONAL = "Colors.optional-args",
            DESCRIPTION = "Colors.description",
            PLAYER_SIZE = "player-help-size",
            CONSOLE_SIZE = "console-help-size";

    // Settings for usage sizes
    private static int
            playerSize = 10,
            consoleSize = 15;

    // Settings for usage display colors
    private static ChatColor
            command = ChatColor.GOLD,
            requiredArgs = ChatColor.LIGHT_PURPLE,
            optionalArgs = ChatColor.GREEN,
            description = ChatColor.GRAY;

    // Settings for usage display formatting
    private static String
            pageFormat = ChatColor.DARK_GREEN + "(" + ChatColor.GOLD + "{page}" + ChatColor.DARK_GREEN + "/" + ChatColor.GOLD + "{max}" + ChatColor.DARK_GREEN + ")",
            noDescription = "No description available",
            noCommands = ChatColor.GRAY + "No commands available",
            nextPage = "Next",
            prevPage = "Previous",
            nextPageHover = "Next Page",
            prevPageHover = "Previous Page";
    private static List<String>
            helpWithButton = new ArrayList<String>() {{
                add(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                add(ChatColor.DARK_GREEN + "{title} - Command Usage {page}");
                add("{commands}");
                add(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                add("{buttons}");
            }},
            helpNoButton = new ArrayList<String>() {{
                add(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                add(ChatColor.DARK_GREEN + "{title} - Command Usage {page}");
                add(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                add("{commands}");
                add(ChatColor.DARK_GRAY + "-----------------------------------------------------");
            }},
            commandUsage = new ArrayList<String>() {{
                add(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                add(ChatColor.GOLD + "{command}");
                add(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                add(ChatColor.GRAY + "{description}");
                add(ChatColor.DARK_GRAY + "-----------------------------------------------------");
            }};

    /**
     * <p>Retrieves the replacement for descriptions when they are not set</p>
     * <p>This is used by the API for the command usage. You generally will not
     * need to use this.</p>
     *
     * @return description replacement
     */
    public static String getDescriptionReplacement() {
        return noDescription;
    }

    /**
     * <p>Registers a new ConfigurableCommand for the plugin</p>
     * <p>Sub commands do not need to be registered as they are
     * linked to the root commands.</p>
     *
     * @param command command to register
     * @throws java.lang.IllegalArgumentException when trying to register a sub command or when the command's name is already taken
     */
    public static void registerCommand(ConfigurableCommand command) {
        if (!command.isRootCommand()) throw new IllegalArgumentException("Cannot register a sub command");
        if (commands.containsKey(command.getName())) throw new IllegalArgumentException("Duplicate command name found: \"" + command.getName() + "\"");

        // Register the command for use
        command.markAsRegistered();
        commands.put(command.getName(), command);

        // Register it for the plugin
        if (!plugins.containsKey(command.getPlugin())) {
            plugins.put(command.getPlugin(), new ArrayList<String>());
            configs.put(command.getPlugin(), new Config(command.getPlugin(), "commands"));
        }
        plugins.get(command.getPlugin()).add(command.getName());

        // Register it with Bukkit
        try {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            if (!field.isAccessible()) field.setAccessible(true);
            CommandMap map = (CommandMap)field.get(Bukkit.getPluginManager());
            map.register(command.getName(), command);
        }
        catch (Exception ex) {
            Bukkit.getLogger().severe("Failed to register the command \"" + command.getName() + "\" with Bukkit");
            ex.printStackTrace();
        }
    }

    /**
     * <p>Unregisters a command</p>
     * <p>If the command wasn't registered, this does nothing</p>
     *
     * <p>Commands are automatically unregistered when your plugin
     * is disabled, so you generally do not need to manually
     * unregister your commands. This would be more for if you want
     * to unregister a command during run time such as if a command
     * was disabled via a command</p>
     *
     * @param command command to unregister
     */
    public static void unregisterCommand(ConfigurableCommand command) {
        commands.remove(command.getName());
    }

    /**
     * <p>Unregisters all of the commands registered by a plugin</p>
     * <p>If the plugin didn't have any registered commands, this does nothing</p>
     *
     * <p>Commands are automatically unregistered when your plugin
     * is disabled, so you generally do not need to manually
     * unregister your commands. This would be more for if you want
     * to unregister commands during run time such as if the commands
     * were disabled via a command</p>
     *
     * @param plugin plugin to unregister for
     */
    public static void unregisterCommands(Plugin plugin) {
        List<String> list = plugins.get(plugin);
        if (list == null) return;

        // Unregister everything for the plugin
        for (String command : list) {
            commands.remove(command);
        }
        plugins.remove(plugin);
        configs.remove(plugin);
    }

    /**
     * <p>Unregisters all commands for all plugins</p>
     * <p>If no commands were registered, this does nothing</p>
     *
     * <p>You shouldn't use this method as it's meant for MCCore to
     * clear the data when it's disabled.</p>
     */
    public static void unregisterAll() {
        plugins.clear();
        commands.clear();
        configs.clear();
    }

    /**
     * <p>Retrieves a registered command by name</p>
     * <p>If there is no registered command with the name,
     * this will return null instead.</p>
     *
     * @param name command name
     * @return     registered command or null if not found
     */
    public static ConfigurableCommand getCommand(String name) {
        return commands.get(name.toLowerCase());
    }

    /**
     * <p>Retrieves the command configuration for the plugin</p>
     * <p>If a configuration for the plugin hasn't yet been set
     * up, this will create one.</p>
     * <p>This is primarily for the commands saving/loading their
     * own data and generally doesn't need to be used.</p>
     *
     * @param plugin plugin to get the config for
     * @return       command configuration
     */
    public static Config getConfig(JavaPlugin plugin) {
        if (!configs.containsKey(plugin)) {
            configs.put(plugin, new Config(plugin, "commands"));
        }
        return configs.get(plugin);
    }

    /**
     * <p>Displays the usage help for the command, showing only the commands
     * that the sender can use.</p>
     *
     * <p>The usage display adjusts to the sender, having different spacing
     * for players and the console.</p>
     *
     * <p>When on 1.7.9+, players also can see buttons to navigate through
     * the help menu as long as it is included in the format.</p>
     *
     * @param c      command to show usage for
     * @param sender sender of the command
     */
    public static void displayUsage(ConfigurableCommand c, CommandSender sender) {
        displayUsage(c, sender, 1);
    }

    /**
     * <p>Displays the usage help for the command, showing only the commands
     * that the sender can use.</p>
     *
     * <p>The usage display adjusts to the sender, having different spacing
     * for players and the console.</p>
     *
     * <p>When on 1.7.9+, players also can see buttons to navigate through
     * the help menu as long as it is included in the format.</p>
     *
     * @param c      command to show usage for
     * @param sender sender of the command
     * @param page   page to display
     */
    public static void displayUsage(ConfigurableCommand c, CommandSender sender, int page) {

        // Get the first command in the chain that can be used
        while (c.hasParent() && !c.canUseCommand(sender)) {
            c = c.getParent();
        }

        // Only show something if the command be used
        if (c.canUseCommand(sender)) {
            if (c.isContainer()) {
                displayGeneralUsage(c, sender, page);
            }
            else displaySpecificUsage(c, sender);
        }
    }

    private static void displaySpecificUsage(ConfigurableCommand c, CommandSender sender) {
        String command = "/" + c.toString() + " " + c.getArgs().replace("[", optionalArgs + "[").replace("<", requiredArgs + "<");
        for (String line : commandUsage) {
            if (line.contains("{description}")) {
                if (sender instanceof Player) {
                    List<String> dLines = TextSizer.split(c.getDescription(), 320 - TextSizer.measureString(line.replace("{description}", "")));
                    for (String d : dLines) {
                        sender.sendMessage(line.replace("{description}", d));
                    }
                }
                else {
                    List<String> dLines = TextSplitter.getLines(c.getDescription(), 60 - ChatColor.stripColor(line.replace("{description}", "")).length());
                    for (String d : dLines) {
                        sender.sendMessage(line.replace("{description}", d));
                    }
                }
            }
            else sender.sendMessage(line.replace("{command}", command));
        }
    }

    private static void displayGeneralUsage(ConfigurableCommand c, CommandSender sender, int page) {
        List<String> keys = c.getUsableCommands(sender);

        // There are no usable commands
        if (keys.size() == 0) {
            sender.sendMessage(noCommands);
            return;
        }

        Collections.sort(keys);

        // Get number of entries
        int entries;
        if (sender instanceof Player) {
            if (VersionManager.isVersionAtLeast(VersionManager.MC_1_7_9_MIN)) {
                entries = playerSize - helpWithButton.size() + 1;
                if (entries >= keys.size() + 1) entries++;
            }
            else entries = playerSize - helpNoButton.size() + 1;
        }
        else entries = consoleSize - helpNoButton.size() + 1;
        if (entries < 1) entries = 1;

        // Limit the page number to within bounds
        int maxPage = (keys.size() + entries - 1) / entries;
        if (page > maxPage) page = maxPage;
        if (page < 1) page = 1;

        // Get the strings to replace filters with
        String pageString = maxPage == 1 ? "" : pageFormat.replace("{page}", page + "").replace("{max}", maxPage + "");
        String titleString = "/" + c.toString();

        // Get the maximum length
        int maxSize = 0;
        int index = 0;
        for (String key : keys) {
            index++;
            if (index <= (page - 1) * entries || index > page * entries) continue;
            String args = c.getSubCommand(key).getArgs();
            int size = sender instanceof Player ? TextSizer.measureString(key + " " + args) : (key + " " + args).length();
            if (size > maxSize) maxSize = size;
        }
        if (sender instanceof Player) maxSize += 4;
        else maxSize += 1;

        // Player usage post-1.7.9
        if (VersionManager.isVersionAtLeast(VersionManager.MC_1_7_9_MIN) && sender instanceof Player) {

            // Button JSON
            String ends = "PreviousNext";
            String spacing = TextSizer.expand(" ", 320 - TextSizer.measureString(ends), true);
            if (!spacing.startsWith(" ")) spacing = spacing.substring(spacing.indexOf(' '));
            String buttons = "tellraw "
                    + sender.getName()
                    + " {\"text\":\"\",\"extra\":[{\"text\":\""
                    + prevPage
                    + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\""
                    + titleString
                    + " "
                    + ((page + maxPage - 2) % maxPage + 1)
                    + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""
                    + prevPageHover
                    + "\"}},{\"text\":\""
                    + spacing
                    + "\"},{\"text\":\""
                    + nextPage
                    + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\""
                    + titleString
                    + " "
                    + ((page % maxPage + 1))
                    + "\"},hoverEvent\":{\"action\":\"show_text\",\"value\":\""
                    + nextPageHover
                    + "\"}}]}";

            // Display the usage
            for (String line : helpWithButton) {
                if (line.contains("{commands}")) {
                    index = 0;
                    for (String key : keys) {
                        index++;
                        if (index <= (page - 1) * entries || index > page * entries) continue;

                        ConfigurableCommand sub = c.getSubCommand(key);
                        String args = sub.getArgs().replace("[", optionalArgs + "[").replace("<", requiredArgs + "<");
                        sender.sendMessage(line.replace("{commands}",
                                command + "/" + c.toString() + " "
                                + TextSizer.expand(key + " " + args, maxSize, false)
                                + ChatColor.GRAY + "- " + description + sub.getDescription()));
                    }
                }
                else if (line.contains("{buttons}")) {
                    if (maxPage > 1) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), buttons);
                    }
                }
                else sender.sendMessage(line.replace("{title}", titleString).replace("{page}", pageString));
            }
        }

        // Player usage pre-1.7.9
        else if (sender instanceof Player) {
            for (String line : helpNoButton) {
                if (line.contains("{commands}")) {
                    index = 0;
                    for (String key : keys) {
                        index++;
                        if (index <= (page - 1) * entries || index > page * entries) continue;

                        ConfigurableCommand sub = c.getSubCommand(key);
                        String args = sub.getArgs().replace("[", optionalArgs + "[").replace("<", requiredArgs + "<");
                        sender.sendMessage(line.replace("{commands}",
                                command + "/" + c.toString() + " "
                                        + TextSizer.expand(key + " " + args, maxSize, false)
                                        + ChatColor.GRAY + "- " + description + sub.getDescription()));
                    }
                }
                else sender.sendMessage(line.replace("{title}", titleString).replace("{page}", pageString));
            }
        }

        // Console usage
        else {
            for (String line : helpNoButton) {
                if (line.contains("{commands}")) {
                    index = 0;
                    for (String key : keys) {
                        index++;
                        if (index <= (page - 1) * entries || index > page * entries) continue;

                        ConfigurableCommand sub = c.getSubCommand(key);
                        String args = sub.getArgs().replace("[", optionalArgs + "[").replace("<", requiredArgs + "<");
                        sender.sendMessage(line.replace("{commands}",
                                command + "/" + c.toString() + " "
                                        + TextSizer.expandConsole(key + " " + args, maxSize, false)
                                        + ChatColor.GRAY + "- " + description + sub.getDescription()));
                    }
                }
                else sender.sendMessage(line.replace("{title}", titleString).replace("{page}", pageString));
            }
        }
    }

    /**
     * <p>Trims the first element off of an args array</p>
     * <p>This is used by the API to handle command execution. You
     * generally will not need to use this.</p>
     *
     * @param args initial args
     * @return     trimmed args
     */
    public static String[] trimArgs(String[] args) {

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
     * <p>Loads options for displaying command usages from the configuration</p>
     * <p>This is called by MCCore automatically and doesn't need to be called by
     * you. If you want to override MCCore's default configuration, you could use
     * this method to apply a different one as long as it uses the same format.</p>
     *
     * @param config configuration to load from
     */
    public static void loadOptions(ConfigurationSection config) {

        // Sizes
        playerSize = config.getInt(PLAYER_SIZE, playerSize);
        consoleSize = config.getInt(CONSOLE_SIZE, consoleSize);

        // Colors
        command = getColor(config.getString(COMMAND, null), command);
        requiredArgs = getColor(config.getString(REQUIRED, null), requiredArgs);
        optionalArgs = getColor(config.getString(OPTIONAL, null), optionalArgs);
        description = getColor(config.getString(DESCRIPTION, null), description);

        // Formats
        pageFormat = TextFormatter.colorString(config.getString(PAGE, pageFormat));
        noDescription = TextFormatter.colorString(config.getString(NO_DESCRIPTION, noDescription));
        noCommands = TextFormatter.colorString(config.getString(NO_COMMANDS, noCommands));
        nextPage = TextFormatter.colorString(config.getString(NEXT_PAGE, nextPage));
        prevPage = TextFormatter.colorString(config.getString(PREV_PAGE, prevPage));
        nextPageHover = TextFormatter.colorString(config.getString(NEXT_PAGE_HOVER, nextPageHover));
        prevPageHover = TextFormatter.colorString(config.getString(PREV_PAGE_HOVER, prevPageHover));
        helpWithButton = config.contains(HELP_BUTTON) && config.isList(HELP_BUTTON)
                ? TextFormatter.colorStringList(config.getStringList(HELP_BUTTON))
                : helpWithButton;
        helpNoButton = config.contains(HELP_NO_BUTTON) && config.isList(HELP_NO_BUTTON)
                ? TextFormatter.colorStringList(config.getStringList(HELP_NO_BUTTON))
                : helpNoButton;
        commandUsage = config.contains(COMMAND_USAGE) && config.isList(COMMAND_USAGE)
                ? TextFormatter.colorStringList(config.getStringList(COMMAND_USAGE))
                : commandUsage;
    }

    /**
     * <p>Gets a ChatColor from the string, falling back to the default color
     * in case the string is invalid or null.</p>
     *
     * @param input        text to parse
     * @param defaultColor fallback color
     * @return             resulting color
     */
    private static ChatColor getColor(String input, ChatColor defaultColor) {
        if (input == null) return defaultColor;
        else {
            try {
                return ChatColor.valueOf(input.toUpperCase().replace(' ', '_'));
            }
            catch (Exception ex) {
                return defaultColor;
            }
        }
    }
}
