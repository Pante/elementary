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
    
    Labels labels = Tools.labels();
    
    @Label("line")
    static final String LINE = "";
    
    @Label("long")
    static final void extremelyLongMethodThatThrowsManyExceptions(String a, String b, String c) throws IllegalArgumentException {
        
    }
    
    @Test
    void highlight_multiple() {
        var method = (ExecutableElement) labels.get("long");
        var snippet = MethodSnippet.of(method, 0);
        var message = Texts.diagnose("<summary>", snippet, Map.of(
            snippet.modifiers, "method should not be static", 
            snippet.type, "method should not return void", 
            snippet.name, "method's name is too damn long",
            snippet.parameters.values.get(method.getParameters().get(0)).name, "method should not contain a string parameter"
        ));
        
        assertEquals(
            "<summary>\n" +
            "|    \n" +
            "|    @Label(\"long\")\n" +
            "|    static final  void extremelyLongMethodThatThrowsManyExceptions(String a, String b, String c) throws IllegalArgumentException\n" +
            "|    ~~~~~~~~~~~~~ ~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~        ^ method should not contain a string parameter\n" +
            "|    |             |    |\n" +
            "|    |             |    |\n" +
            "|    |             |    method's name is too damn long\n" +
            "|    |             |\n" +
            "|    |             method should not return void\n" +
            "|    |\n" +
            "|    method should not be static\n" +
            "|    ", message);
    }
    
    @Test
    void highlight_line() {
        var line = VariableLine.of((VariableElement) labels.get("line"), 0, 4);
        var message = Texts.diagnose("<summary>", line, line.name, "what happened");
        // Message seems misaligned due to additional "\"s
        assertEquals(
            "<summary>\n" +
            "|    \n" +
            "|        static final @Label(\"line\") String LINE\n" +
            "|                                           ~~~~ what happened\n" +
            "|    ", 
            message
        );
    }
    
    @Test
    void highlight_snippet() {
        var snippet = VariableSnippet.of((VariableElement) labels.get("line"), 4);
        var message = Texts.diagnose("<summary>", snippet, snippet.name, "what happened");
        assertEquals(
            "<summary>\n" +
            "|    \n" +
            "|    @Label(\"line\")\n" +
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
        assertArrayEquals(new Modifier[] {PUBLIC, STATIC, FINAL, NATIVE}, Texts.sort(STATIC, FINAL, NATIVE, PUBLIC));
    }

} 
