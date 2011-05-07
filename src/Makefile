
CXX = g++
CXXFLAGS = -O3
AR = ar

all: libosmpbf.a fileformat.pb.h osmformat.pb.h

libosmpbf.a: fileformat.pb.o osmformat.pb.o
	$(AR) -cr $@ fileformat.pb.o osmformat.pb.o

%.pb.o: %.pb.cc
	$(CXX) $(CXXFLAGS) -c -o $@ $<

%.pb.cc %.pb.h: %.proto
	protoc --proto_path=. --cpp_out=. $<

install:
	install -m 755 -g root -o root -d $(DESTDIR)/usr/lib
	install -m 644 -g root -o root libosmpbf.a $(DESTDIR)/usr/lib
	install -m 755 -g root -o root -d $(DESTDIR)/usr/include/osmpbf
	install -m 644 -g root -o root fileformat.pb.h osmformat.pb.h $(DESTDIR)/usr/include/osmpbf

clean:
	rm -f *.pb.h *.pb.cc *.pb.o libosmpbf.a

