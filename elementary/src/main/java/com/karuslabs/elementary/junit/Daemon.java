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

import com.karuslabs.annotations.Ignored;
import com.karuslabs.elementary.junit.DaemonCompiler.Environment;
import com.karuslabs.elementary.junit.annotations.*;

import java.lang.reflect.*;

import org.checkerframework.checker.nullness.qual.Nullable;

import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.*;
import org.junit.jupiter.api.extension.InvocationInterceptor.Invocation;

/**
 * An extension that manages the lifecycle of a daemon compiler.
 */
abstract class Daemon implements TestInstanceFactory, InvocationInterceptor, AfterAllCallback {
    
    private static final String PARALLEL = "junit.jupiter.execution.parallel.enabled";
    private static final String PARALLEL_CLASS_MODE = "junit.jupiter.execution.parallel.mode.classes.default";
    private static final String PARALLEL_METHOD_MODE = "junit.jupiter.execution.parallel.mode.default";
    private static final String COMPILER = "tools.extension.compiler";
    
    /**
     * Starts the compiler and creates an instance of the test class if it has only 1 
     * constructor with supported parameters.
     * 
     * @param factory the factory
     * @param context the extension context
     * @return an instance of the test class
     * @throws TestInstantiationException if the test class has more than 1 constructor
     * @throws UnsupportedOperationException if parallel test execution is enabled
     */
    @Override
    public Object createTestInstance(@Ignored TestInstanceFactoryContext factory, ExtensionContext context) throws TestInstantiationException {
        var constructors = context.getRequiredTestClass().getDeclaredConstructors();
        if (constructors.length != 1) {
            throw new TestInstantiationException("Test class contains " + constructors.length + " constructors, should contain only 1");
        }
         
        if (parallel(context)) {
            throw new UnsupportedOperationException("ToolsExtension currently does not support parallel test execution");
        }
        
        return create(constructors[0], initialize(context));
    }
    
    /**
     * Creates an instance of the test class using the given constructor and environment.
     * 
     * @param constructor the constructor
     * @param environment the environment
     * @return an instance of the test class
     * @throws TestInstantiationException if an instance could not be created
     */
    abstract Object create(Constructor constructor, Environment environment) throws TestInstantiationException;
    
    Environment initialize(ExtensionContext context) {
        var compiler = compiler(context);
        
        if (compiler == null) {
            compiler = DaemonCompiler.of(context.getRequiredTestClass());
            compiler.start();
            
            context.getStore(Namespace.create(getClass(), context.getRequiredTestClass())).put(COMPILER, compiler);
        }
        
        var environment = compiler.environment();
        Tools.environment = environment;
        
        return environment;
    }
    
    /**
     * Determines if parallel test execution is enabled.
     * 
     * @param context the context
     * @return {@code true} if parallel test execution is enabled
     */
    private boolean parallel(ExtensionContext context) {
        return context.getConfigurationParameter(PARALLEL).orElse("").equalsIgnoreCase("true")
            && (context.getConfigurationParameter(PARALLEL_CLASS_MODE).orElse("").equalsIgnoreCase("concurrent")
             || context.getConfigurationParameter(PARALLEL_METHOD_MODE).orElse("").equalsIgnoreCase("concurrent"));
    }
    
    
    /**
     * Intercepts the test method to determine if it is annotated with {@code @Classpath} or {@code @Inline}. 
     * 
     * @param invocation the invocation
     * @param method the method
     * @param context the context
     * @throws IllegalArgumentException if the test method is annotated with {@code @Classpath} or {@code @Inline}
     * @throws Throwable
     */
    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> method, @Ignored ExtensionContext context) throws Throwable {
        var executable = method.getExecutable();
        if (executable.getAnnotationsByType(Classpath.class).length > 0 || executable.getAnnotationsByType(Inline.class).length > 0) {
            throw new IllegalArgumentException("Method cannot be annotated with @Classpath or @Inline when using ToolsExtension");
        }
        
        invocation.proceed();
    }

    /**
     * Tears down the daemon compiler.
     * 
     * @param context the context
     */
    @Override
    public void afterAll(ExtensionContext context) {
        compiler(context).shutdown();        
        Tools.environment = null;
    }
    
    
    /**
     * Retrieves a cached instance of the compiler for the test class.
     * 
     * @param context the context
     * @return a cached instance of the compiler if available; else {@code null}
     */
    @Nullable DaemonCompiler compiler(ExtensionContext context) { 
        return context.getStore(Namespace.create(getClass(), context.getRequiredTestClass())).get(COMPILER, DaemonCompiler.class);
    }
    
}
