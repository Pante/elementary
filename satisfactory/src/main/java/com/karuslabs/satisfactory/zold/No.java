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
package com.karuslabs.satisfactory.zold;

import java.lang.annotation.Annotation;
import javax.lang.model.element.Modifier;

/**
 * Represents the negation of an assertion's arguments.
 * 
 * @param <T> the type of the negated arguments
 */
public class No<T> {
    
    /**
     * The elements of this negation.
     */
    public final T[] elements;
    
    /**
     * Creates a {@code No} with the given elements.
     * 
     * @param elements the elements to be negated
     */
    public No(T... elements) {
        this.elements = elements;
    }
    
    /**
     * Functions as a named parameter for the negation of annotations for an assertion.
     */
    public static class Annotations extends No<Class<? extends Annotation>> {
        /**
         * Creates an {@code Annotations} with the given annotations.
         * 
         * @param annotations the annotations to be negated
         */
        public Annotations(Class<? extends Annotation>... annotations) {
            super(annotations);
        }
    }
    
    /**
     * Functions as a named parameter for the negation of modifiers for an assertion.
     */
    public static class Modifiers extends No<Modifier> {
        /**
         * Creates an {@code Modifiers} with the given modifiers.
         * 
         * @param modifiers the modifiers to be negated
         */
        public Modifiers(Modifier... modifiers) {
            super(modifiers);
        }
    }
    
}
