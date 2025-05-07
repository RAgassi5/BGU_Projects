import random
# ************************ QUESTION 2.1 **************************
### WRITE CODE HERE
def split_words(game_words):
    """
    this function receives a string consisting of "$" and game words, the function separates game words from the "$" and
    creates the words for the game
    :param game_words: a string consisting of words with "$" between them
    :return: a list consisting the words for the game
    """
    word_list = []
    word_str = ""
    for i in range(0, len(game_words)):
        input_letter = game_words[i]

        if input_letter != "$":
            word_str = word_str + input_letter
        elif input_letter == "$":
            word_list.append(word_str)
            word_str = ""
    word_list.append(word_str)
    return word_list


# ************************ QUESTION 2.2 **************************
### WRITE CODE HERE
def get_guess_result(secret_word, guess_letter, currunt_word):
    """
    this function receives a word, a guessed letter and a status on the completion of the word
    :param secret_word: the chosen game word
    :param guess_letter: a guessed letter
    :param currunt_word: the status of the completion of the secret word
    :return: the current letter placement in the word
    """
    for i in range(0, len(secret_word)):
        input_letter = secret_word[i]
        if input_letter == guess_letter:
            currunt_word[i] = guess_letter
    return currunt_word


# ************************ QUESTION 2.3 **************************
def choose_secret_word(words_list):
    return random.choice(words_list)


### WRITE CODE HERE
def print_current_word_placements(current_word):
    """
    this function prints out the letter placement in the game word
    :param current_word: the chosen game word
    :return: the letter placement in the chosen word
    """
    str_word = ""
    for i in range(0, len(current_word)):
        str_word = str_word + current_word[i]
        if i != len(current_word)-1:
            str_word = str_word + " "
    if "_" in str_word:
        print(str_word)


def create_word_template(received_word):
    """
    this function creates the template for the chosen word, placing "_" to represent each letter in the chosen game word
    :param received_word: the chosen game word
    :return: a template of the letters in the chosen game word
    """
    new_word = []
    for i in range(0, len(received_word)):
        new_word.append("_")
    return new_word


def play_hangman(game_words):
    """
    this function runs the hangman game, and lets the user play
    :param game_words: the list of game words
    :return: prints out to the console the process of the game
    """
    chosen_word = choose_secret_word(split_words(game_words))
    current_word = create_word_template(chosen_word)
    length_word = str(len(chosen_word))
    print("Welcome to Hangman Game!")
    player_attempts = input("Enter number of attempts: >? ")
    print("The word has " + length_word + " letters. You have " + player_attempts + " attempts.")
    print_current_word_placements(current_word)

    used_letters = []
    while "_" in current_word:
        player_guess = input("Guess a letter: >? ")
        word_answer = get_guess_result(chosen_word, player_guess, current_word)
        if player_guess in used_letters:
            print("You already guessed that letter.")
            print_current_word_placements(word_answer)
        else:
            if player_guess in chosen_word:
                print_current_word_placements(word_answer)
                used_letters.append(player_guess)

            elif player_guess not in chosen_word:
                player_attempts = int(player_attempts) - 1
                print("Wrong guess! Attempts remaining: " + (str(int(player_attempts))))
                if player_attempts == 0:
                    print("Sorry, you've run out of attempts. The word was: " + chosen_word)
                    break
                print_current_word_placements(word_answer)
                used_letters.append(player_guess)

    if "_" not in current_word:
        print("Congratulations! You guessed the word: " + chosen_word)


if __name__ == '__main__':
   play_hangman("romi$queen$love$roii")
