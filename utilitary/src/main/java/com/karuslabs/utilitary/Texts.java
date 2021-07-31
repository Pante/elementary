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

import com.karuslabs.utilitary.snippet.*;
import java.util.*;
import java.util.function.BiConsumer;
import javax.lang.model.element.*;

import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.joining;

/**
 * Utilities for formatting messages.
 */
public class Texts {
    
    private static final String DELIMITER = "\n|    ";
    
    /**
     * The format used to describe strings.
     */
    public static final BiConsumer<String, StringBuilder> STRING = (string, builder) -> builder.append(string);
    /**
     * The format used to describe objects which string representation is in screaming case.
     */
    public static final BiConsumer<Object, StringBuilder> SCREAMING_CASE = (object, builder) -> builder.append(object.toString().toLowerCase().replace('_', ' '));
    
    /**
     * Creates an diagnostic message that highlights part of the given line with 
     * a message. The highlighted part is indicated using {@code ~}.
     * 
     * @param brief the brief diagnostic message
     * @param line the line which contains an issue
     * @param highlight the part to highlight
     * @param message a message that describes
     * @return a diagnostic message
     */
    public static String highlight(String brief, Line line, Line highlight, String message) {
        return highlight(brief, line, highlight, "~", message);
    }
    
    /**
     * Creates an diagnostic message that highlights part of the given snippet with 
     * a message. The highlighted part is indicated using {@code ~}.
     * 
     * @param brief the brief diagnostic message
     * @param snippet the snippet which contains an issue
     * @param highlight the part to highlight
     * @param message a message that describes
     * @return a diagnostic message
     */
    public static String highlight(String brief, Snippet snippet, Line highlight, String message) {
        return highlight(brief, snippet, highlight, "~", message);
    }
    
    
    public static String highlight(String brief, Line line, Line highlight, String indicator, String message) {
        return highlight(brief, line, highlight.position, highlight.length(), indicator, message);
    }
    
    public static String highlight(String brief, Snippet snippet, Line highlight, String indicator, String message) {
        return highlight(brief, snippet, highlight.column, highlight.position, highlight.length(), indicator, message);
    }
    
    
    public static String highlight(String brief, Line line, int position, int length, String indicator, String message) {
        return highlight(brief, Map.of(0, line), 0, position - line.position, length, indicator, message);
    }
    
    public static String highlight(String brief, Snippet snippet, int column, int position, int length, String indicator, String message) {
        return highlight(brief, snippet.lines, column, position, length, indicator, message);
    }
    
    
    public static String highlight(String brief, Map<Integer, CharSequence> lines, int column, int position, int length, String indicator, String message) {
        lines = new HashMap<>(lines);
        if (position > 120 && message.length() < 120) {
            lines.put(column + 1, " ".repeat(position - message.length() - 1) + message + " " + indicator.repeat(length));
        } else {
            lines.put(column + 1, " ".repeat(position) + indicator.repeat(length) + " " + message);
        }
        
        return brief + DELIMITER.repeat(2) + lines.entrySet().stream().sorted(comparingByKey()).map(entry -> entry.getValue() + DELIMITER).collect(joining(""));
    }
    
    
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
