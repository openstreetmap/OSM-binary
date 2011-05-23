#include <stdarg.h>
#include <stdio.h>
#include <zlib.h>
#include <osmpbf/fileformat.pb.h>
#include <osmpbf/osmformat.pb.h>

bool is_a_tty;

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


    fclose(fp);
    google::protobuf::ShutdownProtobufLibrary();
}
