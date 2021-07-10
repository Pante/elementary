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
package com.karuslabs.utilitary.texts;

import java.util.*;
import java.util.function.BiConsumer;
import javax.lang.model.element.Modifier;

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
        join(builder, elements, consumer, delimiter);
        return builder.toString();
    }
    
    /**
     * Joins the given elements together with the given delimiter.
     * 
     * @param <T> the type of the elements to be formatted
     * @param builder the {@code StringBuilder} used to join the elements
     * @param elements the elements to be joined
     * @param consumer the consumer used to format each element
     * @param delimiter the delimiter used to join the elements
     */
    public static <T> void join(StringBuilder builder, Collection<? extends T> elements, BiConsumer<? super T, StringBuilder> consumer, String delimiter) {
        var i = 0;
        for (var element : elements) {
            consumer.accept(element, builder);
            if (i < elements.size() - 1) {
                builder.append(delimiter);
            }
            i++;
        }
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
        join(builder, elements, consumer, delimiter);
        return builder.toString();
    }
    
    /**
     * Joins the given elements together with the given delimiter.
     * 
     * @param <T> the type of the elements to be formatted
     * @param builder the {@code StringBuilder} used to join the elements
     * @param elements the elements to be joined
     * @param consumer the consumer used to format each element
     * @param delimiter the delimiter used to join the elements
     */
    public static <T> void join(StringBuilder builder, T[] elements, BiConsumer<? super T, StringBuilder> consumer, String delimiter) {
        for (int i = 0; i < elements.length; i++) {
            consumer.accept(elements[i], builder);
            if (i < elements.length - 1) {
                builder.append(delimiter);
            }
        }
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
    
    /**
     * Sorts the given modifiers according to common Java conventions.
     * 
     * @param modifiers the unsorted modifiers
     * @return the sorted modifiers
     */
    public static Modifier[] sort(Collection<Modifier> modifiers) {
        return sort(modifiers.toArray(Modifier[]::new));
    }
    
    /**
     * Sorts the given modifiers according to common Java conventions.
     * 
     * @param modifiers the unsorted modifiers
     * @return the sorted modifiers
     */
    public static Modifier[] sort(Modifier... modifiers) {
        Arrays.sort(modifiers, (a, b) -> Integer.compare(order(a), order(b)));
        return modifiers;
    }
    
    static int order(Modifier modifier) {
        switch (modifier) {
            case PUBLIC:
            case PROTECTED:
            case PRIVATE:
                return 0;
            case STATIC:
                return 1;
            case ABSTRACT:
                return 2;
            default:
                return 3;
        }
    }
    
}
