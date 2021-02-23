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
import java.nio.file.Path;
import java.util.*;
import javax.tools.*;

public abstract class ForwardingFileManager implements StandardJavaFileManager {

    private final StandardJavaFileManager manager;
    
    public ForwardingFileManager(StandardJavaFileManager manager) {
        this.manager = manager;
    }
    
    @Override
    public boolean contains(Location location, FileObject fo) throws IOException {
        return manager.contains(location, fo);
    }
    
    @Override
    public boolean isSameFile(FileObject a, FileObject b) {
        return manager.isSameFile(a, b);
    }

    
    @Override
    public Iterable<? extends JavaFileObject> getJavaFileObjectsFromFiles(Iterable<? extends File> files) {
        return manager.getJavaFileObjectsFromFiles(files);
    }
    
    @Override
    public Iterable<? extends JavaFileObject> getJavaFileObjectsFromStrings(Iterable<String> strings) {
        return manager.getJavaFileObjectsFromStrings(strings);
    }


    @Override
    public Iterable<? extends JavaFileObject> getJavaFileObjects(File... files) {
        return manager.getJavaFileObjects(files);
    }

    @Override
    public Iterable<? extends JavaFileObject> getJavaFileObjects(String... strings) {
        return manager.getJavaFileObjects(strings);
    }

    
    @Override
    public Iterable<? extends File> getLocation(Location location) {
        return manager.getLocation(location);
    }
    
    @Override
    public Location getLocationForModule(Location location, String moduleName) throws IOException {
        return manager.getLocationForModule(location, moduleName);
    }
    
    @Override
    public Location getLocationForModule(Location location, JavaFileObject file) throws IOException {
        return manager.getLocationForModule(location, file);
    }
    
    @Override
    public Iterable<Set<Location>> listLocationsForModules(Location location) throws IOException {
        return manager.listLocationsForModules(location);
    }
    
    
    @Override
    public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) throws IOException {
        return manager.getJavaFileForInput(location, className, kind);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        return manager.getJavaFileForOutput(location, className, kind, sibling);
    }

    
    @Override
    public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
        return manager.getFileForInput(location, packageName, relativeName);
    }

    @Override
    public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling) throws IOException {
        return manager.getFileForOutput(location, packageName, relativeName, sibling);
    }
    
    
    @Override
    public void setLocation(Location location, Iterable<? extends File> files) throws IOException {
        manager.setLocation(location, files);
    }
    
    @Override
    public void setLocationForModule(Location location, String moduleName, Collection<? extends Path> paths) throws IOException {
        manager.setLocationForModule(location, moduleName, paths);
    }
    
    @Override
    public void setLocationFromPaths(Location location, Collection<? extends Path> paths) throws IOException {
        manager.setLocationFromPaths(location, paths);
    }
    
    @Override
    public void setPathFactory(PathFactory factory) {
        manager.setPathFactory(factory);
    }
    
    
    @Override
    public boolean hasLocation(Location location) {
        return manager.hasLocation(location);
    }
    
    
    @Override
    public ClassLoader getClassLoader(Location location) {
        return manager.getClassLoader(location);
    }
    
    @Override
    public <S> ServiceLoader<S> getServiceLoader(Location location, Class<S> service) throws  IOException {
        return manager.getServiceLoader(location, service);
    }

    
    @Override
    public Iterable<JavaFileObject> list(Location location, String pack, Set<JavaFileObject.Kind> kinds, boolean recursive) throws IOException {
        return manager.list(location, pack, kinds, recursive);
    }

    
    @Override
    public Path asPath(FileObject file) {
        return manager.asPath(file);
    }
    
    @Override
    public String inferBinaryName(Location location, JavaFileObject file) {
        return manager.inferBinaryName(location, file);
    }
    
    @Override
    public String inferModuleName(Location location) throws IOException {
        return manager.inferModuleName(location);
    }

    
    @Override
    public boolean handleOption(String current, Iterator<String> remaining) {
        return manager.handleOption(current, remaining);
    }
    
    @Override
    public int isSupportedOption(String option) {
        return manager.isSupportedOption(option);
    }


    @Override
    public void flush() throws IOException {
        manager.flush();
    }

    @Override
    public void close() throws IOException {
        manager.close();
    }

}
