cmake_minimum_required(VERSION 3.10)

project(hunspell-lib)

# Collect all Hunspell source files
file(GLOB HUNSPELL_SRC
    hunspell/*.cxx
)

add_library(
    hunspell-lib
    SHARED
    ${HUNSPELL_SRC}
    hunspell_jni.cpp
)

target_include_directories(hunspell-lib PRIVATE
    hunspell
)

target_link_libraries(
    hunspell-lib
    log
)