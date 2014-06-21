package com.rit.sucy.region;

import org.bukkit.Location;

import java.util.Iterator;

public class BorderIterator implements Iterable<Location>, Iterator<Location> {

    private Region region;
    private Location loc;
    private int x1, y1, z1, x2, y2, z2, x3, y3, z3;

    public BorderIterator(Region region, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax) {
        this.region = region;
        this.loc = new Location(region.getWorld(), xMin, yMin, zMin);
        this.x1 = (this.x3 = xMin) - 1;
        this.y1 = this.y3 = yMin;
        this.z1 = this.z3 = zMin;
        this.x2 = xMax;
        this.y2 = yMax;
        this.z2 = zMax;
    }

    public boolean hasNext() {
        return this.x3 < this.x2 || this.y3 < this.y2 && this.z3 < this.z2;
    }

    public Location next() {
        if (z3 == z1 || z3 == z2 || y3 == y1 || y3 == y2) {
            x3++;
        }
        else {
            x3 += x2 - x1;
        }
        if (x3 > x2) {
            x3 = x1;
            if (z3 == z1 || z3 == z2) {
                y3++;
            }
            else {
                y3 += y2 - y1;
            }
            if (y3 > y2) {
                y3 = y1;
                z3++;
            }
        }
        loc.add(x3 - loc.getBlockX(), y3 - loc.getBlockY(), z3 - loc.getBlockZ());
        return loc;
    }

    public void remove() {
        throw new UnsupportedOperationException("Cannot remove a location from a region iterator");
    }

    public Iterator<Location> iterator() {
        return this;
    }
}
