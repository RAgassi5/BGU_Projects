Advanced Programming - Assignment #3
Submitted by: Roii Agassi, ID: 207455213
- - - - - - - - - - - - - - - - - - - - - - - -

In this project, I focused on designing generic Abstract Data Types (ADTs) that function as independent, reusable data structures. These ADTs can operate on their own or be combined to work seamlessly together, forming more complex and powerful structures. These ADTs are designed to be flexible and reusable, allowing them to work with a wide range of data types. The project is organized into multiple components, with each file serving a specific purpose and functionality. When combined, these components create a cohesive system capable of efficiently managing and manipulating data.

The main components of the project are:
- Jerry.c 
- Jerry.h
- KeyValuePair.c
- KeyValuePair.h
- LinkedList.c
- LinkedList.h
- HashTable.c
- HashTable.h (given as part of the files for the project)
- HashTable.c
- MultiValueHashTable.c
- MultiValueHashTable.h
-JerryBorreMain.c
- Defs.h 
- makefile 
- ReadMe.txt (this file)


------------------
      Jerry
------------------
In the component of the project, I reused the code I previously wrote for the first assignment with a few minor adjustments. I added a function that compares two different instances of Jerry and a function that shallow copies an instance of a Jerry. In addition, in this file there are functions to create instances of planets, create, add and delete instances of a physical characteristic and many other functionalities regarding instances of Jerry.

------------------
   KeyValuePair
------------------
This component of the project implements a generic ADT for pairs made of keys and values. The implementation of the ADT KeyValuePair is a key part of our project. This ADT is essential for managing pairs of related data, where each key is associated with a specific value. This structure can be widely used to store any type of data as key, and any type of data as value, in my project specifically I use this ADT to implement the functionality of the HashTable and the MultiValueHashTable.

------------------
    LinkedList
------------------
This component of the project implements a generic ADT for a Linked List data structure. The linked list is a fundamental data structure that allows for efficient insertion and deletion of elements. The implementation includes various functionalities to manage the linked list, such as adding, deleting, and retrieving nodes. The linked list has a pointer to the "head" which is the first node in the list, a pointer to the "tail" which is the last node in the list and a variable that maintains the size of the linked list. Each node in the linked list consists of a data segment, that can hold any kind of data that is inserted, depends on the user. Additionally, each node has a pointer to the next node after it in the linked list.

------------------
    HashTable
------------------
In this component of the project I implemented a generic hash table that stores key-value pairs, where each bucket is a linked list. Key functions in the hash table include adding, searching for, and removing key-value pairs, as well as displaying all elements. The hash table is initialized with function pointers to handle the specifics of copying, comparing, printing, and freeing keys and values. The "addToHashTable" function computes a hash index for each key using a hash function that relies on the "transformIntoNumber" function inserted by the user and inserts the corresponding pair into the correct linked list. This function ensures that keys are distributed evenly across the table. The "lookupInHashTable" function searches for a value by key, and the removeFromHashTable function deletes a pair from the appropriate bucket. The table uses linked lists to handle collisions by chaining multiple pairs in the same bucket. The code ensures that the memory for keys and values is properly managed through functions for copying and freeing. This generic ADT of a hash table that I created provides an efficient way to store and retrieve and look for key-value pairs with an average time complexity of O(1).

---------------------
 MultiValueHashTable
---------------------
The MultiValueHashTable is a data structure that extends a standard hash table to allow each key to be associated with multiple values. It works by using a hash table to store keys, where each key maps to a list of values, instead of just a single value. This enables efficient storage and retrieval of multiple values under the same key. When a new key-value pair is added, the table checks if the key already exists. If it does, the new value is added to the existing list of values for that key. If the key doesn't exist, a new list is created and the value is added. The table also allows for easy removal of specific values from a key's list, and if the list for a key becomes empty after removal, the key is deleted entirely. The structure provides functionality to look up the values for a given key, remove values, and display the values associated with a key. Custom operations, such as how keys and values are copied, freed, or printed, can be specified by passing function pointers during the creation of the hash table. This makes the structure flexible and customizable to various needs.

- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

**** Determining Hash Table & Multi-Hash Table Size ****

In the instructions, we were asked to determine the initial size of the structures. After researching and considering different approaches, I decided to calculate the size based on the expected number of objects to be inserted. This includes the total count of Jerry IDs, physical characteristic names, and all other instances. To ensure efficient performance, I divided this total by a load factor of 0.75. I chose 0.75 as the load factor because it helps maintain an average time complexity of O(1) for operations like searching for a specific key or value, minimizing the risk of collisions and performance degradation. Once I had the estimated number of items, I used a function to find the next prime number greater than the calculated value. This approach ensures the size of the hash table is optimal for both space and performance. With the prime number as the size, I then initialized the hash table structures accordingly.

- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

---------------------
  JerryBoreeMain.c
---------------------
This file contains the core functionality of the project. The program begins by reading data from the provided configuration file. During this phase, all instances of planets are stored in an array, while all instances of Jerries are inserted into a linked list. Once the system is initialized, a HashTable is created to map Jerry IDs (as keys) to pointers to their respective instances (as values). Additionally, a MultiValueHashTable is set up where each physical characteristic name (as a key) maps to all instances of Jerries that possess that specific characteristic.After ensuring that the initialization completes without memory-related issues, the program displays a menu to the user. This menu provides nine distinct options, each corresponding to a specific functionality. To enhance readability and maintainability, the code for each menu option is implemented in a separate function, minimizing redundancy and improving clarity.Once the user has explored the menu options and decides to exit, the system performs a thorough cleanup. It deallocates all memory, clears the data, and destroys all data structures used throughout the program. This ensures that the program terminates properly without leaving any memory leaks.

------------------
     Defs.h
------------------
This component of the project serves a critical role in our project by defining and documenting the various functions and methods used throughout the project. This file ensures consistency and clarity, making it easier for users or anyone reviewing my code to understand and utilize the provided functionalities.

Key Components in the Defs.h file:
	
	There are two Enums:

	- status: This enum is used to indicate the outcome of operations. It includes values such as SUCCESS and FAILURE, providing a clear and standardized way to handle and check the results of function calls.

	- bool: This enum represents boolean values, defined as TRUE and FALSE. It is used to facilitate logical operations and condition checks within the project.

	Also there are Function Definitions - the file contains the declarations of functions that each Abstract Data Type (ADT) requires from the user for initialization. By defining these functions in Defs.h, we ensure that all ADTs have a consistent interface and can be easily 		integrated and managed within the project.

-----------------
    makefile
-----------------
The makefile provided in this project outlines all dependencies between the files, ensuring that the build process is streamlined and efficient. It allows any programmer working with the project to compile and use it properly, minimizing setup errors and ensuring consistency across different environments.





