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
package com.karuslabs.elementary.junit.tools;

import com.karuslabs.annotations.Lazy;
import com.karuslabs.utilitary.Logger;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.util.*;
import java.util.concurrent.*;
import javax.annotation.processing.*;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;

import org.junit.jupiter.api.extension.InvocationInterceptor.Invocation;

import static javax.lang.model.SourceVersion.RELEASE_11;

@SupportedAnnotationTypes({"*"})
@SupportedSourceVersion(RELEASE_11)
class DaemonProcessor extends AbstractProcessor {
    
    private final CountDownLatch initialization = new CountDownLatch(1);
    private final Exchanger<Invocation> invocations = new Exchanger<>();
    private final Exchanger<Throwable> throwables = new Exchanger<>();
    private volatile @Lazy Environment environment;
    
    @Override
    public void init(ProcessingEnvironment env) {
        super.init(env);
        environment = new Environment(env.getElementUtils(), env.getTypeUtils(), env.getMessager(), env.getFiler());
        initialization.countDown();
    }
    
    public Environment environment() throws InterruptedException {
        initialization.await();
        return environment;
    }
    
    
    @Override
    public boolean process(Set<? extends TypeElement> types, RoundEnvironment round) {
        if (!round.processingOver()) {
            return false;
        }
        
        try {
            while (true) {
                var invocation = invocations.exchange(null);
                try {
                    invocation.proceed();
                    throwables.exchange(null);

                } catch (Throwable e) {
                    throwables.exchange(e);
                }
            }
            
        } catch (InterruptedException e) {
            return false;
        }
    }
    
    public @Nullable Throwable exchange(Invocation invocation) throws InterruptedException {
        invocations.exchange(invocation);
        return throwables.exchange(null);
    }
    
    
    public static class Environment {
        public final Elements elements;
        public final Types types;
        public final Messager messager;
        public final Filer filer;
        public final TypeMirrors typeMirrors;
        public final Logger logger;
        
        Environment(Elements elements, Types types, Messager messager, Filer filer) {
            this.elements = elements;
            this.types = types;
            this.messager = messager;
            this.filer = filer;
            typeMirrors = new TypeMirrors(elements, types);
            logger = new Logger(messager);
        }
    }

}
