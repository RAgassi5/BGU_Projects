#include "HashTable.h"
#include "LinkedList.h"
#include "KeyValuePair.h"

//wrapper function - makes sure the received element is KeyValuePair_Pointer
status DestroyKeyValuePair(Element elem) {
    return destroyKeyValuePair((KeyValuePair_Pointer)elem);
}

//wrapper function - makes sure the received element is KeyValuePair_Pointer
status DisplayPair(Element elem) {
    return displayPair((KeyValuePair_Pointer)elem);
}

//wrapper function - makes sure the received element is KeyValuePair_Pointer
bool ISequal(Element a, Element b) {
    return isEqualKey((KeyValuePair_Pointer)a, b);
}

//construction of generic ADT for Hashtable
struct hashTable_s {
    ListPointer *Hash_structure;
    CopyFunction copyKey;
    CopyFunction copyValue;
    FreeFunction freeKey;
    FreeFunction freeValue;
    PrintFunction printKey;
    PrintFunction printValue;
    EqualFunction equalKey;
    TransformIntoNumberFunction transformIntoNumber;
    int tableSize;
};

//a function that creates a new instance of a hashTable_s
hashTable createHashTable(CopyFunction copyKey, FreeFunction freeKey, PrintFunction printKey, CopyFunction copyValue,
                          FreeFunction freeValue, PrintFunction printValue, EqualFunction equalKey,
                          TransformIntoNumberFunction transformIntoNumber, int hashNumber) {
    if (copyKey == NULL || freeKey == NULL || printKey == NULL || copyValue == NULL || freeValue == NULL || equalKey ==
        NULL || transformIntoNumber == NULL || printValue == NULL) {
        return NULL;
    }

    hashTable newHashTable = malloc(sizeof(struct hashTable_s));
    if (newHashTable == NULL) {
        return NULL;
    }
    newHashTable -> Hash_structure = malloc(sizeof(ListPointer) * hashNumber);
    if (newHashTable -> Hash_structure == NULL) {
        free(newHashTable);
        return NULL;
    }
    for (int i = 0; i < hashNumber; i++) {
        newHashTable->Hash_structure[i] = createLinkedList(DestroyKeyValuePair, DisplayPair, ISequal);
        if (newHashTable->Hash_structure[i] == NULL) {
            for (int j = 0; j < i; j++) {
                destroyLinkedList(newHashTable->Hash_structure[j]);
            }
            free(newHashTable->Hash_structure);
            free(newHashTable);
            return NULL;
        }
    }

    newHashTable->copyKey = copyKey;
    newHashTable->copyValue = copyValue;
    newHashTable->freeKey = freeKey;
    newHashTable->printKey = printKey;
    newHashTable->freeValue = freeValue;
    newHashTable->printValue = printValue;
    newHashTable->equalKey = equalKey;
    newHashTable->transformIntoNumber = transformIntoNumber;
    newHashTable->tableSize = hashNumber;
    return newHashTable;
}

//a function that deletes an instance of a hashTable_s
status destroyHashTable(hashTable htable_instance) {
    if (htable_instance == NULL) {
        return failure;
    }
    for (int i = 0; i < htable_instance->tableSize; i++) {
        if(destroyLinkedList(htable_instance->Hash_structure[i]) == failure) {
            return failure;
        }
    }
    free(htable_instance->Hash_structure);
    free(htable_instance);
    return success;
}

//a function that add new data to an instance of a hashTable_s
status addToHashTable(hashTable hTable_instance, Element key, Element value) {
    if (hTable_instance == NULL || key == NULL || value == NULL) {
        return failure;
    }
    KeyValuePair_Pointer data_to_insert = createKeyValuePair(key, value, hTable_instance->copyKey,
                                                             hTable_instance->copyValue, hTable_instance->freeKey,
                                                             hTable_instance->freeValue, hTable_instance->printKey,
                                                             hTable_instance->printValue, hTable_instance->equalKey);
    if(data_to_insert == NULL) {
        return failure;
    }
    int key_to_insert = (hTable_instance->transformIntoNumber(key)) % (hTable_instance->tableSize);
    if(appendNode(hTable_instance->Hash_structure[key_to_insert], data_to_insert) != success) {
        destroyKeyValuePair(data_to_insert);
        return failure;
    }
    return success;
}

//search for a value that belongs to a specific key in an instance of a hashTable_s
Element lookupInHashTable(hashTable hTable_instance, Element key) {
    if(hTable_instance == NULL || key == NULL) {
        return NULL;
    }
    int key_to_search = (hTable_instance->transformIntoNumber(key)) % (hTable_instance->tableSize);
    Element data =  searchByKeyInList(hTable_instance->Hash_structure[key_to_search], key);
    if(data == NULL) {
        return NULL;
    }

    return getValue(data);
}

//a function that removes a value from an instance of an hashTable_s by a specific key
status removeFromHashTable(hashTable hTable_instance, Element key) {
    if (hTable_instance == NULL || key == NULL) {
        return failure;
    }
    int key_to_delete = (hTable_instance->transformIntoNumber(key)) % hTable_instance->tableSize;
    if(deleteNode(hTable_instance->Hash_structure[key_to_delete], key) == success) {
        return success;
    }
    return failure;
}


//a function that prints out all the data in an instance of a hashTable_s
status displayHashElements(hashTable hTable_instance) {
    if (hTable_instance == NULL) {
        return failure;
    }
    for (int i = 0; i < hTable_instance->tableSize; i++) {
        if(displayList(hTable_instance->Hash_structure[i]) == failure) {
            return failure;
        }
    }
    return success;
}
