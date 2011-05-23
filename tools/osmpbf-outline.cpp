#include <stdarg.h>
#include <stdio.h>
#include <zlib.h>
#include <netinet/in.h>
#include <osmpbf/fileformat.pb.h>
#include <osmpbf/osmformat.pb.h>

bool is_a_tty;

const int MAX_BLOB_HEADER_SIZE = 64 * 1024;
const int MAX_BLOB_SIZE = 32 * 1024 * 1024;

char buffer[MAX_BLOB_SIZE];
char unpack_buffer[MAX_BLOB_SIZE];

OSMPBF::BlobHeader blobheader;
OSMPBF::Blob blob;

bool flag = false;


void msg(const char* format, int color, va_list args) {
    if(is_a_tty) fprintf(stderr, "\x1b[0;%dm", color);

    vfprintf(stderr, format, args);

    if(is_a_tty) fprintf(stderr, "\x1b[0m\n");
    else fprintf(stderr, "\n");
}

void err(const char* format, ...) {
    va_list args;
    va_start(args, format);
    msg(format, 31, args);
    va_end(args);
    exit(1);
}

void warn(const char* format, ...) {
    va_list args;
    va_start(args, format);
    msg(format, 33, args);
    va_end(args);
}

void info(const char* format, ...) {
    va_list args;
    va_start(args, format);
    msg(format, 37, args);
    va_end(args);
}


int main(int argc, char *argv[]) {
    if(isatty(2)) {
        is_a_tty = true;
    }

    if(argc != 2)
        err("usage: %s file.osm.pbf", argv[0]);

    FILE *fp = fopen(argv[1], "r");
    while(!feof(fp)) {
        int32_t sz;
        if(fread(&sz, sizeof(sz), 1, fp) != 1)
            break; // EOF

        sz = ntohl(sz);
        if(sz > MAX_BLOB_HEADER_SIZE)
            err("blob-header-size is bigger then allowed (%u > %u)", sz, MAX_BLOB_HEADER_SIZE);

        if(fread(buffer, sz, 1, fp) != 1)
            err("unable to read blob-header from file");

        blobheader.ParseFromArray(buffer, sz);

        info("BlobHeader (%d bytes)", sz);
        info(" type = %s", blobheader.type().c_str());
        sz = blobheader.datasize();
        info(" datasize = %u", sz);
        if(blobheader.has_indexdata())
            info(" indexdata = %u bytes", blobheader.indexdata().size());
        else
            info(" no indexdata");

        if(sz > MAX_BLOB_SIZE)
            err("blob-size is bigger then allowed (%u > %u)", sz, MAX_BLOB_SIZE);

        if(fread(buffer, sz, 1, fp) != 1)
            err("unable to read blob from file");

        blob.ParseFromArray(buffer, sz);

        info("Blob (%d bytes)", sz);

        if(blob.has_raw()) {
            flag = true;
            info(" contains uncompressed data: %u bytes", blob.raw().size());
            if(blob.raw().size() != blob.raw_size())
                warn(" reports wrong raw_size: %u bytes", blob.raw_size());
        }

        if(blob.has_zlib_data()) {
            if(flag)
                warn(" contains raw- and zlib-data at the same time");

            flag = true;
            info(" contains zlib-compressed data: %u bytes", blob.zlib_data().size());
            info(" uncompressed size: %u bytes", blob.raw_size());
        }

        if(blob.has_lzma_data()) {
            if(flag)
                warn(" contains raw- and lzma-data at the same time");

            flag = true;
            info(" contains lzma-compressed data: %u bytes", blob.lzma_data().size());
            info(" uncompressed size: %u bytes", blob.raw_size());

            err(" lzma-decompression is not supported");
        }

        if(!flag)
            err(" does not contain any known data stream");

        flag = false;
    }

    fclose(fp);
    google::protobuf::ShutdownProtobufLibrary();
}
