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

import com.karuslabs.elementary.junit.*;
import com.karuslabs.elementary.junit.annotations.*;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.*;
import javax.lang.model.type.TypeMirror;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.karuslabs.satisfactory.Assertions.*;
import static com.karuslabs.satisfactory.sequence.Sequences.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ToolsExtension.class)
@Introspect
class TimesSequenceTest {
    
    Sequence<TypeMirror> contains = contains(times(1, subtype(String.class)), max(3, type(Integer.class)));
    Sequence<TypeMirror> equal = equal(times(1, subtype(String.class)), max(3, type(Integer.class)));
    TypeMirrors types = Tools.typeMirrors();
    Cases cases = Tools.cases();
    TypeMirror string = cases.get(0).asType();
    TypeMirror integer = cases.get(1).asType();
    TypeMirror uuid = cases.get(2).asType();
    
    
    @Test
    void contains_test() {
        assertTrue(contains.test(types, List.of(integer, string, integer, uuid)));
    }
    
    @Test
    void contains_test_false() {
        assertFalse(contains.test(types, List.of(string, integer, integer, integer, integer)));
    }
    
    @Test
    void contains_condition() {
        assertEquals("contains [1 subtype of String, 3 or less Integers]", contains.condition());
    }
    
    @Test
    void contains_part() {
        assertEquals(TypeMirror.class, contains.part());
    }
    
    @Test
    void equal_test() {
        assertTrue(equal.test(types, List.of(integer, string, integer)));
    }
    
    @Test
    void equal_test_false() {
        assertFalse(equal.test(types, List.of(string, integer, integer, uuid)));
    }
    
    @Test
    void equal_condition() {
        assertEquals("equal [1 subtype of String, 3 or less Integers]", equal.condition());
    }
    
    @Test
    void equal_part() {
        assertEquals(TypeMirror.class, equal.part());
    }
    
    
    @Case
    static String FIRST;
    
    @Case
    static Integer SECOND;
    
    @Case
    static UUID third;
    
}


