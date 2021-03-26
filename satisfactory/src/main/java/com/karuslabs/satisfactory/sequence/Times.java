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
package com.karuslabs.satisfactory.sequence;

import com.karuslabs.satisfactory.Part;
import com.karuslabs.satisfactory.*;
import com.karuslabs.utilitary.type.TypeMirrors;

public abstract class Times<T> implements Part {

    private final Assertion<T> assertion;
    private final String condition;
    private int current = 0;
    
    public Times(Assertion<T> assertion, String condition) {
        this.assertion = assertion;
        this.condition = condition;
    }
    
    public boolean test() {
        var valid = test(current);
        current = 0;
        return valid;
    }
    
    protected abstract boolean test(int current);
    
    public boolean add(TypeMirrors types, T value) {
        if (assertion.test(types, value)) {
            current++;
            return true;
            
        } else {
            return false;
        }
    }
    
    public String condition() {
        return condition;
    }
    
    @Override
    public Class<?> type() {
        return assertion.type();
    }
    
}

class Exact<T> extends Times<T> {

    static String format(int times, Assertion<?> assertion) {
        if (times == 1) {
            return "1 " + assertion.condition();
            
        } else {
            return times + " " + assertion.conditions();
        }
    }
    
    private final int times;
    
    public Exact(int times, Assertion<T> assertion) {
        super(assertion, format(times, assertion));
        this.times = times;
    }

    @Override
    protected boolean test(int current) {
        return times == current;
    }
    
}

class Range<T> extends Times<T> {
    
    private final int min;
    private final int max;
    
    Range(int min, int max, Assertion<T> assertion) {
        super(assertion, min + " to " + max + " " + assertion.conditions());
        this.min = min;
        this.max = max;
    }

    @Override
    protected boolean test(int current) {
        return current >= min && current < max;
    }
    
}

class Min<T> extends Times<T> {
    
    private final int min;
    
    Min(int min, Assertion<T> assertion) {
        super(assertion, min + " or more " + assertion.conditions());
        this.min = min;
    }

    @Override
    protected boolean test(int current) {
        return current >= min;
    }
    
}

class Max<T> extends Times<T> {
    
    private final int max;
    
    Max(int max, Assertion<T> assertion) {
        super(assertion, "less than " + max + " " + assertion.conditions());
        this.max = max;
    }

    @Override
    protected boolean test(int current) {
        return current < max;
    }
    
}
