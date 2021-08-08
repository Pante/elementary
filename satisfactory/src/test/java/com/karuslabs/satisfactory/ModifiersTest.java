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

import com.karuslabs.satisfactory.old.Assertions;
import com.karuslabs.satisfactory.old.Assertion;
import java.util.Set;
import javax.lang.model.element.Modifier;

import org.junit.jupiter.api.*;

import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;

class ModifiersTest {

    Assertion<Set<Modifier>> contains = Assertions.contains(PUBLIC, FINAL);
    Assertion<Set<Modifier>> equal = Assertions.equal(PUBLIC, FINAL);
    
    @Test
    void part() {
        assertEquals(Modifier.class, contains.part());
    }
    
    
    @Test
    void contains_true() {
        assertTrue(contains.test(null, Set.of(FINAL, NATIVE, PUBLIC)));
    }
    
    @Test
    void contains_false() {
        assertFalse(contains.test(null, Set.of(PUBLIC)));
    }
    
    @Test
    void contains_condition() {
        assertEquals("contains [public final]", contains.condition());
        assertEquals("contains [public final]", contains.conditions());
    }
    
    
    @Test
    void equal_true() {
        assertTrue(equal.test(null, Set.of(PUBLIC, FINAL)));
    }
    
    @Test
    void equal_false() {
        assertFalse(equal.test(null, Set.of(PUBLIC, STATIC, FINAL)));
    }
    
    @Test
    void equal_condition() {
        assertEquals("equal [public final]", equal.condition());
        assertEquals("equal [public final]", equal.conditions());
    }
    
}
