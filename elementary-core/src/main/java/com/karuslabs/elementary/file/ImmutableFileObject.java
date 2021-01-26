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
import java.nio.charset.Charset;
import javax.tools.*;

abstract class ImmutableFileObject extends SimpleJavaFileObject {
    
    final long timestamp = System.currentTimeMillis();
    
    ImmutableFileObject(URI uri, JavaFileObject.Kind kind) {
        super(uri, kind);
    }
    
    @Override
    public abstract InputStream openInputStream();

    @Override
    public abstract Reader openReader(@Ignored boolean ignoreEncodingErrors);
    
    @Override
    public abstract CharSequence getCharContent(@Ignored boolean ignoreEncodingErrors);
    
}

class ByteFileObject extends ImmutableFileObject {

    private final byte[] bytes;
    private @Lazy String string;
    
    ByteFileObject(URI uri, JavaFileObject.Kind kind, byte[] bytes) {
        super(uri, kind);
        this.bytes = bytes;
    }
    
    @Override
    public InputStream openInputStream() {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public Reader openReader(@Ignored boolean ignoreEncodingErrors) {
        return new StringReader(toString());
    }
    
    @Override
    public CharSequence getCharContent(@Ignored boolean ignoreEncodingErrors) {
        if (string == null) {
            string =  new String(bytes, Charset.defaultCharset());
        }
        
        return string;
    }
    
}

class StringFileObject extends ImmutableFileObject {
    
    private final String string;
    private @Lazy byte[] bytes;
    
    StringFileObject(URI uri, Kind kind, String string) {
        super(uri, kind);
        this.string = string;
    }
    
    @Override
    public InputStream openInputStream() {
        if (bytes == null) {
            bytes = string.getBytes(Charset.defaultCharset());
        }
        
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public Reader openReader(boolean ignoreEncodingErrors) {
        return new StringReader(string);
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return string;
    }
    
}
