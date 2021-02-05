package a;


import static com.karuslabs.elementary.Compiler.javac;
import static com.karuslabs.elementary.file.FileObjects.ofLines;

import java.lang.reflect.Method;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.junit.jupiter.api.extension.support.TypeBasedParameterResolver;


public class Extendy extends TypeBasedParameterResolver<ToolkitProcessor.Toolkit> implements InvocationInterceptor {
    
    private ToolkitProcessor processor = new ToolkitProcessor();
    private Thread thread = new Thread() {
        @Override
        public void run() {
            javac().currentClasspath().processors(processor)
                   .compile(ofLines("com.karuslabs.Help", "package com.karuslabs.help; class Help {}"));
        }
    };
    
    @Override
    public ToolkitProcessor.Toolkit resolveParameter(ParameterContext pc, ExtensionContext ec) throws ParameterResolutionException {
        thread.start();
        while (processor.kit == null) {
            
        }
        
        return processor.kit;
    }
    
    
    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
        processor.invocations.add(invocation);
        processor.semaphore.release(1);
        processor.semaphore.acquire(2);
    }

}
