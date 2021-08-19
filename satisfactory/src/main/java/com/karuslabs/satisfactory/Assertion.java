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

import com.karuslabs.satisfactory.Assertion.*;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.function.*;

import org.checkerframework.checker.nullness.qual.Nullable;

@FunctionalInterface
public interface Assertion<T, R extends Failure> {

    @Nullable R test(TypeMirrors types, T value);
    
    default Assertion<T, AndFailure<R>> and(Assertion<T, R> other) {
        return new And<>(this, other);
    }
    
    default Assertion<T, OrFailure<R>> or(Assertion<T, R> other) {
        return new Or<>(this, other);
    }
    
    default BiPredicate<TypeMirrors, T> predicate() {
        return (types, value) -> test(types, value) == null;
    }
    
    static interface Failure {
        
        <T, R> R accept(Visitor<T, R> visitor, T value);
        
    }
    
    static class AndFailure<T extends Failure> implements Failure {

        public final T failure;
        
        AndFailure(T failure) {
            this.failure = failure;
        }
        
        @Override
        public <T, R> R accept(Visitor<T, R> visitor, T value) {
            return visitor.visitAnd(this, value);
        }
        
    }
    
    static class OrFailure<T extends Failure> implements Failure {
        
        public final T left;
        public final T right;
        
        OrFailure(T left, T right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T, R> R accept(Visitor<T, R> visitor, T value) {
            return visitor.visitOr(this, value);
        }
        
    }
    
    static interface Visitor<T, R> { 
        
        default @Nullable R visitAnd(AndFailure<?> failure, T value) {
            return visit(failure, value);
        }
        
        default @Nullable R visitOr(OrFailure<?> failure, T value) {
            return visit(failure, value);
        }
        
        default @Nullable R visit(Failure failure, T value) {
            return null;
        }
        
    }
    
}

class And<T, R extends Failure> implements Assertion<T, AndFailure<R>> {

    private final Assertion<T, R> left;
    private final Assertion<T, R> right;
    
    And(Assertion<T, R> left, Assertion<T, R> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public @Nullable AndFailure<R> test(TypeMirrors types, T value) {
        var failure = left.test(types, value);
        if (failure != null) {
            return new AndFailure<>(failure);
        }
        
        failure = right.test(types, value);
        if (failure != null) {
            return new AndFailure<>(failure);
        }
        
        return null;
    }

}

class Or<T, R extends Failure> implements Assertion<T, OrFailure<R>> {
    
    private final Assertion<T, R> left;
    private final Assertion<T, R> right;
    
    Or(Assertion<T, R> left, Assertion<T, R> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public @Nullable OrFailure<R> test(TypeMirrors types, T value) {
        var first = left.test(types, value);
        if (first == null) {
            return null;
        }
        
        var second = right.test(types, value);
        if (second == null) {
            return null;
        }
        
        return new OrFailure<>(first, second);
    }
    
}
