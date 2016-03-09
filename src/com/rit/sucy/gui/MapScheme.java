/**
 * MCCore
 * com.rit.sucy.gui.MapScheme
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Steven Sucy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software") to deal
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

import com.rit.sucy.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A scheme used by a menu
 */
public class MapScheme
{
    private static final HashMap<String, MapScheme> schemes = new HashMap<String, MapScheme>();

    private static final String SCHEME_FILE = "schemes.yml";

    private static final String FONTS  = "fonts";
    private static final String FAMILY = "family";
    private static final String SIZE   = "size";
    private static final String STYLE  = "style";
    private static final String SPACE  = "space";
    private static final String COLORS = "colors";

    private HashMap<String, MapImage> images = new HashMap<String, MapImage>();
    private HashMap<String, MapFont>  fonts  = new HashMap<String, MapFont>();
    private HashMap<String, Byte>     colors = new HashMap<String, Byte>();

    private JavaPlugin plugin;
    private File root;
    private File folder;
    private String key = "default";
    private boolean finalized = false;

    /**
     * Creates the schemes for the given plugin using the
     * root folder as the place to load images for schemes from.
     *
     * @param plugin plugin reference
     * @param root   the root folder for scheme images
     * @return the created map scheme
     */
    public static MapScheme create(JavaPlugin plugin, File root)
    {
        return new MapScheme(plugin, root);
    }

    /**
     * Retrieves a scheme for the plugin
     *
     * @param plugin plugin to get the scheme for
     * @param name   name of the scheme
     * @return the plugin's scheme or the default scheme if not found
     */
    public static MapScheme get(JavaPlugin plugin, String name)
    {
        String key = plugin.getName() + "_" + name.toLowerCase();
        return schemes.containsKey(key) ? schemes.get(key) : schemes.get(plugin.getName() + "_default");
    }

    /**
     * Retrieves a list of schemes for the plugin. If the plugin
     * never set up a scheme, this will return an empty list instead.
     *
     * @param plugin plugin schemes
     * @return list of schemes for the plugin
     */
    public static ArrayList<MapScheme> list(JavaPlugin plugin)
    {
        ArrayList<MapScheme> list = new ArrayList<MapScheme>();
        for (String key : schemes.keySet())
        {
            if (key.startsWith(plugin.getName() + "_"))
            {
                list.add(schemes.get(key));
            }
        }
        return list;
    }

    /**
     * Creates a map scheme for the given plugin
     *
     * @param plugin plugin owning the scheme
     */
    private MapScheme(JavaPlugin plugin, File root)
    {
        this.plugin = plugin;
        this.root = root;
        schemes.put(plugin.getName() + "_" + key, this);
        folder = new File(root, "default");
        folder.mkdirs();
    }

    private MapScheme(String name, JavaPlugin plugin)
    {
        key = name;
        schemes.put(plugin.getName() + "_" + key, this);
    }

    /**
     * Retrieves the key for the scheme
     *
     * @return scheme key
     */
    public String getKey()
    {
        return key;
    }

    /**
     * Retrieves an image from the scheme
     *
     * @param key image key
     * @return the image for the scheme
     */
    public MapImage getImage(String key) {
        return images.get(key);
    }

    /**
     * Retrieves a font from the scheme
     *
     * @param key font key
     * @return the font of the scheme
     */
    public MapFont getFont(String key) {
        return fonts.get(key);
    }

    /**
     * Retrieves a color for the scheme
     *
     * @param key color key
     * @return the color for the scheme
     */
    public byte getColor(String key) {
        return colors.get(key);
    }

    /**
     * Defines an image used by the scheme if the
     * scheme is not already finalized. The image name will
     * be used as the access key.
     *
     * @param image name of the embedded image resource to use
     */
    public void defineImg(String image)
    {
        defineImg(image, image);
    }

    /**
     * Defines an image used by the scheme if the
     * scheme is not already finalized.
     *
     * @param key   key to use for the image
     * @param image name of the embedded image resource to use
     */
    public void defineImg(String key, String image)
    {
        if (finalized) return;
        try
        {
            image = image + ".png";
            MapImageManager.copyImageResource(plugin, image, folder.getAbsolutePath());
            images.put(key, new MapImage(new File(folder, image)));
        }
        catch (Exception ex)
        {
            Bukkit.getLogger().warning("Invalid scheme image " + this.key + ":" + key);
        }
    }

    /**
     * Defines a font used by the scheme if the scheme is not already
     * finalized.
     *
     * @param key         key to use for the font
     * @param defaultFont default font to use
     */
    public void defineFont(String key, MapFont defaultFont)
    {
        if (finalized) return;
        if (defaultFont == null) Bukkit.getLogger().warning("Invalid scheme font " + this.key + ":" + key);
        else fonts.put(key, defaultFont);
    }

    /**
     * Defines a color used by the scheme if the scheme is not
     * already finalized
     *
     * @param key      key to use for the color
     * @param colorHex hex code of the default color
     */
    public void defineColor(String key, String colorHex)
    {
        if (finalized) return;
        try
        {
            colorHex = colorHex.replace("#", "");
            if (!colorHex.startsWith("0X"))
            {
                colorHex = "0X" + colorHex;
            }
            colors.put(key, MapImage.matchColor(Color.decode(colorHex)));
        }
        catch (Exception ex)
        {
            Bukkit.getLogger().warning("Invalid scheme color " + this.key + ":" + key);
        }
    }

    /**
     * Finalizes the scheme and loads all other schemes
     */
    public void finalize()
    {
        if (finalized) return;
        finalized = true;

        // Load the alternate schemes
        File[] files = root.listFiles();
        for (File file : files)
        {
            if (file.getName().equals("default")) continue;
            load(file);
        }

        // Create/update the config file
        Config configFile = new Config(plugin, SCHEME_FILE);
        configFile.clear();
        save(configFile.getConfig());
        for (File file : files)
        {
            if (file.getName().equals("default")) continue;
            MapScheme scheme = get(plugin, file.getName());
            scheme.save(configFile.getConfig());
        }
        configFile.save();
    }

    private void save(ConfigurationSection config)
    {
        ConfigurationSection scheme = config.createSection(key);

        // Save fonts
        ConfigurationSection fontSection = scheme.createSection(FONTS);
        for (String s : fonts.keySet())
        {
            MapFont font = fonts.get(s);
            ConfigurationSection fontData = fontSection.createSection(s);
            fontData.set(FAMILY, font.getFont().getFamily());
            fontData.set(SIZE, font.getFont().getSize());
            fontData.set(STYLE, font.getFont().getStyle());
            fontData.set(SPACE, font.getSpace());
        }

        // Save colors
        ConfigurationSection colorSection = scheme.createSection(COLORS);
        for (String s : colors.keySet())
        {
            colorSection.set(s, Integer.toHexString(MapImage.getColor(colors.get(s)).getRGB()));
        }
    }

    /**
     * Loads a scheme from the given folder using the current
     * scheme as the default scheme
     *
     * @param folder        folder containing the scheme images
     * @return the loaded scheme or null if invalid
     */
    private MapScheme load(File folder)
    {
        try
        {
            String name = folder.getName();
            MapScheme scheme = new MapScheme(name, plugin);
            scheme.key = name.toLowerCase();

            // Load images
            for (String s : images.keySet())
            {
                try
                {
                    File image = new File(folder, s + ".png");
                    scheme.images.put(s, image.exists() ? new MapImage(image) : images.get(s));
                }
                catch (Exception ex)
                {
                    Bukkit.getLogger().info("Invalid scheme image " + name + ":" + s);
                    scheme.images.put(s, images.get(s));
                }
            }

            // Load data from the config
            Config configFile = new Config(scheme.plugin, SCHEME_FILE);
            ConfigurationSection config = configFile.getConfig();
            if (config.contains(name))
            {
                ConfigurationSection data = config.getConfigurationSection(name);

                // Load fonts
                if (data.contains(FONTS))
                {
                    ConfigurationSection fonts = data.getConfigurationSection(FONTS);
                    for (String s : this.fonts.keySet())
                    {
                        try
                        {
                            if (fonts.contains(s))
                            {
                                ConfigurationSection fontData = fonts.getConfigurationSection(s);
                                MapFont d = this.fonts.get(s);
                                String family = fontData.getString(FAMILY, d.getFont().getFamily());
                                int size = fontData.getInt(SIZE, d.getFont().getSize());
                                int style = fontData.getInt(STYLE, d.getFont().getStyle());
                                int space = fontData.getInt(SPACE, d.getSpace());
                                scheme.fonts.put(s, new MapFont(new Font(family, style, size), space));
                            }
                        }
                        catch (Exception ex)
                        {
                            Bukkit.getLogger().warning("Invalid scheme font " + name + ":" + s);
                            scheme.fonts.put(s, this.fonts.get(s));
                        }
                    }
                }
                else scheme.fonts = this.fonts;

                // Load colors
                if (data.contains(COLORS))
                {
                    ConfigurationSection colorData = data.getConfigurationSection(COLORS);
                    for (String s : colors.keySet())
                    {
                        if (colorData.contains(s))
                        {
                            scheme.colors.put(s, MapImage.matchColor(Color.decode(colorData.getString(s))));
                        }
                        else scheme.colors.put(s, colors.get(s));
                    }
                }
                else scheme.colors = colors;
            }
            return scheme;
        }
        catch (Exception ex)
        {
            Bukkit.getLogger().info("Invalid scheme: " + folder.getName());
            return null;
        }
    }
}
