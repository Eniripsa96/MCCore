package com.rit.sucy.region;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for regions
 */
public abstract class Region {

    protected World world;

    /**
     * @return world containing to the region
     */
    public World getWorld() {
        return world;
    }

    /**
     * Checks if the player is contained within the region
     *
     * @param player player to check
     * @return       true if in the cuboid, false otherwise
     */
    public abstract boolean contains(Player player);

    /**
     * Checks if the location is contained within the region
     *
     * @param loc location to check
     * @return    true if in the cuboid, false otherwise
     */
    public abstract boolean contains(Location loc);

    /**
     * Checks if the coordinates are within the region
     *
     * @param w world
     * @param x x-position
     * @param y y-position
     * @param z z-position
     * @return  true if in the cuboid, false otherwise
     */
    public abstract boolean contains(World w, int x, int y, int z);

    /**
     * Retrieves a list of all players in the cuboid
     *
     * @return list of all players in the cuboid
     */
    public List<Player> getPlayers() {
        ArrayList<Player> list = new ArrayList<Player>();
        for (Player player : getWorld().getPlayers()) {
            if (contains(player)) {
                list.add(player);
            }
        }
        return list;
    }

    /**
     * Retrieves a list of all blocks in the cuboid
     *
     * @return list of all blocks in the cuboid
     */
    public abstract List<Block> getBlocks();

    /**
     * @return volume of the cuboid
     */
    public abstract int getVolume();

    /**
     * Saves the cuboid data to the config section
     *
     * @param config config section to save to
     */
    public abstract void save(ConfigurationSection config);
}
