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

import com.karuslabs.satisfactory.*;
import com.karuslabs.satisfactory.sequence.Sequence.*;
import com.karuslabs.utilitary.type.TypeMirrors;
import java.util.function.Supplier;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

public class Method implements Assertion<ExecutableElement> {
    
    private final Ordered<AnnotationMirror> annotations;
    private final Unordered<Modifier> modifiers;
    private final Ordered<TypeMirror> bounds;
    private final Assertion<TypeMirror> type;
    private final Assertion<String> name;
    private final Ordered<VariableElement> parameters;
    private final Ordered<TypeMirror> thrown;
    
    Method(
        Ordered<AnnotationMirror> annotations, Unordered<Modifier> modifiers, Ordered<TypeMirror> bounds,
        Assertion<TypeMirror> type, Assertion<String> name, Ordered<VariableElement> parameters, Ordered<TypeMirror> thrown
    ) {
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.bounds = bounds;
        this.type = type;
        this.name = name;
        this.parameters = parameters;
        this.thrown = thrown;
    }
    
    @Override
    public Result test(ExecutableElement executable, TypeMirrors types) {
        var annotations = this.annotations.test(executable.getAnnotationMirrors(), types);
        var modifiers = this.modifiers.test(executable.getModifiers(), types);
        var bounds = this.bounds.test(executable.getTypeParameters().stream().map(TypeParameterElement::asType).toList(), types);
        var type = this.type.test(executable.asType(), types);
        var name = this.name.test(executable.getSimpleName().toString(), types);
        var parameters = this.parameters.test(executable.getParameters(), types);
        var thrown = this.thrown.test(executable.getThrownTypes(), types);
        
        return new Result.AST.Method(
            executable, 
            annotations, 
            modifiers, 
            bounds,
            type, 
            name,
            parameters,
            thrown,
            annotations.success() && modifiers.success() && bounds.success() && type.success() && name.success() && parameters.success() && thrown.success()
        );
    }
    
    public static class Builder implements Supplier<Method> {

        private Ordered<AnnotationMirror> annotations = Ordered.any();
        private Unordered<Modifier> modifiers = Unordered.any();
        private Ordered<TypeMirror> bounds = Ordered.any();
        private Assertion<TypeMirror> type = Assertion.any();
        private Assertion<String> name = Assertion.any();
        private Ordered<VariableElement> parameters;
        private Ordered<TypeMirror> thrown;
        
        public Builder annotations(Ordered<AnnotationMirror> annotations) {
            this.annotations = annotations;
            return this;
        }
        
        public Builder modifiers(Unordered<Modifier> modifiers) {
            this.modifiers = modifiers;
            return this;
        }
        
        public Builder bounds(Ordered<TypeMirror> bounds) {
            this.bounds = bounds;
            return this;
        }
        
        public Builder type(Assertion<TypeMirror> type) {
            this.type = type;
            return this;
        }
        
        public Builder name(Assertion<String> name) {
            this.name = name;
            return this;
        }
        
        public Builder parameters(Ordered<VariableElement> parameters) {
            this.parameters = parameters;
            return this;
        }
        
        public Builder thrown(Ordered<TypeMirror> thrown) {
            this.thrown = thrown;
            return this;
        }
        
        @Override
        public Method get() {
            return new Method(annotations, modifiers, bounds, type, name, parameters, thrown);
        }
        
    }
    
}
