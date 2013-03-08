
DESTDIR ?= /usr/local
CXX ?= g++
CXXFLAGS ?= -g -I../include
LDFLAGS ?= -L../src -lpthread -lz -lprotobuf-lite -losmpbf

all: osmpbf-outline

osmpbf-outline: osmpbf-outline.cpp
	$(CXX) $(CXXFLAGS) -o $@ $< $(LDFLAGS)

install:
	install -m 755 -d $(DESTDIR)/usr/bin
	install -m 644 -s osmpbf-outline $(DESTDIR)/usr/bin

clean:
	rm -f osmpbf-outline
