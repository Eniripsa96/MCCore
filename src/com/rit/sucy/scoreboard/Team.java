package com.rit.sucy.scoreboard;

/**
 * A team used by the Scoreboard Manager
 */
public class Team {

    private static int nextId = 0;

    private String name;
    private String prefix;
    private String suffix;
    private int id;

    /**
     * <p>Constructor with a team name</p>
     */
    public Team(String name) {
        this(name, null, null);
    }

    /**
     * <p>Constructor with a team name, prefix and suffix</p>
     * <p>A null prefix or suffix will mean the team doesn't have one</p>
     *
     * @param name   team name
     * @param prefix team prefix
     * @param suffix team suffix
     */
    public Team(String name, String prefix, String suffix) {
        this.name = name;
        setPrefix(prefix);
        setSuffix(suffix);
        this.id = nextId++;
    }

    /**
     * <p>Sets the prefix for the team</p>
     * <p>Setting it to null will remove the prefix</p>
     * <p>If the prefix is longer than 16 characters,
     * it will be truncated down to 16 characters</p>
     *
     * @param prefix new prefix
     */
    public void setPrefix(String prefix) {
        if (prefix != null && prefix.length() > 16) prefix = prefix.substring(0, 16);
        this.prefix = prefix;
    }

    /**
     * <p>Sets the suffix for the team</p>
     * <p>Setting it to null will remove the suffix</p>
     * <p>If the suffix is longer than 16 characters,
     * it will be truncated down to 16 characters</p>
     *
     * @param suffix new suffix
     */
    public void setSuffix(String suffix) {
        if (suffix != null && suffix.length() > 16) suffix = suffix.substring(0, 16);
        this.suffix = suffix;
    }

    /**
     *
     * @return the team name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the team's prefix or null if none
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @return the team's suffix or null if none
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * @return ID assigned to the team
     */
    public int getId() {
        return id;
    }
}
