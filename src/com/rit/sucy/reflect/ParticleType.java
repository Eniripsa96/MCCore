package com.rit.sucy.reflect;

public enum ParticleType {

    ANGRY_VILLAGER ("angryVillager"),
    BUBBLE ("bubble"),
    CLOUD ("cloud"),
    CRIT ("crit"),
    DEATH_SUSPEND ("deathSuspend"),
    DRIP_LAVA ("dripLava"),
    DRIP_WATER ("dripWater"),
    ENCHANTMENT_TABLE ("enchantmenttable"),
    EXPLODE ("explode"),
    FIREWORKS_SPARK ("fireworksSpark"),
    FLAME ("flame"),
    FOOTSTEP ("footstep"),
    HAPPY_VILLAGER ("happyVillager"),
    HEART ("heart"),
    HUGE_EXPLOSION ("hugeexplosion"),
    INSTANT_SPELL ("instantSpell"),
    LARGE_EXPLODE ("largeexplode"),
    LARGE_SMOKE ("largesmoke"),
    LAVA ("lava"),
    MAGIC_CRIT ("magicCrit"),
    MOB_SPELL ("mobSpell"),
    MOB_SPELL_AMBIENT ("mobSpellAmbient"),
    NOTE ("note"),
    PORTAL ("portal"),
    RED_DUST ("reddust"),
    SLIME ("slime"),
    SNOWBALL_POOF ("snowballpoof"),
    SNOW_SHOVEL ("snowshovel"),
    SPELL ("spell"),
    SPLASH ("splash"),
    SUSPEND ("suspend"),
    TOWN_AURA ("townaura"),
    WITCH_MAGIC ("witchMagic"),

    ;

    private final String packetString;

    private ParticleType(String packetString) {
        this.packetString = packetString;
    }

    public String getPacketString() {
        return packetString;
    }
}
