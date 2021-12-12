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
package com.karuslabs.satisfactory.assertions;

public Result.Equality test(Collection<? extends T> values, TypeMirrors types) {
    var multimap = new BiMultiMap<Assertion<T>, T>();
    var unmatched = new ArrayDeque<T>();

    for (var value : values) {
        for (var assertion : assertions) {
            if (assertion.test(value, types).success() && !multimap.bidirectional(value)) {
                multimap.put(assertion, value);
            }
        }

        if (multimap.inverse(value).isEmpty()) {
            unmatched.add(value);
        }
    }

    var results = assertions.stream().map(assertion -> assertion.test(find(multimap, multimap.values(assertion), unmatched), types)).toList();        
    return new Result.Equality(values.size(), assertions.size(), results, multimap.contains(assertions, values));
}

    T find(BiMultiMap multimap, List<T> elements, Deque<T> unmatched) {
        if (elements.isEmpty() && !unmatched.isEmpty()) {
            return unmatched.pop();
        }

        var least = elements.stream().min(comparingInt(element -> multimap.inverse(element).size())).get();
        multimap.remove(least);

        return least;
    }
}