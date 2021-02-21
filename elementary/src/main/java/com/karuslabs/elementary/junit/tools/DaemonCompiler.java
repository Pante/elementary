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
package com.karuslabs.elementary.junit.tools;

import com.karuslabs.elementary.Compiler;
import com.karuslabs.elementary.CompilationException;
import com.karuslabs.elementary.junit.*;
import com.karuslabs.elementary.junit.tools.DaemonProcessor.Environment;

import java.util.*;
import javax.tools.JavaFileObject;

import org.checkerframework.checker.nullness.qual.Nullable;

import org.junit.jupiter.api.extension.TestInstantiationException;

import static com.karuslabs.elementary.Compiler.javac;
import static com.karuslabs.elementary.file.FileObjects.*;

class DaemonCompiler extends Thread {
    
    private static final JavaFileObject SOURCE = ofLines("Dummy", "final class Dummy {}");
    
    public static DaemonCompiler of(Class<?> type) {
        var files = new ArrayList<JavaFileObject>();
        files.add(SOURCE);
        
        for (var classpath : type.getAnnotationsByType(Classpath.class)) {
            files.add(ofResource(classpath.value()));
        }
        
        for (var inline : type.getAnnotationsByType(Inline.class)) {
            files.add(ofLines(inline.name(), inline.source()));
        }
        
        return new DaemonCompiler(Thread.currentThread(), javac().currentClasspath(), files);
    }
    
    
    public final DaemonProcessor processor = new DaemonProcessor();
    private final Thread parent;
    private final Compiler compiler;
    private final List<JavaFileObject> files;
    private @Nullable volatile Throwable thrown;
    
    DaemonCompiler(Thread parent, Compiler compiler, List<JavaFileObject> files) {
        this.parent = parent;
        this.compiler = compiler.processors(processor);
        this.files = files;
    }
    
    @Override
    public void run() {
        try {
            var results = compiler.compile(files);
            if (!results.success) {
                throw new CompilationException(results.find().errors().full());
            }
            
        } catch (Throwable e) {
            thrown = e;
            parent.interrupt();
        }
        
    }
    
    public Environment environment() throws TestInstantiationException {
        try {
            return processor.environment();
            
        } catch (InterruptedException e) {
            throw new TestInstantiationException("Failed to start javac", thrown == null ? e: thrown);
        }
    }
    
}
