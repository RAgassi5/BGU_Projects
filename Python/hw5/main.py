import Aquarium
from Exceptions import *
WATERLINE = 2
def add_animal(myaqua):
    choice = 0
    while not 1 <= choice <= 5:
        print("Please select:")
        print("1. Scalar")
        print("2. Molly")
        print("3. Ocypode")
        print("4. Shrimp")
        try:
            choice = int(input("What animal do you want to put in the aquarium? "))
        except:
            choice = 0

    name = input("Please enter a name: ")
    age = 0
    while not 0 < age < 120:
        try:
            age = int(input("Please enter age: "))
        except:
            age = 0


    x, y = 0, 0
    while not 1 <= x <= (myaqua.aqua_width - 1):
        try:
            x = int(input("Please enter an X axis location (1 - %d): " % (myaqua.aqua_width - 1)))
        except:
            x = 0
    if choice == 1 or choice == 2:
        while not WATERLINE < y <= (myaqua.aqua_height - 1):
            try:
                y = int(input("Please enter an Y axis location (%d - %d): " % (WATERLINE + 1, myaqua.aqua_height - 1)))
            except:
                y = 0

    directionH, directionV = -1, -1
    while not (directionH == '0' or directionH == '1'):
        directionH = input("Please enter horizontal direction (0 for Left, 1 for Right): ")
    directionH = int(directionH)
    if choice == 1 or choice == 2:
        while not (directionV == '0' or directionV == '1'):
            directionV = input("Please enter vertical direction  (0 for Down, 1 for Up): ")
        directionV = int(directionV)

    try:
        if choice == 1:
            myaqua.add_animal(name, age, x, y, directionH, directionV, 'scalar')
        elif choice == 2:
            myaqua.add_animal(name, age, x, y, directionH, directionV, 'molly')
        elif choice == 3:
            myaqua.add_animal(name, age, x, myaqua.aqua_height, directionH, 0, 'ocypode')
        elif choice == 4:
            myaqua.add_animal(name, age, x, myaqua.aqua_height, directionH, 0, 'shrimp')
        elif choice == 5:
            myaqua.add_animal(name, age, x, myaqua.aqua_height, directionH, 0, 'cat')
    except NotAvailablePlace:
        print('The place is not available! Please try again later.')



if __name__ == '__main__':
    width = 0

    height = 0

    print('Welcome to "The OOP Aquarium"')
    while True:
        try:
            width = int(input("The width of the aquarium (Minimum 40): "))
            height = int(input("The height of the aquarium (Minimum 25): "))
            myaqua = Aquarium.Aquarium(width, height)
            break
        except:
            continue


    while True:
        choice = 0
        while True:
            print("Main menu")
            print("-" * 30)
            print("1. Add an animal")
            print("2. Print all animals")
            print("3. Drop food into the aquarium")
            print("4. Take a step forward")
            print("5. Take several steps")
            print("6. Exit")

            choice = input("What do you want to do? ")

            if choice == '1':
                add_animal(myaqua)
                print(repr(myaqua))
            elif choice == '2':
                print(myaqua)
            elif choice == '3':
                myaqua.feed_all()
            elif choice == '4':
                myaqua.next_step()
                print(repr(myaqua))
            elif choice == '5':
                while True:
                    steps = input("How many steps do you want to take? ")
                    try:
                        steps = int(steps)
                        break
                    except:
                        continue
                myaqua.several_steps(steps)
                print(repr(myaqua))
            elif choice == '6':
                print("Bye bye")
                exit()
            else:
                print('Please Choose a number between 1-6.')


