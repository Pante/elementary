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
package com.karuslabs.utilitary;

import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;

/**
 * A skeletal implementation of an annotation processor that  
 */
public abstract class ElementProcessor extends AnnotationProcessor {

    /**
     * Finds and processes all elements in the current round that is annotated with
     * any of the given annotations. Processing of each element is delegated to
     * {@link #process{Element}}. Tearing down is subsequently delegated to {@link #clear}.
     * 
     * @param annotations the annotations
     * @param round the current round
     * @return {@code false}
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment round) {
        for (var element : round.getElementsAnnotatedWithAny(annotations.toArray(TypeElement[]::new))) {
            process(element);
        }
        clear();
        
        return false;
    }
    
    /**
     * Processes the given annotated element.
     * 
     * @param element the element
     */
    protected abstract void process(Element element);
    
    /**
     * Tears down the this annotation processor after each round of processing.
     */
    protected void clear() {}
    
}
