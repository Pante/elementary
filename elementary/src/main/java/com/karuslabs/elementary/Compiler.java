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

/**
 * Represents a compiler that may compile Java source files.
 */
public class Compiler {
    
    /**
     * Creates a Java compiler.
     * 
     * @return a Java compiler
     */
    public static Compiler javac() {
        return new Compiler(ToolProvider.getSystemJavaCompiler());
    }
    
    private static final ClassLoader PLATFORM = ClassLoader.getPlatformClassLoader();
    private static final ClassLoader APPLICATION = ClassLoader.getSystemClassLoader();
    
    
    private final JavaCompiler compiler;
    private final List<Processor> processors = new ArrayList<>();
    private final List<String> options = new ArrayList<>();
    private @Nullable List<File> classpath;
    
    /**
     * Creates a {@code Compiler} with the given underlying compiler.
     * 
     * @param compiler the Java compiler
     */
    Compiler(JavaCompiler compiler) {
        this.compiler = compiler;
    }
    
    
    /**
     * Compiles the given files.
     * 
     * @param files the Java source files
     * @return the results of this compilation
     */
    public Results compile(JavaFileObject... files) {
        return compile(List.of(files));
    }
    
    /**
     * Compiles the given files.
     * 
     * @param files the Java source files
     * @return the results of this compilation
     */
    public Results compile(Iterable<? extends JavaFileObject> files) {
        var diagnostics = new Diagnostics();
        var manager = new MemoryFileManager(compiler.getStandardFileManager(diagnostics, Locale.getDefault(), UTF_8));
        
        if (classpath != null) {
            setLocation(manager, StandardLocation.CLASS_PATH, classpath);
        }
        
        var task = compiler.getTask(null, manager, diagnostics, options, null, files);
        task.setProcessors(processors);
        
        return new Results(manager.outputFiles(), manager.generatedSources(), diagnostics, task.call());
    }
    
    /**
     * Associates the given search paths with the given location.
     * 
     * @param manager the manager
     * @param location the location
     * @param paths the search paths
     * 
     * @throws UncheckedIOException if {@code location} is an output location and
     * does not represent an existing directory
     */
    void setLocation(StandardJavaFileManager manager, StandardLocation location, Iterable<File> paths) {
        try {
            manager.setLocation(location, paths);
            
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    
    /**
     * Adds the given annotation processors to be applied during compilation.
     * 
     * @param processors the annotation processors
     * @return {@code this}
     */
    public Compiler processors(Processor... processors) {
        Collections.addAll(this.processors, processors);
        return this;
    }
    
    /**
     * Adds the given annotation processors to be applied during compilation.
     * 
     * @param processors the annotation processors
     * @return {@code this}
     */
    public Compiler processors(Collection<Processor> processors) {
        this.processors.addAll(processors);
        return this;
    }
    
    
    /**
     * Adds the given compiler options to be applied during compilation.
     * 
     * @see <a href = "https://docs.oracle.com/en/java/javase/11/tools/javac.html">javac flags</a>
     * 
     * @param options the options
     * @return {@code this}
     */
    public Compiler options(String... options) {
        Collections.addAll(this.options, options);
        return this;
    }
    
    /**
     * Adds the given compiler options to be applied during compilation.
     * 
     * @see <a href = "https://docs.oracle.com/en/java/javase/11/tools/javac.html">javac flags</a>
     * 
     * @param options the options
     * @return {@code this}
     */
    public Compiler options(Collection<String> options) {
        this.options.addAll(options);
        return this;
    }
    
    
    /**
     * Sets the current classpath as the the compilation classpath.
     * 
     * @return {@code this}
     */
    public Compiler currentClasspath() {
        return classpath(getClass().getClassLoader());
    }
    
    
    /**
     * Sets the classpath of the given {@code ClassLoader} as the the compilation classpath.
     * 
     * @param loader the {@code ClassLoader} which classpath is to be used during compilation
     * @return {@code this}
     * 
     * @throws IllegalArgumentException if the given {@code ClassLoader} or its parents are not
     *                                  the system/platform {@code ClassLoader} or {@code URLClassLoader}s,
     *                                  or if the given {@code ClassLoader} or its parents contains a classpath
     *                                  that consists of folders
     */
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
    
    /**
     * Sets the given classpath as the compilation classpath.
     * 
     * @param classpath the compilation classpath
     * @return {@code this}
     */
    public Compiler classpath(Collection<File> classpath) {
        this.classpath = new ArrayList<>(classpath);
        return this;
    }
    
}
