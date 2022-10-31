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

import com.karuslabs.elementary.junit.*;
import com.karuslabs.elementary.junit.annotations.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static javax.lang.model.element.ElementKind.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ToolsExtension.class)
@Introspect
class TypeLineTest {
    
    @Case("annotation")
    static @interface Annotation {}
    
    @Case("enum")
    static enum Enum {}
    
    @Case("interface")
    static interface Interface {}
    
    @Case("class")
    static class Type {}
    
    @Test
    void annotation() {
        var line = TypeLine.of(Tools.cases().one("annotation").getKind(), 0, 1);
        assertEquals("@interface", line.toString());
        assertEquals(ANNOTATION_TYPE, line.kind);
    }
    
    @Test
    void enum_() {
        var line = TypeLine.of(Tools.cases().one("enum").getKind(), 0, 1);
        assertEquals("enum", line.toString());
        assertEquals(ENUM, line.kind);
    }
    
    @Test
    void interface_() {
        var line = TypeLine.of(Tools.cases().one("interface").getKind(), 0, 1);
        assertEquals("interface", line.toString());
        assertEquals(INTERFACE, line.kind);
    }
    
    @Test
    void class_() {
        var line = TypeLine.of(Tools.cases().one("class").getKind(), 0, 1);
        assertEquals("class", line.toString());
        assertEquals(CLASS, line.kind);
    }

} 
