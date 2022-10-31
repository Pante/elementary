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

import javax.lang.model.element.TypeElement;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ToolsExtension.class)
@Introspect
class TypeSnippetTest {
    
    @Label("type")
    static abstract class Type<T extends String> extends Supertype implements Runnable {}
            
    static class Supertype {}
    
    TypeElement type = (TypeElement) Tools.labels().get("type");
    TypeSnippet snippet = TypeSnippet.of(type, 0);
    
    @Test
    void toString_() {
        assertEquals(
            "@Label(\"type\")\nstatic abstract class Type<T extends String> extends TypeSnippetTest.Supertype implements Runnable {",
            snippet.toString()
        );
    }
    
    @Test
    void fields() {
        assertEquals("@Label(\"type\")", snippet.annotations.toString());
        assertEquals("static abstract ", snippet.modifiers.toString());
        assertEquals("class", snippet.type.toString());
        assertEquals("Type", snippet.name.toString());
        assertEquals("<T extends String>", snippet.typeParameters.toString());
        assertEquals(" extends TypeSnippetTest.Supertype", snippet.supertype.toString());
        assertEquals(" implements Runnable", snippet.interfaces.toString());
    }

} 
