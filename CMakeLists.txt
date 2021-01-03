cmake_minimum_required(VERSION 3.7)

project(OSMBinary)

include(GNUInstallDirs)

set(CMAKE_CXX_STANDARD 11)

find_package(Protobuf REQUIRED)

add_subdirectory(osmpbf)

# The osmpbf-outline tool does not compile on Windows because getopt.h is missing
if(NOT MSVC)
    add_subdirectory(tools)
endif()
