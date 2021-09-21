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

import com.karuslabs.satisfactory.zold.Times;
import java.util.Set;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

import org.junit.jupiter.api.*;

import static com.karuslabs.satisfactory.zold.Assertions.*;
import static com.karuslabs.satisfactory.zold.Sequences.*;
import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;

class TimesTest {

    Times<Set<Modifier>> times = zero(() -> contains(PUBLIC));
    
    @Test
    void test_true() {
        assertTrue(times.test(null, Set.of(PUBLIC)));
    }
    
    @Test
    void test_false() {
        assertFalse(times.test(null, Set.of(STATIC)));
    }
    
    @Test
    void part() {
        assertEquals(Modifier.class, times.part());
    }
    
}

class ExactTest {
    
    Times<TypeMirror> times = times(1, () -> ANY_TYPE);
    
    @Test
    void times_true() {
        times.test(null, null);
        
        assertTrue(times.times());
    }
    
    @Test
    void times_false() {
        times.test(null, null);
        times.test(null, null);
        
        assertFalse(times.times());
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
    void times_min() {
        range.test(null, null);
        
        assertTrue(range.times());
    }
    
    @Test
    void times_max() {
        range.test(null, null);
        range.test(null, null);
        
        assertTrue(range.times());
    }
    
    @Test
    void times_min_false() {
        assertFalse(range.times());
    }
    
    @Test
    void times_max_false() {
        range.test(null, null);
        range.test(null, null);
        range.test(null, null);
        
        assertFalse(range.times());
    }
    
    @Test
    void condition() {
        assertEquals("1 to 3 subtypes of String", range(1, 3, subtype(String.class)).condition());
    }
    
}

class MinTest {
    
    Times<TypeMirror> min = min(1, () -> ANY_TYPE);
    
    @Test
    void times() {
        min.test(null, null);
        assertTrue(min.times());
    }
    
    @Test
    void times_false() {
        assertFalse(min.times());
    }
    
    @Test
    void condition() {
        assertEquals("1 or more subtypes of String", min(1, subtype(String.class)).condition());
    }
    
}

class MaxTest {
    
    Times<TypeMirror> max = max(2, () -> ANY_TYPE);
    
    @Test
    void times() {
        max.test(null, null);
        max.test(null, null);
        assertTrue(max.times());
    }
    
    @Test
    void times_false() {
        max.test(null, null);
        max.test(null, null);
        max.test(null, null);
        assertFalse(max.times());
    }
    
}
