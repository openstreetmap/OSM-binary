PROTOBUF_GENERATE_CPP(CPPS HS fileformat.proto osmformat.proto)

if(MSVC)
    set(CMAKE_CXX_FLAGS "/O3")        
else()
    set(CMAKE_CXX_FLAGS "-O3")        
endif()

add_library(osmpbf STATIC ${CPPS})

set_target_properties(osmpbf PROPERTIES LINKER_LANGUAGE CXX LANGUAGE CXX)

SET(INCLUDE_INSTALL "include/osmpbf")
FILE(GLOB files "${CMAKE_CURRENT_SOURCE_DIR}/*.h")
INSTALL(FILES ${files} DESTINATION ${INCLUDE_INSTALL})
INSTALL(FILES "${CMAKE_CURRENT_SOURCE_DIR}/../include/osmpbf/osmpbf.h" DESTINATION ${INCLUDE_INSTALL})
install(TARGETS osmpbf LIBRARY DESTINATION lib ARCHIVE DESTINATION lib)
