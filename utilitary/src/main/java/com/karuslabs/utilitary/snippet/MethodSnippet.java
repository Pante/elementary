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
import javax.lang.model.element.ExecutableElement;

import static com.karuslabs.utilitary.type.TypePrinter.simple;

public class MethodSnippet extends Snippet {

    public static MethodSnippet of(ExecutableElement method, int column) {
        var lines = new LinkedHashMap<Integer, CharSequence>();
        
        var annotations = AnnotationsSnippet.of(method.getAnnotationMirrors(), column);
        lines.putAll(annotations.lines);
        
        var builder = new StringBuilder();
        column = annotations.last + 1;
        
        var modifiers = ModifiersLine.of(method.getModifiers(), column, builder.length());
        builder.append(modifiers);
        
        var generics = TypeParametersLine.of(method.getTypeParameters(), column, builder.length());
        builder.append(generics).append(" ");
        
        var type = new Line(simple(method.getReturnType()), column, builder.length());
        builder.append(type).append(" ");
        
        var name = new Line(method.getSimpleName().toString(), column, builder.length());
        builder.append(name);
        
        var parameters = VariablesLine.of(method.getParameters(), column, builder.length());
        builder.append(parameters);
        
        var exceptions = ThrowsLine.of(method.getThrownTypes(), column, builder.length());
        builder.append(exceptions);
        
        lines.put(column, builder.toString());
        
        return new MethodSnippet(annotations, modifiers, generics, type, name, parameters, exceptions, lines);
    }
    
    
    public final AnnotationsSnippet annotations;
    public final ModifiersLine modifiers;
    public final TypeParametersLine generics;
    public final Line type;
    public final Line name;
    public final VariablesLine parameters;
    public final ThrowsLine exceptions;
    
    public MethodSnippet(
        AnnotationsSnippet annotations, 
        ModifiersLine modifiers, 
        TypeParametersLine generics,
        Line type, 
        Line name, 
        VariablesLine parameters, 
        ThrowsLine exceptions,
        Map<Integer, CharSequence> lines
    ) {
        super(lines);
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.generics = generics;
        this.type = type;
        this.name = name;
        this.parameters = parameters;
        this.exceptions = exceptions;
    }
    
}
