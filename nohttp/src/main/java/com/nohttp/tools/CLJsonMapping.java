package com.nohttp.tools;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by simon on 2017/11/16 0016.
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CLJsonMapping {
    public static int TYPE_OBJECT = 1;
    public static int TYPE_COLLECTION = 2;
    public String tagPath();
    public String keyPath() default "";
    public int type() default TYPE_OBJECT;
    public String genericType() default "";
    public String trueValue() default "true";
    public String dateFormat() default "yyyy/MM/dd HH:mm:ss";
    public boolean isSinglePath() default false;
}
