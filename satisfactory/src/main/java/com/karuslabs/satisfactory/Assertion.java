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

import com.karuslabs.satisfactory.logical.*;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.function.Supplier;

/**
 * Represents an assertion for a syntactical construct in an annotation processing
 * environment.
 * 
 * @param <T> a type which this {@code Assertion} tests
 */
public interface Assertion<T> extends Part {

    boolean test(TypeMirrors types, T value);
    
    String condition();
    
    default String conditions() {
        return condition();
    }
    
    
    default Assertion<T> and(Assertion<T> other) {
        return new And<>(this, other);
    }
    
    default Assertion<T> and(Supplier<Assertion<T>> other) {
        return new And<>(this, other.get());
    }
    
    default Assertion<T> or(Assertion<T> other) {
        return new Or<>(this, other);
    }
    
    default Assertion<T> or(Supplier<Assertion<T>> other) {
        return new Or<>(this, other.get());
    }
    
}
