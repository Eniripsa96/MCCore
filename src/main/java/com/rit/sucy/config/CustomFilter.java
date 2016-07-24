/**
 * MCCore
 * com.rit.sucy.config.CustomFilter
 * <p/>
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2014 Steven Sucy
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software") to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.rit.sucy.config;

/**
 * <p>A custom filter to apply to language messages</p>
 * <p>This can be used to insert data such as player names, stats, or other data</p>
 */
public class CustomFilter {

    private String token;
    private String replacement;

    /**
     * <p>Creates a new custom filter</p>
     *
     * @param token       string to search for to replace (e.g. "{player}")
     * @param replacement string to replace the token with (e.g. "Bob")
     */
    public CustomFilter(String token, String replacement) {
        this.token = token;
        this.replacement = replacement;
    }

    /**
     * Gets the token string of the filter
     *
     * @return token string
     */
    public String getToken() {
        return token;
    }

    /**
     * Gets the replacement string of the filter
     *
     * @return replacement string
     */
    public String getReplacement() {
        return replacement;
    }

    /**
     * Sets the replacement string for the filter
     *
     * @param replacement replacement
     */
    public CustomFilter setReplacement(String replacement) {
        this.replacement = replacement;
        return this;
    }

    /**
     * Applies the filter to the string
     *
     * @param string string to apply to
     */
    public String apply(String string) {
        return string.replace(token, replacement);
    }

    /**
     * Applies the filter to the string builder
     *
     * @param sb string builder to apply to
     */
    public void apply(StringBuilder sb) {
        int index = sb.indexOf(token);
        while (index >= 0) {
            sb.replace(index, index + token.length(), replacement);
            index = sb.indexOf(token);
        }
    }
}
