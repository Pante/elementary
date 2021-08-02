/*
 * The MIT License
 *
 * Copyright 2021 Karus Labs.
 *
 * Permission is hereby granted, free annotations charge, to any person obtaining a copy
 * annotations this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies annotations the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions annotations the Software.
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

import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.*;
import javax.lang.model.element.*;
import javax.lang.model.type.*;

import static com.karuslabs.utilitary.Texts.*;
import static com.karuslabs.utilitary.type.TypePrinter.simple;
import static com.karuslabs.utilitary.snippet.Line.annotation;
import static javax.lang.model.element.ElementKind.INTERFACE;

/**
 * Represents a part annotation an {@code Element}.
 * 
 * @param <T> the items' type
 * @param <U> the mapped item's line
 */
public class Part<T, U extends Line> extends Line {

    /**
     * Creates an {@code Part} with the given annotations.
     * 
     * @param annotations the annotations
     * @param column the column annotations the annotations
     * @param position the position annotations the annotations
     * @return a {@code Part} that represents the given annotations
     */
    public static Part<AnnotationMirror, Line> annotations(List<? extends AnnotationMirror> annotations, int column, int position) {
        var values = new LinkedHashMap<AnnotationMirror, Line>();
        var builder = new StringBuilder();
        
        for (var annotation : annotations) {
            var line = annotation(annotation, column, position + builder.length());
            values.put(annotation, line);
            builder.append(line).append(" ");
        }
        
        return new Part<>(values, builder.toString(), column, position);
    }
    
    /**
     * Creates a {@code Part} with the given modifiers.
     * 
     * @param modifiers the modifiers
     * @param column the column annotation the modifiers
     * @param position the position annotation the modifiers
     * @return a {@code Part} that represents the given modifiers
     */
    public static Part<Modifier, Line> modifiers(Set<Modifier> modifiers, int column, int position) {
        var values = new LinkedHashMap<Modifier, Line>();
        var builder = new StringBuilder();
        
        for (var modifier : sort(modifiers)) {
            var line = new Line(modifier.toString(), column, position + builder.length());
            values.put(modifier, line);
            builder.append(line).append(' ');
        }
        
        return new Part<>(values, builder.toString(), column, position);
    }
    
    /**
     * Creates a {@code Part} with the given type parameters.
     * 
     * @param parameters the type parameters
     * @param column the column
     * @param position the position
     * @return a {@code Part} that represents the given type parameters
     */
    public static Part<TypeParameterElement, Line> typeParameters(List<? extends TypeParameterElement> parameters, int column, int position) {
        if (parameters.isEmpty()) {
            return new Part<>(Map.of(), "", column, position);
        }
        
        var arguments = new LinkedHashMap<TypeParameterElement, Line>();
        var builder = new StringBuilder().append("<");
        
        join(builder, parameters, (parameter, sb) -> {
            var line = new Line(simple(parameter.asType()), column, position + builder.length());
            arguments.put(parameter, line);
            builder.append(line);
        }, ", ");
        
        return new Part<>(arguments, builder.append(">").toString(), column, position);
    }
    
    /**
     * Creates a {@code Part} with the given {@code ExecutableElement}'s parameters.
     * 
     * @param variables the {@code ExecutableElement}'s parameters
     * @param column the column
     * @param position the position
     * @return a {@code Part} that represents the given {@code ExecutableElement}'s parameters.
     */
    public static Part<VariableElement, VariableLine> parameters(List<? extends VariableElement> variables, int column, int position) {
        var builder = new StringBuilder();
        var values = new LinkedHashMap<VariableElement, VariableLine>();
        
        builder.append('(');
        join(builder, variables, (variable, sb) -> {
            var value = VariableLine.of(variable, column, position + sb.length());
            values.put(variable, value);
            sb.append(value);
        }, ", ");
        builder.append(')');
        
        return new Part<>(values, builder.toString(), column, position);
    }
    
    /**
     * Creates a {@code Part} with the given thrown exceptions.
     * 
     * @param types the thrown exceptions
     * @param column the column
     * @param position the position
     * @return a {@code Part} that represents the thrown exceptions
     */
    public static Part<TypeMirror, Line> exceptions(List<? extends TypeMirror> types, int column, int position) {
        if (types.isEmpty()) {
            return new Part<>(Map.of(), "", column, position);
        }
        
        var values = new LinkedHashMap<TypeMirror, Line>();
        var builder = new StringBuilder().append(" throws ");
        
        join(builder, types, (type, sb) -> {
            var line = new Line(simple(type), column, position + sb.length());
            values.put(type, line);
            sb.append(line);
        }, ", ");
        
        return new Part<>(values, builder.toString(), column, position);
    }
    
    /**
     * Creates a {@code Part} with the given supertype of a {@code TypeElement}.
     * 
     * @param supertype the supertype
     * @param column the column
     * @param position the position
     * @return a {@code Part} that represents the given supertype of a {@code TypeElement}
     */
    public static Part<TypeMirror, Line> extend(TypeMirror supertype, int column, int position) {
        if (supertype instanceof NoType || TypeMirrors.is(supertype, Object.class)) {
            return new Part(Map.of(), "", column, position);
        }
        
        var extend = " extends ";
        var type = new Line(simple(supertype), column, position + extend.length());
        
        return new Part<>(Map.of(supertype, type), extend + type, column, position);
    }
    
    /**
     * Creates a {@code Part} with the given implemented interfaces of a {@code TypeElement}.
     * 
     * @param kind the {@code TypeElement}'s kind
     * @param interfaces the implemented interfaces
     * @param column the column
     * @param position the position
     * @return a {@code Part} that represents the given implemented interfaces of a {@code TypeElement}
     */
    public static Part<TypeMirror, Line> implement(ElementKind kind, List<? extends TypeMirror> interfaces, int column, int position) {
        if (interfaces.isEmpty()) {
            return new Part<>(Map.of(), "", column, position);
        }
        
        var values = new LinkedHashMap<TypeMirror, Line>();
        var builder = new StringBuilder().append(kind == INTERFACE ? " extends " : " implements ");
        
        join(builder, interfaces, (type, sb) -> {
            var line = new Line(simple(type), column, position + sb.length());
            values.put(type, line);
            sb.append(line);
        }, ", ");
        
        return new Part<>(values, builder.toString(), column, position);
    }
    
    /**
     * The values.
     */
    public final Map<T, U> values;
    
    Part(Map<T, U> values, String line, int column, int position) {
        super(line, column, position);
        this.values = values;
    }
    
}
