/**
 * MCCore
 * com.rit.sucy.gui.MapFont
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
package com.rit.sucy.gui;

import org.bukkit.Bukkit;
import org.bukkit.util.Vector;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Represents a font that can more efficiently draw to a map canvas
 */
public class MapFont {
    private final HashMap<Character, MapChar> chars = new HashMap<Character, MapChar>();
    private Font font;
    private int space;

    /**
     * Initializes a MapFont using a Font template
     *
     * @param font  font template
     * @param space spacing between characters
     */
    public MapFont(Font font, int space) {
        this.font = font;
        this.space = space;
    }

    /**
     * Retrieves the font wrapped by this object
     *
     * @return wrapped font
     */
    public Font getFont() {
        return font;
    }

    /**
     * Retrieves the spacing between letters for
     * this font
     *
     * @return spacing between letters
     */
    public int getSpace() {
        return space;
    }

    /**
     * Measures the dimensions of a string
     *
     * @param str   string to measure
     *
     * @return the dimensions of the string { width, height, yOffset }
     */
    public int[] measureString(String str) {
        int minY = 999;
        int maxY = 0;
        int width = 0;
        for (int i = 0; i < str.length(); i++) {
            MapChar c = getChar(str.charAt(i));
            minY = Math.min(minY, c.getBase());
            maxY = Math.max(maxY, c.getBase() + c.getHeight());
            width += c.getWidth() + getSpace();
        }
        return new int[]{width, maxY - minY, minY};
    }

    /**
     * Gets data for a character from the font. If the character is not initialized,
     * it will be initialized first.
     *
     * @param c character to get the data of
     *
     * @return character data for the font
     */
    public MapChar getChar(char c) {
        if (!font.canDisplay(c)) return null;
        if (!chars.containsKey(c)) {
            if (c == '.') {
                chars.put(c, new MapChar(new boolean[]{true}, 1, 1, 0));
            } else {
                BufferedImage buffer = new BufferedImage(32, 32, 2);
                Graphics2D g = buffer.createGraphics();
                g.setFont(font);
                Rectangle2D bounds = font.createGlyphVector(g.getFontRenderContext(), c + "").getPixelBounds(g.getFontRenderContext(), 0, 0);
                if (bounds.getWidth() <= 0 || bounds.getHeight() <= 0 || bounds.getWidth() > 32 || bounds.getHeight() > 32) {
                    chars.put(c, new MapChar(new boolean[]{false}, 1, 1, 0));
                } else {
                    g.drawString(c + "", 0, -(int) bounds.getY());
                    boolean[] data = new boolean[(int) (bounds.getWidth() * bounds.getHeight())];
                    int[] pixels = new int[data.length * 4];
                    buffer.getData().getPixels(0, 0, (int) bounds.getWidth(), (int) bounds.getHeight(), pixels);
                    for (int j = 0; j < bounds.getHeight(); j++) {
                        for (int i = 0; i < bounds.getWidth(); i++) {
                            data[(j * (int) bounds.getWidth()) + i] = pixels[(j * 4 * (int) bounds.getWidth()) + i * 4] > 0;
                        }
                    }
                    chars.put(c, new MapChar(data, (int) bounds.getWidth(), (int) bounds.getHeight(), (int) bounds.getY()));
                }
            }
        }
        return chars.get(c);
    }

    /**
     * Represents the per-pixel data of a character
     */
    class MapChar {
        private boolean[] data;
        private int width;
        private int height;
        private int base;

        private MapChar(boolean[] data, int w, int h, int b) {
            this.data = data;
            this.width = w;
            this.height = h;
            this.base = b;
        }

        /**
         * Retrieves the pixel data of the character
         *
         * @return pixel data
         */
        public boolean[] getData() {
            return data;
        }

        /**
         * Retrieves the width of the character
         *
         * @return character width
         */
        public int getWidth() {
            return width;
        }

        /**
         * Retrieves the height of the character
         *
         * @return character height
         */
        public int getHeight() {
            return height;
        }

        /**
         * Retrieves the baseline offset of the character
         *
         * @return baseline offset
         */
        public int getBase() {
            return base;
        }
    }
}
