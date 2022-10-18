/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
package com.karuslabs.utilitary.type;

import com.karuslabs.elementary.junit.*;
import com.karuslabs.elementary.junit.annotations.*;

import java.util.*;
import java.util.stream.Stream;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;

@ExtendWith(ToolsExtension.class)
@Introspect
class TypeMirrorsTest {
    
    TypeMirrors types = new TypeMirrors(Tools.elements(), Tools.types());
    Cases cases = Tools.cases();
    
    @Case("primitive") int primitive;
    @Case("string_variable") String string_variable;
    @Case("generic_executable") <T extends String> T generic_executable() { return null; }
    
    
    @Test
    void is_primitive() {
        var type = mock(PrimitiveType.class);
        assertTrue(TypeMirrors.is(cases.one("primitive").asType(), int.class));
    }
    
    @Test
    void is_not_primitive() {
        var type = mock(PrimitiveType.class);
        assertFalse(TypeMirrors.is(cases.one("primitive").asType(), String.class));
    }
    
    @Test
    void is_declared_type() {
        assertTrue(TypeMirrors.is(cases.one("string_variable").asType(), String.class));
    }
    
    @Test
    void is_not_declared_type() {
        assertFalse(TypeMirrors.is(((ExecutableElement) cases.one("generic_executable")).getReturnType(), String.class));
    }
    
    
    @ParameterizedTest
    @MethodSource("kind_parameters")
    void kind(TypeKind expected, Class<?> type) {
        assertEquals(expected, TypeMirrors.kind(type));
    }
    
    static Stream<Arguments> kind_parameters() {
        return Stream.of(
            of(TypeKind.BOOLEAN, boolean.class),
            of(TypeKind.BYTE, byte.class),
            of(TypeKind.SHORT, short.class),
            of(TypeKind.INT, int.class),
            of(TypeKind.LONG, long.class),
            of(TypeKind.FLOAT, float.class),
            of(TypeKind.DOUBLE, double.class),
            of(TypeKind.CHAR, char.class),
            of(TypeKind.VOID, void.class),
            of(TypeKind.DECLARED, Object.class)
        );
    }
    
    
    @Test
    void annotation() {
        var element = cases.one("string_variable");
        var annotation = element.getAnnotationMirrors().get(0);
        assertEquals(annotation, types.annotation(element, annotation.getAnnotationType()));
    }
    
    @Test
    void annotation_null() {
        var element = cases.one("string_variable");
        assertNull(types.annotation(element, (DeclaredType) element.asType()));
    }
    
    @Test
    void annotations() {
        var element = cases.one("string_variable");
        var annotation = element.getAnnotationMirrors().get(0);
        assertEquals(List.of(annotation), types.annotations(element, annotation.getAnnotationType()));
    }
    
    @Test
    void annotations_empty() {
        var element = cases.one("string_variable");
        assertEquals(List.of(), types.annotations(element, (DeclaredType) element.asType()));
    }
    
    
    @Test
    void element_type() {
        var element = Tools.typeMirrors().asElement(types.type(String.class));
        assertEquals(element, types.asTypeElement(cases.one("string_variable").asType()));
    }
    
    @Test
    void element_primitive() {
        assertNull(types.asTypeElement(cases.one("primitive").asType()));
    }
    
    @Test
    void box_primitive() {
        assertEquals(types.type(Integer.class), types.box(types.type(int.class)));
    }
    
    @Test
    void box_non_primitive() {
        var type = types.type(String.class);
        assertSame(type, types.box(type));
    }
    
    @Test
    void type() {
        assertNotNull(types.type(String.class));
    }
    
    @Test
    void type_primitive() {
        assertEquals(TypeKind.INT, types.type(int.class).getKind());
    }
    
    @Test
    void erasure_class() {
        var list = types.erasure(List.class);
        assertNotEquals(types.type(List.class), list);
        assertEquals("java.util.List", list.toString());
    }
    
    @Test
    void specialize_classes() {
        var list = types.specialize(List.class, UUID.class);
        assertNotEquals(types.type(List.class), list);
        assertEquals("java.util.List<java.util.UUID>", list.toString());
    }
    
}

class DelegateTypeMirrorsTest {

    Elements elements = mock(Elements.class);
    Types delegate = mock(Types.class);
    TypeMirrors types = new TypeMirrors(elements, delegate);
    
    TypeElement element = mock(TypeElement.class);
    DeclaredType type = mock(DeclaredType.class);
    DeclaredType other = mock(DeclaredType.class);
    
    @Test
    void asElement() {
        types.asElement(type);
        verify(delegate).asElement(type);
    }
    
    @Test
    void isSameType() {
        types.isSameType(type, other);
        verify(delegate).isSameType(type, other);
    }
    
    @Test
    void isSubtype() {
        types.isSubtype(type, other);
        verify(delegate).isSubtype(type, other);
    }
    
    @Test
    void isAssignable() {
        types.isAssignable(type, other);
        verify(delegate).isAssignable(type, other);
    }
    
    @Test
    void contains() {
        types.contains(type, other);
        verify(delegate).contains(type, other);
    }
    
    @Test
    void isSubsignature() {
        var first = mock(ExecutableType.class);
        var second = mock(ExecutableType.class);
        types.isSubsignature(first, second);
        verify(delegate).isSubsignature(first, second);
    }
    
    @Test
    void directSupertypes() {
        types.directSupertypes(type);
        verify(delegate).directSupertypes(type);
    }
    
    @Test
    void erasure_typemirror() {
        types.erasure(type);
        verify(delegate).erasure(type);
    }
    
    @Test
    void boxedClass() {
        var type = mock(PrimitiveType.class);
        types.boxedClass(type);
        verify(delegate).boxedClass(type);
    }
    
    @Test
    void unboxedType() {
        types.unboxedType(type);
        verify(delegate).unboxedType(type);
    }
    
    @Test
    void capture() {
        types.capture(type);
        verify(delegate).capture(type);
    }
    
    @Test
    void getPrimitiveType() {
        types.getPrimitiveType(TypeKind.INT);
        verify(delegate).getPrimitiveType(TypeKind.INT);
    }
    
    @Test
    void getNullType() {
        types.getNullType();
        verify(delegate).getNullType();
    }
    
    @Test
    void getNoType() {
        types.getNoType(TypeKind.INT);
        verify(delegate).getNoType(TypeKind.INT);
    }
    
    @Test
    void getArrayType() {
        types.getArrayType(type);
        verify(delegate).getArrayType(type);
    }
    
    @Test
    void getWildcardType() {
        types.getWildcardType(type, other);
        verify(delegate).getWildcardType(type, other);
    }
    
    @Test
    void getDeclaredType() {
        types.getDeclaredType(element, type, other);
        verify(delegate).getDeclaredType(element, type, other);
    }
    
    @Test
    void getDeclaredType_nested() {
        var declared = mock(DeclaredType.class);
        types.getDeclaredType(declared, element, type, other);
        verify(delegate).getDeclaredType(declared, element, type, other);
    }
    
    @Test
    void asMemberOf() {
        var declared = mock(DeclaredType.class);
        types.asMemberOf(declared, element);
        verify(delegate).asMemberOf(declared, element);
    }

}
