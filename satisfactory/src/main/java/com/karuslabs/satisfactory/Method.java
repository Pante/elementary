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

import com.karuslabs.satisfactory.sequence.Sequence;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Supplier;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

import static com.karuslabs.satisfactory.Assertions.*;
import static com.karuslabs.satisfactory.sequence.Sequences.*;

/**
 * An assertion for methods which delegates testing to {@code Assertion}s and {@code Sequence}s 
 * for individual parts of a method.
 */
public class Method implements Assertion<ExecutableElement> {

    private final Assertion<Element> annotations;
    private final Assertion<Set<Modifier>> modifiers;
    private final Assertion<TypeMirror> type;
    private final Sequence<VariableElement> parameters;
    private final Sequence<TypeMirror> exceptions;
    private final String condition;
    private final String conditions;
    
    /**
     * Creates a {@code Method} with the given arguments.
     * 
     * @param annotations the assertion for annotations
     * @param modifiers the assertion for modifiers
     * @param type the assertion for the return type
     * @param parameters a sequence for the parameters
     * @param exceptions a sequence for thrown exceptions
     * @param condition the condition for satisfying this assertion
     * @param conditions the conditions for satisfying this assertion
     */
    Method(
        Assertion<Element> annotations, Assertion<Set<Modifier>> modifiers, 
        Assertion<TypeMirror> type, Sequence<VariableElement> parameters,
        Sequence<TypeMirror> exceptions, String condition,
        String conditions
    ) {
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.type = type;
        this.parameters = parameters;
        this.exceptions = exceptions;
        this.condition = conditions;
        this.conditions = conditions;
    }
    
    @Override
    public boolean test(TypeMirrors types, ExecutableElement element) {
        return annotations.test(types, element) && modifiers.test(types, element.getModifiers())
            && type.test(types, element.getReturnType()) && parameters.test(types, element.getParameters())
            && exceptions.test(types, element.getThrownTypes());
    }

    @Override
    public String condition() {
        return condition;
    }
    
    @Override
    public String conditions() {
        return conditions;
    }

    @Override
    public Class<ExecutableElement> type() {
        return ExecutableElement.class;
    }
    
    /**
     * A builder for {@code Method}s.
     */
    public static class Builder implements Supplier<Method> {
        
        private static final Set<Class<?>> SUPPORTED_ASSERTIONS = Set.of(Annotation.class, Modifier.class, TypeMirror.class);
        private static final Set<Class<?>> SUPPORTED_SEQUENCES = Set.of(VariableElement.class, TypeMirror.class);
        private final Map<Class<?>, Assertion<?>> assertions = new HashMap<>();
        private final Map<Class<?>, Sequence<?>> sequences = new HashMap<>();
        
        /**
         * Creates a {@code Builder} with the given assertions and sequences.
         * 
         * @param parts the assertions and sequences
         * @throws IllegalArgumentException if a given assertion or sequence is not supported
         * @throws IllegalStateException if the given assertions and sequences contains
         *                               assertions and sequences of the same type
         */
        Builder(Part... parts) {
            for (var part : parts) {
                if (part instanceof Assertion<?>) {
                    assertion((Assertion<?>) part);
                    
                } else if (part instanceof Sequence<?>) {
                    sequence((Sequence<?>) part);
                    
                } else {
                    throw new IllegalArgumentException("Part for " + part.type().getName() + " is not supported");
                }
            }
        }
        
        /**
         * Determines whether the given assertion's type is supported.
         * 
         * @param assertion the assertion
         * @throws IllegalArgumentException if a given assertion is not supported
         * @throws IllegalStateException if a given assertion has already been declared
         */
        final void assertion(Assertion<?> assertion) {
            if (!SUPPORTED_ASSERTIONS.contains(assertion.type())) {
                throw new IllegalArgumentException("Assertion for " + assertion.type().getName() + " is not supported");
            }
                
            if (assertions.put(assertion.type(), assertion) != null) {
                throw new IllegalStateException("Already declared an assertion for " + assertion.type().getName());
            }
        }
        
        /**
         * Determines whether the given sequences 's type is supported.
         * 
         * @param sequence the sequence
         * @throws IllegalArgumentException if a given sequence is not supported
         * @throws IllegalStateException if a given sequence has already been declared
         */
        final void sequence(Sequence<?> sequence) {
            if (!SUPPORTED_SEQUENCES.contains(sequence.type())) {
                throw new IllegalArgumentException("Sequence for " + sequence.type().getName() + " is not supported");
            }
                
            if (sequences.put(sequence.type(), sequence) != null) {
                throw new IllegalStateException("Already declared a sequence for " + sequence.type().getName());
            }
        }

        /**
         * Returns a {@code Method} with the default condition message.
         * 
         * @return a {@code Method} with the default condition message
         */
        @Override
        public Method get() {
            var annotations = assertions.getOrDefault(Annotation.class, ANY_ANNOTATIONS);
            var modifiers = assertions.getOrDefault(Modifier.class, ANY_MODIFIERS);
            var type = assertions.getOrDefault(TypeMirror.class, ANY_TYPE);
            
            var parameters = sequences.getOrDefault(VariableElement.class, ANY_PARAMETERS);
            var exceptions = sequences.getOrDefault(TypeMirror.class, ANY_EXCEPTIONS);
            
            return or(
                "{"
              + "\n  annotations " + annotations.condition()
              + "\n  modifiers " + modifiers.condition()
              + "\n  returns " + type.condition()
              + "\n  parameters " + parameters.condition()
              + "\n  throws " + exceptions.condition()
              + "\n}"
            );
        }
        
        /**
         * Returns a {@code Method} with the given condition.
         * 
         * @param condition the condition for satisfying this assertion
         * @return a {@code Method}
         */
        public Method or(String condition) {
            return or(condition, condition);
        }
        
        /**
         * Returns a {@code Method} with the given condition messages.
         * 
         * @param condition the condition for satisfying this assertion
         * @param conditions the conditions for satisfying this assertion
         * @return a {@code Method}
         */
        public Method or(String condition, String conditions) {
            return new Method(
                (Assertion<Element>) assertions.getOrDefault(Annotation.class, ANY_ANNOTATIONS),
                (Assertion<Set<Modifier>>) assertions.getOrDefault(Modifier.class, ANY_MODIFIERS),
                (Assertion<TypeMirror>) assertions.getOrDefault(TypeMirror.class, ANY_TYPE),
                (Sequence<VariableElement>) sequences.getOrDefault(VariableElement.class, ANY_PARAMETERS),
                (Sequence<TypeMirror>) sequences.getOrDefault(TypeMirror.class, ANY_EXCEPTIONS),
                condition,
                conditions
            );
        }
        
    }

}
