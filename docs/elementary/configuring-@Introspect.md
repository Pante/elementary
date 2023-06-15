The `@Introspect` annotation allows a test file to inspect its own `Element` and `TypeMirror` representation inside tests. 
This meant that test cases can be declared in the same file as a test suite.

Unfortunately, to achieve this, the test sources need to be copied from the original folder to the output folder. This can 
usually be achieved by adjusting the build tool's configuration. Below, we provide examples for `Maven` and `Gradle`.

## Maven
In the `<build/>` section of a project's pom.xml:
```xml
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

## Gradle
In the project's build.gradle:
```groovy
task copyTestSources(type: Copy) {
    from file("${project.projectDir}/src/test/java/")
    into file("${project.projectDir}/build/classes/java/test/")
}

test {
    dependsOn 'copyTestSources'
}
```