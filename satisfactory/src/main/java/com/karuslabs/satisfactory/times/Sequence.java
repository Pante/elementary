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
package com.karuslabs.satisfactory.times;

import com.karuslabs.satisfactory.Assertion;
import com.karuslabs.utilitary.Texts;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.Collection;
import java.util.function.BiConsumer;

public interface Sequence<T> {
    
    static final BiConsumer<Assertion<?>, StringBuilder> FORMAT = (assertion, builder) -> builder.append(assertion.condition());
    

    boolean test(TypeMirrors types, Collection<? extends T> values);
    
    String condition(); 
    
}

class EqualSequence<T> implements Sequence<T> {
    
    private final Assertion<T>[] assertions;
    private final String condition;
    
    EqualSequence(Assertion<T>... assertions) {
        this.assertions = assertions;
        condition = "equal [" + Texts.join(assertions, FORMAT, ", ") + "]";
    }
    
    @Override
    public boolean test(TypeMirrors types, Collection<? extends T> values) {
        if (values.size() != assertions.length) {
            return false;
        }
        
        int i = 0;
        for (var value : values) {
            if (!assertions[i++].test(types, value)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public String condition() {
        return condition;
    }
    
}

class EachSequence<T> implements Sequence<T> {
    
    private final Assertion<T> assertion;
    private final String condition;
    
    EachSequence(Assertion<T> assertion) {
        this.assertion = assertion;
        condition = "each [" + assertion.condition() + "]";
    }

    @Override
    public boolean test(TypeMirrors types, Collection<? extends T> values) {
         for (var value : values) {
            if (!assertion.test(types, value)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public String condition() {
        return condition;
    }
    
}
