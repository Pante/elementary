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
package com.karuslabs.elementary.junit;

import com.karuslabs.annotations.Lazy;
import com.karuslabs.elementary.junit.annotations.Case;

import java.util.*;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents the elements annotated with {@link Case} in an annotation processing
 * round when used in conjunction with {@code ToolsExtension}.
 */
public class Cases implements Iterable<Element> {
    
    private final RoundEnvironment environment;
    private @Lazy List<Element> elements;
    
    /**
     * Creates a {@code Cases} for the given round.
     * 
     * @param environment the round's environment
     */
    public Cases(RoundEnvironment environment) {
        this.environment = environment;
    }
    
    /**
     * Returns an iterator over {@code Element}s.
     * 
     * @return an iterator
     */
    @Override
    public Iterator<Element> iterator() {
        return list().iterator();
    }
    
    
    /**
     * Returns an element annotated with {@code @Case} if the annotation processing
     * round contains exactly one such element.
     * 
     * @return the only element annotated with {@code @Case}; otherwise {@code null}
     */
    public @Nullable Element one() {
        return list().size() == 1 ? list().get(0) : null;
    }
    
    /**
     * Returns an element annotated with {@code @Case} if the annotation processing
     * round contains exactly one such element which label exactly matches the given
     * label.
     * 
     * @param label the label
     * @return the only annotated element which label exactly matches the given label; else {@code null} 
     */
    public @Nullable Element one(String label) {
       var elements = get(label);
       return elements.size() == 1 ? elements.get(0) : null;
    }
    
    /**
     * Returns an element annotated with {@code @Case} at the given index in the
     * annotation processing round.
     * 
     * @param index the index
     * @return the element at the given index
     */
    public Element get(int index) {
        return list().get(index);
    }
    
    /**
     * Returns elements annotated with {@code @Case} which label exactly matches
     * the given label.
     * 
     * @param label the label
     * @return the annotated elements
     */
    public List<Element> get(String label) {
        var matches = new ArrayList<Element>();
        for (var element : list()) {
            if (element.getAnnotation(Case.class).value().equals(label)) {
                matches.add(element);
            }
        }
        
        return matches;
    }
    
    
    /**
     * Returns elements annotated with {@code @Case} in the annotation processing
     * round.
     * 
     * @return the annotated elements
     */
    public List<Element> list() {
        if (elements == null) {
            elements = new ArrayList<>(environment.getElementsAnnotatedWith(Case.class));
        }
        
        return elements;
    }
    
    /**
     * Returns the number of elements annotated with {@code @Case} in the annotation
     * processing round.
     * 
     * @return the number of elements
     */
    public int count() {
        return list().size();
    }

}
