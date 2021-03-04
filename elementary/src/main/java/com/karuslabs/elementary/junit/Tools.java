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
package com.karuslabs.elementary.junit;

import com.karuslabs.annotations.Static;
import com.karuslabs.elementary.junit.DaemonCompiler.Environment;
import com.karuslabs.utilitary.Logger;
import com.karuslabs.utilitary.type.TypeMirrors;

import javax.annotation.processing.*;
import javax.lang.model.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A utility class from which an annotation processing environment can be accessed
 * when used in conjunction with {@link ToolsExtension}. All methods throw an exception
 * when called outside a test class extended with {@code ToolsExtension}.
 * 
 * @see ToolsExtension
 */
public @Static class Tools {
    
    static @Nullable Environment environment;
    
    private static Environment environment() {
        if (environment == null) {
            throw new IllegalStateException("Test class should be annotated with \"@ExtendWith(ToolsExtension.class)\"");
        }
        
        return environment;
    }
    
    /**
     * Returns a {@code Cases}.
     * 
     * @return a {@code Casses}
     */
    public static Cases cases() {
        return environment().cases;
    }
    
    /**
     * Returns a {@code RoundEnvironment}.
     * 
     * @return a {@code RoundEnvironment}
     */
    public static RoundEnvironment round() {
        return environment().round;
    }
    
    /**
     * Returns an {@code Elements}.
     * 
     * @return an {@code Elements}
     */
    public static Elements elements() {
        return environment().elements;
    }
    
    /**
     * Returns a {@code Types}.
     * 
     * @return a {@code Types}
     */
    public static Types types() {
        return environment().types;
    }
    
    /**
     * Returns a {@code Messager}.
     * 
     * @return a {@code Messager}
     */
    public static Messager messager() {
        return environment().messager;
    }
    
    /**
     * Returns a {@code Filer}.
     * 
     * @return a {@code Filer}
     */
    public static Filer filer() {
        return environment().filer;
    }
    
    /**
     * @return a {@code TypeMirrors}
     */
    public static TypeMirrors typeMirrors() {
        return environment().typeMirrors;
    }
    
    /**
     * Returns a {@code Logger}.
     * 
     * @return a {@code Logger}
     */
    public static Logger logger() {
        return environment().logger;
    }

}
