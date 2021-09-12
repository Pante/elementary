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

import java.util.*;
import java.util.function.*;

import org.checkerframework.checker.nullness.qual.Nullable;

@FunctionalInterface
public interface Assertion<T> {
    
    static <T> Assertion<T> not(Assertion<T> assertion) {
        if (assertion instanceof Not<?>) {
            return ((Not<T>) assertion).assertion;
            
        } else {
            return new Not<>(assertion);
        }
    }

    Result test(TypeMirrors types, T value);
    
    default Assertion<T> and(Assertion<T> other) {
        return new And<>(this, other);
    }
    
    default Assertion<T> or(Assertion<T> other) {
        return new Or<>(this, other);
    }
    
    default BiPredicate<TypeMirrors, T> predicate() {
        return (types, value) -> test(types, value) == null;
    }
    
    static class AndResult extends Result {
        
        static boolean success(Result... clauses) {
            for (var clause : clauses) {
               if (!clause.success) {
                   return false;
               }
            }
            
            return true;
        }

        public final List<Result> clauses;
        
        AndResult(Result... clauses) {
            super(success(clauses));
            this.clauses = List.of(clauses);
        }
        
        @Override
        public <T, R> @Nullable R accept(Visitor<T, R> visitor, Set<Flag> flags, T value) {
            return visitor.visit(this, flags, value);
        }

        @Override
        public AndResult empty() {
            return new AndResult();
        }
        
    }
    
    static class OrResult extends Result {
        
        static boolean success(Result... clauses) {
            for (var clause : clauses) {
                if (clause.success) {
                    return true;
                }
            }
            
            return false;
        }
        
        public final List<Result> clauses;
       
        OrResult(Result... clauses) {
            super(success(clauses));
            this.clauses = List.of(clauses);
        }

        @Override
        public <T, R> @Nullable R accept(Visitor<T, R> visitor, Set<Flag> flags, T value) {
            return visitor.visitOr(this, flags, value);
        }

        @Override
        public OrResult empty() {
            return new OrResult();
        }
       
    }
    
    static class NegationResult extends Result {

        public final Result negation;
        
        NegationResult(Result negation) {
            super(!negation.success);
            this.negation = negation;
        }
        
        @Override
        public <T, R> @Nullable R accept(Visitor<T, R> visitor, Set<Flag> flags, T value) {
            return visitor.visitNegation(this, flags, value);
        }

        @Override
        public NegationResult empty() {
            return new NegationResult(negation.empty());
        }
        
    }
    
}

class And<T> implements Assertion<T> {

    final Assertion<T> left;
    final Assertion<T> right;
    
    And(Assertion<T> left, Assertion<T> right) {
        this.left = left;
        this.right = right;
    }
    
    @Override
    public AndResult test(TypeMirrors types, T value) {
        var first = left.test(types, value);
        return new AndResult(first, first.success ? right.test(types, value) : first.empty());
    }
    
}

class Or<T> implements Assertion<T> {

    final Assertion<T> left;
    final Assertion<T> right;
    
    Or(Assertion<T> left, Assertion<T> right) {
        this.left = left;
        this.right = right;
    }
    
    @Override
    public OrResult test(TypeMirrors types, T value) {
        var first = left.test(types, value);
        return new OrResult(first, first.success ? first.empty() : right.test(types, value));
    }
    
}

class Not<T> implements Assertion<T> {

    final Assertion<T> assertion;
    
    Not(Assertion<T> assertion) {
        this.assertion = assertion;
    }
    
    @Override
    public Result test(TypeMirrors types, T value) {
        return new NegationResult(assertion.test(types, value));
    }
    
}