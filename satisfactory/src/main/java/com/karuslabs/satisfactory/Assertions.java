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

import com.karuslabs.satisfactory.logical.*;

import java.util.Set;
import java.util.function.Supplier;
import java.lang.annotation.Annotation;
import javax.lang.model.element.*;
import javax.lang.model.type.*;

import static com.karuslabs.satisfactory.Type.Relation.*;

public class Assertions {

    public static final Assertion<VariableElement> ANY_VARIABLE = new Any<>(VariableElement.class);
    public static final Assertion<Element> ANY_ANNOTATIONS = new Any<>(Annotation.class);
    public static final Assertion<Set<Modifier>> ANY_MODIFIERS = new Any<>(Modifier.class);
    public static final Assertion<TypeMirror> ANY_TYPE = new Any<>(TypeMirror.class);
    
    
    public static Method.Builder method(Part... parts) {
        return new Method.Builder(parts);
    }
    
    public static Variable.Builder variable(Assertion<?>... assertions) {
        return new Variable.Builder(assertions);
    }
    
    
    public static Assertion<Element> annotations(Assertion<Element> assertion) {
        return assertion;
    }
    
    public static Assertion<Element> contains(Class<? extends Annotation>... annotations) {
        return new ContainsAnnotations(annotations);
    }
    
    public static Assertion<Element> contains(No.Annotations annotations) {
        return not(contains(annotations.values));
    }
    
    
    public static Assertion<Set<Modifier>> modifiers(Assertion<Set<Modifier>> assertion) {
        return assertion;
    }
    
    public static Assertion<Set<Modifier>> contains(Modifier... modifiers) {
        return new ContainsModifiers(modifiers);
    }
    
    public static Assertion<Set<Modifier>> contains(No.Modifiers modifiers) {
        return not(new ContainsModifiers(modifiers.values));
    }
    
    public static Assertion<Set<Modifier>> equal(Modifier... modifiers) {
        return new EqualModifiers(modifiers);
    }
    
    
    public static Assertion<TypeMirror> type(TypeKind primitive) {
        return new Primitive(primitive);
    }
    
    public static Assertion<TypeMirror> type(Class<?> type) {
        return new ClassType(IS, type);
    }
    
    public static Assertion<TypeMirror> type(TypeMirror type) {
        return new MirrorType(IS, type);
    }
    
    public static Assertion<TypeMirror> subtype(Class<?>... types) {
        return new ClassType(SUBTYPE, types);
    }
    public static Assertion<TypeMirror> subtype(TypeMirror... types) {
        return new MirrorType(SUBTYPE, types);
    }
    
    public static Assertion<TypeMirror> supertype(Class<?>... types) {
        return new ClassType(SUPERTYPE, types);
    }
    public static Assertion<TypeMirror> supertype(TypeMirror... types) {
        return new MirrorType(SUPERTYPE, types);
    }
    
    
    
    public static <T> Assertion<T> not(Assertion<T> assertion) {
        return new Not<>(assertion);
    }
    
    public static <T> Assertion<T> not(Supplier<? extends Assertion<T>> assertion) {
        return new Not<>(assertion.get());
    }
    
    public static No.Annotations no(Class<? extends Annotation>... annotations) {
        return new No.Annotations(annotations);
    }
    
    public static No.Modifiers no(Modifier... modifiers) {
        return new No.Modifiers(modifiers);
    }
    
}
