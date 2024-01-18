# Migrating to Elementary 2

## TL;DR
- `@Case` renamed to `@Label`
- `Cases` renamed to `Labels`
- Replace `Cases.one(String)` with `Labels.get(String)`
- `@Label` values must be unique
- Retrieval by index is longer supported

## Detailed Explanation

This document covers the breaking changes made in Elementary 2.

Elementary 2 aims to provide an API with better ergonomics. This means code written with Elementary 2 will be both less error-prone and easier to understand. 
To achieve that, Elementary's API for retrieving elements during tests had to be overhauled.

Previously, to retrieve an element in Elementary 1.X:
```java
@ExtendWith(ToolsExtension.class)
@Introspect
class ToolsExtensionExampleTest {
    
    Lint lint = new Lint(Tools.typeMirrors());
    
    @Test
    void lint_string_variable(Cases cases) { // Cases can also be obtained via Tools.cases() and used to initialize a field
        var first = cases.one("first");
        assertTrue(lint.lint(first));
    }
    
    @Test
    void lint_method_that_returns_string(Cases cases) {
        var second = cases.get(1); // Alternatively, we can use cases.one("second")
        assertFalse(lint.lint(second));
    }
    
    static class Sample {
        @Case("first") String something;
        @Case String second() { return ""; } // The method/variable name is used as the get if none is specified
    }
    
}
```

In Elementary 2.X, the above will be written as: 
```java
@ExtendWith(ToolsExtension.class)
@Introspect
class ToolsExtensionExampleTest {
    
    Lint lint = new Lint(Tools.typeMirrors());
    
    @Test
    void lint_string_variable(Labels labels) { // Labels can also be obtained via Tools.labels() and used to initialize a field
        var first = labels.get("first");
        assertTrue(lint.lint(first));
    }
    
    @Test
    void lint_method_that_returns_string(Labels labels) {
        var second = labels.group("invalid").get("second"); // Alternatively, we can use labels.get("second")
        assertFalse(lint.lint(second));
    }
    
    static class Sample {
        @Label("first") String something;
        @Label(value = "second", group = "invalid") String second() { return ""; }
    }
    
}
```

Firstly, `@Case` and `Cases` has been renamed to `@Label` and `Labels` respectively. In addition, all methods related to cases have been renamed accordingly.
`@Case` misleads developers into thinking that it is some case distinction (as in "cases of a switch statement"). While that was the original purpose of `@Case`,
we realized after release that it can be used in a wider variety scenarios, some that we did not originally consider. As such, naming it `@Case` will unwittingly
shoehorn developers into thinking it can be only used for denoting test cases. Hence, a more generalized name, `Label`, was chosen as a replacement.

Secondly, retrieving of elements by index is no longer support. This has proven to both be confusing and error-prone in practice. It often happens that the
annotated elements are re-ordered during refactoring, causing unrelated tests to break. More fundemenetally, ordering annotated elements, especially
across multiple source files is ill-defined. 

Thirdly, to support retrieving of elements by labels, labels must now contain a mandatory and unique identifier (scoped to the test class). This makes retrieval
of elements less ambiguous (which element am I retrieving?) and hence less error-prone.

Lastly, several method names has been renamed to better reflect their purpose/less confusing between the transition from `Cases` to `Labels`. The most prominent being
`Cases.one(String)` -> `Labels.get(String)`.
