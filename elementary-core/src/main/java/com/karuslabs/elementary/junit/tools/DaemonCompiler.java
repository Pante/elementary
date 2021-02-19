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
import com.karuslabs.elementary.file.FileObjects;
import com.karuslabs.elementary.junit.*;

import java.util.*;
import javax.tools.JavaFileObject;

import static com.karuslabs.elementary.Compiler.javac;

public class DaemonCompiler extends Thread {
    
    public static DaemonCompiler of(Classpath[] classClasspaths, Inline[] classInlines, Classpath[] methodClasspaths, Inline[] methodInlines) {
        var files = new ArrayList<JavaFileObject>();
        for (var classpath : classClasspaths) {
            files.add(FileObjects.ofResource(classpath.value()));
        }
        
        for (var classpath : methodClasspaths) {
            files.add(FileObjects.ofResource(classpath.value()));
        }
        
        for (var inline : classInlines) {
            files.add(FileObjects.ofLines(inline.name(), inline.value()));
        }
        
        for (var inline : methodInlines) {
            files.add(FileObjects.ofLines(inline.name(), inline.value()));
        }
        
        return new DaemonCompiler(javac().currentClasspath(), files);
    }
    
    
    public final DaemonProcessor processor = new DaemonProcessor();
    private final Compiler compiler;
    private final List<JavaFileObject> files;
    
    DaemonCompiler(Compiler compiler, List<JavaFileObject> files) {
        this.compiler = compiler.processors(processor);
        this.files = files;
    }
    
    @Override
    public void run() {
        var results = compiler.compile(files);
        // TODO: handle results
    }
    
}
