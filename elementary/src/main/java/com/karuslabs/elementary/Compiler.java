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

import java.io.*;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.*;
import javax.annotation.processing.Processor;
import javax.tools.*;

import org.checkerframework.checker.nullness.qual.Nullable;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.*;

/**
 * Represents a Java compiler.
 */
public class Compiler {
    
    /**
     * Creates a Java compiler.
     * 
     * @return a Java compiler
     */
    public static Compiler javac(File classOutput, File sourceOutput) {
        return new Compiler(ToolProvider.getSystemJavaCompiler(), classOutput, sourceOutput);
    }
    
    private static final ClassLoader PLATFORM = ClassLoader.getPlatformClassLoader();
    private static final ClassLoader APPLICATION = ClassLoader.getSystemClassLoader();

    /**
     * The location of the generated classes.
     */
    public final File classOutput;
    /**
     * The location of the generated sources.
     */
    public final File sourceOutput;

    private final JavaCompiler compiler;
    private final List<Processor> processors = new ArrayList<>();
    private final List<String> options = new ArrayList<>();
    @Nullable Set<File> classpath;
    
    /**
     * Creates a {@code Compiler} with the given underlying compiler.
     * 
     * @param compiler the Java compiler
     */
    Compiler(JavaCompiler compiler, File classOutput, File sourceOutput) {
        this.compiler = compiler;
        this.classOutput = classOutput;
        this.sourceOutput = sourceOutput;
    }
    
    
    /**
     * Compiles the given Java source files.
     * 
     * @param files the Java source files to be compiled
     * @return the results of this compilation
     */
    public Results compile(JavaFileObject... files) {
        return compile(List.of(files));
    }
    
    /**
     * Compiles the given Java source files.
     * 
     * @param files the Java source files to be compiled
     * @return the results of this compilation
     */
    public Results compile(List<JavaFileObject> files) {
        var diagnostics = new Diagnostics();
        var manager = compiler.getStandardFileManager(diagnostics, Locale.getDefault(), UTF_8);

        setLocation(manager, StandardLocation.CLASS_OUTPUT, List.of(classOutput));
        setLocation(manager, StandardLocation.SOURCE_OUTPUT, List.of(sourceOutput));
        if (classpath != null) {
            setLocation(manager, StandardLocation.CLASS_PATH, classpath);
        }
        
        var task = compiler.getTask(null, manager, diagnostics, options, null, files);
        task.setProcessors(processors);

        var success = task.call();
        var generatedSources = new ArrayList<JavaFileObject>();

        try {
            var outputs = new ArrayList<File>();
            for (var file : manager.getLocation(StandardLocation.SOURCE_OUTPUT)) {
                Files.walk(file.toPath())
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .forEach(outputs::add);
            }

            manager.getJavaFileObjectsFromFiles(outputs).forEach(generatedSources::add);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return new Results(files, generatedSources, diagnostics, success);
    }
    
    /**
     * Associates the given search paths with the given location.
     * 
     * @param manager the manager
     * @param location the location
     * @param paths the search paths
     * 
     * @throws UncheckedIOException if {@code location} is an output location and
     *         does not represent an existing directory
     */
    void setLocation(StandardJavaFileManager manager, StandardLocation location, Iterable<File> paths) {
        try {
            manager.setLocation(location, paths);
            
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    
    /**
     * Adds the given annotation processors to this {@code Compiler}.
     * 
     * @param processors the annotation processors
     * @return {@code this}
     */
    public Compiler processors(Processor... processors) {
        Collections.addAll(this.processors, processors);
        return this;
    }
    
    /**
     * Adds the given annotation processors to this {@code Compiler}.
     * 
     * @param processors the annotation processors
     * @return {@code this}
     */
    public Compiler processors(Collection<Processor> processors) {
        this.processors.addAll(processors);
        return this;
    }
    
    
    /**
     * Adds the given compiler options to this {@code Compiler}.
     * 
     * @param options the compiler options
     * @return {@code this}
     * 
     * @see <a href = "https://docs.oracle.com/en/java/javase/11/tools/javac.html">javac options</a>
     */
    public Compiler options(String... options) {
        Collections.addAll(this.options, options);
        return this;
    }
    
    /**
     * Adds the given compiler options to this {@code Compiler}.
     * 
     * @param options the compiler options
     * @return {@code this}
     * 
     * @see <a href = "https://docs.oracle.com/en/java/javase/11/tools/javac.html">javac options</a>
     */
    public Compiler options(Collection<String> options) {
        this.options.addAll(options);
        return this;
    }
    
    
    /**
     * Adds the module and its transitive dependencies to the compilation classpath.
     * 
     * @param module the module
     * @return {@code this}
     */
    public Compiler module(Module module) {
        var layer = module.getLayer();
        if (layer == null) {
            return this;
        }
        
        for (var resolved : layer.configuration().modules()) {
            var location = resolved.reference().location().orElseThrow(() -> new IllegalStateException("Could not find location for module: " + resolved.name()));
            classpath().add(new File(location.getPath()));
        }

        return this;
    }
    
    
    /**
     * Adds the current classpath as the compilation classpath.
     * 
     * @return {@code this}
     */
    public Compiler currentClasspath() {
        return classpath(getClass().getClassLoader());
    }
    
    /**
     * Adds the classpath of the given {@code ClassLoader} as the compilation classpath.
     * 
     * @param loader the {@code ClassLoader} which classpath is to be used during compilation
     * @return {@code this}
     * 
     * @throws IllegalArgumentException if the given {@code ClassLoader} or its parents are neither
     *         {@code URLClassLoader}s nor the system/platform classloader, or if they contain a 
     *         classpath with folders
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
        
        classpath().addAll(paths.stream().map(File::new).collect(toSet()));
        
        return this;
    }
    
    /**
     * Adds the given classpath as the compilation classpath.
     * 
     * @param files the compilation classpath
     * @return {@code this}
     */
    public Compiler classpath(Collection<File> files) {
        
        classpath().addAll(files);
        return this;
    }
    
    
    Set<File> classpath() {
        if (classpath == null) {
            classpath = new HashSet<>();
        }
        
        return classpath;
    }
    
}
