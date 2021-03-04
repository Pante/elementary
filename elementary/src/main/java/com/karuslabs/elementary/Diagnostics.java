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
 * The diagnostic messages for the result of a compilation.
 */
public @ValueType final class Diagnostics implements DiagnosticListener<JavaFileObject> {
    
    /**
     * All diagnostics messages.
     */
    public final List<Diagnostic<? extends JavaFileObject>> all = new ArrayList<>();
    /**
     * All errors.
     */
    public final List<Diagnostic<? extends JavaFileObject>> errors = new ArrayList<>();
    /**
     * Mandatory and non-mandatory warnings.
     */
    public final List<Diagnostic<? extends JavaFileObject>> warnings = new ArrayList<>();
    /**
     * All notes.
     */
    public final List<Diagnostic<? extends JavaFileObject>> notes = new ArrayList<>();
    
    @Override
    public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
        all.add(diagnostic);
        switch (diagnostic.getKind()) {
            case ERROR:
                errors.add(diagnostic);
                return;

            case MANDATORY_WARNING:
            case WARNING:
                warnings.add(diagnostic);
                return;

            case NOTE:
                notes.add(diagnostic);
        }
    } 

}
