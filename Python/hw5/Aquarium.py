import Exceptions
from Molly import Molly
from Shrimp import Shrimp
from Scalar import Scalar
from Ocypode import Ocypode
from Fish import Fish
from Crab import Crab


class Aquarium:
    def __init__(self, aqua_width, aqua_height):
        self.aqua_height = aqua_height
        self.aqua_width = aqua_width
        self.step = 0
        self.animals = []
        self.board = []

        if type(self.aqua_height) is not int:
            raise Exceptions.InvalidInputException
        if self.aqua_height < 25:
            raise Exceptions.TooSmallAquariumSize
        if type(self.aqua_width) is not int:
            raise Exceptions.TooSmallAquariumSize
        if self.aqua_width < 40:
            raise Exceptions.TooSmallAquariumSize

        for i in range(aqua_height):
            if i != aqua_height - 1:
                row = ["|"]
                for j in range(aqua_width - 2):
                    if i == 2:
                        row.append("~")
                    else:
                        row.append(" ")
                row.append("|")
                self.board.append(row)
            if i == aqua_height - 1:
                row = ["\\"]
                for j in range(aqua_width - 2):
                    row.append("_")
                row.append("/")
                self.board.append(row)

    def __str__(self):
        my_lst = []
        for animal in self.animals:
            my_lst.append(animal.__str__())
        return f'The aquarium, sized {self.aqua_height}/{self.aqua_width} and currently in step {self.step}, contains the following animals:\n' + "\n".join(
            my_lst)+ "\n"

    def __repr__(self):
        lst = []
        for row in self.board:
            printed_row = " ".join(row)
            lst.append(printed_row)
        return '\n'.join(lst) + '\n'

    def feed_all(self):
        for item in self.animals:
            item.add_food(10)

    def __insert_animal_to_board(self, animal):
        animal_picture = animal.get_animal()
        row = animal.y
        column = animal.x

        for i in range(animal.height):
            for j in range(animal.width):
                if self.board[row][column] == "*":
                    pass
                else:
                    self.board[row][column] = animal_picture[i][j]
                column += 1
            column = animal.x
            row += 1

    def __delete_animal_from_board(self, animal):
        row = animal.y
        column = animal.x

        for i in range(animal.height):
            for j in range(animal.width):
                self.board[row][column] = " "
                column += 1
            column = animal.x
            row += 1

    def add_animal(self, name, age, x, y, directionH, directionV, animaltype):
        animaltype_options = ["scalar", "molly", "ocypode", "shrimp"]

        if type(animaltype) is not str:
            raise Exceptions.InvalidInputException

        if animaltype not in animaltype_options:
            raise Exceptions.InvalidAnimalType(animaltype)

        else:
            if animaltype == "shrimp":
                created_animal = Shrimp(name, age, x, self.aqua_height - 4, directionH)
                new_x = self.set_x_value(created_animal)

                new_shrimp = Shrimp(name, age, new_x, self.aqua_height - 4, directionH)

                if self.check_availability(new_shrimp) is True:
                    self.animals.append(new_shrimp)
                    self.__insert_animal_to_board(new_shrimp)

            if animaltype == "ocypode":
                created_animal = Ocypode(name, age, x, self.aqua_height - 5, directionH)
                new_x = self.set_x_value(created_animal)

                new_ocypode = Ocypode(name, age, new_x, self.aqua_height - 5, directionH)

                if self.check_availability(new_ocypode) is True:
                    self.animals.append(new_ocypode)
                    self.__insert_animal_to_board(new_ocypode)

            if animaltype == "scalar":
                created_animal = Scalar(name, age, x, y, directionH, directionV)
                new_x = self.set_x_value(created_animal)
                new_y = self.set_y_value(created_animal)

                new_scalar = Scalar(name, age, new_x, new_y, directionH, directionV)

                if self.check_availability(new_scalar) is True:
                    self.animals.append(new_scalar)
                    self.__insert_animal_to_board(new_scalar)

            if animaltype == "molly":
                created_animal = Molly(name, age, x, y, directionH, directionV)
                new_x = self.set_x_value(created_animal)
                new_y = self.set_y_value(created_animal)

                new_molly = Molly(name, age, new_x, new_y, directionH, directionV)

                if self.check_availability(new_molly) is True:
                    self.animals.append(new_molly)
                    self.__insert_animal_to_board(new_molly)

    def check_availability(self, animal):
        row = animal.x
        column = animal.y
        for i in range(animal.height):
            for j in range(animal.width):
                if self.board[column][row] == " ":
                    row += 1

                else:
                    raise Exceptions.NotAvailablePlace

            row = row - animal.width
            column += 1
        return True

    def set_x_value(self, animal_choice):
        if isinstance(animal_choice, Fish):
            if animal_choice.x >= self.aqua_width - 9:
                animal_choice.x = self.aqua_width - 9
                return animal_choice.x
            else:
                return animal_choice.x

        if isinstance(animal_choice, Crab):
            if animal_choice.x >= self.aqua_width - 8:
                animal_choice.x = self.aqua_width - 8
                return animal_choice.x
            else:
                return animal_choice.x

    def set_y_value(self, animal_choice):
        if isinstance(animal_choice, Molly):
            if animal_choice.y > self.aqua_height - 8:
                animal_choice.y = self.aqua_height - 8
                return animal_choice.y
            elif animal_choice.y < 3:
                animal_choice.y = 3
                return animal_choice.y
            else:
                return animal_choice.y

        if isinstance(animal_choice, Scalar):

            if animal_choice.y > self.aqua_height - 10:
                animal_choice.y = self.aqua_height - 10
                return animal_choice.y

            elif animal_choice.y < 3:
                animal_choice.y = 3
                return animal_choice.y
            else:
                return animal_choice.y

    def __kill_animal(self, animal):
        if animal.starvation() is True:
            self.__delete_animal_from_board(animal)
            self.animals.remove(animal)

        if animal.die() is True:
            self.__delete_animal_from_board(animal)
            self.animals.remove(animal)

    def next_step(self):
        self.step += 1
        self.check_death()
        self.crab_collision()
        self.check_wall()
        self.check_water_line()
        self.move_all()
        self.life_in_aquarium()

    def check_death(self):
        for animal in self.animals:
            self.__kill_animal(animal)

    def check_wall(self):
        """
        checks if the animal is colliding with a wall
        :return:
        """
        right_wall = self.aqua_width - 1
        for animal in self.animals:
            if animal.get_directionH() == 0:
                if animal.x == 1:
                    animal.set_directionH(1)

                else:
                    continue

            if animal.get_directionH() == 1:
                if animal.x == right_wall - animal.width:
                    animal.set_directionH(0)

                else:
                    continue

    def check_water_line(self):
        """
        checks if the animal pass the height of the water
        :return:
        """
        water_line = 3
        bottom = self.aqua_height - 5

        for animal in self.animals:
            if isinstance(animal, Fish):
                if animal.get_directionV() == 1:
                    if animal.y == water_line:
                        animal.set_directionV(0)
                    else:
                        continue

            if isinstance(animal, Fish):
                if animal.get_directionV() == 0:
                    if animal.y == bottom - animal.height:
                        animal.set_directionV(1)
                    else:
                        continue

    def move_all(self):
        """
        moves all the animals in the aquarium
        :return:
        """
        for animal in self.animals:
            self.__delete_animal_from_board(animal)
            animal.move()
        for animal in self.animals:
            self.__insert_animal_to_board(animal)

    def crab_collision(self):
        """
        calls the recursive crab function
        :return:
        """
        self.recursive_crab(self.animals)

    def recursive_crab(self, animal_list, index1=0, index2=1):
        """
        a recursive function that check if the crabs collide
        :param animal_list: the list of animals in the aquarium
        :param index1: first index
        :param index2: second index
        :return: sets the directionV of the colliding crabs
        """

        if len(animal_list) < 2:
            return True
        if index1 == len(animal_list):
            return True

        if index1 == len(animal_list) - 1 and index2 == len(animal_list):
            return True

        if index2 == len(animal_list) and index1 != len(animal_list):
            if index1 + 2 >= len(animal_list):
                return self.recursive_crab(animal_list, index1 + 1, len(animal_list))
            else:
                return self.recursive_crab(animal_list, index1 + 1, index1 + 2)

        if not isinstance(animal_list[index1], Crab):
            return self.recursive_crab(animal_list, index1 + 1, index2)

        if not isinstance(animal_list[index2], Crab):
            return self.recursive_crab(animal_list, index1, index2 + 1)

        distance = animal_list[index1].x - animal_list[index2].x

        if distance == -8 or distance == -7:
            if animal_list[index1].get_directionH() == animal_list[index2].get_directionH():
                return self.recursive_crab(animal_list, index1, index2 + 1)

            else:
                animal_list[index1].set_directionH(0)
                animal_list[index2].set_directionH(1)
                return self.recursive_crab(animal_list, index1, index2 + 1)

        if distance == 8 or distance == 7:
            if animal_list[index1].get_directionH() == animal_list[index2].get_directionH():
                return self.recursive_crab(animal_list, index1, index2 + 1)
            else:
                animal_list[index1].set_directionH(1)
                animal_list[index2].set_directionH(0)
                return self.recursive_crab(animal_list, index1, index2 + 1)

        else:
            return self.recursive_crab(animal_list, index1, index2 + 1)

    def life_in_aquarium(self):
        """
        updates the life in the aquarium after every 10 steps
        :return:
        """
        if self.step % 10 == 0:
            for animal in self.animals:
                animal.inc_age()
                animal.dec_food()

    def several_steps(self, steps):
        for i in range(steps):
            self.next_step()




