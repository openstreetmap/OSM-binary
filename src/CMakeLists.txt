PROTOBUF_GENERATE_CPP(CPPS HS fileformat.proto osmformat.proto)

if(MSVC)
    set(CMAKE_CXX_FLAGS "/O3")
else()
    set(CMAKE_CXX_FLAGS "-O3")
endif()

add_library(osmpbf STATIC ${CPPS})

set_target_properties(osmpbf PROPERTIES LINKER_LANGUAGE CXX LANGUAGE CXX)

ADD_CUSTOM_COMMAND(TARGET osmpbf POST_BUILD  COMMAND ${CMAKE_COMMAND} -E copy ${CMAKE_BINARY_DIR}/src/fileformat.pb.h ${CMAKE_SOURCE_DIR}/include/osmpbf)
ADD_CUSTOM_COMMAND(TARGET osmpbf POST_BUILD  COMMAND ${CMAKE_COMMAND} -E copy ${CMAKE_BINARY_DIR}/src/osmformat.pb.h ${CMAKE_SOURCE_DIR}/include/osmpbf)

SET(INCLUDE_INSTALL "include/osmpbf")
SET(files ${CMAKE_SOURCE_DIR}/include/osmpbf/osmpbf.h ${CMAKE_BINARY_DIR}/src/osmformat.pb.h ${CMAKE_BINARY_DIR}/src/fileformat.pb.h)
INSTALL(FILES ${files} DESTINATION ${INCLUDE_INSTALL})
install(TARGETS osmpbf LIBRARY DESTINATION lib ARCHIVE DESTINATION lib)

configure_file("OsmpbfConfig.cmake.in" "OsmpbfConfig.cmake" @ONLY)
install(FILES "${CMAKE_CURRENT_BINARY_DIR}/OsmpbfConfig.cmake" DESTINATION ${CMAKE_INSTALL_PREFIX}/lib/cmake/Osmpbf/)

configure_file("osmpbf.pc.in" "osmpbf.pc" @ONLY)
install(FILES "${CMAKE_CURRENT_BINARY_DIR}/osmpbf.pc" DESTINATION ${CMAKE_INSTALL_PREFIX}/lib/pkgconfig/)
