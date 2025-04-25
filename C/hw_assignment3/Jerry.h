#ifndef JERRY_H
#define JERRY_H
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "Defs.h"

//constructs an object called "Planet"
struct Planet {
    char *name;
    float x;
    float y;
    float z;
};

typedef struct Planet Planet;

//constructs an object called "Origin"
struct Origin {
    char *dimension;
    Planet *home_planet;
};

typedef struct Origin Origin;

//constructs an object called "PhysicalCharacteristics"
struct PhysicalCharacteristics {
    char *characteristic_name;
    float characteristic_value;
};

typedef struct PhysicalCharacteristics PhysicalCharacteristics;

//constructs an object called "Jerry"
struct Jerry {
    char *ID;
    short happiness_level;
    Origin *origin;
    PhysicalCharacteristics **physical_characteristics_array;
    int physical_characteristics_array_size;
};

typedef struct Jerry Jerry;

//-------------- Planet Functions ------------------------------//

//creates a new instance of a planet
Planet *create_new_planet(char *name, float x, float y, float z);

//prints an instance of a planet
status print_Planet(Planet *planet);

//a function that deletes an instance of a planet
status delete_planet(Planet *Planet_instance);

//a function that compares to instances of planets
bool compare_planets(Planet *planet1, Planet *planet2);


//-------------- Jerry Functions ------------------------------//

/*
 *NOTE TO THE USER:
 *the creation functions for Origin and Physical Characteristic are part of larger creation functions, therefore they are not visible to the user
 *the deletion functions for Origin and Physical Characteristic are part of larger deletion functions, therefore they are not visible to the user
 */

//creates a new instance of a Jerry
//the creation of Jerry also creates an instance of Origin that the Jerry belongs to
Jerry *create_new_Jerry(char *ID, short happiness_level, char *dimension, Planet *planetName);

//a boolean function that returns "true" or "false" if a specific characteristic is found
bool search_Characteristic(Jerry *Jerry_instance, char *characteristic_name);

//adds a physical characteristic to an instance of a Jerry
//the function creates a new instance of a Physical Characteristic and adds it to the instance of Jerry
status add_characteristic_to_Jerry(Jerry *Jerry_instance, char *characteristic_name, float value);

//deletes a given instance of a Jerry
//the function destroys the instance of the Physical Characteristic that the Jerry had
status delete_characteristic_from_Jerry(Jerry *Jerry_instance, char *characteristic_name);

//prints an instance of a Jerry
status print_Jerry(Jerry *Jerry_instance);

//a function that deletes an instance of a Jerry
//the function also destroys the instance of the Origin that belongs to that Jerry
status delete_Jerry(Jerry *Jerry_instance);

//a function that compares between two instances of Jerry
bool compare_Jerry(Jerry *Jerry1, Jerry *Jerry2);

//a function that copies an instance of a Jerry
Jerry *copy_Jerry(Jerry *Jerry_instance);


#endif //JERRY_H
