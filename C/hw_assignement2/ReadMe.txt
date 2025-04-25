Homework assignment #2 - Advanced Programming 
Submitted by: Roii Agassi, ID: 207455213

This assignment consists of five files:
- Jerry.h
- Jerry.c
- ManageJerrries.c
- Defs.h
- ReadMe.txt (this file)

General Overview of my submission:

Throughout the entire assignment and while writing my code, I kept the principle of modularity in mind, aiming to follow its principles by ensuring that each file has a specific functionality and operates independently of other files. 
Here is a brief description and functionality of each of my files:

Jerry.h:
This header file declares all the structs used in the project and all the functions that belong to all the different structs.

Jerry.c:
This C file contains all the implementations of all of the declared functions in Jerry.h

Defs.h:
This file is used for a definitions file, it declares two enum types to provide information about the functions. For example it helps to determine wether a function succeeded or failed or in case of a bool function - true/false

ManageJerries.c:
The main purpose of this file is to implement the menu system that manages the Jerries. It consists of several helper functions and concludes with the main function. One of the key functions in this file is read_configFile, which reads a configuration file containing all the system information and interprets and integrates it into the program. Additionally, there are other functions that assist in implementing the menu's full functionality.

ReadMe.txt:
This text file provides a general explanation of the project structure and its components.




