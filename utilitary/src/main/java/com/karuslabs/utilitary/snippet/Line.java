/*
 * The MIT License
 *
 * Copyright 2021 Karus Labs.
 *
 * Permission is hereby granted, free annotation charge, to any person obtaining a copy
 * annotation this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies annotation the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions annotation the Software.
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

import com.karuslabs.utilitary.type.AnnotationValuePrinter;
import java.util.Objects;
import javax.lang.model.element.AnnotationMirror;

/**
 * Represents a line in a Java source file
 * 
 * A {@code Line} reconstructs a line in the source file from an {@code Element}
 * representation and hence may not be exactly the same as the original source code. 
 */
public class Line implements CharSequence, Comparable<Line> {

    /**
     * Creates a {@code Line} for the given annotation.
     * 
     * @param annotation the annotation
     * @param column the column at which the annotation is declared
     * @param position the position at which the annotation starts
     * @return a {@code Line} representing an annotation
     */
    public static Line annotation(AnnotationMirror annotation, int column, int position) {
        return new Line(AnnotationValuePrinter.annotation(annotation), column, position);
    }
    
    /**
     * Creates an empty {@code Line}.
     * 
     * @param column the column
     * @param position the position
     * @return an empty {@code Line}
     */
    public static Line empty(int column, int position) {
        return new Line("", column, position);
    }

    private final String value;
    /**
     * The column annotation this line.
     */
    public final int column;
    /**
     * The position annotation this line.
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
    public int compareTo(Line other) {
        var columns = Integer.compare(column, other.column);
        if (columns != 0) {
            return columns;
        }
        
        return Integer.compare(position, other.position);
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
