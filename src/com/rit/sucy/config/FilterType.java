package com.rit.sucy.config;

/**
 * <p>Types of filters available for the language configuration</p>
 */
public enum FilterType {

    /**
     * No filters are to be applied. The String will be returned as-is
     */
    NONE,

    /**
     * The string will be colored using '&' followed by a valid character
     * to represent color codes.
     */
    COLOR,

    /**
     * <p>The string will look for special filters and replace them
     * with the appropriate replacement. Special filters include:</p>
     * <ul>
     *     <li>{expandFront(&lt;text&gt;,&lt;size&gt;,&lt;length&gt;)} -
     *     Expands the text to the desired size, appending whitespace in
     *     front of the text. This can be used to perfectly line up lines
     *     of text with each other when you size things to the same value.
     *     The 'size' is the size for player displays whereas the length
     *     is the desired length of the string for the console. The two
     *     values are due to the console having a monotype font while
     *     in game is not one.</li>
     *     <li>{expandBack(&lt;text&gt;,&lt;size&gt;,&lt;length&gt;)} -
     *     The same as {expandFront(&lt;text&gt;,&lt;size&gt;,&lt;length&gt;)}
     *     except it places the whitespace after the text.</li>
     *     <li>{break} - Fills the remaining space on the line with
     *     dashes. To make it a solid line, preceed it with
     *     strike-through (&m).</li>
     * </ul>
     */
    SPECIAL,

    /**
     * <p>Applies all available filters described above</p>
     */
    ALL
}
