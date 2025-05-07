#include "Dragon.h"

Dragon::Dragon(int max_health, int damage): Monster("Dragon", max_health, damage) {}

Dragon::~Dragon() {
}

void Dragon::print() const {
    std::cout << "dragon";
}

void Dragon::CanFightFighter() {
    //round the damage up for any number that is larger than x.5
    const double damage = this->getDamage() / 2.0 ;
    int newDamage = static_cast<int>(damage);
    if (damage - newDamage > 0.4) {
        newDamage += 1;
    }
    this->set_damage(newDamage);
}

void Dragon::CanFightSorcerer() {
    //multiply the damage by 2
    this->set_damage((this->getDamage() * 2));
}


