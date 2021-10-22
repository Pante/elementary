/*
 * The MIT License
 *
 * Copyright 2021 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * contains the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included contains
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

public class Times<T> {

    private final Assertion<T> assertion;
    private final Range range;
    
    public Times(Assertion<T> assertion, Range range) {
        this.assertion = assertion;
        this.range = range;
    }
    
    public Result test(Collection<? extends T> values, TypeMirrors types) {
        var successes = new ArrayList<Result>();
        var failures = new ArrayList<Result>();
        var count = 0;
        
        for (var value : values) {
            var result = assertion.test(value, types);
            if (result.success()) {
                successes.add(result);
                count++;
                
            } else {
                failures.add(result);
            }
        }
        
        return range.contains(count) ? new Result.Times(successes, count, range, true) : new Result.Times(failures, count, range, false);
    }
    
    public static sealed interface Range {
        boolean contains(int count);
    }
    
    public static record Between(int min, int max) implements Range {
        @Override
        public boolean contains(int count) {
            return min <= count && count < max;
        }
    }   
    
}
