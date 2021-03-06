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

import java.util.*;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.stream.Collectors.toList;

/**
 * A {@code Types} implementation that contains methods to create {@code TypeMirror}s
 * from {@code Class}es. All overridden methods delegate execution to an underlying
 * {@code Types}.
 */
public class TypeMirrors implements Types {   
    
    /**
     * Tests if the given {@code TypeMirror} and {@code Class} both represent
     * the same type
     * 
     * @param type the {@code TypeMirror}
     * @param expected the {@code Class}
     * @return {@code true} if both the given {@code TypeMirror} and {@code Class}
     *         represent the same type
     */
    public static boolean is(TypeMirror type, Class<?> expected) {
        if (type instanceof PrimitiveType) {
            return type.getKind() == kind(expected);
        }
        
        if (!(type instanceof DeclaredType)) {
            return false;
        }
        
        var element = ((DeclaredType) type).asElement();
        return ((TypeElement) element).getQualifiedName().contentEquals(expected.getName());
    }
    
    /**
     * Returns the {@code TypeKind} of the given type.
     * 
     * @param type the type
     * @return the {@code TypeKind}
     */
    public static TypeKind kind(Class<?> type) {
        switch (type.getName()) {
            case "boolean":
                return TypeKind.BOOLEAN;
            case "byte":
                return TypeKind.BYTE;
            case "short":
                return TypeKind.SHORT;
            case "int":
                return TypeKind.INT;
            case "long":
                return TypeKind.LONG;
            case "float":
                return TypeKind.FLOAT;
            case "double":
                return TypeKind.DOUBLE;
            case "char":
                return TypeKind.CHAR;
            case "void":
                return TypeKind.VOID;
            default:
                return TypeKind.DECLARED;
        }
    }
    
    
    private final Elements elements;
    private final Types types;
    
    /**
     * Creates a {@code TypeMirrors} with the given arguments.
     * 
     * @param elements the {@code Elements}
     * @param types the {@code Types}
     */
    public TypeMirrors(Elements elements, Types types) {
        this.elements = elements;
        this.types = types;
    }
    
    /**
     * Returns the first annotation on the given asTypeElement which matches the given type.
     * 
     * @param element the asTypeElement
     * @param type the annotation type
     * @return the annotation, or {@code null} if no annotation matches the given type
     */
    public @Nullable AnnotationMirror annotation(Element element, DeclaredType type) {
        for (var annotation : element.getAnnotationMirrors()) {
            if (isSameType(annotation.getAnnotationType(), type)) {
                return annotation;
            }
        }
        
        return null;
    }
    
    /**
     * Returns all annotations on the given asTypeElement that match the given type.
     * 
     * @param element the asTypeElement
     * @param type the annotation type
     * @return the annotations that match the given type
     */
    public List<AnnotationMirror> annotations(Element element, DeclaredType type) {
        return element.getAnnotationMirrors().stream()
                      .filter(annotation -> isSameType(annotation.getAnnotationType(), type))
                      .collect(toList());
    }
    
    /**
     * Returns a {@code TypeElement} that represents the given type.
     * 
     * @param type the type
     * @return a {@code TypeElement} that represents the given type, or {@code null}
     *         if the given type is not represented by a {@code TypeElement}
     */
    public @Nullable TypeElement asTypeElement(TypeMirror type) {
        var element = types.asElement(type);
        return element instanceof TypeElement ? (TypeElement) element : null;
    }
    
    /**
     * Returns the boxed type of a given primitive type, or the given type if it is
     * not a primitive type.
     * 
     * @param type the primitive type
     * @return the boxed type of the given primitive, or the given type if it is
     *         not a primitive
     */
    public TypeMirror box(TypeMirror type) {
        if (type instanceof PrimitiveType) {
            return types.boxedClass((PrimitiveType) type).asType();
            
        } else {
            return type;
        }
    }
    
    
    /**
     * Returns a {@code TypeMirror} that represents the given {@code Class}.
     * 
     * @param type the type
     * @return a {@code TypeMirror} representation of the given type
     */
    public TypeMirror type(Class<?> type) {
        if (type.isPrimitive()) {
            return types.getPrimitiveType(kind(type));
            
        } else {
            return elements.getTypeElement(type.getCanonicalName()).asType();
        }
    }
    
    /**
     * Returns the erasure of a type.
     *
     * @param type  the type to be erasured
     * @return the erasure of the given type
     * @throws IllegalArgumentException if given a type for a package or module
     */
    public TypeMirror erasure(Class<?> type) {
        return types.erasure(elements.getTypeElement(type.getName()).asType());
    }
    
    /**
     * Creates a {@code TypeMirror} that represents the a specialized generic type.
     * 
     * @param type a generic type 
     * @param parameters the type parameters
     * @return a {@code TypeMirror} that represents a specialized generic type
     */
    public TypeMirror specialize(Class<?> type, Class<?>... parameters) {
        var mirrors = new TypeMirror[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            mirrors[i] = type(parameters[i]);
        }
        
        return specialize(type, mirrors);
    }
    
    /**
     * Creates a {@code TypeMirror} that represents the a specialized generic type.
     * 
     * @param type a generic type 
     * @param parameters the type parameters
     * @return a {@code TypeMirror} that represents a specialized generic type
     */
    public TypeMirror specialize(Class<?> type, TypeMirror... parameters) {
        return types.getDeclaredType(elements.getTypeElement(type.getName()), parameters);
    }

    
    @Override
    public Element asElement(TypeMirror t) {
        return types.asElement(t);
    }

    @Override
    public boolean isSameType(TypeMirror t1, TypeMirror t2) {
        return types.isSameType(t1, t2);
    }

    @Override
    public boolean isSubtype(TypeMirror t1, TypeMirror t2) {
        return types.isSubtype(t1, t2);
    }

    @Override
    public boolean isAssignable(TypeMirror t1, TypeMirror t2) {
        return types.isAssignable(t1, t2);
    }

    @Override
    public boolean contains(TypeMirror t1, TypeMirror t2) {
        return types.contains(t1, t2);
    }

    @Override
    public boolean isSubsignature(ExecutableType m1, ExecutableType m2) {
        return types.isSubsignature(m1, m2);
    }

    @Override
    public List<? extends TypeMirror> directSupertypes(TypeMirror t) {
        return types.directSupertypes(t);
    }

    @Override
    public TypeMirror erasure(TypeMirror t) {
        return types.erasure(t);
    }

    @Override
    public TypeElement boxedClass(PrimitiveType p) {
        return types.boxedClass(p);
    }

    @Override
    public PrimitiveType unboxedType(TypeMirror t) {
        return types.unboxedType(t);
    }

    @Override
    public TypeMirror capture(TypeMirror t) {
        return types.capture(t);
    }

    @Override
    public PrimitiveType getPrimitiveType(TypeKind kind) {
        return types.getPrimitiveType(kind);
    }

    @Override
    public NullType getNullType() {
        return types.getNullType();
    }

    @Override
    public NoType getNoType(TypeKind kind) {
        return types.getNoType(kind);
    }

    @Override
    public ArrayType getArrayType(TypeMirror componentType) {
        return types.getArrayType(componentType);
    }

    @Override
    public WildcardType getWildcardType(TypeMirror extendsBound, TypeMirror superBound) {
        return types.getWildcardType(extendsBound, superBound);
    }

    @Override
    public DeclaredType getDeclaredType(TypeElement typeElem, TypeMirror... typeArgs) {
        return types.getDeclaredType(typeElem, typeArgs);
    }

    @Override
    public DeclaredType getDeclaredType(DeclaredType containing, TypeElement typeElem, TypeMirror... typeArgs) {
        return types.getDeclaredType(containing, typeElem, typeArgs);
    }

    @Override
    public TypeMirror asMemberOf(DeclaredType containing, Element element) {
        return types.asMemberOf(containing, element);
    }
    
}
