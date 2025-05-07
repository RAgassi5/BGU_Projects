# ************************ HOMEWORK 1 QUESTION 4 **************************
def question_4(input_list):
    ### WRITE CODE HERE
    perfect_num = []                ##open an empty list, that only perfect numbers can be added to
    for i in range(0, len(input_list)):  ## create a "for" loop to decide whether the number is a perfect number or not
        valid_dividers = []
        input_num = input_list[i]

        if input_num == 0:                  ## once a 0 is in the given list, we stop the run
            break
        for j in range(1, input_num):   ## need to check if the given number is considered a perfect number based on the conditions that define such a number
            if input_num % j == 0:
                valid_dividers.append(j)
        if sum(valid_dividers) == input_num:

            perfect_num.append(input_num)           ## if the given number is a perfect number, it is added to the perfect number list

    if len(perfect_num) != 0:
        print(sum(perfect_num)/len(perfect_num))  ## if the length of the perfect number list is not 0, we do the average
    else:
        print(sum(perfect_num))  ## in any other case we just sum up the list, which will always equal 0, because there were no perfect numbers
















