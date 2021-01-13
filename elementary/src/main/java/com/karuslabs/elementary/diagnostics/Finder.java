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
package com.karuslabs.elementary.diagnostics;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import javax.tools.*;
import javax.tools.Diagnostic.Kind;

import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class Finder<Self extends Finder> {

    private final List<Diagnostic<? extends JavaFileObject>> results;
    private final Diagnostics diagnostics;
    
    public Finder(Diagnostics diagnostics) {
        results = new ArrayList<>(diagnostics.all);
        this.diagnostics = diagnostics;
    }
    
    public Self kind(Kind... kinds) {
        var allowed = Set.of(kinds);
        results.removeIf(result -> !allowed.contains(result.getKind()));
        return self();
    }
    
    public Self errors() {
        results.retainAll(diagnostics.errors);
        return self();
    }
    
    public Self warnings() {
        results.retainAll(diagnostics.warnings);
        return self();
    }
    
    public Self notes() {
        results.retainAll(diagnostics.notes);
        return self();
    }
    
    
    public Self where(Predicate<Diagnostic<? extends JavaFileObject>> condition) {
        results.removeIf(Predicate.not(condition));
        return self();
    }
    
    
    public Self matches(String message) {
        results.removeIf(result -> !result.getMessage(Locale.getDefault()).equals(message));
        return self();
    }
    
    
    public Self contains(String substring) {
        results.removeIf(result -> !result.getMessage(Locale.getDefault()).contains(substring));
        return self();
    }
    
    public Self contains(Pattern pattern) {
        results.removeIf(result -> !pattern.matcher(result.getMessage(Locale.getDefault())).matches());
        return self();
    }
    
    
    public @Nullable Diagnostic<? extends JavaFileObject> one() {
        return results.size() == 1 ? results.get(0) : null;
    }
    
    public List<Diagnostic<? extends JavaFileObject>> list() {
        return results;
    }
    
    public Map<Kind, List<Diagnostic<? extends JavaFileObject>>> map() {
        var map = new HashMap<Kind, List<Diagnostic<? extends JavaFileObject>>>();
        for (var result : results) {
            var list = map.get(result.getKind());
            if (list == null) {
                map.put(result.getKind(), list = new ArrayList<>());
            }
            
            list.add(result);
        }
        
        return map;
    }
    
    public int count() {
        return results.size();
    }
    
    
    protected abstract Self self();
    
}
