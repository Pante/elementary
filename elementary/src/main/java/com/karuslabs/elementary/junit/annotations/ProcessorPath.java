/*
 * The MIT License
 *
 * Copyright 2022 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.karuslabs.elementary.junit.annotations;

import com.karuslabs.elementary.junit.JavacExtension;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Represents the javac --processor-path (or -processorpath) option to use during compilation.
 *
 * @see <a href = "https://docs.oracle.com/en/java/javase/11/tools/javac.html">javac flags</a>
 */
@Usage({JavacExtension.class})
@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD})
@Repeatable(ProcessorPaths.class)
public @interface ProcessorPath {

    /**
     * Processor dependency artifact in the format <code>"&lt;groupId&gt;:&lt;artifactId&gt;:&lt;version&gt;"</code>.
     *
     * @return the processor dependency artifact
     */
    String value();

    /**
     * The repository from where to get the artifact.
     *
     * @return the source repository
     */
    String repository() default Repository.LOCAL;

    class Repository {
        public static final String LOCAL = "${USERPROFILE}${HOME}/.m2/repository";
        public static final String REPO1 = "https://repo1.maven.org/maven2/";
    }

}
