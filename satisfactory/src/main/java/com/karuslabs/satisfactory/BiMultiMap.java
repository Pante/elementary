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

import java.util.*;

class BiMultiMap<K, V> {
    private final Map<K, List<V>> map = new HashMap<>();
    private final Map<V, List<K>> inverse = new HashMap<>();

    boolean contains(Collection<? extends K> keys, Collection<? extends V> values) {
        return map.keySet().containsAll(keys) && inverse.keySet().containsAll(values);
    }
    
    boolean bidirectional(V value) {
        var keys = inverse(value);
        return keys.size() == 1 && values(keys.get(0)).size() == 1;
    }
    
    List<V> values(K key) {
        return list(map, key);
    }
    
    List<K> inverse(V value) {
        return list(inverse, value);
    }
    
    void put(K key, V value) {
        list(map, key).add(value);
        list(inverse, value).add(key);
    }

    private <K, V> List<V> list(Map<K, List<V>> map, K key) {
        var list = map.get(key);
        if (list == null) {
            map.put(key, list = new ArrayList<>());
        }

        return list;
    }
}
