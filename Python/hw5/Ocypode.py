import Crab



class Ocypode(Crab.Crab):
    def __init__(self, name, age, x, y, directionH):
        super().__init__(name, age, x, y, directionH)
        self.width = 7
        self.height = 4

    def __str__(self):
        return f'The ocypode {super().__str__()}'

    def get_animal(self):
        ocypode = [[" ", "*", " ", " ", " ", "*", " "],
           [" ", " ", "*", "*", "*", " ", " "],
           ["*", "*", "*", "*", "*", "*", "*"],
           ["*", " ", " ", " ", " ", " ", "*"]]
        return ocypode

    def __repr__(self):
        return super().__repr__()

    def get_position(self):
        return super().get_position()

    def get_directionH(self):
        return super().get_directionH()


    def get_size(self):
        return (self.width, self.height)

    def dec_food(self):
        super().dec_food()

    def add_food(self, amount):
        super().add_food(amount)

    def inc_age(self):
        super().inc_age()

    def move(self):
        super().move()

    def set_directionH(self, directionH):
        super().set_directionH(directionH)

    def starvation(self):
        return super().starvation()

    def die(self):
        return super().die()


