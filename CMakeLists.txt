PROJECT(OSMBinary)
cmake_minimum_required(VERSION 2.8.5)

SET(SEARCH_PREFIX CACHE PATH "Additional prefix to search libraries (Protobuf etc.)" )

SET(CMAKE_PREFIX_PATH ${CMAKE_PREFIX_PATH} ${SEARCH_PREFIX})

include(FindProtobuf)
find_package(Protobuf REQUIRED)
include_directories(${PROTOBUF_INCLUDE_DIR})

add_subdirectory(src)
if(NOT MSVC)
add_subdirectory(tools)
endif()