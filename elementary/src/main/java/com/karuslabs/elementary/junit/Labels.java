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

import java.util.*;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;

import com.karuslabs.elementary.junit.annotations.Label;

/**
 * Utilities to find and manipulate elements annotated with {@code @Label} in an 
 * annotation processing round when used in conjunction with {@code ToolsExtension}.
 */
public class Labels implements Iterable<Map.Entry<String, Element>> {
    
    private final RoundEnvironment environment;
    private Map<String, Element> all;
    private Map<String, Map<String, Element>> groups;
    
    
    /**
     * Creates a {@code Labels} for the given annotation processing round.
     * 
     * @param environment the round's environment
     */
    public Labels(RoundEnvironment environment) {
        this.environment = environment;
    }

    /**
     * Returns an element annotated with the given label in the annotation processing round.
     * 
     * @param label the index of the element
     * @return the element at the given index
     * @throws NoSuchElementException if no element with the given label exists
     */
    public Element get(String label) {
        var element = all().get(label);
        if (element == null) {
            throw new NoSuchElementException("An element annotated with @Label(\"" + label + "\") does not exist. Did you spell the label correctly?");
        }
        
        return element;
    }
    
    /**
     * Returns an element if this annotation processing round contains exactly a 
     * single element annotated with {@code @Label}.
     * 
     * @return the only element annotated with {@code @Label}; otherwise {@code null}
     * @throws IllegalStateException if there is no elements or more than 1 elements
     */
    public Element single() {
        var elements = all().values();
        if (elements.size() == 1) {
            return elements.toArray(Element[]::new)[0];
            
        } else {
            throw new IllegalStateException("Labels.single() can only be called if 1 element is annotated with @Label. However, there are " + elements.size() + " elements annotated with @Label.");
        }
    }

    /**
     * Returns elements annotated with {@code @Label} and the given get.
     * 
     * @param group the get
     * @return the annotated elements
     * @throws NoSuchElementException if no elements with the given group exists
     */
    public Map<String, Element> group(String group) {
        var elements = groups().get(group);
        if (elements == null) {
            throw new NoSuchElementException("The group, \"" + group + "\" does not exist. Did you spell the group correctly?");
        }
    
        return elements;
    }
    
    
    /**
     * Returns all elements annotated with {@code @Label} in this annotation processing
     * round.
     * 
     * @return the annotated elements
     */
    public Map<String, Element> all() {
        initialize();
        return all;
    }
    
    
    /**
     * Returns all elements annotated with {@code @Label} in this annotation processing
     * round.
     * 
     * @return the annotated elements
     */
    public Map<String, Map<String, Element>> groups() {
        initialize();
        return groups;
    }
    

    
    void initialize() {
        if (all == null || groups == null) {
            all = new HashMap<>();
            groups = new HashMap<>();
            
            for (var element : environment.getElementsAnnotatedWith(Label.class)) {
                var annotation = element.getAnnotation(Label.class);
                if (all.put(annotation.value(), element) != null) {
                    throw new IllegalStateException("An element annotated with @Label(\"" + annotation.value() + "\") already exists. Labels must be unique in a single annotation processing round.");
                }

                var group = groups.computeIfAbsent(annotation.group(), k -> new HashMap<>());
                group.put(annotation.value(), element);
            }
        }
    }
    
    
    /**
     * Returns an iterator over the {@code Element}s annotated with {@code @Label}.
     * 
     * @return an iterator
     */
    @Override
    public Iterator<Map.Entry<String, Element>> iterator() {
        return all().entrySet().iterator();
    }
    
    /**
     * Returns the number of elements annotated with {@code @Label} in this annotation
     * processing round.
     * 
     * @return the number of elements
     */
    public int size() {
        return all().size();
    }
    
}
