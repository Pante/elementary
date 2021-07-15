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
package com.karuslabs.satisfactory;

import com.karuslabs.utilitary.text.Texts;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.*;
import javax.lang.model.element.Modifier;

import static com.karuslabs.utilitary.text.Texts.*;

/**
 * A skeletal implementation of an assertion for modifiers.
 */
public abstract class Modifiers implements Assertion<Set<Modifier>> {

    /**
     * The modifiers.
     */
    protected final Set<Modifier> modifiers;
    /**
     * The condition.
     */
    protected final String condition;
    
    /**
     * Creates a {@code Modifiers} with the given modifiers and condition.
     * 
     * @param modifiers the modifiers
     * @param condition the condition
     */
    public Modifiers(Set<Modifier> modifiers, String condition) {
        this.modifiers = modifiers;
        this.condition = condition;
    }
    
    @Override
    public String condition() {
        return condition;
    }
    
    @Override
    public Class<Modifier> part() {
        return Modifier.class;
    }

}

/**
 * An assertion that is satisfied if a set of modifiers contains the specified modifiers.
 */
class ContainsModifiers extends Modifiers {
    
    /**
     * Creates a {@code ContainsModifiers} with the given modifiers.
     * 
     * @param modifiers the modifiers
     */
    ContainsModifiers(Modifier... modifiers) {
        super(Set.of(modifiers), "contains [" + Texts.join(sort(modifiers), SCREAMING_CASE, " ") + "]");
    }

    @Override
    public boolean test(TypeMirrors types, Set<Modifier> modifiers) {
        return modifiers.containsAll(this.modifiers);
    }
    
}

/**
 * An assertion that is satisfied if a set of modifiers is equal to the specified modifiers.
 */
class EqualModifiers extends Modifiers {

    /**
     * Creates a {@code EqualModifiers} with the given modifiers.
     * 
     * @param modifiers the expected modifiers
     */
    EqualModifiers(Modifier... modifiers) {
        super(Set.of(modifiers), "equal [" + Texts.join(sort(modifiers), SCREAMING_CASE, " ") + "]");
    }
    
    @Override
    public boolean test(TypeMirrors types, Set<Modifier> modifiers) {
        return this.modifiers.equals(modifiers);
    }
    
}
