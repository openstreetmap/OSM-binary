
# OSMPBF

Osmpbf is a Java/C library to read and write OpenStreetMap PBF files.
PBF (Protocol buffer Binary Format) is a binary file format for OpenStreetMap
data that uses Google Protocol Buffers as low-level storage.

For more information see https://wiki.openstreetmap.org/wiki/PBF_Format .


## Java Version

To build the osmpbf.jar file run:

```sh
ant
```

For a Java usage example, see src.java/crosby/binary/test/ReadFileExample.java


## C Version

To compile:

```sh
make -C src
```

To install:

```sh
make -C src install
```

There is a tool named osmpbf-outline that shows a debug output of the contents
of a PBF file. To compile it:

```sh
  make -C tools
```


## Using the C Library

To include in your program use:

```c
#include <osmpbf/osmpbf.h>
```

and link with:

```
-pthread -lz -lprotobuf-lite -losmpbf
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

