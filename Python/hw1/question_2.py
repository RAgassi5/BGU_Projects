# ************************ HOMEWORK 1 QUESTION 2 **************************
def question_2(spell,witches_num):
    ##only an even number of witches apply for this condition
    ##each condition determines what spell to cast, in consideration to an even number of witches
    while int(witches_num % 2 == 0):
        if spell == "Alohomora!":
            print("Doors Unlocked")
            break
        elif spell == "Nox!":
            print("Darkness")
            break
        elif spell == "Lumos!":
            print("Light")
            break
        elif spell == "Riddikulus!":
            print("")
            break
        else:
            print("")
            break
    ##only an odd number of witches apply for this condition
    ##each condition determines what spell to cast, in consideration to an odd number of witches
    while int(witches_num % 2 == 1):
        if spell == "Alohomora!":
            print("Windows Unlocked")
            break
        elif spell == "Nox!":
            print("")
            break
        elif spell == "Lumos!":
            print("")
            break
        elif spell == "Riddikulus!":
            print("Funny")
            break
        else:
            print("")
            break

if __name__ == '__main__':
    question_2(4333,"Riddikulus!")