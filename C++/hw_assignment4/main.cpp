#include <iostream>
#include "Game.h"

int main(int argc, char *argv[]) {
    //initialize the "Dungeon" using the Game's constructor
    Game Dungeon(argv[3], std::stoi(argv[1]), std::stoi(argv[2]));
    //start playing the "Dungeon"
    Dungeon.StartGame();
}
