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
import javax.lang.model.element.*;

/**
 * A code snippet that represents a {@code VariableElement}.
 */
public class VariableSnippet extends Snippet {

    /**
     * Creates a {@code VariableSnippet} with the given {@code VariableElement}.
     * 
     * @param element the variable
     * @param column the column
     * @return a {@code VariableSnippet}
     */
    public static VariableSnippet of(VariableElement element, int column) {
        var lines = new TreeMap<Integer, Line>();
        
        var annotations = AnnotationsSnippet.of(element.getAnnotationMirrors(), column);
        lines.putAll(annotations.lines);
        
        var variable = VariableLine.of(element, false, annotations.last + 1, 0);
        
        lines.put(variable.column, variable);
        
        return new VariableSnippet(annotations, variable.modifiers, variable.type, variable.name, lines);
    }
    
    /**
     * The annotations.
     */
    public final AnnotationsSnippet annotations;
    /**
     * The modifiers.
     */
    public final Part<Modifier, Line> modifiers;
    /**
     * The type.
     */
    public final Line type;
    /**
     * The name.
     */
    public final Line name;
    
    VariableSnippet(AnnotationsSnippet annotations, Part<Modifier, Line> modifiers, Line type, Line name, TreeMap<Integer, Line> lines) {
        super(lines);
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.type = type;
        this.name = name;
    }
    
}
