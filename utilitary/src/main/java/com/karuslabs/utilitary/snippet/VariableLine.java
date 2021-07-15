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

import javax.lang.model.element.VariableElement;

import static com.karuslabs.utilitary.type.TypePrinter.simple;

public class VariableLine extends Line {
    
    public static VariableLine of(VariableElement variable, int column, int indentation) {
        var builder = new StringBuilder();
        
        var modifiers = ModifiersLine.of(variable.getModifiers(), column, indentation);
        builder.append(modifiers);
        
        var annotations = AnnotationsLine.of(variable.getAnnotationMirrors(), column, indentation + builder.length());
        builder.append(annotations);
        
        var type = new Line(simple(variable.asType()), column, indentation + builder.length());
        builder.append(type).append(" ");
        
        var name = new Line(variable.getSimpleName().toString(), column, indentation + builder.length());
        builder.append(name);
        
        return new VariableLine(modifiers, annotations, type, name, builder.toString(), column, indentation);
    } 
    
    public final ModifiersLine modifiers;
    public final AnnotationsLine annotations;
    public final Line type;
    public final Line name;
    
    VariableLine(ModifiersLine modifiers, AnnotationsLine annotations, Line type, Line name, String value, int column, int position) {
        super(value, column, position);
        this.modifiers = modifiers;
        this.annotations = annotations;
        this.type = type;
        this.name = name;
    }
    
}
