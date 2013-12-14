package com.rit.sucy.chat;

import com.rit.sucy.config.ISavable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains player data for the chat
 */
public class ChatData implements ISavable {

    ArrayList<Prefix> pluginPrefixes;
    ArrayList<Prefix> unlockedPrefixes;
    Prefix playerPrefix;
    String playerName;
    String displayName;

    /**
     * Initial constructor
     *
     * @param playerName name of the player
     */
    ChatData(String playerName) {
        pluginPrefixes = new ArrayList<Prefix>();
        unlockedPrefixes = new ArrayList<Prefix>();
        this.playerName = playerName;
        this.displayName = playerName;
    }

    /**
     * Constructor
     *
     * @param config     config to load data from
     * @param playerName the name of the player to store the data of
     */
    ChatData(ConfigurationSection config, String playerName) {
        this(playerName);
        load(config);
    }

    /**
     * Retrieves the player's chat tag (e.g. <[Prefix] DisplayName>)
     *
     * @return the player's chat tag
     */
    public String getChatTag() {
        String tag = "<";
        for (Prefix prefix : pluginPrefixes) {
            tag += prefix.braceColor + "[" + prefix.text + prefix.braceColor + "]";
        }
        if (playerPrefix != null) tag += playerPrefix.braceColor + "[" + playerPrefix.text + playerPrefix.braceColor + "]";
        return tag + ChatColor.WHITE + " " + displayName + ">";
    }

    /**
     * @return player display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Retrieves the current prefix used by the player
     *
     * @return prefix chosen by player or null if none are applied
     */
    public Prefix getPrefix() {
        return playerPrefix;
    }

    /**
     * Retrieves the current prefix for the given plugin
     *
     * @param pluginName name of the plugin
     * @return           prefix currently assigned by the plugin
     */
    public Prefix getPrefix(String pluginName) {

        // Look for the prefix
        for (Prefix prefix : pluginPrefixes) {
            if (prefix.pluginName.equalsIgnoreCase(pluginName)) return prefix;
        }

        // Prefix not found
        return null;
    }

    /**
     * Retrieves a list of all prefixes unlocked through a plugin
     *
     * @param pluginName name of the plugin that gives the prefixes
     * @return           list of all unlocked prefixes from the plugin
     */
    public List<Prefix> getUnlockedPrefixes(String pluginName) {
        List<Prefix> prefixes = new ArrayList<Prefix>();
        for (Prefix prefix : unlockedPrefixes) if (prefix.pluginName.equalsIgnoreCase(pluginName)) prefixes.add(prefix);
        return prefixes;
    }

    /**
     * Sets the player's display name
     *
     * @param displayName display name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Unlocks the given prefix
     *
     * @param prefix prefix to unlock
     * @param apply  whether or not to apply it right away
     * @return       true if successful, false if already unlocked
     */
    public boolean unlockPrefix(Prefix prefix, boolean apply) {
        if (hasPrefix(prefix.textWithoutColor)) return false;

        unlockedPrefixes.add(prefix);

        Player player = Bukkit.getPlayer(playerName);
        player.sendMessage("You unlocked the prefix: " + prefix.text);
        if (apply) setPrefix(prefix.textWithoutColor);
        else player.sendMessage("    - Equip it by typing: /setprefix " + prefix.textWithoutColor);
        return true;
    }

    /**
     * Removes the prefix from the player, making them unable to use it anymore
     *
     * @param pluginName name of the plugin that gives the prefix
     * @param prefixText prefix text (with or without color)
     * @return           true if removed, false if it wasn't unlocked
     */
    public boolean removePrefix(String pluginName, String prefixText) {
        prefixText = ChatColor.stripColor(prefixText);

        for (Prefix prefix : unlockedPrefixes) {
            if (prefix.textWithoutColor.equalsIgnoreCase(prefixText)
                    && prefix.pluginName.equalsIgnoreCase(pluginName)) {
                unlockedPrefixes.remove(prefix);
                Bukkit.getPlayer(playerName).sendMessage("You lost the prefix " + prefix.text);
                if (playerPrefix == prefix) {
                    playerPrefix = null;
                    updateDisplayName();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Removes all prefixes unlocked through the given plugin
     *
     * @param pluginName name of the plugin with the prefixes
     */
    public void removePrefixes(String pluginName) {
        for (int i = 0; i < unlockedPrefixes.size(); i++) {
            if (unlockedPrefixes.get(i).pluginName.equalsIgnoreCase(pluginName)) {
                if (unlockedPrefixes.get(i) == playerPrefix) {
                    playerPrefix = null;
                    updateDisplayName();
                }
                unlockedPrefixes.remove(i);
                Bukkit.getPlayer(playerName).sendMessage("You lost the prefix " + unlockedPrefixes.get(i).text);
                i--;
            }
        }
    }

    /**
     * Sets the prefix for a plugin
     * Use this only for game-changing prefixes. If it doesn't need
     * to be displayed, use unlockPrefix(CPrefix, boolean) instead.
     *
     * @param prefix prefix data
     */
    public void setPluginPrefix(Prefix prefix) {

        // Clear any previous prefix by the plugin
        Prefix currentPrefix = getPrefix(prefix.pluginName);
        if (currentPrefix != null) pluginPrefixes.remove(currentPrefix);

        pluginPrefixes.add(0, prefix);
        updateDisplayName();
    }

    /**
     * Clears the prefix assigned to the plugin with the given name
     *
     * @param pluginName name of the plugin with the prefix
     */
    public void clearPluginPrefix(String pluginName) {
        Prefix prefix = getPrefix(pluginName);
        if (prefix != null) pluginPrefixes.remove(prefix);
        updateDisplayName();
    }

    /**
     * Checks if the prefix has been obtained for the given plugin
     *
     * @param prefixText the text of the prefix (with or without color)
     * @return           true if the prefix is unlocked
     */
    public boolean hasPrefix(String prefixText) {
        for (Prefix unlocked : unlockedPrefixes) {
            if (unlocked.textWithoutColor.equalsIgnoreCase(prefixText))
                return true;
        }
        return false;
    }

    /**
     * Sets the player's personal prefix
     *
     * @param prefix text of the prefix (with or without color)
     */
    void setPrefix(String prefix) {
        for (Prefix unlocked : unlockedPrefixes) {
            if (unlocked.textWithoutColor.equalsIgnoreCase(prefix)) {
                playerPrefix = unlocked;
                updateDisplayName();
                return;
            }
        }
    }

    /**
     * Checks whether or not the prefix is unlocked
     *
     * @param pluginName the plugin that gives the prefix
     * @param prefixText prefix (with or without color)
     * @return           true if the prefix is unlocked, false otherwise
     */
    boolean isPrefixAvailable(String pluginName, String prefixText) {
        prefixText = ChatColor.stripColor(prefixText);

        // Search through the prefixes for the one requested
        for (Prefix prefix : unlockedPrefixes) {
            if (prefix.pluginName.equalsIgnoreCase(pluginName)
                    && prefix.textWithoutColor.equalsIgnoreCase(prefixText)) {
                return true;
            }
        }

        // Prefix wasn't found
        return false;
    }

    /**
     * Updates the display name
     */
    void updateDisplayName() {

        String name = "";

        // Add each of the active prefixes
        for (Prefix prefix : pluginPrefixes) {
            if (prefix == null) continue;
            name += prefix.braceColor + "[" + prefix.text + prefix.braceColor + "]";
        }
        if (playerPrefix != null) name += playerPrefix.braceColor + "[" + playerPrefix.text + playerPrefix.braceColor + "]";
        if (name.length() > 0) name += " ";

        // Set the display name to the prefixes plus the regular display name
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            if (displayName.equalsIgnoreCase(playerName)) {
                displayName = player.getName();
            }
            player.setDisplayName(
                name + ChatColor.WHITE + displayName);
        }
    }

    /**
     * Saves the player data to the config file
     */
    public void save(ConfigurationSection config, String base) {
        config.set(base + "name", displayName);
        config.set(base + "unlocked", unlockedList());
        config.set(base + "prefixes", setList());
        if (playerPrefix != null) config.set(base + "prefix", playerPrefix.toString());
    }

    /**
     * Loads player data from the config file
     */
    void load(ConfigurationSection config) {

        // Plugin prefixes
        for (String prefix : config.getStringList(playerName.toLowerCase() + ".prefixes")) {
            pluginPrefixes.add(new Prefix(prefix));
        }

        // Unlocked prefixes
        unlockedPrefixes.clear();
        List<String> unlocked = config.getStringList(playerName.toLowerCase() + ".unlocked");
        for (String prefix : unlocked) unlockedPrefixes.add(new Prefix(prefix));

        if (config.contains(playerName.toLowerCase() + ".name")) {
            displayName = config.getString(playerName.toLowerCase() + ".name");
        }
        else displayName = playerName.toLowerCase();

        if (config.contains(playerName + ".prefix")) {
            playerPrefix = new Prefix(config.getString(playerName + ".prefix"));
        }

        updateDisplayName();
    }

    /**
     * Retrieves the set prefixes as a list of serialized strings
     *
     * @return list of all set prefixes as serialized strings
     */
    private List<String> setList() {
        ArrayList<String> list = new ArrayList<String>();
        for (Prefix prefix : pluginPrefixes) list.add(prefix == null ? null : prefix.toString());
        return list;
    }

    /**
     * Retrieves the unlocked prefixes as a list of serialized strings
     *
     * @return list of all unlocked prefixes as serialized strings
     */
    private List<String> unlockedList() {
        ArrayList<String> list = new ArrayList<String>();
        for (Prefix prefix : unlockedPrefixes) list.add(prefix.toString());
        return list;
    }
}
