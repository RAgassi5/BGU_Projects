# ************************ HOMEWORK 1 QUESTION 2 **************************
def question_2(spell, witches_num):
    ##only an even number of witches apply for this condition
    ##each condition determines what spell to cast, in consideration to an even number of witches
    if witches_num % 2 == 0:
        if spell == "Alohomora!":
            print("Doors Unlocked")
        elif spell == "Nox!":
            print("Darkness")
        elif spell == "Lumos!":
            print("Light")
        elif spell == "Riddikulus!":
            print("")
        else:
            print("")
    ##only an odd number of witches apply for this condition
    ##each condition determines what spell to cast, in consideration to an odd number of witches
    else: ##witches_num % 2 == 1:
        if spell == "Alohomora!":
            print("Windows Unlocked")
        elif spell == "Nox!":
            print("")
        elif spell == "Lumos!":
            print("")
        elif spell == "Riddikulus!":
            print("Funny")
        else:
            print("")



