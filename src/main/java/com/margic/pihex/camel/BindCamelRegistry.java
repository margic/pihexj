package com.margic.pihex.camel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by paulcrofts on 3/27/15.
 */
@Retention(RUNTIME)
@Target({ ElementType.TYPE})
public @interface BindCamelRegistry {
    String ref();
}