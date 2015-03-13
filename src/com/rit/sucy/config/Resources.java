package com.rit.sucy.config;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>A utility class for copying resource files to the server directory.</p>
 */
public class Resources
{

    /**
     * <p>Copies a resource to the plugin's data folder using the same file name
     * as the resource. If the file already exists it will be overwritten.</p>
     *
     * @param plugin   plugin reference
     * @param resource resource to copy
     */
    public static void copy(Plugin plugin, String resource)
    {
        copy(plugin, plugin.getDataFolder().getAbsolutePath() + File.separator + resource, resource, true);
    }

    /**
     * <p>Copies a resource to the plugin's data folder using the same file name
     * as the resource.</p>
     *
     * @param plugin    plugin reference
     * @param resource  resource to copy
     * @param overwrite whether or not to overwrite existing files
     */
    public static void copy(Plugin plugin, String resource, boolean overwrite)
    {
        copy(plugin, plugin.getDataFolder().getAbsolutePath() + File.separator + resource, resource, overwrite);
    }

    /**
     * <p>Copies a resource to the plugin's data folder using the provided
     * destination to save to. If the file already exists it will be overwritten.</p>
     *
     * @param plugin      plugin reference
     * @param resource    resource to copy
     * @param destination destination to save the file to
     */
    public static void copy(Plugin plugin, String resource, String destination)
    {
        copy(plugin, resource, destination, true);
    }

    /**
     * <p>Copies a resource to the plugin's data folder using the provided
     * destination to save to.</p>
     *
     * @param plugin      plugin reference
     * @param resource    resource to copy
     * @param destination destination to save the file to
     * @param overwrite   whether or not to overwrite existing files
     */
    public static void copy(Plugin plugin, String resource, String destination, boolean overwrite)
    {
        if (!resource.startsWith(File.separator)) resource = File.separator + resource;
        String folder = null;
        if (destination.contains(File.separator))
        {
            folder = destination.substring(0, destination.lastIndexOf(File.separator));
        }

        // Don't copy if it's already there
        File target = new File(destination);
        if (!overwrite && target.exists()) return;

        try
        {

            // Prepare to copy the file
            InputStream stream = plugin.getClass().getResourceAsStream(resource);
            OutputStream resStreamOut;
            int readBytes;
            byte[] buffer = new byte[4096];
            if (folder != null)
            {
                File directory = new File(folder);
                directory.mkdirs();
            }
            resStreamOut = new FileOutputStream(target);

            // Copy to the file
            while ((readBytes = stream.read(buffer)) > 0)
            {
                resStreamOut.write(buffer, 0, readBytes);
            }

            // Close the streams
            stream.close();
            resStreamOut.close();
        }

        // An error occurred
        catch (Exception ex)
        {
            plugin.getLogger().severe("Failed to copy the resource: " + resource);
        }
    }
}
