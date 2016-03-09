/**
 * MCCore
 * com.rit.sucy.scoreboard.TextBoard
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
package com.rit.sucy.scoreboard;

import com.rit.sucy.text.TextSplitter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;

/**
 * Manages displaying messages via a scoreboard to a player
 */
public class TextBoard extends Board
{

    private final ArrayList<String> messages = new ArrayList<String>();
    private final boolean separateMessages;

    /**
     * Constructor
     *
     * @param title            scoreboard title
     * @param separateMessages whether or not to add lines between messages
     */
    public TextBoard(String title, String plugin, boolean separateMessages)
    {
        super(title, plugin);
        this.separateMessages = separateMessages;
    }

    /**
     * Adds a message to the scoreboard after smart-splitting it
     *
     * @param message message to add
     */
    public void addMessage(String message)
    {
        ArrayList<String> result = TextSplitter.getLines(message, 16);
        if (separateMessages && messages.size() > 0)
        {
            String separator = ChatColor.DARK_GRAY + "=---------------";
            while (messages.contains(separator) && !separator.equals(ChatColor.DARK_GRAY + "---------------="))
                separator = separator.replace("=-", "-=");
            if (!messages.contains(separator))
                messages.add(separator);
        }
        for (String line : result)
        {
            if (messages.contains(line)) continue;
            messages.add(line);
        }
        update();
    }

    /**
     * Updates the scoreboard
     */
    public void update()
    {
        while (messages.size() > 15)
        {
            scoreboard.resetScores(Bukkit.getOfflinePlayer(messages.get(0)));
            messages.remove(0);
        }
        int index = 15;
        for (String message : messages)
        {
            obj.getScore(Bukkit.getOfflinePlayer(message)).setScore(index--);
        }
    }
}
