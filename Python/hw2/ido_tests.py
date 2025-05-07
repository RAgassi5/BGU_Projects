# ************************ QUESTION 1.1 **************************
### WRITE CODE HERE
def generate_pascals_triangle(rows):
    """
    this function generates a big list consisting of smaller lists, each representing a row in the pascale triangle
    :param rows: the amount of rows that need to be generated
    :return: a list made of smaller lists, each representing a row in the pascal triangle
    """
    pascale_triangle = []
    if rows == 0:
        pascale_triangle = []
    else:
        pascale_triangle.append([1])
        for i in range(0, rows - 1):
            row_list = [1]
            rows = 0 + i
            previous_row = pascale_triangle[-1]
            for j in range(0, rows):
                row_list.append(previous_row[j] + previous_row[j + 1])
            row_list.append(1)
            pascale_triangle.append(row_list)

    return pascale_triangle


# ************************ QUESTION 1.2 **************************
### WRITE CODE HERE
def print_pascals_triangle(triangle):
    """
    this function receives a big list consisting small lists, each representing a row in the pascale triangle
    the function prints out the pascale triangle


    :param triangle: a long list consisting lists representing pascal triangle's rows
    :return: prints out the pascal triangle
    """
    if triangle == []:
        print("")
    else:
        base_long = base_length(triangle[-1])
        for i in range(0, len(triangle)):
            my_str = ""
            current_row = triangle[i]
            for j in range(len(current_row)):
                my_str = my_str + str(current_row[j])
                if j != len(current_row) - 1:
                    my_str = my_str + " "

            print(my_str.center(base_long))


def base_length(base):
    """
    this function calculates the base length of pascal triangle

    :param base:a list of numbers
    :return: the amount of digits in the triangle's base
    """
    base_str = ""
    for i in range(len(base)):
        current_num = base[i]
        base_str = base_str + str(current_num) + " "

    return len(base_str) - 1


if __name__ == '__main__':
    ##print(generate_pascals_triangle(0))
    print_pascals_triangle(generate_pascals_triangle(6))
    ##print(base_length())
    print(base_length.__doc__)
