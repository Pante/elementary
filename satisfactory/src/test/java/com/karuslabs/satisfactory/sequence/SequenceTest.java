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

import java.util.*;
import javax.lang.model.element.*;

import org.junit.jupiter.api.*;

import static com.karuslabs.satisfactory.Assertions.*;
import static com.karuslabs.satisfactory.sequence.Sequences.*;
import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EqualSequenceTest {

    Assertion<Set<Modifier>> first = contains(STATIC);
    Assertion<Set<Modifier>> second = contains(FINAL);
    Sequence<Set<Modifier>> sequence = equal(() -> first, () -> second);
    
    @Test
    void test() {
        assertTrue(sequence.test(null, List.of(Set.of(STATIC, FINAL), Set.of(PUBLIC, STATIC, FINAL))));
    }
    
    @Test
    void test_size() {
        assertFalse(sequence.test(null, List.of(Set.of(STATIC, FINAL), Set.of(PUBLIC, STATIC, FINAL), Set.of(ABSTRACT, STATIC, FINAL))));
    }
    
    @Test
    void test_false() {
        assertFalse(sequence.test(null, List.of(Set.of(STATIC, FINAL), Set.of(STATIC))));
    }
    
    @Test
    void condition() {
        assertEquals("equal [contains [static], contains [final]]", sequence.condition());
    }
    
    @Test
    void type() {
        assertEquals(Modifier.class, sequence.type());
    }
    
}

class EachSequenceTest {
    
    Sequence<Set<Modifier>> sequence = each(() -> contains(STATIC));
    
    @Test
    void test() {
        assertTrue(sequence.test(null, List.of(Set.of(STATIC), Set.of(FINAL, STATIC))));
    }
    
    @Test
    void test_false() {
        assertFalse(sequence.test(null, List.of(Set.of(STATIC), Set.of(STATIC, FINAL), Set.of(ABSTRACT))));
    }
    
    @Test
    void condition() {
        assertEquals("each contains [static]", sequence.condition());
    }
    
    @Test
    void type() {
        assertEquals(Modifier.class, sequence.type());
    }
    
}

class NoSequenceTest {
    
    @Test
    void test() {
        assertTrue(NO_PARAMETERS.test(null, List.of()));
    }
    
    @Test
    void test_false() {
        assertFalse(NO_PARAMETERS.test(null, List.of(mock(VariableElement.class))));
    }
    
    @Test
    void condition() {
        assertEquals("empty", NO_PARAMETERS.condition());
    }
    
    @Test
    void type() {
        assertEquals(VariableElement.class, NO_PARAMETERS.type());
    }
    
}
