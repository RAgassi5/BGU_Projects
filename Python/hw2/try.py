import random
# ************************ QUESTION 2.1 **************************
### WRITE CODE HERE
def split_words(game_words):
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
    str_word = ""
    for i in range(0, len(current_word)):
        str_word = str_word + current_word[i]
        if i != len(current_word)-1:
            str_word = str_word + " "
    return str_word


def create_word_template(received_word):
    new_word = []
    for i in range(0, len(received_word)):
        new_word.append("_")
    return new_word


def play_hangman(game_words):
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

        else:
            if player_guess in chosen_word:
                current_word = print_current_word_placements(word_answer)
                if "_" not in current_word:
                    continue
                else:
                    print(current_word)
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
   play_hangman("arrange")
