#ifndef OSMPBF_H
#define OSMPBF_H

// this describes the low-level blob storage
#include <osmpbf/fileformat.pb.h>

// this describes the high-level OSM objects
#include <osmpbf/osmformat.pb.h>

namespace osmpbf {

    // the maximum size of a blob header in bytes
    const int max_blob_header_size = 64 * 1024;

    // the maximum size of a blob in bytes
    const int max_uncompressed_blob_size = 32 * 1024 * 1024;

}

#endif // OSMPBF_H
