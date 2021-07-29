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
import javax.lang.model.type.TypeMirror;

import static com.karuslabs.utilitary.Texts.join;
import static com.karuslabs.utilitary.type.TypePrinter.*;

/**
 * Represents the thrown exceptions of an {@code ExecutableElement}.
 */
public class ThrowsLine extends Line {

    /**
     * Creates a {@code ThrownsLine} with the given thrown exceptions.
     * 
     * @param types the thrown exceptions
     * @param column the column
     * @param position the position
     * @return a {@code ThrowsLine}
     */
    public static ThrowsLine of(List<? extends TypeMirror> types, int column, int position) {
        if (types.isEmpty()) {
            return new ThrowsLine(Map.of(), "", column, position);
        }
        
        var values = new LinkedHashMap<TypeMirror, Line>();
        var builder = new StringBuilder().append(" throws ");
        
        join(builder, types, (type, sb) -> {
            var line = new Line(simple(type), column, position + sb.length());
            values.put(type, line);
            sb.append(line);
        }, ", ");
        
        return new ThrowsLine(values, builder.toString(), column, position);
    }
    
    /**
     * The exceptions.
     */
    public final Map<TypeMirror, Line> values;
    
    ThrowsLine(Map<TypeMirror, Line> values, String value, int column, int position) {
        super(value, column, position);
        this.values = values;
    }
    
}
