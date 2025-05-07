#include <fstream>
#include <iostream>
#include "Game.h"
#include "GameException.h"
#include "Fighter.h"
#include "Sorcerer.h"


/*
 *a function that sets up the game
 *the function is responsible to placing each room from the configuration file in its proper array and proper index according to the room's path
 */
void findPath(Room *start, Room *to_add) {
    //put the whole function in try and catch in case the function can't append the room correctly, clean and rethrow an exception
    try {
        std::string path = to_add->get_RoomPath();
        Room *current = start;
        int i = 0;
        //transferring chars to their ASCII value to create the proper index in int
        int index = path[i] - '0';
        //if the room already exists, set it as current room
        while (current->findRoom(index) == true) {
            current = (*current)[index];
            ++i;
            //update the index properly for each search
            if (i < path.length()) {
                index = path[i] - '0';
            } else {
                break;
            }
        }
        //if the loop stopped earlier than the desired location, throw an exception
        if (i != (path.length() - 1)) {
            throw RoomException();
        }
        //add the room to its proper location
        current->addRoomByIndex(to_add);
    } catch (RoomException &Error) {
        delete to_add;
        throw;
    }
}

Game::Game(const std::string &configFile, const std::string &type, int PlayerMaxHealth, int PlayerDamage) {
    //if the given arguments are negative, throw an exception
    if (PlayerMaxHealth < 0 || PlayerDamage < 0) {
        throw ValueException();
    }

    //create the first room of the Dungeon -> an empty room
    this->StartingRoom = new(std::nothrow) Room("", 0, "", 0, 0);
    //if memory allocation failed, throw an exception
    if (this->StartingRoom == nullptr) {
        throw MemoryException();
    }
    //at the beginning of the game the first room is also the current room the player is in
    this->CurrentRoom = this->StartingRoom;
    std::ifstream CurrentFile(configFile);
    //if the provided file isn't found, throw an exception
    if (!CurrentFile.is_open()) {
        throw FileNotFoundException();
    }

    std::string CurrentLine;
    while (std::getline(CurrentFile, CurrentLine)) {
        //break the current line to "tokens"
        std::string W = CurrentLine.substr(0, CurrentLine.find(' '));
        CurrentLine.erase(0, CurrentLine.find(' ') + 1);
        std::string X = CurrentLine.substr(0, CurrentLine.find(' '));
        CurrentLine.erase(0, CurrentLine.find(' ') + 1);
        std::string MON = CurrentLine.substr(0, CurrentLine.find(' '));
        MON.erase(MON.find_last_not_of(" \n\r") + 1);
        int camp = std::stoi(X);
        //check that there are no invalid values, if there are -> throw exception
        if (camp < 0) throw ValueException();
        //if there is no monster in the room -> we set the value to 0
        int y = 0;
        int z = 0;

        //if MON isn't equal "N", it means there is a monster in the room, therefore we keep reading the line and set the room properly
        if (MON != "N") {
            CurrentLine.erase(0, CurrentLine.find(' ') + 1);
            std::string Y = CurrentLine.substr(0, CurrentLine.find(' '));
            CurrentLine.erase(0, CurrentLine.find(' ') + 1);
            std::string Z = CurrentLine;

            y = std::stoi(Y);
            z = std::stoi(Z);
            //check that there are no invalid values, if there are -> throw exception
            if (y < 0 || z < 0) throw ValueException();
        }


        //create new instance of room according to the data provided in the file
        Room *newRoom = new(std::nothrow) Room(W, camp, MON, y, z);
        //if memory allocation for a new room failed, throw an exception
        if (newRoom == nullptr) {
            throw MemoryException();
        }

        //place the new instance of the created room in to its proper location
        findPath(this->CurrentRoom, newRoom);
    }
    //close the file
    CurrentFile.close();
    //create an instance of Entity that represents the player of the game
    if (type == "F") {
        //try allocating space for an instance of Fighter, if failed throw an exception
        try {
            this->CurrentPlayer = new Fighter(PlayerMaxHealth, PlayerDamage);
        } catch (const std::bad_alloc &Error) {
            throw MemoryException();
        }
    } else if (type == "S") {
        //try allocating space for an instance of Sorcerer, if failed throw an exception
        try {
            this->CurrentPlayer = new Sorcerer(PlayerMaxHealth, PlayerDamage);
        } catch (const std::bad_alloc &Error) {
            throw MemoryException();
        }
        //if a different kind of letter was inserted, throw invalid player exception
    } else {
        throw PlayerException();
    }
}

Game::~Game() {
    delete this->StartingRoom;
    delete this->CurrentPlayer;
}

Player &Game::get_Player() const {
    return *this->CurrentPlayer;
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
    this->CurrentPlayer->set_Specialty();
    return 1;
}


//a function that heals the player that arrived to a room with a campfire
void Feel_The_Heat(const Game &game) {
    const int heal = game.get_CurrentRoom()->get_fireCamp();
    game.get_Player() += heal;
    game.get_Player().set_Specialty();
    std::cout << "You sit by the campfire and heal " << heal << " health" << std::endl;
}

/*
 *a function that manages a fight in the game between the monster in the room and the player
 *the function prints out the proper messages depending on what going on in the battle
 *
 *Return: returns an int as kind of "status" to know who won the battle  -> "1" means the player won, and we can continue the game, "0" means the player lost and we end the game
 */
int Fight_Or_Flight(const Game &game) {
    Monster *monster = game.get_CurrentRoom()->get_MonsterInDaRoom();
    Player &player = game.get_Player();
    if (player > *monster) {
        std::cout << "You encounter a smaller ";
        monster->print();
        std::cout << std::endl;
    }

    if (player < *monster) {
        std::cout << "You encounter a larger ";
        monster->print();
        std::cout << std::endl;
    }

    if (player == *monster) {
        std::cout << "You encounter a equally sized ";
        monster->print();
        std::cout << std::endl;
    }

    std::cout << *monster << std::endl;
    player.LetsFight(*monster);
    int option_switch = 0;
    //if the battle ends once either the player or the monster dies
    while (player.is_dead() != true && monster->is_dead() != true) {
        if (option_switch == 0) {
            player.activateSpecialtySorcerer();
            *monster -= player;
            std::cout << "You deal " << player.getDamage() << " damage to the ";
            monster->print();
            std::cout << " and leave it with " << monster->getCurrentHealth() << " health" << std::endl;


            option_switch = 1;
        } else {
            if (player.activateSpecialtyFighter() == true) {
                std::cout << "The ";
                monster->print();
                std::cout << " deals 0 damage to you and leaves you with " << player.getCurrentHealth() << " health" <<
                        std::endl;
            } else {
                player -= *monster;
                std::cout << "The ";
                monster->print();
                std::cout << " deals " << monster->getDamage() << " damage to you and leaves you with " << player
                        .getCurrentHealth() << " health" << std::endl;
            }
            option_switch = 0;
        }
    }

    if (player.is_dead()) {
        std::cout << "You lost to the dungeon" << std::endl;
        return 0;
    } else {
        std::cout << "You defeat the ";
        monster->print();
        std::cout << " and go on with your journey" << std::endl;
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
int Whats_In_Da_Room(const Game &game) {
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
 *this function starts the process of playing the game
 *the function is built from a while loop that continues until either the player won or the player died
 */
void Game::StartGame() {
    while (true) {
        if (Whats_In_Da_Room(*this) == 0) {
            break;
        }
        if (this->set_CurrentRoom() == 0) {
            break;
        }
    }
}
