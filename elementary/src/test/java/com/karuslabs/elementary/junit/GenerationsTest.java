package com.karuslabs.elementary.junit;

import com.karuslabs.elementary.junit.annotations.Generation;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

import java.io.File;
import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenerationsTest {

    StubStore store = new StubStore();
    ExtensionContext context = when(mock(ExtensionContext.class).getStore(any())).thenReturn(store).getMock();

    @Test
    void compiler_no_annotations() {
        doReturn(NoAnnotations.class).when(context).getRequiredTestClass();

        var entry = Generations.initialize(context);
        var directory = store.get(Generations.TEMP_DIRECTORY, File.class);

        assertTrue(directory.exists());
        assertTrue(directory.getName().startsWith(NoAnnotations.class.getCanonicalName()));

        assertTrue(entry.getKey().exists());
        assertTrue(entry.getKey().toPath().startsWith(directory.toPath()));
        assertEquals("generated-classes", entry.getKey().getName());

        assertTrue(entry.getValue().exists());
        assertTrue(entry.getValue().toPath().startsWith(directory.toPath()));
        assertEquals("generated-sources", entry.getValue().getName());
    }

    @Test
    void compiler_default_output() {
        doReturn(DefaultAnnotations.class).when(context).getRequiredTestClass();

        var entry = Generations.initialize(context);
        var directory = store.get(Generations.TEMP_DIRECTORY, File.class);

        assertTrue(directory.exists());
        assertTrue(directory.getName().startsWith(DefaultAnnotations.class.getCanonicalName()));

        assertTrue(entry.getKey().exists());
        assertTrue(entry.getKey().toPath().startsWith(directory.toPath()));
        assertEquals("generated-classes", entry.getKey().getName());

        assertTrue(entry.getValue().exists());
        assertTrue(entry.getValue().toPath().startsWith(directory.toPath()));
        assertEquals("generated-sources", entry.getValue().getName());
    }

    @Test
    void compiler_custom_output() {
        doReturn(RetainCustomAnnotations.class).when(context).getRequiredTestClass();

        var entry = Generations.initialize(context);
        var directory = store.get(Generations.TEMP_DIRECTORY, File.class);

        assertTrue(directory.exists());
        assertTrue(directory.getName().startsWith(RetainCustomAnnotations.class.getCanonicalName()));

        assertTrue(entry.getKey().exists());
        assertFalse(entry.getKey().toPath().startsWith(directory.toPath()));
        assertEquals(new File("path/to/classes"), entry.getKey());

        assertTrue(entry.getValue().exists());
        assertFalse(entry.getValue().toPath().startsWith(directory.toPath()));
        assertEquals(new File("path/to/sources"), entry.getValue());
    }


    @Test
    void teardown_no_annotations() {
        doReturn(NoAnnotations.class).when(context).getRequiredTestClass();

        var entry = Generations.initialize(context);
        var directory = store.get(Generations.TEMP_DIRECTORY, File.class);

        Generations.teardown(context);

        assertTrue(store.map.isEmpty());
        assertFalse(directory.exists());
        assertFalse((entry.getKey().exists()));
        assertFalse((entry.getValue().exists()));
    }

    @Test
    void teardown_default_annotations() {
        doReturn(DefaultAnnotations.class).when(context).getRequiredTestClass();

        var compiler = Generations.initialize(context);
        var directory = store.get(Generations.TEMP_DIRECTORY, File.class);

        Generations.teardown(context);

        assertTrue(store.map.isEmpty());
        assertFalse(directory.exists());
        assertFalse((compiler.getKey().exists()));
        assertFalse((compiler.getValue().exists()));
    }

    @Test
    void teardown_unretained_custom_annotations() {
        doReturn(UnretainedCustomAnnotations.class).when(context).getRequiredTestClass();

        var compiler = Generations.initialize(context);
        var directory = store.get(Generations.TEMP_DIRECTORY, File.class);

        Generations.teardown(context);

        assertTrue(store.map.isEmpty());
        assertFalse(directory.exists());
        assertFalse((compiler.getKey().exists()));
        assertFalse((compiler.getValue().exists()));
    }

    @Test
    void teardown_retain_custom_annotations() {
        doReturn(RetainCustomAnnotations.class).when(context).getRequiredTestClass();

        var compiler = Generations.initialize(context);
        var directory = store.get(Generations.TEMP_DIRECTORY, File.class);

        Generations.teardown(context);

        assertTrue(store.map.isEmpty());
        assertTrue(directory.exists());
        assertTrue((compiler.getKey().exists()));
        assertTrue((compiler.getValue().exists()));
    }


    @AfterEach
    void after() {
        new File("path/to/classes").delete();
        new File("path/to/sources").delete();

        ((File) store.lifetime.get(Generations.TEMP_DIRECTORY)).delete();
    }

}

class NoAnnotations {}

@Generation
class DefaultAnnotations {}

@Generation(retain = true, classes = "path/to/classes", sources = "path/to/sources")
class RetainCustomAnnotations {}

@Generation(classes = "path/to/classes", sources = "path/to/sources")
class UnretainedCustomAnnotations {}

class StubStore implements Store {
    Map<Object, Object> map = new HashMap<>();
    Map<Object, Object> lifetime = new HashMap<>();

    @Override
    public Object get(Object key) {
        return null;
    }

    @Override
    public <V> V get(Object key, Class<V> requiredType) {
        return requiredType.cast(map.get(key));
    }

    @Override
    public <K, V> Object getOrComputeIfAbsent(K key, Function<K, V> defaultCreator) {
        return null;
    }

    @Override
    public <K, V> V getOrComputeIfAbsent(K key, Function<K, V> defaultCreator, Class<V> requiredType) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        map.put(key, value);
        lifetime.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return null;
    }

    @Override
    public <V> V remove(Object key, Class<V> requiredType) {
        return requiredType.cast(map.remove(key));
    }
}

