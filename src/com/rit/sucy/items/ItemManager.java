package com.rit.sucy.items;

import com.rit.sucy.text.TextFormatter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Converts item names to/from vanilla names
 */
public class ItemManager {

    /**
     * Gets the vanilla name from the bukkit name
     *
     * @param bukkitName bukkit name
     * @return           vanilla name
     */
    public static String getVanillaName(String bukkitName) {
        return getVanillaName(bukkitName, (short)0);
    }

    /**
     * Gets the vanilla name from the bukkit name and damage ID
     *
     * @param bukkitName bukkit name
     * @param damage     damage ID
     * @return           vanilla name
     */
    public static String getVanillaName(String bukkitName, short damage) {
        bukkitName = bukkitName.toUpperCase();
        if (oddNames.containsKey(bukkitName))
            return oddNames.get(bukkitName);

        if (special.containsKey(bukkitName)) {
            HashMap<Short, String> data = special.get(bukkitName);
            if (data.containsKey(damage))
                return data.get(damage);
            else return data.get((short)0);
        }

        else return TextFormatter.format(bukkitName);
    }

    /**
     * Gets the vanilla name of the material
     *
     * @param type material
     * @return     vanilla name
     */
    public static String getVanillaName(Material type) {
        return getVanillaName(type.name(), (short)0);
    }

    /**
     * Gets the vanilla name of the material and damage ID
     *
     * @param type   material
     * @param damage damage ID
     * @return       vanilla name
     */
    public static String getVanillaName(Material type, short damage) {
        return getVanillaName(type.name(), damage);
    }

    /**
     * Gets the vanilla name of the item stack
     *
     * @param item item stack
     * @return     vanilla name
     */
    public static String getVanillaName(ItemStack item) {
        return getVanillaName(item.getType().name(), item.getDurability());
    }

    /**
     * Restores the bukkit name from the vanilla name
     *
     * @param vanillaName vanilla name
     * @return            bukkit name
     */
    public static String getBukkitName(String vanillaName) {
        for (Map.Entry<String, String> entry : oddNames.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(vanillaName))
                return entry.getKey();
        }
        for (Map.Entry<String, HashMap<Short, String>> entry : special.entrySet()) {
            Collection<String> values = entry.getValue().values();
            for (String value : values)
                if (value.equalsIgnoreCase(vanillaName))
                    return entry.getKey();
        }
        return vanillaName.replace(" ", "_").toUpperCase();
    }

    private static final HashMap<String, HashMap<Short, String>> special = new HashMap<String, HashMap<Short, String>>() {{
        put("WOOD", new HashMap<Short, String>() {{
            put((short) 0, "Oak Wood Planks");
            put((short) 1, "Spruce Wood Planks");
            put((short) 2, "Birch Wood Planks");
            put((short) 3, "Jungle Wood Planks");
            put((short) 4, "Acacia Wood Planks");
            put((short) 5, "Dark Oak Wood Planks");
        }});
        put(Material.LOG_2.name(), new HashMap<Short, String>() {{
            put((short)0, "Acacia Wood");
            put((short)1, "Dark Oak Wood");
        }});
        put("SAPLING", new HashMap<Short, String>() {{
            put((short) 0, "Oak Sapling");
            put((short) 1, "Spruce Sapling");
            put((short) 2, "Birch Sapling");
            put((short) 3, "Jungle Sapling");
        }});
        put("LOG", new HashMap<Short, String>() {{
            put((short) 0, "Oak Wood");
            put((short) 1, "Spruce Wood");
            put((short) 2, "Birch Wood");
            put((short) 3, "Jungle Wood");
        }});
        put("LEAVES", new HashMap<Short, String>() {{
            put((short) 0, "Oak Leaves");
            put((short) 1, "Spruce Leaves");
            put((short) 2, "Birch Leaves");
            put((short) 3, "Jungle Leaves");
        }});
        put("SANDSTONE", new HashMap<Short, String>() {{
            put((short)0, "Sandstone");
            put((short)1, "Chiseled Sandstone");
            put((short)2, "Smooth Sandstone");
        }});
        put("LONG_GRASS", new HashMap<Short, String>() {{
            put((short)0, "Dead Shrub");
            put((short)1, "Tall Grass");
            put((short)2, "Fern");
        }});
        put("WOOL", new HashMap<Short, String>() {{
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
        put("DOUBLE_STEP", new HashMap<Short, String>() {{
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
        put("STEP", new HashMap<Short, String>() {{
            put((short)0, "Stone Slab");
            put((short)1, "Sandstone Slab");
            put((short)2, "Wooden Slab");
            put((short)3, "Cobblestone Slab");
            put((short)4, "Brick Slab");
            put((short)5, "Stone Brick Slab");
            put((short)6, "Nether Brick Slab");
            put((short)7, "Quartz Slab");
        }});
        put("MONSTER_EGGS", new HashMap<Short, String>() {{
            put((short)0, "Stone Monster Egg");
            put((short)1, "Cobblestone Monster Egg");
            put((short)2, "Stone Brick Monster Egg");
        }});
        put("SMOOTH_BRICK", new HashMap<Short, String>() {{
            put((short)0, "Stone Bricks");
            put((short)1, "Mossy Stone Bricks");
            put((short)2, "Cracked Stone Bricks");
            put((short)3, "Chiseled Stone Bricks");
        }});
        put("COAL", new HashMap<Short, String>() {{
            put((short)0, "Coal");
            put((short)1, "Charcoal");
        }});
        put("INK_SACK", new HashMap<Short, String>() {{
            put((short)0, "Ink Sack");
            put((short)1, "Rose Red");
            put((short)2, "Cactus Green");
            put((short)3, "Cocoa Beans");
            put((short)4, "Lapis Lazuli");
            put((short)5, "Purple Dye");
            put((short)6, "Cyan Dye");
            put((short)7, "Light Gray Dye");
            put((short)8, "Gray Dye");
            put((short)9, "Pink Dye");
            put((short)10, "Lime Dye");
            put((short)11, "Dandelion Yellow");
            put((short)12, "Light Blue Dye");
            put((short)13, "Magenta Dye");
            put((short)14, "Orange Dye");
            put((short)15, "Bone Meal");
        }});
        put("POTION", new HashMap<Short, String>() {{
            put((short)0, "Water Bottle");
            put((short)16, "Awkward Potion");
            put((short)32, "Thick Potion");
            put((short)64, "Mundane Potion");
            put((short)8192, "Mundane Potion");
            put((short)8193, "Potion of Regeneration");
            put((short)8194, "Potion of Swiftness");
            put((short)8195, "Potion of Fire Resistance");
            put((short)8196, "Potion of Poison");
            put((short)8197, "Potion of Healing");
            put((short)8198, "Potion of Night Vision");
            put((short)8200, "Potion of Weakness");
            put((short)8201, "Potion of Strength");
            put((short)8202, "Potion of Slowness");
            put((short)8204, "Potion of Harming");
            put((short)8206, "Potion of Invisibility");
            put((short)8225, "Potion of Regeneration");
            put((short)8226, "Potion of Swiftness");
            put((short)8228, "Potion of Poison");
            put((short)8229, "Potion of Healing");
            put((short)8233, "Potion of Strength");
            put((short)8236, "Potion of Harming");
            put((short)8257, "Potion of Regeneration");
            put((short)8258, "Potion of Swiftness");
            put((short)8259, "Potion of Fire Resistance");
            put((short)8260, "Potion of Poison");
            put((short)8262, "Potion of Night Vision");
            put((short)8264, "Potion of Weakness");
            put((short)8265, "Potion of Strength");
            put((short)8266, "Potion of Slowness");
            put((short)8270, "Potion of Invisibility");
            put((short)8289, "Potion of Regeneration");
            put((short)8290, "Potion of Swiftness");
            put((short)8292, "Potion of Poison");
            put((short)8297, "Potion of Strength");
            put((short)16385, "Splash Potion of Regeneration");
            put((short)16386, "Splash Potion of Swiftness");
            put((short)16387, "Splash Potion of Fire Resistance");
            put((short)16388, "Splash Potion of Poison");
            put((short)16389, "Splash Potion of Healing");
            put((short)16390, "Splash Potion of Night Vision");
            put((short)16392, "Splash Potion of Weakness");
            put((short)16393, "Splash Potion of Strength");
            put((short)16394, "Splash Potion of Slowness");
            put((short)16396, "Splash Potion of Harming");
            put((short)16398, "Splash Potion of Invisibility");
            put((short)16417, "Splash Potion of Regeneration");
            put((short)16418, "Splash Potion of Swiftness");
            put((short)16420, "Splash Potion of Poison");
            put((short)16421, "Splash Potion of Healing");
            put((short)16425, "Splash Potion of Strength");
            put((short)16428, "Splash Potion of Harming");
            put((short)16449, "Splash Potion of Regeneration");
            put((short)16450, "Splash Potion of Swiftness");
            put((short)16451, "Splash Potion of Fire Resistance");
            put((short)16452, "Splash Potion of Poison");
            put((short)16454, "Splash Potion of Night Vision");
            put((short)16456, "Splash Potion of Weakness");
            put((short)16457, "Splash Potion of Strength");
            put((short)16458, "Splash Potion of Slowness");
            put((short)16462, "Splash Potion of Invisibility");
            put((short)16481, "Splash Potion of Regeneration");
            put((short)16482, "Splash Potion of Swiftness");
            put((short)16484, "Splash Potion of Poison");
            put((short)16489, "Splash Potion of Strength");
        }});
        put("MONSTER_EGG", new HashMap<Short, String>() {{
            put((short)0, "Spawn");
            put((short)50, "Spawn Creeper");
            put((short)51, "Spawn Skeleton");
            put((short)52, "Spawn Spider");
            put((short)54, "Spawn Zombie");
            put((short)55, "Spawn Slime");
            put((short)56, "Spawn Ghast");
            put((short)57, "Spawn Zombie Pigman");
            put((short)58, "Spawn Enderman");
            put((short)59, "Spawn Cave Spider");
            put((short)60, "Spawn Silverfish");
            put((short)61, "Spawn Blaze");
            put((short)62, "Spawn Magma Cube");
            put((short)65, "Spawn Bat");
            put((short)66, "Spawn Witch");
            put((short)90, "Spawn Pig");
            put((short)91, "Spawn Sheep");
            put((short)92, "Spawn Cow");
            put((short)93, "Spawn Chicken");
            put((short)94, "Spawn Squid");
            put((short)95, "Spawn Wolf");
            put((short)96, "Spawn Mooshroom");
            put((short)98, "Spawn Ocelot");
            put((short)100, "Spawn Horse");
            put((short)120, "Spawn Villager");
        }});
        put("WOOD_STEP", new HashMap<Short, String>() {{
            put((short)0, "Oak Wood Slab");
            put((short)1, "Spruce Wood Slab");
            put((short)2, "Birch Wood Slab");
            put((short)3, "Jungle Wood Slab");
            put((short)4, "Acacia Wood Slab");
            put((short)5, "Dark Oak Wood Slab");
        }});
        put("SKULL_ITEM", new HashMap<Short, String>() {{
            put((short)0, "Skeleton Skull");
            put((short)1, "Wither Skeleton Skull");
            put((short)2, "Zombie Head");
            put((short)3, "Head");
            put((short)4, "Creeper Head");
        }});
        put("FIREWORK_CHARGE", new HashMap<Short, String>() {{
            put((short)0, "Firework Star");
            put((short)1, "White Firework Star");
            put((short)2, "Orange Firework Star");
            put((short)3, "Magenta Firework Star");
            put((short)4, "Light Blue  Firework Star");
            put((short)5, "Yellow Firework Star");
            put((short)6, "Lime Firework Star");
            put((short)7, "Pink Firework Star");
            put((short)8, "Gray Firework Star");
            put((short)9, "Light Gray Firework Star");
            put((short)10, "Cyan Firework Star");
            put((short)11, "Purple Firework Star");
            put((short)12, "Blue Firework Star");
            put((short)13, "Brown Firework Star");
            put((short)14, "Green Firework Star");
            put((short)15, "Red Firework Star");
            put((short)16, "Black Firework Star");
        }});
        put("QUARTZ_BLOCK", new HashMap<Short, String>() {{
            put((short)0, "Quartz Block");
            put((short)1, "Chiseled Quartz");
            put((short)2, "Quartz Pillar");
        }});
        put("RAW_FISH", new HashMap<Short, String>() {{
            put((short)0, "Raw Fish");
            put((short)1, "Raw Salmon");
            put((short)2, "Clownfish");
            put((short)3, "Pufferfish");
        }});
        put("COOKED_FISH", new HashMap<Short, String>() {{
            put((short)0, "Cooked Fish");
            put((short)1, "Cooked Salmon");
        }});
        put("STAINED_GLASS", new HashMap<Short, String>() {{
            put((short)0, "White Stained Glass");
            put((short)1, "Orange Stained Glass");
            put((short)2, "Magenta Stained Glass");
            put((short)3, "Light Blue Stained Glass");
            put((short)4, "Yellow Stained Glass");
            put((short)5, "Lime Stained Glass");
            put((short)6, "Pink Stained Glass");
            put((short)7, "Gray Stained Glass");
            put((short)8, "Light Gray Stained Glass");
            put((short)9, "Cyan Stained Glass");
            put((short)10, "Purple Stained Glass");
            put((short)11, "Blue Stained Glass");
            put((short)12, "Brown Stained Glass");
            put((short)13, "Green Stained Glass");
            put((short)14, "Red Stained Glass");
            put((short)15, "Black Stained Glass");
        }});
        put("STAINED_CLAY", new HashMap<Short, String>() {{
            put((short)0, "White Stained Clay");
            put((short)1, "Orange Stained Clay");
            put((short)2, "Magenta Stained Clay");
            put((short)3, "Light Blue Stained Clay");
            put((short)4, "Yellow Stained Clay");
            put((short)5, "Lime Stained Clay");
            put((short)6, "Pink Stained Clay");
            put((short)7, "Gray Stained Clay");
            put((short)8, "Light Gray Stained Clay");
            put((short)9, "Cyan Stained Clay");
            put((short)10, "Purple Stained Clay");
            put((short)11, "Blue Stained Clay");
            put((short)12, "Brown Stained Clay");
            put((short)13, "Green Stained Clay");
            put((short)14, "Red Stained Clay");
            put((short)15, "Black Stained Clay");
        }});
        put("STAINED_GLASS_PANE", new HashMap<Short, String>() {{
            put((short)0, "White Stained Glass Pane");
            put((short)1, "Orange Stained Glass Pane");
            put((short)2, "Magenta Stained Glass Pane");
            put((short)3, "Light Blue Stained Glass Pane");
            put((short)4, "Yellow Stained Glass Pane");
            put((short)5, "Lime Stained Glass Pane");
            put((short)6, "Pink Stained Glass Pane");
            put((short)7, "Gray Stained Glass Pane");
            put((short)8, "Light Gray Stained Glass Pane");
            put((short)9, "Cyan Stained Glass Pane");
            put((short)10, "Purple Stained Glass Pane");
            put((short)11, "Blue Stained Glass Pane");
            put((short)12, "Brown Stained Glass Pane");
            put((short)13, "Green Stained Glass Pane");
            put((short)14, "Red Stained Glass Pane");
            put((short)15, "Black Stained Glass Pane");
        }});
        put("CARPET", new HashMap<Short, String>() {{
            put((short)0, "White Carpet");
            put((short)1, "Orange Carpet");
            put((short)2, "Magenta Carpet");
            put((short)3, "Light Blue Carpet");
            put((short)4, "Yellow Carpet");
            put((short)5, "Lime Carpet");
            put((short)6, "Pink Carpet");
            put((short)7, "Gray Carpet");
            put((short)8, "Light Gray Carpet");
            put((short)9, "Cyan Carpet");
            put((short)10, "Purple Carpet");
            put((short)11, "Blue Carpet");
            put((short)12, "Brown Carpet");
            put((short)13, "Green Carpet");
            put((short)14, "Red Carpet");
            put((short)15, "Black Carpet");
        }});
        put("DIRT", new HashMap<Short, String>() {{
            put((short)0, "Dirt");
            put((short)1, "Dirt");
            put((short)2, "Podzol");
        }});
        put("COBBLE_WALL", new HashMap<Short, String>() {{
            put((short)0, "Cobblestone Wall");
            put((short)1, "Mossy Cobblestone Wall");
        }});
        put("RED_ROSE", new HashMap<Short, String>() {{
            put((short)0, "Poppy");
            put((short)1, "Blue Orchid");
            put((short)2, "Allium");
            put((short)3, "Azure Bluet");
            put((short)4, "Red Tulip");
            put((short)5, "Orange Tulip");
            put((short)6, "White Tulip");
            put((short)7, "Pink Tulip");
            put((short)8, "Oxeye Daisy");
        }});
        put("STONE", new HashMap<Short, String>() {{
            put((short)0, "Stone");
            put((short)1, "Granite");
            put((short)2, "Polished Granite");
            put((short)3, "Diorite");
            put((short)4, "Polished Diorite");
            put((short)5, "Andesite");
            put((short)6, "Polished Andesite");
        }});
        put("DOUBLE_PLANT", new HashMap<Short, String>() {{
            put((short)0, "Sunflower");
            put((short)1, "Lilac");
            put((short)2, "Double Tallgrass");
            put((short)3, "Large Fern");
            put((short)4, "Rose Bush");
            put((short)5, "Peony");
        }});
    }};

    private static final HashMap<String, String> oddNames = new HashMap<String, String>(){{
        put("LAPIS_ORE", "Lapis Lazuli Ore");
        put("LAPIS_BLOCK", "Lapis Lazuli Block");
        put("STICKY_PISTON_BASE", "Sticky Piston");
        put("PISTON_STICKY_BASE", "Sticky Piston");
        put("PISTON_BASE", "Piston");
        put("YELLOW_FLOWER", "Dandelion");
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
        put("HUGE_MUSHROOM_1", "Brown Mushroom");
        put("HUGE_MUSHROOM_2", "Red Mushroom");
        put("THIN_GLASS", "Glass Pane");
        put("VINE", "Vines");
        put("SMOOTH_STAIRS", "Stone Brick Stairs");
        put("IRON_SPADE", "Iron Shovel");
        put("FLINT_AND_STEEL", "Flint and Steel");
        put("WOOD_SWORD", "Wooden Sword");
        put("WOOD_PICKAXE", "Wooden Pickaxe");
        put("WOOD_SPADE", "Wooden Shovel");
        put("WOOD_AXE", "Wooden Axe");
        put("STONE_SPADE", "Stone Spade");
        put("DIAMOND_SPADE", "Diamond Shovel");
        put("GOLD_SPADE", "Golden Shovel");
        put("GOLD_PICKAXE", "Golden Pickaxe");
        put("GOLD_AXE", "Golden Axe");
        put("GOLD_HOE", "Golden Hoe");
        put("GOLD_SWORD", "Golden Sword");
        put("SULPHUR", "Gunpowder");
        put("WOOD_HOE", "Wooden Hoe");
        put("LEATHER_HELMET", "Leather Cap");
        put("LEATHER_CHESTPLATE", "Leather Tunic");
        put("LEATHER_LEGGINGS", "Leather Pants");
        put("CHAINMAIL_HELMET", "Chain Helmet");
        put("CHAINMAIL_LEGGINGS", "Chain Leggings");
        put("CHAINMAIL_CHESTPLATE", "Chain Chestplate");
        put("CHAINMAIL_BOOTS", "Chain Boots");
        put("GOLD_HELMET", "Golden Helmet");
        put("GOLD_CHESTPLATE", "Golden Chestplate");
        put("GOLD_LEGGINGS", "Golden Leggings");
        put("GOLD_BOOTS", "Golden Boots");
        put("PORK", "Raw Porkchop");
        put("GRILLED_PORK", "Cooked Porkchop");
        put("WOOD_DOOR", "Wooden Door");
        put("CLAY_BALL", "Clay");
        put("SLIME_BALL", "Slimeball");
        put("WATCH", "Clock");
        put("COOKED_BEEF", "Steak");
        put("DIODE", "Redstone Repeater");
        put("WATER_LILY", "Lily Pad");
        put("CAULDRON_ITEM", "Cauldron");
        put("BREWING_STAND_ITEM", "Brewing Stand");
        put("NETHER_FENCE", "Nether Brick Fence");
        put("NETHER_WARTS", "Nether Wart");
        put("NETHER_STALK", "Nether Wart");
        put("ENDER_STONE", "End Stone");
        put("SPECKLED_MELON", "Glistering Melon");
        put("EXP_BOTTLE", "Bottle o' Enchanting");
        put("FIREBALL", "Fire Charge");
        put("GOLD_RECORD", "Music Disc");
        put("GREEN_RECORD", "Music Disc");
        put("RECORD_3", "Music Disc");
        put("RECORD_4", "Music Disc");
        put("RECORD_5", "Music Disc");
        put("RECORD_6", "Music Disc");
        put("RECORD_7", "Music Disc");
        put("RECORD_8", "Music Disc");
        put("RECORD_9", "Music Disc");
        put("RECORD_10", "Music Disc");
        put("RECORD_11", "Music Disc");
        put("RECORD_12", "Music Disc");
        put("REDSTONE_LAMP_OFF", "Redstone Lamp");
        put("REDSTONE_LAMP_ON", "Redstone Lamp");
        put("REDSTONE_TORCH_OFF", "Redstone Torch");
        put("BOOK_AND_QUILL", "Book and Quill");
        put("COMMAND", "Command Block");
        put("FLOWER_POT_ITEM", "Flower Pot");
        put("CARROT_ITEM", "Carrot");
        put("POTATO_ITEM", "Potato");
        put("WOOD_BUTTON", "Button");
        put("FIREWORK", "Firework Rocket");
        put("REDSTONE_BLOCK", "Block of Redstone");
        put("DAYLIGHT_DETECTOR", "Daylight Sensor");
        put("QUARTZ", "Nether Quartz");
        put("GOLD_PLATE", "Weighted Pressure Plate (Light)");
        put("IRON_PLATE", "Weighted Pressure Plate (Heavy)");
        put("LEASH", "Lead");
        put("ACACIA_STAIRS", "Acacia Wood Stairs");
        put("DARK_OAK_STAIRS", "Dark Oak Wood Stairs");
        put("EMERALD_BLOCK", "Block of Emerald");
        put("COAL_BLOCK", "Block of Coal");
        put("HAY_BLOCK", "Hay Bale");
        put("REDSTONE_BLOCK", "Block of Redstone");
        put("IRON_BARDING", "Iron Horse Armor");
        put("GOLD_BARDING", "Gold Horse Armor");
        put("DIAMOND_BARDING", "Diamond Horse Armor");
        put("QUARTZ_ORE", "Nether Quartz Ore");
    }};
}
