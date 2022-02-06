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
package com.karuslabs.elementary;

import com.karuslabs.utilitary.AnnotationProcessor;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.*;

import org.junit.jupiter.api.*;

import static com.karuslabs.elementary.Compiler.javac;
import static com.karuslabs.elementary.file.FileObjects.*;
import static javax.lang.model.SourceVersion.latest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompilerTest {
    
    @Test
    void setLocation() throws IOException {
        var manager = mock(StandardJavaFileManager.class);
        doThrow(IOException.class).when(manager).setLocation(any(), any());
        
        assertThrows(UncheckedIOException.class, () -> javac().setLocation(manager, StandardLocation.CLASS_PATH, List.of()));
    }
    
    
    @Test
    void processors_varargs() {
        var results = javac().processors(new WarningProcessor()).compile(DUMMY);
        assertEquals(1, results.warnings.size());
    }
    
    @Test
    void processors_collection() {
        var results = javac().processors(List.of(new WarningProcessor())).compile(DUMMY);
        assertEquals(1, results.warnings.size());
    }
    
    
    @Test
    void processors_generated_files() {
         var results = javac().processors(List.of(new GeneratorProcessor())).compile(DUMMY);
         assertEquals(1, results.generated.size());
    }
    
    
    @Test
    void options_varargs() throws IOException, URISyntaxException {
        var results = javac().options("-nowarn").processors(new WarningProcessor()).compile(DUMMY);
        assertEquals(0, results.warnings.size());
    }
    
    @Test
    void options_collection() throws IOException, URISyntaxException {
        var results = javac().options(List.of("-nowarn")).processors(new WarningProcessor()).compile(DUMMY);
        assertEquals(0, results.warnings.size());
    }
    
    
    @Test
    void classpath_classloader() throws MalformedURLException {
        var loader = new URLClassLoader(new URL[] {new URL("file", "", 0, "")}, getClass().getClassLoader());
        var results = javac().classpath(loader).compile(DUMMY);
        
        assertTrue(results.diagnostics.isEmpty());
    }
    
    @Test
    void classpath_platform_classloader() {
        var results = javac().classpath(ClassLoader.getPlatformClassLoader()).compile(ofLines("Some", "import com.karuslabs.elementary.*; class Some {}"));
        assertEquals("package com.karuslabs.elementary does not exist", results.find().errors().one().getMessage(Locale.ENGLISH));
    }
    
    @Test
    void classpath_invalid_classloader() {
        assertEquals(
            "Given ClassLoader and its parents must be a URLClassLoader",
            assertThrows(IllegalArgumentException.class, () -> javac().classpath(mock(ClassLoader.class))).getMessage()
        );
    }
    
    @Test
    void classpath_invalid_url() throws MalformedURLException {
        var loader = new URLClassLoader(new URL[] {new URL("jar", "", 0, "")});
        assertEquals(
            "Given ClassLoader and its parents may not contain classpaths that consist of folders",
            assertThrows(IllegalArgumentException.class, () -> javac().classpath(loader)).getMessage()
        );
    }
    
    @Test
    void classpath_files() throws URISyntaxException {
        var file = new File(getClass().getClassLoader().getResource("com/karuslabs/elementary/junit").toURI());
        var results = javac().classpath(List.of(file)).compile(ofLines("B", "class B { void b() {new A().method();} }"));
        
        assertEquals("package org.checkerframework.checker.nullness.qual does not exist", results.find().list().get(0).getMessage(Locale.ENGLISH));
    }
    
}


@SupportedAnnotationTypes({"*"})
class GeneratorProcessor extends AnnotationProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment round) {
        if (round.processingOver()) {
            try {
                processingEnv.getFiler().createSourceFile("GeneratedFile", elements.getTypeElement("java.util.ArrayList"));
                
            } catch (IOException ex) {
                return false;
            }
        }
        
        return false;
    }
    
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latest();
    }
}