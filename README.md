# Elementary is a suite of libraries that simplify creating and unit testing annotation processors.

[![CI/CD](https://github.com/Pante/elementary/workflows/Elementary%20CI/CD/badge.svg)](https://github.com/Pante/elementary/actions?query=workflow%3ACI%2FCD)
[![Codecov](https://codecov.io/gh/Pante/elementary/branch/master/graph/badge.svg)](https://codecov.io/gh/Pante/elementary)
[![Discord](https://img.shields.io/discord/140273735772012544.svg?style=flat-square)](https://discord.gg/uE4C9NQ)

**Elementary and Utilitary requires Java 11+. Satisfactory requires Java 17.**

**Read the [docs](https://github.com/Pante/elementary/docs) or check out the [Gradle demo project](https://github.com/toolforger/elementary-demo) to get started!**

***
## Elementary - A suite of tools and JUnit extensions to test annotation processors and compilation
[![releases-maven](https://img.shields.io/maven-central/v/com.karuslabs/elementary)](https://central.sonatype.com/artifact/com.karuslabs/elementary/)
[![snapshots-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/elementary-snapshots/com/karuslabs/elementary/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/elementary-snapshots/com/karuslabs/elementary/)
[![javadoc](https://img.shields.io/badge/javadoc-stable-brightgreen.svg)](https://repo.karuslabs.com/repository/elementary/latest/elementary/apidocs/index.html)
[![javadoc](https://img.shields.io/badge/javadoc-nightly-brightgreen.svg)](https://repo.karuslabs.com/repository/elementary/nightly/elementary/apidocs/index.html)

Migrating from Elementary 1? See [migrating to Elementary 2](./docs/elementary/migration/migrating-to-elementary-2.md).

```XML
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

***
## Satisfactory - A composable `Element` matching library
[![releases-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/elementary-releases/com/karuslabs/satisfactory/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/elementary-releases/com/karuslabs/satisfactory/)
[![snapshots-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/elementary-snapshots/com/karuslabs/satisfactory/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/elementary-snapshots/com/karuslabs/satisfactory/)
[![javadoc](https://img.shields.io/badge/javadoc-stable-brightgreen.svg)](https://repo.karuslabs.com/repository/elementary/latest/satisfactory/apidocs/index.html)
[![javadoc](https://img.shields.io/badge/javadoc-nightly-brightgreen.svg)](https://repo.karuslabs.com/repository/elementary/nightly/satisfactory/apidocs/index.html)

Satisfactory is hosted the following repository.

```xml
<repositories>
    <repository>
        <id>elementary-releases</id>
        <url>https://repo.karuslabs.com/repository/elementary-releases/</url>
    </repository>
</repositories>
```

```XML
<dependency>
    <groupId>com.karuslabs</groupId>
    <artifactId>satisfactory</artifactId>
    <version>1.1.2</version>
</dependency>
```

***
## Utilitary - Utilities to simplify implementation of annotation processors.
[![releases-maven](https://img.shields.io/maven-central/v/com.karuslabs/utilitary)](https://central.sonatype.com/artifact/com.karuslabs/utilitary/)
[![snapshots-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/elementary-snapshots/com/karuslabs/utilitary/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/elementary-snapshots/com/karuslabs/utilitary/)
[![javadoc](https://img.shields.io/badge/javadoc-stable-brightgreen.svg)](https://repo.karuslabs.com/repository/elementary/latest/utilitary/apidocs/index.html)
[![javadoc](https://img.shields.io/badge/javadoc-nightly-brightgreen.svg)](https://repo.karuslabs.com/repository/elementary/nightly/utilitary/apidocs/index.html)
```XML
<dependency>
    <groupId>com.karuslabs</groupId>
    <artifactId>utilitary</artifactId>
    <version>2.0.0</version>
</dependency>
```
