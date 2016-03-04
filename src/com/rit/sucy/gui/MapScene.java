package com.rit.sucy.gui;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a collection of images with positions to keep track
 * of when drawing to a MapBuffer. This allows for less network
 * bandwith to be used while still keeping the incredible speed
 * of MapBuffer. Use this instead of drawing directly to a
 * MapBuffer to reduce network bandwidth used by map menus.
 */
public class MapScene
{
    private HashMap<String, MapObject> items   = new HashMap<String, MapObject>();
    private ArrayList<MapObject>       ordered = new ArrayList<MapObject>();

    /**
     * Adds an object to the scene
     *
     * @param key access key for the object
     * @param obj object to add to the scene
     */
    public void add(String key, MapObject obj)
    {
        items.put(key, obj);
        ordered.add(obj);
    }

    /**
     * Retrieves an object from the scene by key
     *
     * @param key access key the object was added with
     * @return the scene object
     */
    public MapObject get(String key)
    {
        return items.get(key);
    }

    /**
     * Clear the scene before moving onto a new menu
     */
    public void clear()
    {
        items.clear();
    }

    /**
     * Updates the scene, getting the "dirty" bounds
     */
    public void apply(MapBuffer buffer) {
        if (items.size() == 0) return;

        int[] bounds = buffer.bounds;
        bounds[0] = bounds[1] = 127;
        bounds[2] = bounds[3] = 0;
        buffer.dirty = false;
        for (MapObject obj : ordered) {
            if (obj.isDirty()) {
                int[] dirty = obj.getFlagBounds();
                bounds[0] = Math.min(bounds[0], dirty[0]);
                bounds[1] = Math.min(bounds[1], dirty[1]);
                bounds[2] = Math.max(bounds[2], dirty[2]);
                bounds[3] = Math.max(bounds[3], dirty[3]);
                buffer.dirty = true;
            }
            obj.clean();
        }
        if (buffer.dirty) {
            buffer.clear();
            for (MapObject obj : ordered) {
                if (obj.visible)
                {
                    buffer.drawImg(obj.img, obj.x, obj.y + obj.img.offset);
                }
            }
        }
    }
}
