#ifndef FIGHTER_H
#define FIGHTER_H

#include "Player.h"

class Fighter : public Player {
public:
    //default constructor for fighter
    Fighter(): Player() {
        specialtySwitch = 0;
    };

    //constructor for an instance of fighter
    Fighter(int max_health, int damage);

    //destructor for an instance of fighter
    virtual ~Fighter();

    //a function that prints the entity's name in lowercase letters
    void print() const;

    //a function that sets the enemy's damage according to the type of player
    virtual void LetsFight(Monster &other);

    //a function that activates the fighter's special ability
    virtual bool activateSpecialtyFighter();

    //a function that activates the sorcerer's special ability
    virtual bool activateSpecialtySorcerer();

    //a function that sets the special ability counter
    virtual void set_Specialty();
};


#endif //FIGHTER_H
