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
    String flag() default ConfigSerializer.ALL_FLAG;
    boolean list() default false;
    boolean map() default false;
}
