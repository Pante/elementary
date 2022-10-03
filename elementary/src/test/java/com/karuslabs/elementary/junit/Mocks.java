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

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.parallel.ExecutionMode;

class MockContext implements ExtensionContext {

    final Class<?> type;
    Map<String, String> parameters = new HashMap<>();
    Map<Namespace, Store> stores = new HashMap<>();
    
    MockContext(Class<?> type) {
        this.type = type;
    }
    
    public MockContext put(boolean parallel, boolean classMode, boolean methodMode) {
        parameters.put("junit.jupiter.execution.parallel.enabled", parallel ? "true" : null);
        parameters.put("junit.jupiter.execution.parallel.mode.classes.default", classMode ? "concurrent" : null);
        parameters.put("junit.jupiter.execution.parallel.mode.default", methodMode ? "concurrent" : null);
        return this;
    }
    
    
    @Override
    public Optional<ExtensionContext> getParent() {
        return null;
    }

    @Override
    public ExtensionContext getRoot() {
        return null;
    }

    @Override
    public String getUniqueId() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public Set<String> getTags() {
        return null;
    }

    @Override
    public Optional<AnnotatedElement> getElement() {
        return null;
    }

    @Override
    public Optional<Class<?>> getTestClass() {
        return Optional.of(type);
    }

    @Override
    public Optional<TestInstance.Lifecycle> getTestInstanceLifecycle() {
        return null;
    }

    @Override
    public Optional<Object> getTestInstance() {
        return null;
    }

    @Override
    public Optional<TestInstances> getTestInstances() {
        return null;
    }

    @Override
    public Optional<Method> getTestMethod() {
        return null;
    }

    @Override
    public Optional<Throwable> getExecutionException() {
        return null;
    }

    @Override
    public Optional<String> getConfigurationParameter(String string) {
        return Optional.ofNullable(parameters.get(string));
    }

    @Override
    public <T> Optional<T> getConfigurationParameter(String string, Function<String, T> fnctn) {
        return null;
    }

    @Override
    public void publishReportEntry(Map<String, String> map) {}

    @Override
    public Store getStore(Namespace namespace) {
        return stores.computeIfAbsent(namespace, (a) -> new MockStore());
    }

    @Override
    public ExecutionMode getExecutionMode() {
        return null;
    }

    @Override
    public ExecutableInvoker getExecutableInvoker() {
        return null;
    }
    
}

class MockStore implements Store {

    Map<Object, Object> map = new HashMap<>();
    
    @Override
    public Object get(Object o) {
        return map.get(o);
    }

    @Override
    public <V> V get(Object o, Class<V> type) {
        return type.cast(map.get(o));
    }

    @Override
    public <K, V> Object getOrComputeIfAbsent(K k, Function<K, V> function) {
        return null;
    }

    @Override
    public <K, V> V getOrComputeIfAbsent(K k, Function<K, V> fnctn, Class<V> type) {
        return null;
    }

    @Override
    public void put(Object o, Object o1) {
        map.put(o1, o1);
    }

    @Override
    public Object remove(Object o) {
        return map.remove(o);
    }

    @Override
    public <V> V remove(Object o, Class<V> type) {
        return null;
    }
    
}
