#ifndef GAME_H
#define GAME_H
#include "Room.h"

class Game {
private:
    //holds a pointer to the current room
    Room * CurrentRoom;
    //holds a pointer to the room where the game started
    Room * StartingRoom;
    //an instance of the player for the current game
    Entity Player;

public:
    //constructor for Game
    Game(const std::string &configFile, int PlayerMaxHealth, int PlayerDamage);

    //destructor
    ~Game();

    //a function that starts the game
    void StartGame();

    //a function that returns a reference to the Player of the game
    Entity & get_Player();

    //a getter function that gets the pointer to the current room
    Room * get_CurrentRoom() const;

    //a setter function that sets the current room according to the user's selections
    int set_CurrentRoom();
};


#endif //GAME_H