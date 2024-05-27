# A Brief Tour of Elementary
[![Discord](https://img.shields.io/discord/140273735772012544.svg?style=flat-square)](https://discord.gg/uE4C9NQ)

This document describes how to use Elementary. We assume that you are already familiar with annotation processing and the
[`javax.lang.model.*`](https://docs.oracle.com/en/java/javase/11/docs/api/java.compiler/javax/lang/model/package-summary.html) packages.

Underneath the hood, Elementary just manages a forked compiler which `JavacExtension` and `ToolsExtension` JUnit extensions rely on.
Configuration is done by annotating the test classes and methods.

## Getting Started
Elementary is available as a maven artifact. It requires Java 11+.

[![releases-maven](https://img.shields.io/maven-central/v/com.karuslabs/elementary)](https://central.sonatype.com/artifact/com.karuslabs/elementary/)

### Maven

```xml
<!-- Requires JUnit 5.9.3 & above -->
<dependencies>
   <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <version>5.9.3</version>
      <scope>test</scope>
   </dependency>

   <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-params</artifactId>
      <version>5.9.3</version>
      <scope>test</scope>
   </dependency>

   <dependency>
      <groupId>com.karuslabs</groupId>
      <artifactId>elementary</artifactId>
      <version>3.0.0</version>
      <scope>test</scope>
   </dependency>
</dependencies>
```

```xml
<!-- Required if you're using the @Introspect annotation -->
<build>
   <plugins>
      <plugin>
         <artifactId>maven-resources-plugin</artifactId>
         <version>3.2.0</version>
         <executions>
            <execution>
               <id>copy-resources</id>
               <phase>process-test-classes</phase>
               <goals>
                  <goal>copy-resources</goal>
               </goals>
               <configuration>
                  <outputDirectory>${basedir}/target/test-classes/</outputDirectory>
                  <resources>
                     <resource>
                        <directory>${basedir}/src/test/java/</directory>
                        <includes>
                           <include>**/*.*</include>
                        </includes>
                     </resource>
                  </resources>
               </configuration>
            </execution>
         </executions>
      </plugin>
   </plugins>
</build>
```

### Gradle
```groovy
dependencies {
   testImplementation 'org.junit.jupiter:junit-jupiter-api:5.9.3'
   testImplementation 'org.junit.jupiter:junit-jupiter-params:5.9.3'
   testImplementation 'com.karuslabs:elementary:3.0.0'
}
```

```groovy
// Required if you're using the @Introspect annotation
task copyTestSources(type: Copy) {
   from file("${project.projectDir}/src/test/java/")
   into file("${project.projectDir}/build/classes/java/test/")
}

test {
   dependsOn 'copyTestSources'
}
```


## `JavacExtension`
For each test, `JavacExtension` compiles the files given by `@Classpath`, `@Inline` and `@Resource` with the annotation
processors given by `@Processors`. The results are then funnelled into the test for inspection.

Configuration is managed via annotations. No additional set-up or tear-down is required.

Consider using `JavacExtension` in these cases:
* Black-box testing an annotation processor
* Testing compilation results
* Testing a simple annotation processor

### Example
```java
import com.karuslabs.elementary.Results;
import com.karuslabs.elementary.junit.JavacExtension;
import com.karuslabs.elementary.junit.annotations.Case;
import com.karuslabs.elementary.junit.annotations.Classpath;
import com.karuslabs.elementary.junit.annotations.Generation;
import com.karuslabs.elementary.junit.annotations.Options;
import com.karuslabs.elementary.junit.annotations.Processors;

@ExtendWith(JavacExtension.class)
@Options("-Werror") // (1)
@Processors({ImaginaryProcessor.class}) // (2)
@Classpath("my.package.ValidCase") // (3) (4)
@Generation(retain = true, classes = "path/to/generated-classes", sources = "path/to/generated-sources") // (5)
class ImaginaryTest {
  @Test
  void process_string_field(Results results) { // (6)
    assertEquals(0, results.find().errors().count());
  }

  @Test
  @Classpath("my.package.InvalidCase") // (4)
  void process_int_field(Results results) { // (6)
    assertEquals(1, results.find().errors().contains("Element is not a string").count());
  }
}

@SupportedAnnotationTypes({"*"})
class ImaginaryProcessor extends AnnotationProcessor {
  @Override
  public boolean process(Set<? extends TypeElement> set, RoundEnvironment round) {
    var elements = round.getElementsAnnotatedWith(Case.class);
    for (var element : elements) {
      if (element instanceof VariableElement variable) {
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

Let’s break down the example:
1. `@Options` allows you to specify compiler flags. In this case, `-Werror` indicates that warnings should be treated as errors.

2. `@Processors` allows you to specify annotation processors to run with the compiler. In this case, `ImaginaryProcessor`.

3. `@Classpath` indicates a Java source file on the classpath to compile. They can also be specified using `@Resource`
   and `@Inline`. `@Resource` also searches the classpath for the Java source file. The difference is that `@Classpath`
   separates directories using `.` while `@Resource` uses `/`. `@Inline` contains a string which is transformed into an
   inline source file for compilation.

4. `@Generation` allows you to specify the location of generated classes and source files. In this case, `retain = true`
   indicates that generated classes and sources should not be automatically deleted after each test. This is useful for
   debugging issues with generated files. By default, classes and source files are generated in temporary directories and 
   automatically deleted after each test.

5. Annotation scopes are tied to the annotated class/method. An annotation on a test class is applied to all test methods
   in that class while an annotation on a test method is applied to only that method.

6. `Results` is the compilation results. It is injected as a test method argument. `process_string_field(...)` will receive
   the results for `ValidCase` while `process_int_field(...)` will receive the results for both `ValidCase` and `InvalidCase`.

### Supported Annotations
| Annotation    | Description                                                                          | Target            |
|---------------|--------------------------------------------------------------------------------------|-------------------|
| `@Classpath`  | Includes a class on the classpath for compilation. Directories are separated by `.`. | Test class/method |
| `@Inline`     | Includes a string representation of a class for compilation.                         | Test class/method |
| `@Options`    | The compiler flags.                                                                  | Test class/method |
| `@Generation` | The location of generated classes and source files.                                  | Test class        |
| `@Processors` | The annotation processors to apply.                                                  | Test class/method |
| `@Resource`   | Includes a class on the classpath for compilation. Directories are separated by `/`  | Test class/method |


## `ToolsExtension`
Provides an annotation processing environment by starting & temporarily suspending a Java compiler on a separate thread.
Test cases can be written in Java and their `Element` representations retrieved in tests.

Configuration is managed via annotations. No additional set-up or tear-down is required.

Consider using `ToolsExtension` in these cases:
* White-box testing an annotation processor
* Testing an annotation processor's individual components
* Testing an annotation processor against multiple complex test cases
* Require access to the annotation processing environment

### Example
```java
import com.karuslabs.elementary.junit.Labels;
import com.karuslabs.elementary.junit.Tools;
import com.karuslabs.elementary.junit.ToolsExtension;
import com.karuslabs.elementary.junit.annotations.Generation;
import com.karuslabs.elementary.junit.annotations.Inline;
import com.karuslabs.elementary.junit.annotations.Introspect;
import com.karuslabs.elementary.junit.annotations.Label;
import com.karuslabs.elementary.junit.annotations.LabelSource;
import com.karuslabs.utilitary.type.TypeMirrors;

import javax.annotation.processing.RoundEnvironment;

@ExtendWith(ToolsExtension.class)
@Introspect // (1)
@Inline(name = "Samples", source = { // (2)
  """
  import com.karuslabs.elementary.junit.annotations.Label;
  
  class Samples {
    @Label("first") String first;"
  }
  """
})
@Generation(retain = true, classes = "path/to/generated-classes", sources = "path/to/generated-sources") // (3)
class ToolsExtensionExampleTest {

  Lint lint = new Lint(Tools.typeMirrors()); // (4)

  @Label("second")  // (6)
  boolean second() {
    return false;
  }
  
  @Test
  void lint_string_variable(Labels labels) {
    var first = labels.get("first");
    assertTrue(lint.lint(first));
  }
  
  @Test
  void lint_method_that_returns_boolean(Labels labels) {
    var second = labels.get("second"); // (6)
    assertFalse(lint.lint(second));
  }
  
  @ParameterizedTest
  @LabelSource(groups = {"my group"}) // (7)
  void lint_grouped_elements(String label, Element element, RoundEnvironment environment) { // (4) (8)
    assertTrue(lint.lint(element));
  }
  
  @Label(value = "string variable", group = "my group") // (5) (7)
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
    return element instanceof VariableElement variable && types.isSameType(expectedType, variable.asType());
  }
}
```
Let’s break down the example:
1. `@Introspect` includes current file for compilation by `ToolsExtension`. The annotated class must also be annotated 
   with `ToolsExtension`. An additional name argument is required if the annotated class and file name are different.

2. `@Inline` allows you to specify an inline Java source file for `ToolsExtension` to compile.

3. `@Generation` allows you to specify the location of generated classes and source files. In this case, `retain = true`
   indicates that generated classes and sources should not be automatically deleted after each test. This is useful for
   debugging issues with generated files. By default, classes and source files are generated in temporary directories and
   automatically deleted after each test.

4. `Tools` contains utilities and the current annotation processing environment such as `RoundEnvironment`. They can
   access via `Tools` or individually injected into the test class's constructor and methods.

5. Annotating things with `@Label` inside a source file compiled by `ToolsExtension` allows you to get their `Element`s 
   in tests. A `@Label` contains a unique label and an optional group for parameterized tests.

6. You can get `Element`s by their `@Label` using `Labels`. `Labels` can be accessed via `Tools` or injected into the test 
   class's constructor and methods.

7. You can supply elements to a parameterized test using `@LabelSource`. A `@LabelSource` will iterate through all elements
   in the group(s).

8. A label, element and the annotation processing environment is injected into the parameterized test method. The label 
   and corresponding element **must** be the first parameters when injected alongside utilities and environments.

### Supported Annotations
| Annotation     | Description                                                                                                                                                                                                                                                                                     | Target        |
|----------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------|
| `@Label`       | Allows the annotated element to be retrieved by `Labels` in tests.                                                                                                                                                                                                                              | Any test case |
| `@LabelSource` | Supplies elements in the given groups to a parameterized test.                                                                                                                                                                                                                                  | Test methods  |
| `@Classpath`   | Includes the Denotes a class on the current classpath to be included for compilation. Directories are separated by `.`.                                                                                                                                                                         | Test class    |
| `@Inline`      | Includes a string representation of a class for compilation.                                                                                                                                                                                                                                    | Test class    |
| `@Introspect`  | Includes the test file for compilation. The annotated test class must also be extended with `ToolsExtension`. An additional name must be specified in the annotation if the annotated class and file are named differently. **Requires additional configuration mentioned in Getting Started.** | Test class    |
| `@Generation`  | The location of generated classes and source files.                                                                                                                                                                                                                                             | Test class    |
| `@Resource`    | Includes a class on the classpath for compilation. Directories are separated by `/`                                                                                                                                                                                                             | Test class    |

## Further Reading

Elementary provides two more examples that illustrate how to use `JavacExtension` and `ToolsExtension` which may be found [here](https://github.com/Pante/elementary/tree/master/elementary/src/test/java/example).
Consider checking out the [Gradle demo project](https://github.com/toolforger/elementary-demo) too.

The Javadocs can be found [here](https://www.javadoc.io/doc/com.karuslabs/elementary).

## Parallel JUnit Tests
Elementary does not support parallel testing (at the moment).


