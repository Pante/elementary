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
package com.karuslabs.utilitary.type;

import com.karuslabs.utilitary.texts.Texts;

import java.util.*;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.*;

import static com.karuslabs.utilitary.type.TypePrinter.SIMPLE;

public class AnnotationValuePrinter extends SimpleAnnotationValueVisitor9<Void, StringBuilder> {
    
    public static final AnnotationValuePrinter PRINTER = new AnnotationValuePrinter();
    
    public static String annotation(AnnotationMirror annotation) {
        var builder = new StringBuilder();
        annotation(annotation, PRINTER, builder);
        return builder.toString();
    }
    
    static void annotation(AnnotationMirror annotation, AnnotationValuePrinter printer, StringBuilder builder) {
        builder.append('@').append(annotation.getAnnotationType().accept(SIMPLE, builder));
        var values = annotation.getElementValues();
        if (values.isEmpty()) {
            return;
        }
        
        builder.append('(');
        if (values.size() == 1 && values.keySet().toArray(ExecutableElement[]::new)[0].getSimpleName().contentEquals("value")) {
            values.values().toArray(AnnotationValue[]::new)[0].accept(printer, builder);

        } else {
            for (var mapping : values.entrySet()) {
                builder.append(mapping.getKey().getSimpleName()).append(" = ").append(mapping.getValue().accept(printer, builder));
            }
        }
        builder.append(')');
    }
    
    @Override
    public Void visitAnnotation(AnnotationMirror annotation, StringBuilder builder) {
        annotation(annotation, this, builder);
        return null;
    }
    
    @Override
    public Void visitArray(List<? extends AnnotationValue> values, StringBuilder builder) {
        builder.append('{');
        Texts.join(builder, values, (value, sb) -> sb.append(value.accept(this, sb)), ",");
        builder.append('}');
        return null;
    }
    
    @Override
    public Void visitEnumConstant(VariableElement value, StringBuilder builder) {
        builder.append(value.getSimpleName());
        return null;
    }
    
    @Override
    public Void visitString(String value, StringBuilder builder) {
        builder.append("\"").append(value).append("\"");
        return null;
    }
    
    @Override
    public Void visitType(TypeMirror type, StringBuilder builder) {
        type.accept(SIMPLE, builder);
        builder.append(".class");
        return null;
    }
    
    @Override
    protected Void defaultAction(Object object, StringBuilder builder) {
        builder.append(object);
        return null;
    }
    
}
