/**
 * MCCore
 * com.rit.sucy.gui.MapObject
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Steven Sucy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
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
package com.rit.sucy.gui;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class MapObject
{
    private static final int[] FLAG_BOUNDS = new int[4];

    protected MapImage img;
    protected int x;
    protected int y;
    protected boolean visible;

    private boolean first = true;
    private int lastX = 0;
    private int lastY = 0;
    private boolean lastVisible = false;

    /**
     * Initializes the MapObject at the given coordinates
     *
     * @param img image to draw
     * @param x   initial X position
     * @param y   initial Y position
     */
    public MapObject(MapImage img, int x, int y)
    {
        this(img, x, y, true);
    }

    /**
     * Initializes the MapObject at the given coordinates and
     * sets the visibility.
     *
     * @param img     image to draw
     * @param x       initial X position
     * @param y       initial Y position
     * @param visible whether or not it can be seen initially
     */
    public MapObject(MapImage img, int x, int y, boolean visible)
    {
        this.img = img;
        this.x = x;
        this.y = y;
        this.visible = visible;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return img.getWidth();
    }

    public int getHeight() {
        return img.getHeight();
    }

    /**
     * Hides the object, no longer drawing it each update
     */
    public void hide() {
        this.visible = false;
    }

    /**
     * Shows the object, enabling drawing it to the map
     */
    public void show() {
        this.visible = true;
    }

    /**
     * Sets the visibility of the object using a boolean value
     *
     * @param visible visibility of the object
     */
    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    /**
     * Moves the object relative to its current location
     *
     * @param x horizontal change
     * @param y vertical change
     */
    public void move(int x, int y) {
        this.x += x;
        this.y += y;
    }

    /**
     * Moves the object to the given position
     *
     * @param x horizontal position
     * @param y vertical position
     */
    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Checks whether or not the object requires an update. This shouldn't needed
     * to be called outside the API. Don't use this unless you have a good reason.
     *
     * @return true if required, false otherwise
     */
    public boolean isDirty() {
        return (first && visible) || (x != lastX) || (y != lastY) || (visible != lastVisible);
    }

    /**
     * Retrieves the bounds of the changes. This shouldn't needed to be called
     * outside the API. Don't use this unless you have a good reason.
     *
     * @return bounds of changes to mark for the packet
     */
    public int[] getFlagBounds() {
        if (first || (visible && !lastVisible)) {
            FLAG_BOUNDS[0] = x;
            FLAG_BOUNDS[1] = y;
            FLAG_BOUNDS[2] = x + img.getWidth() - 1;
            FLAG_BOUNDS[3] = y + img.getHeight() - 1;
        }
        else if (visible && lastVisible) {
            FLAG_BOUNDS[0] = Math.min(x, lastX);
            FLAG_BOUNDS[1] = Math.min(y, lastY);
            FLAG_BOUNDS[2] = Math.max(x, lastX) + img.getWidth() - 1;
            FLAG_BOUNDS[3] = Math.max(y, lastY) + img.getHeight() - 1;
        }
        else if (lastVisible) {
            FLAG_BOUNDS[0] = lastX;
            FLAG_BOUNDS[1] = lastY;
            FLAG_BOUNDS[2] = lastX + img.getWidth() - 1;
            FLAG_BOUNDS[3] = lastY + img.getHeight() - 1;
        }

        FLAG_BOUNDS[0] = Math.max(0, FLAG_BOUNDS[0]);
        FLAG_BOUNDS[1] = Math.max(0, FLAG_BOUNDS[1]);
        FLAG_BOUNDS[2] = Math.min(127, FLAG_BOUNDS[2]);
        FLAG_BOUNDS[3] = Math.min(127, FLAG_BOUNDS[3]);

        return FLAG_BOUNDS;
    }

    /**
     * Cleans the object, removing any tracked changes. This should
     * only be called by the API. Do not call this yourself
     */
    public void clean() {
        first = false;
        lastX = x;
        lastY = y;
        lastVisible = visible;
    }
}
