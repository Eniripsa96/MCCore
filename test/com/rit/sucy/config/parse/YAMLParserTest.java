/**
 * MCCore
 * com.rit.sucy.config.parse.YAMLParserYest
 * <p>
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 Steven Sucy
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.rit.sucy.config.parse;

import org.junit.Test;

import java.util.List;

public class YAMLParserTest
{
    @Test
    public void testBasicYAML()
    {
        DataSection data = YAMLParser.parseText("key:\n  value: 2\n  list:\n  - a\n  - 'b'" +
                                               "\n  -dashKey: {}\n  emptyList: []\n  quoted: 'text'");
        DataSection subData = data.getSection("key");
        List<String> list = subData.getList("list");

        assert subData.getInt("value") == 2;
        assert list.get(0).equals("a");
        assert list.get(1).equals("b");
        assert list.size() == 2;
        assert subData.getSection("-dashKey") != null;
        assert subData.getList("emptyList").size() == 0;
        assert subData.getString("quoted").equals("text");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidYAML()
    {
        YAMLParser.parseText("key\n  value: 2");
    }

    @Test
    public void testConfig() { testFile("config"); }

    private void testFile(String file)
    {
        try
        {
            DataSection data = YAMLParser.parseFile(file + ".yml");
            assert data != null;
            assert data.keys().size() > 0;
        }
        catch (Exception ex)
        {
            assert false;
        }
    }
}