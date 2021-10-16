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

import com.karuslabs.satisfactory.assertions.Type.Relation;
import com.karuslabs.satisfactory.logic.Operator;

import java.lang.annotation.Annotation;
import java.util.*;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.*;

import org.checkerframework.checker.nullness.qual.Nullable;

public sealed interface Failure extends Result {
    
    public static record Logical(Operator operator, List<Failure> failures) implements Failure {
        @Override
        public <T, R> @Nullable R accept(Visitor<T, R> visitor, T value) {
            return visitor.visitLogical(this, value);
        }
    }
    
    
    public static record Primitive(TypeKind expected, TypeMirror actual) implements Failure {
        @Override
        public <T, R> R accept(Visitor<T, R> visitor, T value) {
            return visitor.visitPrimitive(this, value);
        }
    }
    
    public static record Type(Relation relation, List<TypeMirror> expected, @Nullable TypeMirror actual) implements Failure {
        @Override
        public <T, R> R accept(Visitor<T, R> visitor, T value) {
            return visitor.visitType(this, value);
        }
    }
    
    
    public static record Annotations(List<Class<? extends Annotation>> expected) implements Failure {
        @Override
        public <T, R> R accept(Visitor<T, R> visitor, T value) {
            return visitor.visitAnnotations(this, value);
        }
    }
    
    public static record Modifiers(List<Modifier> expected, List<Modifier> actual) implements Failure {
        @Override
        public <T, R> R accept(Visitor<T, R> visitor, T value) {
            return visitor.visitModifiers(this, value);
        }
    }
    
    
    
}