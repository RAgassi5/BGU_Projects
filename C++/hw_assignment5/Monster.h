#ifndef MONSTER_H
#define MONSTER_H
#include "Entity.h"


class Monster : public Entity {

    public:
    //default constructor for Monster
    Monster(): Entity() {
    };

    //constructor for an instance of Monster
    Monster(const std::string &name, int max_health, int damage);

    //a copy constructor for Monster
    Monster(const Monster &other);

    //destructor for Monster
    virtual ~Monster();

    //a print function for the monster's name in lowercase letters
    virtual void print() const = 0;

    //a function implemented in the class inheriting from Monster to set the fight setting according to the opponent
    virtual void CanFightFighter() = 0;

    //a function implemented in the class inheriting from Monster to set the fight setting according to the opponent
    virtual void CanFightSorcerer() = 0;

};
#endif //MONSTER_H
