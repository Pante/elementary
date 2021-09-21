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

import java.util.Collection;

/**
 * A {@code Sequence} that expects each value in a collection to be called a certain 
 * number of times.
 * 
 * @param <T> the type of the tested values
 */
public abstract class TimeSequence<T> extends Sequence<T> {
    
    /**
     * Formats the given {@code Times}.
     * 
     * @param times the times
     * @return a formatted description of the given {@code Times}
     */
    static String format(Times<?>... times) {
        return "[" + Texts.join(times, (time, builder) -> builder.append(time.condition()), ", ") + "]";
    }
    
    /**
     * The {@code Times} that must be satisfied.
     */
    protected final Times<T>[] times;
    
    /**
     * Creates a {@code TimeSequence} with the given prefix and times.
     * 
     * @param prefix the prefix for the condition
     * @param times the times that needs to be satisfied
     */
    public TimeSequence(String prefix, Times<T>... times) {
        super(prefix + " " + format(times));
        this.times = times;
    }
    
}

/**
 * A {@code Sequence} that is satisfied if a sequence of elements contains all
 * {@code Times}. The order of the sequence of elements is ignored.
 * 
 * @param <T> the type of the tested values
 */
class ContainsSequence<T> extends TimeSequence<T> {
    
    /**
     * Creates a {@code ContainsSequence} with the given times.
     * 
     * @param times the times
     */
    ContainsSequence(Times<T>... times) {
        super("contains", times);
    }

    @Override
    public boolean test(TypeMirrors types, Collection<? extends T> values) {
        for (var value : values) {
            for (var time : times) {
                time.test(types, value);
            }
        }
        
        for (var time : times) {
            if (!time.times()) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public Class<?> part() {
        return times[0].part();
    }
    
}

/**
 * A {@code Sequence} that is satisfied if a sequence of elements is equal to this
 * sequence of {@code Times}. The order of the sequence of elements is ignored.
 * 
 * @param <T> the type of the tested values
 */
class EqualTimeSequence<T> extends TimeSequence<T> {

    /**
     * Creates a {@code EqualTimeSequence} with the given times.
     * 
     * @param times the times
     */
    EqualTimeSequence(Times<T>... times) {
        super("equal", times);
    }
    
    @Override
    public boolean test(TypeMirrors types, Collection<? extends T> values) {
        for (var value : values) {
            var equal = false;
            for (var time : times) {
                equal |= time.test(types, value);
            }
            
            if (!equal) {
                return false;
            }
        }
        
        for (var time : times) {
            if (!time.times()) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public Class<?> part() {
        return times[0].part();
    }
    
}
