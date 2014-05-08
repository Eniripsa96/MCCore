package com.rit.sucy.scoreboard;

import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Map;

/**
 * Simple interface for providing stats for a scoreboard
 */
public interface StatHolder {

    public ArrayList<OfflinePlayer> getStats();
    public ArrayList<Integer> getValues();

}
