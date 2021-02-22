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
package com.karuslabs.elementary.junit;

import com.karuslabs.elementary.Result;
import com.karuslabs.elementary.junit.annotations.Classpath;
import com.karuslabs.elementary.junit.annotations.Flags;
import com.karuslabs.elementary.junit.annotations.Inline;
import com.karuslabs.elementary.junit.annotations.Processors;
import com.karuslabs.utilitary.AnnotationProcessor;

import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.element.TypeElement;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static javax.lang.model.SourceVersion.RELEASE_11;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(JavacExtension.class)
@Classpath("A.java")
@Inline(name = "Derp", source = "class Derp {}")
@Flags("-g")
class JavacExtensionTest {

    @Test
    @Processors({SomeProcessor.class})
    @Inline(name = "MethodInline", source = "")
    @Flags("-nowarn")
    void test(Result result) {
        assertTrue(result.find().errors().count() == 0);
    }
    
}

@SupportedAnnotationTypes({"*"})
@SupportedSourceVersion(RELEASE_11)
class SomeProcessor extends AnnotationProcessor {
    
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment round) {
        logger.note(null, "Message");
        return false;
    }

}
