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
import java.util.regex.*;
import java.util.stream.Collectors;
import javax.annotation.processing.Processor;

import com.karuslabs.elementary.junit.annotations.Module;
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
public class JavacExtension implements ParameterResolver {

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

    /**
     * Modifies the compiler using the annotations on the given annotated element.
     *
     * @param compiler  the compiler
     * @param annotated the annotated element
     */
    void resolve(Compiler compiler, AnnotatedElement annotated) {
        resolveOptions(compiler, annotated);
        resolveModuleOptions(compiler, annotated);
        resolveModulePathOptions(compiler, annotated);
        resolveProcessorPathOptions(compiler, annotated);
        resolveProcessors(compiler, annotated);
    }

    private static void resolveOptions(Compiler compiler, AnnotatedElement annotated) {
        var optionsAnnotation = annotated.getAnnotation(Options.class);
        if (optionsAnnotation != null) {
            compiler.options(Arrays.asList(optionsAnnotation.value().split(" ")));
        }
    }

    private static void resolveModuleOptions(Compiler compiler, AnnotatedElement annotated) {
        var moduleAnnotation = annotated.getAnnotation(Module.class);
        if (moduleAnnotation != null) {
            compiler.options("--module", moduleAnnotation.value());
            compiler.options("--module-source-path", "./src/test/resources/" + moduleAnnotation.sourcePath());

            // The value of this option is not important, but it does have to be supplied
            compiler.options("-d", ".");
        }
    }

    private static void resolveModulePathOptions(Compiler compiler, AnnotatedElement annotated) {
        var modulePathAnnotations = annotated.getAnnotationsByType(ModulePath.class);
        if (modulePathAnnotations.length > 0) {
            var modulePath = Arrays.stream(modulePathAnnotations)
                    .map(modulePathAnnotation -> getPath(modulePathAnnotation.value(), resolveEnvVars(modulePathAnnotation.repository())))
                    .collect(Collectors.joining(";"));
            compiler.options("--module-path", modulePath);
        }
    }

    private static void resolveProcessorPathOptions(Compiler compiler, AnnotatedElement annotated) {
        var modulePathAnnotations = annotated.getAnnotationsByType(ProcessorPath.class);
        if (modulePathAnnotations.length > 0) {
            var modulePath = Arrays.stream(modulePathAnnotations)
                    .map(modulePathAnnotation -> getPath(modulePathAnnotation.value(), resolveEnvVars(modulePathAnnotation.repository())))
                    .collect(Collectors.joining(";"));
            compiler.options("--processor-path", modulePath);
        }
    }

    private static String getPath(String artifact, String repository) {
        var artifactSegments = artifact.split(":");
        if (artifactSegments.length != 3) {
            throw new ParameterResolutionException("Failed to resolve artifact \"" + artifact + "\", the artifact should look like <groupId>:<artifactId>:<version>");
        }
        var relPath = artifactSegments[0].replace(".", "/") + "/" + artifactSegments[1] + "/" + artifactSegments[2] + "/" + artifactSegments[1] + "-" + artifactSegments[2] + ".jar";

        if (repository.endsWith("/")){
            return repository + relPath;
        }
        return repository + "/" + relPath;
    }

    private static String resolveEnvVars(String rawRepository) {
        Matcher envVarMatcher = Pattern.compile("\\$\\{([^}]*)}").matcher(rawRepository);
        return envVarMatcher.replaceAll(matchResult -> {
            var env = System.getenv(matchResult.group(1));
            return env != null ? env.replace("\\", "\\\\") : "";
        });
    }

    private static void resolveProcessors(Compiler compiler, AnnotatedElement annotated) {
        var processors = new ArrayList<Processor>();
        var processorsAnnotation = annotated.getAnnotation(Processors.class);
        if (processorsAnnotation != null) {
            for (var type : processorsAnnotation.value()) {
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
     * @param context   this extension's context
     * @return {@code true} if the parameter is a {@code Results}; otherwise {@code false}
     */
    @Override
    public boolean supportsParameter(ParameterContext parameter, ExtensionContext context) {
        return parameter.getParameter().getType() == Results.class;
    }

}
