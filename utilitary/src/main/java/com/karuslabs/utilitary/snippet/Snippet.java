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
package com.karuslabs.utilitary.snippet;

import java.util.*;

import static java.util.stream.Collectors.joining;

/**
 * Represents a code snippet. A code snippet may start from any non-negative index.
 */
public class Snippet implements CharSequence {
    
    /**
     * The lines in this snippet.
     */
    public final TreeMap<Integer, Line> lines;
    /**
     * The index of the first line.
     */
    public final int first;
    /**
     * The index of the last line.
     */
    public final int last;
    private final String value;
    
    /**
     * Creates a {@code Snippet} with the given lines.
     * 
     * @param lines the lines
     */
    public Snippet(TreeMap<Integer, Line> lines) {
        this.lines = lines;
        this.first = lines.firstKey();
        this.last = lines.lastKey();
        this.value = lines.values().stream().collect(joining("\n"));
    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        
        if (!(other instanceof Snippet)) {
            return false;
        }
        
        var snippet = (Snippet) other;
        return lines.equals(snippet.lines);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(lines);
    }
    
    @Override
    public String toString() {
        return value;
    }

}
