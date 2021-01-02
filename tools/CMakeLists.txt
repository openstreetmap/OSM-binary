find_package(ZLIB REQUIRED)
find_package(Threads)

include_directories("${CMAKE_SOURCE_DIR}/include")
include_directories(${CMAKE_BINARY_DIR})

add_executable(osmpbf-outline osmpbf-outline.cpp)

target_include_directories(osmpbf-outline SYSTEM PRIVATE ${ZLIB_INCLUDE_DIR})
target_link_libraries(osmpbf-outline PRIVATE osmpbf ZLIB::ZLIB protobuf::libprotobuf)

install(TARGETS osmpbf-outline RUNTIME DESTINATION bin)
install(FILES osmpbf-outline.1 DESTINATION share/man/man1)
