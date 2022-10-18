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
import com.karuslabs.elementary.junit.annotations.ModulePath;
import com.karuslabs.elementary.junit.annotations.Options;
import com.karuslabs.elementary.junit.annotations.ProcessorPath;
import com.karuslabs.elementary.junit.annotations.Processors;

import java.util.List;
import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.element.TypeElement;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import static com.karuslabs.elementary.Compiler.javac;
import static com.karuslabs.elementary.junit.annotations.ModulePath.Repository.REPO1;
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
                    "--module-source-path", "./src/test/resources/",
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
                    "--module-source-path", "./src/test/resources/my/modules",
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
    @ModulePath("foo.bar:baz:1.0-RC1")
    class ModulePathAnnotationTest {

        @Test
        @EnabledIfEnvironmentVariable(named = "HOME", matches = "(.*)")
        void resolve_happyflow_modulePathLinux() throws IllegalAccessException, NoSuchFieldException {
            // prepare
            var compiler = javac();
            var annotated = this.getClass();

            var expectedOptions = List.of(
                    "--module-path", System.getenv("HOME") + "/.m2/repository/foo/bar/baz/1.0-RC1/baz-1.0-RC1.jar"
            );

            // execute
            extension.resolve(compiler, annotated);

            // verify
            var optionsField = Compiler.class.getDeclaredField("options");
            optionsField.setAccessible(true);
            assertIterableEquals(expectedOptions, (Iterable<?>) optionsField.get(compiler));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "USERPROFILE", matches = "(.*)")
        void resolve_happyflow_modulePathWindows() throws IllegalAccessException, NoSuchFieldException {
            // prepare
            var compiler = javac();
            var annotated = this.getClass();

            var expectedOptions = List.of(
                    "--module-path", System.getenv("USERPROFILE") + "/.m2/repository/foo/bar/baz/1.0-RC1/baz-1.0-RC1.jar"
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
    @ModulePath("foo")
    class InvalidModulePathAnnotationTest {

        @Test
        void resolve_ParameterResolutionException_modulePathInvalidPattern() {
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
                    "Failed to resolve artifact \"foo\", the artifact should look like <groupId>:<artifactId>:<version>",
                    actualException.getMessage()
            );
        }
    }

    @Nested
    @Processors(ValidProcessor.class)
    @ModulePath(value = "foo.bar:baz:1.0-RC1", repository = REPO1)
    class ModulePathWithRepositoryAnnotationTest {

        @Test
        void resolve_happyflow_modulePathAndRepository() throws IllegalAccessException, NoSuchFieldException {
            // prepare
            var compiler = javac();
            var annotated = this.getClass();
            var expectedOptions = List.of(
                    "--module-path", "https://repo1.maven.org/maven2/foo/bar/baz/1.0-RC1/baz-1.0-RC1.jar"
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
    @ProcessorPath("foo.bar:baz:1.0-RC1")
    class ProcessorPathAnnotationTest {

        @Test
        @EnabledIfEnvironmentVariable(named = "HOME", matches = "(.*)")
        void resolve_happyflow_processorPathLinux() throws IllegalAccessException, NoSuchFieldException {
            // prepare
            var compiler = javac();
            var annotated = this.getClass();

            var expectedOptions = List.of(
                    "--processor-path", System.getenv("HOME") + "/.m2/repository/foo/bar/baz/1.0-RC1/baz-1.0-RC1.jar"
            );

            // execute
            extension.resolve(compiler, annotated);

            // verify
            var optionsField = Compiler.class.getDeclaredField("options");
            optionsField.setAccessible(true);
            assertIterableEquals(expectedOptions, (Iterable<?>) optionsField.get(compiler));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "USERPROFILE", matches = "(.*)")
        void resolve_happyflow_processorPathWindows() throws IllegalAccessException, NoSuchFieldException {
            // prepare
            var compiler = javac();
            var annotated = this.getClass();

            var expectedOptions = List.of(
                    "--processor-path", System.getenv("USERPROFILE") + "/.m2/repository/foo/bar/baz/1.0-RC1/baz-1.0-RC1.jar"
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
    @ProcessorPath("foo")
    class InvalidProcessorPathAnnotationTest {

        @Test
        void resolve_ParameterResolutionException_processorPathInvalidPattern() {
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
                    "Failed to resolve artifact \"foo\", the artifact should look like <groupId>:<artifactId>:<version>",
                    actualException.getMessage()
            );
        }
    }

    @Nested
    @Processors(ValidProcessor.class)
    @ProcessorPath(value = "foo.bar:baz:1.0-RC1", repository = REPO1)
    class ProcessorPathWithRepositoryAnnotationTest {

        @Test
        void resolve_happyflow_processorPathAndRepository() throws IllegalAccessException, NoSuchFieldException {
            // prepare
            var compiler = javac();
            var annotated = this.getClass();
            var expectedOptions = List.of(
                    "--processor-path", "https://repo1.maven.org/maven2/foo/bar/baz/1.0-RC1/baz-1.0-RC1.jar"
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
