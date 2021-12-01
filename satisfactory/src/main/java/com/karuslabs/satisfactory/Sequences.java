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

import com.karuslabs.satisfactory.Sequence.*;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.*;

import static java.lang.Long.min;

record Equals<T>(Assertion<T>... assertions) implements Ordered<T> {
    @Override
    public Result.Equality test(List<? extends T> values, TypeMirrors types) {
        var success = assertions.length == values.size();
        var results = new ArrayList<Result>();
        
        for (int i = 0; i < min(assertions.length, values.size()); i++) {
            var result = assertions[i].test(values.get(i), types);
            results.add(result);
            if (!result.success()) {
                success = false;
            }
        }
        
        return new Result.Equality(values.size(), assertions.length, results, success);
    }
}

record Contents<T>(List<Assertion<T>> assertions) implements Unordered<T> {
    @Override
    public Result.Equality test(Collection<? extends T> values, TypeMirrors types) {
        var success = assertions.size() == values.size();
        var results = new ArrayList<Result>();
        
        var asserts = new HashMap<Assertion<T>, List<T>>();
        var elements = new HashMap<T, List<Assertion<T>>>();
        
        for (var assertion : assertions) {
            var matches = get(asserts, assertion);
            for (var value : values) {
                if (assertion.test(value, types).success()) {
                    matches.add(value);
                    get(elements, value).add(assertion);
                }
            }
            
            success &= !matches.isEmpty();
        }
        
        
        return new Result.Equality(values.size(), assertions.size(), results, success);
    }
    
    <K, V> List<V> get(Map<K, List<V>> map, K key) {
        var list = map.get(key);
        if (list == null) {
            map.put(key, list = new ArrayList<>());
        }
        
        return list;
    }
}
    

record Each<T>(Assertion<T> assertion) implements Unordered<T> {
    @Override
    public Result test(Collection<? extends T> values, TypeMirrors types) {
        var results = new ArrayList<Result>();
        var success = true;
        for (var value : values) {
            var result = assertion.test(value, types);
            results.add(result);
            
            if (!result.success()) {
                success = false;
            }
        }
        
        return new Result.Each(results, success);
    }
}
