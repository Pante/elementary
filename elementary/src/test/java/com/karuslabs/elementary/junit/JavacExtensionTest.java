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

import com.karuslabs.elementary.Results;
import com.karuslabs.elementary.junit.annotations.*;

import java.io.*;
import java.nio.file.Paths;
import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.StandardLocation;

import com.karuslabs.utilitary.AnnotationProcessor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.io.TempDir;

import static com.karuslabs.elementary.Compiler.javac;
import static org.junit.jupiter.api.Assertions.*;

@Processors(InvalidProcessor.class)
class JavacExtensionTest {

    @TempDir
    File classes;
    @TempDir
    File sources;

    JavacExtension extension = new JavacExtension();
    
    @Test
    void resolve_fails() {
        assertEquals(
            "Failed to create \"" + InvalidProcessor.class.getName() + "\", annotation processor should have a constructor with no arguments",
            assertThrows(
                ParameterResolutionException.class,
                () -> extension.resolveOptions(javac(classes, sources), JavacExtensionTest.class)
            ).getMessage()
        );
    }
    
}

class InvalidProcessor extends AbstractProcessor {

    InvalidProcessor(String a) {}

    @Override
    public boolean process(Set<? extends TypeElement> types, RoundEnvironment round) {
        return false;
    }

}


@ExtendWith(JavacExtension.class)
@Options("-Werror")
@Processors({ElementaryIssue315Processor.class})
@Classpath("com.karuslabs.elementary.junit.example.ValidCase")
class ElementaryIssue315Test {

    @Test
    void generate_source_does_not_throw_error(Results results) {
        assertTrue(results.errors.isEmpty());
    }

}

@SupportedAnnotationTypes({"*"})
class ElementaryIssue315Processor extends AnnotationProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            try {
                var file = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", "ignore.tmp");
                Paths.get(file.toUri()).getParent();

            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }
}


