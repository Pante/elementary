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

import java.util.List;
import javax.lang.model.element.*;

import static com.karuslabs.utilitary.type.TypePrinter.simple;

/**
 * Represents a {@code VariableElement} with all annotations inlined.
 */
public class VariableLine extends Line {
    
    /**
     * Creates a {@code VariableLine} with the given {@code VariableElement} that
     * includes the annotations on the given {@code VariableElement}.
     * 
     * @param variable the variable
     * @param column the column
     * @param position the position
     * @return a {@code VariableLine} 
     */
    public static VariableLine of(VariableElement variable, int column, int position) {
        return of(variable, true, column, position);
    }
    
    /**
     * Creates a {@code VariableLine} with the given {@code VariableElement}.
     * 
     * @param variable the variable
     * @param annotated whether to include the annotations on the given {@code VariableElement}
     * @param column the column
     * @param position the position
     * @return a {@code VariableLine} 
     */
    public static VariableLine of(VariableElement variable, boolean annotated, int column, int position) {
        var builder = new StringBuilder();
        
        var modifiers = Part.modifiers(variable.getModifiers(), column, position);
        builder.append(modifiers);
        
        var annotations = Part.annotations(annotated ? variable.getAnnotationMirrors() : List.of(), column, position + builder.length());
        builder.append(annotations);
        
        var type = new Line(simple(variable.asType()), column, position + builder.length());
        builder.append(type).append(" ");
        
        var name = new Line(variable.getSimpleName().toString(), column, position + builder.length());
        builder.append(name);
        
        return new VariableLine(modifiers, annotations, type, name, builder.toString(), column, position);
    } 
    
    /**
     * The modifiers.
     */
    public final Part<Modifier, Line> modifiers;
    /**
     * The annotations.
     */
    public final Part<AnnotationMirror, Line> annotations;
    /**
     * The type.
     */
    public final Line type;
    /**
     * The name.
     */
    public final Line name;
    
    VariableLine(Part<Modifier, Line> modifiers, Part<AnnotationMirror, Line> annotations, Line type, Line name, String value, int column, int position) {
        super(value, column, position);
        this.modifiers = modifiers;
        this.annotations = annotations;
        this.type = type;
        this.name = name;
    }
    
}
