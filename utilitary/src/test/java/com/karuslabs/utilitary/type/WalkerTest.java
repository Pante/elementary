/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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

import java.util.*;
import javax.lang.model.type.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ToolsExtension.class)
@Introspect("WalkerTest")
class AncestorWalkerTest {
    
    TypeMirrors types = Tools.typeMirrors();
    Walker<TypeMirror> walker = Walker.ancestor(types);
    Cases cases = Tools.cases();
    @Case("ancestor") Collection<String> ancestor;
    @Case("descendent") List<String> descendent;
    @Case("other") List<Integer> other;
    
    @Test
    void visitDeclared_same() {
        var ancestor = cases.one("ancestor").asType();
        assertTrue(types.isSameType(ancestor, ancestor.accept(walker, ancestor)));
    }
    
    @Test
    void visitDeclared_found_ancestor() {
        var ancestor = cases.one("ancestor").asType();
        assertTrue(types.isSameType(ancestor, cases.one("descendent").asType().accept(walker, ancestor)));
    }
    
    @Test
    void visitDeclared_no_ancestor() {
        assertNull(cases.one("other").asType().accept(walker, cases.one("ancestor").asType()));
    }
    
    @Test
    void defaultAction() {
        assertNull(walker.defaultAction(null, null));
    }

}
