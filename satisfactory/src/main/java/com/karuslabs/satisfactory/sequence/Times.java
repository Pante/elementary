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

import com.karuslabs.satisfactory.Part;
import com.karuslabs.satisfactory.*;
import com.karuslabs.utilitary.type.TypeMirrors;

/**
 * Represents the expected number of times an assertion needs to be satisfied.
 * 
 * @param <T> the type of the tested value
 */
public abstract class Times<T> implements Part {

    private final Assertion<T> assertion;
    private final String condition;
    private int current;
    
    /**
     * Creates a {@code Times} with the given assertion and condition.
     * 
     * @param assertion the assertion
     * @param condition the condition for satisfying this {@code Times}
     */
    public Times(Assertion<T> assertion, String condition) {
        this.assertion = assertion;
        this.condition = condition;
    }
    
    /**
     * Tests whether the current number of times is the expected number of times
     * an assertion needs to be satisfied.
     * 
     * @return {@code true} if the given current number of times is the expected
     *         number of times
     */
    public boolean test() {
        var valid = test(current);
        current = 0;
        return valid;
    }
    
    /**
     * Tests whether the given number is the expected number of times an assertion
     * needs to be satisfied.
     * 
     * @param current the current number of times the assertion is satisfied 
     * @return {@code true} if the given count is the expected number of times
     */
    protected abstract boolean test(int current);
    
    /**
     * Tests whether the given value satisfies the assertion using the given types 
     * and increases an internal counter if so.
     * 
     * @param types the types
     * @param value the value to be tested
     * @return {@code true} if the given value satisfies the assertion
     */
    public boolean add(TypeMirrors types, T value) {
        if (assertion.test(types, value)) {
            current++;
            return true;
            
        } else {
            return false;
        }
    }
    
    /**
     * Returns the condition for satisfying this {@code Times}.
     * 
     * @return the condition for satisfying this {@code Times}
     */
    public String condition() {
        return condition;
    }
    
    @Override
    public Class<?> type() {
        return assertion.type();
    }
    
}

/**
 * Represents an exact number of times that an assertion needs to be satisfied.
 * 
 * @param <T> the type of the assertion
 */
class Exact<T> extends Times<T> {

    /**
     * Formats the given assertion and times.
     * 
     * @param times the times
     * @param assertion the assertion
     * @return a formatted string representation of the given assertion
     */
    static String format(int times, Assertion<?> assertion) {
        if (times == 1) {
            return "1 " + assertion.condition();
            
        } else {
            return times + " " + assertion.conditions();
        }
    }
    
    private final int times;
    
    /**
     * Creates a {@code Exact} with the given times and assertion.
     * 
     * @param times the expected number of times
     * @param assertion the assertion to satisfy
     */
    public Exact(int times, Assertion<T> assertion) {
        super(assertion, format(times, assertion));
        this.times = times;
    }

    @Override
    protected boolean test(int current) {
        return times == current;
    }
    
}

/**
 * A range of times that an assertion needs to be satisfied.
 * 
 * @param <T> the type of the tested value
 */
class Range<T> extends Times<T> {
    
    private final int min;
    private final int max;
    
    /**
     * Creates a {@code Range} with the given min, max and assertion.
     * 
     * @param min the minimum number of times, inclusive
     * @param max the maximum number of times, exclusive
     * @param assertion the assertion to satisfy
     */
    Range(int min, int max, Assertion<T> assertion) {
        super(assertion, min + " to " + max + " " + assertion.conditions());
        this.min = min;
        this.max = max;
    }

    @Override
    protected boolean test(int current) {
        return current >= min && current < max;
    }
    
}

/**
 * A minimum number of times that an assertion needs to be satisfied.
 * 
 * @param <T> the type of the tested value
 */
class Min<T> extends Times<T> {
    
    private final int min;
    
    /**
     * Creates a {@code Min} with the given minimum and assertion.
     * 
     * @param min the minimum number of times, inclusive
     * @param assertion the assertion to satisfy
     */
    Min(int min, Assertion<T> assertion) {
        super(assertion, min + " or more " + assertion.conditions());
        this.min = min;
    }

    @Override
    protected boolean test(int current) {
        return current >= min;
    }
    
}

/**
 * A maximum number of times that an assertion need to be satisfied.
 * 
 * @param <T> the type of the tested value
 */
class Max<T> extends Times<T> {
    
    private final int max;
    
    /**
     * Creates a {@code Max} with the given maximum and assertion.
     * 
     * @param max the maximum number of times, inclusive
     * @param assertion the assertion to satisfy
     */
    Max(int max, Assertion<T> assertion) {
        super(assertion, max + " or less " + assertion.conditions());
        this.max = max;
    }

    @Override
    protected boolean test(int current) {
        return current <= max;
    }
    
}
