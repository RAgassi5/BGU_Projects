#include "Monster.h"

Monster::Monster(const std::string &name, const int max_health, const int damage): Entity(name,max_health, damage) {}

Monster::Monster(const Monster &other): Entity(other){}

Monster::~Monster() {
}


