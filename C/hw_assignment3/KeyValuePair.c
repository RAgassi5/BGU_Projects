#include "KeyValuePair.h"


//create the ADT KeyValuePair
struct KeyValuePair {
    Element key;
    Element value;
    CopyFunction copyKey;
    CopyFunction copyValue;
    FreeFunction freeKey;
    FreeFunction freeValue;
    PrintFunction printKey;
    PrintFunction printValue;
    EqualFunction compareKeys;
};

//a function that creates a new instance of the ADT KeyValuePair
KeyValuePair_Pointer createKeyValuePair(Element key, Element value, CopyFunction copyKey, CopyFunction copyValue,
                                        FreeFunction freeKey, FreeFunction freeValue, PrintFunction printKey,
                                        PrintFunction printValue,
                                        EqualFunction compareFunction) {
    if (key == NULL || value == NULL || copyKey == NULL || copyValue == NULL || freeKey == NULL || freeValue == NULL ||
        printKey == NULL
        || printValue == NULL ||
        compareFunction == NULL) {
        return NULL;
    }
    KeyValuePair_Pointer new_KeyValuePair = malloc(sizeof(struct KeyValuePair));
    if (new_KeyValuePair == NULL) {
        return NULL;
    }


    new_KeyValuePair->key = copyKey(key);
    if (new_KeyValuePair->key == NULL) {
        free(new_KeyValuePair);
        return NULL;
    }
    new_KeyValuePair->value = copyValue(value);
    if (new_KeyValuePair->value == NULL) {
        free(new_KeyValuePair);
        return NULL;
    }
    new_KeyValuePair->copyKey = copyKey;
    new_KeyValuePair->copyValue = copyValue;
    new_KeyValuePair->freeKey = freeKey;
    new_KeyValuePair->freeValue = freeValue;
    new_KeyValuePair->printKey = printKey;
    new_KeyValuePair->printValue = printValue;
    new_KeyValuePair->compareKeys = compareFunction;
    return new_KeyValuePair;
}

//a function that deletes the instance of the ADT KeyValuePair
status destroyKeyValuePair(KeyValuePair_Pointer instance_of_KeyValuePair) {
    if (instance_of_KeyValuePair == NULL) {
        return failure;
    }
    instance_of_KeyValuePair->freeKey(instance_of_KeyValuePair->key);
    instance_of_KeyValuePair->freeValue(instance_of_KeyValuePair->value);
    free(instance_of_KeyValuePair);
    return success;
}

//a function that prints out the key of an KeyValuePair instance
status displayKey(KeyValuePair_Pointer instance_of_KeyValuePair) {
    if (instance_of_KeyValuePair == NULL) {
        return failure;
    }
    instance_of_KeyValuePair->printKey(instance_of_KeyValuePair->key);
    return success;
}

//a function that prints out the value of an KeyValuePair instance
status displayValue(KeyValuePair_Pointer instance_of_KeyValuePair) {
    if (instance_of_KeyValuePair == NULL) {
        return failure;
    }
    instance_of_KeyValuePair->printValue(instance_of_KeyValuePair->value);
    return success;
}

//a function that returns the value of the Key of an KeyValuePair instance
Element getKey(KeyValuePair_Pointer instance_of_KeyValuePair) {
    if (instance_of_KeyValuePair == NULL) {
        return NULL;
    }
    Element key_value = instance_of_KeyValuePair->copyKey(instance_of_KeyValuePair->key);
    return key_value;
}

//a function that returns the value of the Value of an KeyValuePair instance
Element getValue(KeyValuePair_Pointer instance_of_KeyValuePair) {
    if (instance_of_KeyValuePair == NULL) {
        return NULL;
    }
    Element value_value = instance_of_KeyValuePair->copyValue(instance_of_KeyValuePair->value);
    return value_value;
}

//a bool function that checks whether a given value is equal to the key of the KeyValuePair instance
bool isEqualKey(KeyValuePair_Pointer instance_of_KeyValuePair, Element other_key) {
    if (instance_of_KeyValuePair == NULL || other_key == NULL) {
        return false;
    }
    bool comparison = instance_of_KeyValuePair->compareKeys(instance_of_KeyValuePair->key, other_key);
    return comparison;
}

//a function that prints out all the data of an instance of a KeyValuePair
status displayPair(KeyValuePair_Pointer instance_of_KeyValuePair) {
    if (instance_of_KeyValuePair == NULL) {
        return failure;
    }
    instance_of_KeyValuePair->printKey(instance_of_KeyValuePair->key);

    instance_of_KeyValuePair->printValue(instance_of_KeyValuePair->value);
    return success;
}