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
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

/**
 * A code snippet that represents a {@code TypeElement}.
 */
public class TypeSnippet extends Snippet {

    /**
     * Creates a {@code TypeSnippet} for the given {@code TypeElement}.
     * 
     * @param element the {@code TypeElement}
     * @param column the column
     * @return a {@code TypeSnippet}
     */
    public static TypeSnippet of(TypeElement element, int column) {
        var lines = new TreeMap<Integer, Line>();
        
        var annotations = AnnotationsSnippet.of(element.getAnnotationMirrors(), column);
        lines.putAll(annotations.lines);
        
        var builder = new StringBuilder();
        column = annotations.last + 1;
        
        var modifiers = Part.modifiers(element.getModifiers(), column, builder.length());
        builder.append(modifiers);
        
        var type = TypeLine.of(element.getKind(), column, builder.length());
        builder.append(type).append(" ");
        
        var name = new Line(element.getSimpleName().toString(), column, builder.length());
        builder.append(name);
        
        var typeParameters = Part.typeParameters(element.getTypeParameters(), column, builder.length());
        builder.append(typeParameters);
        
        var supertype = Part.extend(element.getSuperclass(), column, builder.length());
        builder.append(supertype);
        
        var interfaces = Part.implement(element.getKind(), element.getInterfaces(), column, builder.length());
        builder.append(interfaces).append(" {");
        
        lines.put(column, new Line(builder.toString(), column, 0));
        
        return new TypeSnippet(annotations, modifiers, type, name, typeParameters, supertype, interfaces, lines);
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
    public final TypeLine type;
    /**
     * The name.
     */
    public final Line name;
    /**
     * The type parameters.
     */
    public final Part<TypeParameterElement, Line> typeParameters;
    /**
     * The supertype.
     */
    public final Part<TypeMirror, Line> supertype;
    /**
     * The implemented interfaces.
     */
    public final Part<TypeMirror, Line> interfaces;
    
    TypeSnippet(
        AnnotationsSnippet annotations, 
        Part<Modifier, Line> modifiers,
        TypeLine type,
        Line name, 
        Part<TypeParameterElement, Line> typeParameters,
        Part<TypeMirror, Line> supertype,
        Part<TypeMirror, Line> interfaces,
        TreeMap<Integer, Line> lines
    ) {
        super(lines);
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.type = type;
        this.name = name;
        this.typeParameters = typeParameters;
        this.supertype = supertype;
        this.interfaces = interfaces;
    }
    
}