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

import com.karuslabs.elementary.*;
import com.karuslabs.elementary.Compiler;
import com.karuslabs.elementary.junit.DaemonCompiler.DaemonProcessor;
import com.karuslabs.elementary.junit.annotations.Inline;

import java.util.*;
import java.util.concurrent.CompletionException;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;

import com.sun.source.util.Trees;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Inline(name = "invalid", source = "f")
class DaemonCompilerTest {

    @Test
    void run() {
        var compiler = DaemonCompiler.of(Object.class);
        compiler.start();
        
        var types = compiler.environment().typeMirrors;
        assertTrue(types.isSameType(types.type(String.class), types.type(String.class)));
        
        compiler.shutdown();
    }
    
    @Test
    void run_compiler_crash() {
        var compiler = mock(Compiler.class);
        when(compiler.processors(any(Processor[].class))).thenReturn(compiler);
        when(compiler.compile(any(List.class))).thenThrow(RuntimeException.class);
        
        var daemon = new DaemonCompiler(compiler, List.of());
        daemon.start();
        
        assertEquals(CompilationException.class, assertThrows(CompletionException.class, daemon::environment).getCause().getClass());
    }
    
    @Test
    void run_invalid_source() {
        var compiler = DaemonCompiler.of(DaemonCompilerTest.class);
        compiler.start();
        
        assertEquals(
            "com.karuslabs.elementary.CompilationException: invalid.java:1: error: reached end of file while parsing\n" +
            "f\n" +
            "^",
            assertThrows(CompletionException.class, compiler::environment).getMessage()
        );
    }
    
}

class DaemonProcessorTest {
    
    DaemonProcessor processor = new DaemonProcessor();
    ProcessingEnvironment environment = mock(ProcessingEnvironment.class);
    RoundEnvironment round = mock(RoundEnvironment.class);

    @Test
    void process() {
        try (MockedStatic<Trees> trees = mockStatic(Trees.class)) {
            trees.when(() -> Trees.instance(environment))
                    .thenReturn(mock(Trees.class));
            
            new Thread(() -> processor.completion.countDown()).start();

            processor.init(environment);

            assertFalse(processor.process(Set.of(), round));
            assertEquals(0L, processor.completion.getCount());
        }
    }
    
    @Test
    void process_ignored() {
        processor.init(mock(ProcessingEnvironment.class));
        
        processor.environment.complete(null);
        
        assertFalse(processor.process(Set.of(), round));
        assertEquals(1L, processor.completion.getCount());
    }
    
    @Test
    void getSupportedSourceVersion() {
        assertEquals(SourceVersion.latest(), processor.getSupportedSourceVersion());
    }
    
}
