/*
 * The MIT License
 *
 * Copyright 2022 Karus Labs.
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
package com.karuslabs.satisfactory.diagnostic;

import com.karuslabs.satisfactory.*;
import com.karuslabs.utilitary.Texts;
import com.karuslabs.utilitary.type.TypePrinter;

public class DiagnosticPrinter implements Visitor<Context, Void> {

    @Override
    public Void type(Result.AST.Type result, Context context) {
        if (result.success() || context.negated) {
            return null;
        }
        
        var verb = context.negated ? "should not be " : "should be ";
        var relation = switch (result.relation()) {
            case IS -> "";
            case SUBTYPE -> "subtype of ";
            case SUPERTYPE -> "supertype of ";
        };
            
        var conditions = context.negated ? 
            Texts.or(result.expected(), (type, builder) -> type.accept(TypePrinter.qualified(), builder)) :
            Texts.and(result.expected(), (type, builder) -> type.accept(TypePrinter.qualified(), builder));
                
        context.builder.append(verb).append(relation).append(conditions);
        
        return null;
    }

    @Override
    public Void result(Result result, Context value) {
        return null;
    }
    
}
