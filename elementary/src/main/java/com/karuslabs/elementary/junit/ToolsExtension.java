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
package com.karuslabs.elementary.junit;

import com.karuslabs.elementary.junit.DaemonProcessor.Environment;
import com.karuslabs.utilitary.Logger;
import com.karuslabs.utilitary.type.TypeMirrors;

import java.lang.reflect.Constructor;
import javax.annotation.processing.*;
import javax.lang.model.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;

import org.junit.jupiter.api.extension.*;

public class ToolsExtension extends Daemon implements ParameterResolver {
    
    @Override
    public Object resolveParameter(ParameterContext parameter, ExtensionContext context) throws ParameterResolutionException {
        var type = parameter.getParameter().getType();
        var resolved = resolve(compiler(context).environment(), type);
        if (resolved == null) {
            throw new ParameterResolutionException("Unable to resolve parameter of type: " + type.getName());
        }
        
        return resolved;
    }
    
    @Override
    public boolean supportsParameter(ParameterContext parameter, ExtensionContext context) {
        return resolve(compiler(context).environment(), parameter.getParameter().getType()) != null;
    }
    
    
    @Override
    protected Object construct(Constructor constructor, Environment environment) throws ParameterResolutionException, TestInstantiationException {
        var types = constructor.getParameterTypes();
        var parameters = new Object[types.length];
        
        for (int i = 0; i < types.length; i++) {
            var parameter = resolve(environment, types[i]);
            if (parameter == null) {
                throw new ParameterResolutionException("Unable to resolve parameter of type: " + types[i].getName());
            }
            
            parameters[i] = parameter;
        }
        
        try {
            constructor.setAccessible(true);
            return constructor.newInstance(parameters);
            
        } catch (ReflectiveOperationException e) {
            throw new TestInstantiationException("Unable to create test instance", e);
        }
    }
    
    
    static @Nullable Object resolve(Environment environment, Class<?> type) throws TestInstantiationException {
        if (type == Elements.class) {
            return environment.elements;
                
        } else if (type == Types.class) {
            return environment.types;
            
        } else if (type == Messager.class) {
            return environment.messager;
            
        } else if (type == Filer.class) {
            return environment.filer;
            
        } else if (type == TypeMirrors.class) {
            return environment.typeMirrors;
            
        } else if (type == Logger.class) {
            return environment.logger;
            
        } else {
            return null;
        }
    }

}
