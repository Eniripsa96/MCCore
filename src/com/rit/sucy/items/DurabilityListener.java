/**
 * MCCore
 * com.rit.sucy.items.DurabilityListener
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
package com.rit.sucy.items;

import com.rit.sucy.MCCore;
import com.rit.sucy.event.ItemGainDurabilityEvent;
import com.rit.sucy.event.ItemLoseDurabilityEvent;
import com.rit.sucy.text.TextFormatter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * <p>Listener for handling custom durabilities</p>
 */
public class DurabilityListener implements Listener
{

    private MCCore plugin;

    /**
     * <p>Initializes a new listener</p>
     * <p>MCCore already handles setting this up so you should
     * not have to call this constructor</p>
     *
     * @param plugin MCCore reference
     */
    public DurabilityListener(MCCore plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Checks for changes in durability for most tools when a player breaks a block
     *
     * @param event event details
     */
    @EventHandler
    public void onBreak(BlockBreakEvent event)
    {
        if (event.getPlayer() != null && Durability.canHaveCustomDurability(event.getPlayer().getItemInHand()))
        {
            new DurabilityTask(event.getPlayer(), event.getPlayer().getItemInHand());
        }
    }

    /**
     * Checks for changes in durability for armor when a player is damaged in any way
     *
     * @param event event details
     */
    @EventHandler
    public void onDamaged(EntityDamageEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            Player player = (Player) event.getEntity();
            for (ItemStack item : player.getInventory().getArmorContents())
            {
                if (item != null && Durability.canHaveCustomDurability(item))
                {
                    new DurabilityTask(player, item);
                }
            }
        }
    }

    /**
     * Checks for changes in durability for weapons when a player hits another entity
     *
     * @param event event details
     */
    @EventHandler
    public void onHit(EntityDamageByEntityEvent event)
    {
        if (event.getDamager() instanceof Player)
        {
            Player player = (Player) event.getDamager();
            if (Durability.canHaveCustomDurability(player.getItemInHand()))
            {
                new DurabilityTask(player, player.getItemInHand());
            }
        }
    }

    /**
     * Checks for changes in durability for bows when a player launches an arrow
     *
     * @param event event details
     */
    @EventHandler
    public void onLaunch(EntityShootBowEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            Player player = (Player) event.getEntity();
            new DurabilityTask(player, player.getItemInHand());
        }
    }

    /**
     * Checks for changes in durability for flint and steel when a player interacts with a block
     *
     * @param event event details
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if (Durability.canHaveCustomDurability(event.getPlayer().getItemInHand()))
            {
                new DurabilityTask(event.getPlayer(), event.getPlayer().getItemInHand());
            }
        }
    }

    /**
     * Checks for changes in durability for shears when a player interacts with an entity
     *
     * @param event event details
     */
    @EventHandler
    public void onShear(PlayerShearEntityEvent event)
    {
        new DurabilityTask(event.getPlayer(), event.getPlayer().getItemInHand());
    }

    /**
     * Checks for changes in durability for fishing rods when a player fishes
     *
     * @param event event details
     */
    @EventHandler
    public void onFish(PlayerFishEvent event)
    {
        new DurabilityTask(event.getPlayer(), event.getPlayer().getItemInHand());
    }

    /**
     * A delayed task for updating item durabilities
     */
    private class DurabilityTask extends BukkitRunnable
    {

        Player    player;
        ItemStack item;
        int       actualDurability;
        int       vanillaDurability;

        /**
         * Initializes a new task to check for changes in item durability
         *
         * @param player player owning the item
         * @param item   item to check
         */
        public DurabilityTask(Player player, ItemStack item)
        {
            this.player = player;
            this.item = item;
            this.actualDurability = Durability.getDurability(item);
            this.vanillaDurability = item.getDurability();
            runTaskLater(plugin, 1);
        }

        /**
         * Runs the task, checking for differences in durability on the item
         */
        @Override
        public void run()
        {

            // Get the difference
            int difference = vanillaDurability - item.getDurability();

            // Do nothing when there's no difference
            if (difference == 0) return;

                // Losing durability
            else if (difference < 0)
            {
                ItemLoseDurabilityEvent event = new ItemLoseDurabilityEvent(player, item, -difference);
                plugin.getServer().getPluginManager().callEvent(event);
                difference = -event.getAmount();
            }

            // Gaining durability
            else if (difference > 0)
            {
                ItemGainDurabilityEvent event = new ItemGainDurabilityEvent(player, item, difference);
                plugin.getServer().getPluginManager().callEvent(event);
                difference = event.getAmount();
            }

            if (difference != 0)
            {

                // Send the message if applicable
                ItemMeta meta = item.hasItemMeta() ? item.getItemMeta() : null;
                String name = meta != null && meta.hasDisplayName() ? item.getItemMeta().getDisplayName() : ItemManager.getVanillaName(item);
                if (player.isOnline() && player.isValid() && plugin.isDurabilityMessageEnabled())
                {
                    player.sendMessage(TextFormatter.colorString(plugin.getDurabilityMessage()
                                                                         .replace("{current}", (actualDurability + difference) + "")
                                                                         .replace("{max}", Durability.getMaxDurability(item) + "")
                                                                         .replace("{item}", name))
                    );
                }

                // Apply the changes
                Durability.setDurability(item, actualDurability + difference);
            }
        }
    }
}
