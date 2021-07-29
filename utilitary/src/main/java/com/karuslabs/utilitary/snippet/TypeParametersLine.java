

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
import javax.lang.model.element.TypeParameterElement;

import static com.karuslabs.utilitary.Texts.join;
import static com.karuslabs.utilitary.type.TypePrinter.simple;

/**
 * Represents the type parameters of a {@code TypeElement}.
 */
public class TypeParametersLine extends Line {

    /**
     * Creates a {@code TypeParametersLine} with the given type parameters.
     * 
     * @param parameters the type parameters
     * @param column the column
     * @param position the position
     * @return a {@code TypeParametersLine}
     */
    public static TypeParametersLine of(List<? extends TypeParameterElement> parameters, int column, int position) {
        if (parameters.isEmpty()) {
            return new TypeParametersLine(Map.of(), "", column, position);
        }
        
        var arguments = new LinkedHashMap<TypeParameterElement, Line>();
        var builder = new StringBuilder().append("<");
        
        join(builder, parameters, (parameter, sb) -> {
            var line = new Line(simple(parameter.asType()), column, position + builder.length());
            arguments.put(parameter, line);
            builder.append(line);
        }, ", ");
        
        return new TypeParametersLine(arguments, builder.append(">").toString(), column, position);
    }
    
    /**
     * The type parameters.
     */
    public final Map<TypeParameterElement, Line> arguments;
    
    TypeParametersLine(Map<TypeParameterElement, Line> arguments, String value, int column, int position) {
        super(value, column, position);
        this.arguments = arguments;
    }
    
}
