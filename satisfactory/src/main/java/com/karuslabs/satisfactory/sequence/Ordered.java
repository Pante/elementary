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
package com.karuslabs.satisfactory.sequence;

import com.karuslabs.satisfactory.*;
import com.karuslabs.satisfactory.sequence.Sequence.*;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.*;

record Pattern<T>(Ordered<T>... subsequences) implements Sequence.Ordered<T> {
    @Override
    public Result test(List<? extends T> values, TypeMirrors types) {
        values = subsequences.length <= 1 ? values : new Range(values);
        var results = new ArrayList<Result>();
        var success = true;
        
        for (var subsequence : subsequences) {
            var result = subsequence.test(values, types);
            results.add(result);
            success &= result.success();
        }
        
        return new Result.Sequence.Pattern(results, success);
    }  
}

record Equals<T>(Times times, Assertion<T>... assertions) implements Sequence.Ordered<T> {
    @Override
    public Result test(List<? extends T> values, TypeMirrors types) {
        var cursor = Cursor.of(values);
        var results = new ArrayList<Result>();
        var count = 0;
        
        for (var i = cursor.current(); i < values.size(); i++, count++) {
            var result = assertions[count % assertions.length].test(values.get(i), types);
            if (!result.success()) {
                break;  
            }
            
            results.add(result);
        }

        cursor.move(count);
        return new Result.Sequence.Equality(times, results, count);
    }
}
