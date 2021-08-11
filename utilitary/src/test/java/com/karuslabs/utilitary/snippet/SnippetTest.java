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
package com.karuslabs.utilitary.snippet;

import com.karuslabs.elementary.junit.ToolsExtension;
import com.karuslabs.elementary.junit.annotations.Introspect;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ToolsExtension.class)
@Introspect
class SnippetTest {
    
    static final TreeMap<Integer, Line> LINES = new TreeMap<>(Map.of(1, new Line("first", 0, 0), 2, new Line("second", 0, 0), 3, new Line("third", 0, 0)));
    
    Snippet snippet = new Snippet(LINES);
    
    @Test
    void snippet() {
        assertEquals(1, snippet.first);
        assertEquals(3, snippet.last);
    }
    
    @Test
    void length() {
        assertEquals(18, snippet.length());
    }
    
    @Test
    void charAt() {
        assertEquals('i', snippet.charAt(1));
    }
    
    @Test
    void subSequence() {
        assertEquals("irst", snippet.subSequence(1, 5));
    }
    
    @Test
    void equals_same() {
        assertTrue(snippet.equals(snippet));
    }
    
    @Test
    void equals_other() {
        assertFalse(snippet.equals(Map.of(1, "first", 2, "second", 3, "third")));
    }
    
    @Test
    void equals() {
        assertEquals(snippet, new Snippet(LINES));
    }
    
    @Test
    void hashCode_() {
        assertEquals(snippet.hashCode(), new Snippet(LINES).hashCode());
    }
    
    @Test
    void hashCode_different() {
        assertNotEquals(snippet.hashCode(), LINES.hashCode());
    }
    
    @Test
    void toString_() {
        assertEquals("first\nsecond\nthird", snippet.toString());
    }

} 
