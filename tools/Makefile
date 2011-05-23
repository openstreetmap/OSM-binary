
CXX = g++
CXXFLAGS = -O3
LDFLAGS = -lpthread
LIB_PROTOBUF = -lz -lprotobuf-lite -losmpbf

all: osmpbf-outline

osmpbf-outline: osmpbf-outline.cpp
	$(CXX) $(CXXFLAGS) -o $@ $< $(LDFLAGS) $(LIB_PROTOBUF)

install:
	install -m 755 -g root -o root -d $(DESTDIR)/usr/bin
	install -m 644 -g root -o root osmpbf-outline $(DESTDIR)/usr/bin

clean:
	rm -f osmpbf-outline
