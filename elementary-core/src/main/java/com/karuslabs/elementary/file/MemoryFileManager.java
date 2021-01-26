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

import com.karuslabs.annotations.Ignored;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import javax.tools.*;
import javax.tools.JavaFileObject.Kind;

import org.checkerframework.checker.nullness.qual.Nullable;

public class MemoryFileManager extends ForwardingFileManager {

    static URI of(Location location, String pack, String relative) {
        return URI.create("mem:///" + location.getName() + "/" + (pack.isEmpty() ? "" : (pack.replace('.', '/') + "/")) + relative);
    }
    
    static URI of(Location location, String type, Kind kind) {
        return URI.create("mem:///" + location.getName() + "/" + type.replace('.', '/') + kind.extension);
    }

    
    private final Map<URI, JavaFileObject> files = new HashMap<>();
    
    public MemoryFileManager(StandardJavaFileManager manager) {
        super(manager);
    }
    
    
    @Override
    public boolean isSameFile(FileObject a, FileObject b) {
        return a.toUri().equals(b.toUri());
    }
    
    
    @Override
    public @Nullable FileObject getFileForInput(Location location, String pack, String relative) throws IOException {
        if (location.isOutputLocation()) {
            return files.get(of(location, pack, relative));
                    
        } else {
            return super.getFileForInput(location, pack, relative);
        }
    }
    
    @Override
    public @Nullable JavaFileObject getJavaFileForInput(Location location, String type, Kind kind) throws IOException {
        if (location.isOutputLocation()) {
            return files.get(of(location, type, kind));
            
        } else {
            return super.getJavaFileForInput(location, type, kind);
        }
    }
    
    
    @Override
    public FileObject getFileForOutput(Location location, String pack, String relative, @Ignored FileObject sibling) {
        return files.computeIfAbsent(of(location, pack, relative), MemoryFileObject::new);
    }
    
    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String type, Kind kind, @Ignored FileObject sibling) {
        return files.computeIfAbsent(of(location, type, kind), MemoryFileObject::new);
    }
    
    
    public List<JavaFileObject> generatedSources() {
        var sources = new ArrayList<JavaFileObject>();
        var prefix = "/" + StandardLocation.SOURCE_OUTPUT.name();
        
        for (var entry : files.entrySet()) {
            if (entry.getKey().getPath().startsWith(prefix) && entry.getValue().getKind() == Kind.SOURCE) {
                sources.add(entry.getValue());
            }
        }
        
        return sources;
    }
    
    public List<JavaFileObject> outputFiles() {
        return new ArrayList<>(files.values());
    }
    
}
