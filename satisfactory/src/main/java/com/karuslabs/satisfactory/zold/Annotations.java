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
package com.karuslabs.satisfactory.zold;

import com.karuslabs.satisfactory.*;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.lang.annotation.Annotation;
import java.util.List;
import javax.lang.model.AnnotatedConstruct;

import static com.karuslabs.satisfactory.Result.SUCCESS;

class ContainsAnnotations implements Assertion<AnnotatedConstruct> {

    private final List<Class<? extends Annotation>> expected;
    private final Failure.Annotations failure;
    
    ContainsAnnotations(Class<? extends Annotation>... annotations) {
        expected = List.of(annotations);
        failure = new Failure.Annotations(expected);
    }

    @Override
    public Result test(AnnotatedConstruct annotated, TypeMirrors types) {
        for (var annotation : expected) {
            if (annotated.getAnnotationsByType(annotation).length == 0) {
                return failure;
            }
        }
        
        return SUCCESS;
    }
    
    @Override
    public Failure.Annotations fail() {
        return failure;
    }
    
}
