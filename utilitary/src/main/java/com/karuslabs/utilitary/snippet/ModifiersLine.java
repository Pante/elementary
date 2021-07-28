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
import javax.lang.model.element.Modifier;

import static com.karuslabs.utilitary.Texts.sort;

/**
 * Represents the modifiers of an {@code Element} sorted according to common Java conventions.
 */
public class ModifiersLine extends Line {

    /**
     * Creates a {@code ModifiersLine} with the given modifiers.
     * 
     * @param modifiers the modifiers
     * @param column the column of the modifiers
     * @param position the position of the modifiers
     * @return 
     */
    public static ModifiersLine of(Set<Modifier> modifiers, int column, int position) {
        var values = new LinkedHashMap<Modifier, Line>();
        var builder = new StringBuilder();
        
        for (var modifier : sort(modifiers)) {
            var line = new Line(modifier.toString(), column, position + builder.length());
            values.put(modifier, line);
            builder.append(line).append(' ');
        }
        
        return new ModifiersLine(values, builder.toString(), column, position);
    }
    
    /**
     * The modifiers.
     */
    public final Map<Modifier, Line> values;
    
    ModifiersLine(Map<Modifier, Line> values, String line, int column, int position) {
        super(line, column, position);
        this.values = values;
    }
    
}
