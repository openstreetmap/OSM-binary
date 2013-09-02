// used for va_list in debug-print methods
#include <stdarg.h>

// file io lib
#include <stdio.h>

// getopt is used to check for the --color-flag
#include <getopt.h>

// zlib compression is used inside the pbf blobs
#include <zlib.h>

// netinet or winsock2 provides the network-byte-order conversion function
#ifdef D_HAVE_WINSOCK
    #include <winsock2.h>
#else
    #include <netinet/in.h>
#endif

// this is the header to pbf format
#include <osmpbf/osmpbf.h>

// should the output use color?
bool usecolor = false;

// buffer for reading a compressed blob from file
char buffer[OSMPBF::max_uncompressed_blob_size];

// buffer for decompressing the blob
char unpack_buffer[OSMPBF::max_uncompressed_blob_size];

// pbf struct of a BlobHeader
OSMPBF::BlobHeader blobheader;

// pbf struct of a Blob
OSMPBF::Blob blob;

// pbf struct of an OSM HeaderBlock
OSMPBF::HeaderBlock headerblock;

// pbf struct of an OSM PrimitiveBlock
OSMPBF::PrimitiveBlock primblock;

// prints a formatted message to stdout, optionally color coded
void msg(const char* format, int color, va_list args) {
    if(usecolor) {
        fprintf(stdout, "\x1b[0;%dm", color);
    }
    vfprintf(stdout, format, args);
    if(usecolor) {
        fprintf(stdout, "\x1b[0m\n");
    } else {
        fprintf(stdout, "\n");
    }
}

// prints a formatted message to stderr, color coded to red
void err(const char* format, ...) {
    va_list args;
    va_start(args, format);
    msg(format, 31, args);
    va_end(args);
    exit(1);
}

// prints a formatted message to stderr, color coded to yellow
void warn(const char* format, ...) {
    va_list args;
    va_start(args, format);
    msg(format, 33, args);
    va_end(args);
}

// prints a formatted message to stderr, color coded to green
void info(const char* format, ...) {
    va_list args;
    va_start(args, format);
    msg(format, 32, args);
    va_end(args);
}

// prints a formatted message to stderr, color coded to white
void debug(const char* format, ...) {
    va_list args;
    va_start(args, format);
    msg(format, 37, args);
    va_end(args);
}

// application main method
int main(int argc, char *argv[]) {
    // check if the output is a tty so we can use colors

#ifdef WIN32
    usecolor = 0;
#else
    usecolor = isatty(1);
#endif

    static struct option long_options[] = {
        {"color",                no_argument, 0, 'c'},
        {0,0,0,0}
    };

    while (1) {
        int c = getopt_long(argc, argv, "c", long_options, 0);

        if (c == -1)
            break;

        switch (c) {
            case 'c':
                usecolor = true;
                break;
            default:
                exit(1);
        }
    }

    // check for proper command line args
    if(optind != argc-1)
        err("usage: %s [--color] file.osm.pbf", argv[0]);

    // open specified file
    FILE *fp = fopen(argv[optind], "rb");

    // read while the file has not reached its end
    while(!feof(fp)) {
        // storage of size, used multiple times
        int32_t sz;

        // read the first 4 bytes of the file, this is the size of the blob-header
        if(fread(&sz, sizeof(sz), 1, fp) != 1)
            break; // end of file reached

        // convert the size from network byte-order to host byte-order
        sz = ntohl(sz);

        // ensure the blob-header is smaller then MAX_BLOB_HEADER_SIZE
        if(sz > OSMPBF::max_blob_header_size)
            err("blob-header-size is bigger then allowed (%u > %u)", sz, OSMPBF::max_blob_header_size);

        // read the blob-header from the file
        if(fread(buffer, sz, 1, fp) != 1)
            err("unable to read blob-header from file");

        // parse the blob-header from the read-buffer
        if(!blobheader.ParseFromArray(buffer, sz))
            err("unable to parse blob header");

        // tell about the blob-header
        info("\nBlobHeader (%d bytes)", sz);
        debug("  type = %s", blobheader.type().c_str());

        // size of the following blob
        sz = blobheader.datasize();
        debug("  datasize = %u", sz);

        // optional indexdata
        if(blobheader.has_indexdata())
            debug("  indexdata = %u bytes", blobheader.indexdata().size());

        // ensure the blob is smaller then MAX_BLOB_SIZE
        if(sz > OSMPBF::max_uncompressed_blob_size)
            err("blob-size is bigger then allowed (%u > %u)", sz, OSMPBF::max_uncompressed_blob_size);

        // read the blob from the file
        if(fread(buffer, sz, 1, fp) != 1)
            err("unable to read blob from file");

        // parse the blob from the read-buffer
        if(!blob.ParseFromArray(buffer, sz))
            err("unable to parse blob");

        // tell about the blob-header
        info("Blob (%d bytes)", sz);

        // set when we find at least one data stream
        bool found_data = false;

        // if the blob has uncompressed data
        if(blob.has_raw()) {
            // we have at least one datastream
            found_data = true;

            // size of the blob-data
            sz = blob.raw().size();

            // check that raw_size is set correctly
            if(sz != blob.raw_size())
                warn("  reports wrong raw_size: %u bytes", blob.raw_size());

            // tell about the blob-data
            debug("  contains uncompressed data: %u bytes", sz);

            // copy the uncompressed data over to the unpack_buffer
            memcpy(unpack_buffer, buffer, sz);
        }

        // if the blob has zlib-compressed data
        if(blob.has_zlib_data()) {
            // issue a warning if there is more than one data steam, a blob may only contain one data stream
            if(found_data)
                warn("  contains several data streams");

            // we have at least one datastream
            found_data = true;

            // the size of the compressesd data
            sz = blob.zlib_data().size();

            // tell about the compressed data
            debug("  contains zlib-compressed data: %u bytes", sz);
            debug("  uncompressed size: %u bytes", blob.raw_size());

            // zlib information
            z_stream z;

            // next byte to decompress
            z.next_in   = (unsigned char*) blob.zlib_data().c_str();

            // number of bytes to decompress
            z.avail_in  = sz;

            // place of next decompressed byte
            z.next_out  = (unsigned char*) unpack_buffer;

            // space for decompressed data
            z.avail_out = blob.raw_size();

            // misc
            z.zalloc    = Z_NULL;
            z.zfree     = Z_NULL;
            z.opaque    = Z_NULL;

            if(inflateInit(&z) != Z_OK) {
                err("  failed to init zlib stream");
            }
            if(inflate(&z, Z_FINISH) != Z_STREAM_END) {
                err("  failed to inflate zlib stream");
            }
            if(inflateEnd(&z) != Z_OK) {
                err("  failed to deinit zlib stream");
            }

            // unpacked size
            sz = z.total_out;
        }

        // if the blob has lzma-compressed data
        if(blob.has_lzma_data()) {
            // issue a warning if there is more than one data steam, a blob may only contain one data stream
            if(found_data)
                warn("  contains several data streams");

            // we have at least one datastream
            found_data = true;

            // tell about the compressed data
            debug("  contains lzma-compressed data: %u bytes", blob.lzma_data().size());
            debug("  uncompressed size: %u bytes", blob.raw_size());

            // issue a warning, lzma compression is not yet supported
            err("  lzma-decompression is not supported");
        }

        // check we have at least one data-stream
        if(!found_data)
            err("  does not contain any known data stream");

        // switch between different blob-types
        if(blobheader.type() == "OSMHeader") {
            // tell about the OSMHeader blob
            info("  OSMHeader");

            // parse the HeaderBlock from the blob
            if(!headerblock.ParseFromArray(unpack_buffer, sz))
                err("unable to parse header block");

            // tell about the bbox
            if(headerblock.has_bbox()) {
                OSMPBF::HeaderBBox bbox = headerblock.bbox();
                debug("    bbox: %.7f,%.7f,%.7f,%.7f",
                    (double)bbox.left() / OSMPBF::lonlat_resolution,
                    (double)bbox.bottom() / OSMPBF::lonlat_resolution,
                    (double)bbox.right() / OSMPBF::lonlat_resolution,
                    (double)bbox.top() / OSMPBF::lonlat_resolution);
            }

            // tell about the required features
            for(int i = 0, l = headerblock.required_features_size(); i < l; i++)
                debug("    required_feature: %s", headerblock.required_features(i).c_str());

            // tell about the optional features
            for(int i = 0, l = headerblock.optional_features_size(); i < l; i++)
                debug("    required_feature: %s", headerblock.optional_features(i).c_str());

            // tell about the writing program
            if(headerblock.has_writingprogram());
                debug("    writingprogram: %s", headerblock.writingprogram().c_str());

            // tell about the source
            if(headerblock.has_source())
                debug("    source: %s", headerblock.source().c_str());
        }

        else if(blobheader.type() == "OSMData") {
            // tell about the OSMData blob
            info("  OSMData");

            // parse the PrimitiveBlock from the blob
            if(!primblock.ParseFromArray(unpack_buffer, sz))
                err("unable to parse primitive block");

            // tell about the block's meta info
            debug("    granularity: %u", primblock.granularity());
            debug("    lat_offset: %u", primblock.lat_offset());
            debug("    lon_offset: %u", primblock.lon_offset());
            debug("    date_granularity: %u", primblock.date_granularity());

            // tell about the stringtable
            debug("    stringtable: %u items", primblock.stringtable().s_size());

            // number of PrimitiveGroups
            debug("    primitivegroups: %u groups", primblock.primitivegroup_size());

            // iterate over all PrimitiveGroups
            for(int i = 0, l = primblock.primitivegroup_size(); i < l; i++) {
                // one PrimitiveGroup from the the Block
                OSMPBF::PrimitiveGroup pg = primblock.primitivegroup(i);

                bool found_items=false;

                // tell about nodes
                if(pg.nodes_size() > 0) {
                    found_items = true;

                    debug("      nodes: %d", pg.nodes_size());
                    if(pg.nodes(0).has_info())
                        debug("        with meta-info");
                }

                // tell about dense nodes
                if(pg.has_dense()) {
                    found_items = true;

                    debug("      dense nodes: %d", pg.dense().id_size());
                    if(pg.dense().has_denseinfo())
                        debug("        with meta-info");
                }

                // tell about ways
                if(pg.ways_size() > 0) {
                    found_items = true;

                    debug("      ways: %d", pg.ways_size());
                    if(pg.ways(0).has_info())
                        debug("        with meta-info");
                }

                // tell about relations
                if(pg.relations_size() > 0) {
                    found_items = true;

                    debug("      relations: %d", pg.relations_size());
                    if(pg.relations(0).has_info())
                        debug("        with meta-info");
                }

                if(!found_items)
                    warn("      contains no items");
            }
        }

        else {
            // unknown blob type
            warn("  unknown blob type: %s", blobheader.type().c_str());
        }
    }

    // close the file pointer
    fclose(fp);

    // clean up the protobuf lib
    google::protobuf::ShutdownProtobufLibrary();
}

