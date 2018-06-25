package com.rit.sucy.text;

import org.bukkit.ChatColor;
import org.junit.Assert;
import org.junit.Test;

/**
 * MCCore © 2017
 * com.rit.sucy.text.TextFormatterTest
 */
public class TextFormatterTest {

    @Test
    public void colorString_goodCode() {
        Assert.assertEquals(
                TextFormatter.colorString("Some text &2 and done"),
                "Some text " + ChatColor.DARK_GREEN + " and done");
    }

    @Test
    public void colorString_multipleCodes() {
        Assert.assertEquals(
                TextFormatter.colorString("Some text &2 and &6 done"),
                "Some text " + ChatColor.DARK_GREEN + " and " + ChatColor.GOLD + " done");
    }

    @Test
    public void colorString_badCode() {
        Assert.assertEquals(
                TextFormatter.colorString("Some text &+ and done"),
                "Some text &+ and done");
    }

    @Test
    public void colorString_noCode() {
        Assert.assertEquals(
                "Some text and done",
                "Some text and done");
    }

    @Test
    public void colorString_builder_goodCode() {
        StringBuilder builder = new StringBuilder()
                .append("Some text &2 and done");

        TextFormatter.colorString(builder);
        Assert.assertEquals(builder.toString(), "Some text " + ChatColor.DARK_GREEN + " and done");
    }

    @Test
    public void colorString_builder_multipleCodes() {
        StringBuilder builder = new StringBuilder()
                .append("Some text &2 and &6 done");

        TextFormatter.colorString(builder);
        Assert.assertEquals(builder.toString(), "Some text " + ChatColor.DARK_GREEN + " and " + ChatColor.GOLD + " done");
    }

    @Test
    public void colorString_builder_badCode() {
        StringBuilder builder = new StringBuilder()
                .append("Some text &+ and done");

        TextFormatter.colorString(builder);
        Assert.assertEquals(builder.toString(), "Some text &+ and done");
    }

    @Test
    public void colorString_builder_noCode() {
        StringBuilder builder = new StringBuilder()
                .append("Some text and done");

        TextFormatter.colorString(builder);
        Assert.assertEquals(builder.toString(), "Some text and done");
    }
}
