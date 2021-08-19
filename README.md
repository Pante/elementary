## Elementary is a suite of libraries that simplify creating and unit testing annotation processors.

[![CI/CD](https://github.com/Pante/elementary/workflows/Elementary%20CI/CD/badge.svg)](https://github.com/Pante/elementary/actions?query=workflow%3ACI%2FCD)
[![Codecov](https://codecov.io/gh/Pante/elementary/branch/master/graph/badge.svg)](https://codecov.io/gh/Pante/elementary)
[![Discord](https://img.shields.io/discord/140273735772012544.svg?style=flat-square)](https://discord.gg/uE4C9NQ)

**Please view the [stable branch](https://github.com/Pante/elementary/tree/stable) for a production version. Requires Java 11+. [Why Java 11?](https://github.com/Pante/elementary/wiki/FAQ#why-does-elementary-require-java-11-and-above) Read the [wiki](https://github.com/Pante/elementary/wiki) to get started.**

**Please read [The Problem With Annotation Processors](https://dzone.com/articles/the-problem-with-annotation-processors) if you're interested in what problem Elementary solves.**

#### Maven Repository
```XML
<repository>
  <id>elementary-releases</id>
  <url>https://repo.karuslabs.com/repository/elementary-releases/</url>
</repository>
```

***
#### Elementary - A suite of tools and JUnit extensions to test annotation processors and compilation
[![releases-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/elementary-releases/com/karuslabs/elementary/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/elementary-releases/com/karuslabs/elementary/)
[![snapshots-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/elementary-snapshots/com/karuslabs/elementary/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/elementary-snapshots/com/karuslabs/elementary/)
[![javadoc](https://img.shields.io/badge/javadoc-latest-brightgreen.svg)](https://repo.karuslabs.com/repository/elementary/latest/elementary/apidocs/index.html)
[![javadoc](https://img.shields.io/badge/javadoc-nightly-brightgreen.svg)](https://repo.karuslabs.com/repository/elementary/nightly/elementary/apidocs/index.html)

> _"If I have seen further that others, it is by standing on the shoulder of giants"_

This project is heavily inspired by Google's [compile-testing](https://github.com/google/compile-testing) project.

```XML
<!-- Requires JUnit 5.7.1 & above -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.7.1</version>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>com.karuslabs</groupId>
    <artifactId>elementary</artifactId>
    <version>1.1.1</version>
  <scope>test</scope>
</dependency>
```

***
#### Satisfactory - A composable `Element` matching library
[![releases-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/elementary-releases/com/karuslabs/satisfactory/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/elementary-releases/com/karuslabs/satisfactory/)
[![snapshots-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/elementary-snapshots/com/karuslabs/satisfactory/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/elementary-snapshots/com/karuslabs/satisfactory/)
[![javadoc](https://img.shields.io/badge/javadoc-latest-brightgreen.svg)](https://repo.karuslabs.com/repository/elementary/latest/satisfactory/apidocs/index.html)
[![javadoc](https://img.shields.io/badge/javadoc-nightly-brightgreen.svg)](https://repo.karuslabs.com/repository/elementary/nightly/satisfactory/apidocs/index.html)
```XML
<dependency>
    <groupId>com.karuslabs</groupId>
    <artifactId>satisfactory</artifactId>
    <version>1.1.1</version>
</dependency>
```

***
#### Utilitary - Utilities to simplify implementation of annotation processors.
[![releases-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/elementary-releases/com/karuslabs/utilitary/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/elementary-releases/com/karuslabs/utilitary/)
[![snapshots-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/elementary-snapshots/com/karuslabs/utilitary/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/elementary-snapshots/com/karuslabs/utilitary/)
[![javadoc](https://img.shields.io/badge/javadoc-latest-brightgreen.svg)](https://repo.karuslabs.com/repository/elementary/latest/utilitary/apidocs/index.html)
[![javadoc](https://img.shields.io/badge/javadoc-nightly-brightgreen.svg)](https://repo.karuslabs.com/repository/elementary/nightly/utilitary/apidocs/index.html)
```XML
<dependency>
    <groupId>com.karuslabs</groupId>
    <artifactId>satisfactory</artifactId>
    <version>1.1.1/version>
</dependency>
```
