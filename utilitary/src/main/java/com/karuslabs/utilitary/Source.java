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

/**
 * Represents Java source code and contains methods for writing Java source code 
 * programmatically.
 */
public class Source implements CharSequence {
    
    /**
     * Returns a string representation of the given arguments enclosed in parentheses.
     * 
     * @param arguments the arguments
     * @return the formatted arguments
     */
    public static String arguments(Object... arguments) {
        var builder = new StringBuilder().append('(');
        for (int i = 0; i < arguments.length; i++) {
            builder.append(arguments[i]);
            if (i < arguments.length - 1) {
                builder.append(", ");
            }
        }
        return builder.append(')').toString();
    }
    
    /**
     * Returns a string representation of the given arguments enclosed in parentheses.
     * 
     * @param arguments the arguments
     * @return the formatted arguments
     */
    public static String arguments(Collection<?> arguments) {
        var builder = new StringBuilder().append('(');
        int i = 0;
        for (var parameter : arguments) {
            builder.append(parameter);
            if (i < arguments.size() - 1) {
                builder.append(", ");
            }
            
            i++;
        }
        
        return builder.append(')').toString();
    }
    
    
    private final StringBuilder builder = new StringBuilder();
    private String indentation = "";
    
    /**
     * Assigns the given value to the variable name of locally inferred type ({@code var}).
     * <br><br>
     * <b>This method supports indention.</b>
     * 
     * @param name the variable name
     * @param value the value to be assignment
     * @return {@code this}
     */
    public Source assign(String name, String value) {
        return assign("var", name, value);
    }
    
    /**
     * Assigns the value to the variable name of the given type.
     * <br><br>
     * <b>This method supports indention.</b>
     * 
     * @param type the type of the variable
     * @param name the variable name
     * @param value the value to be assigned
     * @return {@code this}
     */
    public Source assign(String type, String name, String value) {
        builder.append(indentation).append(type).append(" ").append(name).append(" = ").append(value).append(";").append(System.lineSeparator());
        return this;
    }
    
    /**
     * Casts and assigns a value to a variable of locally inferred type ({@code var}).
     * <br><br>
     * <b>This method supports indention.</b>
     * 
     * @param name the variable name
     * @param type the cast type of the value
     * @param value the value to be assigned
     * @return {@code this}
     */
    public Source cast(String name, String type, String value) {
        builder.append(indentation).append("var ").append(name).append(" = (").append(type).append(") ").append(value).append(";").append(System.lineSeparator());
        return this;
    }
    
    /**
     * Declares the current package name.
     * 
     * @param pack the package name
     * @return {@code this}
     */
    public Source pack(String pack) {
        builder.append("package ").append(pack).append(";").append(System.lineSeparator());
        return this;
    }
    
    /**
     * Imports the given class, nested class member or package.
     * 
     * @param pack the class, class member or package to be imported
     * @return {@code this}
     */
    public Source include(String pack) {
        builder.append("import ").append(pack).append(";").append(System.lineSeparator());
        return this;
    }
    
    /**
     * Appends a new line using the system's line separator.
     * 
     * @return {@code this}
     */
    public Source line() {
        builder.append(System.lineSeparator());
        return this;
    }
    
    /**
     * Appends the given value on a new line.
     * <br><br>
     * <b>This method supports indention.</b>
     * 
     * @param value the value
     * @return {@code this}
     */
    public Source line(Object value) {
        builder.append(indentation).append(value).append(System.lineSeparator());
        return this;
    }
    
    /**
     * Appends the given values on a new line.
     * <br><br>
     * <b>This method supports indention.</b>
     * 
     * @param values the values
     * @return {@code this}
     */
    public Source line(Object... values) {
        builder.append(indentation);
        for (var value : values) {
            builder.append(value);
        }
        
        builder.append(System.lineSeparator());
        return this;
    }
    
    /**
     * Appends the given value.
     * <br><br>
     * <b>This method supports indention.</b>
     * 
     * @param value the value
     * @return {@code this}
     */
    public Source append(Object value) {
        builder.append(value);
        return this;
    }
    
    
    /**
     * Increases the current indentation by four spaces.
     * 
     * @return {@code this}
     */
    public Source indent() {
        indentation = indentation + "    ";
        return this;
    }
    
    /**
     * Decreases the current indentation by four spaces.
     * 
     * @return {@code this}
     */
    public Source unindent() {
        indentation = indentation.length() <= 4 ? "" : indentation.substring(0, indentation.length() - 4);
        return this;
    }
    
    /**
     * Sets the current indentation.
     * 
     * @param indentation the number of spaces
     * @return {@code this}
     */
    public Source indentation(int indentation) {
        this.indentation = " ".repeat(indentation);
        return this;
    }


    @Override
    public char charAt(int index) {
        return builder.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return builder.subSequence(start, end);
    }
    
    @Override
    public int length() {
        return builder.length();
    }
    
    @Override
    public String toString() {
        return builder.toString();
    }

}
