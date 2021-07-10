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
package com.karuslabs.utilitary.texts;

import javax.lang.model.element.*;

import static com.karuslabs.utilitary.type.TypePrinter.simple;

public class MethodText extends Text {

    public static MethodText of(ExecutableElement method, int column, int position) {
        var builder = new StringBuilder();
        var top = column;
        var first = position;
        
        var annotations = AnnotationsText.inline(method.getAnnotationMirrors(), column, position + builder.length());
        builder.append(annotations);
        
        column += annotations.values.size();
        position -= annotations.length();
        
        var modifiers = ModifiersText.of(method.getModifiers(), column, position + builder.length());
        builder.append(modifiers);
        
        var type = new Text(simple(method.getReturnType()), column, position + builder.length());
        builder.append(type).append(" ");
        
        var name = new Text(method.getSimpleName().toString(), column, position + builder.length());
        builder.append(name);
        
        var parameters = VariablesText.of(method.getParameters(), column, position + builder.length());
        builder.append(parameters);
        
        var exceptions = TypesText.of(method.getThrownTypes(), column, position + builder.length());
        if (!exceptions.positions.isEmpty()) {
            builder.append(" throws ").append(exceptions);
        }
        
        return new MethodText(annotations, modifiers, type, name, parameters, exceptions, builder.toString(), top, first);
    }
    
    public final AnnotationsText annotations;
    public final ModifiersText modifiers;
    public final Text type;
    public final Text name;
    public final VariablesText parameters;
    public final TypesText exceptions;
    
    public MethodText(AnnotationsText annotations, ModifiersText modifiers, Text type, Text name, VariablesText parameters, TypesText exceptions, String value, int column, int position) {
        super(value, column, position);
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.type = type;
        this.name = name;
        this.parameters = parameters;
        this.exceptions = exceptions;
    }
    
}
