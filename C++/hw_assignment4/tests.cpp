//
// Created by Roii Agassi on 15/01/2025.
//
#include <cassert>
#include <sstream>
#include <fstream>
#include "Game.h"
#include "Entity.h"
#include "Room.h"

// Test helper function to create temporary config file
void createTestConfigFile(const std::string& filename) {
    std::ofstream file(filename);
    file << "1 10 50 5\n";  // Room 1 with firecamp 10, monster health 50, damage 5
    file << "12 0 30 10\n"; // Room 12 (path 1->2) with no firecamp, monster health 30, damage 10
    file.close();
}

void testEntityClass() {
    std::cout << "\nTesting Entity Class..." << std::endl;

    // Test constructors
    Entity defaultEntity;
    assert(defaultEntity.getCurrentHealth() == 0);
    assert(defaultEntity.getDamage() == 0);

    Entity warrior("Warrior", 100, 20);
    assert(warrior.getCurrentHealth() == 100);
    assert(warrior.getDamage() == 20);

    // Test copy constructor
    Entity warriorCopy(warrior);
    assert(warriorCopy.getCurrentHealth() == warrior.getCurrentHealth());
    assert(warriorCopy.getDamage() == warrior.getDamage());

    // Test health modification operators
    warrior += 50;  // Heal
    assert(warrior.getCurrentHealth() == 100);  // Should cap at max health

    Entity enemy("Enemy", 50, 10);
    warrior -= enemy;  // Take damage
    assert(warrior.getCurrentHealth() == 90);

    // Test damage modification
    warrior -= 5;  // Lower damage
    assert(warrior.getDamage() == 15);

    // Test comparison operators
    Entity strong("Strong", 100, 30);
    Entity weak("Weak", 50, 10);
    assert(strong > weak);
    assert(weak < strong);

    // Test death check
    Entity dying("Dying", 10, 5);
    strong -= dying;  // Take damage
    assert(!dying.is_dead());
    dying -= strong;  // Take fatal damage
    assert(dying.is_dead());

    std::cout << "Entity Class Tests Passed!" << std::endl;
}

void testRoomClass() {
    std::cout << "\nTesting Room Class..." << std::endl;

    // Test room construction
    Room startRoom("1", 10, 50, 5);
    assert(startRoom.get_fireCamp() == 10);
    assert(startRoom.get_RoomPath() == "1");
    assert(startRoom.get_RoomID() == '1');
    assert(startRoom.get_roomCount() == 0);

    // Test monster creation in room
    Entity* monster = startRoom.get_MonsterInDaRoom();
    assert(monster != nullptr);
    assert(monster->getCurrentHealth() == 50);
    assert(monster->getDamage() == 5);

    // Test room connections
    Room* connectedRoom = new Room("12", 0, 30, 10);
    startRoom.addRoomByIndex(connectedRoom);
    assert(startRoom.get_roomCount() > 0);
    assert(startRoom.findRoom(2));
    assert(!startRoom.findRoom(9));

    // Test room indexing
    assert(startRoom[2] == connectedRoom);
    assert(startRoom[9] == nullptr);

    // Test copy constructor
    Room roomCopy(startRoom);
    assert(roomCopy.get_fireCamp() == startRoom.get_fireCamp());
    assert(roomCopy.get_RoomPath() == startRoom.get_RoomPath());
    assert(roomCopy.get_roomCount() == startRoom.get_roomCount());

    std::cout << "Room Class Tests Passed!" << std::endl;
}

void testGameClass() {
    std::cout << "\nTesting Game Class..." << std::endl;

    // Create temporary config file
    createTestConfigFile("test_config.txt");

    // Test game initialization
    Game game("test_config.txt", 100, 20);
    assert(game.get_Player().getCurrentHealth() == 100);
    assert(game.get_Player().getDamage() == 20);

    // Test current room access
    Room* currentRoom = game.get_CurrentRoom();
    assert(currentRoom != nullptr);

    // Test player reference
    Entity& player = game.get_Player();
    assert(player.getCurrentHealth() == 100);
    assert(player.getDamage() == 20);

    // Clean up
    std::remove("test_config.txt");

    std::cout << "Game Class Tests Passed!" << std::endl;
}

void testIntegration() {
    std::cout << "\nTesting Integration..." << std::endl;

    createTestConfigFile("test_config.txt");
    Game game("test_config.txt", 100, 20);

    // Test basic game flow (without user input)
    Room* currentRoom = game.get_CurrentRoom();
    Entity& player = game.get_Player();

    // Test combat simulation
    if (Entity* monster = currentRoom->get_MonsterInDaRoom()) {
        int initialPlayerHealth = player.getCurrentHealth();
        int initialMonsterHealth = monster->getCurrentHealth();

        player -= *monster;  // Player takes damage
        assert(player.getCurrentHealth() < initialPlayerHealth);

        *monster -= player;  // Monster takes damage
        assert(monster->getCurrentHealth() < initialMonsterHealth);
    }

    // Test healing at firecamp
    int initialHealth = player.getCurrentHealth();
    if (currentRoom->get_fireCamp() > 0) {
        player += currentRoom->get_fireCamp();
        assert(player.getCurrentHealth() > initialHealth);
    }

    // Clean up
    std::remove("test_config.txt");

    std::cout << "Integration Tests Passed!" << std::endl;
}

int main() {
    std::cout << "Starting Game System Tests..." << std::endl;

    testEntityClass();
    testRoomClass();
    testGameClass();
    testIntegration();

    std::cout << "\nAll Tests Passed Successfully!" << std::endl;
    return 0;
}
