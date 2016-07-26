/**
 * MCCore
 * com.rit.sucy.player.Protection
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
package com.rit.sucy.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides checks for protection plugins
 */
public class Protection {

    /**
     * Checks whether or not an entity can be attacked by a player
     *
     * @param attacker player trying to attack
     * @param target   target of the attack
     *
     * @return true if the target can be attacked, false otherwise
     */
    public static boolean canAttack(LivingEntity attacker, LivingEntity target) {
        return canAttack(attacker, target, false);
    }

    /**
     * Checks whether or not an entity can be attacked by a player
     *
     * @param attacker    player trying to attack
     * @param target      target of the attack
     * @param passiveAlly whether or not passive mobs are considered allies
     *
     * @return true if the target can be attacked, false otherwise
     */
    public static boolean canAttack(LivingEntity attacker, LivingEntity target, boolean passiveAlly) {
        if (attacker == target) return false;
        if (target instanceof Tameable) {
            Tameable entity = (Tameable) target;
            if (entity.isTamed()) {
                return canAttack(attacker, (Player) entity.getOwner(), false);
            }
        } else if (passiveAlly && target instanceof Animals) {
            return false;
        }
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(attacker, target, EntityDamageEvent.DamageCause.CUSTOM, 1.0);
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    /**
     * Checks if the target is an ally
     *
     * @param attacker entity attacking
     * @param target   ally of the player
     *
     * @return true if ally, false otherwise
     */
    public static boolean isAlly(LivingEntity attacker, LivingEntity target) {
        return !canAttack(attacker, target);
    }

    /**
     * Retrieves all living entities the entity can attack from the list
     *
     * @param attacker entity that is attacking
     * @param targets  targets the player is trying to attack
     *
     * @return list of targets the player can attack
     */
    public static List<LivingEntity> canAttack(LivingEntity attacker, List<LivingEntity> targets) {
        List<LivingEntity> list = new ArrayList<LivingEntity>();
        for (LivingEntity entity : targets) {
            if (canAttack(attacker, entity)) {
                list.add(entity);
            }
        }
        return list;
    }

    /**
     * Retrieves all living entities the entity cannot attack from the list
     *
     * @param attacker entity that is attacking
     * @param targets  targets the player is trying to attack
     *
     * @return list of targets the player cannot attack
     */
    public static List<LivingEntity> cannotAttack(LivingEntity attacker, List<LivingEntity> targets) {
        List<LivingEntity> list = new ArrayList<LivingEntity>();
        for (LivingEntity entity : targets) {
            if (!canAttack(attacker, entity)) {
                list.add(entity);
            }
        }
        return list;
    }
}
