SET(PROJECT osmpbf-outline)
PROJECT(${PROJECT})

if(MSVC)
    set(CMAKE_CXX_FLAGS "/O3")
else()
    set(CMAKE_CXX_FLAGS "-O3 -D_LARGEFILE_SOURCE -D_FILE_OFFSET_BITS=64")
endif()

SET(CPP_SRC osmpbf-outline.cpp)
SET(HPP_SRC "")
SET(C_SRC "")
include_directories("${CMAKE_SOURCE_DIR}/include")

add_executable(${PROJECT} ${HPP_SRC} ${CPP_SRC} ${C_SRC})
set_target_properties(${PROJECT} PROPERTIES LINKER_LANGUAGE CXX LANGUAGE CXX)

find_package(ZLIB REQUIRED)
include_directories(${ZLIB_INCLUDE_DIR})

INCLUDE (CheckIncludeFiles)
CHECK_INCLUDE_FILES(winsock2.h HAVE_WINSOCK)
if(HAVE_WINSOCK)
   add_definitions( -DD_HAVE_WINSOCK )
   SET(WINSOCK_LIB ws2_32)
endif()


find_package(Threads)
target_link_libraries(${PROJECT} ${WINSOCK_LIB} ${ZLIB_LIBRARY}  osmpbf ${PROTOBUF_LITE_LIBRARIES})

INSTALL(TARGETS ${PROJECT} RUNTIME DESTINATION bin)
