#include "MultiValueHashTable.h"
#include "HashTable.h"
#include "LinkedList.h"
#include "KeyValuePair.h"

//a function that "copies" an instance of a linked list
ListPointer copy_linked(ListPointer list) {
    if (list == NULL) {
        return NULL;
    }
    return  list;
}

 //construction of an ADT called MultiValueHashTable
struct MultiValueHashTable {
    hashTable structure;
    CopyFunction copyValue;
    FreeFunction freeValue;
    PrintFunction printKey;
    PrintFunction printValue;
    EqualFunction compareValue;
    TransformIntoNumberFunction transformIntoNumber;
    int multi_tableSize;
};

//a function that creates a new instance of a MultiValueHashTable
MultiPointer createMultiValueHashTable(CopyFunction copyKey, CopyFunction copyValue, FreeFunction freeKey,
                                       FreeFunction freeValue, PrintFunction printKey, PrintFunction printValue,
                                       EqualFunction equalKey, EqualFunction compareValue, TransformIntoNumberFunction transformIntoNumber,
                                       int multi_tableSize) {
    if (copyKey == NULL || copyValue == NULL || freeKey == NULL || freeValue == NULL || printKey == NULL || printValue
        == NULL || equalKey == NULL || compareValue == NULL ||transformIntoNumber == NULL || multi_tableSize < 1) {
        return NULL;
    }


    MultiPointer new_multiPointer = malloc(sizeof(struct MultiValueHashTable));
    if (new_multiPointer == NULL) {
        return NULL;
    }

    new_multiPointer->structure = createHashTable(copyKey, freeKey, printKey, (CopyFunction) copy_linked, (FreeFunction)destroyLinkedList, (PrintFunction)displayList,
                                                  equalKey, transformIntoNumber, multi_tableSize);
    if (new_multiPointer->structure == NULL) {
        free(new_multiPointer);
        return NULL;
    }

    new_multiPointer->copyValue = copyValue;
    new_multiPointer->freeValue = freeValue;
    new_multiPointer->printKey = printKey;
    new_multiPointer->printValue = printValue;
    new_multiPointer->compareValue = compareValue;
    new_multiPointer->transformIntoNumber = transformIntoNumber;
    new_multiPointer->multi_tableSize = multi_tableSize;


    return new_multiPointer;
}

//a function that deletes an instance of a MultiValueHashTable and all its data
status destroyMultiValueHashTable(MultiPointer multiPointer) {
    if (multiPointer == NULL) {
        return failure;
    }
    if(destroyHashTable(multiPointer->structure) == failure) {
        return failure;
    }
    free(multiPointer);
    return success;
}

//a function that adds a new key and value to the MultiValueHashTable
status addToMultiValueHashTable(MultiPointer multiPointer, Element key, Element value) {
    if (multiPointer == NULL || key == NULL || value == NULL) {
        return failure;
    }

    ListPointer value_to_add_to = lookupInHashTable(multiPointer->structure, key);
    if(value_to_add_to == NULL) {

        ListPointer new_value = createLinkedList(multiPointer->freeValue, multiPointer->printValue, multiPointer->compareValue);
        if(new_value == NULL) {
            return failure;
        }
        if(appendNode(new_value, value) == failure) {
            destroyLinkedList(new_value);
            return failure;
        }

        if(addToHashTable(multiPointer->structure, key, new_value) == failure) {
            destroyLinkedList(new_value);
            return failure;
        }
       // displayHashElements(multiPointer->structure);
        return success;
    }

    if(appendNode((ListPointer)value_to_add_to, value) == failure) {
        return failure;
    }

    return success;
}

//a function that searches for a specific key's value, if exists returns the value, else returns NULL
Element lookupInMultiValueHashTable(MultiPointer multiPointer, Element key) {
    if (multiPointer == NULL || key == NULL) {
        return NULL;
    }

    Element value_to_return = lookupInHashTable(multiPointer->structure, key);
    return value_to_return;
}

//a function that removes data from an instance of a MultiValueHashTable by key
status removeFromMultiValueHashTable(MultiPointer multiPointer, Element key, Element value) {
    if (multiPointer == NULL || key == NULL || value == NULL) {
        return failure;
    }
    Element data_to_delete = lookupInMultiValueHashTable(multiPointer, key);
    if (data_to_delete == NULL) {
        return failure;
    }
    if(getLengthList((ListPointer)data_to_delete) == 1) {
        deleteNode((ListPointer)data_to_delete, value);
        removeFromHashTable(multiPointer->structure, key);
        return success;
    }

    deleteNode((ListPointer)data_to_delete, value);
    return success;
}

//a function that prints out a specific item by key from an instance of MultiValueHashTable
status displayMultiValueHashElementsByKey(MultiPointer multiPointer, Element key) {
    if (multiPointer == NULL) {
        return failure;
    }
    //Element key_to_display = lookupInMultiValueHashTable(multiPointer,key);
    Element key_to_display = lookupInHashTable(multiPointer->structure, key);
    if (key_to_display == NULL) {
        return failure;
    }


    if(multiPointer->printKey(key) == failure) {
        return failure;
    }

    if(displayList((ListPointer)key_to_display)== failure) {
        return failure;
    }
    return success;
}