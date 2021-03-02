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

import com.karuslabs.elementary.junit.annotations.Processors;

import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.element.TypeElement;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import static com.karuslabs.elementary.Compiler.javac;
import static org.junit.jupiter.api.Assertions.*;

@Processors(InvalidProcessor.class)
class JavacExtensionTest {

    JavacExtension extension = new JavacExtension();
    
    @Test
    void resolve_fails() {
        assertEquals(
            "Failed to create \"" + InvalidProcessor.class.getName() + "\", annotation processor should have a constructor with no arguments",
            assertThrows(ParameterResolutionException.class, () -> extension.resolve(javac(), JavacExtensionTest.class)).getMessage()
        );
    }
    
}

class InvalidProcessor extends AbstractProcessor {
    
    InvalidProcessor(String a) {}

    @Override
    public boolean process(Set<? extends TypeElement> types, RoundEnvironment round) {
        return false;
    }
    
}
