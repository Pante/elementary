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
    
    default @Nullable R type(AST.Type type, T value) {
        return result(type, value);
    }

    default @Nullable R primitive(AST.Primitive primitive, T value) {
        return result(primitive, value);
    }

    
    default @Nullable R pattern(Result.Sequence.Pattern pattern, T value) {
        return result(pattern, value);
    }
    
    default @Nullable R equality(Result.Sequence.Equality equality, T value) {
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
