/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
package com.karuslabs.utilitary;

import java.util.Collection;
import java.util.function.BiConsumer;

/**
 * Utilities for formatting messages.
 */
public class Texts {
    
    /**
     * The format used to describe strings.
     */
    public static final BiConsumer<String, StringBuilder> STRING = (string, builder) -> builder.append(string);
    /**
     * The format used to describe objects which string representation is in screaming case.
     */
    public static final BiConsumer<Object, StringBuilder> SCREAMING_CASE = (object, builder) -> builder.append(object.toString().toLowerCase().replace('_', ' '));
    
    
    /**
     * Joins the given elements together with commas and {@code "and"}.
     * 
     * @param <T> the type of the elements to be formatted
     * @param elements the elements to be joined
     * @param consumer the consumer used to format each element 
     * @return the joined elements
     */
    public static <T> String and(Collection<? extends T> elements, BiConsumer<? super T, StringBuilder> consumer) {
        return conjunction(elements, consumer, "and");
    }
    
    /**
     * Joins the given elements together with commas and {@code "and"}.
     * 
     * @param <T> the type of the elements to be formatted
     * @param elements the elements to be joined
     * @param consumer the consumer used to format each element
     * @return the joined elements
     */
    public static <T> String and(T[] elements, BiConsumer<? super T, StringBuilder> consumer) {
        return conjunction(elements, consumer, "and");
    }
    
    
    /**
     * Joins the given elements together with commas and {@code "or"}.
     * 
     * @param <T> the type of the elements to be formatted
     * @param elements the elements to be joined
     * @param consumer the consumer used to format each element
     * @return the joined elements
     */
    public static <T> String or(Collection<? extends T> elements, BiConsumer<? super T, StringBuilder> consumer) {
        return conjunction(elements, consumer, "or");
    }
    
    /**
     * Joins the given elements together with commas and {@code "or"}.
     * 
     * @param <T> the type of the elements to be formatted
     * @param elements the elements to be joined
     * @param consumer the consumer used to format each element
     * @return the joined elements
     */
    public static <T> String or(T[] elements, BiConsumer<? super T, StringBuilder> consumer) {
        return conjunction(elements, consumer, "or");
    }
    
    
    /**
     * Joins the given elements together with commands and the given conjunction.
     * 
     * @param <T> the type of the elements to be formatted
     * @param elements the elements to be joined
     * @param consumer the consumer used to format each element
     * @param conjunction the conjunction used to join the last two elements
     * @return the joined elements
     */
    public static <T> String conjunction(Collection<? extends T> elements, BiConsumer<? super T, StringBuilder> consumer, String conjunction) {
        var builder = new StringBuilder();
        var i = 0;
        for (var element : elements) {
            consumer.accept(element, builder);
            if (i < elements.size() - 2) {
                builder.append(", ");
                
            } else if (i < elements.size() - 1) {
                builder.append(' ').append(conjunction).append(' ');
            }
            i++;
        }
        
        
        return builder.toString();
    }
    
    /**
     * Joins the given elements together with commands and the given conjunction.
     * 
     * @param <T> the type of the elements to be formatted
     * @param elements the elements to be joined
     * @param consumer the consumer used to format each element
     * @param conjunction the conjunction used to join the last two elements
     * @return the joined elements
     */
    public static <T> String conjunction(T[] elements, BiConsumer<? super T, StringBuilder> consumer, String conjunction) {
        var builder = new StringBuilder();
        for (int i = 0; i < elements.length; i++) {
            consumer.accept(elements[i], builder);
            if (i < elements.length - 2) {
                builder.append(", ");
                
            } else if (i < elements.length - 1) {
                builder.append(' ').append(conjunction).append(' ');
            }
        }
        
        return builder.toString();
    }
    
    
    /**
     * Joins the given elements together with the given delimiter.
     * 
     * @param <T> the type of the elements to be formatted
     * @param elements the elements to be joined
     * @param consumer the consumer used to format each element
     * @param delimiter the delimiter used to join the elements
     * @return the joined elements
     */
    public static <T> String join(Collection<? extends T> elements, BiConsumer<? super T, StringBuilder> consumer, String delimiter) {
        var builder = new StringBuilder();
        var i = 0;
        for (var element : elements) {
            consumer.accept(element, builder);
            if (i < elements.size() - 1) {
                builder.append(delimiter);
            }
            i++;
        }
        
        return builder.toString();
    }
    
    /**
     * Joins the given elements together with the given delimiter.
     * 
     * @param <T> the type of the elements to be formatted
     * @param elements the elements to be joined
     * @param consumer the consumer used to format each element
     * @param delimiter the delimiter used to join the elements
     * @return the joined elements
     */
    public static <T> String join(T[] elements, BiConsumer<? super T, StringBuilder> consumer, String delimiter) {
        var builder = new StringBuilder();
        for (int i = 0; i < elements.length; i++) {
            consumer.accept(elements[i], builder);
            if (i < elements.length - 1) {
                builder.append(delimiter);
            }
        }
        
        return builder.toString();
    }
    
    
    /**
     * Joins the {@code left} and {@code right} with the given delimiter if neither
     * are blank.
     * 
     * @param left the string on the left
     * @param delimiter the delimiter used to join the two strings
     * @param right the string on the right
     * @return the joined string
     */
    public static String join(String left, String delimiter, String right) {
        if (left.isBlank()) {
            return right;
            
        } else if (right.isBlank()) {
            return left;
            
        } else {
            return left + delimiter + right;
        }
    }
    
    
    /**
     * Formats the given error value and reason.
     * 
     * @param value the value of the error
     * @param reason the reason behind an error
     * @return a message that describes an error
     */
    public static String format(Object value, String reason) {
        return quote(value) + " " + reason;
    }
    
    /**
     * Formats the given error value, reason and resolution.
     * 
     * @param value the value of the error
     * @param reason the reason behind an error
     * @param resolution the solution to the error
     * @return a message that describes an error
     */
    public static String format(Object value, String reason, String resolution) {
        return quote(value) + " " + reason + ", " + resolution;
    }
    
    /**
     * Enclose the given value in quotation marks.
     * 
     * @param value the value to be enclosed in quotation marks
     * @return the enclosed value
     */
    public static String quote(Object value) {
        return "\"" + value + "\"";
    }
    
}
