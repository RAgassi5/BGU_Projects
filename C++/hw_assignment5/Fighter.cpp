#include "Fighter.h"


Fighter::Fighter(int max_health, int damage): Player("Fighter", max_health, damage) {
}

Fighter::~Fighter() {
}

void Fighter::print() const {
    std::cout << "fighter";
}

void Fighter::LetsFight(Monster &other) {
    other.CanFightFighter();
}

bool Fighter::activateSpecialtyFighter() {
    if (this->specialtySwitch == 4) {
        this->reset_Specialty();
        return true;
    }
    return false;
}

bool Fighter::activateSpecialtySorcerer() {
    this->set_Specialty();
    return false;
}

void Fighter::set_Specialty() {
    if (this->specialtySwitch == 4) {
        this->specialtySwitch = 4;
    } else {
        ++(this->specialtySwitch);
    }
}
