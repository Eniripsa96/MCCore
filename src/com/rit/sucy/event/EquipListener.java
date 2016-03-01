package com.rit.sucy.event;

import com.rit.sucy.MCCore;
import com.rit.sucy.version.VersionManager;
import com.rit.sucy.version.VersionPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * <p>A repeating task to check when players equip and unequip items</p>
 * <p>This is set up by MCCore already and should not be instantiated
 * by another plugin as it will cause duplicate results leading to
 * undesired behavior.</p>
 */
public class EquipListener implements Listener
{

    private final HashMap<String, ItemStack[]> equipment = new HashMap<String, ItemStack[]>();
    private final MCCore plugin;

    /**
     * <p>Creates a new listener for player equipment</p>
     * <p>You should not be instantiating this class as MCCore
     * handles it already.</p>
     *
     * @param plugin plugin reference
     */
    public EquipListener(MCCore plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        // Load player equipment for equip events
        for (Player player : VersionManager.getOnlinePlayers())
        {
            equipment.put(new VersionPlayer(player).getIdString(), player.getEquipment().getArmorContents());
        }
    }

    /**
     * Listens for inventory events for changing equipment
     *
     * @param event event details
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onClick(InventoryClickEvent event)
    {
        if (event.getInventory().getType() == InventoryType.CRAFTING || event.getInventory().getType() == InventoryType.PLAYER)
        {
            if (event.getSlotType() == InventoryType.SlotType.ARMOR || event.isShiftClick())
            {
                Player player = new VersionPlayer(event.getWhoClicked()).getPlayer();
                checkEquips(player);
            }
        }
    }

    /**
     * Listens for right clicking with armor for changing equipment
     *
     * @param event event details
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            String name = event.getPlayer().getItemInHand().getType().name();
            if (name.contains("_CHESTPLATE") || name.contains("_LEGGINGS") || name.contains("_BOOTS") || name.contains("_HELMET"))
                checkEquips(event.getPlayer());
        }
    }

    /**
     * Listens for the player dying and losing their equipment
     *
     * @param event event details
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event)
    {
        checkEquips(event.getEntity());
    }

    /**
     * Runs a task one tick later that evaluates the player's equipment for any changes
     *
     * @param player player to evaluate
     */
    private void checkEquips(final Player player)
    {
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable()
        {
            @Override
            public void run()
            {
                String id = new VersionPlayer(player).getIdString();
                ItemStack[] equips = player.getEquipment().getArmorContents();
                ItemStack[] previous = equipment.get(id);
                for (int i = 0; i < equips.length; i++)
                {
                    if (equips[i] == null && (previous != null && previous[i] != null))
                        plugin.getServer().getPluginManager().callEvent(new PlayerUnequipEvent(player, previous[i]));
                    else if (equips[i] != null && (previous == null || previous[i] == null))
                        plugin.getServer().getPluginManager().callEvent(new PlayerEquipEvent(player, equips[i]));
                    else if (previous != null && !equips[i].toString().equalsIgnoreCase(previous[i].toString()))
                    {
                        plugin.getServer().getPluginManager().callEvent(new PlayerUnequipEvent(player, previous[i]));
                        plugin.getServer().getPluginManager().callEvent(new PlayerEquipEvent(player, equips[i]));
                    }
                }
                equipment.put(id, equips);
            }
        }, 1);
    }
}
