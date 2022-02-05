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
import com.karuslabs.satisfactory.sequence.Sequence;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.function.Supplier;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

public class Variable implements Assertion<VariableElement> {

    public static Builder variable() {
        return new Builder();
    }
    
    private final Sequence.Ordered<AnnotationMirror> annotations;
    private final Sequence.Unordered<Modifier> modifiers;
    private final Assertion<TypeMirror> type;
    private final Assertion<String> name;
    
    Variable(Sequence.Ordered<AnnotationMirror> annotations, Sequence.Unordered<Modifier> modifiers, Assertion<TypeMirror> type, Assertion<String> name) {
        this.annotations = annotations;
        this.modifiers = modifiers;
        this.type = type;
        this.name = name;
    }

    @Override
    public Result test(VariableElement variable, TypeMirrors types) {
        var annotations = this.annotations.test(variable.getAnnotationMirrors(), types);
        var modifiers = this.modifiers.test(variable.getModifiers(), types);
        var type = this.type.test(variable.asType(), types);
        var name = this.name.test(variable.getSimpleName().toString(), types);
        
        return new Result.AST.Variable(
            variable, 
            annotations, 
            modifiers, 
            type, 
            name, 
            annotations.success() && modifiers.success() && type.success() && name.success()
        );
    }
    
    public static class Builder implements Supplier<Variable> {

        private Sequence.Ordered<AnnotationMirror> annotations = Sequence.Ordered.any();
        private Sequence.Unordered<Modifier> modifiers = Sequence.Unordered.any();
        private Assertion<TypeMirror> type = Assertion.any();
        private Assertion<String> name = Assertion.any();
        
        public Builder annotations(Sequence.Ordered<AnnotationMirror> annotations) {
            this.annotations = annotations;
            return this;
        }
        
        public Builder modifiers(Sequence.Unordered<Modifier> modifiers) {
            this.modifiers = modifiers;
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
        
        @Override
        public Variable get() {
            return new Variable(annotations, modifiers, type, name);
        }
        
    }
    
}
