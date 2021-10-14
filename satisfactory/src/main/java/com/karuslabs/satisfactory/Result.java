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
package com.karuslabs.satisfactory;

import com.karuslabs.satisfactory.Failure.*;
import org.checkerframework.checker.nullness.qual.Nullable;

public sealed interface Result permits Failure, Success {
    
    public static final Success SUCCESS = new Success();
    
    <T, R> R accept(Visitor<T, R> visitor, T value);
    
    public static interface Visitor<T, R> {
        
        default @Nullable R visitAnnotations(Annotations failure, T value) {
            return failure(failure, value);
        }
        
        default @Nullable R visitModifiers(Modifiers failure, T value) {
            return failure(failure, value);
        }
        
        default @Nullable R visitLogical(Logical failure, T value) {
            return failure(failure, value);
        }
        
        default @Nullable R success(Success success, T value) {
            return null;
        }
        
        default @Nullable R failure(Failure failure, T value) {
            return null;
        }

    }
    
}
