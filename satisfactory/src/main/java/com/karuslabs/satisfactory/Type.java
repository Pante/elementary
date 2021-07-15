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

import com.karuslabs.utilitary.text.Texts;
import com.karuslabs.utilitary.type.*;

import java.util.function.BiConsumer;
import javax.lang.model.type.*;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A skeleton implementation of a type assertion.
 */
public abstract class Type implements Assertion<TypeMirror> {
    
    /**
     * The expected relationship.
     */
    protected final Relation relation;
    /**
     * The expected types.
     */
    protected final TypeMirror[] expectations;
    private final String types;
    
    /**
     * Creates a {@code Type} with the given relation, expectations and types.
     * 
     * @param relation the expected relation between the types
     * @param expectations the expected types
     * @param types a formatted description of the expected types
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
    public Class<TypeMirror> part() {
        return TypeMirror.class;
    }
    
    /**
     * Represents the expected relationship between types.
     */
    public static abstract class Relation {
        
        /**
         * A {@code Relation} that expects the actual type and expected types to be equal.
         */
        public static final Relation IS = new Is();
        /**
         * A {@code Relation} which expects the actual type to be a subtype of the 
         * expected types.
         */
        public static final Relation SUBTYPE = new Subtype();
        /**
         * A {@code Relation} which expects the actual type to be a supertype of
         * the expected types.
         */
        public static final Relation SUPERTYPE = new Supertype();
        
        /**
         * Tests the relationship between the actual and expected types using the
         * given {@code TypeMirrors}.
         * 
         * @param types the {@code TypeMirrors}
         * @param expectations the expected types
         * @param actual the actual type
         * @return {@code true} if the relationship between the actual and expected
         *         types is expected
         */
        public boolean test(TypeMirrors types, TypeMirror[] expectations, TypeMirror actual) {
            for (var expected : expectations) {
                if (!test(types, expected, actual)) {
                    return false;
                }
            }

            return true;
        }
        
        /**
         * Tests the relationship between the actual and expected types using the
         * given {@code TypeMirrors}.
         * 
         * @param types the types
         * @param expected the expected type
         * @param actual the actual type
         * @return {@code true} if relationship the actual and expected type is expected
         */
        protected abstract boolean test(TypeMirrors types, TypeMirror expected, TypeMirror actual);

        /**
         * Returns the expected clause for the given type.
         * 
         * @param type the type
         * @return a clause that describes the the expected relationship between
         *         the actual and given types
         */
        public abstract String clause(String type);
        
        /**
         * Returns the expected clauses for the given type.
         * 
         * @param type the type
         * @return the clauses that describe the expected relationship between the
         *         actual and given types
         */
        public abstract String clauses(String type);
        
    }
    
    /**
     * A {@code Relation} that expects the actual and expected types to be equal.
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
     * Creates a {@code Primitive} of the given kind.
     * 
     * @param kind the type kind
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
    public Class<TypeMirror> part() {
        return TypeMirror.class;
    }
    
}

/**
 * A type assertion that represents expected types as {@code Class}es.
 */
class ClassType extends Type {

    /**
     * The format used to describe classes.
     */
    static final BiConsumer<Class<?>, StringBuilder> FORMAT = (type, builder) -> builder.append(type.getSimpleName());
    
    @Nullable Class<?>[] classes;
    
    /**
     * Creates a {@code ClassType} with the given relation and expected classes.
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
 * A type assertion that represents expected types as {@code TypeMirror}s.
 */
class MirrorType extends Type {
    
    /**
     * The format used to describe {@code TypeMirror}s.
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
