This document describes how to use Elementary to unit test an annotation processor. We assume that the reader is acquainted with annotation processing and the [`javax.lang.model.*`](https://docs.oracle.com/en/java/javase/11/docs/api/java.compiler/javax/lang/model/package-summary.html) packages.

If you learn best by jumping straight into code, check out the [Gradle demo project](https://github.com/toolforger/elementary-demo) contributed by ToolForger!

Tests are required to be compiled against Java 11 although the classes under test can be compiled against earlier version of Java. In addition, the library requires a minimum JUnit version of `5.9.3`.

Do join [Karus Labs' discord](https://discord.gg/eBDVT2guwV) if you require any assistance.

This article details _how to use Elementary_, if you are interested in _how Elementary works_, please read [The Problem With Annotation Processors](https://dzone.com/articles/the-problem-with-annotation-processors).

## A Brief Tour of Elementary

At the heart of Elementary is the standalone compiler upon which everything else is built, including the `JavacExtension` and `ToolsExtension` JUnit extensions. Configuration of said extensions is done through annotations on the test classes and methods. In the interest of keeping things short and sweet, we shall skim over the standalone compiler since it is seldom used barring a few advanced cases. Most will find the higher-level `JavacExtension` and `ToolsExtension` more pleasant to use anyways.

### Downloading Elementary
Elementary is available as a maven artifact.

[![releases-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/elementary-releases/com/karuslabs/elementary/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/elementary-releases/com/karuslabs/elementary/)
[![snapshots-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/elementary-snapshots/com/karuslabs/elementary/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/elementary-snapshots/com/karuslabs/elementary/)

```xml
<!-- Requires JUnit 5.9.3 & above -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.9.3</version>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>com.karuslabs</groupId>
    <artifactId>elementary</artifactId>
    <version>2.0.0</version>
    <scope>test</scope>
</dependency>
```

## The `JavacExtension`
For each test, `JavacExtension` compiles a suite of test files with the given annotation processor(s). The results of the compilation are then funneled to the test method for subsequent assertions. All configuration is handled via annotations with no additional set-up or tear-down required.

We recommend using the `JavacExtension` in the following scenarios:
* Black-box testing an annotation processor
* Testing the results of a compilation
* Testing an extremely simple annotation processor

A typical usage of the `JavacExtension` will look similar to the following:
```java
import com.karuslabs.elementary.Results;
import com.karuslabs.elementary.junit.JavacExtension;
import com.karuslabs.elementary.junit.annotations.Case;
import com.karuslabs.elementary.junit.annotations.Classpath;
import com.karuslabs.elementary.junit.annotations.Options;
import com.karuslabs.elementary.junit.annotations.Processors;

@ExtendWith(JavacExtension.class)
@Options("-Werror")
@Processors({ImaginaryProcessor.class})
@Classpath("my.package.ValidCase")
class ImaginaryTest {
    @Test
    void process_string_field(Results results) {
        assertEquals(0, results.find().errors().count());
    }
    
    @Test
    @Classpath("my.package.InvalidCase")
    void process_int_field(Results results) {
        assertEquals(1, results.find().errors().contains("Element is not a string").count());
    }
}

@SupportedAnnotationTypes({"*"})
class ImaginaryProcessor extends AnnotationProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment round) {
        var elements = round.getElementsAnnotatedWith(Case.class);
        for (var element : elements) {
            if (element instanceof VariableElement)) {
                var variable = (VariableElement) element;
                if (!types.isSameType(variable.asType(), types.type(String.class))) {
                    logger.error(element, "Element is not a string");
                }
            } else {
                logger.error(element, "Element is not a variable");
            }
        }
        return false;
    }
}
```

Let’s break down the code snippet.

* By annotating the test class with `@Options`, we can specify the compiler flags used when compiling the test cases. In this snippet, `-Werror` indicates that all warnings will be treated as errors.

* To specify which annotation processor(s) is to be invoked with the compiler, we can annotate the test class with `@Processors`.

* Test cases can be included for compilation by annotating the test class with either `@Classpath`, `@Resource` or `@Inline`. Java source files on the classpath can be included using `@Classpath` or `@Resource` while strings inside `@Inline` can be transformed into an inline source file for compilation. One difference between `@Classpath` and `@Resource` is how directories are separated. `@Classpath` separates directories using `.` while `@Resource` uses `/`.

* An annotation’s scope is tied to its target’s scope. If a test class is annotated, the annotation will be applied for all test methods in that class. On the same note, an annotation on a test method will only be applied on said method.

* Results represent the results of a compilation. We can specify Results as a parameter of test methods to obtain the compilation results. In this snippet, `process_string_field(...)` will receive the results for `ValidCase` while `process_int_field(...)` will receive the results for both `ValidCase` and `InvalidCase`.

### Supported Annotations
| Annotation    | Description                                                                                                | Target            |
|---------------|------------------------------------------------------------------------------------------------------------|-------------------|
| `@Classpath`  | Denotes a class on the current classpath to be included for compilation. Directories are separated by `.`. | Test class/method |
| `@Inline`     | A string representation of a class to be included for compilation.                                         | Test class/method |
| `@Options`    | Represents the compiler flags to be used during compilation.                                               | Test class/method |
| `@Processors` | The annotation processors to be applied during compilation.                                                | Test class/method |
| `@Resource`   | Denotes a file on the current classpath to be included for compilation. Directories are separated by `/`.  | Test class/method |


## The `ToolsExtension`
A Java compiler with a blocking annotation processor is invoked on a daemon thread each time an instance of a test class is created. During which, compilation is halted and the annotation processing environment made available to the test. In addition, test cases can be written in plain old Java and its `Element` representation subsequently retrieved in a test. Similar to `JavacExtension`, all configuration is handled through annotations.

We recommend using `ToolsExtenion` in the following circumstances:
* White-box testing an annotation processor
* Testing individual components of an annotation processor
* Testing an annotation processor against multiple complex test cases
* Requiring access to the annotation processing environment

A typical usage of the `ToolsExtension` will look similar to the following:

```java
import com.karuslabs.elementary.junit.Labels;
import com.karuslabs.elementary.junit.Tools;
import com.karuslabs.elementary.junit.ToolsExtension;
import com.karuslabs.elementary.junit.annotations.Label;
import com.karuslabs.elementary.junit.annotations.LabelSource;
import com.karuslabs.elementary.junit.annotations.Inline;
import com.karuslabs.elementary.junit.annotations.Introspect;
import com.karuslabs.utilitary.type.TypeMirrors;

import javax.annotation.processing.RoundEnvironment;

@ExtendWith(ToolsExtension.class) // -------(1)
@Introspect                       // -------^
@Inline(name = "Samples", source = { // --------(2)
    "import com.karuslabs.elementary.junit.annotations.Label;",
    "",
    "class Samples {",
    "  @Label(\"first\") String first;",
    "}"})
class ToolsExtensionExampleTest {

    Lint lint = new Lint(Tools.typeMirrors()); // --------(3)

    @Label("second")
    boolean second() {
        return false;
    } // --------(4)


    @Test
    void lint_string_variable(Labels labels) { // --------(5)
        var first = labels.get("first");
        assertTrue(lint.lint(first));
    }

    @Test
    void lint_method_that_returns_boolean(Labels labels) {
        var second = labels.get("second");
        assertFalse(lint.lint(second));
    }


    // This test will be called twice. Once with `stringVariable`, and another with `booleanVariable`.
    @ParameterizedTest
    @LabelSource(groups = {"my group"}) // --------(6)
    void lint_grouped_elements(String label, Element element, RoundEnvironment environment) { // --------(7)
        assertTrue(lint.lint(element)); 
    }
    
    @Label(value = "string variable", group = "my group")
    String stringVariable = "";

    @Label(value = "boolean variable", group = "my group")
    boolean booleanVariable = false;

}

class Lint {

    final TypeMirrors types;
    final TypeMirror expectedType;

    Lint(TypeMirrors types) {
        this.types = types;
        this.expectedType = types.type(String.class);
    }

    public boolean lint(Element element) {
        if (!(element instanceof VariableElement)) {
            return false;
        }

        var variable = (VariableElement) element;
        return types.isSameType(expectedType, variable.asType());
    }

}
```
Let’s break down the code snippet:
1. Annotating the class with `@Introspect` includes said test file for indexing. The annotated test class must also be extended  with `ToolsExtension`. An additional name must be specified in the annotation if the annotated class and file is differently named.

2. By annotating the class with `@Inline` we can specify an inline Java source file which `ToolsExtension` includes for compilation.

3. The annotation processing environment can be accessed via either the `Tools` class or dependency injection into the test class's constructor or test methods.

4. By annotating an element with `@Label` inside a Java source file, we can fetch its corresponding element from Cases. A `@Label` may also contain a label to simplify retrieval. 
  This can be used in conjunction with `@Introspect` to make elements in the test file available.

5. Through `Labels`, we can fetch elements by their label. You can obtain an instance of `Labels `via `Tools.labels()` or through dependency injection.

6. You can provide elements to a parameterized test using `@LabelSource`. A `@LabelSource` will provide all elements in the specified groups.

7. A label, element and the annotation processing environment is provided through dependency injection for each test iteration. 
   The label and corresponding element _must_ be the first parameters of a test method if used in conjunction with other parameters injected by ToolsExtension.

### Supported Annotations
| Annotation     | Description                                                                                                                                                                                                                                                                                                                                                                        | Target        |
|----------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------|
| `@Label`       | Denotes that the annotated target is a test case that can be retrieved by `Cases`.                                                                                                                                                                                                                                                                                                 | Any test case |
| `@LabelSource` | Provides the labelled elements in the specified groups to the parameterized test.                                                                                                                                                                                                                                                                                                  |               |
| `@Classpath`   | Denotes a class on the current classpath to be included for compilation. Directories are separated by `.`.                                                                                                                                                                                                                                                                         | Test class    |
| `@Inline`      | A string representation of a class to be included for compilation.                                                                                                                                                                                                                                                                                                                 | Test class    |
| `@Introspect`  | Includes the test file for compilation. The annotated test class must also be extended with `ToolsExtension`. An additional name must be specified in the annotation if the annotated class and file is differently named. **May require additional configuration, please see [`@Introspect` Configuration](https://github.com/Pante/elementary/wiki/@Introspect-Configuration).** | Test class    |
| `@Resource`    | Denotes a file on the current classpath to be included for compilation. Directories are separated by `/`.                                                                                                                                                                                                                                                                          | Test class    |

## Further Reading

Elementary provides two more examples that illustrate how to use `JavacExtension` and `ToolsExtension` which may be found [here](https://github.com/Pante/elementary/tree/master/elementary/src/test/java/example). 
In addition, Elementary is used in production by Satisfactory to test the multiple assertions that it contains, the tests can be found [here](https://github.com/Pante/elementary/tree/master/satisfactory/src/test/java/com/karuslabs/satisfactory).

The Javadocs can be found [here](https://repo.karuslabs.com/repository/elementary/latest/elementary/apidocs/index.html).

## Parallel JUnit Tests
Elementary does not support parallel testing (at the moment).


