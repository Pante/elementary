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

public sealed interface Result {

    <T, R> R accept(Visitor<T, R> visitor, T value);
    
    boolean success();
    
    static sealed interface AST extends Result {
        static record Annotation(AnnotationMirror annotation, Result type, Result values, boolean success) implements AST {
            @Override
            public <T, R> R accept(Visitor<T, R> visitor, T value) {
                return visitor.annotation(this, value);
            }
        }
        
        static record AnnotationField(Entry<? extends ExecutableElement, ? extends AnnotationValue> actual, String name, Result literal, boolean success) implements AST {
            @Override
            public <T, R> R accept(Visitor<T, R> visitor, T value) {
                return visitor.annotationField(this, value);
            }
        }
        
        static record Modifiers(Set<? extends Modifier> actual, Set<Modifier> expected, boolean success) implements AST {
            @Override
            public <T, R> R accept(Visitor<T, R> visitor, T value) {
                return visitor.modifiers(this, value);
            }
        }
        
        static record Type(TypeMirror actual, Relation relation, List<TypeMirror> expected, boolean success) implements AST {
            @Override
            public <T, R> R accept(Visitor<T, R> visitor, T value) {
                return visitor.type(this, value);
            }
        }
    
        static record Primitive(TypeKind actual, TypeKind expected, boolean success) implements AST {
            @Override
            public <T, R> R accept(Visitor<T, R> visitor, T value) {
                return visitor.primitive(this, value);
            }
        } 
    }
    
    static sealed interface Sequence extends Result {
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
