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

import com.karuslabs.utilitary.type.TypeMirrors;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Supplier;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

import static com.karuslabs.satisfactory.zold.Assertions.*;
import static com.karuslabs.utilitary.text.Texts.join;

/**
 * An assertion for variables which delegates testing of individual parts of a 
 * variable to {@code Assertion}s.
 */
public class Variable implements Assertion<VariableElement> {

    private final Assertion<Element> annotations;
    private final Assertion<Set<Modifier>> modifiers;
    private final Assertion<TypeMirror> type;
    private final String condition;
    private final String conditions;
    
    /**
     * Creates a {@code Variable} with the given assertions and conditions.
     * 
     * @param annotations the assertion for annotations
     * @param modifiers the assertion for modifiers
     * @param type the type assertion
     * @param condition the condition for satisfying this assertion
     * @param conditions the conditions for satisfying this assertion
     */
    Variable(Assertion<Element> annotations, Assertion<Set<Modifier>> modifiers, Assertion<TypeMirror> type, String condition, String conditions) {
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.type = type;
        this.condition = condition;
        this.conditions = conditions;
    }
    
    @Override
    public boolean test(TypeMirrors types, VariableElement value) {
        return annotations.test(types, value) && modifiers.test(types, value.getModifiers()) && type.test(types, value.asType());
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
    public Class<VariableElement> part() {
        return VariableElement.class;
    }
    
    /**
     * A builder of {@code Variable}s.
     */
    public static class Builder implements Supplier<Variable> {
        
        private static final Set<Class<?>> SUPPORTED = Set.of(Annotation.class, Modifier.class, TypeMirror.class);
        private final Map<Class<?>, Assertion<?>> assertions = new HashMap<>();
        
        /**
         * Creates a {@code Builder} with the given assertions.
         * 
         * @param parts the assertions
         */
        Builder(Assertion<?>... parts) {
            for (var part : parts) {
                if (!SUPPORTED.contains(part.part())) {
                    throw new IllegalArgumentException("Assertion for " + part.part().getName() + " is not supported");
                }
                
                if (assertions.put(part.part(), part) != null) {
                    throw new IllegalStateException("Already declared an assertion for " + part.part().getName());
                }
            }
        }
        
        /**
         * Returns a {@code Variable} with the default condition message.
         * 
         * @return a {@code Variable} with the default condition message
         */
        @Override
        public Variable get() {
            var annotations = assertions.getOrDefault(Annotation.class, ANY_ANNOTATIONS);
            var modifiers = assertions.getOrDefault(Modifier.class, ANY_MODIFIERS);
            var type = assertions.getOrDefault(TypeMirror.class, ANY_TYPE);
            
            return or("{" + join(annotations.condition(), ", ", join(modifiers.condition(), ", ", type.condition())) + "}");
        }
        
        /**
         * Returns a {@code Variable} with the given condition.
         * 
         * @param condition the condition for satisfying this assertion
         * @return a {@code Variable}
         */
        public Variable or(String condition) {
            return or(condition, condition);
        }
        
        /**
         * Returns a {@code Variable} with the given condition messages.
         * 
         * @param condition the condition for satisfying this assertion
         * @param conditions the conditions for satisfying this assertion
         * @return a {@code Variable}
         */
        public Variable or(String condition, String conditions) {
            return new Variable(
                (Assertion<Element>) assertions.getOrDefault(Annotation.class, ANY_ANNOTATIONS),
                (Assertion<Set<Modifier>>) assertions.getOrDefault(Modifier.class, ANY_MODIFIERS),
                (Assertion<TypeMirror>) assertions.getOrDefault(TypeMirror.class, ANY_TYPE),
                condition,
                conditions
            );
        }

        
    }

}
