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
import javax.lang.model.type.TypeMirror;

import static com.karuslabs.utilitary.type.TypePrinter.simple;

/**
 * A code snippet that represents a {@code ExecutableElement}.
 */
public class MethodSnippet extends Snippet {

    /**
     * Creates a {@code MethodSnippet} with the given {@code ExecutableElement}.
     * 
     * @param method the method
     * @param column the column
     * @return a {@code VariableSnippet}
     */
    public static MethodSnippet of(ExecutableElement method, int column) {
        var lines = new TreeMap<Integer, Line>();
        
        var annotations = AnnotationsSnippet.of(method.getAnnotationMirrors(), column);
        lines.putAll(annotations.lines);
        
        var builder = new StringBuilder();
        column = annotations.last + 1;
        
        var modifiers = Part.modifiers(method.getModifiers(), column, builder.length());
        builder.append(modifiers);
        
        var typeParameters = Part.typeParameters(method.getTypeParameters(), column, builder.length());
        builder.append(typeParameters).append(" ");
        
        var type = new Line(simple(method.getReturnType()), column, builder.length());
        builder.append(type).append(" ");
        
        var name = new Line(method.getSimpleName().toString(), column, builder.length());
        builder.append(name);
        
        var parameters = Part.parameters(method.getParameters(), column, builder.length());
        builder.append(parameters);
        
        var exceptions = Part.exceptions(method.getThrownTypes(), column, builder.length());
        builder.append(exceptions);
        
        lines.put(column, new Line(builder.toString(), column, 0));
        
        return new MethodSnippet(annotations, modifiers, typeParameters, type, name, parameters, exceptions, lines);
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
     * The type parameters.
     */
    public final Part<TypeParameterElement, Line> typeParameters;
    /**
     * The return type.
     */
    public final Line type;
    /**
     * The name.
     */
    public final Line name;
    /**
     * The parameters.
     */
    public final Part<VariableElement, VariableLine> parameters;
    /**
     * The thrown exceptions.
     */
    public final Part<TypeMirror, Line> exceptions;
    
    MethodSnippet(
        AnnotationsSnippet annotations, 
        Part<Modifier, Line> modifiers, 
        Part<TypeParameterElement, Line> typeParameters,
        Line type, 
        Line name, 
        Part<VariableElement, VariableLine> parameters, 
        Part<TypeMirror, Line> exceptions,
        TreeMap<Integer, Line> lines
    ) {
        super(lines);
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.typeParameters = typeParameters;
        this.type = type;
        this.name = name;
        this.parameters = parameters;
        this.exceptions = exceptions;
    }
    
}
