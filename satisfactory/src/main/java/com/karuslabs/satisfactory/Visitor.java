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
package com.karuslabs.satisfactory;

import com.karuslabs.satisfactory.Result.*;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface Visitor<T, R> {
    
    default @Nullable R variable(AST.Variable variable, T value) {
        return result(variable, value);
    }
    
    default @Nullable R annotation(AST.Annotation annotation, T value) {
        return result(annotation, value);
    }
    
    default @Nullable R annotationField(AST.AnnotationField field, T value) {
        return result(field, value);
    }
    
    default @Nullable R modifiers(AST.Modifiers modifiers, T value) {
        return result(modifiers, value);
    }
    
    default @Nullable R type(AST.Type type, T value) {
        return result(type, value);
    }

    default @Nullable R primitive(AST.Primitive primitive, T value) {
        return result(primitive, value);
    }

    
    default @Nullable R pattern(Result.Sequence.Ordered.Pattern pattern, T value) {
        return result(pattern, value);
    }
    
    default @Nullable R equal(Result.Sequence.Ordered.Equal equality, T value) {
        return result(equality, value);
    }
    
    default @Nullable R contains(Result.Sequence.Unordered.Contains contains, T value) {
        return result(contains, value);
    }
    
    default @Nullable R contents(Result.Sequence.Unordered.Contents equality, T value) {
        return result(equality, value);
    }
    
    default @Nullable R each(Result.Sequence.Unordered.Each each, T value) {
        return result(each, value);
    }
    
    default @Nullable R size(Result.Sequence.Size size, T value) {
        return result(size, value);
    }
    

    default @Nullable R constant(Result.Constant constant, T value) {
        return result(constant, value);
    }
    
    default @Nullable R equal(Result.Equal equality, T value) {
        return result(equality, value);
    }

    default @Nullable R not(Result.Not not, T value) {
        return result(not, value);
    }

    default @Nullable R and(Result.And and, T value) {
        return result(and, value);
    }

    default @Nullable R or(Result.Or or, T value) {
        return result(or, value);
    }

    @Nullable R result(Result result, T value);

}
