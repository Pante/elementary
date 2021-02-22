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
import com.karuslabs.elementary.Result;
import com.karuslabs.elementary.junit.annotations.*;

import java.util.*;
import java.lang.reflect.AnnotatedElement;
import javax.annotation.processing.Processor;
import javax.tools.JavaFileObject;

import org.junit.jupiter.api.extension.*;

import static com.karuslabs.elementary.Compiler.javac;
import static com.karuslabs.elementary.file.FileObjects.*;

public class JavacExtension implements ParameterResolver {

    private static final JavaFileObject SOURCE = ofLines("Dummy", "final class Dummy {}");
    private static final String[] EMPTY = new String[] {};
    
    @Override
    public Object resolveParameter(ParameterContext parameter, ExtensionContext context) throws ParameterResolutionException {
        var type = context.getRequiredTestClass();
        var executable = parameter.getDeclaringExecutable();
        
        var compiler = javac();
        resolve(compiler, type);
        resolve(compiler, executable);
        
        var files = scan(type);
        files.addAll(scan(executable));
        
        return compiler.currentClasspath().compile(files);
    }
    
    void resolve(Compiler compiler, AnnotatedElement annotated) {
        var flags = annotated.getAnnotation(Flags.class);
        compiler.options(flags == null ? EMPTY : flags.value().split(" -"));
        
        var processors = new ArrayList<Processor>();
        var annotation = annotated.getAnnotation(Processors.class);
        if (annotation != null) {
            for (var type : annotation.value()) {
                try {
                    processors.add(type.getDeclaredConstructor().newInstance());

                } catch (ReflectiveOperationException e) {
                    throw new ParameterResolutionException("Failed to create \"" + type.getName() + "\", annotation processor should have a constructor with no arguments", e);
                }
            }
        }
        
        compiler.processors(processors);
    }
    
    
    @Override
    public boolean supportsParameter(ParameterContext parameter, ExtensionContext context) {
        return parameter.getParameter().getType() == Result.class;
    }

}
