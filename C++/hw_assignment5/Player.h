#ifndef PLAYER_H
#define PLAYER_H
#include "Entity.h"
#include "Monster.h"

class Player : public Entity {
protected:
    int specialtySwitch;

    Player(): Entity() {
        specialtySwitch = 0;
    };

public:
    //constructor for Player
    Player(const std::string &name, int max_health, int damage);

    //copy constructor for Player
    Player(const Player &other);

    //destructor for Player
    virtual ~Player();

    //a print function
    virtual void print() const = 0;

    //a function that sets the battle settings according to the opponent
    virtual void LetsFight(Monster &other) = 0;

    //a function that activates the fighter's special ability
    virtual bool activateSpecialtyFighter() = 0;

    //a function that activates the sorcerer's special ability
    virtual bool activateSpecialtySorcerer() = 0;

    //set the specialty ability switch
    virtual void set_Specialty() = 0;

    //reset the specialty ability switch
    virtual void reset_Specialty();
};

#endif //PLAYER_H
