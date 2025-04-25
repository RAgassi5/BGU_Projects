#ifndef MULTIVALUEHASHTABLE_H
#define MULTIVALUEHASHTABLE_H
#include "Defs.h"

typedef struct MultiValueHashTable *MultiPointer;

//a function that creates a new instance of a MultiValueHashTable
MultiPointer createMultiValueHashTable(CopyFunction copyKey, CopyFunction copyValue, FreeFunction freeKey,
                                       FreeFunction freeValue, PrintFunction printKey, PrintFunction printValue,
                                       EqualFunction equalKey, EqualFunction compareValue,TransformIntoNumberFunction transformIntoNumber,
                                       int multi_tableSize);

//a function that deletes an instance of MultiValueHashTable and all its data
status destroyMultiValueHashTable(MultiPointer multiPointer);

//a function that adds a new item to an instance of a MultiValueHashTable
status addToMultiValueHashTable(MultiPointer multiPointer, Element key, Element value);

//a function that searches for a specific key in an instance of a MultiValueHashTable
Element lookupInMultiValueHashTable(MultiPointer multiPointer, Element key);

//a function that removes a specific data from an instance of a MultiValueHashTable by specific key and value
status removeFromMultiValueHashTable(MultiPointer multiPointer, Element key, Element value);

//a function that prints a data segment that belongs to a specific key in an instance of a MultiValueHashTable
status displayMultiValueHashElementsByKey(MultiPointer multiPointer, Element key);

#endif //MULTIVALUEHASHTABLE_H
