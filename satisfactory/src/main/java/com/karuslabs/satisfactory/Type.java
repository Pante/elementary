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

/**
 * A skeleton implementation of a type assertion.
 */
public abstract class Type implements Assertion<TypeMirror> {
    
    protected final Relation relation;
    protected final TypeMirror[] expectations;
    private final String types;
    
    /**
     * Creates a {@code Type} with the given relation, expectations and types.
     * 
     * @param relation the expected relation between the types
     * @param expectations the expected types
     * @param types a string representation of the expected types used for describing
     *              this {@code Type}
     */
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
    
    /**
     * Represents the expected relationship between types.
     */
    public static abstract class Relation {
        
        /**
         * A {@code Relation} which expects the actual type to be exactly equal 
         * to the expected types.
         */
        public static final Relation IS = new Is();
        /**
         * A {@code Relation} which expects the actual type to be a subtype of
         * the expected types.
         */
        public static final Relation SUBTYPE = new Subtype();
        /**
         * A {@code Relation} which expects the actual type to be a supertype of
         * the expected types.
         */
        public static final Relation SUPERTYPE = new Supertype();
        
        public boolean test(TypeMirrors types, TypeMirror[] expectations, TypeMirror actual) {
            for (var expected : expectations) {
                if (!test(types, expected, actual)) {
                    return false;
                }
            }

            return true;
        }
        
        /**
         * Tests whether the relation between the actual and expected types using
         * the given {@code TypeMirrors} is expected.
         * 
         * @param types the types
         * @param expected the expected type
         * @param actual the actual type
         * @return {@code true} if relation the actual and expected types is expected
         */
        protected abstract boolean test(TypeMirrors types, TypeMirror expected, TypeMirror actual);

        public abstract String clause(String type);
        
        public abstract String clauses(String type);
        
    }
    
    /**
     * A {@code Relation} that expects the actual type to be exactly equal to the
     * expected type.
     */
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

    /**
     * A {@code Relation} that expects the actual type to be a subtype of the expected
     * type.
     */
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

    /**
     * A {@code Relation} that expects the actual type to be a supertype of the
     * expected type.
     */
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

/**
 * An assertion for primitive types.
 */
class Primitive implements Assertion<TypeMirror> {
    
    private final TypeKind kind;
    
    /**
     * Creates a {@code Primitive} with the given primitive kind.
     * 
     * @param kind the kind
     */
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

/**
 * A type assertion that tests an actual type against classes.
 */
class ClassType extends Type {

    /**
     * The format for describing classes.
     */
    static final BiConsumer<Class<?>, StringBuilder> FORMAT = (type, builder) -> builder.append(type.getSimpleName());
    
    @Nullable Class<?>[] classes;
    
    /**
     * Creates a {@code ClassType} with the given relation and classes.
     * 
     * @param relation the expected relation between types
     * @param classes the expected types
     */
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

/**
 * A type assertion that tests an actual type against {@code TypeMirror}s.
 */
class MirrorType extends Type {
    
    /**
     * The format for describing {@code TypeMirror}s.
     */
    static final BiConsumer<TypeMirror, StringBuilder> FORMAT = (type, builder) -> type.accept(TypePrinter.SIMPLE, builder);
    
    /**
     * Creates a {@code MirrorTyoe} with the given relation and expected types.
     * 
     * @param relation the expected relation between types
     * @param mirrors the expected types
     */
    MirrorType(Relation relation, TypeMirror... mirrors) {
        super(relation, mirrors, Texts.and(mirrors, FORMAT));
    }
    
    @Override
    public boolean test(TypeMirrors types, TypeMirror type) {
        return relation.test(types, expectations, type);
    }
    
}
