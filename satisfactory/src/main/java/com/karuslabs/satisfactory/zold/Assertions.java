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
package com.karuslabs.satisfactory.zold;


import java.util.Set;
import java.util.function.Supplier;
import java.lang.annotation.Annotation;
import javax.lang.model.element.*;
import javax.lang.model.type.*;

import static com.karuslabs.satisfactory.zold.Type.Relation.*;

/**
 * This class provides constants for frequently used assertion and utility methods 
 * for creating {@code Assertion}s.
 */
public class Assertions {
    
    /**
     * An assertion that is satisfied by any {@code VariableElement}.
     */
    public static final Assertion<VariableElement> ANY_VARIABLE = new Any<>(VariableElement.class);
    /**
     * An assertion that is satisfied by any annotation.
     */
    public static final Assertion<Element> ANY_ANNOTATIONS = new Any<>(Annotation.class);
    /**
     * An assertion that is satisfied by any modifier.
     */
    public static final Assertion<Set<Modifier>> ANY_MODIFIERS = new Any<>(Modifier.class);
    /**
     * An assertion that is satisfied by any {@code TypeMirror}.
     */
    public static final Assertion<TypeMirror> ANY_TYPE = new Any<>(TypeMirror.class);
    
    
    /**
     * Creates a {@code Method.Builder} with the given assertions and sequences.
     * <br><br>
     * The supported assertions and sequences are as folllows:<br>
     * <ul>
     * <li>Annotation assertions<li>
     * <li>Modifier assertions<li>
     * <li>Type assertions</li>
     * <li>{@code VariableElement} sequences<li>
     * </ul>
     * 
     * @param parts the assertions and sequences
     * @return a {@code Method.Builder} with the given assertions and sequences
     * @throws IllegalArgumentException if a given assertion or sequence is unsupported
     * @throws IllegalStateException if the given parts contain multiple assertions 
     *                               or sequences for the same part
     */
    public static Method.Builder method(Part... parts) {
        return new Method.Builder(parts);
    }
    
    /**
     * Creates a {@code Variable.Builder} with the given assertions.
     * <br><br>
     * The supported assertions are as follows:<br>
     * <ul>
     * <li>Annotation assertions<li>
     * <li>Modifier assertions<li>
     * <li>Type assertions</li>
     * </ul>
     * 
     * @param assertions the assertions
     * @return a {@code Variable.Builder}
     * @throws IllegalArgumentException if a given assertion is unsupported
     * @throws IllegalStateException if the given assertions contain multiple assertions
                                     for the same part
     */
    public static Variable.Builder variable(Assertion<?>... assertions) {
        return new Variable.Builder(assertions);
    }
    
    /**
     * Functions as a named parameter for an assertion for annotations.
     * 
     * @param assertion an assertion for annotations
     * @return the given assertion
     */
    public static Assertion<Element> annotations(Assertion<Element> assertion) {
        return assertion;
    }
    
    /**
     * Returns an assertion that is satisfied if an {@code Element} contains the
     * given annotations.
     * 
     * @param annotations the annotations that an {@code Element} should contain
     * @return an assertion that is satisfied if an {@code Element} contains the 
     *         given annotations
     */
    public static Assertion<Element> contains(Class<? extends Annotation>... annotations) {
        return new ContainsAnnotations(annotations);
    }
    
    /**
     * Returns an assertion that is satisfied if an {@code Element} does not contain
     * the given annotations.
     * 
     * @param annotations the annotations that an {@code Element} should not contain
     * @return an assertion that is satisfied if an {@code Element} does not contain 
     *         the given annotations
     */
    public static Assertion<Element> contains(No.Annotations annotations) {
        return not(contains(annotations.elements));
    }
    
    
    /**
     * Functions as a named parameter for an assertion for modifiers.
     * 
     * @param assertion the assertion for modifiers
     * @return the given assertion
     */
    public static Assertion<Set<Modifier>> modifiers(Assertion<Set<Modifier>> assertion) {
        return assertion;
    }
    
    /**
     * Returns an assertion that is satisfied if a set of modifiers contains
     * the given modifiers.
     * 
     * @param modifiers the modifiers that a set of modifiers should contain
     * @return an assertion that is satisfied if a set of modifiers contains the
     *         given modifiers
     */
    public static Assertion<Set<Modifier>> contains(Modifier... modifiers) {
        return new ContainsModifiers(modifiers);
    }
    
    /**
     * Returns an assertion that is satisfied if a set of modifiers does not contain
     * the given modifiers.
     * 
     * @param modifiers the modifiers that a set of modifiers should not contain
     * @return an assertion that is satisfied if a set of modifiers does not contain 
     *         the given modifiers
     */
    public static Assertion<Set<Modifier>> contains(No.Modifiers modifiers) {
        return not(new ContainsModifiers(modifiers.elements));
    }
    
    /**
     * Returns an assertion that is satisfied if a set of modifiers is equal to
     * the given modifiers.
     * 
     * @param modifiers the modifiers to which a set of modifiers should be equal
     * @return an assertion that is satisfied if a set of modifiers is equal to
     *         the given modifiers
     */
    public static Assertion<Set<Modifier>> equal(Modifier... modifiers) {
        return new EqualModifiers(modifiers);
    }
    
    /**
     * Returns an assertion that is satisfied if a {@code TypeMirror} is equal to 
     * the given primitive {@code TypeKind}.
     * 
     * @param primitive the primitive {@code TypeKind}
     * @return an assertion that is satisfied if a {@code TypeMirror}'s kind is
     *         equal to the given kind
     */
    public static Assertion<TypeMirror> type(TypeKind primitive) {
        return new Primitive(primitive);
    }
    
    /**
     * Returns an assertion that is satisfied if a {@code TypeMirror} is exactly
     * the same as the given type.
     * 
     * @param type the type
     * @return an assertion that is satisfied if a {@code TypeMirror} is exactly
     *         the same as the given type
     */
    public static Assertion<TypeMirror> type(Class<?> type) {
        return new ClassType(IS, type);
    }
    
    /**
     * Returns an assertion that is satisfied if a {@code TypeMirror} is exactly
     * the same as the given type.
     * 
     * @param type the type
     * @return an assertion that is satisfied if a {@code TypeMirror} is exactly
     *         the same as the given type
     */
    public static Assertion<TypeMirror> type(TypeMirror type) {
        return new MirrorType(IS, type);
    }
    
    /**
     * Returns an assertion that is satisfied if a {@code TypeMirror} is a subtype
     * of the given types.
     * 
     * @param types the types
     * @return an assertion that is satisfied if a {@code TypeMirror} is a subtype
     *         of the given types
     */
    public static Assertion<TypeMirror> subtype(Class<?>... types) {
        return new ClassType(SUBTYPE, types);
    }
    
    /**
     * Returns an assertion that is satisfied if a {@code TypeMirror} is a subtype
     * of the given types.
     * 
     * @param types the types
     * @return an assertion that is satisfied if a {@code TypeMirror} is a subtype
     *         of the given types
     */
    public static Assertion<TypeMirror> subtype(TypeMirror... types) {
        return new MirrorType(SUBTYPE, types);
    }
    
    /**
     * Returns an assertion that is satisfied if a {@code TypeMirror} is a supertype
     * of the given types.
     * 
     * @param types the types
     * @return an assertion that is satisfied if a {@code TypeMirror} is a supertype
     *         of the given types
     */
    public static Assertion<TypeMirror> supertype(Class<?>... types) {
        return new ClassType(SUPERTYPE, types);
    }
    
    /**
     * Returns an assertion that is satisfied if a {@code TypeMirror} is a supertype
     * of the given types.
     * 
     * @param types the types
     * @return an assertion that is satisfied if a {@code TypeMirror} is a supertype
     *         of the given types
     */
    public static Assertion<TypeMirror> supertype(TypeMirror... types) {
        return new MirrorType(SUPERTYPE, types);
    }
    
    
    /**
     * Returns a negation of the given assertion.
     * 
     * @param <T> a type which the given assertion tests
     * @param assertion the assertion to negate
     * @return an assertion that negates the given assertion
     */
    public static <T> Assertion<T> not(Assertion<T> assertion) {
        return new Not<>(assertion);
    }
    
    /**
     * Returns an assertion that is the negation of the supplied assertion.
     * 
     * @param <T> the type of the tested value
     * @param assertion the supplier which provides the assertion to be negated
     * @return an assertion that negates the supplied assertion
     */
    public static <T> Assertion<T> not(Supplier<? extends Assertion<T>> assertion) {
        return new Not<>(assertion.get());
    }
    
    /**
     * Functions as a named parameter for the negation of the given annotations.
     * 
     * @param annotations the annotations to be negated
     * @return the annotations
     */
    public static No.Annotations no(Class<? extends Annotation>... annotations) {
        return new No.Annotations(annotations);
    }
    
    /**
     * Functions as a named parameter for the negation of the given modifiers.
     * 
     * @param modifiers the modifiers to be negated
     * @return the modifiers
     */
    public static No.Modifiers no(Modifier... modifiers) {
        return new No.Modifiers(modifiers);
    }
    
}
