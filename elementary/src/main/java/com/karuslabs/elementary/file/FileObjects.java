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
package com.karuslabs.elementary.file;

import com.karuslabs.annotations.*;
import com.karuslabs.elementary.junit.annotations.*;

import java.io.*;
import java.lang.reflect.AnnotatedElement;
import java.net.*;
import java.util.*;
import javax.tools.*;

import static javax.tools.JavaFileObject.Kind.SOURCE;

/**
 * A utility class for creating {@code JavaFileObject}s.
 */
public @Static class FileObjects {
    
    /**
     * A dummy Java source file.
     */
    public static final JavaFileObject DUMMY = ofLines("Dummy", "class Dummy {}");
    
    /**
     * Creates {@code JavaFileObject}s using the {@code @Classpath} and {@code @Inline} 
     * annotations on the given element.
     * 
     * @param annotated the annotated element
     * @return the {@code JavaFileObject}s
     */
    public static List<JavaFileObject> scan(AnnotatedElement annotated) {
        var files = new ArrayList<JavaFileObject>();
        for (var classpath : annotated.getAnnotationsByType(Classpath.class)) {
            files.add(ofResource(classpath.value()));
        }
        
        for (var inline : annotated.getAnnotationsByType(Inline.class)) {
            files.add(ofLines(inline.name(), inline.source()));
        }
        
        return files;
    }
    
    /**
     * Creates a {@code JavaFileObject} using the given fully qualified class name
     * and source code.
     * 
     * @param fullyQualifiedName the fully qualified class name
     * @param lines the source code
     * @return a {@code JavaFileObject}
     */
    public static JavaFileObject ofLines(String fullyQualifiedName, String... lines) {
        return ofLines(fullyQualifiedName, String.join(System.lineSeparator(), lines));
    }
    
    /**
     * Creates a {@code JavaFileObject} using the given fully qualified class name
     * and source code.
     * 
     * @param fullyQualifiedName the fully qualified class name
     * @param lines the source code
     * @return a {@code JavaFileObject}
     */
    public static JavaFileObject ofLines(String fullyQualifiedName, Iterable<String> lines) {
        return ofLines(fullyQualifiedName, String.join(System.lineSeparator(), lines));
    }
    
    /**
     * Creates a {@code JavaFileObject} using the given fully qualified class name
     * and source code.
     * 
     * @param fullyQualifiedName the fully qualified class name
     * @param source the source code
     * @return a {@code JavaFileObject}
     */
    public static JavaFileObject ofLines(String fullyQualifiedName, String source) {
        return new StringFileObject(URI.create(fullyQualifiedName.replace('.', '/') + SOURCE.extension), SOURCE, source);
    }
    
    /**
     * Creates a {@code JavaFileObject} from the given resource.
     * 
     * @param resource the path to a resource, relative to the current ClassLoader
     * @return a {@code JavaFileObject}
     * @throws IllegalArgumentException if the given resource does not exist on the
     *         current clssspath
     * @throws UncheckedIOException if the resource could not be opened
     */
    public static JavaFileObject ofResource(String resource) {
        var url = FileObjects.class.getClassLoader().getResource(resource);
        if (url == null) {
            throw new IllegalArgumentException("\"" + resource + "\" does not exist on the current classpath");
        }
        
        return ofResource(url);
    }
    
    /**
     * Creates a {@code JavaFileObject} from the given resource.
     * 
     * @param resource the path to a resource, relative to the current ClassLoader
     * @return a {@code JavaFileObject}
     * @throws IllegalArgumentException if the given resource does not exist on the
     *         current clssspath
     * @throws UncheckedIOException if the resource could not be opened
     */
    public static JavaFileObject ofResource(URL resource) {
        try (var stream = resource.openStream()) {
            var uri = uri(resource);
            return new ByteFileObject(uri, deduce(uri), stream.readAllBytes());
            
        } catch (IOException e) {
            throw new UncheckedIOException(e);
            
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    /**
     * Creates a URI from the path portion of the given URL if it represents a
     * resource in a JAR.
     * 
     * @param resource the resource
     * @return the URI
     * @throws URISyntaxException if this URL is not formatted strictly according to
     *         to RFC2396 and cannot be converted to a URI.
     */
    static URI uri(URL resource) throws URISyntaxException {
        if (!resource.getProtocol().equals("jar")) {
            return resource.toURI();
        }
        
        return URI.create(resource.getPath().split("!")[1]);
    }
    
    
    
    /**
     * Deduces the file kind of the given URI.
     * 
     * @param uri the URI
     * @return the file kind that the given URI represents
     */
    public static JavaFileObject.Kind deduce(URI uri) {
        var path = uri.getPath();
        for (var kind : JavaFileObject.Kind.values()) {
            if (path.endsWith(kind.extension)) {
                return kind;
            }
        }
        
        
        return null; // Will never occur
    }
    
}
