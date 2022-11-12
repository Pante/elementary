## 2.0.0 - Next Release

This release focuses on providing facilities for creating more precise and detailed diagnostic messages.

## Elementary

This release contains breaking changes that overhaul how elements are retrieved in tests.
It also provides first-class parameterized test support and JDK9 modules support.

Please read [upgrading to Elementary 2](./upgrading-to-elementary-2.md) for more information.

- Add `Compiler.module(...)`
- Change `@Case` to `@Label`
- Change `Labels` to `Labels`
- Change `Tools.labels()` to `ToolsExtension.labels()`
- Change `Compiler`'s classpath related methods to add to the classpath rather than set the classpath
- Change `DaemonCompiler.Environment.labels` to `DaemonCompiler.Environment.labels`
- Change `DaemonCompiler.Environment.labels` to `DaemonCompiler.Environment.labels`
- Change` DaemonCompiler.of(Class<?>)` to process the given class's module if the module is named
- Remove `DaemonCompiler.of(AnnotatedConstruct)`
- Remove `DaemonCompiler.of(List<JavaFileObject>)`

## Utilitary
- Add `com.karuslabs.utilitary.snippet`
- Add `AnnotationValuePrinter`
- Add `Logger.error(Element, AnnotationMirror, Object)`
- Add `Logger.warn(Element, AnnotationMirror, Object)`
- Add `Logger.note(Element, AnnotationMirror, Object)`
- Change `Logger.error(Element, String)` to `Logger.error(Element, Object)`
- Change `Logger.warn(Element, String)` to `Logger.warn(Element, Object)`
- Change `Logger.note(Element, String)` to `Logger.note(Element, Object)`
- Change `Source` to always append a newline using `\n` instead of the system's line separator - using the system's line separator was a terrible mistake
- Change `TypeMirrors.element(TypeMirror)` to `TypeMirrors.asTypeElement(TypeMirror)`
- Change `TypePrinter.SIMPLE` to `TypePrinter.simple()`
- Change `TypePrinter.QUALIFIED` to `TypePrinter.qualified()`
- Change `Walker.specializedAncestor(TypeMirrors)` to `Walker.ancestor(TypeMirrors)`
- Fix `TypeMirrors.is(TypeMirror, Class)` always returning false on primitive types
- Fix `TypePrinter` throwing a StackOverflowError on circular type parameters
- Remove `com.karuslabs.annotations` dependency - annnotation is now compiled agaisnt JDK 16
- Remove `Names`
- Remove `Logger.error(Element, Object, String, String)`
- Remove `Logger.error(Element, Object, String)`
- Remove `Logger.warn(Element, Object, String, String)`
- Remove `Logger.warn(Element, Object, String)`
- Remove `Logger.note(Element, Object, String, String)`
- Remove `Logger.note(Element, Object, String)`
- Remove `Texts.quote(String)`
- Remove `Walker.erasuredAncestor(TypeMirrors)` - Erasured ancestors is subtlely flawed since a Collection<String> will be an ancestor of a List<Integer>

## 1.1.3 (23/10/2022)

This is a hotfix for a few issues.

## Elementary
- Change `DaemonCompiler.run(...)`'s diagnostic message to better explain java crashes
- Fix cyclic dependency between Elementary and Utilitary
- Fix `MemoryFileManager` not encoding URLs, causing modules to not compile 


## 1.1.2 (06/02/2022)

This is a hotfix for a few issues.

## Elementary
- Fix `Results` returning empty generated files


## 1.1.1 - Polish & Shine (20/06/2021)

This release focuses on fixing a few pesky issues.

## Elementary Project
- Fix source jars not being uploaded to the maven repository

## Elementary
- Change `Labels.label(String)` to `Labels.get(String)`, the inconsistency between `Labels.label(String)` and `Labels.get(int)` was driving me nuts

## Satisfactory
- Fix `EqualTimeSequence` not comparing sequences properly

## Utilitary
- Add `Walker.erasuredAncestor(TypeMirrors)`
- Change `Walker.ancestor(TypeMirrors)` to `Walker.specializedAncestor(TypeMirrors)`

## 1.1.0 - The Case for Labels (17/05/2021)

This release focuses on improving the quality of life for labels.

### Elementary
- Add `Labels.labels()`
- Change `Labels.get(String)` to `Labels.label(String)`
- Change `Labels.list()` to `Labels.all()`
- Change `@Case` annotation to use the annotated target's name if available as its label by default
- Fix missing annotations

### Satisfactory
- Fix missing annotations

### Utilitary
- Fix missing annotations

## 1.0.0 - Initial Launch! (22/04/2021)
