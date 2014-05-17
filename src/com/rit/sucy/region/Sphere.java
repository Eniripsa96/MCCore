package com.rit.sucy.region;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Sphere extends Region {

    /**
     * Config nodes for saving/loading
     */
    private static final String WORLD = "world";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String Z = "z";
    private static final String R = "r";

    private int x;
    private int y;
    private int z;
    private int r;

    /**
     * Constructor from a location and radius
     *
     * @param loc    center point
     * @param radius radius
     */
    public Sphere(Location loc, int radius) {
        world = loc.getWorld();
        x = loc.getBlockX();
        y = loc.getBlockY();
        z = loc.getBlockZ();
        r = radius;
    }

    /**
     * Constructor from coordinates
     *
     * @param world world
     * @param x     x-coordinate
     * @param y     y-coordinate
     * @param z     z-coordinate
     * @param r     radius
     */
    public Sphere(World world, int x, int y, int z, int r) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.r = r;
    }

    /**
     * @return approximate volume of the sphere
     */
    @Override
    public int getVolume() {
        return (int)(4 * Math.PI * r * r * r / 3);
    }

    /**
     * @return sphere radius
     */
    public int getRadius() {
        return r;
    }

    /**
     * Returns a center location that does not change the region if modified
     *
     * @return center location
     */
    public Location getCenter() {
        return new Location(world, x, y, z);
    }

    /**
     * Checks if the player is contained within the sphere
     *
     * @param player player to check
     * @return       true if contained in the sphere, false otherwise
     */
    @Override
    public boolean contains(Player player) {
        return contains(player.getLocation());
    }

    /**
     * Checks if the location is contained within the sphere
     *
     * @param loc location to check
     * @return    true if contained, false otherwise
     */
    @Override
    public boolean contains(Location loc) {
        return contains(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    /**
     * Checks if the point at the given coordinates is contained within the sphere
     *
     * @param w world
     * @param x x-position
     * @param y y-position
     * @param z z-position
     * @return  trueif contained, false otherwise
     */
    @Override
    public boolean contains(World w, int x, int y, int z) {
        return world.getName().equals(w.getName()) && r * r < distance(x, y, z);
    }

    /**
     * Checks if teh sphere is completely contained by this sphere
     *
     * @param sphere sphere to check
     * @return       true if contained, false otherwise
     */
    public boolean contains(Sphere sphere) {
        return sphere.world.getName().equals(world.getName())
                && sphere.x + sphere.r - 1 <= x + r - 1
                && sphere.x - sphere.r + r >= x - r + 1
                && sphere.y + sphere.r - 1 <= y + r - 1
                && sphere.y - sphere.r + 1 >= y - r + 1
                && sphere.z + sphere.r - 1 <= z + r - 1
                && sphere.z - sphere.r + 1 >= z - r + 1;
    }

    /**
     * Checks if the sphere is intersected by this sphere
     *
     * @param sphere sphere to check
     * @return       true if intersects, false otherwise
     */
    public boolean intersects(Sphere sphere) {
        int d = sphere.r + r;
        return d * d > distance(sphere.x, sphere.y, sphere.z);
    }

    /**
     * Gets the distance from the center that the coordinates are
     *
     * @param x x-coordinate
     * @param y y-coorindate
     * @param z z-coordinate
     * @return  distance from center to the coordinates
     */
    private int distance(int x, int y, int z) {
        int xDif = this.x - x;
        int yDif = this.y - y;
        int zDif = this.z - z;
        return xDif * xDif + yDif * yDif + zDif * zDif;
    }

    /**
     * Gets all blocks contained within the sphere
     *
     * @return list of contained blocks
     */
    @Override
    public List<Block> getBlocks() {
        ArrayList<Block> list = new ArrayList<Block>();
        for (int i = x - r + 1; i < x + r; i++) {
            for (int j = y - r + 1; j < y + r; j++) {
                for (int k = z - r + 1; k < z + r; k++) {
                    if (contains(world, i, j, k)) {
                        list.add(world.getBlockAt(i, j, k));
                    }
                }
            }
        }
        return list;
    }

    /**
     * Saves the sphere to a config section
     *
     * @param config config section to save to
     */
    @Override
    public void save(ConfigurationSection config) {
        config.set(WORLD, world.getName());
        config.set(X, x);
        config.set(Y, y);
        config.set(Z, z);
        config.set(R, r);
    }

    /**
     * Loads a sphere from the config
     *
     * @param config config section to load from
     * @return       loaded sphere
     */
    public static Sphere load(ConfigurationSection config) {
        World world = Bukkit.getWorld(config.getString(WORLD));
        int x = config.getInt(X);
        int y = config.getInt(Y);
        int z = config.getInt(Z);
        int r = config.getInt(R);

        return new Sphere(world, x, y, z, r);
    }
}
