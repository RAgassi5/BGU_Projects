#include "Player.h"

Player::Player(const std::string &name, int const max_health, int const damage): Entity(name, max_health, damage) {
    this->specialtySwitch = 3;
}

Player::Player(const Player &other) : Entity(other) {
    this->specialtySwitch = other.specialtySwitch;
}

Player::~Player() {}


void Player::reset_Specialty() {
    this->specialtySwitch = 0;
}
