/**
 * MCCore
 * com.rit.sucy.text.TextSplitter
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

import java.util.ArrayList;

/**
 * Manages text for scoreboards
 */
public class TextSplitter {

    private static final int MIN_WRAP = 4;
    private static final int SPACE = 1;
    private static final int DASH = 1;

    /**
     * Smart-splits messages to wrap for the scoreboard
     *
     * @param message   message to split
     * @param maxLength number of characters per line
     *
     * @return lines of text
     */
    public static ArrayList<String> getLines(String message, int maxLength) {

        // Make sure the message isn't null
        if (message == null) throw new IllegalArgumentException("Null string");

        ArrayList<String> lines = new ArrayList<String>();
        String[] pieces;
        if (message.contains(" ")) pieces = message.split(" ");
        else pieces = new String[]{message};
        String line = "";
        int offset = 0;

        for (int i = 0; i < pieces.length; i++) {

            // When its the first element
            if (line.length() == 0) {

                // If its longer than a line, make the next 15 characters a line
                if (pieces[i].length() - offset > maxLength) {
                    lines.add(pieces[i].substring(offset, maxLength - DASH + offset) + "-");
                    offset += maxLength - DASH;
                    i--;
                }

                // Otherwise just add it to the line
                else {
                    line += pieces[i].substring(offset);
                    offset = 0;
                }
            }

            // When it isn't the second element
            else {

                // When it exceeds the line limit
                if (pieces[i].length() + line.length() > maxLength - SPACE) {

                    // If it would hardly wrap, just wait for the next line
                    if (line.length() > maxLength - MIN_WRAP || line.length() + pieces[i].length() < maxLength - SPACE - DASH + MIN_WRAP) {
                        lines.add(line);
                        line = "";
                        i--;
                    }

                    // Otherwise, wrap the word using a dash to separate the pieces
                    else {
                        offset = maxLength - DASH - SPACE - line.length();
                        line += " " + pieces[i].substring(0, maxLength - DASH - SPACE - line.length()) + "-";
                        lines.add(line);
                        line = "";
                        i--;
                    }
                }

                // Otherwise just add it to the line
                else line += " " + pieces[i].substring(offset);
            }
        }

        // Add any leftover text
        lines.add(line);

        return lines;
    }
}
