cmake_minimum_required(VERSION 3.29)
project(hw_assignment4)

set(CMAKE_CXX_STANDARD 11)


add_executable(hw_assignment4 main.cpp
        Room.cpp
        Room.h
        Game.cpp
        Game.h
        Entity.cpp
        Entity.h
        tests.cpp)
