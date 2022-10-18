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
import com.karuslabs.elementary.junit.annotations.Module;
import com.karuslabs.elementary.junit.annotations.Options;
import com.karuslabs.elementary.junit.annotations.Processors;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.element.TypeElement;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import static com.karuslabs.elementary.Compiler.javac;
import static org.junit.jupiter.api.Assertions.*;

class JavacExtensionTest {
    
    JavacExtension extension = new JavacExtension();

    @Nested
    @Processors(ValidProcessor.class)
    @Options("foo bar")
    class OptionsAnnotationTest {
        
        @Test
        void resolve_happyflow_options() throws IllegalAccessException, NoSuchFieldException {
            // prepare
            var compiler = javac();
            var annotated = this.getClass();
            var expectedOptions = List.of("foo", "bar");

            // execute
            extension.resolve(compiler, annotated);
            
            // verify
            var optionsField = Compiler.class.getDeclaredField("options");
            optionsField.setAccessible(true);
            assertIterableEquals(expectedOptions, (Iterable<?>) optionsField.get(compiler));
        }
    }

    @Nested
    @Processors(ValidProcessor.class)
    @Module("foo")
    class ModuleAnnotationTest {

        @Test
        void resolve_happyflow_module() throws IllegalAccessException, NoSuchFieldException {
            // prepare
            var compiler = javac();
            var annotated = this.getClass();
            var expectedOptions = List.of(
                    "--module", "foo", 
                    "--module-source-path", "." + File.separatorChar + "src" + File.separatorChar + "test" + File.separatorChar + "resources" + File.separatorChar,
                    "-d", "."
            );

            // execute
            extension.resolve(compiler, annotated);

            // verify
            var optionsField = Compiler.class.getDeclaredField("options");
            optionsField.setAccessible(true);
            assertIterableEquals(expectedOptions, (Iterable<?>) optionsField.get(compiler));
        }
    }

    @Nested
    @Processors(ValidProcessor.class)
    @Module(value = "foo", sourcePath = "my/modules")
    class ModuleWithSourcePathAnnotationTest {

        @Test
        void resolve_happyflow_moduleAndSourcePath() throws IllegalAccessException, NoSuchFieldException {
            // prepare
            var compiler = javac();
            var annotated = this.getClass();
            var expectedOptions = List.of(
                    "--module", "foo",
                    "--module-source-path", "." + File.separatorChar + "src" + File.separatorChar + "test" + File.separatorChar + "resources" + File.separatorChar + "my/modules",
                    "-d", "."
            );

            // execute
            extension.resolve(compiler, annotated);

            // verify
            var optionsField = Compiler.class.getDeclaredField("options");
            optionsField.setAccessible(true);
            assertIterableEquals(expectedOptions, (Iterable<?>) optionsField.get(compiler));
        }
    }
    
    
    @Nested
    @Processors(InvalidProcessor.class)
    class InvalidProcessorTest {

        @Test
        void resolve_fails_invalidProcessor() {
            // prepare
            var compiler = javac();
            var annotated = this.getClass();
            
            // execute
            var actualException = assertThrows(
                    ParameterResolutionException.class, 
                    () -> extension.resolve(compiler, annotated)
            );
            
            // verify
            assertEquals(
                    "Failed to create \"" + InvalidProcessor.class.getName() + "\", annotation processor should have a constructor with no arguments",
                    actualException.getMessage()
            );
        }
    }

    static class InvalidProcessor extends AbstractProcessor {

        InvalidProcessor(String a) {
        }

        @Override
        public boolean process(Set<? extends TypeElement> types, RoundEnvironment round) {
            return false;
        }
    }

    static class ValidProcessor extends AbstractProcessor {

        @Override
        public boolean process(Set<? extends TypeElement> types, RoundEnvironment round) {
            return false;
        }
    }
}
