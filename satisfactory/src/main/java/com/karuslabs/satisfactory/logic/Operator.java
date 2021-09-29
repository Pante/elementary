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
package com.karuslabs.satisfactory.logic;

import com.karuslabs.satisfactory.Assertion;

import java.util.*;

public enum Operator {
    
    NOT(true),
    AND(false),
    NAND(true),
    OR(false),
    NOR(true);
    
    public final boolean negation;
    
    private Operator(boolean negation) {
        this.negation = negation;
    }
    
    public static <T> Assertion<T> not(Assertion<T> assertion) {
        // TODO: Replace with switch pattern matching
        if (assertion instanceof Not not) {
            return not.assertion();
            
        } else if (assertion instanceof And and) {
            return new Nand(and.assertions());
            
        } else if (assertion instanceof Or or) {
            return new Nor(List.of(or.assertions()));
            
        } else {
            return new Not<>(assertion);
        }
    }
    
    public static <T> Assertion<T> and(Assertion<T> left, Assertion<T>... right) {
        var assertions = new ArrayList<Assertion<T>>();
        for (var assertion : concat(left, right)) {
            if (assertion instanceof And) {
                Collections.addAll(assertions, ((And<T>) assertion).assertions());
                
            } else {
                assertions.add(assertion);
            }
        }
        
        return new And<>(assertions.toArray(Assertion[]::new));
    }
    
    public static <T> Assertion<T> or(Assertion<T> left, Assertion<T>... right) {
        var assertions = new ArrayList<Assertion<T>>();
        for (var assertion : concat(left, right)) {
            if (assertion instanceof Or) {
                Collections.addAll(assertions, ((Or<T>) assertion).assertions());
                
            } else {
                assertions.add(assertion);
            }
        }
        
        return new Or<>(assertions.toArray(Assertion[]::new));
    }
    
    static <T> Assertion<T>[] concat(Assertion<T> left, Assertion<T>... right) {
        var assertions = new Assertion[1 + right.length];
        assertions[0] = left;
        
        System.arraycopy(right, 0, assertions, 1, right.length);
        
        return assertions;
    }

}
