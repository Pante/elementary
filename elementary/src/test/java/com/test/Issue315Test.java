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
package com.test;

import com.karuslabs.elementary.Results;
import com.karuslabs.elementary.junit.JavacExtension;
import com.karuslabs.elementary.junit.annotations.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(JavacExtension.class)
@Options("-Werror")
@Processors({DummyProcessor.class})
class Issue315Test {
    
    @Test
    @Classpath("com.karuslabs.elementary.junit.example.InvalidCase")
    void process(Results results) {
        assertEquals(0, results.find().errors().contains("Element is not a string").count());
    }
    
}

@SupportedAnnotationTypes({"example.ExampleAnnotation"})
class DummyProcessor extends AbstractProcessor {
    
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment round) {
        if (round.processingOver()) {
          try {
            var filer = processingEnv.getFiler();
            var tempResource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "some.tmp");
            var uri = tempResource.toUri();
            System.out.println(uri);

          } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);
          }
        }
        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

}
