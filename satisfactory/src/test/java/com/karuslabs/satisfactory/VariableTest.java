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

import com.karuslabs.satisfactory.old.Variable;
import com.karuslabs.elementary.junit.*;
import com.karuslabs.elementary.junit.annotations.*;
import com.karuslabs.utilitary.type.TypeMirrors;

import javax.lang.model.element.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.karuslabs.satisfactory.old.Assertions.*;
import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ToolsExtension.class)
@Introspect
class VariableTest {

    @Case
    static final String CASE = "";
    
    @Case
    int a;
    
    TypeMirrors types = Tools.typeMirrors();
    Cases cases = Tools.cases();
    Variable variable = variable(
        annotations(contains(Case.class)),
        modifiers(equal(STATIC, FINAL)),
        type(String.class)
    ).get();
    
    
    @Test
    void test_true() {
        assertTrue(variable.test(types, (VariableElement) cases.get(0)));
    }
    
    @Test
    void test_false() {
        assertFalse(variable.test(types, (VariableElement) cases.get(1)));
    }
    
    @Test
    void condition() {
        assertEquals("{contains [@Case], equal [static final], String}", variable.condition());
        assertEquals("{contains [@Case], equal [static final], String}", variable.conditions());
    }
    
    @Test
    void variable_part() {
        assertEquals(VariableElement.class, variable.part());
    }
    
    
    @Test
    void builder_unsupported_assertion() {
        assertEquals(
            "Assertion for " + variable.part().getName() + " is not supported", 
            assertThrows(IllegalArgumentException.class, () -> variable(variable).get()).getMessage()
        );
    }
    
    @Test
    void builder_duplicate_assertion() {
        assertEquals(
            "Already declared an assertion for " + Modifier.class.getName(),
            assertThrows(IllegalStateException.class, () -> variable(contains(STATIC), equal(STATIC)).get()).getMessage()
        );
    }
    
    @Test
    void or_default() {
        var any = variable().get();
        assertTrue(any.test(types, (VariableElement) cases.get(0)));
        assertTrue(any.test(types, (VariableElement) cases.get(1)));
    }
    
}
