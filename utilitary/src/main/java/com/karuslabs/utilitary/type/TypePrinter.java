/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
package com.karuslabs.utilitary.type;

import com.karuslabs.utilitary.Texts;

import java.util.*;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.SimpleTypeVisitor9;

/**
 * A {@code TypeVisitor} that creates a string representation of the visited type's
 * name.
 */
public abstract class TypePrinter extends SimpleTypeVisitor9<Void, StringBuilder> {

    /**
     * Returns the fully qualified name of the given type.
     * 
     * @param type the type
     * @return the fully qualified name
     */
    public static String qualified(TypeMirror type) {
        var builder = new StringBuilder();
        type.accept(qualified(), builder);
        return builder.toString();
    }
    
    /**
     * Creates a {@code TypePrinter} that creates a string representation of a visited type's
     * fully qualified name.
     * 
     * @return a {@code TypePrinter}
     */
    public static final TypePrinter qualified() {
        return new QualifiedTypePrinter();
    }
    
    /**
     * Returns the simple name of the given type.
     * 
     * @param type the type
     * @return the simple name
     */
    public static String simple(TypeMirror type) {
        var builder = new StringBuilder();
        type.accept(simple(), builder);
        return builder.toString();
    }
    
    /**
     * Creates a {@code TypePrinter} that creates a string representation of a visited 
     * type's simple name.
     * 
     * @return a {@code TypePrinter}
     */
    public static TypePrinter simple() {
        return new SimpleTypePrinter();
    }
    
    private final Set<TypeMirror> visited = new HashSet<>();
    
    @Override
    public Void visitDeclared(DeclaredType type, StringBuilder builder) {
        rawType(type, builder);
        var arguments = type.getTypeArguments();
        if (!arguments.isEmpty()) {
            builder.append('<');
            for (int i = 0; i < arguments.size() - 1; i++) {
                arguments.get(i).accept(this, builder);
                builder.append(", ");
            }
            arguments.get(arguments.size() - 1).accept(this, builder);
            builder.append('>');
        }

        return null;
    }
    
    @Override
    public Void visitTypeVariable(TypeVariable variable, StringBuilder builder) {
        builder.append(variable.asElement().getSimpleName());
        if (!visited.add(variable)) {
            return null;
        }
        
        // We do this to ignore the default <T extends Object> upper bound
        var upper = variable.getUpperBound();
        if (!TypeMirrors.is(upper, Object.class)) {
            upper.accept(this, builder.append(" extends "));
        }
        
        // Generics can never have lower bounds
        
        return null;
    }
    
    @Override
    public Void visitWildcard(WildcardType type, StringBuilder builder) {
        builder.append('?');
        
        var extension = type.getExtendsBound();
        if (extension != null) {
            extension.accept(this, builder.append(" extends "));
        }
        
        var superBound = type.getSuperBound();
        if (superBound != null) {
            superBound.accept(this, builder.append(" super "));
        }
        
        return null;
    }
    
    @Override
    public Void visitIntersection(IntersectionType intersection, StringBuilder builder) {
        Texts.join(builder, intersection.getBounds(), (bound, sb) -> {
            bound.accept(this, builder);
        }, " & ");
        
        return null;
    }
    
    @Override
    public Void visitArray(ArrayType type, StringBuilder builder) {
        type.getComponentType().accept(this, builder);
        builder.append("[]");
        return null;
    }
    
    @Override
    public Void visitPrimitive(PrimitiveType type, StringBuilder builder) {
        builder.append(type.getKind().toString().toLowerCase());
        return null;
    }
    
    @Override
    public Void visitNoType(NoType type, StringBuilder builder) {
        builder.append("void");
        return null;
    }
    
    @Override
    protected Void defaultAction(TypeMirror type, StringBuilder builder) {
        builder.setLength(0);
        throw new UnsupportedOperationException("TypePrinter does not support " + type.getKind());
    }
    
    /**
     * Builds a string representation of the given raw type.
     * 
     * @param type the raw type
     * @param builder the {@code StringBuildeer}
     */
    protected abstract void rawType(DeclaredType type, StringBuilder builder);
    
}

/**
 * A {@code TypePrinter} that creates a string representation of a visited type's
 * fully qualified name.
 */
class QualifiedTypePrinter extends TypePrinter {
    @Override
    protected void rawType(DeclaredType type, StringBuilder builder) {
        if (type.asElement() instanceof TypeElement) {
            var element = (TypeElement) type.asElement();
            builder.append(element.getQualifiedName());
        } else {
            throw new IllegalStateException("DeclaredType should be a TypeElement");
        }
    }
}

/**
 * A {@code TypePrinter} that creates a string representation of a visited type's
 * simple name.
 */
class SimpleTypePrinter extends TypePrinter {
    @Override
    protected void rawType(DeclaredType type, StringBuilder builder) {
        if (type.asElement() instanceof TypeElement) {
            var element = (TypeElement) type.asElement();
            var pack = element.accept(Find.PACKAGE, null).getQualifiedName().toString();
            var qualified = element.getQualifiedName().toString();
            
            if (pack.isEmpty()) {
                builder.append(qualified);
                
            } else {
                builder.append(qualified.substring(pack.length() + 1));
            }
            
        } else {
            throw new IllegalStateException("DeclaredType should be a TypeElement");
        }
    }
}
