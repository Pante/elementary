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
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.*;

record Contents<T>(List<Assertion<T>> assertions) implements Sequence.Unordered<T> {
    @Override
    public Result test(Collection<? extends T> values, TypeMirrors types) {
        var graph = new Graph<T>();
        var connected = new ArrayDeque<Assertion<T>>(assertions.size());
        
        for (var assertion : assertions) {
            for (var value : values) {
                graph.putIfSuccess(assertion, value, assertion.test(value, types));
            }
            
            
        }
        
        var results = new ArrayList<Result>();
        for (var assertion : connected) { // Just like me IRL :(
            var associated = graph.remove(assertion);
            if (associated.isEmpty()) {
                graph.isolated.add(assertion);
                continue;
            }
            
            var first = associated.entrySet().iterator().next();
            results.add(first.getValue());
            
            for (var touched : graph.remove(first.getKey())) {
                graph.get(assertion)
            }
        }
    }
    
    void add(Deque conntected Graph<T> graph, Assertion<T> assertion) {
        var size = graph.get(assertion).size();
            if (size == 1) {
                connected.addFirst(assertion);
                
            } else if (size > 1) {
                connected.addLast(assertion);
            }
    }
}

class Graph<T> {
    
    final Set<Assertion<T>> isolated = new HashSet<>();
    private final Map<Assertion<T>, Map<T, Result>> assertions = new HashMap<>();
    private final Map<T, List<Assertion<T>>> values = new HashMap<>();
    
    Map<T, Result> get(Assertion<T> assertion) {
        return assertions.getOrDefault(assertion, Map.of());
    }
    
    List<Assertion<T>> get(T value) {
        return values.getOrDefault(value, List.of());
    }
    
    void putIfSuccess(Assertion<T> assertion, T value, Result result) {
        var results = assertions.computeIfAbsent(assertion, key -> new HashMap<>());
        var asserts = values.computeIfAbsent(value, key -> new ArrayList<>());
        
        if (result.success()) {
            results.put(value, result);
            asserts.add(assertion);
            isolated.remove(assertion);

        } else if (results.isEmpty()) {
            isolated.add(assertion);
        }
    }
    
    Map<T, Result> remove(Assertion<T> assertion) {
        var associated = assertions.remove(assertion);
        if (associated == null) {
            return Map.of();
        }
        
        isolated.remove(assertion);
        for (var value : associated.keySet()) {
            values.get(value).remove(assertion);
        }
        
        return associated;
    }
    
    List<Assertion<T>> remove(T value) {
        var associated = values.remove(value);
        if (associated == null) {
            return List.of();
        }
        
        for (var assertion : associated) {
            assertions.get(assertion).remove(value);
        }
        
        return associated;
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
//            of(key).putIfSuccess(value);
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
//            results.putIfSuccess(result);
//            
//            if (!result.success()) {
//                success = false;
//            }
//        }
//        
//        return new Result.Each(results, success);
//    }
//}
