package dev.latvian.apps.webutils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.PACKAGE})
@Retention(RetentionPolicy.CLASS)
public @interface NonnullByDefault {
}
