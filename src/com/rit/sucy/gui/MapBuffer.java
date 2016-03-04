package com.rit.sucy.gui;

import com.rit.sucy.reflect.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapView;

import java.lang.reflect.Method;

/**
 * A specific MapImage used to represent a full map canvas
 * and draw to it as efficiently as possible.
 * <p/>
 * Just as a comparison between render methods:
 * - Built in MapCanvas.drawImage: ~40ms for 128x128 image
 * - Slow MapBuffer method: ~ 0.08ms for 128x128 image
 * - Fast MapBuffer method: ~ 0.001ms for 128x128 image
 * <p/>
 * In other words:
 * - Slow MapBuffer method is ~500x faster than MapCanvas.drawImage
 * - Fast MapBuffer method is ~40,000x faster than MapCanvas.drawImage
 */
public class MapBuffer extends MapImage
{
    private Object worldMap;
    private Method flagDirty;
    private Method flagDirty2;

    protected final int[] bounds;
    protected boolean dirty = true;

    /**
     * Initializes a new MapBuffer with a size of 128x128 that
     * acts as a buffer for the given MapView
     */
    public MapBuffer(MapView view)
    {
        super(128, 128);

        bounds = new int[] { 0, 0, 127, 127 };

        // Grab the needed values for fast rendering via reflection
        worldMap = Reflection.getValue(view, "worldMap");
        flagDirty = Reflection.getMethod(worldMap, "flagDirty", int.class, int.class);

        // Older servers use a slightly different method
        if (flagDirty == null)
        {
            flagDirty2 = Reflection.getMethod(worldMap, "flagDirty", int.class, int.class, int.class);
        }

        // Just validate the methods before assuming they're good to go
        try
        {
            if (flagDirty != null)
            {
                flagDirty.invoke(worldMap, 0, 0);
            }
            if (flagDirty2 != null)
            {
                flagDirty2.invoke(worldMap, 0, 0);
            }
        }
        catch (Exception ex)
        {
            // Test failed, they aren't supported after all
            flagDirty = null;
            flagDirty2 = null;
        }
    }

    /**
     * Checks whether or not fast drawing is supported. Fast
     * drawing can handle thousands of draws every tick while
     * normal drawing can only handle about 50-100 maximum.
     *
     * @return true if fast drawing is supported on this server, false otherwise
     */
    public boolean fastDrawSupported()
    {
        return flagDirty != null || flagDirty2 != null;
    }

    /**
     * Efficiently draws the buffer to the canvas
     *
     * @param canvas canvas to draw to
     */
    public void drawTo(MapCanvas canvas)
    {
        if (!this.dirty) return;

        // Try a much faster way of setting the data
        boolean fast = false;
        if (flagDirty != null)
        {
            try
            {
                // Flag the min/max bounds
                flagDirty.invoke(worldMap, bounds[0], bounds[1]);
                flagDirty.invoke(worldMap, bounds[2], bounds[3]);

                // Apply the buffer data
                Reflection.setValue(canvas, "buffer", getData());
                fast = true;
            }
            catch (Exception ex)
            {
                // Drawing failed to work, use normal method instead
                flagDirty = null;
            }
        }

        // Fast drawing on older servers
        else if (flagDirty2 != null)
        {
            try
            {
                // Flag the min/max bounds
                flagDirty2.invoke(worldMap, 0, bounds[0], bounds[2]);
                flagDirty2.invoke(worldMap, 1, bounds[1], bounds[3]);

                // Apply the buffer data
                Reflection.setValue(canvas, "buffer", getData());
                fast = true;
            }
            catch (Exception ex)
            {
                // Didn't work, use normal method instead
                flagDirty2 = null;
            }
        }

        // Otherwise use the tried and true method
        if (!fast)
        {
            // Just set each pixel individually
            // Not terrible since images are pre-processed
            // (about 500 times faster), but still not great.
            byte[] data = getData();
            for (int i = bounds[0]; i < bounds[2]; i++)
            {
                for (int j = bounds[1]; j < bounds[3]; j++)
                {
                    canvas.setPixel(i, j, data[i]);
                }
            }
        }
    }
}
