/*
 * The MIT License
 *
 * Copyright 2021 Karus Labs.
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

import com.karuslabs.elementary.junit.*;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Denotes that the containing file of the annotated element is to be included
 * for compilation.
 * <br>
 * <br>
 * <b>Note: </b><br>
 * If no value is specified for this annotation, the top level enclosing class of
 * the annotated element is used to resolve the file name. The file may not always
 * be named after the top level class of an annotated element if it contains multiple top level
 * classes.
 * <br>
 * In addition, this annotation requires a build tool to copy source files from the
 * source directory to the output directory.
 * 
 * @see <a href = "https://github.com/Pante/elementary/wiki/@Introspect-Configuration">@Introspect configuration</a>
 */
@Usage({JavacExtension.class, ToolsExtension.class})
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Introspect {

    /**
     * A value to signify that the name of the enclosing top level class is to be used.
     */
    public static final String DEFAULT = "${TRACE_ENCLOSING_CLASS}";
    
    /**
     * The name of the top level class after which the file is named.
     * 
     * @return the top level class name
     */
    String value() default DEFAULT;
    
}
