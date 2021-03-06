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

/**
 * Indicates that compilation has failed. 
 */
public class CompilationException extends RuntimeException {
    
    /**
     * Creates a {@code CompilationException} with the given error messages.
     * 
     * @param messages the error messages
     */
    public CompilationException(List<String> messages) {
        this(String.join("\n", messages));
    }
    
    /**
     * Creates a {@code CompilationException} with the given error message.
     * 
     * @param message the error message
     */
    public CompilationException(String message) {
        this(message, null);
    }
    
    /**
     * Creates a {@code CompilationException} with the given error message and
     * underlying exception.
     * 
     * @param message the error message
     * @param e the underlying exception
     */
    public CompilationException(String message, Throwable e) {
        super(message, e);
    }
    
}
