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
 * diagnostics copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.karuslabs.elementary;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import javax.tools.*;
import javax.tools.Diagnostic.Kind;

import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.stream.Collectors.toList;

public class Finder implements Iterable<Diagnostic<? extends JavaFileObject>> {

    private final List<Diagnostic<? extends JavaFileObject>> diagnostics;
    private final Result result;
    
    public Finder(Result result) {
        diagnostics = new ArrayList<>(result.diagnostics);
        this.result = result;
    }
    
    @Override
    public Iterator<Diagnostic<? extends JavaFileObject>> iterator() {
        return diagnostics.iterator();
    }
    
    
    public Finder kind(Kind... kinds) {
        return kind(Set.of(kinds));
    }
    
    public Finder kind(Collection<Kind> kinds) {
        diagnostics.removeIf(diagnostic -> !kinds.contains(diagnostic.getKind()));
        return this;
    }
    
    public Finder errors() {
        diagnostics.retainAll(result.errors);
        return this;
    }
    
    public Finder warnings() {
        diagnostics.retainAll(result.warnings);
        return this;
    }
    
    public Finder notes() {
        diagnostics.retainAll(result.notes);
        return this;
    }
    
    
    public Finder in(JavaFileObject file) {
        var path = file.toUri().getPath();
        diagnostics.removeIf(diagnostic -> !diagnostic.getSource().toUri().getPath().equals(path));
        return this;
    }
    
    public Finder on(long line) {
        diagnostics.removeIf(diagnostic -> diagnostic.getLineNumber() != line);
        return this;
    }
    
    public Finder at(long column) {
        diagnostics.removeIf(diagnostic -> diagnostic.getColumnNumber() != column);
        return this;
    }
    
    
    public Finder where(Predicate<Diagnostic<? extends JavaFileObject>> condition) {
        diagnostics.removeIf(Predicate.not(condition));
        return this;
    }
    
    
    public Finder matches(String message) {
        diagnostics.removeIf(diagnostic -> !diagnostic.getMessage(Locale.getDefault()).equals(message));
        return this;
    }
    
    public Finder contains(String substring) {
        diagnostics.removeIf(diagnostic -> !diagnostic.getMessage(Locale.getDefault()).contains(substring));
        return this;
    }
    
    public Finder contains(Pattern pattern) {
        diagnostics.removeIf(diagnostic -> !pattern.matcher(diagnostic.getMessage(Locale.getDefault())).matches());
        return this;
    }
    
    
    public List<String> full() {
        return diagnostics.stream().map(Diagnostic::toString).collect(toList());
    }
    
    public List<String> messages() {
        return diagnostics.stream().map(diagnostic -> diagnostic.getMessage(Locale.getDefault())).collect(toList());
    }
    
    public List<Long> lines() {
        return diagnostics.stream().map(Diagnostic::getLineNumber).collect(toList());
    }
    
    public List<Long> columns() {
        return diagnostics.stream().map(Diagnostic::getColumnNumber).collect(toList());
    }
    
    public List<Long> positions() {
        return diagnostics.stream().map(Diagnostic::getPosition).collect(toList());
    }
    
    public List<String> codes() {
        return diagnostics.stream().map(Diagnostic::getCode).collect(toList());
    }
    
    
    public @Nullable Diagnostic<? extends JavaFileObject> one() {
        return diagnostics.size() == 1 ? diagnostics.get(0) : null;
    }
    
    public List<Diagnostic<? extends JavaFileObject>> list() {
        return diagnostics;
    }
    
    public Map<Kind, List<Diagnostic<? extends JavaFileObject>>> map() {
        var map = new HashMap<Kind, List<Diagnostic<? extends JavaFileObject>>>();
        for (var diagnostic : diagnostics) {
            var list = map.get(diagnostic.getKind());
            if (list == null) {
                map.put(diagnostic.getKind(), list = new ArrayList<>());
            }
            
            list.add(diagnostic);
        }
        
        return map;
    }
    
    
    public int count() {
        return diagnostics.size();
    }
    
}
