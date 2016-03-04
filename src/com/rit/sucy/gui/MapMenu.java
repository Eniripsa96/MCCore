package com.rit.sucy.gui;

import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Represents one menu of navigation for a map. Menus can
 * navigate between each other or be independent.
 */
public abstract class MapMenu
{
    private static final HashMap<String, HashMap<String, Object>> MISC = new HashMap<String, HashMap<String, Object>>();

    private static final HashMap<String, Integer>  SELECTIONS = new HashMap<String, Integer>();
    private static final HashMap<String, MapScene> SCENES     = new HashMap<String, MapScene>();

    private MapMenu parent;

    /**
     * Gets the current selection of the player
     *
     * @param player player to get the selection of
     * @return the current selection of the player
     */
    public static Integer getSelection(Player player)
    {
        if (!SELECTIONS.containsKey(player.getName()))
            SELECTIONS.put(player.getName(), 0);
        return SELECTIONS.get(player.getName());
    }

    /**
     * Sets the current selection for the player
     *
     * @param player    player to set the selection for
     * @param selection selection to use
     */
    public static void setSelection(Player player, int selection) {
        SELECTIONS.put(player.getName(), selection);
    }

    /**
     * Retrieves the scene manager for the player
     *
     * @param player player to get the scene manager for
     * @return the scene manager for the player
     */
    public static MapScene getScene(Player player) {
        if (!SCENES.containsKey(player.getName()))
            SCENES.put(player.getName(), new MapScene());
        return SCENES.get(player.getName());
    }

    /**
     * Retrieves stored data for the player by key
     *
     * @param player player to get the data for
     * @param key    key the data was stored under
     * @return the retrieved data
     */
    public static Object getData(Player player, String key)
    {
        if (!MISC.containsKey(player.getName())) MISC.put(player.getName(), new HashMap<String, Object>());
        return MISC.get(player.getName()).get(key);
    }

    /**
     * Sets data for the player that persists between menus
     *
     * @param player player to set the data for
     * @param key    key to store the data under
     * @param data   data to store
     */
    public static void setData(Player player, String key, Object data)
    {
        if (!MISC.containsKey(player.getName())) MISC.put(player.getName(), new HashMap<String, Object>());
        MISC.get(player.getName()).put(key, data);
    }

    /**
     * Retrieves the parent menu for this menu. If a map
     * was created using this menu as the top level, the
     * parent will not be returned to despite hitting back.
     * This is more for the global menu connecting everything
     * together.
     *
     * @return parent menu
     */
    public final MapMenu getParent()
    {
        return parent;
    }

    /**
     * Sets the parent of the menu which will be the menu
     * that is returned to when the back button is hit.
     *
     * @param menu menu to return to when back is hit
     */
    public final void setParent(MapMenu menu)
    {
        this.parent = menu;
    }

    /**
     * The method used to render the map to the buffer.
     * This will be called automatically when the player
     * is looking at a map with this menu as the active menu.
     *
     * @param buffer buffer to draw to
     */
    public abstract void render(MapBuffer buffer, Player player);

    /**
     * Provides any setup for a player that may need to be
     * done when navigating to the menu such as setting up
     * the scene or applying selection data.
     *
     * @param player player to initialize
     */
    public void setup(Player player) { }

    /**
     * Called when the up key is pressed. This should
     * be overridden when needed.
     *
     * @param player player that moved up
     */
    public void onUp(Player player) { }

    /**
     * Called when the down key is pressed. This should
     * be overridden when needed.
     *
     * @param player player that moved down
     */
    public void onDown(Player player) { }

    /**
     * Called when the left key is pressed. This should
     * be overridden when needed.
     *
     * @param player player that moved left
     */
    public void onLeft(Player player) { }

    /**
     * Called when the right key is pressed. This should
     * be overridden when needed.
     *
     * @param player player that moved right
     */
    public void onRight(Player player) { }

    /**
     * Called when the select key is pressed. This should
     * be overridden when needed.
     *
     * @param player player that selected the current item
     */
    public void onSelect(Player player) { }

    /**
     * Called when the back key is pressed. This should
     * be overridden when needed. Transitioning to the
     * previous menu will be handled automatically for
     * this control, but if you have other actions you
     * want to take care of such as applying changes
     * before exiting, apply them here.
     *
     * @param player player that hit the back button
     */
    public void onBack(Player player) { }

    /**
     * Similar to onBack, this is provided in case you
     * need to clean up values. This is when the player
     * switches their item from the map, exiting the menu.
     *
     * @param player player that exited the menu
     */
    public void onExit(Player player) { }
}
