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
package com.karuslabs.utilitary.type;

import javax.lang.model.element.*;
import javax.lang.model.util.SimpleElementVisitor9;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * An {@code ElementVisitor} that recursively traverses enclosing elements until
 * it finds the target element. If found, the target element is returned, otherwise
 * {@code null}.
 * 
 * @param <T> the type of the {@code Element} to find
 */
public abstract class Find<T extends Element> extends SimpleElementVisitor9<T, Void> {
    
    /**
     * A {@code Find} that finds the enclosing {@code ExecutableElement} of an element.
     */
    public static final Find<ExecutableElement> EXECUTABLE = new FindExecutable();
    /**
     * A {@code Find} that finds the enclosing {@code TypeElement} of an element.
     */
    public static final Find<TypeElement> TYPE = new FindType();
    /**
     * A {@code Find} that finds the enclosing {@code PackageElement} of an element.
     */
    public static final Find<PackageElement> PACKAGE = new FindPackage();
    /**
     * A {@code Find} that finds the enclosing {@code ModuleElement} of an element.
     */
    public static final Find<ModuleElement> MODULE = new FindModule();
    
    @Override
    protected @Nullable T defaultAction(Element element, Void parameter) {
        var enclosing = element.getEnclosingElement();
        return enclosing == null ? DEFAULT_VALUE : enclosing.accept(this, null);
    }
    
}

/**
 * A {@code Find} that finds the enclosing {@code ExecutableElement} of an element.
 */
class FindExecutable extends Find<ExecutableElement> {
    
    @Override
    public @Nullable ExecutableElement visitModule(ModuleElement element, Void parameter) {
        return DEFAULT_VALUE;
    }
    
    @Override
    public @Nullable ExecutableElement visitPackage(PackageElement element, Void parameter) {
        return DEFAULT_VALUE;
    }
    
    @Override
    public @Nullable ExecutableElement visitType(TypeElement element, Void parameter) {
        return DEFAULT_VALUE;
    }
    
    @Override
    public ExecutableElement visitExecutable(ExecutableElement element, Void parameter) {
        return element;
    }
    
}

/**
 * A {@code Find} that finds the enclosing {@code TypeElement} of an element.
 */
class FindType extends Find<TypeElement> {
    
    @Override
    public @Nullable TypeElement visitModule(ModuleElement element, Void parameter) {
        return DEFAULT_VALUE;
    }
    
    @Override
    public @Nullable TypeElement visitPackage(PackageElement element, Void parameter) {
        return DEFAULT_VALUE;
    }
    
    @Override
    public TypeElement visitType(TypeElement element, Void parameter) {
        return element;
    }
    
}

/**
 * A {@code Find} that finds the enclosing {@code PackageElement} of an element.
 */
class FindPackage extends Find<PackageElement> {
    
    @Override
    public @Nullable PackageElement visitModule(ModuleElement element, Void parameter) {
        return DEFAULT_VALUE;
    }
    
    
    @Override
    public @Nullable PackageElement visitPackage(PackageElement element, Void parameter) {
        return element;
    }
    
}

/**
 * A {@code Find} that finds the enclosing {@code ModuleElement} of an element.
 */
class FindModule extends Find<ModuleElement> {
    
    @Override
    public @Nullable ModuleElement visitModule(ModuleElement element, Void parameter) {
        return element;
    }
    
}