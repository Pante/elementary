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
package com.karuslabs.satisfactory.logical;

import com.karuslabs.satisfactory.*;
import com.karuslabs.utilitary.type.TypeMirrors;

/**
 * An assertion that represents a short-circuiting logical OR.
 * 
 * @param <T> the type of the tested value
 */
public final class Or<T> implements Assertion<T> {

    /**
     * The assertion on the left side of the logical OR.
     */
    public final Assertion<T> left;
    /**
     * The assertion on the right side of the logical OR.
     */
    public final Assertion<T> right;
    
    /**
     * Creates a {@code OR} with the given assertions for the left and right sides.
     * 
     * @param left the assertion on the left side
     * @param right the assertion on the right side
     */
    public Or(Assertion<T> left, Assertion<T> right) {
        this.left = left;
        this.right = right;
    }
    
    @Override
    public boolean test(TypeMirrors types, T value) {
        return left.test(types, value) || right.test(types, value);
    }

    @Override
    public String condition() {
        return left.condition() + " | " + right.condition();
    }
    
    @Override
    public Class<?> type() {
        return left.type();
    }
    
}
