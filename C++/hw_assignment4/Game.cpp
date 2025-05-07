#include <fstream>
#include <iostream>
#include "Game.h"

/*
 *a function that sets up the game
 *the function is responsible to placing each room from the configuration file in its proper array and proper index according to the room's path
 */
void findPath(Room *start, Room *to_add) {
    std::string path = to_add->get_RoomPath();
    Room *current = start;
    int i = 0;
    //transferring chars to their ASCII value to create the proper index in int
    int index = path[i] - '0';
    //if the room already exists, set it as current room
    while (current->findRoom(index) == true) {
        current = (*current)[index];
        ++i;;
        //update the index properly for each search
        if (i < path.length()) {
            index = path[i] - '0';
        } else {
            break;
        }
    }
    //add the room to its proper location
    current->addRoomByIndex(to_add);
}

Game::Game(const std::string &configFile, int PlayerMaxHealth, int PlayerDamage) {
    //create the first room of the Dungeon -> an empty room
    this->StartingRoom = new Room("", 0, 0, 0);
    //at the beginning of the game the first room is also the current room the player is in
    this->CurrentRoom = this->StartingRoom;
    std::ifstream CurrentFile(configFile);
    if (!CurrentFile.is_open()) {
        //there was a problem opening the provided configuration file -> the game can't be set up, so we exit the program
        std::cout << "The provided file: " << configFile << " does not exist. Please enter a valid configuration file."
                << std::endl;
        exit(1);
    }

    std::string CurrentLine;
    while (std::getline(CurrentFile, CurrentLine)) {
        //break the current line to "tokens"
        std::string W = CurrentLine.substr(0, CurrentLine.find(' '));
        CurrentLine.erase(0, CurrentLine.find(' ') + 1);
        std::string X = CurrentLine.substr(0, CurrentLine.find(' '));
        CurrentLine.erase(0, CurrentLine.find(' ') + 1);
        std::string Y = CurrentLine.substr(0, CurrentLine.find(' '));
        CurrentLine.erase(0, CurrentLine.find(' ') + 1);
        std::string Z = CurrentLine;

        int x = std::stoi(X);
        int y = std::stoi(Y);
        int z = std::stoi(Z);

        //create new instance of room according to the data provided in the file
        Room *newRoom = new Room(W, x, y, z);
        //place the new instance of the created room in to its proper location
        findPath(this->CurrentRoom, newRoom);
    }
    //close the file
    CurrentFile.close();
    //create an instance of Entity that represents the player of the game
    this->Player = Entity("Player", PlayerMaxHealth, PlayerDamage);
}

Game::~Game() {
    delete this->StartingRoom;
}

Entity &Game::get_Player() {
    return this->Player;
}

Room *Game::get_CurrentRoom() const {
    return this->CurrentRoom;
}

/*
 *a function that manages the movement between rooms and continuing to the next room
 *this function sets the CurrentRoom in an instance of a game according to the players progress in the game
 */
int Game::set_CurrentRoom() {
    if (this->CurrentRoom->get_roomCount() == 0) {
        std::cout << "The room continues and opens up to the outside. You won against the dungeon" << std::endl;
        return 0;
    }
    if (this->CurrentRoom->get_roomCount() == 1) {
        std::cout << "You see a single corridor ahead of you labeled 0" << std::endl;
    } else {
        std::cout << "You see corridors labeled from 0 to " << (this->CurrentRoom->get_roomCount()) - 1 << "." <<
                " Which one will you choose?" << std::endl;
    }
    int selection;
    std::cin >> selection;
    this->CurrentRoom = (*this->CurrentRoom)[selection];
    return 1;
}


//a function that heals the player that arrived to a room with a campfire
void Feel_The_Heat(Game &game) {
    const int heal = game.get_CurrentRoom()->get_fireCamp();
    game.get_Player() += heal;
    std::cout << "You sit by the campfire and heal " << heal << " health" << std::endl;
}

/*
 *a function that manages a fight in the game between the monster in the room and the player
 *the function prints out the proper messages depending on what going on in the battle
 *
 *Return: returns an int as kind of "status" to know who won the battle  -> "1" means the player won and we can continue the game, "0" means the player lost and we end the game
 */
int Fight_Or_Flight(Game &game) {
    Entity *Monster = game.get_CurrentRoom()->get_MonsterInDaRoom();
    Entity &Player = game.get_Player();
    if (Player > *Monster) {
        std::cout << "You encounter a smaller monster" << std::endl;
    }

    if (Player < *Monster) {
        std::cout << "You encounter a larger monster" << std::endl;
    }

    if (Player == *Monster) {
        std::cout << "You encounter a equally sized monster" << std::endl;
    }

    std::cout << *Monster << std::endl;

    int option_switch = 0;
    //if the battle ends once either the player or the monster dies
    while (Player.is_dead() != true && Monster->is_dead() != true) {
        if (option_switch == 0) {
            *Monster -= Player;
            std::cout << "You deal " << Player.getDamage() << " damage to the monster and leave it with " << Monster->
                    getCurrentHealth() << " health" << std::endl;
            option_switch = 1;
        } else {
            Player -= *Monster;
            std::cout << "The monster deals " << Monster->getDamage() << " damage to you and leaves you with " << Player
                    .getCurrentHealth() << " health" << std::endl;
            option_switch = 0;
        }
    }

    if (Player.is_dead()) {
        std::cout << "You lost to the dungeon" << std::endl;
        return 0;
    } else {
        std::cout << "You defeat the monster and go on with your journey" << std::endl;
        return 1;
    }
}

/*
 *a function that controls the flow of the game
 *first, check if there is no fire camp and no monster -> empty room
 *if there is a camp fire, add health to player through function "Feel_The_Heat"
 *if there is a monster, the player fights the monster using the function "Fight_Or_Flight"
 *
 *Return: the function returns an int, "1" means continue the game, "0" means we need to end the game
 */
int Whats_In_Da_Room(Game &game) {
    std::cout << game.get_Player() << std::endl;

    if (game.get_CurrentRoom()->get_fireCamp() == 0 && game.get_CurrentRoom()->get_MonsterInDaRoom() == nullptr) {
        std::cout << "You arrive to an empty room" << std::endl;
        return 1;
    }

    if (game.get_CurrentRoom()->get_fireCamp() != 0) {
        Feel_The_Heat(game);
    }

    if (game.get_CurrentRoom()->get_MonsterInDaRoom() != nullptr) {
        return Fight_Or_Flight(game);
    }

    return 1;
}

/*
 *a function that handles the beginning of the game, decreases the player's damage by the user's choice
 */
void The_Journey_Begins(Game &game) {
    std::cout << game.get_Player() << std::endl;
    std::cout << "I see you like challenges, by how much do you want to reduce your damage?" << std::endl;
    int lowerBy;
    std::cin >> lowerBy;
    game.get_Player() -= lowerBy;
}

/*
 *this function starts the process of playing the game
 *the function is built from a while loop that continues until either the player won or the player died
 */
void Game::StartGame() {
    The_Journey_Begins(*this);
    while (true) {
        if (Whats_In_Da_Room(*this) == 0) {
            break;
        }
        if (this->set_CurrentRoom() == 0) {
            break;
        }
    }
}
