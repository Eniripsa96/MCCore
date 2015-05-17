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
public class Protection
{

    /**
     * Checks whether or not an entity can be attacked by a player
     *
     * @param attacker player trying to attack
     * @param target   target of the attack
     *
     * @return true if the target can be attacked, false otherwise
     */
    public static boolean canAttack(LivingEntity attacker, LivingEntity target)
    {
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
    public static boolean canAttack(LivingEntity attacker, LivingEntity target, boolean passiveAlly)
    {
        if (attacker == target) return false;
        if (target instanceof Tameable)
        {
            Tameable entity = (Tameable) target;
            if (entity.isTamed())
            {
                return canAttack(attacker, (Player) entity.getOwner(), false);
            }
        }
        else if (passiveAlly && target instanceof Animals)
        {
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
    public static boolean isAlly(LivingEntity attacker, LivingEntity target)
    {
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
    public static List<LivingEntity> canAttack(LivingEntity attacker, List<LivingEntity> targets)
    {
        List<LivingEntity> list = new ArrayList<LivingEntity>();
        for (LivingEntity entity : targets)
        {
            if (canAttack(attacker, entity))
            {
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
    public static List<LivingEntity> cannotAttack(LivingEntity attacker, List<LivingEntity> targets)
    {
        List<LivingEntity> list = new ArrayList<LivingEntity>();
        for (LivingEntity entity : targets)
        {
            if (!canAttack(attacker, entity))
            {
                list.add(entity);
            }
        }
        return list;
    }
}
