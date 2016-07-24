/**
 * MCCore
 * com.rit.sucy.gui.MapImage
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
import org.bukkit.ChatColor;
import org.bukkit.map.MapCanvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Represents an image in the format used by Minecraft maps
 * to speed up the drawing process. MapImages loaded from
 * files/URLs can take a little while to load though, so
 * set them up on enable to avoid any stalls during the game.
 */
public class MapImage {
    public static void init() {
    }

    private byte[] data;
    private int width;
    private int height;
    protected int offset;

    /**
     * Initializes a default map image matching
     * the size of a general map (128x128).
     */
    public MapImage() {
        this(128, 128);
    }

    /**
     * Creates an empty image with a format that
     * works better with the map canvas.
     *
     * @param width  width of the image
     * @param height height of the image
     */
    public MapImage(int width, int height) {
        data = new byte[width * height];
        this.width = width;
        this.height = height;
    }

    /**
     * Creates an empty image with a format that
     * works better with the map canvas.
     *
     * @param size size of the image { width, height }
     */
    public MapImage(int[] size) {
        this(size[0], size[1]);
    }

    /**
     * Initializes a new MapImage from an external URL,
     * converting it to a format that works better with
     * the map canvas.
     *
     * @param url URL to load from
     */
    public MapImage(URL url) throws IOException {
        Image img = ImageIO.read(url);
        width = img.getWidth(null);
        height = img.getHeight(null);
        data = imageToBytes(img);
    }

    /**
     * Initializes a new MapImage from a file, converting
     * it to a format that works better with the map canvas
     *
     * @param file file to load from
     *
     * @throws IOException
     */
    public MapImage(File file) throws IOException {
        Image img = ImageIO.read(file);
        width = img.getWidth(null);
        height = img.getHeight(null);
        data = imageToBytes(img);
    }

    /**
     * Retrieves the byte data of the image
     *
     * @return byte data of the image
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Retrieves the height of the image
     *
     * @return height of the image
     */
    public int getHeight() {
        return height;
    }

    /**
     * Retrieves the width of the image
     *
     * @return width of the image
     */
    public int getWidth() {
        return width;
    }

    /**
     * Clears the image
     */
    public void clear() {
        Arrays.fill(data, (byte) 0);
    }

    /**
     * Fills the image with the given color
     *
     * @param color color to fill
     */
    public void fill(byte color) {
        Arrays.fill(data, color);
    }

    /**
     * Draws an image onto this image
     *
     * @param img image to draw
     * @param x   horizontal position
     * @param y   vertical position
     */
    public void drawImg(MapImage img, int x, int y) {
        if (x >= width || y >= height) return;

        int maxX = Math.min(this.width, x + img.width);
        int maxY = Math.min(this.height, y + img.height);

        if (maxX <= 0 || maxY <= 0) return;

        int posX = Math.max(0, x);
        int posY = Math.max(0, y);

        int i, j;
        for (i = posX; i < maxX; i++) {
            for (j = posY; j < maxY; j++) {
                int source = i - x + (j - y) * img.width;
                if (img.data[source] != 0) {
                    data[i + j * width] = img.data[source];
                }
            }
        }
    }

    /**
     * Draws a string to the image
     *
     * @param font  font to use
     * @param color color of the text
     * @param str   string to draw
     * @param x     starting horizontal position
     * @param y     baseline position of the text
     *
     * @return x-coordinate at the end of the render
     */
    public int drawString(MapFont font, byte color, String str, int x, int y) {
        for (int i = 0; i < str.length() && x < width; i++) {
            MapFont.MapChar c = font.getChar(str.charAt(i));
            boolean[] data = c.getData();
            for (int j = 0; j < c.getWidth(); j++) {
                int a = j + x;
                for (int k = 0; k < c.getHeight(); k++) {
                    int b = k + y + c.getBase();
                    if (a >= 0 && a < width && b >= 0 && b < height && data[j + k * c.getWidth()]) {
                        this.data[a + b * width] = color;
                    }
                }
            }
            x += c.getWidth() + font.getSpace();
        }
        return x;
    }

    /**
     * Draws a string to the image
     *
     * @param font  font to use
     * @param color starting color of the text
     * @param str   string to draw
     * @param x     starting horizontal position
     * @param y     baseline position of the text
     *
     * @return x-coordinate at the end of the render
     */
    public int drawColorString(MapFont font, byte color, String str, int x, int y, char token) {
        byte currentColor = color;
        int first = 0;
        int next = str.indexOf(ChatColor.COLOR_CHAR);
        while (next >= 0) {
            if (first != next) {
                String part = str.substring(first, next);
                x = drawString(font, currentColor, part, x, y);
            }
            first = next + 2;
            char c = str.charAt(next + 1);
            if (colorCodes.containsKey(c)) {
                currentColor = colorCodes.get(c);
            } else if (c == 'r') {
                currentColor = color;
            }
            next = str.indexOf(ChatColor.COLOR_CHAR, first);
        }
        if (first < str.length()) x = drawString(font, color, str.substring(first), x, y);
        return x;
    }

    /**
     * Draws the image to the canvas. Normally you should instead
     * draw to a MapBuffer and then draw the mapBuffer to the canvas
     * unless you are only drawing a single small image.
     *
     * @param canvas canvas to draw to
     */
    public void drawTo(MapCanvas canvas, int x, int y) {
        for (int i = 0; i < data.length; i++) {
            int a = i % width;
            int b = i / width;

            canvas.setPixel(a + x, b + y, data[i]);
        }
    }

    // Available map palette colors
    private static final Color[] colors = new Color[]{
            new Color(0, 0, 0, 0), new Color(0, 0, 0, 0),
            new Color(0, 0, 0, 0), new Color(0, 0, 0, 0),
            c(89, 125, 39), c(109, 153, 48), c(127, 178, 56), c(67, 94, 29),
            c(174, 164, 115), c(213, 201, 140), c(247, 233, 163), c(130, 123, 86),
            c(117, 117, 117), c(144, 144, 144), c(167, 167, 167), c(88, 88, 88),
            c(180, 0, 0), c(220, 0, 0), c(255, 0, 0), c(135, 0, 0),
            c(112, 112, 180), c(138, 138, 220), c(160, 160, 255), c(84, 84, 135),
            c(117, 117, 117), c(144, 144, 144), c(167, 167, 167), c(88, 88, 88),
            c(0, 87, 0), c(0, 106, 0), c(0, 124, 0), c(0, 65, 0),
            c(180, 180, 180), c(220, 220, 220), c(255, 255, 255), c(135, 135, 135),
            c(115, 118, 129), c(141, 144, 158), c(164, 168, 184), c(86, 88, 97),
            c(129, 74, 33), c(157, 91, 40), c(183, 106, 47), c(96, 56, 24),
            c(79, 79, 79), c(96, 96, 96), c(112, 112, 112), c(59, 59, 59),
            c(45, 45, 180), c(55, 55, 220), c(64, 64, 255), c(33, 33, 135),
            c(73, 58, 35), c(89, 71, 43), c(104, 83, 50), c(55, 43, 26),
            c(180, 177, 172), c(220, 217, 211), c(255, 252, 245), c(135, 133, 129),
            c(152, 89, 36), c(186, 109, 44), c(216, 127, 51), c(114, 67, 27),
            c(125, 53, 152), c(153, 65, 186), c(178, 76, 216), c(94, 40, 114),
            c(72, 108, 152), c(88, 132, 186), c(102, 153, 216), c(54, 81, 114),
            c(161, 161, 36), c(197, 197, 44), c(229, 229, 51), c(121, 121, 27),
            c(89, 144, 17), c(109, 176, 21), c(127, 204, 25), c(67, 108, 13),
            c(170, 89, 116), c(208, 109, 142), c(242, 127, 165), c(128, 67, 87),
            c(53, 53, 53), c(65, 65, 65), c(76, 76, 76), c(40, 40, 40),
            c(108, 108, 108), c(132, 132, 132), c(153, 153, 153), c(81, 81, 81),
            c(53, 89, 108), c(65, 109, 132), c(76, 127, 153), c(40, 67, 81),
            c(89, 44, 125), c(109, 54, 153), c(127, 63, 178), c(67, 33, 94),
            c(36, 53, 125), c(44, 65, 153), c(51, 76, 178), c(27, 40, 94),
            c(72, 53, 36), c(88, 65, 44), c(102, 76, 51), c(54, 40, 27),
            c(72, 89, 36), c(88, 109, 44), c(102, 127, 51), c(54, 67, 27),
            c(108, 36, 36), c(132, 44, 44), c(153, 51, 51), c(81, 27, 27),
            c(17, 17, 17), c(21, 21, 21), c(25, 25, 25), c(13, 13, 13),
            c(176, 168, 54), c(215, 205, 66), c(250, 238, 77), c(132, 126, 40),
            c(64, 154, 150), c(79, 188, 183), c(92, 219, 213), c(48, 115, 112),
            c(52, 90, 180), c(63, 110, 220), c(74, 128, 255), c(39, 67, 135),
            c(0, 153, 40), c(0, 187, 50), c(0, 217, 58), c(0, 114, 30),
            c(14, 14, 21), c(18, 17, 26), c(21, 20, 31), c(11, 10, 16),
            c(79, 1, 0), c(96, 1, 0), c(112, 2, 0), c(59, 1, 0)
    };

    // Color code conversion
    private static final HashMap<Character, Byte> colorCodes = new HashMap<Character, Byte>() {{
        put('0', MapImage.matchColor(new Color(0, 0, 0)));
        put('1', MapImage.matchColor(new Color(0, 0, 190)));
        put('2', MapImage.matchColor(new Color(0, 190, 0)));
        put('3', MapImage.matchColor(new Color(0, 190, 190)));
        put('4', MapImage.matchColor(new Color(190, 0, 0)));
        put('5', MapImage.matchColor(new Color(190, 0, 190)));
        put('6', MapImage.matchColor(new Color(217, 163, 52)));
        put('7', MapImage.matchColor(new Color(190, 190, 190)));
        put('8', MapImage.matchColor(new Color(63, 63, 63)));
        put('9', MapImage.matchColor(new Color(63, 63, 254)));
        put('a', MapImage.matchColor(new Color(63, 254, 63)));
        put('b', MapImage.matchColor(new Color(63, 254, 254)));
        put('c', MapImage.matchColor(new Color(254, 63, 63)));
        put('d', MapImage.matchColor(new Color(254, 63, 254)));
        put('e', MapImage.matchColor(new Color(254, 254, 63)));
        put('f', MapImage.matchColor(new Color(255, 255, 255)));
    }};

    /**
     * Iniitalizes a new color from the RGB channels
     *
     * @param r red channel
     * @param g green channel
     * @param b blue channel
     *
     * @return color object
     */
    private static Color c(int r, int g, int b) {
        return new Color(r, g, b);
    }

    /**
     * Gets the weighted difference between two colors for
     * determining the similarity between them
     *
     * @param c1 first color to compare
     * @param c2 second color to compare
     *
     * @return the weighted similarity value between the two
     */
    private static double getDistance(Color c1, Color c2) {
        double rmean = (c1.getRed() + c2.getRed()) * 0.5;
        double r = c1.getRed() - c2.getRed();
        double g = c1.getGreen() - c2.getGreen();
        int b = c1.getBlue() - c2.getBlue();
        double weightR = 2.0 + rmean * 0.00390625;
        double weightG = 4.0;
        double weightB = 2.0 + (255.0 - rmean) * 0.00390625;
        return weightR * r * r + weightG * g * g + weightB * b * b;
    }

    /**
     * Converts an image to a byte array to use for map drawing
     *
     * @param image image to convert
     *
     * @return data byte array
     */
    private static byte[] imageToBytes(Image image) {
        BufferedImage temp = new BufferedImage(image.getWidth(null), image.getHeight(null), 2);
        Graphics2D graphics = temp.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();

        int[] pixels = new int[temp.getWidth() * temp.getHeight()];
        temp.getRGB(0, 0, temp.getWidth(), temp.getHeight(), pixels, 0, temp.getWidth());

        byte[] result = new byte[temp.getWidth() * temp.getHeight()];
        for (int i = 0; i < pixels.length; i++) {
            result[i] = matchColor(new Color(pixels[i], true));
        }
        return result;
    }

    /**
     * Gets the closest color to the provided one that
     * is available for the map canvas.
     *
     * @param color color to convert to a map palette color
     *
     * @return map palette color closest to the original
     */
    public static byte matchColor(Color color) {
        if (color.getAlpha() < 128) return 0;

        int index = 0;
        double best = Double.MAX_VALUE;

        for (int i = 4; i < colors.length; i++) {
            double distance = getDistance(color, colors[i]);
            if (distance < best) {
                best = distance;
                index = i;
            }

        }

        return (byte) (index < 128 ? index : index - 256);
    }

    /**
     * Retrieves a color by a given Map Palette ID
     *
     * @param id map palette ID
     *
     * @return java color represented by the ID
     */
    public static Color getColor(byte id) {
        int index = id;
        if (index < 0) index = 256 + id;
        return colors[index];
    }
}
