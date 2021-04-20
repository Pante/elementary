
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

/**
 * Represents a criteria that a collection of values should satisfy.
 * 
 * @param <T> the type of the tested values
 */
public abstract class Sequence<T> implements Part {
    
    private final String condition;
    
    /**
     * Creates a {@code Sequence} with the given condition.
     * 
     * @param condition the condition for satisfying this {@code Sequence}
     */
    public Sequence(String condition) {
        this.condition = condition;
    }
    

    /**
     * Tests the given values using the given {@code TypeMirrors}. 
     * 
     * @param types the {@code TypeMirror}s.
     * @param values the values
     * @return {@code true} if the given values satisfies this sequence of assertions
     */
    public abstract boolean test(TypeMirrors types, Collection<? extends T> values);
    
    /**
     * Returns the condition for satisfying this sequence.
     * 
     * @return the condition for satisfying this sequence
     */
    public String condition() {
        return condition;
    }
    
}

/**
 * A {@code Sequence} that expects a collection of values and {@code Assertion}s
 * to be equal and ordered. 
 * <br><br>
 * <b>Results may be inconsistent between invocations if the 
 * the sequence of elements is unordered.</b>
 * 
 * @param <T> the type of the tested values
 */
class EqualSequence<T> extends Sequence<T> {
    
    /**
     * Formats the given assertions.
     * 
     * @param assertions the assertions
     * @return a formatted description of the given assertions
     */
    static String format(Assertion<?>... assertions) {
        return "[" + Texts.join(assertions, (assertion, builder) -> builder.append(assertion.condition()), ", ") + "]";
    }
    
    private final Assertion<T>[] assertions;
    
    /**
     * Creates a {@code EqualSequence} with the given assertions.
     * 
     * @param assertions the assertions to which a collection of values should be equal
     */
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
    public Class<?> part() {
        return assertions[0].part();
    }
    
}

/**
 * A {@code Sequence} that expects a collection of values to each satisfy an
 * {@code Assertion}.
 * 
 * @param <T> the type of the values
 */
class EachSequence<T> extends Sequence<T> {
    
    private final Assertion<T> assertion;
    
    /**
     * Creates a {@code EachSequence} with the given assertion.
     * 
     * @param assertion the assertion that a collection of values should all satisfy
     */
    EachSequence(Assertion<T> assertion) {
        super("each " + assertion.condition());
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
    public Class<?> part() {
        return assertion.part();
    }
    
}

/**
 * A {@code Sequence} that expects an empty collection of values.
 * 
 * @param <T> the type of the values
 */
class NoSequence<T> extends Sequence<T> {
    
    private final Class<?> part;
    
    /**
     * Creates a {@code NoSequence} for the given part.
     * 
     * @param part the part
     */
    NoSequence(Class<?> part) {
        super("empty");
        this.part = part;
    }

    @Override
    public boolean test(TypeMirrors types, Collection<? extends T> values) {
        return values.isEmpty();
    }

    @Override
    public Class<?> part() {
        return part;
    }
    
}
