#include "LinkedList.h"

//a construction of a Node
typedef struct Node_s {
    struct Node_s *next;
    Element data;
} Node;

//a function that creates a new instance of a Node
Node *createNode(Element data) {
    Node *node = (Node *) malloc(sizeof(Node));
    if (node == NULL) {
        return NULL;
    }
    node->data = data;
    node->next = NULL;
    return node;
}


//construct an ADT of a LinkedList
struct LinkedList {
    Node *head;
    Node *tail;
    int size;
    FreeFunction freeData;
    PrintFunction printData;
    EqualFunction compareFunction;
};

//a function that creates an instance of an ADT LinkedList
ListPointer createLinkedList(FreeFunction freeData, PrintFunction printData,
                             EqualFunction compareFunction) {
    if (freeData == NULL || printData == NULL || compareFunction == NULL) {
        return NULL;
    }
    ListPointer list = malloc(sizeof(struct LinkedList));
    if (list == NULL) {
        return NULL;
    }
    list->freeData = freeData;
    list->printData = printData;
    list->compareFunction = compareFunction;
    list->head = NULL;
    list->tail = NULL;
    list->size = 0;
    return list;
}

//a function that deletes all the data in an instance of a LinkedList including the ADT itself
status destroyLinkedList(ListPointer list_instance) {
    if (list_instance == NULL) {
        return failure;
    }

    Node *current = list_instance->head;
    while (current != NULL) {
        Node *next = current->next;
        list_instance->freeData(current->data);
        free(current);
        current = next;
    }
    free(list_instance);
    return success;
}

//a function that adds a new node to the end of an instance of LinkedList
status appendNode(ListPointer list_instance, Element data_to_insert) {
    if (list_instance == NULL) {
        return failure;
    }
    Node *newNode = createNode(data_to_insert);
    if (newNode == NULL) {
        return failure;
    }

    if (list_instance->size == 0) {
        list_instance->head = newNode;
        list_instance->tail = newNode;
        list_instance->size++;
        return success;
    } else {
        Node *current = list_instance->tail;
        current->next = newNode;
        list_instance->tail = newNode;
        list_instance->size++;
        return success;
    }
}

//a function that locates a specific node containing the requested data and deletes it
status deleteNode(ListPointer list_instance, Element data_to_delete) {
    if (list_instance == NULL) {
        return failure;
    }
    Node *current = list_instance->head;
    Node *previous = NULL;

    while (current != NULL) {
        //if we found the data we want to delete, we delete it and then reset the head/tail of the list properly
        if (list_instance->compareFunction(current->data, data_to_delete) == true) {
            list_instance->freeData(current->data);
            if (current == list_instance->head) {
                list_instance->head = current->next;
                if (list_instance->head == NULL) {
                    list_instance->tail = NULL;
                }
            }
            if (current == list_instance->tail) {
                if (previous != NULL) {
                    list_instance->tail = previous;
                    previous->next = NULL;
                }
            } else {
                if (previous != NULL) {
                    previous->next = current->next;
                }
            }
            list_instance->size--;
            free(current);
            return success;
        }
        //if the data in the node isn't the data we are looking for, we continue to the next node
        previous = current;
        current = current->next;
    }
    return failure;
}

//a function that prints out an instance of a LinkedList
status displayList(ListPointer list_instance) {
    if (list_instance == NULL) {
        return failure;
    }
    Node *current = list_instance->head;
    while (current != NULL) {
        if (list_instance->printData(current->data) == failure) {
            return failure;
        }

        current = current->next;
    }
    return success;
}

//a function that returns the data of a node that is in the given index
Element getDataByIndex(ListPointer list_instance, int index) {
    if (list_instance == NULL) {
        return NULL;
    }
    if (index < 1 || index > list_instance->size) {
        return NULL;
    }

    int i = 1;
    Node *current = list_instance->head;
    while (i < index) {
        current = current->next;
        i++;
    }
    return current->data;
}

//a function that returns the length of an instance of a LinkedList
int getLengthList(ListPointer list_instance) {
    if (list_instance == NULL) {
        return -1;
    }
    return list_instance->size;
}


//a function that returns a specific data from an instance of a LinkedList by key
Element searchByKeyInList(ListPointer list_instance, Element key) {
    if (list_instance == NULL) {
        return NULL;
    }
    Node *current = list_instance->head;
    while (current != NULL) {
        if (list_instance->compareFunction(current->data, key) == true) {
            return current->data;
        }
        current = current->next;
    }
    return NULL;
}
