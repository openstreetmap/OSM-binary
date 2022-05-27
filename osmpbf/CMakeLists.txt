protobuf_generate_cpp(CPPS HS fileformat.proto osmformat.proto)

add_library(osmpbf STATIC ${CPPS})
target_include_directories(osmpbf SYSTEM PUBLIC ${Protobuf_INCLUDE_DIRS})
install(TARGETS osmpbf ARCHIVE DESTINATION ${CMAKE_INSTALL_LIBDIR})

add_library(osmpbf_shared SHARED ${CPPS})
target_link_libraries(osmpbf_shared PRIVATE protobuf::libprotobuf)
target_include_directories(osmpbf_shared SYSTEM PUBLIC ${Protobuf_INCLUDE_DIRS})
set_target_properties(osmpbf_shared PROPERTIES OUTPUT_NAME osmpbf
                                               VERSION ${PROJECT_VERSION}
                                               SOVERSION ${PROJECT_VERSION_MAJOR})
install(TARGETS osmpbf_shared LIBRARY DESTINATION ${CMAKE_INSTALL_LIBDIR})

install(FILES ${CMAKE_SOURCE_DIR}/include/osmpbf/osmpbf.h
              ${CMAKE_BINARY_DIR}/osmpbf/osmformat.pb.h
              ${CMAKE_BINARY_DIR}/osmpbf/fileformat.pb.h
        DESTINATION ${CMAKE_INSTALL_INCLUDEDIR}/osmpbf)

