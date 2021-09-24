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

import com.karuslabs.satisfactory.Logical.*;

import org.checkerframework.checker.nullness.qual.Nullable;

public sealed interface Result {
    
    static final Success SUCCESS = new Success();
    
    <T, R> R accept(Visitor<T, R> visitor, T value);
    
    
    public static final class Success implements Result {
        @Override
        public <T, R> R accept(Visitor<T, R> visitor, T value) {
            return visitor.success(this, value);
        }
    }
    
    public static non-sealed interface Failure extends Result {}
    
    
    public static interface Visitor<T, R> {
        
        default @Nullable R visitAnd(AndFailure failure, T value) {
            for (var nested : failure.nested()) {
                nested.accept(this, value);
            }
            return null;
        }
        
        default @Nullable R visitOr(OrFailure failure, T value) {
            for (var nested : failure.nested()) {
                nested.accept(this, value);
            }
            return null;
        }
        
        
        
        default @Nullable R success(Success success, T value) {
            return null;
        }
        
        default @Nullable R failure(Failure failure, T value) {
            return null;
        }

    }
    
}
