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

import com.karuslabs.utilitary.Texts;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.Collection;

public abstract class TimeSequence<T> extends Sequence<T> {
    
    static String format(Times<?>... times) {
        return Texts.join(times, (time, builder) -> builder.append('[').append(time.condition()).append(']'), ", ");
    }
    
    protected final Times<T>[] times;
    
    public TimeSequence(String prefix, Times<T>... times) {
        super(prefix + " " + format(times));
        this.times = times;
    }
    
}

class ContainsSequence<T> extends TimeSequence<T> {
    
    ContainsSequence(Times<T>... times) {
        super("contains", times);
    }

    @Override
    public boolean test(TypeMirrors types, Collection<? extends T> values) {
        for (var value : values) {
            for (var time : times) {
                time.add(types, value);
            }
        }
        
        for (var time : times) {
            if (!time.test()) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public Class<?> type() {
        return times[0].type();
    }
    
}

class EqualTimeSequence<T> extends TimeSequence<T> {

    EqualTimeSequence(Times<T>... times) {
        super("equal", times);
    }
    
    @Override
    public boolean test(TypeMirrors types, Collection<? extends T> values) {
        for (var value : values) {
            var equal = false;
            for (var time : times) {
                equal |= time.add(types, value);
            }
            
            if (!equal) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public Class<?> type() {
        return times[0].type();
    }
    
}
