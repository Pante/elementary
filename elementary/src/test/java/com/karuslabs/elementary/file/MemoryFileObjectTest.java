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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemoryFileObjectTest {

    MemoryFileObject file;
    ImmutableFileObject source = mock(ImmutableFileObject.class);

    MemoryFileObjectTest() throws URISyntaxException {
        this.file = new MemoryFileObject(new URI("A.java"));
        file.source = source;
    }
    
    
    @Test
    void openInputStream() throws FileNotFoundException {
        file.openInputStream();
        verify(source).openInputStream();
    }
    
    @Test
    void openOutputStream() throws IOException {
        try (var stream = file.openOutputStream()) {
            stream.write("help".getBytes());
        }
        
        assertEquals("help", file.source.getCharContent(true));
    }
    
    @Test
    void openReader() throws FileNotFoundException {
        file.openReader(true);
        verify(source).openReader(true);
    }
    
    @Test
    void openWriter() throws IOException {
        try (var writer = file.openWriter()) {
            writer.write("help");
        }
        
        assertEquals("help", file.source.getCharContent(true));
    }
    
    
    @Test
    void delete() {
        assertTrue(file.delete());
        assertNull(file.source);
    }
    
    
    @Test
    void getCharContent() throws FileNotFoundException, IOException {
        try (var writer = file.openWriter()) {
            writer.write("help");
        }
        assertEquals("help", file.getCharContent(true));
    }
    
    
    @Test
    void getLastModified() throws IOException {
        try (var writer = file.openWriter()) {
            writer.write("help");
        }
        
        assertNotEquals(0L, file.getLastModified());
    }
    
    @Test
    void getLastModified_null() {
        file.source = null;
        assertEquals(0L, file.getLastModified());
    }
    
    
    @Test
    void source_empty() {
        file.source = null;
        assertThrows(FileNotFoundException.class, () -> file.source());
    }
    
    
    @Test
    void toString_() {
        assertEquals("MemoryFileObject{uri = A.java, kind = SOURCE}", file.toString());
    }
    
}
