## 1.2.0 - Spit Shine

- Add `Logger.error(Element, AnnotationMirror, Object)`
- Add `Logger.warn(Element, AnnotationMirror, Object)`
- Add `Logger.note(Element, AnnotationMirror, Object)`
- Add `TypeMirrors.annotation(Element, DeclaredType)`
- Add `TypeMirrors.annotations(Element, DeclaredType)`
- Change `Logger.error(Element, String)` to `Logger.error(Element, Object)`
- Change `Logger.warn(Element, String)` to `Logger.warn(Element, Object)`
- Change `Logger.note(Element, String)` to `Logger.note(Element, Object)`
- Remove `Logger.error(Element, Object, String, String)`
- Remove `Logger.error(Element, Object, String)`
- Remove `Logger.warn(Element, Object, String, String)`
- Remove `Logger.warn(Element, Object, String)`
- Remove `Logger.note(Element, Object, String, String)`
- Remove `Logger.note(Element, Object, String)`

## 1.1.1 - Polish & Shine (20/06/2021)

This release focuses on fixing a few pesky issues.

## Elementary Project
- Fix source jars not being uploaded to the maven repository

## Elementary
- Change `Cases.label(String)` to `Cases.get(String)`, the inconsistency between `Cases.label(String)` and `Cases.get(int)` was driving me nuts

## Satisfactory
- Fix `EqualTimeSequence` not comparing sequences properly

## Utilitary
- Add `Walker.erasuredAncestor(TypeMirrors)`
- Change `Walker.ancestor(TypeMirrors)` to `Walker.specializedAncestor(TypeMirrors)`

## 1.1.0 - The Case for Cases (17/05/2021)

This release focuses on improving the quality of life for cases.

### Elementary
- Add `Cases.labels()`
- Change `Cases.get(String)` to `Cases.label(String)`
- Change `Cases.list()` to `Cases.all()`
- Change `@Case` annotation to use the annotated target's name if available as its label by default
- Fix missing annotations

### Satisfactory
- Fix missing annotations

### Utilitary
- Fix missing annotations

## 1.0.0 - Initial Launch! (22/04/2021)
