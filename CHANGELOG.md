## Unreleased

- C: Support hardening buildflags set in the environment ([09845ca4](https://github.com/openstreetmap/OSM-binary/commit/09845ca4087c7404b1de33914233dbf53f9de4c3))
- C: Updated CMake configuration. This can now completely replace the configuration using the Makefiles. The Makefiles are deprecated now and will be removed in a future version.
- Proto: Add optional way to add node locations to way ([e7d3201](https://github.com/openstreetmap/OSM-binary/commit/e7d3201a97a86ef0c0608bfcd960e44e54962d7b))
- Proto: Add optional LZ4 and ZSTD compressed data blocks ([218bfe8](https://github.com/openstreetmap/OSM-binary/commit/218bfe8ed800173279ede2d9028fd9a5c33d12e4))

## Release notes for 1.4.0 (2020-10-20)

- License the `.proto` and header files under MIT ([5a4c8c11](https://github.com/openstreetmap/OSM-binary/commit/5a4c8c11564104afca14b787ef14131053977b5b))
- Proto: Disable deprecated `optimize_for = LITE_RUNTIME` option ([f7b77826](https://github.com/openstreetmap/OSM-binary/commit/f7b77826e493ce272daf5b1fe8b2143a818134c9))
- Proto: Specify `proto2` syntax to fix warnings with protobuf 3.0.0 ([17f2228c](https://github.com/openstreetmap/OSM-binary/commit/17f2228ca80a6477af947c4d282b99a19482fb73))
- C: Build and install a shared library ([cfea70cd](https://github.com/openstreetmap/OSM-binary/commit/cfea70cdc8f321c950f53250cd9b580043ab3266))
- C: Add support for large files ([779715f1](https://github.com/openstreetmap/OSM-binary/commit/779715f1a09f32e235d6d621b37596632d8bcba0))
- Java: Deploy Java library to Maven Central ([7a05c39e](https://github.com/openstreetmap/OSM-binary/commit/7a05c39ea5dcb8b8794e2a1b531b506ce1fae5f8))
- Java: Update Java build
- Enable GitHub CI for Java and C ([fcdf48d9](https://github.com/openstreetmap/OSM-binary/commit/fcdf48d942578af38897d4ea9f911bcdd888b3de), [2a636a31](https://github.com/openstreetmap/OSM-binary/commit/2a636a31f07d47cbb247a064bf8ec2bf9bc2bf65))

## Release notes for 1.3.3 (2014-03-15)

- Fixed ant build file.

## Release notes for 1.3.2 (2014-03-12)

- Small bugfixes and build enhancements.
- Improved Debian build in coordination with Debian maintainers.

## Release notes for 1.3.1 (2013-12-11)

- Several building fixes and enhancements.

## Release notes for 1.3.0 (2012-12-04)

- Add osmosis replication fields into the header.  
  -- Changes from Frederick Ramm <frederik@remote.org>

## Release notes for 1.2.1 (2011-10-29)

## Release notes for 1.2.0 (NOT RELEASED)

- Bad tag, so not releasable.
- Switched to the lite (non-introspective) version of protocol buffers
  for a smaller library.
- Includes support for "visible" flag on OSM objects. This allows PBF to
  handle OSM history files.
- Namespace and include conventions changes for the C++ library. Everything
  is in the OSMPBF namespace. You now do:
  #include <osmpbf/osmpbf.h>
- Added stuff needed to build Debian/Ubuntu libosmpbf-dev package containing
  the C++ library.
- Added osmpbf-outline tool that shows internals of a PBF file for debugging.
- Added magic file that can recognize OSM PBF files.

  -- Changes from Jochen Topf <jochen@topf.org>
  Peter <github@mazdermind.de>

- Added a pom.xml

  -- Changes from Zsombor Welker <flaktack@welker.hu>
