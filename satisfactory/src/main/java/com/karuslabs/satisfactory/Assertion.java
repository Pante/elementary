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
package com.karuslabs.satisfactory;

import com.karuslabs.utilitary.type.TypeMirrors;

/**
 * An assertion that elements of Java's abstract syntax tree (AST) must satisfy.
 * Assertions are intended to be a declarative approach to matching elements.
 * By composing primitive assertions, increasingly complex conditions and elements
 * can be asserted, i.e. an assertion may contain other assertions. 
 * 
 * An analogy for assertions is "RegEx for Java's AST".
 * 
 * An assertion provides the {@code test} to assert an element. It returns a {@code Result}
 * which can be inspected to determine exactly which part of an assertion failed.
 * 
 * In most cases, it should not necessary to manually implement {@code Assertion}.
 * Consider using the composable assertions provided out-of-box before implementing
 * a custom assertion.
 * 
 * @param <T> the type of the value to be asserted
 * 
 * @see com.karuslabs.satisfactory.ast
 * @see com.karuslabs.satisfactory.sequence
 */
@FunctionalInterface
public interface Assertion<T> {
    
    /**
     * Returns an assertion that is always true.
     * 
     * @param <T> the type of the value to be asserted
     * @return an assertion that is always true
     */
    static <T> Assertion<T> any() {
        return (value, types) -> Result.TRUE;
    }
    
    /**
     * Returns an assertion that tests if a given value is equal to {@code other}.
     * 
     * @param <T> the type of the value to be asserted
     * @param other the expected value
     * @return an assertion that determines if two values are equal
     */
    static <T> Assertion<T> equal(T other) {
        return (value, types) -> new Result.Equal(value, other, value.equals(other));
    }
    
    /**
     * Returns a logical negation of the given assertion.
     * 
     * @param <T> the type of the value to be asserted
     * @param assertion the assertion to be negated
     * @return a negation of the given assertion
     */
    static <T> Assertion<T> not(Assertion<T> assertion) {
        return Not.of(assertion);
    }
    
    /**
     * Tests if the given value satisfies this assertion. A {@link Result} that 
     * describes the results of this assertion is returned.
     * 
     * @param value the value to be asserted
     * @param types a {@code TypeMirrors} used to facilitate this assertion
     * @return the results of this assertion
     */
    Result test(T value, TypeMirrors types);
    
    /**
     * Returns a composed {@code Assertion} that represents a non-short-circuiting
     * logical AND of this assertion and {@code others}.
     * 
     * @param others the predicates will be logically-ANDed with this predicate
     * @return a composed predicate that represents a non-short-circuiting logical AND
     */
    default Assertion<T> and(Assertion<T>... others) {
        return And.of(this, others);
    }
    
    /**
     * Returns a composed {@code Assertion} that represents a non-short-circuiting
     * logical OR of this assertion and {@code others}.
     * 
     * @param others the predicates will be logically-ORed with this predicate
     * @return a composed predicate that represents a non-short-circuiting logical OR
     */
    default Assertion<T> or(Assertion<T>... others) {
        return Or.of(this, others);
    }
    
}