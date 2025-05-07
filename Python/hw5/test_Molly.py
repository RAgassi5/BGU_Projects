from unittest import TestCase
from Molly import Molly


class TestMolly(TestCase):
    def setUp(self):
        self.name = "Super Molly"
        self.age = 12
        self.x = 12
        self.y = 15
        self.direction_h = 0
        self.direction_v = 1
        self.default_molly_size = (8, 3)

        self.my_molly1 = Molly(self.name, self.age, self.x, self.y, self.direction_h, self.direction_v)

    def test_init(self):
        self.assertEqual(self.my_molly1.name, self.name)
        self.assertEqual(self.my_molly1.age, self.age)
        self.assertEqual(self.my_molly1.x, self.x)
        self.assertEqual(self.my_molly1.y, self.y)
        self.assertEqual(self.my_molly1.directionH, self.direction_h)
        self.assertEqual(self.my_molly1.directionV, self.direction_v)
        self.assertEqual(self.my_molly1.width, 8)
        self.assertEqual(self.my_molly1.height, 3)
        self.assertEqual(self.my_molly1.food, 10)

    def test_get_animal(self):
        self.assertEqual(self.my_molly1.get_animal(), [[' ', '*', '*', '*', '*', ' ', ' ', '*'],
                                                       ['*', '*', '*', '*', '*', '*', '*', '*'],
                                                       [' ', '*', '*', '*', '*', ' ', ' ', '*']])

    def test_get_position(self):
        self.assertEqual(self.my_molly1.get_position(), (self.x, self.y))

    def test_get_direction_h(self):
        self.assertEqual(self.my_molly1.get_directionH(), self.direction_h)

    def test_get_direction_v(self):
        self.assertEqual(self.my_molly1.get_directionV(), self.direction_v)

    def test_get_size(self):
        self.assertEqual(self.my_molly1.get_size(), self.default_molly_size)

    def test_dec_food(self):
        self.my_molly1.dec_food()
        self.assertEqual(self.my_molly1.food, 9)

    def test_add_food(self):
        self.my_molly1.add_food(12)
        self.assertEqual(self.my_molly1.food, 22)

    def test_inc_age(self):
        self.my_molly1.inc_age()
        self.assertEqual(self.my_molly1.age, self.age + 1)

    def test_move(self):
        self.my_molly1.move()
        self.assertEqual(self.my_molly1.x, self.x - 1)
        self.assertEqual(self.my_molly1.y, self.y - 1)

    def test_set_direction_h(self):
        self.my_molly1.set_directionH(0)
        self.assertEqual(self.my_molly1.directionH, 0)

    def test_set_direction_v(self):
        self.my_molly1.set_directionV(1)
        self.assertEqual(self.my_molly1.directionV, 1)

    def test_starvation(self):
        self.my_molly1.food = 7
        self.assertFalse(self.my_molly1.starvation())

        self.my_molly1.food = 0
        self.assertTrue(self.my_molly1.starvation())

    def test_die(self):
        self.assertFalse(self.my_molly1.die())
        self.my_molly1.age = 120
        self.assertTrue(self.my_molly1.die())
