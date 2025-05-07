def colcat(mat_a, mat_b):
    # Your Code Here
    """
    this function calls the recursive function
    :param mat_a: chosen matrix #1
    :param mat_b: chosen matrix #2
    :return: a matrix that adds matrix b to matrix 1
    """
    return recursion_colcat(mat_a, mat_b, 0, 0, [])


def recursion_colcat(mat_a, mat_b, index_a, index_b, new_mat):
    """
    this recursive function merges two matrices together
    :param mat_a: chosen matrix #1
    :param mat_b: chosen matrix #2
    :param index_a: index of matrix a
    :param index_b: index of matrix b
    :param new_mat: the new matrix containing the merged matrices
    :param inner_index_b: an inner index for matrix b
    :return: a new matrix containing the merged matrix a and matrix b
    """
    if index_a == len(mat_a) and index_b == len(mat_b):
        return new_mat

    if len(mat_a) == len(mat_b):
        new_mat.append(mat_a[index_a] + mat_b[index_b])
        return recursion_colcat(mat_a, mat_b, index_a + 1, index_b + 1, new_mat)


def vertical_split(input_mat):
    # Your Code Here
    """
    this function calls the recursive function
    :param input_mat: the wanted matrix to be divided
    :return: 2 new matrices
    """
    mat_division = len(input_mat)
    return (recursion_vertical_split(input_mat, create_template(mat_division, 0, 0, 0),create_template(mat_division, 0, 0, 0), 0, 0))


def recursion_vertical_split(input_mat, new1, new2, input_index, inner_index):
    """
    this recursive function vertically splits the given matrix
    :param input_mat: the matrix that will be divided
    :param new1: new matrix #1
    :param new2: new matrix #2
    :param input_index: the index of the given matrix
    :param inner_index:the inner index of the given matrix
    :return: new matrices with the divided columns
    """
    divided_to = len(input_mat[0]) // 2
    if input_index == len(input_mat):
        return new1, new2

    if inner_index == len(input_mat[input_index]):
        return recursion_vertical_split(input_mat, new1, new2, input_index + 1, inner_index == 0)

    if inner_index < divided_to:
        new1[input_index].append(input_mat[input_index][inner_index])
        return recursion_vertical_split(input_mat, new1, new2, input_index, inner_index + 1)

    if inner_index > divided_to - 1:
        new2[input_index].append(input_mat[input_index][inner_index])
        return recursion_vertical_split(input_mat, new1, new2, input_index, inner_index + 1)


def create_template(amount_of_rows, template, index, status):
    """
    this function creates the template for the new matrices
    :param amount_of_rows: amount of rows wanted in each matrix
    :param template: none
    :param index: none
    :param status: none
    :return: returns a template of the new matrices
    """
    if index == amount_of_rows:
        return template

    if status == 0:
        template = []
        return create_template(amount_of_rows, template, 0, 1)
    else:
        template.append([])
        return create_template(amount_of_rows, template, index + 1, 1)


def rotate_mat_rec(input_mat):
    # Your Code Here - Bonus for 20% additional assignment grade!
    return


