# ************************ QUESTION 1.1 **************************
### WRITE CODE HERE
def generate_pascals_triangle(rows):
    "the function generates a big list consisting of smaller lists, each representing a row in the pascale triangle"
    pascale_triangle = []
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
def generate_base_length(triangle):
    "the function determines the length of the base of the triangle, later used to understand where the center of the triangle is"
    last_row = triangle[-1]
    base_len = 0
    for M in range(0, len(triangle[-1])):
        final_row = last_row[M]
        final_row = len(str(final_row))
        base_len = base_len + final_row + 2
    return base_len - 2


def print_pascals_triangle(triangle):
    "the function prints out to the user the pascal triangle, based on the rows given"
    base_length = generate_base_length(triangle)
    for k in range(0, len(triangle)):
        previous_row = triangle[k]
        current_num = previous_row
        my_rows = ""
        for m in range(0, len(previous_row)):
            my_rows = my_rows + (str(current_num[m]).center(2)) + " "
        ##print(my_rows.center(generate_base_length()," "))
        x = my_rows.center(base_length)
        print(x)


if __name__ == '__main__':
   ## pascals_triangle = generate_pascals_triangle(10)
    ##print_pascals_triangle(pascals_triangle)
    print_pascals_triangle(generate_pascals_triangle(8))