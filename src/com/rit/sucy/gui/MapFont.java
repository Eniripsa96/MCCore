package com.rit.sucy.gui;

import org.bukkit.Bukkit;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Represents a font that can more efficiently draw to a map canvas
 */
public class MapFont
{
    private final HashMap<Character, MapChar> chars = new HashMap<Character, MapChar>();
    private Font font;
    private int  space;

    /**
     * Initializes a MapFont using a Font template
     *
     * @param font  font template
     * @param space spacing between characters
     */
    public MapFont(Font font, int space)
    {
        this.font = font;
        this.space = space;
    }

    /**
     * Retrieves the font wrapped by this object
     *
     * @return wrapped font
     */
    public Font getFont()
    {
        return font;
    }

    /**
     * Retrieves the spacing between letters for
     * this font
     *
     * @return spacing between letters
     */
    public int getSpace()
    {
        return space;
    }

    /**
     * Gets data for a character from the font. If the character is not initialized,
     * it will be initialized first.
     *
     * @param c character to get the data of
     *
     * @return character data for the font
     */
    public MapChar getChar(char c)
    {
        if (!font.canDisplay(c)) return null;
        if (!chars.containsKey(c))
        {
            if (c == '.') {
                chars.put(c, new MapChar(new boolean[]{ true }, 1, 1, 0));
            }

            else
            {
                BufferedImage buffer = new BufferedImage(32, 32, 2);
                Graphics2D g = buffer.createGraphics();
                g.setFont(font);
                Rectangle2D bounds = font.createGlyphVector(g.getFontRenderContext(), c + "").getPixelBounds(g.getFontRenderContext(), 0, 0);
                if (bounds.getWidth() <= 0 || bounds.getHeight() <= 0 || bounds.getWidth() > 32 || bounds.getHeight() > 32) {
                    chars.put(c, new MapChar(new boolean[] { false }, 1, 1, 0));
                }
                else
                {
                    g.drawString(c + "", 0, -(int) bounds.getY());
                    boolean[] data = new boolean[(int) (bounds.getWidth() * bounds.getHeight())];
                    int[] pixels = new int[data.length * 4];
                    buffer.getData().getPixels(0, 0, (int) bounds.getWidth(), (int) bounds.getHeight(), pixels);
                    for (int j = 0; j < bounds.getHeight(); j++)
                    {
                        for (int i = 0; i < bounds.getWidth(); i++)
                        {
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
    class MapChar
    {
        private boolean[] data;
        private int       width;
        private int       height;
        private int       base;

        private MapChar(boolean[] data, int w, int h, int b)
        {
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
        public boolean[] getData()
        {
            return data;
        }

        /**
         * Retrieves the width of the character
         *
         * @return character width
         */
        public int getWidth()
        {
            return width;
        }

        /**
         * Retrieves the height of the character
         *
         * @return character height
         */
        public int getHeight()
        {
            return height;
        }

        /**
         * Retrieves the baseline offset of the character
         *
         * @return baseline offset
         */
        public int getBase()
        {
            return base;
        }
    }
}
