package a;

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
import com.karuslabs.utilitary.Logger;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.*;
import java.util.concurrent.Semaphore;
import javax.annotation.processing.*;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.*;

import org.junit.jupiter.api.extension.InvocationInterceptor.Invocation;

import static javax.lang.model.SourceVersion.RELEASE_11;

@SupportedSourceVersion(RELEASE_11)
@SupportedAnnotationTypes({"*"})
public class ToolkitProcessor extends AbstractProcessor {
    

    public volatile Toolkit kit;
    public final List<Invocation<Void>> invocations = new ArrayList<>();
    public final Semaphore semaphore = new Semaphore(0);
    
    @Override
    public void init(ProcessingEnvironment environment) {
        super.init(environment);
        kit = new Toolkit(environment.getElementUtils(), environment.getFiler(), environment.getMessager(), environment.getTypeUtils());
    }
    
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment round) {
        if (round.processingOver()) {
            try {
                semaphore.acquire();
                for (var invocation : invocations) {
                    invocation.proceed();
                }
                semaphore.release(2);
                
            } catch (Throwable ex) {
                
            }
        }

        return false;
    }
        
    
    public static class Toolkit {
        
        public final Elements elements;
        public final Filer filer;
        public final Logger logger;
        public final TypeMirrors types;
        
        public Toolkit(Elements elements, Filer filer, Messager messager, Types types) {
            this.elements = elements;
            this.filer = filer;
            this.logger = new Logger(messager);
            this.types = new TypeMirrors(elements, types);
        }
        
    }

}
