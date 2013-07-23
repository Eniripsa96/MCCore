package com.rit.sucy.scoreboard;

import com.rit.sucy.text.TextSplitter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;

/**
 * Manages displaying messages via a scoreboard to a player
 */
public class TextBoard extends Board {

    private final ArrayList<String> messages = new ArrayList<String>();
    private final boolean separateMessages;

    /**
     * Constructor
     *
     * @param title         scoreboard title
     * @param separateMessages whether or not to add lines between messages
     */
    public TextBoard(String title, String plugin, boolean separateMessages) {
        super(title, plugin);
        this.separateMessages = separateMessages;
    }

    /**
     * Adds a message to the scoreboard after smart-splitting it
     *
     * @param message message to add
     */
    public void addMessage(String message) {
        ArrayList<String> result = TextSplitter.getLines(message, 16);
        if (separateMessages && messages.size() > 0) {
            String separator = ChatColor.DARK_GRAY + "=---------------";
            while (messages.contains(separator) && !separator.equals(ChatColor.DARK_GRAY + "---------------="))
                separator = separator.replace("=-", "-=");
            if (!messages.contains(separator))
                messages.add(separator);
        }
        for (String line : result) {
            if (messages.contains(line)) continue;
            messages.add(line);
        }
        update();
    }

    /**
     * Updates the scoreboard
     */
    public void update() {
        while (messages.size() > 15) {
            scoreboard.resetScores(Bukkit.getOfflinePlayer(messages.get(0)));
            messages.remove(0);
        }
        int index = 15;
        for (String message : messages) {
            obj.getScore(Bukkit.getOfflinePlayer(message)).setScore(index--);
        }
    }
}
