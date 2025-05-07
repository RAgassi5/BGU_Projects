#ifndef GOBLIN_H
#define GOBLIN_H
#include "Monster.h"

class Goblin : public Monster {
public:
    //constructor for an instance of Goblin
    Goblin(int max_health, int damage);

    //function that prints out the name of the Goblin in lowercase letters
    virtual void print() const;

    //destructor for Goblin
    virtual ~Goblin();

    //sets the damage according to the dragon's opponent
    virtual void CanFightFighter();

    //sets the damage according to the dragon's opponent
    virtual void CanFightSorcerer();
};
#endif //GOBLIN_H
