
CXX ?= g++
CXXFLAGS ?= -O3
AR ?= ar
PROTOC ?= protoc

all: libosmpbf.a ../include/osmpbf/fileformat.pb.h ../include/osmpbf/osmformat.pb.h

libosmpbf.a: fileformat.pb.o osmformat.pb.o
	$(AR) -cr $@ fileformat.pb.o osmformat.pb.o

%.pb.o: %.pb.cc
	$(CXX) $(CXXFLAGS) -c -o $@ $<

%.pb.cc ../include/osmpbf/%.pb.h: %.proto
	$(PROTOC) --proto_path=. --cpp_out=. $<
	cp *.pb.h ../include/osmpbf/

install:
	install -m 755 -g root -o root -d $(DESTDIR)/usr/lib
	install -m 644 -g root -o root libosmpbf.a $(DESTDIR)/usr/lib
	install -m 755 -g root -o root -d $(DESTDIR)/usr/include/osmpbf
	install -m 644 -g root -o root ../include/osmpbf/osmpbf.h $(DESTDIR)/usr/include/osmpbf
	install -m 644 -g root -o root ../include/osmpbf/fileformat.pb.h $(DESTDIR)/usr/include/osmpbf
	install -m 644 -g root -o root ../include/osmpbf/osmformat.pb.h $(DESTDIR)/usr/include/osmpbf

clean:
	rm -f *.pb.h *.pb.cc *.pb.o libosmpbf.a

