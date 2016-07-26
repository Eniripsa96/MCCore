/**
 * MCCore
 * com.rit.sucy.gui.MapImageManager
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

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * Manages loading images for all plugins to prevent duplicate loading
 * of the same image and keeps everything in one place.
 */
public class MapImageManager {
    private static HashMap<String, MapImage> images = new HashMap<String, MapImage>();

    /**
     * Retrieves an image from the specified path. The path
     * starts from the root of the server, so if you're getting
     * an image from your plugin's data folder, you need to
     * include "plugins/MyPlugin" before it. This will load the
     * image if it hasn't been loaded already.
     *
     * @param path path to the image
     * @return loaded image
     */
    public static MapImage getImage(String path) {

        // Already loaded!
        if (images.containsKey(path)) {
            return images.get(path);
        }

        // Load it and return the result
        else {
            try {
                MapImage img = new MapImage(new File(path));
                images.put(path, img);
                return img;
            } catch (Exception ex) {
                return null;
            }
        }
    }

    /**
     * Copies an embedded image resource from the plugin to the given path.
     * The path is from the root of the server so to place it within your
     * plugin's data folder, preceed the path with "plugins/MyPlugin".
     *
     * @param plugin plugin to get the resource from
     * @param name   name of the resource image (including extension)
     * @param path   path to the destination
     * @return true if copied successfully, false otherwise
     */
    public static boolean copyImageResource(JavaPlugin plugin, String name, String path) {
        try {
            // Prepare to copy the file
            InputStream stream = plugin.getClass().getResourceAsStream("/" + name);
            OutputStream resStreamOut;
            int readBytes;
            byte[] buffer = new byte[4096];
            File dir = new File(path);
            dir.mkdirs();
            resStreamOut = new FileOutputStream(new File(dir + File.separator + name));

            // Copy to the file
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }

            // Close the streams
            stream.close();
            resStreamOut.close();

            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
