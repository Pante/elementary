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
package com.karuslabs.utilitary.type;

import com.karuslabs.elementary.junit.*;
import com.karuslabs.elementary.junit.annotations.*;

import javax.lang.model.element.Modifier;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static javax.lang.model.element.Modifier.STATIC;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ToolsExtension.class)
@Introspect
class AnnotationValuePrinterTest {
    
    Cases cases = Tools.cases();
    
    String print(String label) {
        var annotation = cases.one(label).getAnnotationMirrors().get(2);
        return AnnotationValuePrinter.annotation(annotation);
    }
    
    @Test
    @Case("empty")
    @Nested
    void annotation_empty() {
        assertEquals("@Nested", print("empty"));
    }
    
    @Test
    @Case("multiple")
    @Multiple(a = "1", b = "2")
    void annotation_multiple_values() {
        assertEquals("@Multiple(a = \"1\", b = \"2\")", print("multiple"));
    }
    
    @Test
    @Case("nested")
    @Nest(@Nested)
    void visitAnnotation_nested() {
        assertEquals("@Nest(@Nested)", print("nested"));
    }
    
    @Test
    @Case("array_single")
    @Array(1)
    void visitArray_single() {
        assertEquals("@Array(1)", print("array_single"));
    }
    
    @Test
    @Case("array_many")
    @Array({1, 2, 3})
    void visitArray_many() {
        assertEquals("@Array({1, 2, 3})", print("array_many"));
    }
    
    @Test
    @Case("array_empty")
    @Array({})
    void visitArray_empty() {
        assertEquals("@Array({})", print("array_empty"));
    }
    
    @Test
    @Case("enum")
    @Enumeration(STATIC)
    void visitEnum() {
        assertEquals("@Enumeration(STATIC)", print("enum"));
    }
    
    @Test
    @Case("string")
    @StringValue("a")
    void visitString() {
        assertEquals("@StringValue(\"a\")", print("string"));
    }
    
    @Test
    @Case("type")
    @Type(String.class)
    void visitType() {
        assertEquals("@Type(String.class)", print("type"));
    }
    
    @Test
    void defaultAction() {
        var builder = new StringBuilder();
        AnnotationValuePrinter.PRINTER.defaultAction("Default", builder);
        assertEquals("Default", builder.toString());
    }

}

@interface Multiple {
    String a();
    
    String b();
}

@interface Nest {
    Nested value();
}
    
@interface Nested {}

@interface Array {
    int[] value();
}

@interface Enumeration {
    Modifier value();
}

@interface StringValue {
    String value();
}

@interface Type {
    Class<?> value();
}