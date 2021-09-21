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

import com.karuslabs.satisfactory.Assertion;
import com.karuslabs.satisfactory.Flag;

import java.util.Set;
import javax.lang.model.element.Modifier;

import static com.karuslabs.satisfactory.syntax.Modifiers.Result.EMPTY;

public class Modifiers {
    
    public static Assertion<Set<Modifier>> ANY_MODIFIERS = (types, actual) -> Result.EMPTY;
    
    public static Assertion<Set<Modifier>> contains(Modifier... modifiers) {
        var expected = Set.of(modifiers);
        var result = new Result("", expected);
        
        return (actual, types) -> actual.containsAll(expected) ? EMPTY : result;
    }
    
    public static Assertion<Set<Modifier>> equals(Modifier... modifiers) {
        var expected = Set.of(modifiers);
        var result = new Result("", expected);
        
        return (actual, types) -> expected.equals(types) ? EMPTY : result;
    }
    
    public static class Result extends com.karuslabs.satisfactory.Result {
        
        public static final Result EMPTY = new Result(true, "", Set.of());
        
        public final String verb;
        public final Set<Modifier> modifiers;
        
        public Result(String verb, Set<Modifier> expected) {
            this(false, verb, expected);
        }
        
        Result(boolean success, String verb, Set<Modifier> modifiers) {
            super(success);
            this.verb = verb;
            this.modifiers = modifiers;
        }
        
        @Override
        public <T, R> R accept(Visitor<T, R> visitor, Set<Flag> flags, T value) {
            return visitor.visitModifiers(this, flags, value);
        }

        @Override
        public Result empty() {
            return EMPTY;
        }
        
    }

}
