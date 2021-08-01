/*
 * The MIT License
 *
 * Copyright 2021 Karus Labs.
 *
 * Permission is hereby granted, free annotation charge, to any person obtaining a copy
 * annotation this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies annotation the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions annotation the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.karuslabs.utilitary.snippet;

import com.karuslabs.elementary.junit.*;
import com.karuslabs.elementary.junit.annotations.*;

import java.util.*;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ToolsExtension.class)
@Introspect("IdentifiersLineTest")
@Case("annotations")
class AnnotationsPartTest {
    
    List<? extends AnnotationMirror> annotations = Tools.cases().one("annotations").getAnnotationMirrors();
    Part<AnnotationMirror, Line> line = Part.annotations(annotations, 0, 1);
    
    @Test
    void annotations_toString_() {
        assertEquals("@ExtendWith(ToolsExtension.class) @Introspect(\"IdentifiersLineTest\") @Case(\"annotations\") ", line.toString());
    }
    
    @Test
    void annotations_values() {
        assertEquals(3, line.values.size());
        assertEquals("@ExtendWith(ToolsExtension.class)", line.values.get(annotations.get(0)).toString());
        assertEquals("@Introspect(\"IdentifiersLineTest\")", line.values.get(annotations.get(1)).toString());
        assertEquals("@Case(\"annotations\")", line.values.get(annotations.get(2)).toString());
    }

}

@ExtendWith(ToolsExtension.class)
@Introspect("IdentifiersLineTest")
class ModifiersPartTest {
    
    Part<Modifier, Line> line = Part.modifiers(Set.of(PUBLIC, STATIC, ABSTRACT), 0, 1);
    
    @Test
    void toString_() {
        assertEquals("public static abstract ", line.toString());
    }
    
    @Test
    void values() {
        assertEquals(3, line.values.size());
        assertEquals("public", line.values.get(PUBLIC).toString());
        assertEquals("static", line.values.get(STATIC).toString());
        assertEquals("abstract", line.values.get(ABSTRACT).toString());
    }

}

@ExtendWith(ToolsExtension.class)
@Introspect("IdentifiersLineTest")
@Case("empty")
class GenericsPartTest {
    
    @Case("generics") static class Type<T extends String, U extends Line> {}
    
    List<? extends TypeParameterElement> parameters = ((TypeElement) Tools.cases().one("generics")).getTypeParameters();      
    Part<TypeParameterElement, Line> line = Part.typeParameters(parameters, 0, 1);
    
    @Test
    void toString_() {
        assertEquals("<T extends String, U extends Line>", line.toString());
    }
    
    @Test
    void toString_empty() {
        assertEquals("", Part.typeParameters(List.of(), 0, 1).toString());
    }
    
    @Test
    void values() {
        assertEquals(2, line.values.size());
        assertEquals("T extends String", line.values.get(parameters.get(0)).toString());
        assertEquals("U extends Line", line.values.get(parameters.get(1)).toString());
    }

}

@ExtendWith(ToolsExtension.class)
@Introspect
class ParametersPartTest {
    
    @Case("arguments")
    void arguments(List<String> a, int b) {}
    
    List<? extends VariableElement> parameters = ((ExecutableElement) Tools.cases().one("arguments")).getParameters();
    Part<VariableElement, VariableLine> line = Part.parameters(parameters, 0, 1);
    
    @Test
    void toString_() {
        assertEquals("(List<String> a, int b)", line.toString());
    }
    
    @Test
    void values() {
        assertEquals("List<String> a", line.values.get(parameters.get(0)).toString());
        assertEquals("int b", line.values.get(parameters.get(1)).toString());
    }

} 


@ExtendWith(ToolsExtension.class)
@Introspect("IdentifiersLineTest")
class ExceptionsPartTest {
    
    List<? extends TypeMirror> thrown = ((ExecutableElement) Tools.cases().one("case")).getThrownTypes();
    Part<TypeMirror, Line> line = Part.exceptions(thrown, 0, 0);
    
    @Test
    @Case("case")
    void toString_() throws IllegalArgumentException, NullPointerException {
        assertEquals(" throws IllegalArgumentException, NullPointerException", line.toString());
    }
    
    @Test
    void toString_empty() {
        assertEquals("", Part.exceptions(List.of(), 0, 0).toString());
    }
    
    @Test
    void values() {
        assertEquals(2, line.values.size());
        assertEquals("IllegalArgumentException", line.values.get(thrown.get(0)).toString());
        assertEquals("NullPointerException", line.values.get(thrown.get(1)).toString());
    }

} 
