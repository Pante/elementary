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

import com.karuslabs.elementary.Diagnostics;

import java.io.IOException;
import java.util.List;
import javax.tools.*;

import org.junit.jupiter.api.*;

import static com.karuslabs.elementary.file.FileObjects.ofResource;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Locale.ENGLISH;
import static javax.tools.JavaFileObject.Kind.SOURCE;
import static javax.tools.StandardLocation.*;

import static org.junit.jupiter.api.Assertions.*;

class MemoryFileManagerTest {

    MemoryFileManager manager = new MemoryFileManager(ToolProvider.getSystemJavaCompiler().getStandardFileManager(new Diagnostics(), ENGLISH, UTF_8));
    
    @Test
    void of_relative_location_illegalCharacters() {
        var location = locationFor("string[with] illegal\"characters"); 
        
        assertDoesNotThrow(() -> MemoryFileManager.of(location, "my.package", "MyClass.java"));
    }
    
    @Test
    void of_string_illegalCharacters() {
        var location = locationFor("string[with] illegal\"characters");

        assertDoesNotThrow(() -> MemoryFileManager.of(location, "my.package.MyClass", SOURCE));
    }
    
    
    @Test
    void isSameFile() {
        assertTrue(manager.isSameFile(ofResource("com/karuslabs/elementary/junit/Placeholder.java"), ofResource("com/karuslabs/elementary/junit/Placeholder.java")));
    }
    
    
    @Test
    void getFileInput_input_location() throws IOException {
        assertNotNull(manager.getFileForInput(CLASS_PATH, "", "com/karuslabs/elementary/junit/Placeholder.java"));
    }
    
    @Test
    void getFileInput_output_location() throws IOException {
        assertNull(manager.getFileForInput(CLASS_OUTPUT, "", "Something.java"));
        
        manager.getFileForOutput(CLASS_OUTPUT, "", "Something.java", null);
        assertNotNull(manager.getFileForInput(CLASS_OUTPUT, "", "Something.java"));
    }
    
    
    @Test
    void getJavaFileInput_input_location() throws IOException {
        assertNotNull(manager.getJavaFileForInput(CLASS_PATH, "com/karuslabs/elementary/junit/Placeholder", SOURCE));
    }
    
    @Test
    void getJavaFileInput_output_location() throws IOException {
        assertNull(manager.getJavaFileForInput(CLASS_OUTPUT, "Something", SOURCE));
        
        manager.getJavaFileForOutput(CLASS_OUTPUT, "Something", SOURCE, null);
        assertNotNull(manager.getJavaFileForInput(CLASS_OUTPUT, "Something", SOURCE));
    }
    
    
    @Test
    void generatedSources() {
        var file = manager.getJavaFileForOutput(SOURCE_OUTPUT, "Something", SOURCE, null);
        assertEquals(List.of(file), manager.generatedSources());
    }
    
    
    @Test
    void outputFiles() {
        var file = manager.getJavaFileForOutput(CLASS_OUTPUT, "Something", SOURCE, null);
        assertEquals(List.of(file), manager.outputFiles());
    }
    
}
