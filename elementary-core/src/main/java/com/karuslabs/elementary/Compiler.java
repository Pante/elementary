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
package com.karuslabs.elementary;

import com.karuslabs.elementary.file.MemoryFileManager;

import java.io.*;
import java.net.URLClassLoader;
import java.util.*;
import javax.annotation.processing.Processor;
import javax.tools.*;

import org.checkerframework.checker.nullness.qual.Nullable;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

public class Compiler {
    
    public static Compiler javac() {
        return new Compiler(ToolProvider.getSystemJavaCompiler());
    }
    
    private static final ClassLoader PLATFORM = ClassLoader.getPlatformClassLoader();
    private static final ClassLoader APPLICATION = ClassLoader.getSystemClassLoader();
    
    
    private final JavaCompiler compiler;
    private Iterable<Processor> processors = List.of();
    private Iterable<String> options = List.of();
    private @Nullable Iterable<File> classpath;
    private @Nullable Iterable<File> annotationProcessorPath;
    
    Compiler(JavaCompiler compiler) {
        this.compiler = compiler;
    }
    
    
    public Result compile(JavaFileObject... files) {
        return compile(List.of(files));
    }
    
    public Result compile(Iterable<? extends JavaFileObject> files) {
        var diagnostics = new Diagnostics();
        var manager = new MemoryFileManager(compiler.getStandardFileManager(diagnostics, Locale.getDefault(), UTF_8));
        
        if (classpath != null) {
            setLocation(manager, StandardLocation.CLASS_PATH, classpath);
        }
        
        if (annotationProcessorPath != null) {
            setLocation(manager, StandardLocation.ANNOTATION_PROCESSOR_PATH, annotationProcessorPath);
        }
        
        var task = compiler.getTask(null, manager, diagnostics, options, null, files);
        task.setProcessors(processors);
        
        return new Result(manager.outputFiles(), manager.generatedSources(), diagnostics, task.call());
    }
    
    void setLocation(StandardJavaFileManager manager, StandardLocation location, Iterable<File> paths) {
        try {
            manager.setLocation(location, paths);
            
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    
    public Compiler processors(Processor... processors) {
        this.processors = List.of(processors);
        return this;
    }
    
    public Compiler processors(Iterable<Processor> processors) {
        this.processors = processors;
        return this;
    }
    
    
    public Compiler options(String... options) {
        this.options = List.of(options);
        return this;
    }
    
    public Compiler options(Iterable<String> options) {
        this.options = options;
        return this;
    }
    
    
    public Compiler currentClasspath() {
        return classpath(getClass().getClassLoader());
    }
    
    public Compiler classpath(ClassLoader loader) {
        var paths = new HashSet<String>();
        while (loader != null) {
            if (loader == PLATFORM) {
                break;
            }
            
            if (loader == APPLICATION) {
                Collections.addAll(paths, System.getProperty("java.class.path").split(System.getProperty("path.separator")));
                break;
            }
            
            if (!(loader instanceof URLClassLoader)) {
                throw new IllegalArgumentException("Given ClassLoader and its parents must be a URLClassLoader");
            }
            
            for (var url : ((URLClassLoader) loader).getURLs()) {
                if (url.getProtocol().equals("file")) {
                    paths.add(url.getPath());
                    
                } else {
                    throw new IllegalArgumentException("Given ClassLoader and its parents may not contain classpaths that consist of folders");
                }
            }
            
            loader = loader.getParent();
        }
        
        classpath = paths.stream().map(File::new).collect(toList());
        
        return this;
    }
    
    public Compiler classpath(Iterable<File> classpath) {
        this.classpath = classpath;
        return this;
    }
    
    
    public Compiler annotationProcessorPath(Iterable<File> path) {
        this.annotationProcessorPath = path;
        return this;
    }
    
}
