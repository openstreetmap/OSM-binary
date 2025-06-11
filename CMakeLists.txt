cmake_minimum_required(VERSION 3.10)

project(osmpbf VERSION 1.5.0)

include(GNUInstallDirs)

# This is needed for the protobuf_generate_cpp() function to work on newer
# versions of the Protobuf CMake config.
set(protobuf_MODULE_COMPATIBLE ON CACHE BOOL "")

# This is needed so that we pick up the (more modern) CONFIG mode cmake file
# before the (older) MODULE mode cmake config file. Seems to be needed on macOS.
set(CMAKE_FIND_PACKAGE_PREFER_CONFIG TRUE)

find_package(Protobuf REQUIRED)

add_subdirectory(osmpbf)

# The osmpbf-outline tool does not compile on Windows because getopt.h is missing
if(NOT MSVC)
    add_subdirectory(tools)
endif()
