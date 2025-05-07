#include "Room.h"
#include "GameException.h"
#include "Dragon.h"
#include "Goblin.h"


Room::Room(const std::string &RoomID, int fireCamp, const std::string &type_of_Monster, int monsterCurrentHealth,
           int monsterDamage) {
    this->RoomID = RoomID;
    this->fireCamp = fireCamp;
    this->monsterCurrentHealth = monsterCurrentHealth;
    this->monsterDamage = monsterDamage;
    this->roomList = nullptr;
    this->roomCount = 0;
    if (type_of_Monster == "D") {
        //try creating an instance of Dragon, if failed throw exception
        try {
            this->MonsterPtr = new Dragon(monsterCurrentHealth, monsterDamage);
        } catch (std::bad_alloc &Error) {
            throw MemoryException();
        }
    } else if (type_of_Monster == "G") {
        //try creating an instance of Goblin, if failed throw exception
        try {
            this->MonsterPtr = new Goblin(monsterCurrentHealth, monsterDamage);
        } catch (std::bad_alloc &Error) {
            throw MemoryException();
        }
        //if there isn't a Dragon or a Goblin, it means there is no monster at all
    } else {
        this->MonsterPtr = nullptr;
    }
}

Room::~Room() {
    if (this->roomList != nullptr) {
        for (int i = 0; i < this->roomCount; i++) {
            delete this->roomList[i];
        }
        delete[] this->roomList;
        this->roomList = nullptr;
    }
    delete this->MonsterPtr;
}

Room::Room(const Room &OtherRoom) {
    this->RoomID = OtherRoom.RoomID;
    this->fireCamp = OtherRoom.fireCamp;
    this->monsterCurrentHealth = OtherRoom.monsterCurrentHealth;
    this->monsterDamage = OtherRoom.monsterDamage;
    if (OtherRoom.roomList != nullptr && OtherRoom.roomCount > 0) {
        this->roomList = new Room *[OtherRoom.roomCount];
        for (int i = 0; i < OtherRoom.roomCount; i++) {
            if (OtherRoom.roomList[i] != nullptr) {
                this->roomList[i] = new(std::nothrow) Room(*OtherRoom.roomList[i]);
                //if memory allocation failed while creating a new room, throw exception
                if (this->roomList[i] == nullptr) {
                    throw MemoryException();
                }
            } else {
                this->roomList[i] = nullptr;
            }
        }
        this->roomCount = OtherRoom.roomCount;
    } else {
        this->roomCount = 0;
        this->roomList = nullptr;
    }

    this->MonsterPtr = OtherRoom.MonsterPtr;
}

int Room::get_roomCount() const {
    return this->roomCount;
}


void Room::addRoomByIndex(Room *OtherRoom) {
    char roomNumber = OtherRoom->get_RoomID();
    int room_to_add_number = roomNumber - '0';

    if (this->roomList == nullptr) {
        this->roomList = new Room *[room_to_add_number + 1];
        for (int i = 0; i < room_to_add_number; i++) {
            this->roomList[i] = nullptr;
        }
        this->roomList[room_to_add_number] = OtherRoom;
        this->roomCount = room_to_add_number + 1;
    } else {
        if (room_to_add_number < this->roomCount) {
            this->roomList[room_to_add_number] = OtherRoom;
        } else {
            Room **NewList = new(std::nothrow)Room *[room_to_add_number + 1];
            //if memory allocation failed while creating a new room, throw exception
            if (NewList == nullptr) {
                throw MemoryException();
            }
            for (int i = 0; i < room_to_add_number; i++) {
                NewList[i] = nullptr;
            }
            for (int i = 0; i < this->roomCount; i++) {
                if (this->roomList[i] != nullptr) {
                    NewList[i] = this->roomList[i];
                }
            }
            NewList[room_to_add_number] = OtherRoom;
            delete[] this->roomList;
            this->roomList = NewList;
            this->roomCount = room_to_add_number + 1;
        }
    }
}


bool Room::findRoom(int room_to_search) const {
    if (room_to_search < 0 || room_to_search >= this->roomCount) {
        return false;
    }
    Room *room = this->roomList[room_to_search];

    if (room == nullptr) {
        return false;
    }

    return true;
}


std::string Room::get_RoomPath() const {
    return this->RoomID;
}

char Room::get_RoomID() const {
    const int id_length = this->RoomID.length();
    return this->RoomID[id_length - 1];
}


Room *Room::operator[](int index) {
    //if index is out of the array's range -> return nullptr(nothing)
    if (index < 0 || index >= this->roomCount) {
        return nullptr;
    }
    return roomList[index];
}

const Room *Room::operator[](int index) const {
    //if index is out of the array's range -> return nullptr(nothing)
    if (index < 0 || index >= this->roomCount) {
        return nullptr;
    }
    return roomList[index];
}

int Room::get_fireCamp() const {
    return this->fireCamp;
}


Monster *Room::get_MonsterInDaRoom() const {
    if (this->monsterCurrentHealth == 0) {
        return nullptr;
    }
    return this->MonsterPtr;
}
