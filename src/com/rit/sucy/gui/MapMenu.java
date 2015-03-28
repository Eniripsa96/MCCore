package com.rit.sucy.gui;

import org.bukkit.entity.Player;

/**
 * Represents one menu of navigation for a map. Menus can
 * navigate between each other or be independent.
 */
public abstract class MapMenu
{
    private MapMenu parent;

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
