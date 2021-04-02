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

import java.util.Set;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

import org.junit.jupiter.api.*;

import static com.karuslabs.satisfactory.Assertions.*;
import static com.karuslabs.satisfactory.sequence.Sequences.*;
import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;

class TimesTest {

    Times<Set<Modifier>> times = zero(() -> contains(PUBLIC));
    
    @Test
    void add_true() {
        assertTrue(times.add(null, Set.of(PUBLIC)));
    }
    
    @Test
    void add_false() {
        assertFalse(times.add(null, Set.of(STATIC)));
    }
    
    @Test
    void type() {
        assertEquals(Modifier.class, times.type());
    }
    
}

class ExactTest {
    
    Times<TypeMirror> times = times(1, () -> ANY_TYPE);
    
    @Test
    void test() {
        times.add(null, null);
        
        assertTrue(times.test());
    }
    
    @Test
    void test_false() {
        times.add(null, null);
        times.add(null, null);
        
        assertFalse(times.test());
    }
    
    @Test
    void condition() {
        assertEquals("1 subtype of String", times(1, subtype(String.class)).condition());
        assertEquals("2 subtypes of String", times(2, subtype(String.class)).condition());
    }
    
}

class RangeTest {
    
    Times<TypeMirror> range = range(1, 3, () -> ANY_TYPE);
    
    @Test
    void test_min() {
        range.add(null, null);
        
        assertTrue(range.test());
    }
    
    @Test
    void test_max() {
        range.add(null, null);
        range.add(null, null);
        
        assertTrue(range.test());
    }
    
    @Test
    void test_min_false() {
        assertFalse(range.test());
    }
    
    @Test
    void test_max_false() {
        range.add(null, null);
        range.add(null, null);
        range.add(null, null);
        
        assertFalse(range.test());
    }
    
    @Test
    void condition() {
        assertEquals("1 to 3 subtypes of String", range(1, 3, subtype(String.class)).condition());
    }
    
}

class MinTest {
    
    Times<TypeMirror> min = min(1, () -> ANY_TYPE);
    
    @Test
    void test() {
        min.add(null, null);
        assertTrue(min.test());
    }
    
    @Test
    void test_false() {
        assertFalse(min.test());
    }
    
    @Test
    void condition() {
        assertEquals("1 or more subtypes of String", min(1, subtype(String.class)).condition());
    }
    
}

class MaxTest {
    
    Times<TypeMirror> max = max(2, () -> ANY_TYPE);
    
    @Test
    void test() {
        max.add(null, null);
        max.add(null, null);
        assertTrue(max.test());
    }
    
    @Test
    void test_false() {
        max.add(null, null);
        max.add(null, null);
        max.add(null, null);
        assertFalse(max.test());
    }
    
}
