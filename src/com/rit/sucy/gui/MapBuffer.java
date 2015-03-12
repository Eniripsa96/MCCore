package com.rit.sucy.gui;

import com.rit.sucy.reflect.Reflection;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapView;

import java.lang.reflect.Method;

/**
 * A specific MapImage used to represent a full map canvas
 * and draw to it as efficiently as possible
 */
public class MapBuffer extends MapImage
{
    private Object worldMap;
    private Method flagDirty;
    private Method flagDirty2;

    /**
     * Initializes a new MapBuffer with a size of 128x128 that
     * acts as a buffer for the given MapView
     *
     */
    public MapBuffer(MapView view)
    {
        super(128, 128);

        worldMap = Reflection.getValue(view, "worldMap");
        flagDirty = Reflection.getMethod(worldMap, "flagDirty", int.class, int.class);
        if (flagDirty == null) {
            flagDirty2 = Reflection.getMethod(worldMap, "flagDirty2", int.class, int.class, int.class);
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
        return flagDirty != null;
    }

    /**
     * Efficiently draws the buffer to the canvas
     *
     * @param canvas canvas to draw to
     */
    public void drawTo(MapCanvas canvas)
    {
        // Try a much faster way of setting the data
        boolean fast = false;
        if (flagDirty != null)
        {
            try
            {
                flagDirty.invoke(worldMap, 0, 0);
                flagDirty.invoke(worldMap, 127, 127);
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
                flagDirty2.invoke(worldMap, 0, 0, 127);
                flagDirty2.invoke(worldMap, 1, 0, 127);
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
            byte[] data = getData();
            for (int i = 0; i < data.length; i++)
            {
                int x = i & 127;
                int y = i >> 7;

                canvas.setPixel(x, y, data[i]);
            }
        }
    }
}
