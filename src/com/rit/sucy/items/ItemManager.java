package com.rit.sucy.items;

import com.rit.sucy.text.TextFormatter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Hashtable;

public class ItemManager {

    public static String getVanillaName(String bukkitName) {
        return getVanillaName(bukkitName, (short)0);
    }

    public static String getVanillaName(String bukkitName, short damage) {
        if (oddNames.containsKey(bukkitName))
            return oddNames.get(bukkitName);

        if (special.containsKey(bukkitName)) {
            return special.get(bukkitName).get(damage);
        }

        else return TextFormatter.format(bukkitName);
    }

    public static String getVanillaName(Material type) {
        return getVanillaName(type.name(), (short)0);
    }

    public static String getVanillaName(Material type, short damage) {
        return getVanillaName(type.name(), damage);
    }

    public static String getVanillName(ItemStack item) {
        return getVanillaName(item.getType().name(), item.getDurability());
    }

    private static final Hashtable<String, Hashtable<Short, String>> special = new Hashtable<String, Hashtable<Short, String>>() {{
        put("WOOD", new Hashtable<Short, String>() {{
            put((short) 0, "Oak Wood Planks");
            put((short) 1, "Spruce Wood Planks");
            put((short) 2, "Birch Wood Planks");
            put((short) 3, "Jungle Wood Planks");
        }});
        put("SAPLING", new Hashtable<Short, String>() {{
            put((short) 0, "Oak Sapling");
            put((short) 1, "Spruce Sapling");
            put((short) 2, "Birch Sapling");
            put((short) 3, "Jungle Sapling");
        }});
        put("LOG", new Hashtable<Short, String>() {{
            put((short) 0, "Oak Wood");
            put((short) 1, "Spruce Wood");
            put((short) 2, "Birch Wood");
            put((short) 3, "Jungle Wood");
        }});
        put("LEAVES", new Hashtable<Short, String>() {{
            put((short) 0, "Oak Leaves");
            put((short) 1, "Spruce Leaves");
            put((short) 2, "Birch Leaves");
            put((short) 3, "Jungle Leaves");
        }});
        put("SANDSTONE", new Hashtable<Short, String>() {{
            put((short)0, "Sandstone");
            put((short)1, "Chiseled Sandstone");
            put((short)2, "Smooth Sandstone");
        }});
        put("LONG_GRASS", new Hashtable<Short, String>() {{
            put((short)0, "Dead Shrub");
            put((short)1, "Tall Grass");
            put((short)2, "Fern");
        }});
        put("WOOL", new Hashtable<Short, String>() {{
            put((short)0, "White Wool");
            put((short)1, "Orange Wool");
            put((short)2, "Magenta Wool");
            put((short)3, "Light Blue Wool");
            put((short)4, "Yellow Wool");
            put((short)5, "Light Green Wool");
            put((short)6, "Pink Wool");
            put((short)7, "Gray Wool");
            put((short)8, "Light Gray Wool");
            put((short)9, "Cyan Wool");
            put((short)10, "Purple Wool");
            put((short)11, "Blue Wool");
            put((short)12, "Brown Wool");
            put((short)13, "Dark Green Wool");
            put((short)14, "Red Wool");
            put((short)15, "Black Wool");
        }});
        put("DOUBLE_STEP", new Hashtable<Short, String>() {{
            put((short)0, "Stone Slab");
            put((short)1, "Sandstone Slab");
            put((short)2, "Wooden Slab");
            put((short)3, "Cobblestone Slab");
            put((short)4, "Brick Slab");
            put((short)5, "Stone Brick Slab");
            put((short)6, "Nether Brick Slab");
            put((short)7, "Quartz Slab");
            put((short)8, "Smooth Stone Slab");
            put((short)9, "Smooth Sandstone Slab");
        }});
        put("STEP", new Hashtable<Short, String>() {{
            put((short)0, "Stone Slab");
            put((short)1, "Sandstone Slab");
            put((short)2, "Wooden Slab");
            put((short)3, "Cobblestone Slab");
            put((short)4, "Brick Slab");
            put((short)5, "Stone Brick Slab");
            put((short)6, "Nether Brick Slab");
            put((short)7, "Quartz Slab");
        }});
    }};

    private static final Hashtable<String, String> oddNames = new Hashtable<String, String>(){{
        put("LAPIS_ORE", "Lapis Lazuli Ore");
        put("LAPIS_BLOCK", "Lapis Lazuli Block");
        put("STICKY_PISTON_BASE", "Sticky Piston");
        put("PISTON_BASE", "Piston");
        put("YELLOW_FLOWER", "Dandelion");
        put("RED_FLOWER", "Red Rose");
        put("TNT", "TNT");
        put("MOSSY_COBBLESTONE", "Moss Stone");
        put("MOB_SPAWNER", "Monster Spawner");
        put("WOOD_STAIRS", "Wooden Stairs");
        put("JUNGLE_WOOD_STAIRS", "Wooden Stairs");
        put("SPRUCE_WOOD_STAIRS", "Wooden Stairs");
        put("BIRCH_WOOD_STAIRS", "Wooden Stairs");
        put("WORKBENCH", "Carting Table");
        put("SOIL", "Soil");
        put("STONE_PLATE", "Stone Pressure Plate");
        put("WOOD_PLATE", "Wooden Pressure Plate");
        put("REDSTONE_TORCH_ON", "Redstone Torch");
        put("STONE_BUTTON", "Button");
        put("CLAY", "Clay Block");
        put("JACK_O_LANTERN", "Jack-O-Lantern");
        put("TRAP_DOOR", "Trapdoor");
    }};
}
