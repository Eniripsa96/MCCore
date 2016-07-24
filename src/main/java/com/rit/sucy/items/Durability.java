/**
 * MCCore
 * com.rit.sucy.items.Durability
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
package com.rit.sucy.items;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * <p>A helper class for controlling the durability of applicable items</p>
 * <p>The durabilities expressed in this class are the amount of durability
 * left on the item rather than the durability consumed like from Bukkit's
 * methods. For example, an item with full durability will return the
 * maximum durability in this class while in Bukkit it will return 0.</p>
 * <p>The methods will still apply the durabilities to items names if
 * durabilities are disabled in MCCore's config, but they will not
 * function in game as if they were changed because MCCore will not
 * be able to update them.</p>
 */
public class Durability {

    /**
     * <p>Checks whether or not the item can have custom durabilities
     * attached to it.</p>
     * <p>This requires an item to pass the following checks:</p>
     * <ul>
     * <li>The item cannot be null</li>
     * <li>The item must have a max durability greater than 0</li>
     * </ul>
     *
     * @param item item to check
     *
     * @return true if can have custom durability, false otherwise
     */
    public static boolean canHaveCustomDurability(ItemStack item) {
        return item != null && item.getType().getMaxDurability() != 0;
    }

    /**
     * <p>Checks whether or not the item has a custom durability attached to it</p>
     *
     * @param item item to check
     *
     * @return true if has custom durability, false otherwise
     */
    public static boolean hasCustomDurability(ItemStack item) {
        if (canHaveCustomDurability(item) && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            return meta.hasDisplayName()
                    && meta.getDisplayName().contains(ChatColor.COLOR_CHAR + "y")
                    && meta.getDisplayName().contains(ChatColor.COLOR_CHAR + "z");
        } else return false;
    }

    /**
     * <p>Clears the custom durability set to an item</p>
     * <p>This will remove the custom durability data but
     * will leave the item's normal data alone so it will
     * still have the same percentage durability left.</p>
     * <p>If the item does not have a custom durability
     * attached to it, this will do nothing.</p>
     *
     * @param item item to clear the durability for
     */
    public static void clearCustomDurability(ItemStack item) {
        if (hasCustomDurability(item)) {
            ItemMeta meta = item.getItemMeta();
            String name = meta.getDisplayName();
            meta.setDisplayName(name.substring(0, name.indexOf(ChatColor.COLOR_CHAR + "y")));
            item.setItemMeta(meta);
        }
    }

    /**
     * <p>Sets the maximum durability of an item</p>
     * <p>This will be cleared if the name on the item is changed
     * such as using an anvil or through plugin manipulation.</p>
     * <p>The current durability of the item will be computed
     * from the current percentage left on the item scaled to
     * the new maximum durability. (e.g. an item with half
     * durability being set to a max durability of 5,000 will
     * now have 2,500 durability left)</p>
     * <p>If the item cannot have a durability, this will
     * do nothing to the item.</p>
     *
     * @param item item to set the max durability of
     * @param max  the new durability
     */
    public static void setMaxDurability(ItemStack item, int max) {
        if (canHaveCustomDurability(item)) {

            // Clear any previous custom data
            int durability = (int) (((double) getDurability(item) / getMaxDurability(item)) * max);
            clearCustomDurability(item);

            ItemMeta meta = item.getItemMeta();
            String name = meta.hasDisplayName() ? meta.getDisplayName() : ItemManager.getVanillaName(item);
            StringBuilder sb = new StringBuilder(name);

            // Append the max durability
            String dString = max + "";
            sb.append(ChatColor.COLOR_CHAR);
            sb.append('y');
            for (char c : dString.toCharArray()) {
                sb.append(ChatColor.COLOR_CHAR);
                sb.append(c);
            }

            // Append the current durability
            dString = durability + "";
            sb.append(ChatColor.COLOR_CHAR);
            sb.append('z');
            for (char c : dString.toCharArray()) {
                sb.append(ChatColor.COLOR_CHAR);
                sb.append(c);
            }

            // Apply the durability
            meta.setDisplayName(sb.toString());
            item.setItemMeta(meta);
        }
    }

    /**
     * <p>Sets the durability of an item</p>
     * <p>This scales down the custom durability to the actual
     * durability range so if the custom durability is wiped,
     * it will still have the same percentage durability.</p>
     * <p>If the item has no custom durability but normally
     * has durability, this will apply the normal durability
     * of the item.</p>
     * <p>If the item cannot have a durability, this will
     * do nothing to the item.</p>
     *
     * @param item       item to set the durability of
     * @param durability the new durability
     */
    public static void setDurability(ItemStack item, int durability) {
        if (hasCustomDurability(item)) {
            ItemMeta meta = item.getItemMeta();
            String name = meta.hasDisplayName() ? meta.getDisplayName() : ItemManager.getVanillaName(item);
            StringBuilder sb = new StringBuilder(name.substring(0, name.indexOf(ChatColor.COLOR_CHAR + "z")));
            int max = getMaxDurability(item);
            durability = Math.max(Math.min(max, durability), 0);
            String dString = durability + "";

            // Apply the new durability
            sb.append(ChatColor.COLOR_CHAR);
            sb.append('z');
            for (char c : dString.toCharArray()) {
                sb.append(ChatColor.COLOR_CHAR);
                sb.append(c);
            }

            meta.setDisplayName(sb.toString());
            item.setItemMeta(meta);
            item.setDurability((short) (item.getType().getMaxDurability() - ((double) durability / max) * item.getType().getMaxDurability()));
        }

        // Set the normal durability of the item
        else if (item != null && item.getType().getMaxDurability() > 0) {
            durability = Math.max(Math.min(durability, item.getType().getMaxDurability()), 0);
            item.setDurability((short) (item.getType().getMaxDurability() - durability));
        }
    }

    /**
     * <p>Retrieves the maximum durability of an item</p>
     * <p>This will look for a custom max durability and return
     * that over the normal max durability. When no custom max
     * durability is found, the default max durability is returned</p>
     *
     * @param item item to get the max durability of
     *
     * @return maximum durability
     */
    public static int getMaxDurability(ItemStack item) {
        if (hasCustomDurability(item)) {
            ItemMeta meta = item.getItemMeta();
            String name = meta.getDisplayName();
            int index = name.indexOf(ChatColor.COLOR_CHAR + "y") + 2;
            int durability = 0;
            while (name.charAt(index + 1) != 'z') {
                durability = durability * 10 + (int) name.charAt(index + 1) - 48;
                index += 2;
            }
            return durability;
        } else return item == null ? 0 : item.getType().getMaxDurability();
    }

    /**
     * <p>Retrieves the durability of an item</p>
     * <p>This will look for a custom durability and return
     * that over the normal durability. When no custom
     * durability is found, the default durability is returned</p>
     *
     * @param item item to get the max durability of
     *
     * @return maximum durability
     */
    public static int getDurability(ItemStack item) {
        // Invalid item
        if (item == null || item.getType().getMaxDurability() == 0) return 0;

        // Check for custom durability
        ItemMeta meta = item.getItemMeta();
        if (meta.hasDisplayName()
                && meta.getDisplayName().contains(ChatColor.COLOR_CHAR + "y")
                && meta.getDisplayName().contains(ChatColor.COLOR_CHAR + "z")) {
            String name = meta.getDisplayName();
            int index = name.indexOf(ChatColor.COLOR_CHAR + "z") + 2;
            int durability = 0;
            while (index < name.length()) {
                durability = durability * 10 + (int) name.charAt(index + 1) - 48;
                index += 2;
            }
            return durability;
        }

        // No custom durability
        else return item.getType().getMaxDurability() - item.getDurability();
    }

    /**
     * <p>Adds durability to the item</p>
     * <p>If the item doesn't have durability, this
     * method will not do anything</p>
     *
     * @param item   item to add durability to
     * @param amount amount of durability to add
     */
    public static void addDurability(ItemStack item, int amount) {
        int durability = getDurability(item);
        setDurability(item, durability + amount);
    }

    /**
     * <p>Subtracts durability from the item</p>
     * <p>If the item doesn't have durability, this
     * method will not do anything</p>
     *
     * @param item   item to subtract durability from
     * @param amount amount of durability to subtract
     */
    public static void subtractDurability(ItemStack item, int amount) {
        addDurability(item, -amount);
    }
}
