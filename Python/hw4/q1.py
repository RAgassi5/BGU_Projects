def word_reconstruction(target_word, char_list):
    """
    this function determines whether a word can be built out of a given letter list
    :param target_word: the chosen word
    :param char_list: a list of letters
    :return: a boolean if possible
    """
    # Your Code Here
    return word_recursion(target_word, char_list, 0, 0)


def word_recursion(target_word, char_list, char_list_index, target_word_index):
    """
    a recursive function that deconstructs the target word, and checks if the word can be constructed
    :param target_word: the chosen word
    :param char_list: a list of letters
    :param char_list_index: current index of the char_list
    :param target_word_index: current index of the target_word
    :return: this function returns a boolean whether the target word can be constructed
    """
    if len(target_word) == 0 or target_word_index == len(target_word):
        return True
    if char_list_index == len(char_list):
        return False

    if char_list[char_list_index] == target_word[target_word_index]:
        target_word_index += 1
        return word_recursion(target_word, char_list, char_list_index, target_word_index)

    else:
        char_list_index += 1
        return word_recursion(target_word, char_list, char_list_index, target_word_index)


