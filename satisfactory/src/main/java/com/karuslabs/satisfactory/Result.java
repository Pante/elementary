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

import com.karuslabs.satisfactory.sequence.Times;
import com.karuslabs.satisfactory.ast.Type.Relation;

import java.util.*;
import java.util.Map.Entry;
import javax.lang.model.element.*;
import javax.lang.model.type.*;

/**
 * The results of an {@code Assertion}. Like {@code Assertion}s, {@code Result}s
 * can be composed to describe the results of a complex assertion. 
 * 
 * A {@code Result} may be inspected to determine exactly which part of an assertion 
 * failed. In addition, it may also contain other {@code Result}s. To traverse a
 * tree of nested {@code Result}s, consider implementing and using a {@code Visitor}.
 */
public sealed interface Result {

    /**
     * A {@code Result} that is always {@code true}.
     */
    static final Result TRUE = new Constant(true);
    /**
     * A {@code Result} that is always {@code false}.
     */
    static final Result FALSE = new Constant(false);
    
    /**
     * Applies a visitor to this {@code Result}.
     * 
     * @param <T> the type of the additional parameter to the visitor's methods
     * @param <R> the return type of the visitor's methods 
     * @param visitor the visitor operating on this {@code Result}
     * @param value an additional parameter to the visitor
     * @return a visitor-specified result
     */
    <T, R> R accept(Visitor<T, R> visitor, T value);
    
    /**
     * Whether an assertion is successful.
     * 
     * @return {@code true} if an assertion is successful; otherwise {@code false}
     */
    boolean success();
    
    /**
     * A {@code Result} for assertions on Java's abstract syntax tree (AST).
     */
    static sealed interface AST extends Result {
        
        /**
         * The results of an assertion on a method.
         */
        static record Method(
            ExecutableElement actual, Result annotations,  Result modifiers, Result bounds,
            Result type, Result name, Result parameters, Result exceptions, boolean success
        ) implements AST {
            @Override
            public <T, R> R accept(Visitor<T, R> visitor, T value) {
                return visitor.method(this, value);
            }
        }
        
        /**
         * The results of an assertion on a variable.
         */
        static record Variable(VariableElement actual, Result annotations, Result modifiers, Result type, Result name, boolean success) implements AST {
            @Override
            public <T, R> R accept(Visitor<T, R> visitor, T value) {
                return visitor.variable(this, value);
            }
        }
        
        /**
         * The results of an assertion on a variable.
         */
        static record Annotation(AnnotationMirror annotation, Result type, Result values, boolean success) implements AST {
            @Override
            public <T, R> R accept(Visitor<T, R> visitor, T value) {
                return visitor.annotation(this, value);
            }
        }
        
        /**
         * The results of an assertion on an annotation value.
         */
        static record AnnotationField(Entry<? extends ExecutableElement, ? extends AnnotationValue> actual, String name, Result literal, boolean success) implements AST {
            @Override
            public <T, R> R accept(Visitor<T, R> visitor, T value) {
                return visitor.annotationField(this, value);
            }
        }
        
        /**
         * The results of an assertion on an AST element's modifiers.
         */
        static record Modifiers(Set<? extends Modifier> actual, Set<Modifier> expected, boolean success) implements AST {
            @Override
            public <T, R> R accept(Visitor<T, R> visitor, T value) {
                return visitor.modifiers(this, value);
            }
        }
        
        /**
         * The results of an assertion on an AST element's type.
         */
        static record Type(TypeMirror actual, Relation relation, List<TypeMirror> expected, boolean success) implements AST {
            @Override
            public <T, R> R accept(Visitor<T, R> visitor, T value) {
                return visitor.type(this, value);
            }
        }
    
        /**
         * The results of an assertion on an AST element's modifiers.
         */
        static record Primitive(TypeKind actual, TypeKind expected, boolean success) implements AST {
            @Override
            public <T, R> R accept(Visitor<T, R> visitor, T value) {
                return visitor.primitive(this, value);
            }
        } 
    }
    
    /**
     * A {@code Result} that represents a sequence of {@code Result}s.
     */
    static sealed interface Sequence extends Result {
        /**
         * A {@code Result} that represents an ordered sequence of {@code Result}s.
         */
        static sealed interface Ordered extends Sequence {
            static record Pattern(List<Result> results, boolean success) implements Ordered {
                @Override
                public <T, R> R accept(Visitor<T, R> visitor, T value) {
                    return visitor.pattern(this, value);
                }
            }
        
            static record Equal(Times times, List<Result> results, int count) implements Ordered {
                @Override
                public <T, R> R accept(Visitor<T, R> visitor, T value) {
                    return visitor.equal(this, value);
                }

                @Override
                public boolean success() {
                    return times.contains(count);
                } 
            }
        }
        
        /**
         * A {@code Result} that represents an unordered sequence of {@code Result}s.
         */
        static sealed interface Unordered extends Sequence {      
            static record Contains(List<Result> results, int actual, int expected, boolean success) implements Unordered {
                @Override
                public <T, R> R accept(Visitor<T, R> visitor, T value) {
                    return visitor.contains(this, value);
                }
            }
            
            static record Contents(List<Result> results, int actual, int expected, boolean success) implements Unordered {
                @Override
                public <T, R> R accept(Visitor<T, R> visitor, T value) {
                    return visitor.contents(this, value);
                }
            }
            
            static record Each(List<Result> results, boolean success) implements Unordered {
                @Override
                public <T, R> R accept(Visitor<T, R> visitor, T value) {
                    return visitor.each(this, value);
                }
            }
        }
        
        static record Size(int actual, int expected) implements Sequence {
            @Override
            public <T, R> R accept(Visitor<T, R> visitor, T value) {
                return visitor.size(this, value);
            }

            @Override
            public boolean success() {
                return actual == expected;
            }
        }
    }
    
    static record Constant(boolean success) implements Result {
        @Override
        public <T, R> R accept(Visitor<T, R> visitor, T value) {
            return visitor.constant(this, value);
        }
    }
    
    static record Equal<T>(T actual, T other, boolean success) implements Result {
        @Override
        public <T, R> R accept(Visitor<T, R> visitor, T value) {
            return visitor.equal(this, value);
        }
    }
    
    static record Not(Result negation, boolean success) implements Result {
        @Override
        public <T, R> R accept(Visitor<T, R> visitor, T value) {
            return visitor.result(this, value);
        }
    }
    
    static record And(List<Result> operands, boolean success) implements Result {
        @Override
        public <T, R> R accept(Visitor<T, R> visitor, T value) {
            return visitor.and(this, value);
        }
    }
    
    static record Or(List<Result> operands, boolean success) implements Result {
        @Override
        public <T, R> R accept(Visitor<T, R> visitor, T value) {
            return visitor.or(this, value);
        }
    }
    
}
