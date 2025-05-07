def estate_homogeneity(divisions, total_area):
    """
    this function determines whether an area division is homogenic or not
    :param divisions: a list of all the divisions made in the given area
    :param total_area: the total area given
    :return: a boolean if the divison is homogenic
    """
    # Your Code Here
    return estate_division(divisions, total_area, 0)


def estate_division(divisions, total_area, divisions_index):
    """
    this function uses recursion to decide whether the division is homogenic
    :param divisions: a list of numbers
    :param total_area: the total area to be divided
    :param divisions_index: the current divisions list index
    :return: a boolean if the division is homogenic or not
    """
    if len(divisions) == 0 or total_area == 0:
        return True
    if len(divisions) == divisions_index:
        return True
    expected_area_per_division = total_area / len(divisions)
    if type(divisions[divisions_index]) is list:
        is_list_homo = estate_division(divisions[divisions_index], expected_area_per_division, 0)
        if is_list_homo is True:
            return estate_division(divisions, total_area, divisions_index + 1)
        else:
            return False
    if type(divisions[divisions_index]) is int:
        if divisions[divisions_index] == expected_area_per_division:
            return estate_division(divisions, total_area, divisions_index + 1)
        else:
            return False

    return False



