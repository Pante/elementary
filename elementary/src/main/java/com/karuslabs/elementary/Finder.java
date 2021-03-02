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

/**
 * Contains methods to filter and map the results of a compilation.
 */
public class Finder implements Iterable<Diagnostic<? extends JavaFileObject>> {

    private final List<Diagnostic<? extends JavaFileObject>> diagnostics;
    private final Results results;
    
    /**
     * Creates a {@code Finder} for the given results.
     * 
     * @param results the results of a compilation
     */
    public Finder(Results results) {
        diagnostics = new ArrayList<>(results.diagnostics);
        this.results = results;
    }
    
    @Override
    public Iterator<Diagnostic<? extends JavaFileObject>> iterator() {
        return diagnostics.iterator();
    }
    
    
    /**
     * Returns a {@code Finder} where each diagnostic message's kind matches the
     * given kinds.
     * 
     * @param kinds the kinds which all diagnostic messages should match
     * @return {@code this}
     */
    public Finder kind(Kind... kinds) {
        return kind(Set.of(kinds));
    }
    
    /**
     * Returns a {@code Finder} where each diagnostic message's kind matches the
     * given kinds.
     * 
     * @param kinds the kinds which all diagnostic messages should match
     * @return {@code this}
     */
    public Finder kind(Collection<Kind> kinds) {
        diagnostics.removeIf(diagnostic -> !kinds.contains(diagnostic.getKind()));
        return this;
    }
    
    /**
     * Returns a {@code Finder} with only errors.
     * 
     * @return {@code this}
     */
    public Finder errors() {
        diagnostics.retainAll(results.errors);
        return this;
    }
    
    /**
     * Returns a {@code Finder} with only warnings.
     * 
     * @return {@code this}
     */
    public Finder warnings() {
        diagnostics.retainAll(results.warnings);
        return this;
    }
    
    /**
     * Returns a {@code Finder} with only notes.
     * 
     * @return {@code this}
     */
    public Finder notes() {
        diagnostics.retainAll(results.notes);
        return this;
    }
    
    
    /**
     * Returns a {@code Finder} with only diagnostic messages in the given Java source file.
     * 
     * @param file the Java source file
     * @return {@code this}
     */
    public Finder in(JavaFileObject file) {
        var path = file.toUri().getPath();
        diagnostics.removeIf(diagnostic -> !diagnostic.getSource().toUri().getPath().equals(path));
        return this;
    }
    
    /**
     * Returns a {@code Finder} with only diagnostic messages that appear on the given line.
     * 
     * @param line the line
     * @return {@code this}
     */
    public Finder on(long line) {
        diagnostics.removeIf(diagnostic -> diagnostic.getLineNumber() != line);
        return this;
    }
    
    /**
     * Returns a {@code Finder} with only diagnostic messages that appear at the given column.
     * 
     * @param column the column
     * @return {@code this}
     */
    public Finder at(long column) {
        diagnostics.removeIf(diagnostic -> diagnostic.getColumnNumber() != column);
        return this;
    }
    
    /**
     * Returns a {@code Finder} with diagnostic messages that satisfy the given predicate.
     * 
     * @param condition the condition
     * @return {@code this}
     */
    public Finder where(Predicate<Diagnostic<? extends JavaFileObject>> condition) {
        diagnostics.removeIf(Predicate.not(condition));
        return this;
    }
    
    
    /**
     * Returns a {@code Finder} with diagnostic messages that exactly match the given message.
     * 
     * @param message the message
     * @return {@code this}
     */
    public Finder matches(String message) {
        diagnostics.removeIf(diagnostic -> !diagnostic.getMessage(Locale.getDefault()).equals(message));
        return this;
    }
    
    /**
     * Returns a {@code Finder} with diagnostic messages that contain the given substring.
     * 
     * @param substring the substring
     * @return {@code this}
     */
    public Finder contains(String substring) {
        diagnostics.removeIf(diagnostic -> !diagnostic.getMessage(Locale.getDefault()).contains(substring));
        return this;
    }
    
    /**
     * Returns a {code Finder} with diagnostic messages that match the given pattern.
     * 
     * @param pattern the pattern
     * @return {@code this}
     */
    public Finder contains(Pattern pattern) {
        diagnostics.removeIf(diagnostic -> !pattern.matcher(diagnostic.getMessage(Locale.getDefault())).matches());
        return this;
    }
    
    
    /**
     * Returns the full string representations of the diagnostic messages.
     * 
     * @return the full diagnostic messages
     */
    public List<String> diagnostics() {
        return diagnostics.stream().map(Diagnostic::toString).collect(toList());
    }
    
    /**
     * Returns only the message portions of the diagnostic messages.
     * 
     * @return the message portions
     */
    public List<String> messages() {
        return diagnostics.stream().map(diagnostic -> diagnostic.getMessage(Locale.getDefault())).collect(toList());
    }
    
    /**
     * Returns the line numbers of the diagnostic messages.
     * 
     * @return the line numbers
     */
    public List<Long> lines() {
        return diagnostics.stream().map(Diagnostic::getLineNumber).collect(toList());
    }
    
    /**
     * Returns the column numbers of the diagnostic messages.
     * 
     * @return the column numbers
     */
    public List<Long> columns() {
        return diagnostics.stream().map(Diagnostic::getColumnNumber).collect(toList());
    }
    
    /**
     * Return the positions of the diagnostic messages from the start of a source file.
     * 
     * @return the positions
     */
    public List<Long> positions() {
        return diagnostics.stream().map(Diagnostic::getPosition).collect(toList());
    }
    
    /**
     * Return the codes of the diagnostic messages.
     * 
     * @return the codes
     */
    public List<String> codes() {
        return diagnostics.stream().map(Diagnostic::getCode).collect(toList());
    }
    
    
    /**
     * Returns the diagnostic message if this {@code Finder} contains exactly one
     * diagnostic message. Otherwise returns {@code null}.
     * 
     * @return the diagnostic message if this {@code Finder} contains exactly one diagnostic message
     */
    public @Nullable Diagnostic<? extends JavaFileObject> one() {
        return diagnostics.size() == 1 ? diagnostics.get(0) : null;
    }
    
    /**
     * Returns the diagnostic messages.
     * 
     * @return the diagnostic messages
     */
    public List<Diagnostic<? extends JavaFileObject>> list() {
        return diagnostics;
    }
    
    /**
     * Returns a map of diagnostic messages associated with their {@code Kind}s.
     * 
     * @return the map
     */
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
    
    
    /**
     * Returns the current number of diagnostic messages.
     * 
     * @return the current number of diagnostic messages
     */
    public int count() {
        return diagnostics.size();
    }
    
}
