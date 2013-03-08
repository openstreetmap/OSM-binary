
DESTDIR ?= /usr/local
CXX ?= g++
CXXFLAGS ?= -g -I../include
LDFLAGS ?= -L../src -lpthread -lz -lprotobuf-lite -losmpbf

all: osmpbf-outline

osmpbf-outline: osmpbf-outline.cpp
	$(CXX) $(CXXFLAGS) -o $@ $< $(LDFLAGS)

install:
	install -m 755 -g root -o root -d $(DESTDIR)/bin
	install -m 644 -g root -o root -s osmpbf-outline $(DESTDIR)/bin

clean:
	rm -f osmpbf-outline
