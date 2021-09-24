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

import com.karuslabs.satisfactory.zold.Flag;
import com.karuslabs.satisfactory.Logical.*;
import com.karuslabs.satisfactory.R.Visitor;
import com.karuslabs.satisfactory.Result.Failure;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;

public class Logical {
    
    static boolean success(R... clauses) {
            for (var clause : clauses) {
               if (!clause.success) {
                   return false;
               }
            }
            
            return true;
        }
    
    static boolean success(R... clauses) {
            for (var clause : clauses) {
                if (clause.success) {
                    return true;
                }
            }
            
            return false;
        }
    
    public static record AndFailure(List<Failure> nested) implements Failure {
        @Override
        public <T, R> R accept(Visitor<T, R> visitor, T value) {
            return visitor.visitAnd(this, value);
        }
    }
    
    public static record OrFailure(List<Failure> nested) implements Failure {
        @Override
        public <T, R> R accept(Visitor<T, R> visitor, T value) {
            return visitor.visitOr(this, value);
        }
    }
    
    public static record NegationFailure(Success success) implements Failure {
        @Override
        public <T, R> @Nullable R accept(Visitor<T, R> visitor, T value) {
            return visitor.visitNegation(this, value);
        }
    }
    
    public static class NegationResult extends R {

        public final R negation;
        
        NegationResult(R negation) {
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
    public AndResult test(T value, TypeMirrors types) {
        var first = left.test(value, types);
        return new AndResult(first, first instanceof ? right.test(value, types) : first.empty());
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
    public OrResult test(T value, TypeMirrors types) {
        var first = left.test(value, types);
        return new OrResult(first, first.success ? first.empty() : right.test(value, types));
    }

}

class Not<T> implements Assertion<T> {

    final Assertion<T> assertion;

    Not(Assertion<T> assertion) {
        this.assertion = assertion;
    }

    @Override
    public NegationResult test(T value, TypeMirrors types) {
        return new NegationResult(assertion.test(value, types));
    }

}
