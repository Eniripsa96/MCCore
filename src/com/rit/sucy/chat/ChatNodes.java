package com.rit.sucy.chat;

/**
 * Permission nodes for chat commands
 */
enum ChatNodes
{

    LIST("core.chat.list"),
    NAME("core.chat.name"),
    PREFIX("core.chat.prefix"),
    RESET("core.chat.reset"),;

    /**
     * Permission node
     */
    private final String node;

    /**
     * Private constructor
     *
     * @param node permission node
     */
    private ChatNodes(String node)
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
