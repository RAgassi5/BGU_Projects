#include "Sorcerer.h"

Sorcerer::Sorcerer(const int max_health, const int damage): Player("Sorcerer", max_health, damage) {
    this->RealDamage = damage;
    this->SpecialDamage = (damage * 2);
}

Sorcerer::~Sorcerer() {
}

void Sorcerer::print() const {
    std::cout << "sorcerer";
}

void Sorcerer::LetsFight(Monster &other) {
    other.CanFightSorcerer();
}

bool Sorcerer::activateSpecialtyFighter() {
    return false;
}

bool Sorcerer::activateSpecialtySorcerer() {
    if (this->specialtySwitch == 4) {
        this->damage = this->SpecialDamage;
        this->reset_Specialty();
        return true;
    }

    this->damage = this->RealDamage;
    this->set_Specialty();
    return false;
}

void Sorcerer::set_Specialty() {
    if (this->specialtySwitch == 4) {
        this->specialtySwitch = 4;
    } else {
        ++(this->specialtySwitch);
    }
}


