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

import java.util.List;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.type.TypeMirror;

import static java.lang.Math.abs;

public sealed interface AnnotationField extends Assertion<AnnotationValue> {

    // TODO: annotation mirror, enum
    
}

// In project Vahalla we trust

record ArrayField(Sequence.Ordered<AnnotationValue> expected) implements AnnotationField {
    @Override
    public Result test(AnnotationValue value, TypeMirrors types) {
        var actual = value.getValue();
        return actual instanceof List values ? expected.test(values, types) : new Result.Equals<>(actual, Object[].class, false);
    }
}

record BooleanField(boolean expected) implements AnnotationField {
    @Override
    public Result test(AnnotationValue value, TypeMirrors types) {
        var actual = value.getValue();
        return new Result.Equals<>(actual, expected, actual instanceof Boolean bool && bool == expected);
    }
}

record ByteField(byte expected) implements AnnotationField {
    @Override
    public Result test(AnnotationValue value, TypeMirrors types) {
        var actual = value.getValue();
        return new Result.Equals<>(actual, expected, actual instanceof Byte val && val == expected);
    }
}

record CharField(char expected) implements AnnotationField {
    @Override
    public Result test(AnnotationValue value, TypeMirrors types) {
        var actual = value.getValue();
        return new Result.Equals<>(actual, expected, actual instanceof Character character && character == expected);
    }
}

record DoubleField(double expected, double epsilon) implements AnnotationField {
    @Override
    public Result test(AnnotationValue value, TypeMirrors types) {
        var actual = value.getValue();
        return new Result.Equals<>(actual, expected, actual instanceof Double number && abs(expected - number) < epsilon);
    }
}

record FloatField(float expected, float epsilon) implements AnnotationField {
    @Override
    public Result test(AnnotationValue value, TypeMirrors types) {
        var actual = value.getValue();
        return new Result.Equals<>(actual, expected, actual instanceof Float number && abs(expected - number) < epsilon);
    }
}

record IntField(int expected) implements AnnotationField {
    @Override
    public Result test(AnnotationValue value, TypeMirrors types) {
        var actual = value.getValue();
        return new Result.Equals<>(actual, expected, actual instanceof Integer number && number == expected);
    }
}

record LongField(long expected) implements AnnotationField {
    @Override
    public Result test(AnnotationValue value, TypeMirrors types) {
        var actual = value.getValue();
        return new Result.Equals<>(actual, expected, actual instanceof Long number && number == expected);
    }
}

record ShortField(short expected) implements AnnotationField {
    @Override
    public Result test(AnnotationValue value, TypeMirrors types) {
        var actual = value.getValue();
        return new Result.Equals<>(actual, expected, actual instanceof Short number && number == expected);
    }
}

record StringField(String expected) implements AnnotationField {
    @Override
    public Result test(AnnotationValue value, TypeMirrors types) {
        var actual = value.getValue();
        return new Result.Equals<>(actual, expected, actual instanceof String string && string.equals(expected));
    }
}

record TypeField(Assertion<TypeMirror> expected) implements AnnotationField {
    @Override
    public Result test(AnnotationValue value, TypeMirrors types) {
        var actual = value.getValue();
        return actual instanceof TypeMirror type ? expected.test(type, types) : new Result.Equals<>(actual, TypeMirror.class, false);
    }
        
}
