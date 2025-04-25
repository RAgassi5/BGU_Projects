#include <ctype.h>
#include "Jerry.h"

/*
 *a function that receives the configuration file and loads all the data into the menu in the Main
 */
status read_configFile(int number_of_planets, int number_of_jerries, char *file_path, Planet **planet_array,
                       Jerry **jerry_array) {
    //if nothing is loaded into the menu - work as an empty menu
    if (number_of_planets <= 0 || number_of_jerries <= 0 || file_path == NULL) {
        return success;
    }

    FILE *config_File = fopen(file_path, "r"); //open the configuration file
    if (config_File == NULL) {
        return failure;
    }
    char current_line[300];
    while (fgets(current_line, sizeof(current_line), config_File)) {
        current_line[strcspn(current_line, "\n")] = 0;
        //read from the file until we get to "planets", then we start entering data into the main
        if (strcmp(current_line, "Planets") == 0) {
            int i = 0;
            fgets(current_line, sizeof(current_line), config_File);
            current_line[strcspn(current_line, "\n")] = 0;
            while (strcmp(current_line, "Jerries") != 0) {
                char *token = strtok(current_line, ",");
                char *name_token = token;
                token = strtok(NULL, ",");
                float x = atof(token);
                token = strtok(NULL, ",");
                float y = atof(token);
                token = strtok(NULL, "\n");
                float z = atof(token);

                Planet *new_planet_instance = create_new_planet(name_token, x, y, z);
                if (new_planet_instance != NULL) {
                    planet_array[i] = new_planet_instance;
                    i++;
                }
                fgets(current_line, sizeof(current_line), config_File);
                current_line[strcspn(current_line, "\n")] = 0;
            }
        }
        //if the current line is "Jerries" - it means we finished loading the all the planets into the menu
        if (strcmp(current_line, "Jerries") == 0) {
            int j = 0;
            //start going over the given data and load all data for Jerry instances
            while (fgets(current_line, sizeof(current_line), config_File)) {
                int tab_char_location = strcspn(current_line, "\t");
                //if a row starts with a tab - means we are loading data for a characteristic
                if (tab_char_location == 0) {
                    char *no_tab_token = strtok(current_line, "\t");
                    char *token = strtok(no_tab_token, ":");
                    char *characteristic_token = token;
                    token = strtok(NULL, "\n");
                    float value = atof(token);

                    if (add_characteristic_to_Jerry(jerry_array[j - 1], characteristic_token, value) == failure) {
                        return failure;
                    }
                }
                // if we are not loading characteristics, we are loading data for the Jerry instance
                else {
                    char *token = strtok(current_line, ",");
                    char *ID_token = token;
                    token = strtok(NULL, ",");
                    char *dimension_token = token;
                    token = strtok(NULL, ",");
                    char *planet_name_token = token;
                    token = strtok(NULL, "\n");
                    double happiness_token = atof(token);

                    for (int k = 0; k < number_of_planets; k++) {
                        if (strcmp(planet_array[k]->name, planet_name_token) == 0) {
                            Jerry *new_instance = create_new_Jerry(ID_token, happiness_token, dimension_token,
                                                                   planet_array[k]);
                            if (new_instance == NULL) {
                                return failure;
                            }

                            jerry_array[j] = new_instance;
                            j++;
                            break;
                        }
                    }
                }
            }
        }
    }
    fclose(config_File); //make sure to close the file we got our data from
    return success;
}

/*
 *a function that prints all Jerry instances in a given array of Jerries
 */
status print_all_Jerries(int number_of_jerries, Jerry **Jerry_Array) {
    if (Jerry_Array == NULL || number_of_jerries <= 0) {
        return failure;
    }

    for (int i = 0; i < number_of_jerries; i++) {
        print_Jerry(Jerry_Array[i]);
    }
    return success;
}

/*
 *a function that prints out all Planet instances in a given array of planets
 */
status print_all_planets(int number_of_planets, Planet **planet_array) {
    if (planet_array == NULL || number_of_planets <= 0) {
        return failure;
    }
    for (int i = 0; i < number_of_planets; i++) {
        print_Planet(planet_array[i]);
    }
    return success;
}

/*
 *a function that transfers all letters of a string to upper case
 */
void change_to_UpperCase(char *string_to_convert) {
    for (int i = 0; i < strlen(string_to_convert); i++) {
        string_to_convert[i] = toupper(string_to_convert[i]);
    }
}

/*
 *a function that adds a physical characteristic to a given instance of a Jerry
 */
status add_PC(Jerry **Jerry_Array, int jerry_array_size, char *input) {
    if (Jerry_Array == NULL || input == NULL) {
        return failure;
    }
    for (int i = 0; i < jerry_array_size; i++) {
        if (strcmp(Jerry_Array[i]->ID, input) == 0) {
            printf("What physical characteristic can you add to Jerry - %s ? \n", input);
            char PC_name[300];
            float pc_value;
            scanf("%s", PC_name);
            for (int j = 0; j < Jerry_Array[i]->physical_characteristics_array_size; j++) {
                if (strcmp(Jerry_Array[i]->physical_characteristics_array[j]->characteristic_name, PC_name) == 0) {
                    change_to_UpperCase(PC_name);
                    printf("RICK I ALREADY KNOW HIS %s ! \n", PC_name);
                    return success;
                }
            }
            printf("What is the value of his %s ? \n", PC_name);
            scanf("%f", &pc_value);
            if (add_characteristic_to_Jerry(Jerry_Array[i], PC_name, pc_value) == failure) {
                return failure;
            }
            print_Jerry(Jerry_Array[i]);
            return success;
        }
    }
    printf("OH NO! I CAN'T FIND HIM RICK ! \n");
    return success;
}

/*
 *a function that deletes a given physical characteristic out of a given instance of Jerry
 */
status delete_PC(Jerry **Jerry_Array, int jerry_array_size, char *input) {
    if (Jerry_Array == NULL || input == NULL) {
        return failure;
    }
    for (int i = 0; i < jerry_array_size; i++) {
        if (strcmp(Jerry_Array[i]->ID, input) == 0) {
            printf("What physical characteristic do you want to remove from Jerry - %s ? \n", input);
            char PC_name[300];
            scanf("%s", PC_name);
            for (int j = 0; j < Jerry_Array[i]->physical_characteristics_array_size; j++) {
                if (strcmp(Jerry_Array[i]->physical_characteristics_array[j]->characteristic_name, PC_name) == 0) {
                    if (delete_characteristic_from_Jerry(Jerry_Array[i], PC_name) == success) {
                        print_Jerry(Jerry_Array[i]);
                        return success;
                    }
                }
            }
            change_to_UpperCase(PC_name);
            printf("RICK I DON'T KNOW HIS %s ! \n", PC_name);
            return success;
        }
    }
    printf("OH NO! I CAN'T FIND HIM RICK ! \n");
    return success;
}

/*
 *a function that returns all instances of Jerry that belong to the same planet instance
 *prints out all instances of Jerry that belong to the inputted planet name
 */
status search_by_planet(Jerry **Jerry_Array, Planet **Planet_Array, int jerry_array_size, int planet_array_size,
                        char *input_planet_name) {
    if (Jerry_Array == NULL || input_planet_name == NULL) {
        return failure;
    }
    int planet_switch = 0;
    for (int i = 0; i < planet_array_size; i++) {
        if (strcmp(Planet_Array[i]->name, input_planet_name) == 0) {
            for (int j = 0; j < jerry_array_size; j++) {
                if (strcmp(Jerry_Array[j]->origin->home_planet->name, input_planet_name) == 0) {
                    print_Jerry(Jerry_Array[j]);
                    planet_switch = 1;
                }
            }
            if (planet_switch != 0) {
                return success;
            }
            change_to_UpperCase(input_planet_name);
            printf("OH NO I DON'T KNOW ANY JERRIES FROM %s ! \n", input_planet_name);
            return success;
        }
    }
    change_to_UpperCase(input_planet_name);
    printf("RICK I NEVER HEARD ABOUT %s ! \n", input_planet_name);
    return success;
}

/*
 *a function that searches for a specific name of physical characteristic in all instances of Jerry
 *prints all instances of Jerry that share the same physical characteristic
 */
status search_by_PC(Jerry **Jerry_Array, int jerry_array_size, char *input_PC_name) {
    if (Jerry_Array == NULL || input_PC_name == NULL) {
        return failure;
    }
    int PC_switch = 0;
    for (int i = 0; i < jerry_array_size; i++) {
        Jerry *current_Jerry = Jerry_Array[i];
        if (search_Characteristic(current_Jerry, input_PC_name) == true) {
            for (int j = 0; j < current_Jerry->physical_characteristics_array_size; j++) {
                if (strcmp(current_Jerry->physical_characteristics_array[j]->characteristic_name, input_PC_name) == 0) {
                    print_Jerry(current_Jerry);
                    PC_switch = 1;
                }
            }
        }
    }
    if (PC_switch != 0) {
        return success;
    }

    change_to_UpperCase(input_PC_name);
    printf("OH NO! I DON'T KNOW ANY JERRY'S %s ! \n", input_PC_name);
    return success;
}


int main(int argc, char *argv[]) {
    Jerry *all_Jerries[atoi(argv[2])];
    Planet *all_planets[atoi(argv[1])];

    if (read_configFile(atoi(argv[1]), atoi(argv[2]), argv[3], all_planets, all_Jerries) == failure) {
        printf("Memory problem");
        for (int i = 0; i < atoi(argv[2]); i++) {
            if (all_Jerries[i] != NULL) {
                delete_Jerry(all_Jerries[i]);
            }
        }
        for (int j = 0; j < atoi(argv[1]); j++) {
            if (all_planets[j] != NULL) {
                delete_planet(all_planets[j]);
            }
        }
        exit(1);
    }


    int user_choice;
    char input[3];
    do {
        printf("AW JEEZ RICK, what do you want to do now ? \n");
        printf("1 : Print all Jerries \n");
        printf("2 : Print all Planets \n");
        printf("3 : Add physical characteristic to Jerry \n");
        printf("4 : Remove physical characteristic from Jerry \n");
        printf("5 : Print Jerries by a planet \n");
        printf("6 : Print Jerries by a physical characteristic \n");
        printf("7 : Go home \n");

        //check that the user inputted a proper option, otherwise we clear the input buffer
        scanf("%s", input);
        if (input[1] != '\0') {
            while (getchar() != '\n');
            printf("RICK WE DON'T HAVE TIME FOR YOUR GAMES ! \n");
            continue;
        }
        if (sscanf(input, "%d", &user_choice) != 1) {
            while (getchar() != '\n');
            printf("RICK WE DON'T HAVE TIME FOR YOUR GAMES ! \n");
            continue;
        }

        //menu function for managing all the Jerries in the system
        switch (user_choice) {
            case 1: //print all instances of Jerry
            {
                print_all_Jerries(atoi(argv[2]), all_Jerries);
                break;
            }
            case 2: //print all instances of Planets
            {
                print_all_planets(atoi(argv[1]), all_planets);
                break;
            }
            case 3: //add a physical characteristic to a Jerry instance
            {
                printf("What is your Jerry's ID ? \n");
                char *input_string = NULL;
                char str[300];
                scanf("%s", str);
                int len_of_str = strlen(str);
                input_string = (char *) malloc(len_of_str + 1);
                if (input_string == NULL) {
                    printf("Memory Problem");
                    free(input_string);
                    exit(1);
                }

                strcpy(input_string, str);

                if (add_PC(all_Jerries, atoi(argv[2]), input_string) == failure) {
                    printf("Memory Problem");
                    free(input_string);
                    for (int i = 0; i < atoi(argv[2]); i++) {
                        if (all_Jerries[i] != NULL) {
                            delete_Jerry(all_Jerries[i]);
                        }
                    }
                    for (int j = 0; j < atoi(argv[1]); j++) {
                        if (all_planets[j] != NULL) {
                            delete_planet(all_planets[j]);
                        }
                    }
                    exit(1);
                }

                free(input_string);
                break;
            }
            case 4: //delete a physical characteristic from a Jerry instance
            {
                printf("What is your Jerry's ID ? \n");
                char *input_string = NULL;
                char str[300];
                scanf("%s", str);
                int len_of_str = strlen(str);
                input_string = (char *) malloc(len_of_str + 1);
                if (input_string == NULL) {
                    printf("Memory Problem");
                    free(input_string);
                    exit(1);
                }
                strcpy(input_string, str);

                delete_PC(all_Jerries, atoi(argv[2]), input_string);

                free(input_string);
                break;
            }
            case 5: //print all Jerry instances that are from the same planet
            {
                printf("What planet is your Jerry from ? \n");
                char *input_string = NULL;
                char str[300];
                scanf("%s", str);
                int len_of_str = strlen(str);
                input_string = (char *) malloc(len_of_str + 1);
                if (input_string == NULL) {
                    printf("Memory Problem");
                    free(input_string);
                    exit(1);
                }
                strcpy(input_string, str);

                search_by_planet(all_Jerries, all_planets, atoi(argv[2]), atoi(argv[1]), input_string);
                free(input_string);
                break;
            }
            case 6: //print all Jerry instances that have the same physical characteristic
            {
                printf("What do you know about your Jerry ? \n");
                char *input_string = NULL;
                char str[300];
                scanf("%s", str);
                int len_of_str = strlen(str);
                input_string = (char *) malloc(len_of_str + 1);
                if (input_string == NULL) {
                    printf("Memory Problem");
                    free(input_string);
                    exit(1);
                }
                strcpy(input_string, str);

                search_by_PC(all_Jerries, atoi(argv[2]), input_string);
                free(input_string);
                break;
            }

            case 7: //clean all the data and exit the program
            {
                for (int i = 0; i < atoi(argv[2]); i++) {
                    if (all_Jerries[i] != NULL) {
                        delete_Jerry(all_Jerries[i]);
                    }
                }
                for (int j = 0; j < atoi(argv[1]); j++) {
                    if (all_planets[j] != NULL) {
                        delete_planet(all_planets[j]);
                    }
                }
                printf("AW JEEZ RICK, ALL THE JERRIES GOT FREE ! \n");
                break;
            }
            default:
                printf("RICK WE DON'T HAVE TIME FOR YOUR GAMES ! \n");
        }
    } while (user_choice != 7);
}
