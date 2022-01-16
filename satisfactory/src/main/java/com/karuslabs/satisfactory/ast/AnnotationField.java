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
import com.karuslabs.satisfactory.sequence.Sequence;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.*;
import java.util.Map.Entry;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

import static java.lang.Math.abs;

public sealed interface AnnotationField extends Assertion<Entry<String, AnnotationValue>> {
    
    // TODO: annotation mirror, enum
    
    static AnnotationField of(String name, boolean value) {
        return new SimpleField<>(name, value);
    }
    
    static AnnotationField of(String name, byte value) {
        return new SimpleField<>(name, value);
    }
    
    static AnnotationField of(String name, char value) {
        return new SimpleField<>(name, value);
    }
    
    static AnnotationField of(double value, double epsilon) {
        return new DoubleField(value, epsilon);
    }
    
    static AnnotationField of(float value, float epsilon) {
        return new FloatField(value, epsilon);
    }
    
    static AnnotationField of(String name, int value) {
        return new SimpleField<>(name, value);
    }
    
    static AnnotationField of(String name, long value) {
        return new SimpleField<>(name, value);
    }
    
    static AnnotationField of(String name, short value) {
        return new SimpleField<>(name, value);
    }
    
    static AnnotationField of(String name, String value) {
        return new SimpleField<>(name, value);
    }
    
    static AnnotationField of(Assertion<TypeMirror> type) {
        return new TypeField(type);
    }
    
}

record SimpleField<T>(String name, T expected) implements AnnotationField {
    @Override
    public Result test(Entry<String, AnnotationValue> field, TypeMirrors types) {
        var actual = field.getValue().getValue();
        var success = name.equals(field.getKey()) && expected.getClass() == actual.getClass() && expected.equals(actual);
        return new Result.AST.AnnotationField(field, name, expected, success);
    }
}