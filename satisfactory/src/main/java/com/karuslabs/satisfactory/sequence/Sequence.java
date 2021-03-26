
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

import com.karuslabs.satisfactory.*;
import com.karuslabs.utilitary.Texts;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.Collection;
import java.util.function.BiConsumer;

public abstract class Sequence<T> implements Part {
    
    static final BiConsumer<Assertion<?>, StringBuilder> FORMAT = (assertion, builder) -> builder.append(assertion.condition());
    
    private final String condition;
    
    public Sequence(String condition) {
        this.condition = condition;
    }
    

    public abstract boolean test(TypeMirrors types, Collection<? extends T> values);
    
    public String condition() {
        return condition;
    }
    
}

class EqualSequence<T> extends Sequence<T> {
    
    static String format(Assertion<?>... assertions) {
        return Texts.join(assertions, (assertion, builder) -> builder.append('[').append(assertion.condition()).append(']'), ", ");
    }
    
    private final Assertion<T>[] assertions;
    
    EqualSequence(Assertion<T>... assertions) {
        super("equal " + format(assertions));
        this.assertions = assertions;
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
    public Class<?> type() {
        return assertions[0].type();
    }
    
}

class EachSequence<T> extends Sequence<T> {
    
    private final Assertion<T> assertion;
    
    EachSequence(Assertion<T> assertion) {
        super("each [" + assertion.condition() + "]");
        this.assertion = assertion;
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
    public Class<?> type() {
        return assertion.type();
    }
    
}

class NoSequence<T> extends Sequence<T> {
    
    private final Class<?> type;
    
    NoSequence(Class<?> type) {
        super("empty");
        this.type = type;
    }

    @Override
    public boolean test(TypeMirrors types, Collection<? extends T> values) {
        return values.isEmpty();
    }

    @Override
    public Class<?> type() {
        return type;
    }
    
}
