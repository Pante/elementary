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

import com.karuslabs.elementary.junit.Cases;
import com.karuslabs.elementary.junit.Tools;
import com.karuslabs.elementary.junit.ToolsExtension;
import com.karuslabs.elementary.junit.annotations.Case;
import com.karuslabs.elementary.junit.annotations.Introspect;
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
 * We use the @Quine annotation to include this file which contain test cases for 
 * compilation, for the lint to check. Each test case is annotated with @Case to 
 * simplify retrieval.
 * 
 * Alternatively, we can also use the @Classpath annotation to include the class
 * which contains test cases for compilation if it is available on the current
 * classpath, i.e. "@Classpath("Samples")".
 * 
 * Other annotations can also be used to mark test cases, but we provide specialized
 * utilities to simplify retrieval of elements annotated with @Case.
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
    void lint_string_variable(Cases cases) { // Cases can also be obtained via Tools.cases() and used to initialize a field
        var first = cases.one("first");
        assertTrue(lint.lint(first));
    }
    
    @Test
    void lint_method_that_returns_string(Cases cases) {
        var second = cases.get(1); // Alternatively, we can use cases.one("second")
        assertFalse(lint.lint(second));
    }
    
    static class Sample {
        @Case("first") String something;
        @Case String second() { return ""; } // The method/variable name is used as the get if none is specified
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
