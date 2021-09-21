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
import java.util.*;
import javax.lang.model.type.TypeMirror;

import static com.karuslabs.satisfactory.syntax.Type.Result.EMPTY;

public class Type {

    public static Assertion<TypeMirror> type(TypeMirror type) {
        var result = new Result("", List.of(type));
        return (actual, types) -> types.isSameType(actual, type) ? EMPTY : result;
    }
    
    public static class Result extends com.karuslabs.satisfactory.Result {
        
        public static final Result EMPTY = new Result(true, "", List.of());
        
        public final String verb;
        public final List<TypeMirror> types;
        
        public Result(String verb, List<TypeMirror> types) {
            this(false, verb, types);
        }
        
        Result(boolean success, String verb, List<TypeMirror> types) {
            super(success);
            this.verb = verb;
            this.types = types;
        }
        
        @Override
        public <T, R> R accept(Visitor<T, R> visitor, Set<Flag> flags, T value) {
            return visitor.visitType(this, flags, value);
        }

        @Override
        public Result empty() {
            return EMPTY;
        }
        
    }
    
}
