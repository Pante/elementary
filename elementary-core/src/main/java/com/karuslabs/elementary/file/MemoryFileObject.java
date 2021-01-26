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

import java.io.*;
import java.net.URI;
import javax.tools.*;

import org.checkerframework.checker.nullness.qual.Nullable;

class MemoryFileObject extends SimpleJavaFileObject {
    
    @Nullable ImmutableFileObject source;
    
    MemoryFileObject(URI uri) {
        super(uri, FileObjects.deduce(uri));
    }
    
    @Override
    public InputStream openInputStream() throws FileNotFoundException {
        return source().openInputStream();
    }
    
    @Override
    public OutputStream openOutputStream() {
        return new ByteArrayOutputStream() {
            @Override
            public void close() throws IOException {
                super.close();
                source = new ByteFileObject(uri, kind, toByteArray());
            }
        };
    }
    
    
    @Override
    public Reader openReader(@Ignored boolean ignoreEncodingErrors) throws FileNotFoundException {
        return source().openReader(ignoreEncodingErrors);
    }
    
    @Override
    public Writer openWriter() {
        return new StringWriter() {
            @Override
            public void close() throws IOException {
                super.close();
                source = new StringFileObject(uri, kind, toString());
            }
        };
    }
    
    
    @Override
    public boolean delete() {
        source = null;
        return true;
    }
    
    
    @Override
    public CharSequence getCharContent(@Ignored boolean ignoreEncodingErrors) throws FileNotFoundException {
        return source().toString();
    }
    
    @Override
    public long getLastModified() {
        return source == null ? 0 : source.timestamp;
    }
    
    
    ImmutableFileObject source() throws FileNotFoundException {
        if (source != null) {
            return source;
            
        } else {
            throw new FileNotFoundException();
        }
    }
    
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{uri = " + toUri() + ", kind = " + kind + "}";
    }
    
}
