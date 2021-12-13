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

import com.karuslabs.satisfactory.Sequence.Unordered;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.*;

import static java.util.Comparator.comparingInt;

record Contents<T>(List<Assertion<T>> assertions) implements Unordered<T> {
    @Override
    public Result.Equality test(Collection<? extends T> values, TypeMirrors types) {
        var matches = new MultiMap<Assertion<T>, T, Result>();
        var unmatched = new ArrayDeque<T>();
        
        for (var value : values) {
            for (var assertion : assertions) {
                var result = assertion.test(value, types);
                if (result.success()) {
                    matches.put(assertion, value, result);
                }
            }
            
            if (matches.keys(value).isEmpty()) {
                unmatched.add(value);
            }
        }
        
        var success = matches.containsAll(assertions, values);
        var results = results(types, matches, unmatched);
        
        return new Result.Equality(values.size(), assertions.size(), results, success);
    }
    
    List<Result> results(TypeMirrors types, MultiMap<Assertion<T>, T, Result> matches, Deque<T> unmatched) {
        var results = new ArrayList<Result>();
        for (var assertion : assertions) {
            var values = matches.values(assertion);
            if (!values.isEmpty()) {
                var least = values.keySet().stream().min(comparingInt(value -> matches.keys(value).size())).get();
                results.add(matches.values(assertion).get(least));
                
                matches.remove(least);

            } else if (!unmatched.isEmpty()) {
                results.add(assertion.test(unmatched.pop(), types));
            }
        }
        
        return results;
    }
}

class MultiMap<K, V, R> {
    private final Map<K, Map<V, R>> map = new HashMap<>();
    private final Map<V, Map<K, R>> inverse = new HashMap<>();
    
    Map<K, R> keys(V value) {
        return map(inverse, value);
    }
    
    Map<V, R> values(K key) {
        return map(map, key);
    }
    
    boolean containsAll(Collection<? extends K> keys, Collection<? extends V> values) {
        return map.keySet().containsAll(keys) && inverse.keySet().containsAll(values);
    }
    
    void put(K key, V value, R result) {
        // What if we did the counting here?
    }
    
    void remove(V value) {
        for (var key : map(inverse, value).keySet()) {
            map(map, key).remove(value);
        }
    }

    private <K, V, R> Map<V, R> map(Map<K, Map<V, R>> map, K key) {
        var values = map.get(key);
        if (values == null) {
            map.put(key, values = new HashMap<>());
        }

        return values;
    }
}
