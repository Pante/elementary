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

import com.karuslabs.satisfactory.zold.Assertions;
import com.karuslabs.satisfactory.old.Assertion;
import java.util.Set;
import javax.lang.model.element.Modifier;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static javax.lang.model.element.Modifier.*;

class AssertionTest {

    Assertion<Set<Modifier>> first = Assertions.contains(PUBLIC);
    Assertion<Set<Modifier>> second = Assertions.contains(FINAL);
    
    @Test
    void and_assertion() {
        assertTrue(first.and(second).test(null, Set.of(PUBLIC, FINAL)));
    }
    
    @Test
    void and_supplier() {
        assertTrue(first.and(() -> second).test(null, Set.of(PUBLIC, FINAL)));
    }
    
    @Test
    void or_assertion() {
        assertTrue(first.or(second).test(null, Set.of(PUBLIC, PRIVATE)));
    }
    
    @Test
    void or_supplier() {
        assertTrue(first.or(() -> second).test(null, Set.of(PUBLIC, PRIVATE)));
    }
    
}
