package com.rit.sucy.commands;

import com.avaje.ebean.text.StringFormatter;
import com.rit.sucy.MCCore;
import com.rit.sucy.config.CommentedConfig;
import com.rit.sucy.config.CustomFilter;
import com.rit.sucy.config.parse.DataSection;
import com.rit.sucy.text.TextFormatter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>A command that is able to be modified via configuration</p>
 * <p/>
 * <p>To set up hierarchies of commands, simply start with your root command,
 * then create your sub commands, adding each to the root.</p>
 * <p/>
 * <p>Example:</p>
 * <code>
 * <p/>
 * // Root commands<br/>
 * ConfigurableCommand root = new ConfigurableCommand(this, "root", SenderType.ANYONE);<br/>
 * ConfigurableCommand group;<br/>
 * <br/>
 * // Add sub commands<br/>
 * root.addSubCommands(<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;new ConfigurableCommand(this, "list", SenderType.ANYONE, new ListFunction(), "Lists available things", "", "perm.list"),<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;group = new ConfigurableCommand(this, "group", SenderType.ANYONE, "Handles group functions")<br/>
 * );<br/>
 * group.addSubCommands(<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;new ConfigurableCommand(this, "add", SenderType.ANYONE, new AddFunction(), "Adds a member to a group", "<player>", "perm.add"),<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;new ConfigurableCommand(this, "remove", SenderType.ANYONE, new RemoveFunction(), "Removes a member from a group", "<player>", "perm.remove")<br/>
 * );<br/>
 * <br/>
 * // Register everything<br/>
 * CommandManager.registerCommand(root);
 * <p/>
 * </code>
 */
public class ConfigurableCommand extends Command
{

    private static final String SENDER_KEY      = "sender";
    private static final String ENABLED_KEY     = "enabled";
    private static final String DESCRIPTION_KEY = "description";
    private static final String PERMISSION_KEY  = "permission";
    private static final String NAME_KEY        = "name";
    private static final String ARGS_KEY        = "args";
    private static final String MESSAGES_KEY    = "messages";
    private static final String COOLDOWN_KEY    = "cooldown";

    private HashMap<String, ConfigurableCommand> subCommands = new HashMap<String, ConfigurableCommand>();
    private HashMap<String, String>              messages    = new HashMap<String, String>();

    private JavaPlugin          plugin;
    private ConfigurableCommand parent;
    private IFunction           function;
    private SenderType          senderType;
    private String              description;
    private String              permission;
    private String              name;
    private String              args;
    private String              key;
    private boolean             registered;
    private boolean             enabled;
    private int                 cooldown;
    private long                timer;

    /**************************************************************************
     Constructors
     **************************************************************************/

    /**
     * <p>Creates a new command that can only hold other commands
     * and displays a command usage for sub commands when executed.</p>
     * <p/>
     * <p>The key is used to tell commands apart and is also the default
     * name of the command.</p>
     * <p/>
     * <p>The command created by this has no default description.
     * It must be set via the configuration.</p>
     *
     * @param plugin     plugin reference
     * @param key        command key
     * @param senderType type of sender needed for the command
     */
    public ConfigurableCommand(JavaPlugin plugin, String key, SenderType senderType)
    {
        this(plugin, key, senderType, null, null, null, null);
    }

    /**
     * <p>Creates a new command that can only hold other commands
     * and displays a command usage for sub commands when executed.</p>
     * <p/>
     * <p>The key is used to tell commands apart and is also the default
     * name of the command.</p>
     *
     * @param plugin      plugin reference
     * @param key         command key
     * @param senderType  type of sender needed for the command
     * @param description default description
     */
    public ConfigurableCommand(JavaPlugin plugin, String key, SenderType senderType, String description)
    {
        this(plugin, key, senderType, null, description, null, null);
    }

    /**
     * <p>Creates a new command that performs its own action when run but
     * cannot have sub commands.</p>
     * <p/>
     * <p>The key is used to tell commands apart and is also the default
     * name of the command.</p>
     * <p/>
     * <p>The command created by this has no default description, arguments,
     * or required permission. They must be set via the configuration.</p>
     *
     * @param plugin     plugin reference
     * @param key        command key
     * @param senderType type of sender needed for the command
     * @param function   command executor
     */
    public ConfigurableCommand(JavaPlugin plugin, String key, SenderType senderType, IFunction function)
    {
        this(plugin, key, senderType, function, null, null, null);
    }

    /**
     * <p>Creates a new command that performs its own action when run but
     * cannot have sub commands.</p>
     * <p/>
     * <p>The key is used to tell commands apart and is also the default
     * name of the command.</p>
     * <p/>
     * <p>The command created by this has no default arguments or required
     * permission. They must be set via the configuration.</p>
     *
     * @param plugin      plugin reference
     * @param key         command key
     * @param senderType  type of sender needed for the command
     * @param function    command executor
     * @param description default description
     */
    public ConfigurableCommand(JavaPlugin plugin, String key, SenderType senderType, IFunction function, String description)
    {
        this(plugin, key, senderType, function, description, null, null);
    }

    /**
     * <p>Creates a new command that performs its own action when run but
     * cannot have sub commands.</p>
     * <p/>
     * <p>The key is used to tell commands apart and is also the default
     * name of the command.</p>
     * <p/>
     * <p>The command created by this has no default required permission. It
     * must be set via the configuration.</p>
     *
     * @param plugin      plugin reference
     * @param key         command key
     * @param senderType  type of sender needed for the command
     * @param function    command executor
     * @param description default description
     * @param args        default arguments
     */
    public ConfigurableCommand(JavaPlugin plugin, String key, SenderType senderType, IFunction function, String description, String args)
    {
        this(plugin, key, senderType, function, description, args, null);
    }

    /**
     * <p>Creates a new command that performs its own action when run but
     * cannot have sub commands.</p>
     * <p/>
     * <p>The key is used to tell commands apart and is also the default
     * name of the command.</p>
     *
     * @param plugin      plugin reference
     * @param key         command key
     * @param senderType  type of sender needed for the command
     * @param function    command executor
     * @param description default description
     * @param args        default arguments
     * @param permission  default required permission
     */
    public ConfigurableCommand(JavaPlugin plugin, String key, SenderType senderType, IFunction function, String description, String args, String permission)
    {
        super(key, description == null ? "" : description, "", new ArrayList<String>());
        this.plugin = plugin;
        this.senderType = senderType;
        this.function = function;
        this.registered = false;
        this.enabled = true;
        this.key = key;
        load(key, description, args, permission);
    }

    /**************************************************************************
     Data Accessors
     **************************************************************************/

    /**
     * <p>Checks whether or not this command has a description</p>
     *
     * @return true if has a description, false otherwise
     */
    public boolean hasDescription()
    {
        return description != null;
    }

    /**
     * <p>Checks whether or not this command has described arguments</p>
     *
     * @return true if it has described arguments, false otherwise
     */
    public boolean hasArguments()
    {
        return args != null;
    }

    /**
     * <p>Checks whether or not this command requires a permission</p>
     *
     * @return true if requires a permission, false otherwise
     */
    public boolean requiresPermission()
    {
        return permission != null;
    }

    /**
     * <p>Checks whether or not this command has the given sub command</p>
     * <p>This is not case-sensitive</p>
     *
     * @param name sub command name
     *
     * @return true if has the sub command, false otherwise
     */
    public boolean hasSubCommand(String name)
    {
        return subCommands.containsKey(name.toLowerCase());
    }

    /**
     * <p>Checks whether or not the sender can use this command</p>
     *
     * @param sender sender of the command
     *
     * @return true if can use, false otherwise
     */
    public boolean canUseCommand(CommandSender sender)
    {
        return enabled
               && (senderType != SenderType.PLAYER_ONLY || sender instanceof Player)
               && (senderType != SenderType.CONSOLE_ONLY || !(sender instanceof Player))
               && (permission == null || sender.hasPermission(permission) || !(sender instanceof Player));
    }

    /**
     * <p>Checks whether or not this is a root command</p>
     * <p>A "root" command would be a command with no parent command</p>
     *
     * @return true if root command, false otherwise
     */
    public boolean isRootCommand()
    {
        return parent == null;
    }

    /**
     * <p>Retrieves the name of the command</p>
     *
     * @return command name
     */
    public String getName()
    {
        return name;
    }

    /**
     * <p>Retrieves the plugin that owns this command</p>
     *
     * @return owning plugin
     */
    public JavaPlugin getPlugin()
    {
        return plugin;
    }

    /**
     * <p>Retrieves the description for this command</p>
     * <p>If this doesn't have a description, this returns null</p>
     *
     * @return required permission or null if none
     */
    public String getDescription()
    {
        return description == null ? CommandManager.getDescriptionReplacement() : description;
    }

    /**
     * <p>Retrieves the described arguments for this command</p>
     * <p>If this doesn't have any arguments described, this returns null</p>
     *
     * @return required permission or null if none
     */
    public String getArgs()
    {
        return args == null ? "" : args;
    }

    /**
     * <p>Retrieves the permission required to use this command</p>
     * <p>If this doesn't require a permission, this returns null</p>
     *
     * @return required permission or null if none
     */
    public String getPermission()
    {
        return permission;
    }

    /**
     * <p>Retrieves the type of sender required to use this command</p>
     *
     * @return required sender type
     */
    public SenderType getSenderType()
    {
        return senderType;
    }

    /**
     * <p>Retrieves the list of commands that are usable by the sender</p>
     *
     * @param sender sender of the command
     *
     * @return list of usable commands
     */
    public List<String> getUsableCommands(CommandSender sender)
    {
        ArrayList<String> keys = new ArrayList<String>();
        for (Map.Entry<String, ConfigurableCommand> entry : subCommands.entrySet())
        {
            if (entry.getValue().canUseCommand(sender))
            {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }

    /**
     * <p>Retrieves a sub command by name</p>
     * <p>If there are no sub commands with the given name,
     * this will return null instead</p>
     *
     * @param name sub command name
     *
     * @return sub command or null if not found
     */
    public ConfigurableCommand getSubCommand(String name)
    {
        return subCommands.get(name);
    }

    /**
     * <p>Checks whether or not the command has a parent command.</p>
     * <p>This command will have a parent if it was added as a sub
     * command to another command</p>
     *
     * @return true if has a parent, false otherwise
     */
    public boolean hasParent()
    {
        return parent != null;
    }

    /**
     * <p>Retrieves the parent of the command.</p>
     * <p>If this does not have a parent, this will return null.</p>
     *
     * @return parent of the command
     */
    public ConfigurableCommand getParent()
    {
        return parent;
    }

    /**
     * <p>Checks whether or not this command is a container command.</p>
     * <p>A container command is one that doesn't have a function of
     * its own but contains other commands that have functions.</p>
     *
     * @return true if a container command, false otherwise
     */
    public boolean isContainer()
    {
        return function == null;
    }

    /**
     * <p>Checks whether or not this command is a functional command.</p>
     * <p>A functional command performs a task when executed and
     * cannot hold other commands in it.</p>
     *
     * @return true if functional command, false otherwise
     */
    public boolean isFunction()
    {
        return function != null;
    }

    /**************************************************************************
     Functions
     **************************************************************************/

    /**
     * <p>Marks the command as registered so that it
     * cannot be added to other commands.</p>
     * <p>This is called on commands as they are registered
     * through the CommandManager class. You do not need
     * to use this method.</p>
     */
    public void markAsRegistered()
    {
        registered = true;
    }

    /**
     * <p>Adds a sub command to this command</p>
     * <p>The sub command cannot be a registered command</p>
     * <p>You cannot register a command if this command is attached to a function</p>
     *
     * @param command sub command to add
     *
     * @throws java.lang.IllegalStateException    when unable to add sub commands
     * @throws java.lang.IllegalArgumentException when the command is registered
     */
    public void addSubCommand(ConfigurableCommand command)
    {
        if (function != null)
            throw new IllegalStateException("A sub command cannot be added to \"/" + toString() + "\", it's already attached to a function");
        if (command.registered)
            throw new IllegalArgumentException("A registered command cannot be added to another command");

        command.parent = this;
        subCommands.put(command.name, command);
    }

    /**
     * <p>Adds multiple sub commands to this command</p>
     * <p>The sub commands cannot be a registered command</p>
     * <p>You cannot register a command if this command is attached to a function</p>
     *
     * @param commands sub commands to add
     *
     * @throws java.lang.IllegalStateException    when unable to add sub commands
     * @throws java.lang.IllegalArgumentException when the command is registered
     */
    public void addSubCommands(ConfigurableCommand... commands)
    {
        for (ConfigurableCommand command : commands)
        {
            addSubCommand(command);
        }
    }

    /**
     * <p>Bukkit executiton of the command.</p>
     * <p>Use execute(CommandSender, String[]) instead.</p>
     *
     * @param sender sender of the command
     * @param label  label of the command
     * @param args   arguments provided by the sender
     *
     * @return true
     */
    @Override
    public boolean execute(CommandSender sender, String label, String[] args)
    {
        return execute(sender, args);
    }

    /**
     * <p>Executes the command using the provided arguments</p>
     * <p>Root commands will pass the arguments onto sub commands or
     * display the command usage if the args don't match any sub commands.</p>
     *
     * @param sender sender of the command
     * @param args   arguments provided by the sender
     */
    public boolean execute(CommandSender sender, String[] args)
    {
        // Cannot use the command
        if (!canUseCommand(sender))
        {
            return false;
        }

        // Check for a cooldown
        if (cooldown > 0 && System.currentTimeMillis() - timer < cooldown * 1000)
        {
            String message = TextFormatter.colorString(MCCore.getPlugin(MCCore.class).getCommandMessage());
            int time = cooldown - (int)(System.currentTimeMillis() - timer + 999) / 1000;
            message = message.replace("{time}", "" + time);
            sender.sendMessage(message);

            return false;
        }
        timer = System.currentTimeMillis();

        // Execute the attached function if applicable
        if (function != null)
        {
            function.execute(this, plugin, sender, args);
        }

        // Otherwise search for a sub command
        else if (args.length > 0 && hasSubCommand(args[0]))
        {
            ConfigurableCommand subCommand = subCommands.get(args[0].toLowerCase());
            args = CommandManager.trimArgs(args);
            return subCommand.execute(sender, args);
        }

        // If no sub commands found, display help
        else displayHelp(sender, args);

        return true;
    }

    /**
     * <p>Displays the help for this command</p>
     * <p>This displays the first page of the usage</p>
     * <p>If this is a function command, this will display
     * the usage for this command including the arguments.</p>
     * <p>If this is a command that contains others, this will
     * display the list of sub commands and their descriptions.</p>
     *
     * @param sender sender of the command
     */
    public void displayHelp(CommandSender sender)
    {
        displayHelp(sender, 1);
    }

    /**
     * <p>Displays the help for this command according to the arguments</p>
     * <p>The displayed page is determined by the provided arguments</p>
     * <p>If this is a function command, this will display
     * the usage for this command including the arguments.</p>
     * <p>If this is a command that contains others, this will
     * display the list of sub commands and their descriptions.</p>
     *
     * @param sender sender of the command
     * @param args   arguments provided by the sender
     */
    public void displayHelp(CommandSender sender, String[] args)
    {
        int page = 1;
        if (args != null && args.length > 0)
        {
            try
            {
                page = Integer.parseInt(args[0]);
            }
            catch (Exception ex)
            {
                // Do nothing
            }
        }
        displayHelp(sender, page);
    }

    /**
     * <p>Displays the help for this command using the given page</p>
     * <p>If the page is less than one, the first page will be displayed</p>
     * <p>If the page is greater than the number of pages, the last page will be displayed</p>
     * <p>If this is a function command, this will display
     * the usage for this command including the arguments.</p>
     * <p>If this is a command that contains others, this will
     * display the list of sub commands and their descriptions.</p>
     *
     * @param sender sender of the command
     * @param page   page number
     */
    public void displayHelp(CommandSender sender, int page)
    {
        CommandManager.displayUsage(this, sender, page);
    }

    /**
     * Retrieves a message for the command, using the default and adding
     * it to the configuration if not already set.
     *
     * @param key            the message key
     * @param defaultMessage the message to use if not set
     * @param filters        filters to use on the message
     *
     * @return the message from the config or default message if not set
     */
    public String getMessage(String key, String defaultMessage, CustomFilter... filters)
    {
        // Get the configuration for this command
        CommentedConfig pluginConfig = CommandManager.getConfig(plugin);
        DataSection main = pluginConfig.getConfig();
        if (!main.has(this.key)) main.createSection(this.key);
        DataSection config = main.getSection(this.key);

        // Add it to the config if it is new
        DataSection msgSection = config.getSection(MESSAGES_KEY);
        if (msgSection == null || !msgSection.has(key))
        {
            if (msgSection == null)
            {
                msgSection = config.createSection(MESSAGES_KEY);
            }
            msgSection.set(key, defaultMessage.replace(ChatColor.COLOR_CHAR, '&'));
            pluginConfig.save();
        }

        // Apply filters before returning
        String msg = TextFormatter.colorString(msgSection.getString(key));
        for (CustomFilter filter : filters)
        {
            msg = filter.apply(msg);
        }
        return msg;
    }

    /**
     * Sends a command message to the sender if the message is not an empty string.
     *
     * @param sender         sender of the command
     * @param key            the message key
     * @param defaultMessage the message to use if not set
     * @param filters        filters to use on the message
     */
    public void sendMessage(CommandSender sender, String key, String defaultMessage, CustomFilter... filters)
    {
        String str = getMessage(key, defaultMessage, filters);
        if (str.length() > 0)
        {
            sender.sendMessage(str);
        }
    }

    /**
     * <p>Loads the command data from the configuration</p>
     * <p>This is handled automatically by MCCore. You generally
     * will not use this command unless you want to override MCCore's
     * default configuration.</p>
     *
     * @param key         command key
     * @param description default description
     * @param args        default arguments
     * @param permission  default required permission
     */
    private void load(String key, String description, String args, String permission)
    {
        // Get the configuration for this command
        CommentedConfig pluginConfig = CommandManager.getConfig(plugin);
        DataSection main = pluginConfig.getConfig();
        DataSection config = main.defaultSection(key);

        // Get the command details
        this.name = config.getString(NAME_KEY, key).toLowerCase();
        this.description = config.getString(DESCRIPTION_KEY, description);
        this.permission = config.getString(PERMISSION_KEY, permission);
        this.args = config.getString(ARGS_KEY, args);
        this.enabled = config.getBoolean(ENABLED_KEY, enabled);
        this.cooldown = config.getInt(COOLDOWN_KEY, cooldown);

        // Configurable messages
        if (config.has(MESSAGES_KEY))
        {
            DataSection msgSection = config.getSection(MESSAGES_KEY);
            for (String msgKey : msgSection.keys())
            {
                messages.put(msgKey, msgSection.getString(msgKey));
            }
        }

        // Restrict the name to not have spaces
        if (this.name.contains(" "))
        {
            String noSpaces = this.name.replace(" ", "");
            plugin.getLogger().warning("Invalid command name \"" + this.name + "\", using \"" + noSpaces + "\" instead");
            this.name = noSpaces;
        }

        // Get the sender type
        try
        {
            this.senderType = config.has(SENDER_KEY) ? SenderType.valueOf(config.getString(SENDER_KEY).toUpperCase().replace(" ", "_")) : senderType;
        }

        // Invalid sender type
        catch (Exception ex)
        {
            plugin.getLogger().warning("Invalid sender type for command \"/" + toString() + "\", using the default instead");
        }

        // Update the config with any new data
        config.set(NAME_KEY, name);
        config.set(DESCRIPTION_KEY, this.description);
        config.set(PERMISSION_KEY, this.permission);
        config.set(ARGS_KEY, args);
        config.set(SENDER_KEY, senderType.name());
        config.set(ENABLED_KEY, enabled);
        config.set(COOLDOWN_KEY, cooldown);
        pluginConfig.save();
    }

    /**
     * <p>Returns a string of the command name</p>
     * <p>If this is a sub command, this returns the parent's name
     * as well as the name of this command.</p>
     * <p>For example, if this is the command "add" and is the sub
     * command of "group", this will return "group add"</p>
     *
     * @return command path
     */
    @Override
    public String toString()
    {
        if (parent == null) return name;
        else return parent.toString() + " " + name;
    }
}
