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

import com.karuslabs.utilitary.type.TypeMirrors;

public class Logical {

    static class And<T> implements Assertion<T> {

        final Assertion<T> left;
        final Assertion<T> right;

        And(Assertion<T> left, Assertion<T> right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public Assertion.AndResult test(TypeMirrors types, T value) {
            var first = left.test(types, value);
            return new Assertion.AndResult(first, first.success ? right.test(types, value) : first.empty());
        }

    }

    static class Or<T> implements Assertion<T> {

        final Assertion<T> left;
        final Assertion<T> right;

        Or(Assertion<T> left, Assertion<T> right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public Assertion.OrResult test(TypeMirrors types, T value) {
            var first = left.test(types, value);
            return new Assertion.OrResult(first, first.success ? first.empty() : right.test(types, value));
        }

    }

    static class Not<T> implements Assertion<T> {

        final Assertion<T> assertion;

        Not(Assertion<T> assertion) {
            this.assertion = assertion;
        }

        @Override
        public Result test(TypeMirrors types, T value) {
            return new Assertion.NegationResult(assertion.test(types, value));
        }

    }
    
}
