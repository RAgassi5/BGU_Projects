#ifndef SORCERER_H
#define SORCERER_H
#include "Player.h"


class Sorcerer : public Player {
private:
    //int specialtySwitch;
    int RealDamage;
    int SpecialDamage;

public:
    //default constructor
    Sorcerer() : Player() {
        specialtySwitch = 0;
        RealDamage = 0;
        SpecialDamage = 0;
    };

    //constructor for an instance of a sorcerer
    Sorcerer(int max_health, int damage);

    //destructor for Sorcerer
    ~Sorcerer();

    //prints the entity's name in  lowercase letters
    void print() const;

    //a function that sets the enemy's damage according to the type of player
    virtual void LetsFight(Monster &other);

    //a function that activates the fighter's special ability
    virtual bool activateSpecialtyFighter();

    //a function that activates the sorcerer's special ability
    virtual bool activateSpecialtySorcerer();

    //a function that set the special ability counter
    virtual void set_Specialty();
};

#endif //SORCERER_H
