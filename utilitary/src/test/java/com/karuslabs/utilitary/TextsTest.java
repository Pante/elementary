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
package com.karuslabs.utilitary;

import com.karuslabs.elementary.junit.*;
import com.karuslabs.elementary.junit.annotations.*;
import com.karuslabs.utilitary.snippet.*;

import java.util.*;
import java.util.stream.Stream;
import javax.lang.model.element.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;

@ExtendWith(ToolsExtension.class)
@Introspect
class TextsTest {
    
    Cases cases = Tools.cases();
    
    @Case("line")
    static final String LINE = "";
    
    @Case("long")
    static final void extremelyLongMethodThatThrowsManyExceptions(String a, String b, String c) throws IllegalArgumentException {
        
    }
    
    @Test
    void example() {
        var method = (ExecutableElement) cases.one("long");
        var snippet = MethodSnippet.of(method, 0);
        assertEquals("", Texts.diagnose("<summary>", snippet, Map.of(
            snippet.modifiers, "method should not be static", 
            snippet.type, "method should not return void", 
            snippet.name, "method's name is too damn long",
            snippet.parameters.values.get(method.getParameters().get(0)).type, "method should not contain a string parameter"
        )));
    }
    
    @Test
    void highlight_long() {
        var snippet = MethodSnippet.of((ExecutableElement) cases.one("long"), 0);
        var message = Texts.diagnose("a brief message", snippet, snippet.exceptions, "what happened");
        assertEquals(
            "a brief message\n" +
            "|    \n" +
            "|    @Case(\"long\")\n" +
            "|    static final  void extremelyLongMethodThatThrowsManyExceptions(String a, String b, String c) throws IllegalArgumentException\n" +
            "|                                                                                  what happened ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
            "|    ", 
            message
        );
    }
    
    @Test
    void highlight_line() {
        var line = VariableLine.of((VariableElement) cases.one("line"), 0, 4);
        var message = Texts.diagnose("a brief message", line, line.name, "what happened");
        assertEquals(
            "a brief message\n" +
            "|    \n" +
            "|    static final @Case(\"line\") String LINE\n" +
            "|                                      ~~~~ what happened\n" +
            "|    ", 
            message
        );
    }
    
    @Test
    void highlight_snippet() {
        var snippet = VariableSnippet.of((VariableElement) cases.one("line"), 4);
        var message = Texts.diagnose("a brief message", snippet, snippet.name, "what happened");
        assertEquals(
            "a brief message\n" +
            "|    \n" +
            "|    @Case(\"line\")\n" +
            "|    static final String LINE\n" +
            "|                        ~~~~ what happened\n" +
            "|    ",
            message
        );
    }
    
    @ParameterizedTest
    @MethodSource("conjunction_parameters")
    void and_list(String expected, String[] values) {
        assertEquals(expected.replace("|", "and"), Texts.and(List.of(values), Texts.STRING));
    }
    
    @ParameterizedTest
    @MethodSource("conjunction_parameters")
    void and_array(String expected, String[] values) {
        assertEquals(expected.replace("|", "and"), Texts.and(values, Texts.STRING));
    }
    
    @ParameterizedTest
    @MethodSource("conjunction_parameters")
    void or_list(String expected, String[] values) {
        assertEquals(expected.replace("|", "or"), Texts.or(List.of(values), Texts.STRING));
    }
    
    @ParameterizedTest
    @MethodSource("conjunction_parameters")
    void or_array(String expected, String[] values) {
        assertEquals(expected.replace("|", "or"), Texts.or(values, Texts.STRING));
    }
    
    static Stream<Arguments> conjunction_parameters() {
        return Stream.of(
            of("A", new String[] {"A"}),
            of("A | B", new String[] {"A", "B"}),
            of("A, B | C", new String[] {"A", "B", "C"})
        );
    }
    
    @ParameterizedTest
    @MethodSource("join_parameters")
    void join_list(String expected, String[] values) {
        assertEquals(expected, Texts.join(List.of(values), Texts.STRING, " "));
    }
    
    @ParameterizedTest
    @MethodSource("join_parameters")
    void join_array(String expected, String[] values) {
        assertEquals(expected, Texts.join(values, Texts.STRING, " "));
    }
    
    static Stream<Arguments> join_parameters() {
        return Stream.of(
            of("A", new String[] {"A"}),
            of("A B", new String[] {"A", "B"}),
            of("A B C", new String[] {"A", "B", "C"})
        );
    }
    
    
    @ParameterizedTest
    @CsvSource({"abc, a, c", "a, a, ''", "c, '', c"})
    void join(String expected, String first, String second) {
        assertEquals(expected, Texts.join(first, "b", second));
    }
    
    @Test
    void sort() {
        assertArrayEquals(new Modifier[] {PUBLIC, STATIC, ABSTRACT, FINAL}, Texts.sort(STATIC, FINAL, PUBLIC, ABSTRACT));
    }

} 
