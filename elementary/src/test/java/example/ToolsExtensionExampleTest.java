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
package example;

import com.karuslabs.elementary.junit.Labels;
import com.karuslabs.elementary.junit.Tools;
import com.karuslabs.elementary.junit.ToolsExtension;
import com.karuslabs.elementary.junit.annotations.*;
import com.karuslabs.utilitary.type.TypeMirrors;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This example demonstrates how to use ToolsExtension to test a lint, described below,
 * which checks if an element is a string variable. 
 * 
 * We use the @Introspect annotation to include this file which contain test elements
 * for the lint to check. Each test element is annotated with @Label to simplify retrieval.
 * A @Label's value must be unique. 
 * 
 * Alternatively, we can also use the @Classpath annotation to include the class
 * which contains test cases for compilation if it is available on the current
 * classpath, i.e. "@Classpath("Samples")".
 * 
 * Other annotations can also be used to mark test cases, but we provide specialized
 * utilities to simplify retrieval of elements annotated with @Label.
 * 
 * In a real world context, the lint may be found in annotation processors to perform a 
 * variety of other checks.
 */
@ExtendWith(ToolsExtension.class)
@Introspect
class ToolsExtensionExampleTest {
    
    // We can also obtain an instance of TypeMirrors via dependency injection in
    // the constructor and test methods.
    Lint lint = new Lint(Tools.typeMirrors());
    
    @Test
    void lint_string_variable(Labels labels) { // Labels can also be obtained via Tools.labels() and used to initialize a field
        var first = labels.get("first");
        assertTrue(lint.lint(first));
    }
    
    @Test
    void lint_method_that_returns_string(Labels labels) {
        var second = labels.group("invalid").get("second"); // Alternatively, we can use labels.get("second")
        assertFalse(lint.lint(second));
    }
    
    static class Sample {
        @Label("first") String something;
        @Label(value = "second", group = "invalid") String second() { return ""; }
    }
    
}

/**
 * This is a simple lint that checks if an element is a string variable.
 */
class Lint {
    
    final TypeMirrors types;
    final TypeMirror expectedType;
    
    Lint(TypeMirrors types) {
        this.types = types;
        this.expectedType = types.type(String.class);
    }
    
    /**
     * Determines if the given element is a string variable.
     * 
     * @param element the element
     * @return {@code true} if the given element is a string variable
     */
    public boolean lint(Element element) {
        if (!(element instanceof VariableElement)) {
            return false;
        }
        
        var variable = (VariableElement) element;
        return types.isSameType(expectedType, variable.asType());
    }
    
}
