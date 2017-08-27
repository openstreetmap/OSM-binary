PROTOBUF_GENERATE_CPP(CPPS HS fileformat.proto osmformat.proto)

if(MSVC)
    set(CMAKE_CXX_FLAGS "/O3")
else()
    set(CMAKE_CXX_FLAGS "-O3")
endif()

add_library(osmpbf STATIC ${CPPS})

set_target_properties(osmpbf PROPERTIES LINKER_LANGUAGE CXX LANGUAGE CXX)

ADD_CUSTOM_COMMAND(TARGET osmpbf POST_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy ${CMAKE_BINARY_DIR}/src/fileformat.pb.h ${CMAKE_SOURCE_DIR}/include/osmpbf
    COMMAND ${CMAKE_COMMAND} -E copy ${CMAKE_BINARY_DIR}/src/osmformat.pb.h ${CMAKE_SOURCE_DIR}/include/osmpbf
    BYPRODUCTS
        ${CMAKE_SOURCE_DIR}/include/osmpbf/fileformat.pb.h
        ${CMAKE_SOURCE_DIR}/include/osmpbf/osmformat.pb.h
)

SET(INCLUDE_INSTALL "include/osmpbf")
SET(files ${CMAKE_SOURCE_DIR}/include/osmpbf/osmpbf.h ${CMAKE_BINARY_DIR}/src/osmformat.pb.h ${CMAKE_BINARY_DIR}/src/fileformat.pb.h)
INSTALL(FILES ${files} DESTINATION ${INCLUDE_INSTALL})
install(TARGETS osmpbf LIBRARY DESTINATION lib ARCHIVE DESTINATION lib)
