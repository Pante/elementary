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

import com.karuslabs.satisfactory.Assertion.Result;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;

@FunctionalInterface
public interface Assertion<T, R extends Result> {

    R test(TypeMirrors types, T value);
    
    static abstract class Result {
        
        public final List<String> messages = new ArrayList<>();
        
        public abstract <T, R> R accept(Visitor<T, R> visitor, T value);
        
    }
    
    static class BiResult<R extends Result> extends Result {
        
        public final R left;
        public final R right;
        
        BiResult(R left, R right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T, R> R accept(Visitor<T, R> visitor, T value) {
            return visitor.visitAnd(this, value);
        }
        
    }
    
    static interface Visitor<T, R> {
        
        default @Nullable R visitAnd(BiResult<?> result, T value) {
            
        }
        
        default @Nullable R visit(Result result, T value) {
            return null;
        }
        
    }
    
}

class And<T, R extends Result> implements Assertion<T, R> {

    private final Assertion<T, R> left;
    private final Assertion<T, R> right;
    
    And(Assertion<T, R> left, Assertion<T, R> right) {
        this.left = left;
        this.right = right;
    }
    
    @Override
    public R test(TypeMirrors types, T value) {
        return left.test(types, value) && right.test(types, value);
    }
    
}
