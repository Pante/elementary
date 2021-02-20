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
package com.test;

import com.karuslabs.elementary.junit.Classpath;
import com.karuslabs.elementary.junit.Inline;
import com.karuslabs.elementary.junit.tools.Tools;
import com.karuslabs.elementary.junit.tools.ToolsExtension;
import com.karuslabs.utilitary.Logger;
import com.karuslabs.utilitary.type.TypeMirrors;

import javax.annotation.processing.Messager;
import javax.lang.model.util.Elements;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ToolsExtension.class)
@Classpath("A.java")
@Inline(name = "Derp", source = "class Derp {}")
public class SomeTest {

    Logger logger;
    Elements elements = Tools.elements();
    TypeMirrors types = Tools.typeMirrors();
    
    SomeTest(Logger logger) {
        this.logger = logger;
    }
    
    
    @Test
    void test() {
        var a = types.type(String.class);
        var b = types.type(String.class);
        
        assertTrue(types.isSameType(a, b));
    }
    
    
    @Test
    void inject(Messager messager, Elements elements) {
        assertNotNull(messager);
        assertNotNull(elements);
    }
    
}
