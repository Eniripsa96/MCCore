package com.rit.sucy.version;

import com.rit.sucy.player.PlayerUUIDs;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * A player implementation compatible across versions
 */
public class VersionPlayer
{

    private static final String  ID_REGEX   = ".{8}-.{4}-.{4}-.{4}-.{12}";
    private static final Pattern ID_PATTERN = Pattern.compile(ID_REGEX);

    private Object id;
    private String idString;

    /**
     * <p>Initializes a new VersionPlayer from a player's UUID</p>
     * <p>You probably don't need to be using this constructor
     * if you have the UUID of players.</p>
     *
     * @param id ID of the player
     */
    public VersionPlayer(UUID id)
    {
        this.id = id;
        this.idString = id.toString().toLowerCase();
    }

    /**
     * <p>Initializes a new VersionPlayer from a player's ID string</p>
     * <p>The ID string could either be a UUID toString or a player's
     * name. If the ID is a UUID, then it will simply be parsed and
     * used to represent the player. If it is the player's name,
     * it will be converted into a UUID if the server is at least
     * on version 1.7.5 or remain the players name otherwise.</p>
     * <p>If the ID is a player's name and they have not logged on
     * since MCCore 1.16 or later has been installed, this will
     * query the Minecraft servers for the player's UUID which
     * can cause some lag if not done during the server's startup.</p>
     *
     * @param id UUID or name of the player
     */
    public VersionPlayer(Object id)
    {
        if (VersionManager.isVersionAtLeast(VersionManager.V1_7_5))
        {
            if (ID_PATTERN.matcher(id.toString()).matches())
            {
                this.id = UUID.fromString(id.toString());
            }
            else
            {
                this.id = VersionManager.getOfflinePlayer(id.toString()).getUniqueId();
            }
        }
        else this.id = id;

        this.idString = this.id.toString().toLowerCase();
    }

    /**
     * <p>Represents the given player as a VersionPlayer, letting
     * you save the player for different versions.</p>
     *
     * @param player Bukkit player object
     */
    public VersionPlayer(Player player)
    {
        this((OfflinePlayer) player);
    }

    /**
     * <p>Represents the given player as a VersionPlayer, letting
     * you save the player for different versions.</p>
     *
     * @param player Bukkit player object
     */
    public VersionPlayer(OfflinePlayer player)
    {
        if (VersionManager.isVersionAtLeast(VersionManager.MC_1_7_5_MIN))
        {
            this.id = player.getUniqueId();
        }
        else this.id = player.getName();

        this.idString = this.id.toString().toLowerCase();
    }

    /**
     * <p>Represents the given player as a VersionPlayer, letting
     * you save the player for different versions.</p>
     *
     * @param player Bukkit player entity object
     */
    public VersionPlayer(HumanEntity player)
    {
        if (VersionManager.isVersionAtLeast(VersionManager.V1_7_5))
        {
            this.id = player.getUniqueId();
        }
        else this.id = player.getName();

        this.idString = this.id.toString().toLowerCase();
    }

    /**
     * <p>Retrieves the appropriate ID for the player</p>
     * <p>If the server is pre-1.7.5, this will return the
     * player's name. Otherwise, this will return the
     * player's UUID.</p>
     *
     * @return player ID or name
     */
    public Object getId()
    {
        return id;
    }

    /**
     * <p>Retrieves the string version of the
     * appropriate ID for the player</p>
     * <p>If the server is pre-1.7.5, this will return the
     * player's name. Otherwise, this will return the
     * toString of the player's UUID.</p>
     *
     * @return string representation of player's ID
     */
    public String getIdString()
    {
        return idString;
    }

    /**
     * <p>Retrieves the name of the current player.</p>
     *
     * @return name of the player
     */
    public String getName()
    {
        if (VersionManager.isVersionAtLeast(VersionManager.V1_7_5))
        {
            String name = PlayerUUIDs.getName((UUID) id);
            if (name != null) return name;
            else return getOfflinePlayer().getName();
        }
        else
        {
            Player p = Bukkit.getPlayer(id.toString());
            if (p != null) return p.getName();
            else return Bukkit.getOfflinePlayer(id.toString()).getName();
        }
    }

    /**
     * Gets the Bukkit player object for the represented player
     *
     * @return Bukkit player object
     */
    public Player getPlayer()
    {
        if (id instanceof String)
        {
            return Bukkit.getPlayer(id.toString());
        }
        else
        {
            return Bukkit.getPlayer((UUID) id);
        }
    }

    /**
     * Gets the Bukkit offline player object for the represented player
     *
     * @return Bukkit offline player object
     */
    public OfflinePlayer getOfflinePlayer()
    {
        if (id instanceof String)
        {
            return Bukkit.getOfflinePlayer(id.toString());
        }
        else
        {
            return Bukkit.getOfflinePlayer((UUID) id);
        }
    }
}
