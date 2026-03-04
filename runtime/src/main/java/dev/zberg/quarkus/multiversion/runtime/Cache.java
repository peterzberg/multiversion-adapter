package dev.zberg.quarkus.multiversion.runtime;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD, PARAMETER})
public @interface Cache {

    long validityInMillis() default 10_000L;
}
