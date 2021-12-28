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

import com.karuslabs.satisfactory.Assertion;
import java.util.List;

record Contents<T>(Times times, List<Assertion<T>> assertions) implements Unordered<T> {
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
            
            if (matches.inverse(value).isEmpty()) {
                unmatched.add(value);
            }
        }
        
        var success = assertions.length == values.size() && matches.containsAll(assertions, values);
        var results = results(types, matches, unmatched);
        return new Result.Equality(values.size(), List.of(assertions), results, success);
    }
    
    List<Result> results(TypeMirrors types, MultiMap<Assertion<T>, T, Result> matches, Deque<T> unmatched) {
        var results = new ArrayList<Result>();
        for (var assertion : assertions) {
            var values = matches.of(assertion);
            if (values.isEmpty() && unmatched.isEmpty()) {
                continue;
            }

            results.add(values.stream().min(comparingInt(value -> matches.inverse(value).size()))
                                       .map(least -> matches.pop(assertion, least))
                                       .orElseGet(() -> assertion.test(unmatched.pop(), types)));
        }
        
        return results;
    }
}

//record Contents<T>(List<Assertion<T>> assertions) implements Unordered<T> {
//    
//    
//    static class MultiMap<K, V, R> {
//        private final Map<K, List<V>> map = new HashMap<>();
//        private final Map<V, Map<K, R>> inverse = new HashMap<>();
//
//        List<V> of(K key) {
//            return map.computeIfAbsent(key, (k) -> new ArrayList<>());
//        }
//
//        Map<K, R> inverse(V value) {
//            return inverse.computeIfAbsent(value, (k) -> new HashMap<>());
//        }
//
//        boolean containsAll(Collection<? extends K> keys, Collection<? extends V> values) {
//            return map.keySet().containsAll(keys) && inverse.keySet().containsAll(values);
//        }
//
//        void put(K key, V value, R result) {
//            of(key).add(value);
//            inverse(value).put(key, result);
//        }
//
//        R pop(K key, V value) {
//            var result = inverse.of(value).of(key);
//            for (var values : map.values()) {
//                values.remove(value);
//            }
//            inverse.remove(value);
//            
//            return result;
//        }
//    }
//}   
//
//record Each<T>(Assertion<T> assertion) implements Unordered<T> {
//    @Override
//    public Result test(Collection<? extends T> values, TypeMirrors types) {
//        var results = new ArrayList<Result>();
//        var success = true;
//        for (var value : values) {
//            var result = assertion.test(value, types);
//            results.add(result);
//            
//            if (!result.success()) {
//                success = false;
//            }
//        }
//        
//        return new Result.Each(results, success);
//    }
//}
