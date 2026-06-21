# Fooble PaperMC Utils

![MIT License](https://img.shields.io/badge/License-MIT-a6da95?style=for-the-badge&labelColor=363a4f)
![Version fpu-0.2](https://img.shields.io/badge/Version-fpu--0.2-f5a97f?style=for-the-badge&labelColor=363a4f)
![Maven Version 0.2.0](https://img.shields.io/badge/Maven-0.2.0-91d7e3?style=for-the-badge&labelColor=363a4f)

A collection of utilities and tools for my PaperMC plugins. You are free to use the Fooble PaperMC Utils in your own
projects, but please give credit where it's due.

## How to Include

### Maven

Add the following repository and dependency to your `pom.xml` file:

```xml
<repository>
    <id>fooble</id>
    <url>https://repo.fooble.dev/</url>
</repository>
```

```xml
<dependency>
    <groupId>dev.fooble.mc</groupId>
    <artifactId>fooble-paper-utils</artifactId>
    <version>0.2.0</version>
    <scope>compile</scope>
</dependency>
```

You cannot add a JAR file to your plugins directory, which means that you need to shade Fooble PaperMC Utils into your
own plugin. You can do this using the Maven Shade Plugin. You will also need to include the official PaperMC repository
in your `pom.xml` file. Please note the version of PaperMC.
