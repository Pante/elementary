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
import javax.lang.model.element.VariableElement;

import static com.karuslabs.utilitary.type.TypePrinter.simple;

public class VariableSnippet extends Snippet {

    public static VariableSnippet of(VariableElement variable, int column) {
        var lines = new LinkedHashMap<Integer, CharSequence>();
        
        var annotations = AnnotationsSnippet.of(variable.getAnnotationMirrors(), column);
        lines.putAll(annotations.lines);
        
        var builder = new StringBuilder();
        column = annotations.last + 1;
        
        var modifiers = ModifiersLine.of(variable.getModifiers(), column, 0);
        builder.append(modifiers);
        
        var type = new Line(simple(variable.asType()), column, builder.length());
        builder.append(type).append(" ");
        
        var name = new Line(variable.getSimpleName().toString(), column, builder.length());
        builder.append(name);
        
        lines.put(column, builder.toString());
        
        return new VariableSnippet(annotations, modifiers, type, name, lines);
    }
    
    public final AnnotationsSnippet annotations;
    public final ModifiersLine modifiers;
    public final Line type;
    public final Line name;
    
    VariableSnippet(AnnotationsSnippet annotations, ModifiersLine modifiers, Line type, Line name, Map<Integer, CharSequence> lines) {
        super(lines);
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.type = type;
        this.name = name;
    }
    
}
