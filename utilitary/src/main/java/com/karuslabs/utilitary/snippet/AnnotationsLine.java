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
import javax.lang.model.element.AnnotationMirror;

/**
 * Represents the annotations of an {@code Element} merged onto a single line.
 */
public class AnnotationsLine extends Line {
    
    /**
     * Creates an {@code AnnotationsLine} with the given annotations.
     * 
     * @param annotations the annotations
     * @param column the column of the annotations
     * @param position the position of the annotations
     * @return a {@code AnnotationsLine}
     */
    public static AnnotationsLine of(List<? extends AnnotationMirror> annotations, int column, int position) {
        var values = new LinkedHashMap<AnnotationMirror, Line>();
        var builder = new StringBuilder();
        
        for (var annotation : annotations) {
            var line = of(annotation, column, position + builder.length());
            values.put(annotation, line);
            builder.append(line).append(" ");
        }
        
        return new AnnotationsLine(values, builder.toString(), column, position);
    }

    /**
     * The annotations.
     */
    public final Map<AnnotationMirror, Line> values;
    
    AnnotationsLine(Map<AnnotationMirror, Line> values, String line, int column, int position) {
        super(line, column, position);
        this.values = values;
    }
    
}
