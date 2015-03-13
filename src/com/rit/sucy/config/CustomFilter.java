package com.rit.sucy.config;

/**
 * <p>A custom filter to apply to language messages</p>
 * <p>This can be used to insert data such as player names, stats, or other data</p>
 */
public class CustomFilter
{

    private String token;
    private String replacement;

    /**
     * <p>Creates a new custom filter</p>
     *
     * @param token       string to search for to replace (e.g. "{player}")
     * @param replacement string to replace the token with (e.g. "Bob")
     */
    public CustomFilter(String token, String replacement)
    {
        this.token = token;
        this.replacement = replacement;
    }

    /**
     * Gets the token string of the filter
     *
     * @return token string
     */
    public String getToken()
    {
        return token;
    }

    /**
     * Gets the replacement string of the filter
     *
     * @return replacement string
     */
    public String getReplacement()
    {
        return replacement;
    }

    /**
     * Sets the replacement string for the filter
     *
     * @param replacement replacement
     */
    public CustomFilter setReplacement(String replacement)
    {
        this.replacement = replacement;
        return this;
    }

    /**
     * Applies the filter to the string
     *
     * @param string string to apply to
     */
    public String apply(String string)
    {
        return string.replace(token, replacement);
    }

    /**
     * Applies the filter to the string builder
     *
     * @param sb string builder to apply to
     */
    public void apply(StringBuilder sb)
    {
        int index = sb.indexOf(token);
        while (index >= 0)
        {
            sb.replace(index, index + token.length(), replacement);
            index = sb.indexOf(token);
        }
    }
}
