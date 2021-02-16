package a;

/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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

import a.ToolkitProcessor.Toolkit;
import javax.lang.model.type.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(Extendy.class)
class AssertionsTest {
    
<<<<<<< Updated upstream
//    TypeMirrors types = mock(TypeMirrors.class);
//    TypeMirror type = when(mock(TypeMirror.class).getKind()).thenReturn(TypeKind.BOOLEAN).getMock();
    
    Toolkit kit = Extendy.kit();
    TypeMirror type = kit.types.getPrimitiveType(TypeKind.BOOLEAN);
=======
    Toolkit kit;
    TypeMirror type;
    
    AssertionsTest(Toolkit kit) {
        this.kit = kit;
        type = kit.types.getPrimitiveType(TypeKind.BOOLEAN);
    }
>>>>>>> Stashed changes
    
    @Test
    void s() {
        assertNotNull(kit.elements);
        assertTrue(kit.types.isSameType(kit.types.type(AssertionsTest.class), kit.types.type(AssertionsTest.class)));
        System.out.println(kit);
    }
    
}
