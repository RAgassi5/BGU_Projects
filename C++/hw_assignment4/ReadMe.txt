Homework assignment #4 in CPP - Advanced Programming 
Submitted by: Roii Agassi, ID: 207455213

My project consists of the following files:
- Entity.cpp
- Entity.h
- Room.cpp
- Room.h
- Game.cpp
- Game.h
- main.cpp
- makefile 
- ReadMe.txt (this file)

Project Overview:

1. ** Entity.cpp / Entity.h **  

   - Implements the `Entity` class to represent entities in the game, such as monsters or the current player of the game.  
   - Each instance of Entity has attributes like name, health, and damage.  
   - The implementation of the class has custom constructors, copy constructors, and operator overloads for functionality as well as getter functions.  

2. ** Room.cpp / Room.h **  

   - Implements the `Room` class, which represents a room in the dungeon.  
   - Each instance has connections to other rooms (`roomList`) and attributes like room ID, fire camp and a pointer to a monster if there is one in that specific room.  
   - The implementation of the class consists of methods to add rooms by index, navigate between rooms, manage dynamic memory, getter functions and setter functions  

3. ** Game.cpp / Game.h **  

   - Implements the `Game` class, which creates the dungeon and manages room navigation, gameplay mechanics, and interactions.  
   - The implementation of the class handles reading a file and retrieving data to set up the game while using  the `findPath` function, which ensures rooms are placed in their correct positions based on paths. 
   - The class has many functions in it in order to control the game flow and continuation of the game until either the player wins the dungeon or dies :( 

4. ** main.cpp **  

   - Contains the main function to initialize the game, set up the dungeon, and simulate interactions.  

5. ** makefile **  

   - A build system file that automates the compilation process.  
   - Includes targets for building the project, cleaning up binaries, and running the executable.  

6. ** ReadMe.txt **  

   - This file, which provides an overview of the project, its structure, and its purpose.

------------------------
## Memory Management:
------------------------

All dynamic memory allocation in this project were carefully handled to avoid memory leaks.
The destructor methods in `Room`,`Entity` and `Game` ensure proper cleanup of dynamically allocated resources.
- The destructor of Room is responsible to delete any allocated memory that belongs to the dynamic room arrays that are in each instance of a Room
- The destructor of Game is responsible to delete all the data that belongs to the game, this is handled by maintaining a pointer to the first room of the dungeon. This way, no matter when the player finishes the game we have access to the entire memory that belongs to the game and we are able to delete all data.