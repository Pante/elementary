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
package com.karuslabs.satisfactory.logical;

import com.karuslabs.satisfactory.old.Assertion;

import java.util.Set;
import javax.lang.model.element.Modifier;

import org.junit.jupiter.api.*;

import static com.karuslabs.satisfactory.old.Assertions.*;
import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;

class NotTest {

    Assertion<Set<Modifier>> assertion = not(contains(PUBLIC));
    
    @Test
    void test() {
        assertTrue(assertion.test(null, Set.of(FINAL)));
    }
    
    @Test
    void supplier_test() {
        assertTrue(not(() -> contains(PUBLIC)).test(null, Set.of(FINAL)));
    }
    
    @Test
    void condition() {
        assertEquals("!(contains [public])", assertion.condition());
    }
    
    @Test
    void part() {
        assertEquals(Modifier.class, assertion.part());
    }
    
}
