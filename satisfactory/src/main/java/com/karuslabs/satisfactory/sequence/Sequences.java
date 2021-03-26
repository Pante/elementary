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
package com.karuslabs.satisfactory.sequence;

import com.karuslabs.satisfactory.Assertion;

import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class Sequences {
    
    public static final Sequence<VariableElement> ANY_PARAMETERS = new AnySequence<>(VariableElement.class);
    public static final Sequence<TypeMirror> ANY_EXCEPTIONS = new AnySequence<>(TypeMirror.class);
    
    public static final Sequence<VariableElement> NO_PARAMETERS = new NoSequence<>(VariableElement.class);
    public static final Sequence<TypeMirror> NO_EXCEPTIONS = new NoSequence<>(TypeMirror.class);
    
    
    public static Sequence<VariableElement> parameters(Sequence<VariableElement> sequence) {
        return sequence;
    }
    
    public static Sequence<TypeMirror> exceptions(Sequence<TypeMirror> sequence) {
        return sequence;
    }
    
    
    public static <T> Sequence<T> equal(Supplier<Assertion<T>>... assertions) {
        var values = new Assertion<?>[] {};
        for (var supplier : assertions) {
            supplier.get()
        }
        
        return equal()
    }
    
    public static <T> Sequence<T> equal(Assertion<T>... assertions) {
        return new EqualSequence<>(assertions);
    }
    
    public static <T> Sequence<T> each(Assertion<T> assertion) {
        return new EachSequence<>(assertion);
    }
    
    
    public static <T> Sequence<T> contains(Times<T>... times) {
        return new ContainsSequence<>(times);
    }
    
    public static <T> Sequence<T> equal(Times<T>... times) {
        return new EqualTimeSequence<>(times);
    }
    
    
    public static <T> Times<T> zero(Assertion<T> assertion) {
        return new Exact(0, assertion);
    }
    
    public static <T> Times<T> times(int times, Assertion<T> assertion) {
        return new Exact(times, assertion);
    }
    
    public static <T> Times<T> range(int min, int max, Assertion<T> assertion) {
        return new Range<>(min, max, assertion);
    }
    
    public static <T> Times<T> min(int min, Assertion<T> assertion) {
        return new Min<>(min, assertion);
    }
    
    public static <T> Times<T> max(int max, Assertion<T> assertion) {
        return new Max<>(max, assertion);
    }
    
}
