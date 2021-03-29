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

import com.karuslabs.elementary.junit.annotations.*;

import java.io.*;
import java.net.*;
import java.util.List;

import org.junit.jupiter.api.*;

import static com.karuslabs.elementary.Compiler.javac;
import static com.karuslabs.elementary.file.FileObjects.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Classpath("com.karuslabs.elementary.junit.CasesCases")
@Resource("com/karuslabs/elementary/junit/A.java")
@Inline(name = "Dummy", source = "class Dummy {}")
class FileObjectsTest {

    @Test
    void scan_nested_class() {
        assertTrue(javac().compile(FileObjects.scan(Nested.class)).success);
    }
    
    @Introspect
    static class Nested {
        
    }
    
    
    @Test
    void scan_element() {
        assertTrue(javac().compile(FileObjects.scan(FileObjectsTest.class)).success);
    }
    
    @Test
    void ofLines_varargs() {
        assertTrue(javac().compile(ofLines("Dummy", List.of("class Dummy {}"))).success);
    }
    
    @Test
    void ofLines_iterable() {
        assertTrue(javac().compile(ofLines("Dummy", List.of("class Dummy {}"))).success);
    }
    
    
    @Test
    void ofResource_invalid_url() {
        assertEquals(
            "\"something\" does not exist on the current classpath",
            assertThrows(IllegalArgumentException.class, () -> ofResource("something")).getMessage()
        );
    }
    
    
    @Test
    void ofResource_throws_IOException() throws IOException {
        URL url = when(mock(URL.class).openStream()).thenThrow(IOException.class).getMock();
        assertThrows(UncheckedIOException.class, () -> ofResource(url));
    }
    
    @Test
    void ofResource_throws_IllegalArgumentException() throws IOException, URISyntaxException {
        URL url = when(mock(URL.class).openStream()).thenReturn(mock(InputStream.class)).getMock();
        when(url.getProtocol()).thenReturn("file");
        when(url.toURI()).thenThrow(URISyntaxException.class);
        
        assertThrows(IllegalArgumentException.class, () -> ofResource(url));
    }
    
    
    @Test
    void uri_jar() throws MalformedURLException, URISyntaxException {
        var url = new URL("jar:file:/C:/Program%20Files/test.jar!/foo/bar");
        
        assertEquals("/foo/bar", uri(url).getPath());
    }
    
}

@Introspect("FileObjectsTest")
class FileObjectsIntrospectTest {
    
    @Test
    void scan_other_top_level_class() {
        assertTrue(javac().compile(FileObjects.scan(FileObjectsIntrospectTest.class)).success);
    }
    
}
