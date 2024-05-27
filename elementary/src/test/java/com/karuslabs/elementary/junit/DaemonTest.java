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

import com.karuslabs.elementary.junit.DaemonCompiler.Environment;
import com.karuslabs.elementary.junit.annotations.*;

import java.lang.reflect.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.InvocationInterceptor.Invocation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;

class DaemonTest {

    Daemon daemon = new Daemon() {
        @Override
        Object create(Constructor<?> constructor, Environment environment) { return ""; }
    };
    
    @Test
    void createTestInstance_multiple_constructors() {
        var context = new MockContext(MultipleConstructors.class);
        assertEquals(
            "Test class contains 2 constructors, should contain only 1",
            assertThrows(TestInstantiationException.class, () -> daemon.createTestInstance(null, context)).getMessage()
        );
    }
    
    @ParameterizedTest
    @MethodSource("createTestInstance_parallel_parameters")
    void createTestInstance_parallel(boolean parallel, ExtensionContext context) {
        if (!parallel) {
            assertEquals("", daemon.createTestInstance(null, context));
        } else {
            assertEquals(
                "ToolsExtension currently does not support parallel test execution",
                assertThrows(UnsupportedOperationException.class, () -> daemon.createTestInstance(null, context)).getMessage()
            );
        }
    }
    
    static Stream<Arguments> createTestInstance_parallel_parameters() {
        return Stream.of(
            of(true, new MockContext(Normal.class).put(true, true, true)),
            of(true, new MockContext(Normal.class).put(true, true, false)),
            of(true, new MockContext(Normal.class).put(true, false, true)),
            of(false, new MockContext(Normal.class).put(false, true, true)),
            of(false, new MockContext(Normal.class).put(true, false, false))
        );
    }
    
    @ParameterizedTest
    @MethodSource("interceptMethod_parameters")
    void interceptTestMethod(Method method) {
        var invocation = mock(Invocation.class);
        ReflectiveInvocationContext<Method> context = when(mock(ReflectiveInvocationContext.class).getExecutable()).thenReturn(method).getMock();
        
        assertEquals(
            "Method cannot be annotated with @Classpath, @Inline or @Resource when using ToolsExtension",
            assertThrows(IllegalArgumentException.class, () -> daemon.interceptTestMethod(invocation, context, null)).getMessage()
        );
    }
    
    static Stream<Method> interceptMethod_parameters() throws ReflectiveOperationException {
        return Stream.of(
            Normal.class.getDeclaredMethod("a"),
            Normal.class.getDeclaredMethod("b")
        );
    }
    
}

class Normal {
    @Resource("a")
    void a() {}   
    
    @Inline(name = "a", source = "")
    void b() {}
}

class MultipleConstructors {
    MultipleConstructors(String a) {}

    MultipleConstructors(int a) {}
}
