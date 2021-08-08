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

import com.karuslabs.satisfactory.old.Assertion;
import com.karuslabs.utilitary.type.TypeMirrors;

/**
 * An assertion that negates another assertion.
 * 
 * @param <T> the type of the tested value
 */
public final class Not<T> implements Assertion<T> {
    
    /**
     * The assertion to be negated.
     */
    public final Assertion<T> assertion;
    
    /**
     * Creates a {@code Not} with the given assertion.
     * 
     * @param assertion the assertion to be negated
     */
    public Not(Assertion<T> assertion) {
        this.assertion = assertion;
    }

    @Override
    public boolean test(TypeMirrors types, T value) {
        return !assertion.test(types, value);
    }

    @Override
    public String condition() {
        return "!(" + assertion.condition() + ")";
    }
    
    @Override
    public Class<?> part() {
        return assertion.part();
    }

}
