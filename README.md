**Elementary is mono-repository that includes a JUnit extension library for testing annotation processors. Instead of mocking/re-implementing [`javax.lang.model`](https://docs.oracle.com/javase/8/docs/api/javax/lang/model/package-summary.html), tests are excuted inside a _real_ javac environment.**

**Please view the [stable branch](https://github.com/Pante/elementary/tree/stable) for a production version. Requires Java 11+.**

**Read the [wiki](https://github.com/Pante/elementary/wiki) to get started.**

[![CI/CD](https://github.com/Pante/elementary/workflows/Elementary%20CI/CD/badge.svg)](https://github.com/Pante/elementary/actions?query=workflow%3ACI%2FCD)
[![Funding](https://img.shields.io/badge/%F0%9F%A4%8D%20-sponsorship-ff69b4?style=flat-square)](https://github.com/sponsors/Pante)
[![Codecov](https://codecov.io/gh/Pante/elementary/branch/master/graph/badge.svg)](https://codecov.io/gh/Pante/elementary)
[![Stable Source Code](https://img.shields.io/badge/stable-branch-blue.svg)](https://github.com/Pante/elementart/tree/stable)
[![Discord](https://img.shields.io/discord/140273735772012544.svg?style=flat-square)](https://discord.gg/uE4C9NQ)

>> _"If I have seen further that others, it is by standing on the shoulder of giants"_

This project is heavily inspired by Google's own [compile-testing](https://github.com/google/compile-testing) project.

#### Maven Repository
```XML
<repository>
  <id>chimera-releases</id>
  <url>https://repo.karuslabs.com/repository/elementary-releases/</url>
</repository>
```

***
#### Satisfactory - A element and type mirror matching library
[![releases-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/elementary-releases/com/karuslabs/satisfactory/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/elementary-releases/com/karuslabs/satisfactory/)
[![snapshots-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/elementary-snapshots/com/karuslabs/satisfactory/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/elementary-snapshots/com/karuslabs/satisfactory/)
[![javadoc](https://img.shields.io/badge/javadoc-1.0.0-brightgreen.svg)](https://repo.karuslabs.com/repository/elementary/1.0.0/satisfactory/apidocs/index.html)
```XML
<dependency>
    <groupId>com.karuslabs</groupId>
    <artifactId>satisfactory</artifactId>
    <version>1.0.0</version>
</dependency>
```
