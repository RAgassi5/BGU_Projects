#ifndef LINKEDLIST_H
#define LINKEDLIST_H
#include "Defs.h"

typedef struct LinkedList *ListPointer;

//a function that creates an instance of a LinkedList
ListPointer createLinkedList(FreeFunction freeData, PrintFunction printData,
                             EqualFunction compareFunction);

//a function that deletes an instance of a LinkedList and all its data
status destroyLinkedList(ListPointer list_instance);

//a function that adds a new node to the end of the linked list
status appendNode(ListPointer list_instance, Element data_to_insert);

//a function that deletes a specific a node from the linked list
status deleteNode(ListPointer list_instance, Element data_to_delete);

//a function that prints out an instance of a LinkedList
status displayList(ListPointer list_instance);

//a function that returns the data of a node located in the index i
Element getDataByIndex(ListPointer list_instance, int index);

//a function that returns the size of an instance of a LinkedList
int getLengthList(ListPointer list_instance);

//a function that searches for a node in a linked list by specific key
Element searchByKeyInList(ListPointer list_instance, Element key);


#endif //LINKEDLIST_H
