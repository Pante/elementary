package com.karuslabs.elementary.junit;

import com.karuslabs.elementary.junit.annotations.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ToolsExtension.class)
@Introspect
class ToolsExtensionInitializationTest {

    // provideArgument might be called before createTestInstance which previously caused the cached compiler from another
    // class to be wronly used instead. This is usually observable when there are only parameterized tests in a class.
    @ParameterizedTest
    @LabelSource(groups = {"valid group"})
    void provideArgument_before_createTestInstance(String label, Element element, RoundEnvironment round) {
        assertEquals("label value", label);
    }

    static class LabelSourceLabels {

        @Label(value = "label value", group = "valid group")
        void test() {}

    }

}
