
# OSMPBF

Osmpbf is a Java/C library to read and write OpenStreetMap PBF files.
PBF (Protocol buffer Binary Format) is a binary file format for OpenStreetMap
data that uses Google Protocol Buffers as low-level storage.

For more information see https://wiki.openstreetmap.org/wiki/PBF_Format .

Note that this is a low-level library that does only part of the
encoding/decoding needed for actually writing/reading an OSM PBF file. For
something more complete see [libosmium](https://osmcode.org/libosmium/).

[![Github Actions C Build Status](https://github.com/openstreetmap/OSM-binary/workflows/C%20CI/badge.svg?branch=master)](https://github.com/openstreetmap/OSM-binary/actions)
[![Github Actions Java Build Status](https://github.com/openstreetmap/OSM-binary/workflows/Java%20CI/badge.svg?branch=master)](https://github.com/openstreetmap/OSM-binary/actions)

## Java Version

We publish the Java library to [Maven Central](https://search.maven.org/):

```xml
<dependency>
  <groupId>org.openstreetmap.pbf</groupId>
  <artifactId>osmpbf</artifactId>
  <version>1.4.0</version>
</dependency>
```

To build the Java library run:

```sh
mvn package
```

For a Java usage example, see src.java/crosby/binary/test/ReadFileExample.java


## C Version

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


## Using the C Library

To include in your program use:

```c
#include <osmpbf/osmpbf.h>
```

and link with:

```
-pthread -lz -lprotobuf -losmpbf
```


## Debian/Ubuntu Packages

To build the Debian/Ubuntu packages:

```sh
debuild -I -us -uc
```

To install the Debian/Ubuntu packages:

```sh
sudo dpkg --install ../libosmpbf-dev_*.deb
sudo dpkg --install ../libosmpbf-java_*.deb
sudo dpkg --install ../osmpbf-bin_*.deb
```

To clean up after:
```sh
debclean
```

## License

The .proto definition files and osmpbf.h are licensed under the MIT license.
The other source code is licensed under the LGPL v3+.

