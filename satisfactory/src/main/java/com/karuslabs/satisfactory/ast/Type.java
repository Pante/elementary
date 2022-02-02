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
package com.karuslabs.satisfactory.ast;

import com.karuslabs.satisfactory.*;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.*;
import java.util.stream.Stream;
import javax.lang.model.type.*;

import org.checkerframework.checker.nullness.qual.Nullable;

import static com.karuslabs.satisfactory.ast.Type.Relation.*;

public sealed abstract class Type implements Assertion<TypeMirror> {
    
    public static Assertion<TypeMirror> ANY_TYPE = (value, types) -> Result.TRUE;
    
    public static Assertion<TypeMirror> equal(Class<?> type) {
        return type.isPrimitive() ? new Primitive(TypeMirrors.kind(type)) : new ClassType(IS, type);
    }
    
    public static Type equal(Class<?>... types) {
        return new ClassType(IS, types);
    }
    
    public static Type equal(TypeMirror... types) {
        return new MirrorType(IS, types);
    }
    
    public static Type subtype(Class<?>... parents) {
        return new ClassType(SUBTYPE, parents);
    }
    
    public static Type subtype(TypeMirror... parents) {
        return new MirrorType(SUBTYPE, parents);
    }
    
    public static Type supertype(Class<?>... children) {
        return new ClassType(SUBTYPE, children);
    }
    
    public static Type supertype(TypeMirror... children) {
        return new MirrorType(SUBTYPE, children);
    }

    
    public static enum Relation {
        IS {
            @Override
            boolean test(TypeMirror actual, TypeMirror expected, TypeMirrors types) {
                return types.isSameType(expected, actual);
            }
        },
        
        SUBTYPE {
            @Override
            boolean test(TypeMirror actual, TypeMirror expected, TypeMirrors types) {
                return types.isSubtype(actual, expected);
            }
        },
        
        SUPERTYPE {
            @Override
            boolean test(TypeMirror actual, TypeMirror expected, TypeMirrors types) {
                return types.isSubtype(expected, actual);
            }
        };
        
        abstract boolean test(TypeMirror actual, TypeMirror expected, TypeMirrors types);
    }
    
    final Relation relation;
    
    Type(Relation relation) {
        this.relation = relation;
    }
    
    @Override
    public Result test(TypeMirror actual, TypeMirrors types) {
        var success = true;
        for (var type : expected(types)) {
            if (!relation.test(actual, type, types)) {
                success = false;
                break;
            }
        }

        return new Result.AST.Type(actual, relation, expected(types), success);
    }
    
    abstract List<TypeMirror> expected(TypeMirrors types);
    
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

record Primitive(TypeKind kind) implements Assertion<TypeMirror> {
    @Override
    public Result test(TypeMirror actual, TypeMirrors types) {
        return new Result.AST.Primitive(actual.getKind(), kind, actual.getKind() == kind);
    }
}
    
