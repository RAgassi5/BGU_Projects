import copy


def merge_zigzag(list1, list2):
    # Your Code Here
    """
    this function calls the recursive function
    :param list1: first number list
    :param list2: second number list
    :return: the longest merged zig-zag list
    """
    return long_zigzag_list([], [], list1, list2, 0, 0)


def is_zigzag(lst, list_index):
    """
    this function checks whether a list is zig-zag
    :param lst: the list that needs to be checked
    :param list_index: the index of the input list
    :return: a boolean if the list is zig-zag
    """
    if list_index == len(lst) - 1:
        return True

    if len(lst) <= 2:
        return True

    else:
        if lst[list_index] < (lst[list_index + 1] / 2) and lst[list_index] < lst[list_index - 1] / 2:
            return is_zigzag(lst, list_index + 1)

        if lst[list_index] > (lst[list_index + 1]) * 2 and lst[list_index] > (lst[list_index - 1]) * 2:
            return is_zigzag(lst, list_index + 1)

    return False


def long_zigzag_list(my_zigzag_list, zigzag_copy, list1, list2, index1, index2):
    """
    this recursive function merges the two given lists and gives the longest zig-zag list
    :param my_zigzag_list: the longest zig-zag list
    :param zigzag_copy: a copy of current my_zigzag_list
    :param list1: first inputted number list
    :param list2: second inputted number list
    :param index1: first list's index
    :param index2: second list's index
    :return: the longest zig-zag list
    """

    if index1 == len(list1) and index2 == len(list2):
        return zigzag_copy

    options_list = []

    if index1 < len(list1):
        if is_zigzag(zigzag_copy, 1) is True:
            new_list = zigzag_copy + [list1[index1]]
        else:
            new_list = zigzag_copy[:-1] + [list1[index1]]

        option1 = long_zigzag_list(my_zigzag_list, new_list, list1, list2, index1 + 1, index2)
        option3 = long_zigzag_list(my_zigzag_list, new_list[:-1], list1, list2, index1 + 1, index2)

        options_list.append(option1)
        options_list.append(option3)

    if index2 < len(list2):
        if is_zigzag(zigzag_copy, 1) is True:
            new_list2 = zigzag_copy + [list2[index2]]
        else:
            new_list2 = zigzag_copy[:-1] + [list2[index2]]

        option2 = long_zigzag_list(my_zigzag_list, new_list2, list1, list2, index1, index2 + 1)
        option4 = long_zigzag_list(my_zigzag_list, new_list2[:-1], list1, list2, index1, index2 + 1)

        options_list.append(option2)
        options_list.append(option4)

    max_list = recursive_max(options_list, [], 0)
    if not is_zigzag(max_list, 1):
        return max_list[:-1]
    else:
        return max_list


def recursive_max(input_list, biggest, index):
    """
    this recursive function checks the max length
    :param input_list: a list of all possible combinations
    :param biggest: the longest list
    :param index: the input_list's index
    :return: the longest list out of the options given
    """
    if index == len(input_list):
        return biggest

    if len(input_list[index]) > len(biggest):
        biggest = input_list[index]

    return recursive_max(input_list, biggest, index + 1)


