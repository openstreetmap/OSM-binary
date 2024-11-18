#ifndef OSMPBF_H
#define OSMPBF_H
/*
 Copyright 2011-2014 Jochen Topf <jochen@topf.org>,
 Copyrigt 2012 Scott A. Crosby. <scott@sacrosby.com>

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 */

// this describes the low-level blob storage
#include <osmpbf/fileformat.pb.h> // IWYU pragma: export

// this describes the high-level OSM objects
#include <osmpbf/osmformat.pb.h> // IWYU pragma: export

#define OSMPBF_VERSION "1.6.0"

namespace OSMPBF {

    // the maximum size of a blob header in bytes
    const int max_blob_header_size = 64 * 1024; // 64 kB

    // the maximum size of an uncompressed blob in bytes
    const int max_uncompressed_blob_size = 32 * 1024 * 1024; // 32 MB

    // resolution for longitude/latitude used for conversion
    // between representation as double and as int
    const int lonlat_resolution = 1000 * 1000 * 1000; 

}

#endif // OSMPBF_H
