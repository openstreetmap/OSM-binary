
# OSMPBF

https://github.com/openstreetmap/OSM-binary

Osmpbf is a Java/C library to read and write OpenStreetMap PBF files.
PBF (Protocol buffer Binary Format) is a binary file format for OpenStreetMap
data that uses Google Protocol Buffers as low-level storage.

For more information see https://wiki.openstreetmap.org/wiki/PBF_Format .

Note that this is a low-level library that does only part of the
encoding/decoding needed for actually writing/reading an OSM PBF file. For
something more complete see [libosmium](https://osmcode.org/libosmium/).

[![Github Actions C Build Status](https://github.com/openstreetmap/OSM-binary/workflows/C%20CI/badge.svg?branch=master)](https://github.com/openstreetmap/OSM-binary/actions)
[![Github Actions Java Build Status](https://github.com/openstreetmap/OSM-binary/workflows/Java%20CI/badge.svg?branch=master)](https://github.com/openstreetmap/OSM-binary/actions)
[![Packaging status](https://repology.org/badge/tiny-repos/libosmpbf.svg)](https://repology.org/project/libosmpbf/versions)

## Java Version

### Building with Maven

We publish the Java library to [Maven Central](https://search.maven.org/):

```xml
<dependency>
  <groupId>org.openstreetmap.pbf</groupId>
  <artifactId>osmpbf</artifactId>
  <version>1.5.0</version>
</dependency>
```

To build the Java library run:

```sh
mvn package
```

For a Java usage example, see
[`ReadFileTest`](https://github.com/openstreetmap/OSM-binary/blob/master/test.java/crosby/binary/ReadFileTest.java).

### Building with Ant

If you can not use Maven for some reason you can use the
[Ant](https://ant.apache.org/) instead:

```sh
ant
```

This will build `osmpbf.jar` in the main directory.

This build is also used for Debian packaging.

## C++ Version

(Earlier versions used Makefiles for building. Please switch to the CMake-based
build, the Makefiles are deprecated and will be removed in a future version.)

To compile:

```sh
mkdir build && cd build
cmake ..
make
```

To install:

```sh
make install
```

There is a tool named osmpbf-outline that shows a debug output of the contents
of a PBF file. To run it:

```sh
tools/osmpbf-outline osm-file.osm.pbf
```


## Using the C++ Library

To include in your program use:

```c
#include <osmpbf/osmpbf.h>
```

and link with:

```
-pthread -lz -lprotobuf -losmpbf
```


## License

The .proto definition files and osmpbf.h are licensed under the MIT license.
The other source code is licensed under the LGPL v3+.

