# cmake_minimum_required(VERSION <specify CMake version here>)
project(Project_3)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES main.c)
add_executable(Project_3 ${SOURCE_FILES})