/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
package com.karuslabs.utilitary;

import com.karuslabs.annotations.Static;

import javax.lang.model.element.Name;

/**
 * Utilities for creating stub {@code Name}s in tests.
 * <br>
 * <b>This class should not be used in production.</b>
 */
public @Static class Names {
    
    /**
     * Creates a {@code Name} from the given type's simple name.
     * 
     * @param type the type
     * @return a {@code Name}
     */
    public static Name of(Class<?> type) {
        return of(type.getSimpleName());
    }
    
    /**
     * Creates a {@code Name} with given value.
     * 
     * @param name the name
     * @return a {@code Name}
     */
    public static Name of(String name) {
        return new MockName(name);
    }
    

}

/**
 * A mock implementation of {@code Name}.
 */
class MockName implements Name {
    
    private final String name;
    
    /**
     * Creates a {@code MockName} with the given value.
     * 
     * @param name the name
     */
    MockName(String name) {
        this.name = name;
    }

    @Override
    public boolean contentEquals(CharSequence sequence) {
        return name.contentEquals(sequence);
    }

    @Override
    public int length() {
        return name.length();
    }

    @Override
    public char charAt(int index) {
        return name.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return name.subSequence(start, end);
    }
    
    @Override
    public String toString() {
        return name;
    }
    
}
