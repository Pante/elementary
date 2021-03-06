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
package com.karuslabs.elementary;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.tools.*;
import javax.tools.Diagnostic.Kind;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;

class DiagnosticsTest {

    Diagnostics diagnostics = new Diagnostics();
    Diagnostic diagnostic = mock(Diagnostic.class);
    
    @ParameterizedTest
    @MethodSource("report_parameters")
    void report(Function<Diagnostics, List<?>> function, Kind kind) {
        when(diagnostic.getKind()).thenReturn(kind);
        
        diagnostics.report(diagnostic);
        
        assertEquals(List.of(diagnostic), diagnostics.all);
        assertEquals(List.of(diagnostic), function.apply(diagnostics));
    }
    
    static Stream<Arguments> report_parameters() {
        return Stream.of(
            of((Function<Diagnostics, List<?>>) d -> d.errors, Kind.ERROR),
            of((Function<Diagnostics, List<?>>) d -> d.warnings, Kind.MANDATORY_WARNING),
            of((Function<Diagnostics, List<?>>) d -> d.warnings, Kind.WARNING),
            of((Function<Diagnostics, List<?>>) d -> d.notes, Kind.NOTE)
        );
    }
    
}
