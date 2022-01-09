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
package com.karuslabs.satisfactory.ast;

import com.karuslabs.satisfactory.*;
import com.karuslabs.satisfactory.sequence.Sequence.Unordered;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.Map.Entry;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

public class Annotation implements Assertion<AnnotationMirror> {

    private final Assertion<TypeMirror> type;
    private final Unordered<Entry<? extends ExecutableElement, ? extends AnnotationValue>> values; 
    
    public Annotation(Assertion<TypeMirror> type, Unordered<Entry<? extends ExecutableElement, ? extends AnnotationValue>> values) {
        this.type = type;
        this.values = values;
    }
    
    @Override
    public Result test(AnnotationMirror annotation, TypeMirrors types) {
        var type = this.type.test(annotation.getAnnotationType(), types);
        var values = this.values.test(annotation.getElementValues().entrySet(), types);
        return new Result.AST.Annotation(annotation, type, values, type.success() && values.success());
    }

}
