#ifndef ROOM_H
#define ROOM_H
#include <string>
#include "Entity.h"

class Room {
private:
    std::string RoomID;
    int fireCamp;
    Entity MonsterInDaRoom;
    int monsterCurrentHealth;
    int monsterDamage;
    Room **roomList;
    int roomCount;

public:
    //constructor
    Room(const std::string &RoomID, int fireCamp, int monsterCurrentHealth, int monsterDamage);

    //destructor
    ~Room();

    //copy constructor
    Room(const Room &OtherRoom);

    //a getter function to get the size of room list in an instance of a room
    int get_roomCount() const;

    //adds a new instance of a room to a room's roomList
    void addRoomByIndex(Room *OtherRoom);

    //a function that searches for an existence of a room
    bool findRoom(int room_to_search) const;

    //a getter function for a room's ID
    std::string get_RoomPath() const;

    //a getter for the actual room number (which is always the last number in the room id)
    char get_RoomID() const;

    //set a value to an index in the roomList
    Room *operator[](int index);

    //search for the value in the given index in roomList
    const Room *operator[](int index) const;

    //a function that returns the health gained from the campfire
    int get_fireCamp() const;

    //a function that returns the monster in the room if there is one
    Entity *get_MonsterInDaRoom();
};


#endif //ROOM_H
