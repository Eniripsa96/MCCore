package com.rit.sucy.scoreboard;

/**
 * Permission nodes for scoreboard commands
 */
public enum ScoreboardNodes
{

    CYCLE("core.board.cycle"),
    LIST("core.board.list"),
    STOP("core.board.stop"),
    SHOW("core.board.show"),;

    /**
     * Permission node
     */
    private final String node;

    /**
     * Private constructor
     *
     * @param node permission node
     */
    private ScoreboardNodes(String node)
    {
        this.node = node;
    }

    /**
     * @return permission node
     */
    public String getNode()
    {
        return node;
    }
}
