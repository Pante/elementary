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

import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.*;

import static com.karuslabs.satisfactory.Concatenation.concat;

record Not<T>(Assertion<T> negation) implements Assertion<T> {
    
    static <T> Assertion<T> of(Assertion<T> assertion) {
        return assertion instanceof Not not ? not.negation : new Not<>(assertion);
    }
    
    @Override
    public Result test(T value, TypeMirrors types) {
        var result = negation.test(value, types);
        return new Result.Not(result, !result.success());
    }
    
}

record And<T>(Assertion<T>... operands) implements Assertion<T> {
    
    static <T> Assertion<T> of(Assertion<T> left, Assertion<T>... right) {
        var assertions = new ArrayList<Assertion<T>>();
        for (var assertion : concat(left, right)) {
            if (assertion instanceof And) {
                Collections.addAll(assertions, ((And<T>) assertion).operands());
                
            } else {
                assertions.add(assertion);
            }
        }
        
        return new And<>(assertions.toArray(Assertion[]::new));
    }
    
    @Override
    public Result test(T value, TypeMirrors types) {
        var results = new ArrayList<Result>(operands.length);
        var success = true;
        
        for (var operand : operands) {
            var result = operand.test(value, types);
            if (result.success() && !success) {
                continue;
            }
            
            if (!result.success() && success) {
                results.clear();
                success = false;
            }
            
            results.add(result);
        }
        
        return new Result.And(results, success);
    }
}

record Or<T>(Assertion<T>... operands) implements Assertion<T> {
    
    static <T> Assertion<T> of(Assertion<T> left, Assertion<T>... right) {
        var assertions = new ArrayList<Assertion<T>>();
        for (var assertion : concat(left, right)) {
            if (assertion instanceof Or) {
                Collections.addAll(assertions, ((Or<T>) assertion).operands());
                
            } else {
                assertions.add(assertion);
            }
        }
        
        return new Or<>(assertions.toArray(Assertion[]::new));
    }
    
    @Override
    public Result test(T value, TypeMirrors types) {
        var results = new ArrayList<Result>(operands.length);
        var success = false;
        
        for (var operand : operands) {
            var result = operand.test(value, types);
            if (!result.success() && success) {
                continue;
            }
            
            if (result.success() && !success) {
                results.clear();
                success = true;
            }
            
            results.add(result);
        }
        
        return new Result.Or(results, success);
    }
}

class Concatenation {
    static <T> Assertion<T>[] concat(Assertion<T> left, Assertion<T>... right) {
        var assertions = new Assertion[1 + right.length];
        assertions[0] = left;
        
        System.arraycopy(right, 0, assertions, 1, right.length);
        
        return assertions;
    }
}
