/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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

import com.karuslabs.annotations.*;
import com.karuslabs.utilitary.type.TypeMirrors;

import javax.annotation.processing.*;
import javax.lang.model.util.*;

/**
 * A skeletal implementation of an annotation processor that contains fields 
 * for accessing the annotation processing environment.
 */
public abstract class AnnotationProcessor extends AbstractProcessor {
    
    /**
     * The {@code Elements} in the current annotation processing environment.
     * Not available until {@link #init(ProcessingEnvironment)} has been called.
     */
    protected @Lazy Elements elements;
    /**
     * The {@code TypeMirrors} in the current annotation processing environment.
     * Not available until {@link #init(ProcessingEnvironment)} has been called.
     */
    protected @Lazy TypeMirrors types;
    /**
     * The {@code Logger} in the current annotation processing environment.
     * Not available until {@link #init(ProcessingEnvironment)} has been called.
     */
    protected @Lazy Logger logger;
    
    /**
     * Initializes this annotation processor and its fields.
     * <br>
     * <b>Subclasses that override this method must call it's superclass's method.</b>
     * 
     * @param environment the processing environment
     */
    @Override
    public void init(ProcessingEnvironment environment) {
        super.init(environment);
        elements = environment.getElementUtils();
        types = new TypeMirrors(elements, environment.getTypeUtils());
        logger = new Logger(environment.getMessager());
    }
    
}
