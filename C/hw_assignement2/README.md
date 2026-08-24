# Jerry Management System

Homework Assignment #2 for **Advanced Programming (372-1-2102)** at Ben-Gurion University.

This project implements a small Jerry management system in C. The program reads planets and Jerries from a configuration file, stores them using dynamically allocated structures, and provides an interactive menu for querying and modifying the data.

## Project Goals

The assignment focuses on:

- Modular C programming
- `struct` design
- Pointers and dynamic memory allocation
- Correct ownership and cleanup of allocated memory
- Reading and parsing configuration files
- Separation between the Jerry module and the main management program
- Exact output formatting

## Project Structure

```text
.
├── Jerry.h
├── Jerry.c
├── ManageJerries.c
├── Defs.h
└── README.md
```

### `Jerry.h`

Declares the data structures and public interface of the Jerry module.

The module works with the following main structures:

- **Planet** — a uniquely named planet with `x`, `y`, and `z` coordinates.
- **Origin** — describes the dimension and planet from which a Jerry originates.
- **PhysicalCharacteristics** — stores the name and numeric value of a physical characteristic.
- **Jerry** — stores a unique ID, happiness level, origin, and a dynamically allocated collection of physical characteristics.

### `Jerry.c`

Implements the functions declared in `Jerry.h`, including creation, destruction, printing, and manipulation of the project structures.

The module is responsible for operations such as:

- Creating and destroying planets
- Creating and destroying origins
- Creating and destroying Jerries
- Creating physical characteristics
- Adding a physical characteristic to a Jerry
- Removing a physical characteristic from a Jerry
- Checking whether a Jerry has a specific characteristic
- Printing Jerry and planet information

### `Defs.h`

Contains shared definitions used throughout the project, including status and Boolean types.

For example:

```c
typedef enum e_bool {
    false,
    true
} bool;

typedef enum e_status {
    success,
    failure
} status;
```

### `ManageJerries.c`

Contains the main program and the interactive Jerry management system.

Its responsibilities include:

- Reading and parsing the configuration file
- Creating the initial planets and Jerries
- Managing the program menu
- Searching for Jerries and planets
- Calling the Jerry module through its public interface
- Handling program-level errors
- Releasing all allocated resources before termination

The configuration parsing logic is handled by helper functions such as `read_configFile`, while the rest of the helper functions support the different menu operations.

## Building the Project

Compile the program using a C compiler such as GCC.

Example:

```bash
gcc -Wall -Wextra -o ManageJerries ManageJerries.c Jerry.c
```

If a course-provided `Makefile` is available, use it instead.

## Running the Program

The program receives three command-line arguments:

```bash
./ManageJerries <numberOfPlanets> <numberOfJerries> <configurationFile>
```

Example:

```bash
./ManageJerries 3 4 configuration.txt
```

Where:

- `numberOfPlanets` — number of planets in the configuration file
- `numberOfJerries` — number of Jerries in the configuration file
- `configurationFile` — path to the configuration file

## Configuration File Format

The configuration file contains a `Planets` section followed by a `Jerries` section.

Example:

```text
Planets
Earth,123.1,8392,99.2
Gaia,983.223,8521,2312
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

Each planet is stored only once in the system. Multiple Jerries originating from the same planet reference the same `Planet` object.

## Interactive Menu

After loading the configuration file, the program provides the following operations:

```text
1 : Print all Jerries
2 : Print all Planets
3 : Add physical characteristic to Jerry
4 : Remove physical characteristic from Jerry
5 : Print Jerries by a planet
6 : Print Jerries by a physical characteristic
7 : Go home
```

### Menu Operations

1. **Print all Jerries**  
   Prints all Jerries in the same order in which they appeared in the configuration file.

2. **Print all Planets**  
   Prints all known planets in configuration-file order.

3. **Add physical characteristic to Jerry**  
   Finds a Jerry by ID and adds a new physical characteristic if it does not already exist.

4. **Remove physical characteristic from Jerry**  
   Removes a characteristic from a Jerry while preserving the order of the remaining characteristics.

5. **Print Jerries by a planet**  
   Prints all Jerries originating from a requested planet.

6. **Print Jerries by a physical characteristic**  
   Prints all Jerries containing a requested characteristic.

7. **Go home**  
   Releases all dynamically allocated memory and exits the program.

## Memory Management

Dynamic memory management is a central requirement of this assignment.

The implementation is designed around the following principles:

- Every dynamically created structure is allocated through its corresponding creation function.
- Every structure is released through its corresponding destruction function.
- Allocation results are checked before use.
- Characteristic arrays contain only the memory currently required.
- Adding a characteristic expands the array only as needed.
- Removing a characteristic shrinks the array while preserving the remaining order.
- Strings are stored using only the memory necessary for their actual contents.
- All allocated memory is released before program termination.

If a memory allocation fails, the program attempts to clean up existing resources and exits gracefully.

## Memory Leak Testing

The assignment is intended to be tested with tools such as Valgrind.

Example:

```bash
valgrind --leak-check=full --show-leak-kinds=all ./ManageJerries 3 4 configuration.txt
```

All menu paths should be tested to ensure that no allocated memory is lost.

## Important Implementation Notes

- Jerry IDs are unique.
- Planet names are unique.
- Planet objects are shared rather than duplicated.
- Searches for Jerry IDs, planet names, and characteristic names are case-sensitive.
- Physical characteristics are kept in insertion order.
- When a characteristic is deleted, the relative order of all remaining characteristics is preserved.
- The program assumes that the configuration file and command-line counts are valid, as specified by the assignment.
- Output formatting must match the assignment specification exactly because the project is evaluated using automated tests.

## Modularity

The project intentionally separates domain logic from application logic.

`Jerry.c` and `Jerry.h` implement the reusable Jerry module, while `ManageJerries.c` handles configuration parsing, user interaction, and the main menu. This separation allows the Jerry module to remain independent of the specific menu implementation and makes it reusable by future assignments.

## Author

Roii Agassi

## Course

**Advanced Programming — 372-1-2102**  
Department of Industrial Engineering and Management / Information Systems Engineering  
Ben-Gurion University
