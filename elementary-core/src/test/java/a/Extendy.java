package a;


import a.ToolkitProcessor.Toolkit;
import static com.karuslabs.elementary.Compiler.javac;
import static com.karuslabs.elementary.file.FileObjects.ofLines;

import java.lang.reflect.Method;
import javax.lang.model.util.Elements;
import org.junit.jupiter.api.extension.BeforeEachCallback;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.junit.jupiter.api.extension.TestInstanceFactory;
import org.junit.jupiter.api.extension.TestInstanceFactoryContext;
import org.junit.jupiter.api.extension.TestInstantiationException;

import static org.junit.platform.commons.util.ReflectionUtils.newInstance;


public class Extendy implements BeforeEachCallback, TestInstanceFactory, InvocationInterceptor {
    
    public static Toolkit kit() {
        return kit
    }
    
    static Toolkit kit;
    
    private ToolkitProcessor processor = new ToolkitProcessor();
    private Thread thread = new Thread() {
        @Override
        public void run() {
            javac().currentClasspath().processors(processor)
                   .compile(ofLines("com.karuslabs.Help", "package com.karuslabs.help; class Help {}"));
        }
    };
    
    @Override
    public void beforeEach(ExtensionContext ec) throws Exception {
        
        System.out.println("Before each");
    }
    
    
    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
        processor.invocations.add(invocation);
        processor.semaphore.release(1);
        processor.semaphore.acquire(2);
    }

    @Override
    public Object createTestInstance(TestInstanceFactoryContext c, ExtensionContext e) throws TestInstantiationException {
        thread.start();
        while (processor.kit == null) {}
        kit = processor.kit;
        
        return newInstance(c.getTestClass());
    }

    

}
