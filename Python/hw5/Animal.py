from abc import ABC, abstractmethod
import Exceptions


class Animal(ABC):

    def __init__(self, name, age, x, y, directionH):
        self.name = name
        self.age = age
        self.food = 10
        self.directionH = directionH
        self.x = x
        self.y = y

        if type(self.name) is not str or self.name == "":
            raise Exceptions.InvalidInputException

        if type(self.age) is not int or self.age < 1 or self.age > 119:
            raise Exceptions.InvalidInputException

        if type(self.directionH) is not int or self.directionH != 0 and self.directionH != 1:
            raise Exceptions.InvalidInputException

        if type(self.x) is not int or self.x <= 0:
            raise Exceptions.InvalidInputException

        if type(self.y) is not int or self.y <= 0:
            raise Exceptions.InvalidInputException

    def __str__(self):
        return f'{self.name} is {self.age} years old and has {self.food} food.'

    @abstractmethod
    def get_animal(self):
        pass

    def __repr__(self):
        animal = self.get_animal()
        lst = []
        for row in animal:
            printed_row = " ".join(row)
            lst.append(printed_row)
        lst.append('\n')
        return '\n'.join(lst)

    def get_position(self):
        return (self.x, self.y)

    def get_directionH(self):
        return self.directionH

    @abstractmethod
    def get_size(self):
        pass

    def dec_food(self):
        self.food = self.food - 1

    def add_food(self, amount):
        self.food = self.food + amount

    def inc_age(self):
        self.age = self.age + 1

    @abstractmethod
    def move(self):
        pass

    def set_directionH(self, directionH):
        self.directionH = directionH

    def starvation(self):
        if self.food > 0:
            return False
        else:
            print(f'{self.name} died at the age of {self.age} years because it ran out of food.')
            return True

    def die(self):
        if self.age == 120:
            print(f'{self.name} died in a good health.')
            return True
        else:
            return False
