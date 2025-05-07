import Animal
import Exceptions


class Fish(Animal.Animal):
    def __init__(self, name, age, x, y, directionH, directionV):
        super().__init__(name, age, x, y, directionH)
        self.directionV = directionV

        if type(self.directionV) is not int or self.directionV != 0 and self.directionV != 1:
            raise Exceptions.InvalidInputException

    def __str__(self):
        return super().__str__()

    def get_animal(self):
        return super().get_animal()

    def __repr__(self):
        return super().__repr__()

    def get_position(self):
        return super().get_position()

    def get_directionH(self):
        return super().get_directionH()

    def get_directionV(self):
        return self.directionV

    def get_size(self):
        return super().get_size()

    def dec_food(self):
        super().dec_food()

    def add_food(self, amount):
        super().add_food(amount)

    def inc_age(self):
        super().inc_age()

    def move(self):
        if self.directionH == 1 and self.directionV == 1:
            self.x = self.x + 1
            self.y = self.y - 1

        if self.directionH == 0 and self.directionV == 1:
            self.x = self.x - 1
            self.y = self.y - 1

        if self.directionH == 1 and self.directionV == 0:
            self.x = self.x + 1
            self.y = self.y + 1

        if self.directionH == 0 and self.directionV == 0:
            self.x = self.x - 1
            self.y = self.y + 1

    def set_directionH(self, directionH):
        super().set_directionH(directionH)

    def set_directionV(self, directionV):
        self.directionV = directionV

    def starvation(self):
        return super().starvation()

    def die(self):
        return super().die()
