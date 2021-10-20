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
package com.karuslabs.satisfactory.assertions;

import com.karuslabs.satisfactory.a.Result;
import com.karuslabs.satisfactory.a.Failure;
import com.karuslabs.satisfactory.*;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.*;
import java.util.stream.Stream;
import javax.lang.model.type.*;

import org.checkerframework.checker.nullness.qual.Nullable;

import static com.karuslabs.satisfactory.a.Result.SUCCESS;

public sealed abstract class Type implements Assertion<TypeMirror> {

    public static enum Relation {
        IS {
            @Override
            boolean test(TypeMirror expected, TypeMirror actual, TypeMirrors types) {
                return types.isSameType(expected, actual);
            }
        },
        
        SUBTYPE {
            @Override
            boolean test(TypeMirror expected, TypeMirror actual, TypeMirrors types) {
                return types.isSubtype(actual, expected);
            }
        },
        
        SUPERTYPE {
            @Override
            boolean test(TypeMirror expected, TypeMirror actual, TypeMirrors types) {
                return types.isSubtype(expected, actual);
            }
        };
        
        abstract boolean test(TypeMirror expected, TypeMirror actual, TypeMirrors types);
    }
    
    final Relation relation;
    
    Type(Relation relation) {
        this.relation = relation;
    }
    
    @Override
    public Result test(TypeMirror actual, TypeMirrors types) {
        for (var expected : expected(types)) {
            if (!relation.test(expected, actual, types)) {
                return fail(actual, types);
            }
        }

        return SUCCESS;
    }

    @Override
    public Failure.Type fail(TypeMirror actual, TypeMirrors types) {
        return new Failure.Type(relation, expected(types), actual);
    }
    
    abstract List<TypeMirror> expected(TypeMirrors types);
    
}

record Primitive(TypeKind kind) implements Assertion<TypeMirror> {
    
    @Override
    public Result test(TypeMirror actual, TypeMirrors types) {
        return actual.getKind() == kind ? SUCCESS : fail(actual, types);
    }

    @Override
    public Failure.Primitive fail(TypeMirror actual, TypeMirrors types) {
        return new Failure.Primitive(kind, actual);
    }
    
}

final class ClassType extends Type {

    final Class<?>[] classes;
    @Nullable List<TypeMirror> expected;
    
    ClassType(Relation relation, Class<?>... classes) {
        super(relation);
        this.classes = classes;
    }
    
    @Override
    List<TypeMirror> expected(TypeMirrors types) {
        if (expected == null) {
            expected = Stream.of(classes).map(type -> types.type(type)).toList();
        }
        
        return expected;
    }
    
}

final class MirrorType extends Type {
    
    final List<TypeMirror> expected;
    
    MirrorType(Relation relation, TypeMirror... types) {
        super(relation);
        expected = List.of(types);
    }

    @Override
    List<TypeMirror> expected(TypeMirrors types) {
        return expected;
    }
    
}
    
