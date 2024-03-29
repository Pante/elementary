/*
 * The MIT License
 *
 * Copyright 2023 Karus Labs.
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
package com.karuslabs.elementary.junit.annotations;

import com.karuslabs.elementary.junit.ToolsExtension;

import java.lang.annotation.*;

import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.ArgumentsSource;

/**
 * Denotes the groups of labeled {@code Element}s to be used as parameters in a 
 * parameterized test.
 * 
 * Annotated tests must also be annotated with {@link ParameterizedTest} and in 
 * a class annotated with {@code @ToolsExtension}.
 * 
 * A label and the corresponding {@code Element} are provided as parameters.
 * <b>Note: </b>The label and corresponding {@code Element} must be the first parameters
 * of a test method if used in conjunction with other parameters injected by {@link ToolsExtension}.
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(ToolsExtension.class)
public @interface LabelSource {
    
    String[] groups();
    
}
