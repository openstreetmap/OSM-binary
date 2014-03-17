
# these default settings can be overridden by setting environment variables
PREFIX   ?= /usr/local
CXX      ?= g++
CXXFLAGS ?= -g

CXXFLAGS += -I../include -D_LARGEFILE_SOURCE -D_FILE_OFFSET_BITS=64 -Wall -Wextra -pedantic -Wredundant-decls -Wdisabled-optimization -Wctor-dtor-privacy -Wnon-virtual-dtor -Woverloaded-virtual -Wsign-promo -Wno-long-long
LDFLAGS  += -L../src -pthread -lz -lprotobuf-lite -losmpbf

.PHONY: clean install

all: osmpbf-outline

osmpbf-outline: osmpbf-outline.cpp
	$(CXX) $(CXXFLAGS) -o $@ $< $(LDFLAGS)

install:
	install -m 755 -d $(DESTDIR)$(PREFIX)/bin
	install -m 755 -s osmpbf-outline $(DESTDIR)$(PREFIX)/bin

clean:
	rm -f osmpbf-outline

