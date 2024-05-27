package com.karuslabs.elementary.junit.annotations;

import java.lang.annotation.*;

/**
 * Represents the configuration for generating classes and sources.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Generation {

    /**
     * A placeholder that signifies that the default output location should be used.
     */
    String DEFAULT_OUTPUT = "${DEFAULT_OUTPUT}";

    /**
     * Whether to retain the generated classes and sources after each run. Defaults to false.
     *
     * @return true if the generated classes and sources should be retained
     */
    boolean retain() default false;

    /**
     * The location of generated classes.
     * <p>
     * Defaults to a {@code <temporary directory unique to method>/generated-classes}.
     *
     * @return the location, or null if the default location is used
     */
    String classes() default DEFAULT_OUTPUT;

    /**
     * The location of generated sources.
     * <p>
     * Defaults to a {@code <temporary directory unique to method>/generated-sources}.
     *
     * @return the location, or null if the default location is used
     */
    String sources() default DEFAULT_OUTPUT;

}
