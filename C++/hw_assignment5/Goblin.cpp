#include "Goblin.h"

Goblin::Goblin(int max_health, int damage): Monster("Goblin", max_health, damage) {
}

void Goblin::print() const {
    std::cout << "goblin";
}

Goblin::~Goblin() {
}


void Goblin::CanFightFighter() {
    //multiply the damage by 2
    this->set_damage((this->getDamage() * 2));
}

void Goblin::CanFightSorcerer() {
    //round the damage up for any number that is larger than x.5
    const double damage = this->getDamage() / 2.0;
    int newDamage = static_cast<int>(damage);
    if (damage - newDamage > 0.4) {
        newDamage += 1;
    }
    this->set_damage(newDamage);
}
