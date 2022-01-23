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
import javax.lang.model.type.TypeMirror;

import static java.lang.Math.abs;

public sealed interface Literal extends Assertion<Object> {

    // TODO: annotation mirror, enum
    
    static Literal of(boolean value) {
        return new ValueLiteral<>(value);
    }
    
    static Literal of(byte value) {
        return new ValueLiteral<>(value);
    }
    
    static Literal of(char value) {
        return new ValueLiteral<>(value);
    }
    
    static Literal of(double value, double epsilon) {
        return new DoubleLiteral(value, epsilon);
    }
    
    static Literal of(float value, float epsilon) {
        return new FloatLiteral(value, epsilon);
    }
    
    static Literal of(int value) {
        return new ValueLiteral<>(value);
    }
    
    static Literal of(long value) {
        return new ValueLiteral<>(value);
    }
    
    static Literal of(short value) {
        return new ValueLiteral<>(value);
    }
    
    static Literal of(String value) {
        return new ValueLiteral<>(value);
    }
    
    static Literal of(Assertion<TypeMirror> type) {
        return new TypeLiteral(type);
    }
    
}

record ArrayLiteral(Sequence.Ordered<Object> expected) implements Literal {
    @Override
    public Result test(Object actual, TypeMirrors types) {
        return actual instanceof List values ? expected.test(values, types) : new Result.Equal<>(actual, Object[].class, false);
    }
}

record ValueLiteral<T>(T expected) implements Literal {
    @Override
    public Result test(Object actual, TypeMirrors types) {
        return new Result.Equal<>(actual, expected, expected.getClass() == actual.getClass() && expected.equals(actual));
    }
}

record DoubleLiteral<T extends Number>(double expected, double epsilon) implements Literal {
    @Override
    public Result test(Object actual, TypeMirrors types) {
        return new Result.Equal<>(actual, expected, actual instanceof Double number && abs(expected - number) < epsilon);
    }
}

record FloatLiteral(float expected, float epsilon) implements Literal {
    @Override
    public Result test(Object actual, TypeMirrors types) {
        return new Result.Equal<>(actual, expected, actual instanceof Float number && abs(expected - number) < epsilon);
    }
}

record TypeLiteral(Assertion<TypeMirror> expected) implements Literal {
    @Override
    public Result test(Object actual, TypeMirrors types) {
        return actual instanceof TypeMirror type ? expected.test(type, types) : new Result.Equal<>(actual, TypeMirror.class, false);
    }
        
}
