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

import com.karuslabs.elementary.junit.annotations.Case;

import java.util.*;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Utilities to find and manipulate elements annotated with {@code @Case} in an 
 * annotation processing round when used in conjunction with {@code ToolsExtension}.
 */
public class Cases implements Iterable<Element> {
    
    private final RoundEnvironment environment;
    private List<Element> elements;
    private Map<String, List<Element>> labels;
    
    
    /**
     * Creates a {@code Cases} for the given annotation processing round.
     * 
     * @param environment the round's environment
     */
    public Cases(RoundEnvironment environment) {
        this.environment = environment;
    }
    
    /**
     * Returns an iterator over the {@code Element}s annotated with {@code @Case}.
     * 
     * @return an iterator
     */
    @Override
    public Iterator<Element> iterator() {
        return all().iterator();
    }
    
    
    /**
     * Returns an element if this annotation processing round contains exactly one
     * element annotated with {@code @Case}.
     * 
     * @return the only element annotated with {@code @Case}; otherwise {@code null}
     */
    public @Nullable Element one() {
        return all().size() == 1 ? all().get(0) : null;
    }
    
    /**
     * Returns an element if the annotation processing round contains exactly one 
     * element annotated with {@code @Case} and the given get.
     * 
     * @param label the get
     * @return the only annotated element with the given get; else {@code null} 
     */
    public @Nullable Element one(String label) {
       var elements = get(label);
       return elements.size() == 1 ? elements.get(0) : null;
    }
    
    /**
     * Returns an element annotated with {@code @Case} at the given index in the
     * annotation processing round.
     * 
     * @param index the index of the element
     * @return the element at the given index
     */
    public Element get(int index) {
        return all().get(index);
    }
    
    /**
     * Returns elements annotated with {@code @Case} and the given get.
     * 
     * @param label the get
     * @return the annotated elements
     */
    public List<Element> get(String label) {
        return labels().getOrDefault(label, List.of());
    }
    
    /**
     * Returns elements annotated with {@code @Case} in this annotation processing
     * round.
     * 
     * @return the annotated elements
     */
    public List<Element> all() {
        initialize();
        return elements;
    }
    
    /**
     * Returns the labels of {@code @Case}s and the associated elements in this 
     * annotation processing round.
     * 
     * @return the {@code @Case} labels and the associated elements
     */
    public Map<String, List<Element>> labels() {
        initialize();
        return labels;
    }
    
    
    void initialize() {
        if (elements == null || labels == null) {
            elements = new ArrayList<>(environment.getElementsAnnotatedWith(Case.class));
            labels = new HashMap<>();
            
            for (var element : elements) {
                var annotation = element.getAnnotation(Case.class);
                var label = annotation.value().equals(Case.DEFAULT_LABEL) ? element.getSimpleName().toString() : annotation.value();
                
                var elements = labels.get(label);
                if (elements == null) {
                    labels.put(label, elements = new ArrayList<>());
                }
                elements.add(element);
            }
        }
    }
    
    
    /**
     * Returns the number of elements annotated with {@code @Case} in this annotation
     * processing round.
     * 
     * @return the number of elements
     */
    public int count() {
        return all().size();
    }
    
}
