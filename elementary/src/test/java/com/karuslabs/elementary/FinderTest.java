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

import com.karuslabs.elementary.junit.JavacExtension;
import com.karuslabs.elementary.junit.annotations.*;
import com.karuslabs.utilitary.AnnotationProcessor;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.karuslabs.elementary.file.FileObjects.ofLines;
import static javax.lang.model.SourceVersion.latest;
import static javax.tools.Diagnostic.Kind.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(JavacExtension.class)
@Inline(name = "Dummy", source = "class Dummy {}")
@Processors(WarningProcessor.class)
class FinderTest {

    @Test
    void iterator(Results results) {
        assertEquals(1, Stream.of(results.find().iterator()).count());
    }
    
    @Test
    @Processors(NoteProcessor.class)
    void kind(Results results) {
        assertEquals(WARNING, results.find().kind(WARNING).one().getKind());
    }
    
    @Test
    @Processors(ErrorProcessor.class)
    void errors(Results results) {
        assertEquals(ERROR, results.find().errors().one().getKind());
    }
    
    @Test
    @Processors(NoteProcessor.class)
    void warnings(Results results) {
        assertEquals(WARNING, results.find().warnings().one().getKind());
    }
    
    @Test
    @Processors(NoteProcessor.class)
    void notes(Results results) {
        assertEquals(NOTE, results.find().notes().one().getKind());
    }
    
    
    @Test
    @Inline(name = "A", source = "class A {")
    void in(Results results) {
        assertEquals(ERROR, results.find().in(ofLines("A", "")).one().getKind());
    }
    
    @Test
    @Inline(name = "A", source = "class A {\n)")
    void on(Results results) {
        assertEquals(2, results.find().on(2).count());
    }
    
    @Test
    @Inline(name = "A", source = "class A { method(); }")
    void at(Results results) {
        assertEquals(1, results.find().at(11).count());
    }
    
    
    @Test
    void where(Results results) {
        assertEquals(1, results.find().where(d -> d.getKind() == WARNING).count());
    }
    
    
    @Test
    void matches(Results results) {
        assertEquals(1, results.find().matches("warning").count());
    }
    
    @Test
    void contains_string(Results results) {
        assertEquals(1, results.find().contains("rnin").count());
    }   
    
    @Test
    void contains_pattern(Results results) {
        assertEquals(1, results.find().contains(Pattern.compile("(.*rning)")).count());
    }
    
    
    @Test
    void diagnostics(Results results) {
        assertEquals(List.of("warning: warning"), results.find().diagnostics());
    }
    
    @Test
    void messages(Results results) {
        assertEquals(List.of("warning"), results.find().messages());
    }
    
    @Test
    @Inline(name = "A", source = "class A {\n)")
    void lines(Results results) {
        assertEquals(List.of(2L, 2L), results.find().lines());
    }
    
    @Test
    @Inline(name = "A", source = "class A { method(); }")
    void columns(Results results) {
        assertEquals(List.of(11L), results.find().columns());
    }
    
    @Test
    @Inline(name = "A", source = "class A { method(); }")
    void positions(Results results) {
        assertEquals(List.of(10L), results.find().positions());
    }
    
    @Test
    @Inline(name = "A", source = "class A { method(); }")
    void codes(Results results) {
        assertEquals(List.of("compiler.err.invalid.meth.decl.ret.type.req"), results.find().codes());
    }
    
    
    @Test
    void one(Results results) {
        assertNotNull(results.find().one());
    }
    
    @Test
    void one_none(Results results) {
        assertNull(results.find().errors().one());
    }
    
    @Test
    @Processors(WarningProcessor.class)
    void one_many(Results results) {
        assertNull(results.find().one());
    }
    
    @Test
    void list(Results results) {
        assertEquals(results.warnings, results.find().warnings().list());
    }
    
    @Test
    void map(Results results) {
        assertEquals(Map.of(WARNING, results.warnings), results.find().map());
    }
    
}


@SupportedAnnotationTypes({"*"})
class ErrorProcessor extends AnnotationProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment round) {
        if (round.processingOver()) {
            logger.error(null, "error");
        }
        
        return false;
    }
    
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latest();
    }
}

@SupportedAnnotationTypes({"*"})
class WarningProcessor extends AnnotationProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment round) {
        if (round.processingOver()) {
            logger.warn(null, "warning");
        }
        
        return false;
    }
    
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latest();
    }
}

@SupportedAnnotationTypes({"*"})
class NoteProcessor extends AnnotationProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment round) {
        if (round.processingOver()) {
            logger.note(null, "note");
        }
        
        return false;
    }
    
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latest();
    }
}
