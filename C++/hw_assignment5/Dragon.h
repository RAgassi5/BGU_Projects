#ifndef DRAGON_H
#define DRAGON_H
#include "Monster.h"

class Dragon : public Monster {

public:

    //a constructor for an instance of Dragon
    Dragon(int max_health, int damage);

    //destructor for Dragon
    virtual ~Dragon() ;

    //a print function that prints the Dragon's name in lowercase
    virtual void print() const;

    //sets the damage according to the dragon's opponent
    virtual void CanFightFighter();

    //sets the damage according to the dragon's opponent
    virtual void CanFightSorcerer();





};
#endif //DRAGON_H
