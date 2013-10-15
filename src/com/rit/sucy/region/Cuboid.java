package com.rit.sucy.region;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * A cuboid that can be defined through coordinates, locations,
 * or locations with dimensions
 */
public class Cuboid extends Region {

    /**
     * Config nodes for saving/loading
     */
    private static final String
        WORLD = "world",
        X_MIN = "x-min",
        X_MAX = "x-max",
        Y_MIN = "y-min",
        Y_MAX = "y-max",
        Z_MIN = "z-min",
        Z_MAX = "z-max";

    private int xMin, xMax, yMin, yMax, zMin, zMax;

    /**
     * Constructor
     *
     * @param world region world
     * @param x1    x-coordinate 1
     * @param y1    y-coordinate 1
     * @param z1    z-coordinate 1
     * @param x2    x-coordinate 2
     * @param y2    y-coordinate 2
     * @param z2    z-coordinate 2
     */
    public Cuboid(World world, int x1, int y1, int z1, int x2, int y2, int z2) {

        this.world = world;

        // X-Coordinate
        if (x1 < x2) {
            xMin = x1;
            xMax = x2;
        }
        else {
            xMin = x2;
            xMax = x1;
        }

        // Y-Coordinate
        if (y1 < y2) {
            yMin = y1;
            yMax = y2;
        }
        else {
            yMin = y2;
            yMax = y1;
        }

        // Z-Coordinate
        if (z1 < z2) {
            zMin = z1;
            zMax = z2;
        }
        else {
            zMin = z2;
            zMax = z1;
        }
    }

    /**
     * Constructor
     *
     * @param point  starting point
     * @param width  x-coordinate size
     * @param height z-coordinate size
     * @param depth  y-coordinate size
     */
    public Cuboid(Location point, int width, int height, int depth) {
        this(point.getWorld(), point.getBlockX(), point.getBlockY(), point.getBlockZ(),
                point.getBlockX() + width, point.getBlockY() + height, point.getBlockZ() + depth);
    }

    /**
     * Constructor
     *
     * @param point1 first point
     * @param point2 second point
     */
    public Cuboid(Location point1, Location point2) {
        this(point1, point2.getBlockX() - point1.getBlockX(),
                point2.getBlockY() - point1.getBlockY(), point2.getBlockZ() - point1.getBlockZ());
    }

    /**
     * Retrieves the minimum corner of the cuboid that
     * does not change the region if modified
     *
     * @return minimum location of the cuboid
     */
    public Location getMinLoc() {
        return new Location(world, xMin, yMin, zMin);
    }

    /**
     * Retrieves the maximum corner of the cuboid that
     * does not change the region if modified
     *
     * @return maximum location of the cuboid
     */
    public Location getMaxLoc() {
        return new Location(world, xMax, yMax, zMax);
    }

    /**
     * @return width of the cuboid (x-direction)
     */
    public int getWidth() {
        return xMax - xMin + 1;
    }

    /**
     * @return height of the cuboid (y-direction)
     */
    public int getHeight() {
        return yMax - yMin + 1;
    }

    /**
     * @return depth of the cuboid (z-direction)
     */
    public int getDepth() {
        return zMax - zMin + 1;
    }

    /**
     * @return volume of the cuboid
     */
    @Override
    public int getVolume() {
        return getWidth() * getHeight() * getDepth();
    }

    /**
     * Checks if the player is contained within the cuboid
     *
     * @param player player to check
     * @return       true if in the cuboid, false otherwise
     */
    @Override
    public boolean contains(Player player) {
        return contains(player.getLocation());
    }

    /**
     * Checks if the location is contained within the cuboid
     *
     * @param loc location to check
     * @return    true if in the cuboid, false otherwise
     */
    @Override
    public boolean contains(Location loc) {
        return contains(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    /**
     * Checks if the coordinates are within the cuboid
     *
     * @param w world
     * @param x x-position
     * @param y y-position
     * @param z z-position
     * @return  true if in the cuboid, false otherwise
     */
    @Override
    public boolean contains(World w, int x, int y, int z) {
        return world.getName().equals(world.getName())
                && x >= xMin && x <= xMax
                && y >= yMin && y <= yMax
                && z >= zMin && z <= zMax;
    }

    /**
     * Checks if the cuboid is entirely contained within this cuboid
     *
     * @param cuboid cuboid to check
     * @return       true if entirely contained, false otherwise
     */
    public boolean contains(Cuboid cuboid) {
        return cuboid.world.getName().equals(world.getName())
                && cuboid.xMax <= xMax && cuboid.xMin >= xMin
                && cuboid.yMax <= yMax && cuboid.yMin >= yMin
                && cuboid.zMax <= zMax && cuboid.yMax >= yMax;
    }

    /**
     * Checks if the cuboid intersects this cuboid
     *
     * @param cuboid cuboid to check
     * @return       true if intersects, false otherwise
     */
    public boolean intersects(Cuboid cuboid) {
        return cuboid.world.getName().equals(world.getName())
                && cuboid.xMin <= xMax && cuboid.xMax >= xMin
                && cuboid.yMin <= yMax && cuboid.yMax >= yMin
                && cuboid.zMin <= zMax && cuboid.zMax >= zMin;
    }

    /**
     * Retrieves a list of all blocks in the cuboid
     *
     * @return list of all blocks in the cuboid
     */
    @Override
    public List<Block> getBlocks() {
        ArrayList<Block> list = new ArrayList<Block>(getVolume());
        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                for (int z = zMin; z <= zMax; z++) {
                    list.add(world.getBlockAt(x, y, z));
                }
            }
        }
        return list;
    }

    /**
     * Gets a random block in the cuboid
     *
     * @return random block
     */
    public Block getRandomBlock() {
        int dx = xMax - xMin;
        int dy = yMax - yMin;
        int dz = zMax - zMin;

        int x = xMin + (int)(Math.random() * dx);
        int y = yMin + (int)(Math.random() * dy);
        int z = zMin + (int)(Math.random() * dz);

        return new Location(world, x, y, z).getBlock();
    }

    /**
     * Saves the cuboid data to the config section
     *
     * @param config config section to save to
     */
    @Override
    public void save(ConfigurationSection config) {
        config.set(WORLD, world.getName());
        config.set(X_MIN, xMin);
        config.set(X_MAX, xMax);
        config.set(Y_MIN, yMin);
        config.set(Y_MAX, yMax);
        config.set(Z_MIN, zMin);
        config.set(Z_MAX, zMax);
    }

    /**
     * Loads the cuboid data from the config section
     *
     * @param config config section to load from
     * @return       loaded cuboid
     */
    public static Cuboid load(ConfigurationSection config) {
        World world = Bukkit.getWorld(config.getString(WORLD));
        int xMin = config.getInt(X_MIN);
        int xMax = config.getInt(X_MAX);
        int yMin = config.getInt(Y_MIN);
        int yMax = config.getInt(Y_MAX);
        int zMin = config.getInt(Z_MIN);
        int zMax = config.getInt(Z_MAX);

        return new Cuboid(world, xMin, yMin, zMin, xMax, yMax, zMax);
    }
}
