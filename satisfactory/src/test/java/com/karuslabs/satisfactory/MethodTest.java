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

import com.karuslabs.satisfactory.zold.Method;
import com.karuslabs.elementary.junit.*;
import com.karuslabs.elementary.junit.annotations.*;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.io.IOException;
import java.util.*;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.karuslabs.satisfactory.zold.Assertions.*;
import static com.karuslabs.satisfactory.zold.Sequences.*;
import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ToolsExtension.class)
@Introspect
class MethodTest {
    
    TypeMirrors types = Tools.typeMirrors();
    Cases cases = Tools.cases();
    ExecutableElement first = (ExecutableElement) cases.get(0);
    ExecutableElement second = (ExecutableElement) cases.get(1);
    
    Method method = method(
        annotations(contains(Case.class)),
        equal(PUBLIC, STATIC),
        type(String.class),
        parameters(equal(
            variable(type(String.class)),
            variable(type(UUID.class))
        )),
        exceptions(equal(max(1, subtype(IOException.class))))
    ).get();
    
    @Case
    public static String first(String a, UUID b) throws IOException { return ""; }
    
    @Case
    void second() {}
    
    
    @Test
    void test() {
        assertTrue(method.test(types, first));
    }
    
    @Test
    void test_false() {
        assertFalse(method.test(types, second));
    }
    
    @Test
    void condition() {
        var condition = "{"
                      + "\n  annotations contains [@Case]"
                      + "\n  modifiers equal [public static]"
                      + "\n  returns String"
                      + "\n  parameters equal [{String}, {UUID}]"
                      + "\n  throws equal [1 or less subtypes of IOException]"
                      + "\n}";
        assertEquals(condition, method.condition());
        assertEquals(condition, method.conditions());
    }
    
    @Test
    void method_part() {
        assertEquals(ExecutableElement.class, method.part());
    }
    
    
    @Test
    void builder_unsupported_assertion() {
        assertEquals(
            "Assertion for " + ExecutableElement.class.getName() + " is not supported",
            assertThrows(IllegalArgumentException.class, () -> method(method)).getMessage()
        );
    }
    
    @Test
    void builder_duplicate_assertion() {
        assertEquals(
            "Already declared an assertion for " + TypeMirror.class.getName(),
            assertThrows(IllegalStateException.class, () -> method(type(String.class), type(String.class))).getMessage()
        );
    }
    
    
    @Test
    void builder_unsupported_sequence() {
        assertEquals(
            "Sequence for " + Modifier.class.getName() + " is not supported",
            assertThrows(IllegalArgumentException.class, () -> method(equal(contains(PUBLIC)))).getMessage()
        );
    }
    
    @Test
    void builder_duplicate_sequence() {
        assertEquals(
            "Already declared a sequence for " + TypeMirror.class.getName(),
            assertThrows(IllegalStateException.class, () -> method(ANY_EXCEPTIONS, ANY_EXCEPTIONS)).getMessage()
        );
    }
    
    @Test
    void builder_invalid_part() {
        assertEquals(
            "Part for " + int.class.getName() + " is not supported",
            assertThrows(IllegalArgumentException.class, () -> method(() -> int.class)).getMessage()
        );
    }
    
}
