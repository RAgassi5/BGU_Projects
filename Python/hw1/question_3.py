# ************************ HOMEWORK 1 QUESTION 3 **************************
def question_3(input_num):

    ### WRITE CODE HERE
    for j in range(0, input_num):          ## a "for" loop that creates a list with the input number
        my_list = [input_num]
        for i in range(0, input_num + 1):        ## a "for" loop that checks two conditions: wether to add an "*" or finsish the list with the input number
            if i < input_num:
                my_list.append("*")
            elif i == input_num:
                my_list.append(input_num)
        input_num = input_num - 1                   ## at the end of the run we get a list of the wanted format, now I need to change it into a string

        my_str = ""
        for k in range(0, len(my_list)):        ## for each run the "for" loop takes a value from the list I created before, and turns it into a string that includes all of my list values
            my_str = my_str + str(my_list[k])
        print(my_str)


