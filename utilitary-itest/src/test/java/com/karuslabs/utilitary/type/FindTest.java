/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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

import java.util.stream.Stream;
import javax.lang.model.element.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.of;

@ExtendWith(ToolsExtension.class)
@Introspect
class FindTest {
      
    Labels labels = Tools.labels();
    Element module = Tools.elements().getModuleOf(labels.get("type"));
    Element pack = Tools.elements().getPackageOf(labels.get("type"));
    
    @Label("type")
    static class Type {
        
        @Label("variable") String variable = "a";
        
        @Label("executable")
        void execute() {}
        
    }
    
    @ParameterizedTest
    @MethodSource("module_parameters")
    void module(Find finder, boolean found) {
        assertEquals(module.accept(finder, null) != null, found);
    }
    
    static Stream<Arguments> module_parameters() {
        return Stream.of(
            of(Find.MODULE, true),
            of(Find.PACKAGE, false),
            of(Find.TYPE, false),
            of(Find.EXECUTABLE, false)
        );
    }
    
    @ParameterizedTest
    @MethodSource("pack_parameters")
    void pack(Find finder, boolean found) {
        assertEquals(found, pack.accept(finder, null) != null);
    }
    
    static Stream<Arguments> pack_parameters() {
        return Stream.of(
            of(Find.MODULE, true),
            of(Find.PACKAGE, true),
            of(Find.TYPE, false),
            of(Find.EXECUTABLE, false)
        );
    }
    
    @ParameterizedTest
    @MethodSource("type_parameters")
    void type(Find finder, boolean found) {
        assertEquals(found, labels.get("type").accept(finder, null) != null);
    }
    
    static Stream<Arguments> type_parameters() {
        return Stream.of(
            of(Find.MODULE, true),
            of(Find.PACKAGE, true),
            of(Find.TYPE, true),
            of(Find.EXECUTABLE, false)
        );
    }
    
    @ParameterizedTest
    @MethodSource("executable_parameters")
    void executable(Find finder, boolean found) {
        assertEquals(found, labels.get("executable").accept(finder, null) != null);
    }
    
    static Stream<Arguments> executable_parameters() {
        return Stream.of(
            of(Find.MODULE, true),
            of(Find.PACKAGE, true),
            of(Find.TYPE, true),
            of(Find.EXECUTABLE, true)
        );
    }
    
    @ParameterizedTest
    @MethodSource("variable_parameters")
    void variable(Find finder, boolean found) {
        assertEquals(found, labels.get("variable").accept(finder, null) != null);
    }
    
    // We cannot test Find.EXECUTABLE since javac performs dead code elimation on
    // local variables in a method.
    static Stream<Arguments> variable_parameters() {
        return Stream.of(
            of(Find.MODULE, true),
            of(Find.PACKAGE, true),
            of(Find.TYPE, true)
        );
    }

} 
