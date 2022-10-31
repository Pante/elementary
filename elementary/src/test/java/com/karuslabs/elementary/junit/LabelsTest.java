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
package com.karuslabs.elementary.junit;

import com.karuslabs.elementary.junit.annotations.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ToolsExtension.class)
@Classpath("com.karuslabs.elementary.junit.DifferentLabels")
class LabelsTest {

    Labels labels = Tools.labels();
    
    @Test
    void get() {
        assertNotNull(labels.get("method"));
    }
    
    @Test
    void get_nonexistent() {
        assertEquals(
            "An element annotated with @Label(\"invalid\") does not exist. Did you spell the label correctly?",
            assertThrows(NoSuchElementException.class, () -> labels.get("invalid")).getMessage()
        );
    }
    
    @Test
    void single_throws() {
        assertEquals(
            "Labels.single() can only be called if 1 element is annotated with @Label. However, there are 2 elements annotated with @Label.",
            assertThrows(IllegalStateException.class, () -> labels.single()).getMessage()
        );
    }
    
    @Test
    void group() {
        assertEquals(Set.of("class"), labels.group("").keySet());
        assertEquals(Set.of("method"), labels.group("methods").keySet());
    }
    
    @Test
    void group_throws() {
        assertEquals(
            "The group, \"invalid\" does not exist. Did you spell the group correctly?",
            assertThrows(NoSuchElementException.class, () -> labels.group("invalid")).getMessage()
        );
    }

    @Test
    void all() {
        assertEquals(2, labels.all().size());
    }
    
    @Test
    void labels() {
        assertEquals(2, labels.groups().size());
    }
    
    @Test
    void size() {
        assertEquals(2, labels.size());
    }
    
}


// This needs to be a separate class since we want to load a different class with a single label.
@ExtendWith(ToolsExtension.class)
@Classpath("com.karuslabs.elementary.junit.SingleLabel")
class LabelSingleTest {

    Labels labels = Tools.labels();
    
    @Test
    void single() {
        
    }

}
