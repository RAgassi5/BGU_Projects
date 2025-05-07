def remove_spaces(text):
    my_str = ""
    secondary_str = ""
    for letter in text:
        if letter == " ":
            if secondary_str == "":
                secondary_str = secondary_str + letter
            elif secondary_str == " ":
                    secondary_str = secondary_str + letter
                    if secondary_str != "" or secondary_str != " ":
                        my_str = my_str + " "
                        secondary_str = ""

        elif letter != " ":
            if secondary_str == " ":
                my_str = my_str + " " + letter
                secondary_str = ""
            else:
                my_str = my_str + letter
        else:
            return text
    return my_str







   if len(spaces_list) == 1:
                my_str = my_str + " "
                spaces_list = []
            elif len(spaces_list) >1:
                if my_str[-1] == " ":
                    continue
                else:
                    my_str = my_str + " "
                    spaces_list = []