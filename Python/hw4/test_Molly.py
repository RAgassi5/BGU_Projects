from unittest import TestCase, main
from Molly import Molly
import Exceptions


class TestMolly(TestCase):
    def test_molly_creation_valid_input(self):
        molly = Molly("Molly", 1, 3, 3, 1, 0)
        self.assertEqual(molly.name, "Molly")
        self.assertEqual(molly.age, 1)
        self.assertEqual(molly.x, 3)
        self.assertEqual(molly.y, 3)
        self.assertEqual(molly.directionH, 1)
        self.assertEqual(molly.directionV, 0)
        self.assertEqual(molly.width, 8)
        self.assertEqual(molly.height, 3)
        self.assertEqual(molly.food, 10)

    def test_molly_creation_invalid_input(self):
        with self.assertRaises(Exceptions.InvalidInputException):
            molly = Molly("", 1, 3, 3, 1, 0)

        with self.assertRaises(Exceptions.InvalidInputException):
            molly = Molly("Molly", 0, 3, 3, 1, 0)

        with self.assertRaises(Exceptions.InvalidInputException):
            molly = Molly("Molly", 1, -3, 9, 1, 0)

        with self.assertRaises(Exceptions.InvalidInputException):
            molly = Molly("Molly", 120, 22, 12, 1, 0)

        with self.assertRaises(Exceptions.InvalidInputException):
            molly = Molly("Molly", 1, 3, 3, 1, 3)
        # Add more test cases for invalid inputs as needed

    def test_molly_move(self):
        molly1 = Molly("Molly", 1, 15, 15, 1, 0)
        molly1.move()
        self.assertEqual(molly1.x, 16)
        self.assertEqual(molly1.y, 16)

        molly2 = Molly("Molly", 1, 7, 18, 1, 0)
        molly2.set_directionH(0)
        molly2.set_directionV(1)
        molly2.move()
        self.assertEqual(molly2.x, 6)
        self.assertEqual(molly2.y, 17)

        # Add more test cases for different movement scenarios

    def test_molly_starvation(self):
        molly = Molly("Molly", 1, 3, 3, 1, 0)
        self.assertFalse(molly.starvation())

        molly.food = 0
        self.assertTrue(molly.starvation())

    def test_molly_die(self):
        molly = Molly("Molly", 1, 3, 3, 1, 0)
        self.assertFalse(molly.die())

        molly.age = 120
        self.assertTrue(molly.die())

    def test_molly_inc_age(self):
        molly = Molly("Molly", 32, 3, 3, 1, 0)
        molly.inc_age()
        self.assertEqual(molly.age, 33)

    def test_molly_dec_food(self):
        molly = Molly("Molly", 1, 3, 3, 1, 0)
        molly.dec_food()
        self.assertEqual(molly.food, 9)

        molly.food = 0
        with self.assertRaises(Exceptions.InvalidInputException):
            molly.dec_food()

    def test_molly_add_food(self):
        molly = Molly("Molly", 1, 3, 3, 1, 0)
        molly.add_food(12)
        self.assertEqual(molly.food, 22)

    def test_molly_get_position(self):
        molly = Molly("Molly", 1, 23, 12, 1, 0)
        self.assertEqual(molly.get_position(), (23, 12))

    def test_molly_get_directionsV(self):
        molly = Molly("Molly", 1, 3, 3, 1, 0)
        self.assertEqual(molly.get_directionV(), 0)

    def test_molly_get_size(self):
        molly = Molly("Molly", 1, 3, 3, 1, 0)
        self.assertEqual(molly.get_size(), (8, 3))

        # Add more test cases for other methods as needed

    def test_molly_str_representation(self):
        molly = Molly("Molly", 55, 3, 3, 1, 0)
        self.assertEqual(str(molly), "The molly Molly is 55 years old and has 10 food.")

    def test_molly_set_directionH(self):
        molly = Molly("Molly", 1, 3, 3, 1, 0)
        molly.set_directionH(0)
        self.assertEqual(molly.directionH, 0)

    def test_molly_set_directionV(self):
        molly = Molly("Molly", 1, 3, 3, 1, 0)
        molly.set_directionV(1)
        self.assertEqual(molly.directionV, 1)

    def test_get_animal(self):
        molly1 = Molly("roii", 12, 12, 15, 0, 1)
        self.assertEqual(molly1.get_animal(), [[' ', '*', '*', '*', '*', ' ', ' ', '*'],
                                               ['*', '*', '*', '*', '*', '*', '*', '*'],
                                               [' ', '*', '*', '*', '*', ' ', ' ', '*']])


