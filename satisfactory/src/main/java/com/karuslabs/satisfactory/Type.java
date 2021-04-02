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

import com.karuslabs.utilitary.Texts;
import com.karuslabs.utilitary.type.*;

import java.util.function.BiConsumer;
import javax.lang.model.type.*;

import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class Type implements Assertion<TypeMirror> {
    
    protected final Relation relation;
    protected final TypeMirror[] expectations;
    private final String types;
    
    public Type(Relation relation, TypeMirror[] expectations, String types) {
        this.relation = relation;
        this.expectations = expectations;
        this.types = types;
    }
    
    @Override
    public String condition() {
        return relation.clause(types);
    }
    
    @Override
    public String conditions() {
        return relation.clauses(types);
    }
    
    @Override
    public Class<TypeMirror> type() {
        return TypeMirror.class;
    }
    
    public static abstract class Relation {
        
        public static final Relation IS = new Is();
        public static final Relation SUBTYPE = new Subtype();
        public static final Relation SUPERTYPE = new Supertype();
        
        public boolean test(TypeMirrors types, TypeMirror[] expectations, TypeMirror actual) {
            for (var expected : expectations) {
                if (!test(types, expected, actual)) {
                    return false;
                }
            }

            return true;
        }

        protected abstract boolean test(TypeMirrors types, TypeMirror expected, TypeMirror actual);

        public abstract String clause(String type);
        
        public abstract String clauses(String type);
        
    }
    
    public static class Is extends Relation {
        @Override
        public boolean test(TypeMirrors types, TypeMirror expected, TypeMirror actual) {
            return types.isSameType(expected, actual);
        }

        @Override
        public String clause(String type) {
            return type;
        }
        
        @Override
        public String clauses(String type) {
            return type + "s";
        }
    }

    public static class Subtype extends Relation {
        @Override
        public boolean test(TypeMirrors types, TypeMirror expected, TypeMirror actual) {
            return types.isSubtype(actual, expected);
        }

        @Override
        public String clause(String type) {
            return "subtype of " + type;
        }
        
        @Override
        public String clauses(String type) {
            return "subtypes of " + type;
        }
    }

    public static class Supertype extends Relation {
        @Override
        public boolean test(TypeMirrors types, TypeMirror expected, TypeMirror actual) {
            return types.isSubtype(expected, actual);
        }

        @Override
        public String clause(String type) {
            return "supertype of " + type;
        }
        
        @Override
        public String clauses(String type) {
            return "supertypes of " + type;
        }
    }
    
}

class Primitive implements Assertion<TypeMirror> {
    
    private final TypeKind kind;
    
    Primitive(TypeKind kind) {
        this.kind = kind;
    }

    @Override
    public boolean test(TypeMirrors types, TypeMirror element) {
        return element.getKind() == kind;
    }
    
    @Override
    public String condition() {
        return kind.toString().toLowerCase();
    }
    
    @Override
    public String conditions() {
        return kind.toString().toLowerCase() + "s";
    }
    
    @Override
    public Class<TypeMirror> type() {
        return TypeMirror.class;
    }
    
}

class ClassType extends Type {

    static final BiConsumer<Class<?>, StringBuilder> FORMAT = (type, builder) -> builder.append(type.getSimpleName());
    
    @Nullable Class<?>[] classes;
    
    ClassType(Relation relation, Class<?>... classes) {
        super(relation, new TypeMirror[classes.length], Texts.and(classes, FORMAT));
        this.classes = classes;
    }
    
    @Override
    public boolean test(TypeMirrors types, TypeMirror type) {
        if (classes != null) {
            for (int i = 0; i < classes.length; i++) {
                expectations[i] = types.type(classes[i]);
            }
            classes = null;
        }
        
        return relation.test(types, expectations, type);
    }
    
}

class MirrorType extends Type {
    
    static final BiConsumer<TypeMirror, StringBuilder> FORMAT = (type, builder) -> type.accept(TypePrinter.SIMPLE, builder);
    
    MirrorType(Relation relation, TypeMirror... mirrors) {
        super(relation, mirrors, Texts.and(mirrors, FORMAT));
    }
    
    @Override
    public boolean test(TypeMirrors types, TypeMirror type) {
        return relation.test(types, expectations, type);
    }
    
}
