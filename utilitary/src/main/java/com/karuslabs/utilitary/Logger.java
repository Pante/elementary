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

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

import org.checkerframework.checker.nullness.qual.Nullable;

import static com.karuslabs.utilitary.Texts.format;
import static javax.tools.Diagnostic.Kind.*;

/**
 * A logger in an annotation processing environment that forwards all logged messages 
 * to an underlying {@code Messager}. In addition, this logger tracks whether an
 * error has been logged. 
 */
public class Logger {

    /**
     * The underlying {@code Messager}.
     */
    public final Messager messager;
    private boolean error;
    
    /**
     * Creates a {@code Logger} with the given messager.
     * 
     * @param messager the messager
     */
    public Logger(Messager messager) {
        this.messager = messager;
        this.error = false;
    }
    
    
    /**
     * Emits an error at the given location with the given value, reason and resolution.
     * 
     * @param location the location of the error, {@code null} if none
     * @param value the error's value
     * @param reason the reason that the error occurred
     * @param resolution a resolution to fix the error
     */
    public void error(@Nullable Element location, Object value, String reason, String resolution) {
        error(location, format(value, reason, resolution));
    }
    
    /**
     * Emits an error at the given location with the given value and reason.
     * 
     * @param location the location of the error, {@code null} if none
     * @param value the error's value
     * @param reason the reason that the error occurred
     */
    public void error(@Nullable Element location, Object value, String reason) {
        error(location, format(value, reason));
    }
    
    /**
     * Emits an error at the given location with the given error message.
     * 
     * @param location the location of the error, {@code null} if none
     * @param message the error message
     */
    public void error(@Nullable Element location, String message) {
        print(location, ERROR, message);
        error = true;
    }
    
    
    /**
     * Emits an warning at the given location with the given value, reason and resolution.
     * 
     * @param location the location of the warning, {@code null} if none
     * @param value the warning's value
     * @param reason the reason that the warning occurred
     * @param resolution a resolution to fix the warning
     */
    public void warn(@Nullable Element location, Object value, String reason, String resolution) {
        warn(location, format(value, reason, resolution));
    }
    
    /**
     * Emits an warning at the given location with the given value and reason.
     * 
     * @param location the location of the warning, {@code null} if none
     * @param value the warning's value
     * @param reason the reason that the warning occurred
     */
    public void warn(@Nullable Element location, Object value, String reason) {
        warn(location, format(value, reason));
    }
    
    /**
     * Emits a warning at the given location with the given warning message.
     * 
     * @param location the location of the warning, {@code null} if none
     * @param message the warning message
     */
    public void warn(@Nullable Element location, String message) {
        print(location, WARNING, message);
    }
    
    
    /**
     * Emits a note at the given location with the given value, reason and resolution.
     * 
     * @param location the location of the note, {@code null} if none
     * @param value the note's value
     * @param reason the reason that the note occurred
     * @param resolution a solution to resolve the note
     */
    public void note(@Nullable Element location, Object value, String reason, String resolution) {
        note(location, format(value, reason, resolution));
    }
    
    /**
     * Emits a note at the given location with the given value and reason.
     * 
     * @param location the location of the note, {@code null} if none
     * @param value the note's value
     * @param reason the reason that the note occurred
     */
    public void note(@Nullable Element location, Object value, String reason) {
        note(location, format(value, reason));
    }
    
    /**
     * Emits a note at the given location with the given note.
     * 
     * @param location the location of the note, {@code null} if none
     * @param message the note
     */
    public void note(@Nullable Element location, String message) {
        print(location, NOTE, message);
    }
    
    
    /**
     * Prints the given message at the given location.
     * 
     * @param location the location of this message, {@code null} if none
     * @param kind the message kind
     * @param message the message
     */
    private void print(@Nullable Element location, Kind kind, String message) {
        if (location != null) {
            messager.printMessage(kind, message, location);
            
        } else {
            messager.printMessage(kind, message);
        }
    }
    
    /**
     * Resets whether an error has been logged. 
     */
    public void clear() {
        error = false;
    }
    
    /**
     * Returns whether an error has been logged.
     * 
     * @return {@code true} if an error has been logged, else {@code false}
     */
    public boolean error() {
        return error;
    }
    
}
