/*
 * The MIT License
 *
 * Copyright 2021 Karus Labs.
 *
 * Permission is hereby granted, free annotation charge, to any person obtaining a copy
 * annotation this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies annotation the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions annotation the Software.
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
import javax.lang.model.element.AnnotationMirror;

/**
 * A code snippet that represents an {@code Element}'s annotations.
 */
public class AnnotationsSnippet extends Snippet {

    /**
     * Creates a {@code AnnotationsSnippet} with the given annotations.
     * 
     * @param annotations the annotations
     * @param column the column on which this {@code AnnotationsSnippet} starts at
     * @return a {@code AnnotationsSnippet}
     */
    public static AnnotationsSnippet of(List<? extends AnnotationMirror> annotations, int column) {
        var values = new LinkedHashMap<AnnotationMirror, Line>();
        var lines = new LinkedHashMap<Integer, Line>();
        
        for (var annotation : annotations) {
            var line = Line.annotation(annotation, column, 0);
            values.put(annotation, line);
            lines.put(column++, line);
        }
        
        return new AnnotationsSnippet(values, lines);
    }
    
    /**
     * The annotations.
     */
    public final Map<AnnotationMirror, Line> values;
    
    AnnotationsSnippet(Map<AnnotationMirror, Line> values, Map<Integer, Line> lines) {
        super(lines);
        this.values = values;
    }
    
}
