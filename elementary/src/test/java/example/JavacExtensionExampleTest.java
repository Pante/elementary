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

import com.karuslabs.elementary.Results;
import com.karuslabs.elementary.junit.JavacExtension;
import com.karuslabs.elementary.junit.annotations.*;
import com.karuslabs.utilitary.AnnotationProcessor;

import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This example demonstrates how to use JavacExtension to test the results of
 * the annotation processors which check if an element is a string field.
 * 
 * Compiler flags can be configured via the @Options annotation. In this example,
 * we enable the "-Werror" flag which causes the compiler to treat all warnings as
 * errors.
 *
 * The location of generated classes and source files can be configured via the @Generation annotation.
 * 
 * We can specify annotation processors to be called during compilation via the
 * @Processors annotation.
 * 
 * The @Classpath, @Generation, @Inline, @Options and @Processors annotations obey the same
 * variable scoping rules as Java. Annotations declared on the test class is applied
 * for all tests inside the annotated test class. On the other hand, annotations
 * declared on test methods are limited to that specific method.
 * 
 * The @Classpath source files for this example can be found under src/test/resources.
 **/
@ExtendWith(JavacExtension.class)
@Options("-Werror")
@Processors({StringFieldProcessor.class})
@Classpath("com.karuslabs.elementary.junit.example.ValidCase")
@Generation(classes = "path/to/generated-classes", sources = "path/to/generated-sources")
class JavacExtensionExampleTest {
    
    @Test
    void process_string_field(Results results) {
        assertEquals(0, results.find().errors().count());
    }
    
    @Test
    @Classpath("com.karuslabs.elementary.junit.example.InvalidCase")
    void process_int_field(Results results) {
        assertEquals(1, results.find().errors().contains("Element is not a string").count());
    }
    
}

@SupportedAnnotationTypes({"example.ExampleAnnotation"})
class StringFieldProcessor extends AnnotationProcessor {
    
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment round) {
        var elements = round.getElementsAnnotatedWith(ExampleAnnotation.class);
        for (var element : elements) {
            if (!(element instanceof VariableElement)) {
                logger.error(element, "Element is not a variable");
                continue;
            }
            
            var variable = (VariableElement) element;
            if (!types.isSameType(variable.asType(), types.type(String.class))) {
                logger.error(element, "Element is not a string");
            }
        }
        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

}
