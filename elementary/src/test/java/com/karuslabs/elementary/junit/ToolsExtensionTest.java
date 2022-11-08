package com.karuslabs.elementary.junit;

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
import com.karuslabs.elementary.junit.annotations.*;
import com.karuslabs.utilitary.Logger;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.lang.reflect.*;
import javax.annotation.processing.*;
import javax.lang.model.util.*;

import com.sun.source.util.Trees;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ToolsExtension.class)
@Introspect
class ToolsExtensionTest {

    ToolsExtension extension = spy(new ToolsExtension());
    
    @Test
    void resolveParmeter_unsupported() {
        var parameter = mock(Parameter.class);
        doReturn(String.class).when(parameter).getType();
        
        ParameterContext context = when(mock(ParameterContext.class).getParameter()).thenReturn(parameter).getMock();
        
        doReturn(mock(DaemonCompiler.class)).when(extension).compiler(any());
        
        assertEquals(
            "Unable to resolve parameter of type: " + String.class.getName(),
            assertThrows(ParameterResolutionException.class, () -> extension.resolveParameter(context, new MockContext(Normal.class))).getMessage()
        );
    }
    
    @Test
    void create_invalid_type() {
        assertEquals(
            "Unable to resolve parameter of type: " + String.class.getName(),
            assertThrows(ParameterResolutionException.class, () -> extension.create(InvalidConstructor.class.getDeclaredConstructors()[0], null)).getMessage()
        );
    }
    
    @Test
    void introspect_class(Labels labels) {
        assertEquals(1, labels.size());
    }
    
    static class IntrospectMethod {
        
        @Label("single")
        void test() {}
        
    }
    
    
    @Test
    void labels(Labels labels) {
        assertNotNull(labels);
    }
    
    @Test
    void round(RoundEnvironment round) {
        assertNotNull(round);
    }
    
    @Test
    void elements(Elements elements) {
        assertNotNull(elements);
    }
    
    @Test
    void types(Types types) {
        assertNotNull(types);
    }

    @Test
    void types(Trees trees) {
        assertNotNull(trees);
    }
    
    @Test
    void messager(Messager messager) {
        assertNotNull(messager);
    }
    
    @Test
    void filer(Filer filer) {
        assertNotNull(filer);
    }
    
    @Test
    void typeMirrors(TypeMirrors types) {
        assertNotNull(types);
    }
    
    @Test
    void logger(Logger logger) {
        assertNotNull(logger);
    }
    
}

class ValidConstructor {
    
}

class InvalidConstructor {
    InvalidConstructor(String a) {}
}