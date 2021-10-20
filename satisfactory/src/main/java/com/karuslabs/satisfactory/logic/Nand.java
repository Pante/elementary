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
package com.karuslabs.satisfactory.logic;

import com.karuslabs.satisfactory.a.Result;
import com.karuslabs.satisfactory.a.Failure;
import com.karuslabs.satisfactory.*;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.stream.Stream;

import static com.karuslabs.satisfactory.a.Result.SUCCESS;
import static com.karuslabs.satisfactory.logic.Operator.NAND;

record Nand<T>(Assertion<T>... assertions) implements Assertion<T> {

    @Override
    public Result test(T value, TypeMirrors types) {
        for (var assertion : assertions) {
            if (assertion.test(value, types) instanceof Failure) {
                return SUCCESS;
            }
        }
        
        return fail(value, types);
    }

    @Override
    public Failure.Logical fail(T value, TypeMirrors types) {
        return new Failure.Logical(NAND, Stream.of(assertions).map(assertion -> assertion.fail(value, types)).toList());
    }

}
