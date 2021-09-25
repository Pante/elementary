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

public enum Operator {
    
    NAND(true),
    NOT(true),
    OR(false);
    
    public final boolean negation;
    
    private Operator(boolean negation) {
        this.negation = negation;
    }
    
    public static <T> Assertion<T> and(Assertion<T> assertion, Assertion<T>... assertions) {
        var and = assertion instanceof And match ? match : new And<>(assertion);
        var negations = assertion instanceof Not || assertion instanceof Nor ? 1 : 0;
        
        for (var clause : assertions) {
            if (clause instanceof And nested) {
                and.assertions.addAll(nested.assertions);
                continue;
            }
            
            and.assertions.add(clause);
            if (negations >= 1 && (clause instanceof Not || clause instanceof Nor)) {
                negations++;
            }
        }
        
        if (negations != assertions.length + 1) {
            return and;
        }
        
        // Assertions will be either NOR or NOT 
        var nor = assertion instanceof Nor match ? match : new Nor<>(((Not<T>) assertion).assertion);
        for (var clause : assertions) {
            if (clause instanceof Nor nested) {
                nor.assertions.addAll(nested.assertions);
            } else {
                nor.assertions.add(((Not<T>) clause).assertion);
            }
        }
        
        return nor;
    }

}
