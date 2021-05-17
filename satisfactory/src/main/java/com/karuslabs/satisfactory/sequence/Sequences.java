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

import com.karuslabs.annotations.Static;
import com.karuslabs.satisfactory.Assertion;

import java.util.function.Supplier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Provides constants for frequently used assertion and utility methods 
 * for creating {@code Assertion}s.
 */
public @Static class Sequences {
    
    /**
     * A {@code Sequence} that is satisfied by any collection of {@code VariableElement}s.
     */
    public static final Sequence<VariableElement> ANY_PARAMETERS = new AnySequence<>(VariableElement.class);
    /**
     * A {@code Sequence} that is satisfied by any collection of {@code TypeMirror}s.
     */
    public static final Sequence<TypeMirror> ANY_EXCEPTIONS = new AnySequence<>(TypeMirror.class);
    /**
     * A {@code Sequence} that is satisfied by an empty collection of {@code VariableElement}s.
     */
    public static final Sequence<VariableElement> NO_PARAMETERS = new NoSequence<>(VariableElement.class);
    /**
     * A {@code Sequence} that is satisfied by an empty collection of {@code TypeMirror}s.
     */
    public static final Sequence<TypeMirror> NO_EXCEPTIONS = new NoSequence<>(TypeMirror.class);
    
    
    /**
     * Functions as a named parameter for a sequence of {@code VariableElement}s.
     * 
     * @param sequence a sequence of {@code VariableElement}s
     * @return the given sequence
     */
    public static Sequence<VariableElement> parameters(Sequence<VariableElement> sequence) {
        return sequence;
    }
    
    /**
     * Functions as a named parameter for a sequence of {@code TypeMirror}s.
     * 
     * @param sequence a sequence of {@code TypeMirror}s
     * @return the given sequence
     */
    public static Sequence<TypeMirror> exceptions(Sequence<TypeMirror> sequence) {
        return sequence;
    }
    
    
    /**
     * Returns a {@code Sequence} that is satisfied if a collection of elements and
     * the supplied assertions are equal and similarly ordered. 
     * <br><br>
     * <b>Results may be inconsistent between invocations if the collection is unordered.</b>
     * 
     * @param <T> the type of the tested values
     * @param assertions the supplier of assertions to satisfy
     * @return a sequence that is satisfied if a collection of elements is equal to
     *         the supplied assertions
     */
    public static <T> Sequence<T> equal(Supplier<? extends Assertion<T>>... assertions) {
        var supplied = new Assertion[assertions.length];
        for (int i = 0; i < assertions.length; i++) {
            supplied[i] = assertions[i].get();
        }
        
        return equal(supplied);
    }
    
    /**
     * Returns a {@code Sequence} that is satisfied if a collection of elements and
     * given assertions are equal and similarly ordered.
     * <br><br>
     * <b>Results may be inconsistent between invocations if the collection is unordered.</b>
     * 
     * @param <T> the type of the tested values
     * @param assertions the assertions to satisfy
     * @return a sequence that is satisfied if a collection of elements is equal to
     *         the given assertions
     */
    public static <T> Sequence<T> equal(Assertion<T>... assertions) {
        return new EqualSequence<>(assertions);
    }
    
    /**
     * Returns a {@code Sequence} that is satisfied if a collection of elements each
     * satisfies the supplied assertion.
     * 
     * @param <T> the type of the tested values
     * @param assertion the supplier of the assertion to satisfy
     * @return a sequence that is satisfied if each element in a collection satisfies
     *         the supplied assertion
     */
    public static <T> Sequence<T> each(Supplier<? extends Assertion<T>> assertion) {
        return each(assertion.get());
    }
    
    /**
     * Returns a {@code Sequence} that is satisfied if each value in a collection
     * satisfies the given assertion.
     * 
     * @param <T> the type of the tested values
     * @param assertion the assertion to satisfy
     * @return a sequence that is satisfied if a collection of elements each satisfies
     *         the given assertion
     */
    public static <T> Sequence<T> each(Assertion<T> assertion) {
        return new EachSequence<>(assertion);
    }
    
    
    /**
     * Returns a {@code Sequence} that is satisfied if a collection of values contains 
     * all the given {@code Times}.
     * 
     * @param <T> the type of the tested values
     * @param times the times to satisfy
     * @return a sequence that is satisfied if a collection of values contains all
     *         the given {@code Times}
     */
    public static <T> Sequence<T> contains(Times<T>... times) {
        return new ContainsSequence<>(times);
    }
    
    /**
     * Returns a {@code Sequence} that is satisfied if a collection of values is
     * equal to the given {@code Times}. The collection of values's order is ignored.
     * 
     * @param <T> the type of the tested values
     * @param times the times to satisfy
     * @return a sequence that is satisfied if a collection of values is equal to
     *         the given {@code Times}
     */
    public static <T> Sequence<T> equal(Times<T>... times) {
        return new EqualTimeSequence<>(times);
    }
    
    
    /**
     * Returns a {@code Times} that expect the supplied assertion to be satisfied
     * exactly zero times.
     * 
     * @param <T> the type of the tested value
     * @param assertion the supplier of the assertion to satisfy
     * @return a {@code Times} that expects the supplied assertion to be satisfied
     *         exactly zero times
     */
    public static <T> Times<T> zero(Supplier<? extends Assertion<T>> assertion) {
        return zero(assertion.get());
    }
    
    /**
     * Returns a {@code Times} that expects thee given assertion to be satisfied
     * exactly zero times.
     * 
     * @param <T> the type of the tested value
     * @param assertion the assertion to satisfy
     * @return a {@code Times} that expects the given assertion to be satisfied
     *         exactly zero times
     */
    public static <T> Times<T> zero(Assertion<T> assertion) {
        return new Exact(0, assertion);
    }
    
    /**
     * Returns a {@code Times} that expects the supplied assertion to be satisfied 
     * a given number of times.
     * 
     * @param <T> the type of the tested value
     * @param times the expected number of times that the supplied assertion is satisfied
     * @param assertion the supplier of the assertion to satisfy
     * @return a {@code Times} that expects the supplied assertion to be satisfied
     *         the given number of times
     */
    public static <T> Times<T> times(int times, Supplier<? extends Assertion<T>> assertion) {
        return times(times, assertion.get());
    }
    
    /**
     * Returns a {@code Times} that expects the given assertion to be satisfied 
     * a given number of times.
     * 
     * @param <T> the type of the tested value
     * @param times the expected number of times that the given assertion is satisfied
     * @param assertion the assertion to satisfy
     * @return a {@code Times} that expects the given assertion to be satisfied
     *         the given number of times
     */
    public static <T> Times<T> times(int times, Assertion<T> assertion) {
        return new Exact(times, assertion);
    }
    
    /**
     * Returns a {@code Times} that represents the expected range of times the supplied 
     * assertion is to be satisfied.
     * 
     * @param <T> the type of the tested value
     * @param min the minimum, inclusive
     * @param max the maximum, exclusive
     * @param assertion the supplier of the assertion to satisfy
     * @return a {@code Times} that represents a range of times the supplied assertion
     *         is expected to be satisfied
     */
    public static <T> Times<T> range(int min, int max, Supplier<? extends Assertion<T>> assertion) {
        return new Range<>(min, max, assertion.get());
    }
    
    /**
     * Returns a {@code Times} that represents the expected range of times the given 
     * assertion is expected to be satisfied.
     * 
     * @param <T> the type of the tested value
     * @param min the minimum, inclusive
     * @param max the maximum, exclusive
     * @param assertion the assertion to satisfy
     * @return a {@code Times} that represents a range of times the given assertion
     *         is expected to be satisfied
     */
    public static <T> Times<T> range(int min, int max, Assertion<T> assertion) {
        return new Range<>(min, max, assertion);
    }
    
    /**
     * Returns a {@code Times} that expects the supplied assertion to be satisfied
     * the given minimum number of times.
     * 
     * @param <T> the type of the test value
     * @param min the minimum, inclusive
     * @param assertion the supplier of the assertion to satisfy
     * @return a {@code Times} that expects the supplied assertion to be satisfied
     *         a minimum number of times
     */
    public static <T> Times<T> min(int min, Supplier<? extends Assertion<T>> assertion) {
        return new Min<>(min, assertion.get());
    }
    
    /**
     * Returns a {@code Times} that expects the given assertion to be satisfied
     * the given minimum number of times.
     * 
     * @param <T> the type of the test value
     * @param min the minimum, inclusive
     * @param assertion the assertion to satisfy
     * @return a {@code Times} that expects the supplied assertion to be satisfied
     *         a minimum number of times
     */
    public static <T> Times<T> min(int min, Assertion<T> assertion) {
        return new Min<>(min, assertion);
    }
    
    /**
     * Returns a {@code Times} that expects the supplied assertion to be satisfied
     * the given maximum number of times.
     * 
     * @param <T> the type of the test value
     * @param max the maximum, inclusive
     * @param assertion the supplier of the assertion to satisfy
     * @return a {@code Times} that expects the supplied assertion to be satisfied
     *         a maximum number of times
     */
    public static <T> Times<T> max(int max, Supplier<? extends Assertion<T>> assertion) {
        return new Max<>(max, assertion.get());
    }
    
    /**
     * Returns a {@code Times} that expects the given assertion to be satisfied
     * the given maximum number of times.
     * 
     * @param <T> the type of the test value
     * @param max the maximum, inclusive
     * @param assertion the assertion to satisfy
     * @return a {@code Times} that expects the given assertion to be satisfied
     *         a maximum number of times
     */
    public static <T> Times<T> max(int max, Assertion<T> assertion) {
        return new Max<>(max, assertion);
    }
    
}
