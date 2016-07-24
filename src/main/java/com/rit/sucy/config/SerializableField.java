/**
 * MCCore
 * com.rit.sucy.config.SerializableField
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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>Annotation for fields during serialization to ask for further traversal</p>
 * <p>Including this annotation makes the serializer attempt to serialize the
 * field instead of just saving its toString value.</p>
 * <p>The flags provide a way to switch between the toString and full serialization
 * in case of multiple file types</p>
 * <p>The list attribute allows you to serialize the objects contained within
 * collections such as lists or hash sets</p>
 * <p>The map attribute allows you to serialize the objects within hash maps
 * if they are the values (e.g. HashMap<string, MyObject>)</p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface SerializableField {

    /**
     * @return the flag in which this field is serializable
     */
    String flag() default ConfigSerializer.ALL_FLAG;

    /**
     * @return whether or not this field is a list
     */
    boolean list() default false;

    /**
     * @return whether or not this field is a map
     */
    boolean map() default false;
}
