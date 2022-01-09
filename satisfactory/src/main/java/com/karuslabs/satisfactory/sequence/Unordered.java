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

import org.jgrapht.alg.interfaces.MatchingAlgorithm.Matching;
import org.jgrapht.alg.matching.HopcroftKarpMaximumCardinalityBipartiteMatching;
import org.jgrapht.graph.SimpleGraph;

record Equals<T>(Set<Assertion<T>> assertions) implements Sequence.Unordered<T> {
    @Override
    public Result test(Set<? extends T> values, TypeMirrors types) {
        var matches = Graphs.matches(assertions, values, types);
        var results = new ArrayList<>(matches.getEdges());
                
        var vertex = matches.getGraph().vertexSet();
        
        var unasserted = new ArrayDeque<>(this.assertions);
        unasserted.removeAll(vertex);
        
        var remaining = new ArrayDeque<>(values);
        remaining.removeAll(vertex);
        
        var success = unasserted.isEmpty() && remaining.isEmpty();
        
        while (!unasserted.isEmpty() && !remaining.isEmpty()) {
            results.add(unasserted.pop().test(remaining.pop(), types));
        }
        
        return new Result.Sequence.Unordered.Equal(results, values.size(), assertions.size(), success);
    }
}

class Graphs {
    static <T> Matching<Object, Result> matches(Set<Assertion<T>> assertions, Set<? extends T> values, TypeMirrors types) {
        var graph = new SimpleGraph<Object, Result>(Result.class);
        for (var assertion : assertions) {
            for (var value : values) {
                var result = assertion.test(value, types);
                if (result.success()) {
                    graph.addEdge(assertion, value, result);
                }
            }
        }
        
        // This is deliberately unchecked since it accepts a Set<T> instead of Set<? extends T>.
        // It works because we can guarantee that HopcroftKarpMaximumCardinalityBipartiteMatching does not
        // modify the two sets.
        return new HopcroftKarpMaximumCardinalityBipartiteMatching(graph, assertions, values).getMatching();
    }
}

record Each<T>(Assertion<T> assertion) implements Sequence.Unordered<T> {
    @Override
    public Result test(Set<? extends T> values, TypeMirrors types) {
        var results = new ArrayList<Result>();
        var success = true;
        for (var value : values) {
            var result = assertion.test(value, types);
            results.add(result);
            
            if (!result.success()) {
                success = false;
            }
        }
        
        return new Result.Sequence.Unordered.Each(results, success);
    }
}
