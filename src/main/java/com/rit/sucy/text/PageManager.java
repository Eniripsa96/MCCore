/**
 * MCCore
 * com.rit.sucy.text.PageManager
 * <p/>
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2014 Steven Sucy
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software") to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.rit.sucy.text;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.*;

/**
 * <p>Handles displaying pages of options or details</p>
 */
public class PageManager {

    private static final String BREAK = ChatColor.DARK_GRAY + "-----------------------------------------------------";

    private String title;
    private boolean breakLines;
    private List<String> lines;

    /**
     * Constructor
     *
     * @param title      title for the display
     * @param breakLines whether or not to use break lines before and after the display
     */
    public PageManager(String title, boolean breakLines) {
        this.title = title;
        this.breakLines = breakLines;
        this.lines = new ArrayList<String>();
    }

    /**
     * Constructor
     *
     * @param title      title for the display
     * @param breakLines whether or not to use break lines before and after the display
     * @param lines      lines to display
     */
    public PageManager(String title, boolean breakLines, List<String> lines) {
        this.title = title;
        this.breakLines = breakLines;
        this.lines = lines;
    }

    /**
     * Constructor
     *
     * @param title      title for the display
     * @param breakLines whether or not to use break lines before and after the display
     * @param lines      lines to display
     */
    public PageManager(String title, boolean breakLines, String... lines) {
        this.title = title;
        this.breakLines = breakLines;
        this.lines = Arrays.asList(lines);
    }

    /**
     * Displays the first page
     *
     * @param recipient receiver of the display
     */
    public void display(CommandSender recipient) {
        display(recipient, 1);
    }

    /**
     * Displays a specified page
     *
     * @param recipient receiver of the display
     * @param page      page to display
     */
    public void display(CommandSender recipient, int page) {

        // Get the lines per page
        int lineCount = 9 - (breakLines ? 2 : 0);

        // Get the maximum page
        int maxPage = (lines.size() + lineCount - 1) / lineCount;
        if (maxPage < 1) maxPage = 1;

        // Limit the specified page
        if (page > maxPage) page = maxPage;
        if (page < 1) page = 1;

        // Display first break line if applicable
        if (breakLines) recipient.sendMessage(BREAK);

        // Display the title
        recipient.sendMessage(title + (maxPage > 1 ? ChatColor.GRAY + "(" + page + "/" + maxPage + ")" : ""));

        // Display the lines
        int index = -1;
        for (String line : lines) {
            index++;
            if (index < page * lineCount - lineCount || index >= page * lineCount) continue;
            recipient.sendMessage(line);
        }

        // Display end break line if applicable
        if (breakLines) recipient.sendMessage(BREAK);
    }

    /**
     * Adds a new line
     *
     * @param line line to add
     */
    public void addLine(String line) {
        lines.add(line);
    }

    /**
     * Adds multiple lines
     *
     * @param lines lines to add
     */
    public void addLines(String... lines) {
        this.lines.addAll(Arrays.asList(lines));
    }

    /**
     * Adds multiple lines
     *
     * @param lines lines to add
     */
    public void addLines(List<String> lines) {
        this.lines.addAll(lines);
    }

    /**
     * Sorts the lines being displayed
     */
    public void sortLines() {
        Collections.sort(lines);
    }

    /**
     * Sorts the lines being displayed using the comparator
     *
     * @param comparator comparator to use
     */
    public void sortLines(Comparator<String> comparator) {
        Collections.sort(lines, comparator);
    }
}
