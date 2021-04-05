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

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents an assertion for a language construct in an annotation processing
 * environment.
 * 
 * @param <T> a type which this {@code Assertion} tests
 */
public interface Assertion<T> extends Part {
    
    /**
     * Tests the given value using the given types.
     * 
     * @param types the types
     * @param value the value
     * @return {@code true} if the given value satisfies this assertion
     */
    boolean test(TypeMirrors types, @Nullable T value);
    
    /**
     * @return the condition for satisfying this assertion
     */
    String condition();
    
    /**
     * <b>Default implementation:</b>
     * Delegates execution to {@link #condition()}.
     * 
     * @return the conditions for satisfying this assertion
     */
    default String conditions() {
        return condition();
    }
    
    /**
     * Returns a composed assertion that represents a short-circuiting logical
     * AND of this assertion and {@code other}.
     * 
     * @param other the other assertion
     * @return a composed assertion that represents the short-circuiting logical 
     *         AND of this assertion and the other assertion
     */
    default Assertion<T> and(Assertion<T> other) {
        return new And<>(this, other);
    }
    
    /**
     * Returns a composed assertion that represents a short-circuiting logical
     * AND of this assertion and the assertion supplied by {@code other}.
     * 
     * @param other the supplier of the other assertion
     * @return a composed assertion that represents the short-circuiting logical 
     *         AND of this assertion and the supplied assertion
     */
    default Assertion<T> and(Supplier<Assertion<T>> other) {
        return new And<>(this, other.get());
    }
    
    /**
     * Returns a composed assertion that represents a short-circuiting logical OR
     * of this assertion and {@code other}.
     * 
     * @param other the other assertion
     * @return a composed assertion that represents the short-circuiting logical 
     *         OR of this assertion and the other assertion
     */
    default Assertion<T> or(Assertion<T> other) {
        return new Or<>(this, other);
    }
    
    /**
     * Returns a composed assertion that represents a short-circuiting logical
     * OR of this assertion and the assertion supplied by {@code other}.
     * 
     * @param other the supplier of the other assertion
     * @return a composed assertion that represents the short-circuiting logical 
     *         OR of this assertion and the supplied assertion
     */
    default Assertion<T> or(Supplier<Assertion<T>> other) {
        return new Or<>(this, other.get());
    }
    
}
