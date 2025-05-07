#include <iostream>
#include "Game.h"
#include "GameException.h"

int main(int argc, char *argv[]) {
    //try running the Dungeon, if an exception is thrown -> exit and clean
    try {
        //initialize the "Dungeon" using the Game's constructor
        Game Dungeon(argv[4], argv[1], std::stoi(argv[2]), std::stoi(argv[3]));
        //start playing the "Dungeon"
        Dungeon.StartGame();
    } catch (const GameException &Error) {
        std::cout << Error.what() << std::endl;
        std::exit(EXIT_FAILURE);
    }
}
