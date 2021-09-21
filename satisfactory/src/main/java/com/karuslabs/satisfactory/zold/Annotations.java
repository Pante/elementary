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
package com.karuslabs.satisfactory.zold;

import com.karuslabs.utilitary.text.Texts;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.lang.annotation.Annotation;
import java.util.function.BiConsumer;
import javax.lang.model.element.Element;

/**
 * A skeletal implementation of an assertion for annotations.
 */
public abstract class Annotations implements Assertion<Element> {
    
    /**
     * A format used to describe annotations.
     */
    public static final BiConsumer<Class<? extends Annotation>, StringBuilder> FORMAT = (type, builder) -> builder.append("@").append(type.getSimpleName());
    /**
     * The annotations.
     */
    protected final Class<? extends Annotation>[] annotations;
    private final String condition;
    
    /**
     * Creates an {@code Annotations} with the given annotations and condition.
     * 
     * @param annotations the annotations
     * @param condition the condition for satisfying this assertion
     */
    public Annotations(Class<? extends Annotation>[] annotations, String condition) {
        this.annotations = annotations;
        this.condition = condition;
    }
    
    @Override
    public String condition() {
        return condition;
    }
    
    @Override
    public Class<Annotation> part() {
        return Annotation.class;
    }

}

/**
 * An assertion that is satisfied if an element contains the specified annotations.
 */
class ContainsAnnotations extends Annotations {
    
    /**
     * Creates a {@code ContainsAnnotations} with the given annotations.
     * 
     * @param annotations the annotations that an element should contain
     */
    ContainsAnnotations(Class<? extends Annotation>... annotations) {
        super(annotations, "contains [" + Texts.join(annotations, FORMAT, ", ") + "]");
    }

    @Override
    public boolean test(TypeMirrors types, Element element) {
        for (var annotation : annotations) {
            if (element.getAnnotationsByType(annotation).length == 0) {
                return false;
            }
        }
        
        return true;
    }
    
}
