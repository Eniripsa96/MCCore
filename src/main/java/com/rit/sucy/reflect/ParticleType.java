/**
 * MCCore
 * com.rit.sucy.reflect.ParticleType
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
package com.rit.sucy.reflect;

/**
 * List of particle types usable in the reflection particle class
 */
public enum ParticleType {

    /**
     * A cracked gray heart used when attacking a villager
     * <br/>
     * Speed: no effect
     */
    ANGRY_VILLAGER("angryVillager"),

    /**
     * The bubble particle from swimming entities
     * <br/>
     * Speed: how fast the particle moves away
     */
    BUBBLE("bubble"),

    /**
     * A white cloud that is used when a mob dies
     * <br/>
     * Speed: how fast the particle moves away
     */
    CLOUD("cloud"),

    /**
     * A light brown cross used when performing critical hits or launching arrows
     * <br/>
     * Speed: how fast the particle moves away
     */
    CRIT("crit"),

    /**
     * A small gray square used near bedrock or in the void
     * <br/>
     * Speed: no effect
     */
    DEATH_SUSPEND("deathSuspend"),

    /**
     * An orange drip used on blocks beneath lava
     * <br/>
     * Speed: no effect
     */
    DRIP_LAVA("dripLava"),

    /**
     * A blue drip used on blocks beneath water
     * <br/>
     * Speed: no effect
     */
    DRIP_WATER("dripWater"),

    /**
     * A random white symbol used around enchanting tables
     * <br/>
     * Speed: the spread of the effect
     */
    ENCHANTMENT_TABLE("enchantmenttable"),

    /**
     * The smaller explosion effect of TNT and creepers
     * <br/>
     * Speed: how fast the particle moves away
     */
    EXPLODE("explode"),

    /**
     * The particle used for white sparkling star trail of launched fireworks
     * <br/>
     * Speed: how fast the particle moves away
     */
    FIREWORKS_SPARK("fireworksSpark"),

    /**
     * A flame used with torches, active furnaces, magma cubes, and monster spawners
     * <br/>
     * Speed: how fast the particle moves away
     */
    FLAME("flame"),

    /**
     * A transparent gray square not used in the game
     * <br/>
     * Speed: no effect
     */
    FOOTSTEP("footstep"),

    /**
     * A green star used when trading with a villager
     * <br/>
     * Speed: no effect
     */
    HAPPY_VILLAGER("happyVillager"),

    /**
     * A red heart used when taming or breeding animals
     * <br/>
     * Speed: no effect
     */
    HEART("heart"),

    /**
     * The larger explosion effect of TNT and creepers
     * <br/>
     * Speed: how large the particle is
     */
    HUGE_EXPLOSION("hugeexplosion"),

    /**
     * A white cross used when instant splash hits something
     * <br/>
     * Speed: how fast the particle moves horizontally
     */
    INSTANT_SPELL("instantSpell"),

    /**
     * The explosion effect of ghast fireballs and wither skulls
     * <br/>
     * Speed: no effect
     */
    LARGE_EXPLODE("largeexplode"),

    /**
     * A large gray cloud used by fire, blazes, and furnace minecarts
     * <br/>
     * Speed: how fast the particle moves away
     */
    LARGE_SMOKE("largesmoke"),

    /**
     * A spark used near lava
     * <br/>
     * Speed: no effect
     */
    LAVA("lava"),

    /**
     * A cyan star used when an enchanted weapon is used
     * <br/>
     * Speed: how fast the particle moves away
     */
    MAGIC_CRIT("magicCrit"),

    /**
     * A randomly colored swirl used with entities with active potion effects
     * <br/>
     * Speed: turns it black when speed is 0
     */
    MOB_SPELL("mobSpell"),

    /**
     * A randomly colored transparent swirl used with beacon buffs
     * <br/>
     * Speed: turns it black when speed is 0
     */
    MOB_SPELL_AMBIENT("mobSpellAmbient"),

    /**
     * A randomly colored note used with note blocks
     * <br/>
     * Speed: turns it green when speed is 0
     */
    NOTE("note"),

    /**
     * A purple cloud used with portals and ender entities
     * <br/>
     * Speed: the spread of the effect
     */
    PORTAL("portal"),

    /**
     * A randomly colored cloud used by redstone objects
     * <br/>
     * Speed: turns it red when speed is 0
     */
    RED_DUST("reddust"),

    /**
     * A chunk of the slime ball icon used by slimes
     * <br/>
     * Speed: no effect
     */
    SLIME("slime"),

    /**
     * A chunk of the snowball icon used when snowballs or eggs hit something
     * <br/>
     * Speed: no effect
     */
    SNOWBALL_POOF("snowballpoof"),

    /**
     * A small white cloud not used in the game
     * <br/>
     * Speed: how fast the particle moves away
     */
    SNOW_SHOVEL("snowshovel"),

    /**
     * A white swirl used when splash potions hit something
     * <br/>
     * Speed: how fast the particle moves horizontally
     */
    SPELL("spell"),

    /**
     * A blue drop used with swimming entities and rain
     * <br/>
     * Speed: no effect
     */
    SPLASH("splash"),

    /**
     * A small blue square used by water
     * <br/>
     * Speed: no effect
     */
    SUSPEND("suspend"),

    /**
     * A small gray square used on mycelium
     * <br/>
     * Speed: no effect
     */
    TOWN_AURA("townaura"),

    /**
     * A blue drop used when fishing
     * <br/>
     * Speed: how fast the particle moves away
     */
    WAKE("wake"),

    /**
     * A purple cross used with witch magic
     * <br/>
     * Speed: how fast the particle moves horizontally
     */
    WITCH_MAGIC("witchMagic"),;

    private final String packetString;

    private ParticleType(String packetString) {
        this.packetString = packetString;
    }

    /**
     * Retrieves the string used by the packet for the particle
     *
     * @return the identifying packet string
     */
    public String getPacketString() {
        return packetString;
    }
}
