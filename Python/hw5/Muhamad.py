from unittest import TestCase
from Exceptions import InvalidInputException
from Molly import Molly



class TestMolly(TestCase):
    def test_molly_init(self):
        # name testing
        with self.assertRaises(InvalidInputException):
            animal1 = Molly("", 10, 20, 50, 0, 0)
        with self.assertRaises(InvalidInputException):
            animal2 = Molly(11, 10, 20, 50, 0, 0)
        with self.assertRaises(InvalidInputException):
            animal3 = Molly(True, 10, 20, 50, 0, 0)

        # age testing
        with self.assertRaises(InvalidInputException):
            animal4 = Molly("name", -10, 20, 50, 0, 0)
        with self.assertRaises(InvalidInputException):
            animal5 = Molly("name", 0, 20, 50, 0, 0)
        with self.assertRaises(InvalidInputException):
            animal6 = Molly("name", 120, 20, 50, 0, 0)
        with self.assertRaises(InvalidInputException):
            animal7 = Molly("name", 122, 20, 50, 0, 0)
        with self.assertRaises(InvalidInputException):
            animal8 = Molly("name", True, 20, 50, 0, 0)
        with self.assertRaises(InvalidInputException):
            animal9 = Molly("name", "122", 20, 50, 0, 0)

        # x - coordinate testing
        with self.assertRaises(InvalidInputException):
            animal10 = Molly("name", 10, 0, 50, 0, 0)
        with self.assertRaises(InvalidInputException):
            animal11 = Molly("name", 10, -1, 50, 0, 0)
        with self.assertRaises(InvalidInputException):
            animal12 = Molly("name", 10, True, 50, 0, 0)
        with self.assertRaises(InvalidInputException):
            animal13 = Molly("name", 10, "20", 50, 0, 0)

        # y - coordinate testing
        with self.assertRaises(InvalidInputException):
            animal14 = Molly("name", 10, 20, 0, 0, 0)
        with self.assertRaises(InvalidInputException):
            animal15 = Molly("name", 10, 20, -1, 0, 0)
        with self.assertRaises(InvalidInputException):
            animal16 = Molly("name", 10, 20, True, 0, 0)
        with self.assertRaises(InvalidInputException):
            animal17 = Molly("name", 10, 20, "50", 0, 0)

        # directionH testing
        with self.assertRaises(InvalidInputException):
            animal18 = Molly("name", 10, 20, 50, True, 0)
        with self.assertRaises(InvalidInputException):
            animal19 = Molly("name", 10, 20, 50, False, 0)
        with self.assertRaises(InvalidInputException):
            animal20 = Molly("name", 10, 20, 50, "0", 0)

        # directionV testing
        with self.assertRaises(InvalidInputException):
            animal21 = Molly("name", 10, 20, 50, 0, True)
        with self.assertRaises(InvalidInputException):
            animal22 = Molly("name", 10, 20, 50, 0, False)
        with self.assertRaises(InvalidInputException):
            animal23 = Molly("name", 10, 20, 50, 0, "0")

        # overall testing
        animal24 = Molly("myFish", 21, 5, 8, 1, 0)
        self.assertEqual("myFish", animal24.name)
        self.assertEqual(21, animal24.age)
        self.assertEqual((5, 8), animal24.get_position())
        self.assertEqual(5, animal24.x)
        self.assertEqual(8, animal24.y)
        self.assertEqual(1, animal24.directionH)
        self.assertEqual(0, animal24.directionV)
        self.assertEqual(10, animal24.food)
        self.assertEqual((8, 3), animal24.get_size())

    def test_animal_functions(self):
        molly_repr_left = ("  * * * *     *\n"
                           "* * * * * * * *\n"
                           "  * * * *     *")
        molly = Molly("animal", 1, 5, 10, 0, 0)
        self.assertEqual(repr(molly), molly_repr_left)
        molly = Molly("animal", 1, 5, 10, 0, 0)
        s = ("*     * * * *  \n"
             "* * * * * * * *\n"
             "*     * * * *  ")
        self.assertEqual(repr(molly), s)
        self.assertEqual(10, molly.food)
        molly.dec_food()
        self.assertEqual(9, molly.food)
        self.assertEqual(1, molly.age)
        molly.inc_age()
        self.assertEqual(2, molly.age)
        self.assertEqual((5, 10), molly.get_position())
        molly.move()
        self.assertEqual((6, 11), molly.get_position())
        self.assertEqual(False, molly.die())
        for i in range(120):
            molly.inc_age()
        self.assertEqual(True, molly.die())
        self.assertEqual(False , molly.starvation())
        for i in range(100) :
            molly.dec_food()
        self.assertEqual(True , molly.starvation())
