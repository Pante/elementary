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

import com.karuslabs.annotations.*;
import com.karuslabs.elementary.Compiler;
import com.karuslabs.elementary.CompilationException;
import com.karuslabs.utilitary.Logger;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.*;
import java.util.concurrent.*;
import java.lang.reflect.AnnotatedElement;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.*;
import javax.tools.JavaFileObject;

import static com.karuslabs.elementary.Compiler.javac;
import static com.karuslabs.elementary.file.FileObjects.*;
import static javax.lang.model.SourceVersion.latest;

/**
 * Represents a Java compiler that is invoked on a daemon thread. The compiler 
 * is invoked with a blocking annotation processor. The annotation processor suspends 
 * processing to allow other threads to use facilities accessible only in an annotation 
 * processing environment, i.e. {@code javax.lang.model.*}. Said facilities can be accessed 
 * safely via {@code DaemonCompiler.environment()}. {@code DaemonCompiler.shutdown()} 
 * should be called once the environment is no longer needed.
 */
class DaemonCompiler extends Thread {
    
    /**
     * Creates a {@code DaemonCompiler} that compiles the Java source files provided
     * by {@code @Classpath} and {@code Inline} annotations on the given annotated
     * class.
     * 
     * @param annotated the annotated elements
     * @return a {@code DaemonCompiler}
     */
    public static DaemonCompiler of(Class<?> annotated) {
        var files = scan(annotated);
        files.add(DUMMY);
        
        return new DaemonCompiler(javac().currentClasspath(), files);
    }
    
    
    private final DaemonProcessor processor = new DaemonProcessor();
    private final Compiler compiler;
    private final List<JavaFileObject> files;
    
    /**
     * Creates a DaemonCompiler with the given compiler and Java source files to
     * be compiled.
     * 
     * @param compiler the compiler
     * @param files the Java source files to be compiled
     */
    DaemonCompiler(Compiler compiler, List<JavaFileObject> files) {
        this.compiler = compiler.processors(processor);
        this.files = files;
    }
    
    @Override
    public void run() {
        try {
            var results = compiler.compile(files);
            if (!results.success) {
                throw new CompilationException(results.find().diagnostics());
            }
            
        } catch (Throwable e) {
            processor.environment.completeExceptionally(new CompilationException("Failed to start javac", e));
        }
    }
    
    /**
     * Returns the current annotation processing environment.
     * 
     * @return the current annotation processing environment
     */
    public Environment environment() {
        return processor.environment.join();
    }
    
    /**
     * Shuts down this {@code DaemonCompiler}.
     */
    public void shutdown() {
        processor.completion.countDown();
    }
    
    
    /**
     * An annotation processor that suspends execution until otherwise signalled
     * to shutdown.
     */
    @SupportedAnnotationTypes({"*"})
    static class DaemonProcessor extends AbstractProcessor {
        
        final CompletableFuture<Environment> environment = new CompletableFuture<>();
        final CountDownLatch completion = new CountDownLatch(1);
        @Lazy ProcessingEnvironment env;

        @Override
        public void init(ProcessingEnvironment env) {
            super.init(env);
            this.env = env;
        }

        @Override
        public boolean process(Set<? extends TypeElement> types, RoundEnvironment round) {
            if (environment.isDone()) {
                return false;
            }
            
            environment.complete(new Environment(round, env.getElementUtils(), env.getTypeUtils(), env.getMessager(), env.getFiler()));
            try {
                completion.await();
            } catch (InterruptedException e) {
                // ignored 
            }
            
            return false;
        }
        
        @Override
        public SourceVersion getSupportedSourceVersion() {
            return latest();
        }
        
    }
    
    /**
     * An annotation processing environment.
     */
    static @ValueType final class Environment {
        public final RoundEnvironment round;
        public final Elements elements;
        public final Types types;
        public final Messager messager;
        public final Filer filer;
        public final Cases cases;
        public final TypeMirrors typeMirrors;
        public final Logger logger;
        
        Environment(RoundEnvironment round, Elements elements, Types types, Messager messager, Filer filer) {
            this.round = round;
            this.elements = elements;
            this.types = types;
            this.messager = messager;
            this.filer = filer;
            cases = new Cases(round);
            typeMirrors = new TypeMirrors(elements, types);
            logger = new Logger(messager);
        }
    }
    
}
