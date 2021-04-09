
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
 * Represents the criteria for a sequences of elements.
 * 
 * @param <T> the type of the elements
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
     * Tests the given values using the given types. 
     * 
     * @param types the types
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
 * A {@code Sequence} that is satisfied if a sequence of elements is equal to and ordered
 * according to this sequence of {@code Assertion}s. Results may be inconsistent between
 * runs if the collection is unordered.
 * 
 * @param <T> the type of the elements
 */
class EqualSequence<T> extends Sequence<T> {
    
    /**
     * Formats the given assertions.
     * 
     * @param assertions the assertions
     * @return a formatted string representation of the given assertions
     */
    static String format(Assertion<?>... assertions) {
        return "[" + Texts.join(assertions, (assertion, builder) -> builder.append(assertion.condition()), ", ") + "]";
    }
    
    private final Assertion<T>[] assertions;
    
    /**
     * Creates a {@code EqualSequence} with the given assertions.
     * 
     * @param assertions the assertions
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
    public Class<?> type() {
        return assertions[0].type();
    }
    
}

/**
 * A {@code Sequence} that is satisfied if a sequence of elements each satisfies an
 * {@code Assertion}.
 * 
 * @param <T> the type of the elements
 */
class EachSequence<T> extends Sequence<T> {
    
    private final Assertion<T> assertion;
    
    /**
     * Creates a {@code EachSequence} with the given assertion.
     * 
     * @param assertion the assertion
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
    public Class<?> type() {
        return assertion.type();
    }
    
}

/**
 * A {@code Sequence} that is satisfied if a sequence of elements is empty.
 * 
 * @param <T> the type of the elements
 */
class NoSequence<T> extends Sequence<T> {
    
    private final Class<?> type;
    
    /**
     * Creates a {@code NoSequence} of the given type.
     * 
     * @param type the type
     */
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
