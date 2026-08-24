# JerryBoree

**Advanced Programming — Assignment #3 (372-1-2102)**  
Ben-Gurion University

JerryBoree is a C project that builds a Jerry daycare management system on top of several **generic Abstract Data Types (ADTs)**. The project focuses on modular design, reusable generic data structures, function pointers, hashing, dynamic memory management, and efficient lookup operations.

The system loads planets and Jerries from a configuration file and then allows the user to add, search, update, remove, display, and interact with Jerries through an interactive menu.

## Project Objectives

The assignment is centered around implementing and combining four generic data structures:

- `LinkedList`
- `KeyValuePair`
- `HashTable`
- `MultiValueHashTable`

These ADTs are implemented independently of the JerryBoree application and are then combined in `JerryBoreeMain.c` to provide efficient management of Jerries.

## Project Structure

```text
.
├── Jerry.c
├── Jerry.h
├── KeyValuePair.c
├── KeyValuePair.h
├── LinkedList.c
├── LinkedList.h
├── HashTable.c
├── HashTable.h
├── MultiValueHashTable.c
├── MultiValueHashTable.h
├── JerryBoreeMain.c
├── Defs.h
├── makefile
└── README.md
```

> `HashTable.h` was provided as part of the assignment and is used without modifying its required interface.

## Components

### Jerry Module

`Jerry.c` and `Jerry.h` contain the Jerry-related data model and operations reused from the previous assignment, with adjustments required for JerryBoree.

The module manages:

- Planets
- Origins
- Physical characteristics
- Jerries
- Jerry creation and destruction
- Adding and removing physical characteristics
- Printing Jerry and planet information
- Jerry comparison
- Shallow-copy functionality used by the generic data structures

The Jerry module remains separate from the daycare application's menu and data-structure logic.

### LinkedList

`LinkedList.c` and `LinkedList.h` implement a generic singly linked list.

The list maintains:

- A head pointer
- A tail pointer
- The current list size
- Generic data in each node
- A pointer from each node to the next node

Supported operations include:

- Creating and destroying a list
- Appending nodes
- Deleting nodes
- Displaying the list
- Retrieving an element by index
- Getting the list length
- Searching for an element by a key or partial value

The implementation operates on generic `Element` values and uses function pointers supplied by the user of the ADT.

### KeyValuePair

`KeyValuePair.c` and `KeyValuePair.h` implement a generic key-value pair.

A key and its associated value may be of different data types. The ADT supports operations such as:

- Creating and destroying a pair
- Retrieving the key
- Retrieving the value
- Displaying the key
- Displaying the value
- Comparing a stored key with another key

This ADT is used internally by the hash-table implementation.

### HashTable

`HashTable.c` implements a generic hash table in which every key maps to a single value.

The implementation uses:

- `KeyValuePair` objects to represent mappings
- `LinkedList` buckets
- Separate chaining for collision handling
- User-provided function pointers for copying, comparing, printing, freeing, and transforming keys

Main operations include:

- Creating and destroying the table
- Adding key-value mappings
- Looking up values by key
- Removing mappings
- Displaying stored elements

For normal lookup, insertion, and removal operations, the design targets **average O(1)** performance.

### MultiValueHashTable

`MultiValueHashTable.c` and `MultiValueHashTable.h` implement a generic hash table in which one key can be associated with multiple values.

Instead of mapping a key directly to one value, each key maps to a collection of values.

The structure is built using the previously implemented generic ADTs, particularly `HashTable`, rather than duplicating hash-table logic.

Supported functionality includes:

- Adding a value under a key
- Looking up all values associated with a key
- Removing a specific value
- Automatically removing a key when its value collection becomes empty
- Displaying all values associated with a key

In JerryBoree, this structure is used to efficiently map a **physical characteristic name** to all Jerries that possess that characteristic.

## Hash Table Sizing

The hash-table sizes are chosen according to the expected number of stored elements.

The implementation estimates the required capacity, applies a load factor of approximately `0.75`, and then finds the next suitable prime number for the table size.

This approach is intended to reduce collisions and preserve efficient average-case hash-table operations.

## JerryBoreeMain.c

`JerryBoreeMain.c` contains the main application logic.

During initialization, the program:

1. Reads the configuration file.
2. Creates the known planets.
3. Creates the initial Jerries.
4. Stores the Jerries in insertion order.
5. Builds a hash table mapping each Jerry ID to its Jerry object.
6. Builds a multi-value hash table mapping physical-characteristic names to the Jerries that possess them.

The same Jerry object may be referenced by multiple data structures. The program does **not** create unnecessary duplicate Jerry objects.

The menu operations are separated into helper functions to keep the main logic readable and modular.

## Building

A `makefile` is included with the project.

Build the executable with:

```bash
make
```

The expected executable is:

```text
JerryBoree
```

To remove generated build files, use the cleanup target defined by the supplied makefile, if applicable:

```bash
make clean
```

## Running

Run the program using:

```bash
./JerryBoree <numberOfPlanets> <configurationFile>
```

Example:

```bash
./JerryBoree 4 configuration.txt
```

Arguments:

- `numberOfPlanets` — number of planets listed in the configuration file
- `configurationFile` — path to the configuration file

Unlike the previous assignment, the number of Jerries is **not** supplied as a command-line argument.

## Configuration File

The input file contains a `Planets` section followed by a `Jerries` section.

Example:

```text
Planets
Earth,123.1,8392,99.2
Gaia,983.223,8521,2312
Gazorpazorp,85.1,555.5,312
Pluto,3454.21,124.112,985.445
Jerries
23dF21,C-137,Earth,50
	Height:166.2
	LimbsNumber:4
	Weight:80
1q456,C-455,Earth,10
S5d2,V-234,Gaia,99
	Age:50
6e45,B-344,Pluto,34
	LimbsNumber:2
```

Each Jerry entry contains:

```text
<ID>,<dimension>,<planet>,<happiness>
```

Physical characteristics belonging to that Jerry appear immediately below it as tab-indented lines:

```text
	<characteristic>:<value>
```

Each planet is created only once. Jerries originating from the same planet reference the same `Planet` object.

## Main Menu

After initialization, the program displays:

```text
Welcome Rick, what are your Jerry's needs today ?
1 : Take this Jerry away from me
2 : I think I remember something about my Jerry
3 : Oh wait. That can't be right
4 : I guess I will take back my Jerry now
5 : I can't find my Jerry. Just give me a similar one
6 : I lost a bet. Give me your saddest Jerry
7 : Show me what you got
8 : Let the Jerries play
9 : I had enough. Close this place
```

### 1. Add a Jerry

Adds a new Jerry to the daycare.

The program verifies that:

- The Jerry ID is not already registered
- The requested planet exists

After receiving the dimension and happiness level, the new Jerry is inserted into the system.

Jerry-ID existence checks are performed using the hash table for average **O(1)** lookup.

### 2. Add a Physical Characteristic

Adds a new physical characteristic to an existing Jerry.

If the characteristic does not already exist for that Jerry, it is:

- Added to the Jerry
- Added to the characteristic-based `MultiValueHashTable`

The system can then efficiently access all Jerries that share that characteristic.

### 3. Remove a Physical Characteristic

Removes an existing physical characteristic from a Jerry.

The relevant indexes are updated as well, while the order of the Jerry's remaining characteristics is preserved.

### 4. Take Back a Jerry

Removes a specific Jerry from the daycare by ID.

Before freeing the Jerry, all references to it are removed from the data structures that index it.

### 5. Find the Most Similar Jerry

Allows a Rick who does not remember his Jerry's ID to provide:

- A physical-characteristic name
- The remembered value of that characteristic

The program examines Jerries with that characteristic and chooses the Jerry whose value has the smallest absolute difference from the requested value.

If multiple Jerries are equally close, the Jerry that entered the relevant ordering first is selected.

The selected Jerry is returned and removed from the daycare.

### 6. Take the Saddest Jerry

Finds the Jerry with the lowest happiness level.

If multiple Jerries have the same minimum happiness, the Jerry that has been in the daycare longer is selected.

The selected Jerry is printed, removed from the system, and returned.

### 7. Display Information

Provides a secondary information menu:

```text
1 : All Jerries
2 : All Jerries by physical characteristics
3 : All known planets
```

This can be used to display:

- Every Jerry currently in the daycare
- Jerries associated with a requested physical characteristic
- All known planets

### 8. Let the Jerries Play

Starts one of the available daycare activities:

```text
1 : Interact with fake Beth
2 : Play golf
3 : Adjust the picture settings on the TV
```

The selected activity updates Jerry happiness levels according to the assignment rules, after which the current Jerries are displayed.

### 9. Close JerryBoree

Terminates the daycare.

Before exiting, the program destroys the allocated data structures and releases all dynamically allocated memory.

## Data-Structure Design

The application combines the generic ADTs as follows:

```text
                           +----------------------+
                           |    JerryBoreeMain    |
                           +----------+-----------+
                                      |
                  +-------------------+-------------------+
                  |                                       |
                  v                                       v
        +-------------------+                  +----------------------+
        |     HashTable     |                  | MultiValueHashTable  |
        | Jerry ID -> Jerry |                  | Characteristic ->    |
        +---------+---------+                  | List of Jerries      |
                  |                            +----------+-----------+
                  v                                       |
        +-------------------+                             v
        |   KeyValuePair    |                    +----------------+
        +---------+---------+                    |   HashTable    |
                  |                              +-------+--------+
                  v                                      |
        +-------------------+                            v
        |    LinkedList     |                    +----------------+
        +-------------------+                    |  Linked Lists  |
                                                 +----------------+
```

This layered design keeps the generic containers independent from Jerry-specific logic.

## Complexity Considerations

The design follows the assignment's efficiency requirements.

Important operations include:

- Jerry lookup by ID: **average O(1)**
- Hash-table insertion/removal/lookup: **average O(1)**
- Characteristic lookup through `MultiValueHashTable`: **average O(1)** to obtain the associated collection
- Operations that must process every Jerry with a specific characteristic necessarily scale with the number of matching Jerries and their physical characteristics

## Generic Programming

The ADTs are generic and therefore do not assume the concrete type of the stored data.

Behavior is provided through function pointers declared through the project's common definitions, including operations for:

- Copying
- Freeing
- Comparing
- Printing
- Transforming a key into a numeric value for hashing

This allows the same data structures to be reused with types unrelated to JerryBoree.

## Memory Management

Dynamic memory management is a major part of the project.

The implementation is designed so that:

- Every allocated structure has a corresponding destruction path.
- Hash-table buckets and linked-list nodes are released correctly.
- Removing a Jerry also removes references to that Jerry from the relevant indexes.
- Shared objects are not unnecessarily duplicated.
- All structures are destroyed before program termination.
- Allocation failures are handled through the project's status/error mechanisms.

Because some objects are referenced from more than one data structure, ownership is handled carefully to avoid both memory leaks and double frees.

## Memory Leak Testing

Valgrind can be used to test the executable:

```bash
valgrind --leak-check=full --show-leak-kinds=all \
./JerryBoree 4 configuration.txt
```

All menu paths should be tested, especially operations that add or remove Jerries and physical characteristics.

## Notes

- Jerry IDs are unique.
- Planet objects are shared between Jerries from the same planet.
- Searches rely on exact names as defined by the assignment.
- Generic ADTs interact only through their public interfaces.
- Hash-table collisions are handled using separate chaining.
- Output formatting is significant because the assignment is also evaluated automatically.
- The project emphasizes clean separation of responsibility between modules.

## Author

**Roii Agassi**

## Course

**Advanced Programming — 372-1-2102**  
Ben-Gurion University
