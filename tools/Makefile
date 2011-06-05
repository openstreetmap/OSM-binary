
CXX = g++
CXXFLAGS = -O3 -I../include
LDFLAGS = -L../src -lpthread -lz -lprotobuf-lite -losmpbf

all: osmpbf-outline

osmpbf-outline: osmpbf-outline.cpp
	$(CXX) $(CXXFLAGS) -o $@ $< $(LDFLAGS)

install:
	install -m 755 -g root -o root -d $(DESTDIR)/usr/bin
	install -m 644 -g root -o root -s osmpbf-outline $(DESTDIR)/usr/bin

clean:
	rm -f osmpbf-outline
