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

import java.io.*;
import java.util.List;
import javax.tools.*;
import javax.tools.JavaFileManager.Location;
import javax.tools.StandardJavaFileManager;
//import javax.tools.StandardJavaFileManager.PathFactory;

import org.junit.jupiter.api.*;

import static org.mockito.Mockito.*;

class ForwardingFileManagerTest {
    
    static class Manager extends ForwardingFileManager {
        Manager(StandardJavaFileManager manager) {
            super(manager);
        }
    }

    StandardJavaFileManager internal = mock(StandardJavaFileManager.class);
    ForwardingFileManager manager = new Manager(internal);
    Location location = mock(Location.class);
    JavaFileObject a = mock(JavaFileObject.class);
    JavaFileObject b = mock(JavaFileObject.class);
    
    @Test
    void contains() throws IOException {
        manager.contains(location, a);
        verify(internal).contains(location, a);
    }
    
    @Test
    void isSameFile() {
        manager.isSameFile(a, b);
        verify(internal).isSameFile(a, b);
    }
    
    @Test
    void getJavaFileObjectsFromFiles() {
        var files = List.of(mock(File.class));
        
        manager.getJavaFileObjectsFromFiles(files);
        verify(internal).getJavaFileObjectsFromFiles(files);
    }
    
    @Test
    void getJavaFileObjectsFromStrings() {
        manager.getJavaFileObjectsFromStrings(List.of("a"));
        verify(internal).getJavaFileObjectsFromStrings(List.of("a"));
    }
    
    
    @Test
    void getJavaFileObjects_files() {
        var file = mock(File.class);
        
        manager.getJavaFileObjects(file);
        verify(internal).getJavaFileObjects(file);
    }
    
    @Test
    void getJavaFileObjects_strings() {
        manager.getJavaFileObjects("a");
        verify(internal).getJavaFileObjects("a");
    }
    
    @Test
    void getLocation() {
        manager.getLocation(location);
        verify(internal).getLocation(location);
    }
//    
//    
//    @Test
//    void getLocationForModule_string() throws IOException {
//        manager.getLocationForModule(location, "name");
//        verify(internal).getLocationForModule(location, "name");
//    }
//    
//    @Test
//    void getLocationForModule_file() throws IOException {
//        manager.getLocationForModule(location, a);
//        verify(internal).getLocationForModule(location, a);
//    }
//
//
//    @Test
//    void getJavaFileForOutput_class_name() throws IOException {
//        manager.getJavaFileForOutput(location, "type", JavaFileObject.Kind.HTML, a);
//        verify(internal).getJavaFileForOutput(location, "type", JavaFileObject.Kind.HTML, a);
//    }
//
//
//    @Test
//    void getFileForInput_package_name() throws IOException {
//        manager.getFileForInput(location, "package", "relative");
//        verify(internal).getFileForInput(location, "package", "relative");
//    }
//
//    @Test
//    void getFileForOutput_package_name() throws IOException {
//        manager.getFileForOutput(location, "package", "relative", a);
//        verify(internal).getFileForOutput(location, "package", "relative", a);
//    }
//
//
//    @Test
//    void setLocationForModule() throws IOException {
//        manager.setLocationForModule(location, "module", List.of());
//        verify(internal).setLocationForModule(location, "module", List.of());
//    }
//
//
//    @Test
//    void setLocationFromPaths() throws IOException {
//        manager.setLocationFromPaths(location, List.of());
//        verify(internal).setLocationFromPaths(location, List.of());
//    }
//
//
    // Fails
//    @Test
//    void setPathFactory() {
//        var factory = mock(PathFactory.class);
//        manager.setPathFactory(factory);
//
//        verify(internal).setPathFactory(factory);
//    }
//
//
//    @Test
//    void getServiceLoader() throws IOException {
//        manager.getServiceLoader(location, String.class);
//        verify(internal).getServiceLoader(location, String.class);
//    }
//
//
    @Test
    void asPath() {
        manager.asPath(a);
        verify(internal).asPath(a);
    }

//    @Test
//    void close() throws IOException {
//        manager.close();
//        verify(internal).close();
//    }
    
}

