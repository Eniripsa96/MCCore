package com.rit.sucy.config;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>Annotation for fields during serialization</p>
 * <p>Flags allow for controlled exclusion in case of multiple save formats or
 * different levels of quality</p>
 * <p>The default flag includes all flags</p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcludeField
{

    /**
     * @return name of the flag to exclude from serialization
     */
    String flag() default ConfigSerializer.ALL_FLAG;
}
