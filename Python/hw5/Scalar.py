import Fish



class Scalar(Fish.Fish):
    def __init__(self, name, age, x, y, directionH, directionV):
        super().__init__(name, age, x, y, directionH, directionV)
        self.width = 8
        self.height = 5

    def __str__(self):
        return f'The scalar {super().__str__()}'

    def get_animal(self):
        scalar_left = [[" ", " ", "*", "*", "*", "*", "*", "*"],
               [" ", "*", "*", "*", " ", " ", " ", " "],
               ["*", "*", "*", "*", "*", "*", " ", " "],
               [" ", "*", "*", "*", " ", " ", " ", " "],
               [" ", " ", "*", "*", "*", "*", "*", "*"]]

        scalar_right = [["*", "*", "*", "*", "*", "*", " ", " "],
                [" ", " ", " ", " ", "*", "*", "*", " "],
                [" ", " ", "*", "*", "*", "*", "*", "*"],
                [" ", " ", " ", " ", "*", "*", "*", " "],
                ["*", "*", "*", "*", "*", "*", " ", " "]]

        if self.directionH == 0:
            return scalar_left
        if self.directionH == 1:
            return scalar_right

    def __repr__(self):
        return super().__repr__()

    def get_position(self):
        return super().get_position()

    def get_directionH(self):
        return super().get_directionH()

    def get_directionV(self):
        return super().get_directionV()

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

    def set_directionV(self, directionV):
        super().set_directionV(directionV)

    def starvation(self):
        return super().starvation()

    def die(self):
        return super().die()


