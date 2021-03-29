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

import com.karuslabs.elementary.junit.*;
import com.karuslabs.elementary.junit.annotations.Classpath;
import com.karuslabs.utilitary.type.TypeMirrors;
import java.util.List;

import javax.lang.model.element.Element;

import org.checkerframework.checker.nullness.qual.Nullable;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(ToolsExtension.class)
//@Classpath("com.karuslabs.satisfactory.AnnotationsCase")
class AnnotationsTest {
    
//    TypeMirrors types = Tools.typeMirrors();
//    Assertion<Element> assertion = Assertions.contains(Nullable.class);
//    
//    @Test
//    void contains_true(Cases cases) {
//        assertTrue(assertion.test(types, cases.get(0)));
//    }
//    
//    @Test
//    void contains_false(Cases cases) {
//        assertFalse(assertion.test(types, cases.get(1)));
//    }
    
    @Test
    void help() {
        System.out.println(List.of(A.class.getNestMembers()));
        System.out.println(A.B.class.getNestHost());
        System.out.println(C.class.getNestHost());
        System.out.println(C.D.class.getNestHost());
    }

} 

class A {
    
    static class B {
    
    }
    
}

class C {
    
    static class D {
        
    }
    
}
