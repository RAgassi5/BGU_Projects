#include "Entity.h"

Entity::Entity(): name("default"), MaxHealth(0), currentHealth(0), damage(0){}


Entity :: Entity(const std::string &name, const int MaxHealth, const int damage) {
    if(MaxHealth < 0 || damage < 0) {
        return;
    }
    this->name = name;
    this->MaxHealth = MaxHealth;
    //start the current health at maximum amount
    this->currentHealth = this->MaxHealth;
    this->damage = damage;
}

Entity::~Entity() {}

Entity :: Entity(const Entity& other){
    this->name = other.name;
    this->MaxHealth = other.MaxHealth;
    this->currentHealth = this->MaxHealth;
    this->damage = other.damage;

}

Entity &Entity::operator+=(int health) {
    this->currentHealth += health;
    if(this->currentHealth > this->MaxHealth) {
        this->currentHealth = this->MaxHealth;
    }
    return *this;
}

Entity &Entity :: operator-=(const Entity &other) {
    this->currentHealth -= other.damage;
    if(this->currentHealth < 0) {
        this->currentHealth = 0;
    }
    return *this;
}

Entity &Entity :: operator-=(int lowerDamage) {
    this->damage -= lowerDamage;
    if(this->damage < 0) {
        this->damage = 0;
    }
    return *this;
}

bool Entity :: operator==(const Entity &other) const {
    return ((this->currentHealth * this->damage) == (other.currentHealth * other.damage));
}

bool  Entity :: operator<(const Entity &other) const {
    return ((this->currentHealth * this->damage) < (other.currentHealth * other.damage));
}

bool  Entity :: operator>(const Entity &other) const {
    return ((this->currentHealth * this->damage) > (other.currentHealth * other.damage));
}

bool Entity::is_dead() const {
    if(this->currentHealth > 0) {
        return false;
    }
    return true;
}

int Entity :: getDamage() const {
    return this->damage;
}

int Entity::getCurrentHealth() const {
    return this->currentHealth;
}

std::ostream &operator<<(std::ostream &printOUT, const Entity &entity) {
    printOUT << entity.name << " (" << entity.currentHealth << "/" << entity.MaxHealth << ")" << " - " << entity.damage;
    return printOUT;
}

