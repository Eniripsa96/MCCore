package com.rit.sucy.text;

import java.util.ArrayList;

/**
 * Manages text for scoreboards
 */
public class TextSplitter {

    /**
     * Smart-splits messages to wrap for the scoreboard
     *
     * @param message message to split
     * @return        lines of text
     */
    public static ArrayList<String> getLines(String message) {

        // Make sure the message isn't null
        if (message == null) throw new IllegalArgumentException("Null string");

        ArrayList<String> lines = new ArrayList<String>();
        String[] pieces;
        if (message.contains(" ")) pieces = message.split(" ");
        else pieces = new String[] { message };
        String line = "";
        int offset = 0;

        for (int i = 0; i < pieces.length; i++) {

            // When its the first element
            if (line.length() == 0) {

                // If its longer than a line, make the next 15 characters a line
                if (pieces[i].length() - offset > 16) {
                    lines.add(pieces[i].substring(offset, 15 + offset) + "-");
                    offset += 15;
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
                if (pieces[i].length() + line.length() > 15) {

                    // If it would hardly wrap, just wait for the next line
                    if (line.length() > 12 || line.length() + pieces[i].length() < 18) {
                        lines.add(line);
                        line = "";
                        i--;
                    }

                    // Otherwise, wrap the word using a dash to separate the pieces
                    else {
                        offset = 14 - line.length();
                        line += " " + pieces[i].substring(0, 14 - line.length()) + "-";
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
