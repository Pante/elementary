This article briefly describes how to use Satisfactory to compose assertions and matches for `Element`s in an annotation processing environment. We assume that the reader is already familiar with annotation processing and the [`javax.lang.model.*`](https://docs.oracle.com/en/java/javase/11/docs/api/java.compiler/javax/lang/model/package-summary.html) packages.

Satisfactory provides a set of composable assertions that can be woven together to build increasingly intricate assertions in a declarative manner. Compared to an imperative approach, matching elements with an assertion declaratively is far more readable yet less verbose.

## Composing Assertions

Satisfactory provides several "building block" assertions for parts of the Java language, i.e. annotations, modifiers and types. Built upon these "building block" assertions is the provided variable assertions. It is likewise upon the "blocking block" assertions and variable assertions that the method assertions are built.

In the following code snippet, we shall illustrate how to compose an assertion that expects a variable that must be final `String`and annotated with `@Something`.

```java
import javax.lang.model.element.VariableElement;

import static com.karuslabs.satisfactory.Assertions.*;
import static javax.lang.model.element.Modifier.FINAL;

Assertion<VariableElement> field = variable(
    annotations(contains(Something.class)),
    modifiers(contains(FINAL)),
    type(String.class)
).or("Field must be a final String annotated with @Something");
```

Let's break down the code snippet:
* All composing methods, i.e. `variable(...)`, `type(...)` are static imports from [`com.karuslabs.satisfactory.Assertions.*`](https://repo.karuslabs.com/repository/elementary/latest/satisfactory/apidocs/com/karuslabs/satisfactory/Assertions.html).

* The sole purpose of `annotations(...)` and `modifiers(...)` is to emulate named parameters and improve the readability of the code. They simply return the given type and may be omitted.

* `contains(Something.class)` returns an assertion that is satisfied if an element is annotated with `@Something`.

* `contains(FINAL)` returns an assertion that is satisfied if an element is final.

* `type(String.class)` returns an assertion that is satisfied if an element is a `String`.

* `variable(...)` returns a builder of an assertion for variables. The method accepts assertions for testing each part of a method. If no assertion for a part is specified, the default behaviour is to accept anything.

* `or(...)` is used to define a custom condition message for the builder of variable assertions and subsequently returns a variable assertion.
  The condition to satisfy an assertion can be viewed via `Assertion.condition()`. Satisfactory automatically generates condition messages, however, in more complex assertions, i,e, methods and variables, the default error message may not be desired hence the choice to specify a custom error message.

Subsequently, the assertion can be used to test a `Element` as demonstrated in the following code snippet:
```java
import com.karuslabs.utilitary.type.TypeMirrors;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;

import static com.karuslabs.satisfactory.Assertions.*;
import static javax.lang.model.element.Modifier.FINAL;

Assertion<VariableElement> field;

void test(TypeMirrors types, Element element) {
    boolean match = field.test(types, element);
}
```

## Verifying Invocations

The number of times an assertion is satisfied can be verified by wrapping the assertion in a `Times` using the methods provided in [`Sequences`](https://repo.karuslabs.com/repository/elementary/nightly/satisfactory/apidocs/com/karuslabs/satisfactory/sequence/Sequences.html). Satisfactory comes with built-in support for both exact numbers and ranges.

The following code snippet demonstrates the usage of `Times`:
```java
import com.karuslabs.utilitary.type.TypeMirrors;
import com.karuslabs.satisfactory.Assertion;

import java.util.Collection;
import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;

import static com.karuslabs.satisfactory.Assertions.*;
import static com.karuslabs.satisfactory.sequence.Sequences.range;
    
void test(TypeMirrors types, Collection<? extends Element> elements) {
    Assertion<VariableElement> field = variable();
    var times = range(1, 3, assertion);
    for (var element : elements) {
        boolean match = times.test(types, element);
    }
    
    boolean inRange = times.times();
}
```
Let's break down the code snippet:
* All in-built `Times` can be found in [`com.karuslabs.satisfactory.sequence.Sequences.*`](https://repo.karuslabs.com/repository/elementary/latest/satisfactory/apidocs/com/karuslabs/satisfactory/sequence/sequences.html) including `range(...)` which expects the given assertions to be called within a range of times.

* Using `Times.test(...)`, we can test if a given `Element` satisfies an assertion. In addition, the method also tracks the number of successful invocations.

* Whether a `Times` was successfully invoked the expected number of times can be determined via `Times.times()`. Said method also resets the number of successful invocations.

## Sequences

Testing a collection of values may prove more useful than testing individual values in cases where we need to ascertain the order and pattern of values, i.e method parameters. To that end, we provide `Sequence`s that can be composed of either `Assertion`s or `Time`s. Unlike assertions, sequences test a collection of values.

In the following code snippet, we illustrate how to compose a method assertion that expects 1 String parameter and between 2 to 3 int parameters.
```java
import com.karuslabs.satisfactory.Method;
    
import static com.karuslabs.satisfactory.Assertions.*;
import static com.karuslabs.satisfactory.sequence.Sequences.*;
    
Method method = method(
    parameters(equal(
        times(1, variable(type(String.class))),
        range(2, 3, variable(type(int.class)))
    ))
);
```
Let's break down the code snippet:
* `method(...)` returns a method assertion and can be found in [`com.karuslabs.satisfactory.Assertions.*`](https://repo.karuslabs.com/repository/elementary/latest/satisfactory/apidocs/com/karuslabs/satisfactory/Assertions.html).

* `parameters(...)` emulates a named parameter to improve readability. It is completely optional and may be omitted.

* `equal(...)` returns a `Sequence` that accepts `Time`s, it can be found in [`com.karuslabs.satisfactory.sequence.Sequences.*`](https://repo.karuslabs.com/repository/elementary/nightly/satisfactory/apidocs/com/karuslabs/satisfactory/sequence/Sequences.html#equal(com.karuslabs.satisfactory.sequence.Times...))

* `times(...)` and `range(...)` bot return a `Times` that wraps a variable assertion.

## Further Reading
The Javadocs can be found [here](https://repo.karuslabs.com/repository/elementary/latest/satisfactory/apidocs/com/karuslabs/satisfactory).