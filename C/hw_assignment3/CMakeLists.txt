cmake_minimum_required(VERSION 3.29)
project(hw_assignment3 C)

set(CMAKE_C_STANDARD 11)

add_executable(hw_assignment3 JerryBoreeMain.c
        LinkedList.c
        LinkedList.h
        KeyValuePair.c
        KeyValuePair.h
        MultiValueHashTable.c
        MultiValueHashTable.h
        HashTable.c
        JerryBoreeMain.c
        Jerry.h
        Jerry.c
        HashTable.h)
