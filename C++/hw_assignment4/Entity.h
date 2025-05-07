#ifndef ENTITY_H
#define ENTITY_H
#include <iostream>
#include <string>

class Entity {
private:
    std::string name;
    int MaxHealth;
    int currentHealth;
    int damage;

public:
    //default constructor
    Entity();

    //constructor
    Entity(const std::string &name, int max_health, int damage);

    //Destructor
    ~Entity();

    //copy constructor
    Entity(const Entity &other);

    //increases the current health amount by a given number
    Entity &operator+=(int health);

    //decreases the current health by the amount of damage taken
    Entity &operator-=(const Entity &other);

    //decreases the current health amount by a given number
    Entity &operator-=(int lowerDamage);

    ////a function for operator "==" -> returns true if this == other
    bool operator==(const Entity &other) const;

    //a function for operator "<" -> returns true if this < other
    bool operator<(const Entity &other) const;

    //a function for operator ">" -> returns true if this > other
    bool operator>(const Entity &other) const;

    //a function that checks if an instance of Entity reached currentHealth = 0
    bool is_dead() const;

    //a getter function for an Entity's damage
    int getDamage() const;

    //a getter function for an Entity's currentHealth
    int getCurrentHealth() const;

    //print an instance of an Entity
    friend std::ostream &operator<<(std::ostream &printOUT, const Entity &entity);

};


#endif //ENTITY_H
