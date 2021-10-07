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
package com.karuslabs.satisfactory.syntax;

import com.karuslabs.satisfactory.*;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.*;
import javax.lang.model.element.Modifier;

import org.checkerframework.checker.nullness.qual.Nullable;

import static com.karuslabs.satisfactory.Result.SUCCESS;
import static com.karuslabs.utilitary.Texts.sort;

class ContainsModifiers implements Assertion<Set<Modifier>> {

    private final List<Modifier> expected;
    private @Nullable Failure failure;
    
    ContainsModifiers(Modifier... modifiers) {
        expected = List.of(modifiers);
    }
    
    @Override
    public Result test(Set<Modifier> actual, TypeMirrors types) {
        return actual.containsAll(expected) ? SUCCESS : new Failure.Modifiers(expected, List.of(sort(actual)));
    }

    @Override
    public Failure fail() {
        if (failure == null) {
            failure = new Failure.Modifiers(expected, List.of());
        }
        
        return failure;
    }
    
}

class EqualsModifiers implements Assertion<Set<Modifier>> {
    
    private final List<Modifier> expected;
    private @Nullable Failure failure;
    
    EqualsModifiers(Modifier... modifiers) {
        expected = List.of(modifiers);
    }
    
    @Override
    public Result test(Set<Modifier> actual, TypeMirrors types) {
        return actual.containsAll(expected) && expected.containsAll(actual) ? SUCCESS : new Failure.Modifiers(expected, List.of(sort(actual)));
    }

    @Override
    public Failure fail() {
        if (failure == null) {
            failure = new Failure.Modifiers(expected, List.of());
        }
        
        return failure;
    }
    
}
