package com.karuslabs.elementary.junit.annotations;

import com.karuslabs.elementary.junit.JavacExtension;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Represents the javac --module (or -m) and --module-source-path option to use during compilation.
 *
 * @see <a href = "https://docs.oracle.com/en/java/javase/11/tools/javac.html">javac flags</a>
 */
@Usage({JavacExtension.class})
@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Module {

    /**
     * Module name.
     *
     * @return the module name
     */
    String value();

    /**
     * Module source path. This path is relative to the <code>&lt;module_root&gt;/src/test/resources</code> base path.
     *
     * @return the module source path
     */
    String sourcePath() default "";
}
