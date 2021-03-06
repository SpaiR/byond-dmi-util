[![Build Status](https://travis-ci.org/SpaiR/byond-dmi-util.svg?branch=master)](https://travis-ci.org/SpaiR/byond-dmi-util)
[![Javadocs](https://www.javadoc.io/badge/io.github.spair/byond-dmi-util.svg)](https://www.javadoc.io/doc/io.github.spair/byond-dmi-util)
[![License](http://img.shields.io/badge/license-MIT-blue.svg)](http://www.opensource.org/licenses/MIT)

# BYOND Dmi Util

## About 

Set of util classes to work with BYOND dmi files.

## Installation
[![Maven Central](https://img.shields.io/maven-central/v/io.github.spair/byond-dmi-util.svg?style=flat)](https://search.maven.org/search?q=a:byond-dmi-util)
[![JCenter](https://img.shields.io/bintray/v/spair/io.github.spair/byond-dmi-util.svg?label=jcenter)](https://bintray.com/spair/io.github.spair/byond-dmi-util/_latestVersion)

Library deployed to Maven Central and JCenter repositories.

#### pom.xml
```
<dependency>
    <groupId>io.github.spair</groupId>
    <artifactId>byond-dmi-util</artifactId>
    <version>${last.version}</version>
</dependency>
```

#### build.gradle:
```
compile 'io.github.spair:byond-dmi-util:${last.version}'
```

## How To Use

### DmiSlurper

Class used to deserialize `.dmi` file.
 - `slurpUp(final File dmiFile)` - from file.
 - `slurpUp(final String dmiName, final String base64content)` - from base64.
 - `slurpUp(final String dmiName, final InputStream input)` - from any input stream.

As a result of deserialization `Dmi` object returns.

### DmiComparator

Class used to compare two dmi's and show difference between them.
 - `compare(@Nullable final Dmi oldDmi, @Nullable final Dmi newDmi)`
 
 As a result of comparison `DmiDiff` object returns.
 
 
More could be found in [JavaDoc](https://www.javadoc.io/doc/io.github.spair/byond-dmi-util).
