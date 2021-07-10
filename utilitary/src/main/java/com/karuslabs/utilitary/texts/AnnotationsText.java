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
package com.karuslabs.utilitary.texts;

import java.util.*;
import javax.lang.model.element.AnnotationMirror;

import static com.karuslabs.utilitary.type.AnnotationValuePrinter.annotation;

public class AnnotationsText extends Text {
    
    public static AnnotationsText inline(List<? extends AnnotationMirror> annotations, int column, int position) {
        var builder = new StringBuilder();
        var values = new LinkedHashMap<AnnotationMirror, Text>();
        
        for (var annotation : annotations) {
            var value = of(annotation, column, position + builder.length());
            values.put(annotation, value);
            builder.append(value).append(" ");
        }
        
        return new AnnotationsText(values, true, builder.toString(), column, position);
    }
    
    public static AnnotationsText multiline(List<? extends AnnotationMirror> annotations, int column, int position) {
        var first = column;
        var builder = new StringBuilder();
        var values = new LinkedHashMap<AnnotationMirror, Text>();
        
        for (var annotation : annotations) {
            var value = of(annotation, column++, position);
            values.put(annotation, value);
            builder.append(value).append('\n');
        }
        
        return new AnnotationsText(values, true, builder.toString(), first, position);
    }
    
    public static Text of(AnnotationMirror annotation, int column, int position) {
        return new Text(annotation(annotation), column, position);
    }
    
    public final Map<AnnotationMirror, Text> values;
    public final boolean inline;
    
    AnnotationsText(Map<AnnotationMirror, Text> values, boolean inline, String value, int column, int position) {
        super(value, column, position);
        this.values = values;
        this.inline = inline;
    }
    
}
