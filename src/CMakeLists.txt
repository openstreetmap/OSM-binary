protobuf_generate_cpp(CPPS HS fileformat.proto osmformat.proto)

add_library(osmpbf STATIC ${CPPS})
target_include_directories(osmpbf SYSTEM PUBLIC ${Protobuf_INCLUDE_DIRS})

install(FILES ${CMAKE_SOURCE_DIR}/include/osmpbf/osmpbf.h
              ${CMAKE_BINARY_DIR}/osmpbf/osmformat.pb.h
              ${CMAKE_BINARY_DIR}/osmpbf/fileformat.pb.h
        DESTINATION "include/osmpbf")

install(TARGETS osmpbf ARCHIVE DESTINATION lib)

