package com.rit.sucy.scoreboard;

import java.util.Map;

/**
 * Simple interface for providing stats for a scoreboard
 */
public interface StatHolder {

    /**
     * Retrieves the map of stats (stat name -> stat value)
     *
     * @return map of stats
     */
    public Map<String, Integer> getStats();
}
