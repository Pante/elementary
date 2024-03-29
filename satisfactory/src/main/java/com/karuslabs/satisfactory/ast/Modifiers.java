/*
 * The MIT License
 *
 * Copyright 2022 Karus Labs.
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
package com.karuslabs.satisfactory.ast;

import com.karuslabs.satisfactory.Result;
import com.karuslabs.satisfactory.sequence.Sequence;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.*;
import javax.lang.model.element.*;

public interface Modifiers {
    static Sequence.Unordered<Modifier> contains(Set<Modifier> modifiers) {
        return new ContainsModifiers(modifiers);
    }
    
    static Sequence.Unordered<Modifier> equals(Set<Modifier> modifiers) {
        return new EqualsModifiers(modifiers);
    }
}

record ContainsModifiers(Set<Modifier> expected) implements Sequence.Unordered<Modifier> {
    @Override
    public Result test(Set<? extends Modifier> actual, TypeMirrors types) {
        return new Result.AST.Modifiers(actual, expected, actual.containsAll(expected));
    }
}

record EqualsModifiers(Set<Modifier> expected) implements Sequence.Unordered<Modifier> {
    @Override
    public Result test(Set<? extends Modifier> actual, TypeMirrors types) {
        return new Result.AST.Modifiers(actual, expected, actual.equals(expected));
    }
}
