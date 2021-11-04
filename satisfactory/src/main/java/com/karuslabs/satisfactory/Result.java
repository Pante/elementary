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

import com.karuslabs.satisfactory.Times.Range;
import com.karuslabs.satisfactory.assertions.Type.Relation;

import java.util.*;
import javax.lang.model.type.*;

public sealed interface Result {

    <T, R> R accept(Visitor<T, R> visitor, T value);
    
    boolean success();
    
    
    static record Type(TypeMirror actual, Relation relation, List<TypeMirror> expected, boolean success) implements Result {
        @Override
        public <T, R> R accept(Visitor<T, R> visitor, T value) {
            return visitor.type(this, value);
        }
    }
    
    static record Primitive(TypeKind actual, TypeKind expected, boolean success) implements Result {
        @Override
        public <T, R> R accept(Visitor<T, R> visitor, T value) {
            return visitor.primitive(this, value);
        }
    }
    
    
    static record Times(List<Result> results, Range range, int count) implements Result {
        @Override
        public <T, R> R accept(Visitor<T, R> visitor, T value) {
            return visitor.times(this, value);
        }
        
        @Override
        public boolean success() {
            return range.contains(count);
        }
    }
    
    
    static record Equality(int actual, int expected, List<Result> results, boolean success) implements Result {
        @Override
        public <T, R> R accept(Visitor<T, R> visitor, T value) {
            return visitor.equality(this, value);
        }
    }
    
    static record Each(List<Result> results, boolean success) implements Result {
        @Override
        public <T, R> R accept(Visitor<T, R> visitor, T value) {
            return visitor.each(this, value);
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
