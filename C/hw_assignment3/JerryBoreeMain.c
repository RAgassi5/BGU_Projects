#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "Defs.h"
#include "Jerry.h"
#include "HashTable.h"
#include "MultiValueHashTable.h"
#include "LinkedList.h"

//a function that checks whether a number is a prime number or not
bool is_PrimeNumber(int JerryCount) {
    // anything that is one or smaller is irrelevant
    if (JerryCount <= 1) {
        return false;
    }
    //if the number is 2 or 3, it is a prime number
    if (JerryCount <= 3) {
        return true;
    }
    // eliminate even numbers greater than 2 and numbers divisible by 3
    if (JerryCount % 2 == 0 || JerryCount % 3 == 0) {
        return false;
    }
    // eliminate even numbers greater than 2 and numbers divisible by 3
    for (int i = 5; i * i <= JerryCount; i = i + 6) {
        if (JerryCount % i == 0 || JerryCount % (i + 2) == 0) {
            return false;
        }
    }
    // if no factors are found, the number is prime
    return true;
}

//a function that finds the next prime number
int find_next_Prime(bool number) {
    number = number + 1;
    while (is_PrimeNumber(number) == false) {
        number++;
    }
    return number;
}


//a function to compare two strings
bool compare_strings(char *str1, char *str2) {
    if (strcmp(str1, str2) == 0) {
        return true;
    }
    return false;
}

// WRAPPER FUNCTION
bool COMPARE_string(Element str1, Element str2) {
    return compare_strings((char *) str1, (char *) str2);
}

//a function that makes a copy of a string - Wrapper function
char *copy_string(char *str) {
    int len = strlen(str);
    char *copy = malloc(len + 1);
    if (copy == NULL) {
        return NULL;
    }
    strcpy(copy, str);
    return copy;
}

// WRAPPER FUNCTION
Element COPY_string(Element str) {
    return copy_string((char *) str);
}

//a function to delete a string - Wrapper function
status delete_string(char *str) {
    free(str);
    return success;
}

// WRAPPER FUNCTION
status DELETE_string(Element str) {
    return delete_string((char *) str);
}

//a function that prints a string - Wrapper function
status print_string(char *str) {
    printf("%s : \n", str);
    return success;
}

// WRAPPER FUNCTION
status PRINT_string(Element str) {
    return print_string((char *) str);
}

//a function that transforms a string to its ASCII value - Wrapper function
int transfer_to_ASCII(char *str) {
    int len = strlen(str);
    int ascii_value = 0;
    for (int i = 0; i < len; i++) {
        ascii_value = ascii_value + (int) str[i];
    }
    return ascii_value;
}

// WRAPPER FUNCTION
int TRANSFER_to_ascii(Element str) {
    return transfer_to_ASCII((char *) str);
}

//WRAPPER FUNCTION
status DELETE_JERRY(Element jerryINSTANCE) {
    return delete_Jerry((Jerry *) jerryINSTANCE);
}

//WRAPPER FUNCTION - because all ADTs share the same content
status fake_delete_Jerry(Element JerryInstance) {
    return success;
}

//WRAPPER FUNCTION
bool COMPARE_JERRY(Element jerry1, Element jerry2) {
    return compare_Jerry((Jerry *) jerry1, (Jerry *) jerry2);
}

//WRAPPER FUNCTION
status PRINT_JERRY(Element jerryINSTANCE) {
    return print_Jerry((Jerry *) jerryINSTANCE);
}

//WRAPPER FUNCTION
Element COPY_JERRY(Element jerryINSTANCE) {
    return copy_Jerry((Jerry *) jerryINSTANCE);
}


//a function that finds the amount of different physical characteristics for all the Jerries
int find_pcCount(ListPointer linked_list) {
    // Initialize the count of distinct physical characteristics to 0
    int count = 0;
    // Create a linked list to store the names of distinct physical characteristics
    ListPointer pcList = createLinkedList(DELETE_string, PRINT_string, COMPARE_string);
    if (pcList == NULL) {
        return -1;
    }

    // Iterate through all the Jerry instances in the provided linked list
    for (int i = 1; i < getLengthList(linked_list) + 1; i++) {
        Jerry *current = getDataByIndex(linked_list, i);
        // Iterate through the array of physical characteristics for the current Jerry
        for (int j = 0; j < current->physical_characteristics_array_size; j++) {
            // Check if the current physical characteristic name already exists
            Element pc_name =
                    searchByKeyInList(pcList, current->physical_characteristics_array[j]->characteristic_name);
            if (pc_name == NULL) {
                // Create a copy of the characteristic name and append it to pcList
                char *copy = copy_string((char *) current->physical_characteristics_array[j]->characteristic_name);
                appendNode(pcList, (void *) copy);
            }
        }
    }
    // Determine the total number of distinct physical characteristics by measuring the length of pcList
    count = getLengthList(pcList);
    destroyLinkedList(pcList);
    // Return the count of distinct physical characteristics
    return count;
}


/*
 *a function that receives the configuration file and loads all the data into the menu in the Main
 */
status read_configFile(int number_of_planets, char *file_path, Planet **planet_array, ListPointer *Linked_jerry) {
    //if nothing is loaded into the menu - work as an empty menu
    if (number_of_planets <= 0 || file_path == NULL) {
        return success;
    }

    *Linked_jerry = createLinkedList(DELETE_JERRY, PRINT_JERRY, COMPARE_JERRY);


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
                    Jerry *current_jerry = getDataByIndex(*Linked_jerry, j);
                    if (add_characteristic_to_Jerry(current_jerry, characteristic_token, value) == failure) {
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

                            appendNode(*Linked_jerry, new_instance);
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
 *a function that sets up an instance of a MultiValueHashTable with all the data for our Jerry daycare
*/
hashTable initialize_hashTABLE(ListPointer Linked_jerry, int size) {
    if (Linked_jerry == NULL) {
        return NULL;
    }
    hashTable jerry_hash = createHashTable(COPY_string, DELETE_string, PRINT_string, COPY_JERRY, fake_delete_Jerry,
                                           PRINT_JERRY,
                                           COMPARE_string, TRANSFER_to_ascii,
                                           size);
    if (jerry_hash == NULL) {
        return NULL;
    }

    for (int i = 1; i < (getLengthList(Linked_jerry) + 1); i++) {
        Jerry *current_jerry = getDataByIndex(Linked_jerry, i);
        if (addToHashTable(jerry_hash, current_jerry->ID, current_jerry) == failure) {
            return NULL;
        }
    }
    return jerry_hash;
}

/*
 *a function that sets up an instance of a MultiValueHashTable with all the data for our Jerry daycare
 */
MultiPointer initialize_Multi(ListPointer linked_list, int size) {
    if (linked_list == NULL) {
        return NULL;
    }
    MultiPointer new_multi = createMultiValueHashTable(COPY_string, COPY_JERRY, DELETE_string, fake_delete_Jerry,
                                                       PRINT_string, PRINT_JERRY, COMPARE_string, COMPARE_JERRY,
                                                       TRANSFER_to_ascii, size);
    if(new_multi == NULL) {
        return NULL;
    }

    for (int i = 1; i < (getLengthList(linked_list) + 1); i++) {
        Jerry *current_jerry = getDataByIndex(linked_list, i);

        if (current_jerry == NULL) {
            return NULL;
        }
        for (int j = 0; j < current_jerry->physical_characteristics_array_size; j++) {
            if(addToMultiValueHashTable(new_multi, (current_jerry->physical_characteristics_array[j]->characteristic_name),
                                     current_jerry) == failure) {
                return NULL;
            }
        }
    }
    return new_multi;
}


//a function that checks if an instance of Jerry is already in the system
Jerry *Where_is_Jerry(hashTable table) {
    if (table == NULL) {
        return NULL;
    }

    printf("What is your Jerry's ID ? \n");
    char *ID_string = NULL;
    char str[300];
    //receive input from the user for Jerry's ID
    scanf("%s", str);
    int len_of_str = strlen(str);
    ID_string = (char *) malloc(len_of_str + 1);
    if (ID_string == NULL) {
        printf("A memory problem has been detected in the program \n");
        return NULL;
    }
    strcpy(ID_string, str);
    Jerry *found_Jerry = lookupInHashTable(table, ID_string);
    if (found_Jerry == NULL) {
        free(ID_string);
        return NULL;
    }
    free(ID_string);
    return found_Jerry;
}


//a function that clears all data from the ADTs
status Close_Program(int planetsNUM, Planet **planet_array, ListPointer linkedLIST, hashTable table,
                     MultiPointer multi) {
    if (planet_array == NULL || linkedLIST == NULL || table == NULL || multi == NULL) {
        return failure;
    }
    destroyHashTable(table);
    destroyMultiValueHashTable(multi);
    destroyLinkedList(linkedLIST);
    for (int i = 0; i < planetsNUM; i++) {
        delete_planet(planet_array[i]);
    }
    return success;
}

//a function that receives information about a new instance of Jerry and adds a new Jerry to the data
status add_NewJerry_to_data(int planetsNUM, Planet **planet_array, ListPointer linkedLIST, hashTable table,
                            MultiPointer multi) {
    if (planet_array == NULL || linkedLIST == NULL || table == NULL || multi == NULL) {
        return failure;
    }

    printf("What is your Jerry's ID ? \n");
    char *ID_string = NULL;
    char str[300];
    //receive input from the user for Jerry's ID
    scanf("%s", str);
    int len_of_str = strlen(str);
    ID_string = (char *) malloc(len_of_str + 1);
    if (ID_string == NULL) {
        printf("A memory problem has been detected in the program \n");
        return failure;
    }
    strcpy(ID_string, str);
    if (lookupInHashTable(table, ID_string) != NULL) {
        printf("Rick did you forgot ? you already left him here ! \n");
        free(ID_string);
        return success;
    }
    printf("What planet is your Jerry from ? \n");
    char *planet_input_string = NULL;
    char str2[300];
    //receive input from the user for a planet name
    scanf("%s", str2);
    int len_of_str2 = strlen(str2);
    planet_input_string = (char *) malloc(len_of_str2 + 1);
    if (planet_input_string == NULL) {
        free(ID_string);
        free(planet_input_string);
        return failure;
    }
    strcpy(planet_input_string, str2);
    for (int i = 0; i < planetsNUM; i++) {
        if (strcmp(planet_input_string, planet_array[i]->name) == 0) {
            printf("What is your Jerry's dimension ? \n");
            char *dimension_input_string = NULL;
            char str3[300];
            //receive input from the user for Jerry's dimension
            scanf("%s", str3);
            int len_of_str3 = strlen(str3);
            dimension_input_string = (char *) malloc(len_of_str3 + 1);
            if (dimension_input_string == NULL) {
                free(ID_string);
                free(planet_input_string);
                return failure;
            }
            strcpy(dimension_input_string, str3);
            printf("How happy is your Jerry now ? \n");
            int happiness;
            //receive input from the user for Jerry's happiness level
            if (scanf("%d", &happiness) == 1) {
                Jerry *newJerry = create_new_Jerry(ID_string, happiness, dimension_input_string, planet_array[i]);
                if (appendNode(linkedLIST, newJerry) == failure) {
                    free(ID_string);
                    free(planet_input_string);
                    free(dimension_input_string);
                    return failure;
                }
                if (addToHashTable(table, ID_string, newJerry) == failure) {
                    free(ID_string);
                    free(planet_input_string);
                    free(dimension_input_string);
                    return failure;
                }
                for (int j = 0; j < newJerry->physical_characteristics_array_size; j++) {
                    if (addToMultiValueHashTable(
                            multi, newJerry->physical_characteristics_array[j]->characteristic_name,
                            newJerry) == failure) {
                        free(ID_string);
                        free(planet_input_string);
                        free(dimension_input_string);
                        return failure;
                    }
                }
                print_Jerry(newJerry);
                free(ID_string);
                free(planet_input_string);
                free(dimension_input_string);
                return success;
            } else {
                free(ID_string);
                free(planet_input_string);
                free(dimension_input_string);
                return failure;
            }
        }
    }
    printf("%s is not a known planet ! \n", planet_input_string);
    free(ID_string);
    free(planet_input_string);
    return success;
}

//a function that receives information form the user and deletes a specific instance of a Jerry from the data
status delete_A_jerry(ListPointer linkedLIST, hashTable table, MultiPointer multi) {
    if (linkedLIST == NULL || table == NULL || multi == NULL) {
        return failure;
    }
    Jerry *to_delete = Where_is_Jerry(table);
    if (to_delete == NULL) {
        printf("Rick this Jerry is not in the daycare ! \n");
        return success;
    }
    if (removeFromHashTable(table, to_delete->ID) == failure) {
        return failure;
    }
    for (int i = 0; i < to_delete->physical_characteristics_array_size; i++) {
        if (removeFromMultiValueHashTable(multi, to_delete->physical_characteristics_array[i]->characteristic_name,
                                          to_delete) == failure) {
            return failure;
        }
    }

    if (deleteNode(linkedLIST, to_delete) == failure) {
        return failure;
    }
    printf("Rick thank you for using our daycare service ! Your Jerry awaits ! \n");
    return success;
}

//a function that adds a characteristic to an instance of Jerry
status upgradeJerry(ListPointer linkedLIST, hashTable table, MultiPointer multi) {
    if (linkedLIST == NULL || table == NULL || multi == NULL) {
        return failure;
    }

    Jerry *Jerry_to_upgrade = Where_is_Jerry(table);
    if (Jerry_to_upgrade == NULL) {
        printf("Rick this Jerry is not in the daycare ! \n");
        return success;
    }
    printf("What physical characteristic can you add to Jerry - %s ? \n", Jerry_to_upgrade->ID);
    char *characteristic_input_string = NULL;
    char Physicalstr[300];
    //receive input from the user for Jerry's ID
    scanf("%s", Physicalstr);
    int len_of_str = strlen(Physicalstr);
    characteristic_input_string = (char *) malloc(len_of_str + 1);
    if (characteristic_input_string == NULL) {
        printf("A memory problem has been detected in the program \n");
        return failure;
    }
    strcpy(characteristic_input_string, Physicalstr);
    // check if the characteristic already exists for the given Jerry
    if (search_Characteristic(Jerry_to_upgrade, characteristic_input_string) == true) {
        printf("The information about his %s already available to the daycare ! \n", characteristic_input_string);
        free(characteristic_input_string);
        return success;
    }
    int PC_value;
    printf("What is the value of his %s ? \n", characteristic_input_string);
    if (scanf("%d", &PC_value) == 1) {
        // add the characteristic and its value to the Jerry instance
        if (add_characteristic_to_Jerry(Jerry_to_upgrade, characteristic_input_string, PC_value) == failure) {
            free(characteristic_input_string);
            return failure;
        }
        // add the characteristic to the multi-value hash table
        if (addToMultiValueHashTable(multi, characteristic_input_string, Jerry_to_upgrade) == failure) {
            free(characteristic_input_string);
            return failure;
        }
        // display all elements associated with the characteristic in the multi-value hash table
        if(displayMultiValueHashElementsByKey(multi, characteristic_input_string) == failure) {
            free(characteristic_input_string);
            return failure;
        }
        free(characteristic_input_string);
        return success;
    }
    free(characteristic_input_string);
    return failure;
}

//a function that removes a specific physical characteristic from an instance of Jerry
status degradeJerry(ListPointer linkedLIST, hashTable table, MultiPointer multi) {
    if (linkedLIST == NULL || table == NULL || multi == NULL) {
        return failure;
    }

    Jerry *Jerry_to_degrade = Where_is_Jerry(table);
    if (Jerry_to_degrade == NULL) {
        printf("Rick this Jerry is not in the daycare ! \n");
        return success;
    }
    printf("What physical characteristic do you want to remove from Jerry - %s ? \n", Jerry_to_degrade->ID);
    char *characteristic_input_string = NULL;
    char Physicalstr[300];
    //receive input from the user for Jerry's ID
    scanf("%s", Physicalstr);
    int len_of_str = strlen(Physicalstr);
    characteristic_input_string = (char *) malloc(len_of_str + 1);
    if (characteristic_input_string == NULL) {
        printf("A memory problem has been detected in the program \n");
        return failure;
    }
    strcpy(characteristic_input_string, Physicalstr);
    // check if the specified characteristic exists for the given Jerry
    if (search_Characteristic(Jerry_to_degrade, characteristic_input_string) == false) {
        printf("The information about his %s not available to the daycare ! \n", characteristic_input_string);
        free(characteristic_input_string);
        return success;
    }
    // try to remove the characteristic from Jerry
    if (delete_characteristic_from_Jerry(Jerry_to_degrade, characteristic_input_string) == failure) {
        free(characteristic_input_string);
        return failure;
    }
    // remove the characteristic from the multi-value hash table
    if (removeFromMultiValueHashTable(multi, characteristic_input_string, Jerry_to_degrade) == failure) {
        free(characteristic_input_string);
        return failure;
    }
    // print the information of Jerry
    if(print_Jerry(Jerry_to_degrade) == failure) {
        free(characteristic_input_string);
        return failure;
    }
    free(characteristic_input_string);
    return success;
}

//a function that calculates the absolute value of the differential between to numbers
int calculate_absolute_value(int a, int b) {
    int result = a - b;
    if (result < 0) {
        result = -result;
    }
    return result;
}

/*
 *a function that searches for the Jerry that is most similar to the user's request. Searches for a specific characteristic that has the closest value to the input that was inserted by the user
 */
status Give_me_similar(ListPointer linkedLIST, hashTable table, MultiPointer multi) {
    if (linkedLIST == NULL || table == NULL || multi == NULL) {
        return failure;
    }
    printf("What do you remember about your Jerry ? \n");
    char *characteristic_input_string = NULL;
    char Physicalstr[300];
    //receive input from the user for Jerry's ID
    scanf("%s", Physicalstr);
    int len_of_str = strlen(Physicalstr);
    characteristic_input_string = (char *) malloc(len_of_str + 1);
    if (characteristic_input_string == NULL) {
        printf("A memory problem has been detected in the program \n");
        return failure;
    }
    strcpy(characteristic_input_string, Physicalstr);
    //search the MultiValueHashTable to see if the characteristic is in the system
    ListPointer all_Jerries_with_PC = lookupInMultiValueHashTable(multi, characteristic_input_string);
    if (all_Jerries_with_PC == NULL) {
        printf("Rick we can not help you - we do not know any Jerry's %s ! \n", characteristic_input_string);
        free(characteristic_input_string);
        return success;
    }
    printf("What do you remember about the value of his %s ? \n", characteristic_input_string);
    int PC_value;
    if (scanf("%d", &PC_value) == 1) {
        //the first Jerry that has the given physical characteristic
        Jerry *theMostSimilar = getDataByIndex(all_Jerries_with_PC, 1);
        for (int i = 2; i < getLengthList(all_Jerries_with_PC) + 1; i++) {
            //the current Jerry with the given characteristic
            Jerry *currentJerry = getDataByIndex(all_Jerries_with_PC, i);
            int closestValue;
            //once we find the given characteristic we calculate the absolute value
            for (int k = 0; k < theMostSimilar->physical_characteristics_array_size; k++) {
                if (strcmp(theMostSimilar->physical_characteristics_array[k]->characteristic_name,
                           characteristic_input_string) == 0) {
                     closestValue = calculate_absolute_value(
                        PC_value, theMostSimilar->physical_characteristics_array[k]->characteristic_value);
                }
            }
            int currentValue;
            //once we find the given characteristic we calculate the absolute value
            for (int h = 0; h < currentJerry->physical_characteristics_array_size; h++) {
                if (strcmp(currentJerry->physical_characteristics_array[h]->characteristic_name,
                           characteristic_input_string) == 0) {
                    currentValue = calculate_absolute_value(
                        PC_value, currentJerry->physical_characteristics_array[h]->characteristic_value);
                }
            }
            //if the absolute value is smaller we update who the most similar Jerry is
            if(currentValue < closestValue) {
                theMostSimilar = currentJerry;
            }
        }
        printf("Rick this is the most suitable Jerry we found : \n");
        //print the information of the chosen Jerry
        if(print_Jerry(theMostSimilar) == failure) {
            free(characteristic_input_string);
            return failure;
        }
        //remove the similar instance of Jerry from the MultiValueHashTable
        if(removeFromMultiValueHashTable(multi,characteristic_input_string, theMostSimilar) == failure) {
            free(characteristic_input_string);
            return failure;
        }
        //remove the similar instance of Jerry from the HashTable
        if(removeFromHashTable(table, theMostSimilar->ID) == failure) {
            free(characteristic_input_string);
            return failure;
        }
        //remove the similar instance of Jerry from the LinkedList
        if(deleteNode(linkedLIST, theMostSimilar) == failure) {
            free(characteristic_input_string);
            return failure;
        }
        free(characteristic_input_string);
        printf("Rick thank you for using our daycare service ! Your Jerry awaits ! \n");
        return success;
    }
    return failure;
}

//a function that searches for the instance of Jerry with the lowest happiness level, once found we "return" it to Rick and delete the instance from our system
status theSADDEST(ListPointer linkedLIST, hashTable table, MultiPointer multi) {
    if (linkedLIST == NULL || table == NULL || multi == NULL) {
        return failure;
    }
    Jerry *theSaddest = getDataByIndex(linkedLIST, 1);
    //go over all the instances of Jerry in find the one with the lowest happiness level
    for (int i = 2; i < getLengthList(linkedLIST) + 1; i++) {
        Jerry *current = getDataByIndex(linkedLIST, i);
        if (current->happiness_level < theSaddest->happiness_level) {
            theSaddest = current;
        }
    }
    printf("Rick this is the most suitable Jerry we found : \n");
    if(print_Jerry(theSaddest) == failure) {
        return failure;
    }
    //-----------DELETE the saddest Jerry from the system-------------------//
    //after finding the proper Jerry we remove him from the database and "return" it to Rick
    if(removeFromHashTable(table, theSaddest->ID) == failure) {
        free(theSaddest);
        return failure;
    }
    for (int j = 0; j < theSaddest->physical_characteristics_array_size; j++) {
        if(removeFromMultiValueHashTable(multi,
                                      theSaddest->physical_characteristics_array[j]->characteristic_name,
                                      theSaddest) == failure) {
            free(theSaddest);
            return failure;
        }
    }
    if(deleteNode(linkedLIST, theSaddest) == failure) {
        free(theSaddest);
        return failure;
    }
    //-----------------------------------------------------//

    printf("Rick thank you for using our daycare service ! Your Jerry awaits ! \n");
    return success;
}




int main(int argc, char *argv[]) {

    int number_of_planets = atoi(argv[1]);
    char *file_path = argv[2];
    Planet *all_planets[number_of_planets];
    ListPointer Linked_Jerry;

    //read the configuration file and initialize the data in the system
    if (read_configFile(number_of_planets, file_path, all_planets, &Linked_Jerry) == failure) {
        printf("A memory problem has been detected in the program \n");
        destroyLinkedList(Linked_Jerry);
        for (int i = 0; i < number_of_planets; i++) {
            delete_planet(all_planets[i]);
        }
        exit(1);
    }

    //find the amount of distinct characteristics in all the given instances of Jerry
    int distinct_Characteristics = find_pcCount(Linked_Jerry);
    //initialize data structures to maintain all data for the menu
    //initialize an instance of a HashTable that holds Jerry IDs as key and instances of Jerry as values
    hashTable Jerry_hash = initialize_hashTABLE(Linked_Jerry, find_next_Prime((getLengthList(Linked_Jerry))) / 0.75);
    //if initialization for HashTable fails, delete all data and exit the program
    if (Jerry_hash == NULL) {
        printf("A memory problem has been detected in the program \n");
        destroyLinkedList(Linked_Jerry);

        for (int i = 0; i < number_of_planets; i++) {
            delete_planet(all_planets[i]);
        }
        exit(1);
    }
    //initialize an instance of a MultiValueHashTable that holds physical characteristic name as key and instances of Jerry as values
    MultiPointer PhysicalCharacteristic_multi = initialize_Multi(Linked_Jerry,
                                                                 find_next_Prime(distinct_Characteristics / 0.75));
    //if initialization for MultiValueHashTable fails, delete all data and exit the program
    if (PhysicalCharacteristic_multi == NULL) {
        printf("A memory problem has been detected in the program \n");
        destroyHashTable(Jerry_hash);
        destroyLinkedList(Linked_Jerry);

        for (int i = 0; i < number_of_planets; i++) {
            delete_planet(all_planets[i]);
        }
        exit(1);
    }

    /*--------------
     *  Main Menu:
     *----------------
     *  The menu lets the user receive information and access the data of the dau care.
     *  There are 1-9 distinct options, each selection has a different functionality
     *
     * NOTE TO THE USER:
     * selections 7 and 8 include code segments in them! -> this was done on purpose because of the small amount of lines and the simplicity of the logic
     */

    int user_choice;
    char input[300];
    do {
        printf("Welcome Rick, what are your Jerry's needs today ? \n");
        printf("1 : Take this Jerry away from me \n");
        printf("2 : I think I remember something about my Jerry \n");
        printf("3 : Oh wait. That can't be right \n");
        printf("4 : I guess I will take back my Jerry now \n");
        printf("5 : I can't find my Jerry. Just give me a similar one \n");
        printf("6 : I lost a bet. Give me your saddest Jerry \n");
        printf("7 : Show me what you got \n");
        printf("8 : Let the Jerries play \n");
        printf("9 : I had enough. Close this place \n");


        //check that the user inputted a proper option, otherwise we clear the input buffer
        scanf("%s", input);
        if (input[1] != '\0') {
            while (getchar() != '\n');
            printf("Rick this option is not known to the daycare ! \n");
            continue;
        }
        if (sscanf(input, "%d", &user_choice) != 1) {
            while (getchar() != '\n');
            printf("Rick this option is not known to the daycare ! \n");
            continue;
        }

        //menu function for managing all the Jerries in the system
        switch (user_choice) {
            case 1: {
                if (add_NewJerry_to_data(number_of_planets, all_planets, Linked_Jerry, Jerry_hash,
                                         PhysicalCharacteristic_multi) == failure) {
                    printf("A memory problem has been detected in the program \n");
                    Close_Program(number_of_planets, all_planets, Linked_Jerry, Jerry_hash,
                                  PhysicalCharacteristic_multi);
                    exit(1);
                }
                break;
            }
            case 2: {
                if (upgradeJerry(Linked_Jerry, Jerry_hash, PhysicalCharacteristic_multi) == failure) {
                    printf("A memory problem has been detected in the program \n");
                    Close_Program(number_of_planets, all_planets, Linked_Jerry, Jerry_hash,
                                  PhysicalCharacteristic_multi);
                    exit(1);
                }

                break;
            }
            case 3: {
                if (degradeJerry(Linked_Jerry, Jerry_hash, PhysicalCharacteristic_multi) == failure) {
                    printf("A memory problem has been detected in the program \n");
                    Close_Program(number_of_planets, all_planets, Linked_Jerry, Jerry_hash,
                                  PhysicalCharacteristic_multi);
                    exit(1);
                }
                break;
            }
            case 4: {
                if (delete_A_jerry(Linked_Jerry, Jerry_hash, PhysicalCharacteristic_multi) == failure) {
                    printf("A memory problem has been detected in the program \n");
                    Close_Program(number_of_planets, all_planets, Linked_Jerry, Jerry_hash,
                                  PhysicalCharacteristic_multi);
                    exit(1);
                }
                break;
            }
            case 5: {
                if(Give_me_similar(Linked_Jerry, Jerry_hash, PhysicalCharacteristic_multi) == failure) {
                    printf("A memory problem has been detected in the program \n");
                    Close_Program(number_of_planets, all_planets, Linked_Jerry, Jerry_hash,
                                  PhysicalCharacteristic_multi);
                    exit(1);
                }
                break;
            }
            case 6: {
                //if there are no Jerries in the day care, there is no need to search for a Jerry, so we return the following message:
                if (getLengthList(Linked_Jerry) == 0) {
                    printf("Rick we can not help you - we currently have no Jerries in the daycare ! \n");
                    break;
                }
                if(theSADDEST(Linked_Jerry, Jerry_hash, PhysicalCharacteristic_multi) == failure) {
                    printf("A memory problem has been detected in the program \n");
                    Close_Program(number_of_planets, all_planets, Linked_Jerry, Jerry_hash,
                                  PhysicalCharacteristic_multi);
                    exit(1);
                }
                break;
            }
            case 7: {
                //a switch that controls when to exit the program
                int counter = 0;
                int mini_menu_choice;
                char mini_input[300];
                do {
                    printf("What information do you want to know ? \n");
                    printf("1 : All Jerries \n");
                    printf("2 : All Jerries by physical characteristics \n");
                    printf("3 : All known planets \n");
                    scanf("%s", mini_input);
                    if (mini_input[1] != '\0') {
                        while (getchar() != '\n');
                        printf("Rick this option is not known to the daycare ! \n");
                        counter ++;
                        continue;
                    }
                    if (sscanf(mini_input, "%d", &mini_menu_choice) != 1) {
                        while (getchar() != '\n');
                        printf("Rick this option is not known to the daycare ! \n");
                        counter ++;
                        continue;
                    }

                    switch (mini_menu_choice) {
                        //display all Jerries that are in the system
                        case 1: {
                            if (getLengthList(Linked_Jerry) > 0) {
                                displayList(Linked_Jerry);
                            } else {
                            //if there are no Jerries in the day care, there is no need to search for a Jerry, so we return the following message:
                                printf("Rick we can not help you - we currently have no Jerries in the daycare ! \n");
                            }
                            counter++;
                            break;
                        }
                        //print all Jerries that share a similar physical characteristic
                        case 2: {
                            printf("What physical characteristics ? \n");
                            char *string = NULL;
                            char str_buffer[300];
                            //receive input from the user for Jerry's ID
                            scanf("%s", str_buffer);
                            int len_of_str = strlen(str_buffer);
                            string = (char *) malloc(len_of_str + 1);
                            if (string == NULL) {
                                printf("A memory problem has been detected in the program \n");
                                Close_Program(number_of_planets, all_planets, Linked_Jerry, Jerry_hash,
                                              PhysicalCharacteristic_multi);
                            }
                            strcpy(string, str_buffer);
                            if (lookupInMultiValueHashTable(PhysicalCharacteristic_multi, string) == NULL) {
                                printf("Rick we can not help you - we do not know any Jerry's %s ! \n", string);
                                while (getchar() != '\n');
                                free(string);
                            }
                            else {
                                displayMultiValueHashElementsByKey(PhysicalCharacteristic_multi, string);
                                free(string);
                            }
                            counter++;
                            break;
                        }
                        //print all planets that are in the system
                        case 3: {
                            for (int i = 0; i < number_of_planets; i++) {
                                print_Planet(all_planets[i]);
                            }
                            counter++;
                            break;
                        }
                        default: {
                            printf("Rick this option is not known to the daycare ! \n");
                            counter ++;
                            while (getchar() != '\n');
                            break;
                        }
                    }
                } while (counter != 1);
                break;
            }
            case 8: {
            //if there are no Jerries in the day care, there is no need to search for a Jerry, so we return the following message:
                if(getLengthList(Linked_Jerry) == 0) {
                    printf("Rick we can not help you - we currently have no Jerries in the daycare ! \n");
                    break;
                }
                //a switch that controls when to exit the program
                int counter = 0;
                int mini_menu_choice;
                char mini_input[300];
                do {
                    printf("What activity do you want the Jerries to partake in ? \n");
                    printf("1 : Interact with fake Beth \n");
                    printf("2 : Play golf \n");
                    printf("3 : Adjust the picture settings on the TV \n");
                    scanf("%s", mini_input);
                    if (mini_input[1] != '\0') {
                        while (getchar() != '\n');
                        printf("Rick this option is not known to the daycare ! \n");
                        counter ++;
                        continue;
                    }
                    if (sscanf(mini_input, "%d", &mini_menu_choice) != 1) {
                        while (getchar() != '\n');
                        printf("Rick this option is not known to the daycare ! \n");
                        counter ++;
                        continue;
                    }

                    switch (mini_menu_choice) {
                        //play with fake Beth
                        //adjust the happiness level for each Jerry in the system accordingly
                        case 1: {
                            for (int i = 1; i < getLengthList(Linked_Jerry) + 1; i++) {
                                Jerry *current_jerry = getDataByIndex(Linked_Jerry, i);
                                if (current_jerry->happiness_level >= 20) {
                                    current_jerry->happiness_level += 15;
                                    if (current_jerry->happiness_level >= 100) {
                                        current_jerry->happiness_level = 100;
                                    }
                                }
                                if (current_jerry->happiness_level < 20) {
                                    current_jerry->happiness_level -= 5;
                                    if (current_jerry->happiness_level < 1) {
                                        current_jerry->happiness_level = 0;
                                    }
                                }
                            }
                            counter++;
                            break;
                        }
                        //play golf
                        //adjust the happiness level for each Jerry in the system accordingly
                        case 2: {
                            for (int i = 1; i < getLengthList(Linked_Jerry) + 1; i++) {
                                Jerry *current_jerry = getDataByIndex(Linked_Jerry, i);
                                if (current_jerry->happiness_level >= 50) {
                                    current_jerry->happiness_level += 10;
                                    if (current_jerry->happiness_level >= 100) {
                                        current_jerry->happiness_level = 100;
                                    }
                                } else {
                                    current_jerry->happiness_level -= 10;
                                    if (current_jerry->happiness_level < 1) {
                                        current_jerry->happiness_level = 0;
                                    }
                                }
                            }
                            counter++;
                            break;
                        }
                        //adjust TV settings
                        //adjust the happiness level for each Jerry in the system accordingly
                        case 3: {
                            for (int i = 1; i < getLengthList(Linked_Jerry) + 1; i++) {
                                Jerry *current_jerry = getDataByIndex(Linked_Jerry, i);
                                current_jerry->happiness_level += 20;
                                if (current_jerry->happiness_level >= 100) {
                                    current_jerry->happiness_level = 100;
                                }
                            }
                            counter++;
                            break;
                        }
                        default: {
                            printf("Rick this option is not known to the daycare ! \n");
                            counter ++;
                            while (getchar() != '\n');
                            break;
                        }
                    }
                    printf("The activity is now over ! \n");
                    displayList(Linked_Jerry);
                } while (counter != 1);
                break;
            }

            case 9: {
                //the user decided to exit the menu, clear all data and free all data structures, exit the program
                Close_Program(number_of_planets, all_planets, Linked_Jerry, Jerry_hash,
                              PhysicalCharacteristic_multi);
                printf("The daycare is now clean and close ! \n");
                break;
            }

            default: {
                printf("Rick this option is not known to the daycare ! \n");
                while (getchar() != '\n');
                break;
            }
        }
    } while (user_choice != 9);
}
