# Elementary - Simplify creating and testing annotation processors.

[![Elementary](https://github.com/Pante/elementary/actions/workflows/build.yaml/badge.svg)](https://github.com/Pante/elementary/actions/workflows/build.yaml)
[![Discord](https://img.shields.io/discord/140273735772012544.svg?style=flat-square)](https://discord.gg/uE4C9NQ)

To get started, check out
the [docs](https://github.com/Pante/elementary/tree/master/docs/elementary/tour.md) & [Gradle example](https://github.com/toolforger/elementary-demo).

## Elementary - JUnit extensions to test compilation & annotation processors

[![releases-maven](https://img.shields.io/maven-central/v/com.karuslabs/elementary)](https://central.sonatype.com/artifact/com.karuslabs/elementary/)
[![javadoc](https://img.shields.io/badge/javadoc-stable-brightgreen.svg)](https://repo.karuslabs.com/repository/elementary/latest/elementary/apidocs/index.html)

This project is mature. It requires Java 11.

```XML
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
    <version>2.0.1</version>
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

## Utilitary - Utilities for annotation processors.

[![releases-maven](https://img.shields.io/maven-central/v/com.karuslabs/utilitary)](https://central.sonatype.com/artifact/com.karuslabs/utilitary/)
[![javadoc](https://img.shields.io/badge/javadoc-stable-brightgreen.svg)](https://repo.karuslabs.com/repository/elementary/latest/utilitary/apidocs/index.html)

This project is mature. It requires Java 11.

```XML
<dependency>
  <groupId>com.karuslabs</groupId>
  <artifactId>utilitary</artifactId>
  <version>2.0.1</version>
</dependency>
```

## Satisfactory - Composable `Element` matching library

[![releases-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/elementary-releases/com/karuslabs/satisfactory/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/elementary-releases/com/karuslabs/satisfactory/)
[![javadoc](https://img.shields.io/badge/javadoc-stable-brightgreen.svg)](https://repo.karuslabs.com/repository/elementary/latest/satisfactory/apidocs/index.html)

This project is currently on hiatus until Java has fully fledged pattern matching.

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
