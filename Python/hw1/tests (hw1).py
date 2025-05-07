import question_1
import question_2
from hw1 import question_3
import question_4


def test_question_1():
    x = 1  # Change me!
    a = 3  # Change me!
    question_1.question_1(x, a)


def test_question_2():
    spell = "Riddikulus!"  # Change me!
    witches_num = 3  # Change me!
    question_2.question_2(spell,witches_num)


def test_question_3():
    input_num = 6  # Change me!
    question_3.question_3(input_num)


def test_question_4():
    input_list = [1, 6, 3, 0]  # Change me!
    question_4.question_4(input_list)


if __name__ == '__main__':
    test_question_1()
    test_question_2()
    test_question_3()
    test_question_4()
