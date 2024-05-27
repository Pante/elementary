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

import com.karuslabs.elementary.Compiler;
import com.karuslabs.elementary.Results;
import com.karuslabs.elementary.junit.annotations.*;

import java.util.*;
import java.lang.reflect.AnnotatedElement;
import javax.annotation.processing.Processor;

import org.junit.jupiter.api.extension.*;

import static com.karuslabs.elementary.Compiler.javac;
import static com.karuslabs.elementary.file.FileObjects.scan;

/**
 * A JUnit extension that compiles Java source files specified by {@code @Classpath},
 * {@code @Inline}, {@code @Introspect} and {@code @Resource} annotations on the 
 * test class and method. Results of the compilation may be obtained by specifying 
 * {@link Results} as a test method parameter. The only constructor and method parameter
 * that this extension supports is {@code Results}.
 * 
 * @see com.karuslabs.elementary.junit.annotations
 */
public class JavacExtension implements ParameterResolver, AfterEachCallback {

    private static final String[] EMPTY = new String[] {};

    @Override
    public Object resolveParameter(ParameterContext parameter, ExtensionContext context) throws ParameterResolutionException {
        var type = context.getRequiredTestClass();
        var executable = parameter.getDeclaringExecutable();
        
        var entry = Generations.initialize(context);
        var compiler = javac(entry.getKey(), entry.getValue());
        resolveOptions(compiler, type);
        resolveOptions(compiler, executable);
        
        var files = scan(type);
        files.addAll(scan(executable));

        context.getStore(ExtensionContext.Namespace.create(JavacExtension.class)).put(Compiler.class, compiler);
        
        return compiler.currentClasspath().compile(files);
    }
    
    /**
     * Modifies the compiler using the annotations on the given annotated element.
     * 
     * @param compiler the compiler
     * @param annotated the annotated element
     */
    void resolveOptions(Compiler compiler, AnnotatedElement annotated) {
        var flags = annotated.getAnnotation(Options.class);
        compiler.options(flags == null ? EMPTY : flags.value().split(" "));
        
        var processors = new ArrayList<Processor>();
        var annotation = annotated.getAnnotation(Processors.class);
        if (annotation != null) {
            for (var type : annotation.value()) {
                try {
                    var constructor = type.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    processors.add(constructor.newInstance());

                } catch (ReflectiveOperationException e) {
                    throw new ParameterResolutionException("Failed to create \"" + type.getName() + "\", annotation processor should have a constructor with no arguments", e);
                }
            }
        }
        
        compiler.processors(processors);
    }
    
    
    /**
     * Tests if a parameter is a {@code Results}.
     * 
     * @param parameter the parameter's context
     * @param context this extension's context
     * @return {@code true} if the parameter is a {@code Results}; otherwise {@code false}
     */
    @Override
    public boolean supportsParameter(ParameterContext parameter, ExtensionContext context) {
        return parameter.getParameter().getType() == Results.class;
    }


    @Override
    public void afterEach(ExtensionContext context) {
        Generations.teardown(context);
    }

}
