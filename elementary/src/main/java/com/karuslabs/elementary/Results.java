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
package com.karuslabs.elementary;

import com.karuslabs.annotations.ValueType;

import java.util.*;
import javax.tools.*;

/**
 * The results of a compilation.
 */
public @ValueType final class Results {
    
    /**
     * The source files which were compiled.
     */
    public final List<JavaFileObject> sources;
    /**
     * The generated source files.
     */
    public final List<JavaFileObject> generated;
    /**
     * The diagnostic messages.
     */
    public final List<Diagnostic<? extends JavaFileObject>> diagnostics;
    /**
     * The errors.
     */
    public final List<Diagnostic<? extends JavaFileObject>> errors;
    /**
     * The warnings.
     */
    public final List<Diagnostic<? extends JavaFileObject>> warnings;
    /**
     * The notes.
     */
    public final List<Diagnostic<? extends JavaFileObject>> notes;
    /**
     * Whether compilation was successful.
     */
    public final boolean success;
    
    /**
     * Creates a {@code Results} with the given arguments.
     * 
     * @param sources the sources which were compiled
     * @param generated the generated sources
     * @param diagnostics the diagnostic messages
     * @param success whether compilation was successful
     */
    public Results(List<JavaFileObject> sources, List<JavaFileObject> generated, Diagnostics diagnostics, boolean success) {
        this.sources = sources;
        this.generated = generated;
        this.diagnostics = diagnostics.all;
        errors = diagnostics.errors;
        warnings = diagnostics.warnings;
        notes = diagnostics.notes;
        this.success = success;
    }
    
    /**
     * Returns a {@code Finder} for this {@code Results}.
     * 
     * @return a {@code Finder}
     */
    public Finder find() {
        return new Finder(this);
    }
    
}
