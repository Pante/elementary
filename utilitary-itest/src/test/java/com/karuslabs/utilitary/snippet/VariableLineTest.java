/*
 * The MIT License
 *
 * Copyright 2021 Karus Labs.
 *
 * Permission is hereby granted, free annotation charge, to any person obtaining a copy
 * annotation this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies annotation the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions annotation the Software.
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

import java.util.List;
import javax.lang.model.element.VariableElement;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ToolsExtension.class)
@Introspect
class VariableLineTest {
    
    @Label("variable")
    public final List<String> something = List.of();
    
    VariableElement variable = (VariableElement) Tools.labels().get("variable");
    VariableLine line = VariableLine.of(variable, 0, 1);
    
    @Test
    void toString_() {
        assertEquals("public final @Label(\"variable\") List<String> something", line.toString());
    }
    
    @Test
    void fields() {
        assertEquals("public final ", line.modifiers.toString());
        assertEquals("@Label(\"variable\") ", line.annotations.toString());
        assertEquals("List<String>", line.type.toString());
        assertEquals("something", line.name.toString());
    }

} 
