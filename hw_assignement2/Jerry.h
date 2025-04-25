#ifndef JERRY_H
#define JERRY_H
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "Defs.h"

//constructs an object called "Planet"
struct Planet {
    char * name;
    float x;
    float y;
    float z;
};
typedef struct Planet Planet;

//constructs an object called "Origin"
struct Origin {
    char * dimension;
    Planet * home_planet;
};
typedef struct Origin Origin;

//constructs an object called "PhysicalCharacteristics"
struct PhysicalCharacteristics {
    char * characteristic_name;
    float characteristic_value;
};
typedef struct PhysicalCharacteristics PhysicalCharacteristics;

//constructs an object called "Jerry"
struct Jerry {
    char * ID;
    short happiness_level;
    Origin * origin;
    PhysicalCharacteristics **physical_characteristics_array;
    int physical_characteristics_array_size;
};
typedef struct Jerry Jerry;

//creates a new instance of a planet
Planet * create_new_planet(char * name, float x, float y, float z);

//creates a new instance of a Jerry
Jerry *create_new_Jerry(char *ID, short happiness_level, char * dimension, Planet * planetName);

//a boolean function that returns "true" or "false" if a specific characteristic is found
bool search_Characteristic(Jerry *Jerry_instance, char *characteristic_name);

//adds a physical characteristic to an instance of a Jerry
status add_characteristic_to_Jerry(Jerry *Jerry_instance, char *characteristic_name, float value);

//deletes a given instance of a Jerry
status delete_characteristic_from_Jerry(Jerry *Jerry_instance, char *characteristic_name);

//prints an instance of a Jerry
status print_Jerry(Jerry *Jerry_instance);

//prints an instance of a planet
status print_Planet(Planet * planet);

status delete_Jerry(Jerry *Jerry_instance);

status delete_planet(Planet *Planet_instance);


#endif //JERRY_H

