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
package com.karuslabs.utilitary.snippet;

import java.util.Objects;
import javax.lang.model.element.AnnotationMirror;

import static com.karuslabs.utilitary.type.AnnotationValuePrinter.annotation;

/**
 * Represents a line in a Java source file
 * 
 * A {@code Line} reconstructs a line in the source file from an {@code Element}
 * representation and hence may not be exactly the same as the original source code. 
 */
public class Line implements CharSequence {

    /**
     * Creates a {@code Line} for the given annotation.
     * 
     * @param annotation the annotation
     * @param column the column at which the annotation is declared
     * @param position the position at which the annotation starts
     * @return 
     */
    public static Line of(AnnotationMirror annotation, int column, int position) {
        return new Line(annotation(annotation), column, position);
    }
    
    private final String value;
    /**
     * The column of this line.
     */
    public final int column;
    /**
     * The position of this line.
     */
    public final int position;
    
    /**
     * Creates a {@code Line} with the given value and location.
     * 
     * @param value the value
     * @param column the column
     * @param position the position
     */
    public Line(String value, int column, int position) {
        this.value = value;
        this.column = column;
        this.position = position;
    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        
        if (!(other instanceof Line)) {
            return false;
        }
        
        var line = (Line) other;
        return value.equals(line.value) && column == line.column && position == line.position;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value, column, position);
    }
    
    @Override
    public String toString() {
        return value;
    }
    
}
