#ifndef KEYVALUEPAIR_H
#define KEYVALUEPAIR_H
#include "Defs.h"


typedef struct KeyValuePair * KeyValuePair_Pointer;

//a function that creates a new instance of KeyValuePair
KeyValuePair_Pointer createKeyValuePair(Element key, Element value, CopyFunction copyKey, CopyFunction copyValue,
                                        FreeFunction freeKey, FreeFunction freeValue, PrintFunction printKey,
                                        PrintFunction printValue,
                                        EqualFunction compareFunction);
//a function that deletes a KeyValuePair and all the data it holds
status destroyKeyValuePair(KeyValuePair_Pointer instance_of_KeyValuePair);

//print the key of an instance of a KeyValuePair
status displayKey(KeyValuePair_Pointer instance_of_KeyValuePair);

//print the value of an instance of a KeyValuePair
status displayValue(KeyValuePair_Pointer instance_of_KeyValuePair);

//a function that returns the item that is the key of an instance of a KeyValuePair
Element getKey(KeyValuePair_Pointer instance_of_KeyValuePair);

//a function that returns the item that is the value of an instance of a KeyValuePair
Element getValue(KeyValuePair_Pointer instance_of_KeyValuePair);

///a bool function that checks whether a given value is equal to the key of the KeyValuePair instance
bool isEqualKey(KeyValuePair_Pointer instance_of_KeyValuePair, Element other_key);

//a print function for an instance of a KeyValuePair
status displayPair(KeyValuePair_Pointer instance_of_KeyValuePair);

#endif //KEYVALUEPAIR_H
