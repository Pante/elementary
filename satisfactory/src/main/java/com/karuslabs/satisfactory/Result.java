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

import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class Result {
        
    public final boolean success;

    public Result(boolean success) {
        this.success = success;
    }

    public abstract <T, R> R accept(Visitor<T, R> visitor, Set<Flag> flags, T value);

    public abstract Result empty();
    
    
    public static interface Visitor<T, R> {
        
        default @Nullable R visitModifiers(Modifiers.Result result, Set<Flag> flags, T value) {
            return visit(result, flags, value);
        }

        default @Nullable R visitAnd(AndResult result, Set<Flag> flags, T value) {
            return visit(result, flags, value);
        }

        default @Nullable R visitOr(OrResult result, Set<Flag> flags, T value) {
            return visit(result, flags, value);
        }

        default @Nullable R visitNegation(NegationResult result, Set<Flag> flags, T value) {
            return visit(result, flags, value);
        }

        default @Nullable R visit(Result result, Set<Flag> flags, T value) {
            return null;
        }

    }

}
