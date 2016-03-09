/**
 * MCCore
 * com.rit.sucy.config.ConfigSerializer
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
package com.rit.sucy.config;

import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>Utility class for serializing objects with control via annotations</p>
 * <p>The two annotations used by this serializer are SerializableField and ExcludeField</p>
 * <p>To exclude a field from serialization, annotate it with ExcludeField</p>
 * <p>To make a field be serialized further, annotate it with SerializableField</p>
 * <p>You can define flags and flag groups to change when fields can be serialized</p>
 * <br/>
 * <p>IMPORTANT: You should exclude any fields that have two-way pointers (I.E. a child
 * element that has its parent as a field when its parent has the child as a field). If
 * you don't, you will likely get a StackOverflowException when serializing! If you need
 * the references, exclude only the child so that when you serialize the parent, it will
 * still serialize the child but not cause the problem.</p>
 * <p>Also, primitive arrays such as int[] give the config problems. Where possible, use
 * array lists instead for better compatibility.</p>
 */
public class ConfigSerializer
{

    public static final String ALL_FLAG = "ALL";

    private static final HashMap<String, Integer> flags     = new HashMap<String, Integer>()
    {{
            put(ALL_FLAG, Integer.MAX_VALUE);
        }};
    private static       int                      flagCount = 0;

    /**
     * Defines a new flag for field exclusion
     *
     * @param name flag name
     */
    public static void defineFlag(String name)
    {

        if (flagCount == 30)
        {
            throw new IllegalArgumentException("Cannot contain any more flags");
        }

        if (flags.containsKey(name))
        {
            throw new IllegalArgumentException("Flag name already in use: " + name);
        }

        flags.put(name, 1 << flagCount++);
    }

    /**
     * Defines a new flag group for field exclusion
     *
     * @param name    flag name
     * @param members names of flags to include in the group
     */
    public static void defineFlagGroup(String name, String... members)
    {

        if (flags.containsKey(name))
        {
            throw new IllegalArgumentException("Flag name already in use: " + name);
        }

        int value = 0;
        for (String flag : members)
        {
            if (flags.containsKey(flag))
            {
                value += flags.get(flag);
            }
        }
        if (value > 0)
        {
            flags.put(name, value);
        }
    }

    /**
     * Serializes an object excluding fields with any exclude annotations
     *
     * @param obj    object to serialize
     * @param config config to serialize to
     */
    public static void serialize(Object obj, ConfigurationSection config)
    {
        serialize(obj, config, ALL_FLAG);
    }

    /**
     * <p>Serializes an object excluding fields that match the provided exclude flag</p>
     * <p>Matching a flag requires the two flags to share any flag. This means that
     * two flag groups will match if at least one of their children match and two
     * regular flags will match only when they are the same.</p>
     *
     * @param obj    object to serialize
     * @param config config to serialize into
     * @param flag   flag name
     */
    public static void serialize(Object obj, ConfigurationSection config, String flag)
    {

        // Validate the parameters
        if (!flags.containsKey(flag))
        {
            throw new IllegalArgumentException("Flag is not registered: " + flag);
        }
        else if (config == null)
        {
            throw new IllegalArgumentException("Cannot serialize into a null configuration");
        }
        else if (obj == null)
        {
            return;
        }

        int value = flags.get(flag);

        // Retrieve all fields of the object
        ArrayList<Field> fields = new ArrayList<Field>();
        Class c = obj.getClass();
        while (c != null)
        {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
            c = c.getSuperclass();
        }

        // Go through each field and serialize it if applicable
        for (Field field : fields)
        {

            // set accessibility to avoid breaking
            field.setAccessible(true);

            // Check if the field has exclude annotations. If so, ignore it
            ExcludeField exclude = field.getAnnotation(ExcludeField.class);
            if (exclude != null && (flags.get(exclude.flag()) & value) > 0)
            {
                continue;
            }

            // Serialize the object depending on whether or not it has the serializable annotation
            try
            {
                SerializableField serializable = field.getAnnotation(SerializableField.class);
                if (serializable != null && (flags.get(serializable.flag()) & value) > 0)
                {
                    if (!config.contains(field.getName()))
                    {
                        config.createSection(field.getName());
                    }
                    if (serializable.list())
                    {
                        int index = 1;
                        ConfigurationSection listSection = config.getConfigurationSection(field.getName());
                        for (Object item : (Iterable<?>) field.get(obj))
                        {
                            String path = "item" + index;
                            if (!listSection.contains(path))
                            {
                                listSection.createSection(path);
                            }
                            serialize(item, listSection.getConfigurationSection(path));
                        }
                    }
                    else if (serializable.map())
                    {
                        ConfigurationSection root = config.getConfigurationSection(field.getName());
                        HashMap<?, ?> map = (HashMap<?, ?>) serializable;
                        for (Map.Entry<?, ?> entry : map.entrySet())
                        {
                            if (!root.contains(entry.getKey().toString()))
                            {
                                root.createSection(entry.getKey().toString());
                            }
                            ConfigurationSection mapSection = root.getConfigurationSection(entry.getKey().toString());
                            serialize(entry.getValue(), mapSection);
                        }
                    }
                    else
                    {
                        serialize(field.get(obj), config.getConfigurationSection(field.getName()), flag);
                    }
                }
                else
                {
                    Object v = field.get(obj);
                    config.set(field.getName(), v);
                }
            }

            catch (Exception ex)
            {
                // If for some reason the field could not be accessed, just move on
            }
        }
    }
}
