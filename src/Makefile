
PREFIX ?= /usr/local
CXX ?= g++
CXXFLAGS ?= -O3

AR ?= ar
PROTOC ?= protoc

.PHONY: clean install

all: libosmpbf.a ../include/osmpbf/fileformat.pb.h ../include/osmpbf/osmformat.pb.h

libosmpbf.a: fileformat.pb.o osmformat.pb.o
	$(AR) -cr $@ fileformat.pb.o osmformat.pb.o

%.pb.o: %.pb.cc
	$(CXX) $(CXXFLAGS) -fPIC -c -o $@ $<

%.pb.cc ../include/osmpbf/%.pb.h: %.proto
	$(PROTOC) --proto_path=. --cpp_out=. $<
	cp *.pb.h ../include/osmpbf/

install:
	install -m 755 -d $(DESTDIR)$(PREFIX)/lib
	install -m 644 libosmpbf.a $(DESTDIR)$(PREFIX)/lib
	install -m 755 -d $(DESTDIR)$(PREFIX)/include/osmpbf
	install -m 644 ../include/osmpbf/osmpbf.h $(DESTDIR)$(PREFIX)/include/osmpbf
	install -m 644 ../include/osmpbf/fileformat.pb.h $(DESTDIR)$(PREFIX)/include/osmpbf
	install -m 644 ../include/osmpbf/osmformat.pb.h $(DESTDIR)$(PREFIX)/include/osmpbf

clean:
	rm -f *.pb.h *.pb.cc *.pb.o libosmpbf.a

