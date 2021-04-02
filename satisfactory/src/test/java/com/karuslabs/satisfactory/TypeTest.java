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
package com.karuslabs.satisfactory;

import com.karuslabs.elementary.junit.*;
import com.karuslabs.elementary.junit.annotations.*;
import com.karuslabs.utilitary.type.TypeMirrors;

import javax.lang.model.type.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ToolsExtension.class)
@Introspect
class TypeTest {

    TypeMirrors types = Tools.typeMirrors();
    Cases cases = Tools.cases();
    
    Assertion<TypeMirror> primitive = Assertions.type(TypeKind.INT);
    Assertion<TypeMirror> isClass = Assertions.type(A.class);
    Assertion<TypeMirror> isMirror = Assertions.type(cases.get(0).asType());
    Assertion<TypeMirror> subtypeClass = Assertions.subtype(A.class);
    Assertion<TypeMirror> subtypeMirror = Assertions.subtype(cases.get(0).asType());
    Assertion<TypeMirror> supertypeClass = Assertions.supertype(B.class);
    Assertion<TypeMirror> supertypeMirror = Assertions.supertype(cases.get(1).asType());
    
    @Case
    static class A {}
    
    @Case
    static class B extends A {}
    
    @Case
    static final int C = 0;
    
    @Case
    static final byte D = 0;
    
    
    @Test
    void type() {
        assertEquals(TypeMirror.class, isClass.type());
    }
    
    
    
    
    
    @Test
    void is_class_true() {
        assertTrue(isClass.test(types, cases.get(0).asType()));
    }
    
    @Test
    void is_class_false() {
        assertFalse(isClass.test(types, cases.get(1).asType()));
    }
    
    @Test
    void is_class_condition() {
        assertEquals("A", isClass.condition());
        assertEquals("As", isClass.conditions());
    }
    
    
    @Test
    void is_mirror_true(Cases cases) {
        assertTrue(isMirror.test(types, cases.get(0).asType()));
    }
    
    @Test
    void is_mirror_false() {
        assertFalse(isMirror.test(types, cases.get(1).asType()));
    }
    
    @Test
    void is_mirror_condition() {
        assertEquals("TypeTest.A", isMirror.condition());
        assertEquals("TypeTest.As", isMirror.conditions());
    }
    
    
    @Test
    void subtype_class_true() {
        assertTrue(subtypeClass.test(types, cases.get(1).asType()));
    }
    
    @Test
    void subtype_class_false() {
        assertFalse(subtypeClass.test(types, types.type(String.class)));
    }
    
    @Test
    void subtype_class_condition() {
        assertEquals("subtype of A", subtypeClass.condition());
        assertEquals("subtypes of A", subtypeClass.conditions());
    }
    
    
    @Test
    void subtype_mirror_true() {
        assertTrue(subtypeMirror.test(types, cases.get(1).asType()));
    }
    
    @Test
    void subtype_mirror_false() {
        assertFalse(subtypeMirror.test(types, types.type(String.class)));
    }
    
    @Test
    void subtype_mirror_condition() {
        assertEquals("subtype of TypeTest.A", subtypeMirror.condition());
        assertEquals("subtypes of TypeTest.A", subtypeMirror.conditions());
    }
    
    
    @Test
    void supertype_class_true() {
        assertTrue(supertypeClass.test(types, cases.get(0).asType()));
    }
    
    @Test
    void supertype_class_false() {
        assertFalse(supertypeClass.test(types, types.type(String.class)));
    }
    
    @Test
    void supertype_class_condition() {
        assertEquals("supertype of B", supertypeClass.condition());
        assertEquals("supertypes of B", supertypeClass.conditions());
    }
    
    
    @Test
    void supertype_mirror_true() {
        assertTrue(supertypeMirror.test(types, cases.get(0).asType()));
    }
    
    @Test
    void supertype_mirror_false() {
        assertFalse(supertypeMirror.test(types, types.type(String.class)));
    }
    
    @Test
    void supertype_mirror_condition() {
        assertEquals("supertype of TypeTest.B", supertypeMirror.condition());
        assertEquals("supertypes of TypeTest.B", supertypeMirror.conditions());
    }
    
    
    @Test
    void primitive_test_true() {
        assertTrue(primitive.test(types, cases.get(2).asType()));
    }
    
    @Test
    void primitive_test_false() {
        assertFalse(primitive.test(types, cases.get(3).asType()));
    }
    
    @Test
    void primitive_conditions() {
        assertEquals("int", primitive.condition());
        assertEquals("ints", primitive.conditions());
    }
    
    @Test
    void primitive_type() {
        assertEquals(TypeMirror.class, primitive.type());
    }
    
}
