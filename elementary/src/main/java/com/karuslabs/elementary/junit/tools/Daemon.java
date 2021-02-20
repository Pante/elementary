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

import com.karuslabs.annotations.Ignored;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.*;
import org.junit.jupiter.api.extension.InvocationInterceptor.Invocation;

public abstract class Daemon implements TestInstanceFactory, InvocationInterceptor, AfterAllCallback {
    
    private static final String PARALLEL = "junit.jupiter.execution.parallel.mode.default";
    private static final String PARALLEL_CLASS_MODE = "junit.jupiter.execution.parallel.mode.classes.default";
    private static final String PARALLEL_METHOD_MODE = "junit.jupiter.execution.parallel.mode.default";
    private static final String COMPILER = "tools.extension.compiler";
    
    @Override
    public Object createTestInstance(TestInstanceFactoryContext factory, ExtensionContext context) throws TestInstantiationException {
        var constructors = context.getRequiredTestClass().getDeclaredConstructors();
        if (constructors.length != 1) {
            throw new TestInstantiationException("Test class contains " + constructors.length + " constructors, should contain only 1");
        }
        
        if (parallel(context)) {
            throw new UnsupportedOperationException("ToolsExtension currently does not support parallel test execution");
        }
        
        return construct(constructors[0], initialize(context));
    }
    
    protected abstract Object construct(Constructor constructor, DaemonProcessor.Environment environment) throws TestInstantiationException;
    
    protected DaemonProcessor.Environment initialize(ExtensionContext context) {
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
    
    boolean parallel(ExtensionContext context) {
        return context.getConfigurationParameter(PARALLEL).orElse("false").equalsIgnoreCase("true")
            && (context.getConfigurationParameter(PARALLEL_CLASS_MODE).orElse("same_thread").equalsIgnoreCase("concurrent")
             || context.getConfigurationParameter(PARALLEL_METHOD_MODE).orElse("same_thread").equalsIgnoreCase("concurrent"));
    }
    
    
    
    @Override
    public void interceptTestMethod(Invocation<Void> invocation, @Ignored ReflectiveInvocationContext<Method> c, ExtensionContext context) throws Throwable {
        var throwable = compiler(context).processor.exchange(invocation);
        if (throwable != null) {
            throw throwable;
        }
    }

    @Override
    public void afterAll(ExtensionContext context) {
        var compiler = compiler(context);
        if (compiler != null) {
            compiler.interrupt();
        }
        
        Tools.environment = null;
    }
    
    
    DaemonCompiler compiler(ExtensionContext context) { 
        return context.getStore(Namespace.create(getClass(), context.getRequiredTestClass())).get(COMPILER, DaemonCompiler.class);
    }
}
