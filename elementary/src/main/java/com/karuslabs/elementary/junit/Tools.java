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

public @Static class Tools {
    
    static @Nullable Environment environment;
    
    private static Environment environment() {
        if (environment == null) {
            throw new IllegalStateException("Test class should be annotated with \"@ExtendWith(ToolsExtension.class)\"");
        }
        
        return environment;
    }
    
    public static Elements elements() {
        return environment().elements;
    }
    
    public static Types types() {
        return environment().types;
    }
    
    public static Messager messager() {
        return environment().messager;
    }
    
    public static Filer filer() {
        return environment().filer;
    }
    
    public static TypeMirrors typeMirrors() {
        return environment().typeMirrors;
    }
    
    public static Logger logger() {
        return environment().logger;
    }

}
