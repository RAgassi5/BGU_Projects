cmake_minimum_required(VERSION 3.30)
project(hw_assignment5)

set(CMAKE_CXX_STANDARD 11)

add_executable(hw_assignment5 main.cpp
        Game.cpp
        Game.h
        Entity.cpp
        Entity.h
        Room.h
        Room.cpp
        Fighter.cpp
        Fighter.h
        Sorcerer.cpp
        Sorcerer.h
        Player.cpp
        Player.h
        Monster.cpp
        Monster.h
        Dragon.cpp
        Dragon.h
        Goblin.cpp
        Goblin.h
        GameException.h)
