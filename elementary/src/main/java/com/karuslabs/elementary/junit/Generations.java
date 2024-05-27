package com.karuslabs.elementary.junit;

import com.karuslabs.elementary.junit.annotations.Generation;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

import java.io.*;
import java.lang.annotation.Annotation;
import java.nio.file.*;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;

/**
 * Provides functions for setting up and tearing down the outputs for generated classes and sources.
  */

class Generations {

    static final String TEMP_DIRECTORY = "TEMP_DIRECTORY";
    static final String GENERATED_CLASSES = "GENERATED_CLASSES";
    static final String GENERATED_SOURCES = "GENERATED_SOURCES";

    /**
     * Returns a compiler that is configured to output generated classes and sources to a temporary directory.
     *
     * @param context the extension context
     * @return the generated classes and generated sources directories
     */
    static Map.Entry<File, File> initialize(ExtensionContext context) {
        try {
            var type = context.getRequiredTestClass();
            var output = type.getAnnotation(Generation.class) == null ? new DefaultGeneration() : type.getAnnotation(Generation.class);
            var directory = Files.createTempDirectory(type.getCanonicalName());

            File classes;
            if (output.classes().equals(Generation.DEFAULT_OUTPUT)) {
                classes = directory.resolve("generated-classes").toFile();
            } else {
                classes = new File(output.classes());
            }
            classes.mkdirs();

            File sources;
            if (output.sources().equals(Generation.DEFAULT_OUTPUT)) {
                sources = directory.resolve("generated-sources").toFile();
            } else {
                sources = new File(output.sources());
            }
            sources.mkdirs();

            var namespace = Namespace.create(Generations.class);
            context.getStore(namespace).put(TEMP_DIRECTORY, directory.toFile());
            context.getStore(namespace).put(GENERATED_CLASSES, classes);
            context.getStore(namespace).put(GENERATED_SOURCES, sources);

            return new SimpleEntry<>(classes, sources);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Deletes the outputs for generated classes and sources.
     *
     * @param context the extension context
     */
    static void teardown(ExtensionContext context) {
        var type = context.getRequiredTestClass();
        var output = type.getAnnotation(Generation.class) == null ? new DefaultGeneration() : type.getAnnotation(Generation.class);

        var namespace = Namespace.create(Generations.class);
        var classes = context.getStore(namespace).remove(GENERATED_CLASSES, File.class);
        var sources = context.getStore(namespace).remove(GENERATED_SOURCES, File.class);
        var directory = context.getStore(namespace).remove(TEMP_DIRECTORY, File.class);

        if (output.retain()) {
            return;
        }

        delete(classes);
        delete(sources);
        delete(directory);
    }

    static void delete(@Nullable File file) {
        if (file == null) {
            return;
        }

        try (var f = Files.walk(file.toPath())) {
            f.map(Path::toFile)
                .sorted(Comparator.reverseOrder())
                .forEach(File::delete);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}

class DefaultGeneration implements Generation {
    @Override
    public boolean retain() {
        return false;
    }

    @Override
    public @Nullable String classes() {
        return DEFAULT_OUTPUT;
    }

    @Override
    public @Nullable String sources() {
        return DEFAULT_OUTPUT;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
