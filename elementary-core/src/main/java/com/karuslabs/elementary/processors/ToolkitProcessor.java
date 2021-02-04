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
package com.karuslabs.elementary.processors;

import com.karuslabs.annotations.Lazy;
import com.karuslabs.utilitary.Logger;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;

import static com.karuslabs.elementary.Compiler.javac;
import static com.karuslabs.elementary.file.FileObjects.ofLines;
import static javax.lang.model.SourceVersion.RELEASE_11;

@SupportedSourceVersion(RELEASE_11)
@SupportedAnnotationTypes({"com.karuslabs.annotations.Lazy"})
public class ToolkitProcessor extends AbstractProcessor {
    
    public static Toolkit toolkit() {
        var processor = new ToolkitProcessor();
        javac().currentClasspath().processors(processor).compile(ofLines("com.karuslabs.Help", "package com.karuslabs.help class Help {}"));
        return processor.value();
    }

    private @Lazy Toolkit kit;
    
    @Override
    public void init(ProcessingEnvironment environment) {
        super.init(environment);
        kit = new Toolkit(environment.getElementUtils(), environment.getFiler(), environment.getMessager(), environment.getTypeUtils());
    }
    
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment round) {
        System.out.println("Hello");
        return true;
    }
    
    public @Nullable Toolkit value() {
        return kit;
    }
        
    
    public static class Toolkit {
        
        public final Elements elements;
        public final Filer filer;
        public final Logger logger;
        public final TypeMirrors types;
        
        Toolkit(Elements elements, Filer filer, Messager messager, Types types) {
            this.elements = elements;
            this.filer = filer;
            this.logger = new Logger(messager);
            this.types = new TypeMirrors(elements, types);
        }
        
    }

}
