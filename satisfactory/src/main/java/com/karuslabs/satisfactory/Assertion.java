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
public interface Assertion<T, R extends Result<R>> {

    R test(TypeMirrors types, T value);
    
    default Assertion<T, AndResult<R>> and(Assertion<T, R> other) {
        return new And<>(this, other);
    }
    
    default BiPredicate<TypeMirrors, T> predicate() {
        return (types, value) -> test(types, value) == null;
    }
    
    static abstract class Result<Self extends Result> {
        
        public final boolean success;
        
        public Result(boolean success) {
            this.success = success;
        }
        
        public abstract <T, R> R accept(Visitor<T, R> visitor, T value);
        
        public abstract Self empty();
        
    }
    
    static class AndResult<R extends Result<R>> extends Result<AndResult<R>> {

        public final R left;
        public final R right;
        
        AndResult(R left, R right) {
            super(left.success && right.success);
            this.left = left;
            this.right = right;
        }
        
        @Override
        public <T, R> R accept(Visitor<T, R> visitor, T value) {
            return visitor.visit(this, value);
        }

        @Override
        public AndResult<R> empty() {
            return new AndResult<>(left.empty(), right.empty());
        }
        
    }
    
    static class OrResult<R extends Result<R>> extends Result<OrResult<R>> {
        
       public final R left;
       public final R right;
       
        OrResult(R left, R right) {
            super(left.success || right.success);
            this.left = left;
            this.right = right;
        }

        @Override
        public <T, R> R accept(Visitor<T, R> visitor, T value) {
            return visitor.visitOr(this, value);
        }

        @Override
        public OrResult<R> empty() {
            return new OrResult<>(left.empty(), right.empty());
        }
       
    }
    
    static interface Visitor<T, R> { 
        
        default @Nullable <U extends Result<U>> R visitAnd(AndResult<U> result, T value) {
            return visit(result, value);
        }
        
        default @Nullable <U extends Result<U>> R visitOr(OrResult<U> result, T value) {
            return visit(result, value);
        }
        
        default @Nullable R visit(Result result, T value) {
            return null;
        }
        
    }
    
}

class And<T, R extends Result<R>> implements Assertion<T, AndResult<R>> {

    final Assertion<T, R> left;
    final Assertion<T, R> right;
    
    And(Assertion<T, R> left, Assertion<T, R> right) {
        this.left = left;
        this.right = right;
    }
    
    @Override
    public AndResult<R> test(TypeMirrors types, T value) {
        var first = left.test(types, value);
        return new AndResult<>(first, first.success ? right.test(types, value) : first.empty());
    }
    
}

class Or<T, R extends Result<R>> implements Assertion<T, OrResult<R>> {

    final Assertion<T, R> left;
    final Assertion<T, R> right;
    
    Or(Assertion<T, R> left, Assertion<T, R> right) {
        this.left = left;
        this.right = right;
    }
    
    @Override
    public OrResult<R> test(TypeMirrors types, T value) {
        var first = left.test(types, value);
        return new OrResult<>(first, first.success ? first.empty() : right.test(types, value));
    }
    
}