import math

# Global Variables
stop_words = [
    "a", "an", "and", "are", "as", "at", "be", "by", "for", "from", "has", "have",
    "in", "is", "it", "its", "of", "on", "that", "the", "to", "was", "were", "with",
    "i", "you", "we", "he", "she", "it", "my", "your", "our", "his", "her", "its", "their",
    "this", "these", "those", "here", "there", "where", "when", "how", "all", "any",
    "many", "much", "more", "most", "other", "some", "such"
]
punctuations = ['.', ',', ':', ';', '!', '?', '"', "'", '(', ')', '[', ']', '{', '}',
                '-', '/', '\\', '&', '@', '#', '$', '%', '*', '_', '~']

corpus = {1: "The cat played the piano",
          2: "5 cats are playing ball in the backyard!",
          3: "The grand piano is in the house",
          }


####### Part A #########
def remove_punctuation(text):
    """
    this function receives a string with punctuation and returns the string with space instead of punctuation
    :param text: any string with punctuation
    :return: string with spaces instead of punctuation
    """
    my_str = ""
    for letter in text:
        if letter not in punctuations:
            my_str = my_str + letter
        elif letter in punctuations:
            my_str = my_str + " "
        else:
            return text
    return my_str


def remove_digits(text):
    """
    this function receives any text, if the text includes digits the function removes them
    :param text: any string with or without digits
    :return: the same input string without the digits
    """
    my_str = ""
    digits_lst = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"]
    for letter in text:
        if letter not in digits_lst:  ## might need to add the space at the end , like the pdf
            my_str = my_str + letter
        elif letter in digits_lst:
            my_str = my_str + ""
        else:
            return text
    return my_str


def remove_spaces(text):  ### make sure logic is working for all cases
    """
    this function receives a string with many spaces and replaces it with 1 space
    :param text: any string with or without spaces
    :return: the same string with 1 space in place of long spaces
    """
    my_str = ""
    spaces_list = []
    for letter in text:
        if letter == " ":
            spaces_list.append(letter)
        elif letter != " ":
            if my_str == "":
                my_str = my_str + letter
                spaces_list = []
            elif len(spaces_list) >= 1:
                my_str = my_str + " " + letter
                spaces_list = []
            else:
                my_str = my_str + letter
        else:
            return text
    return my_str


def remove_stopwords(words_list):
    """
    this function receives a list of words and returns a new list without the stop words
    :param words_list: a list of words
    :return: a new list with no stop words
    """
    new_list = []
    for word in words_list:
        if word not in stop_words:
            new_list.append(word)
        elif word in stop_words:
            continue
    return new_list


def stemming(words_list):  ###check that only happens to ending of word!
    """
    this function receives a list of words and returns a new list consisting of the stem words with no endings
    :param words_list: a list of words
    :return: a list of words with no endings, only the base word
    """
    new_list = []
    ending_1 = "ies"
    ending_2 = "sses"
    ending_3 = "s"
    ending_4 = "ed"
    ending_5 = "ing"
    for word in words_list:
        if ending_1 in word:
            new_list.append(word.replace(ending_1, "y"))
        elif ending_2 in word:
            new_list.append(word.replace(ending_2, "ss"))
        elif ending_3 in word and word[-1] == ending_3:
            new_list.append(word[:-1])
        elif ending_4 in word:
            new_list.append((word.replace(ending_4, "")))
        elif ending_5 in word:
            new_list.append(word.replace(ending_5, ""))
        else:
            new_list.append(word)
    return new_list


def preprocessing(text):
    """
    this function depends on the other functions created before, the function receives a string a returns a list consisting
    only of important words, based on the assignment's rules
    :param text: any string
    :return: a list of important words, based on our rules
    """
    new_text = (remove_spaces(remove_digits(remove_punctuation(text)))).lower()
    text_list = new_text.split(" ")
    updated_list = stemming(remove_stopwords(text_list))
    if "" in updated_list:
        return updated_list.remove("")
    else:
        return updated_list


###### Part B #######
def get_documents_data(corpus):
    """
    this function receives a dictionary of files that need to be processed and returns a new dictionary with the
    processed data
    :param corpus: a dictionary of files
    :return: a dictionary with the processed data, keys: document ID, values: number of words in document
    """
    documents_data = {}
    for key in corpus:
        documents_data[key] = len(preprocessing(corpus[key]))
    return documents_data


def create_inverted_index(corpus):
    """
    this function receives a dictionary and returns a new dictionary showing the count of appearances of each word in
    a specific document
    :param corpus: a dictionary
    :return: a new dictionary with number of appearances of each word
    """
    inverted_index = {}
    for key in corpus:
        current_value = corpus[key]
        processed_value = preprocessing(current_value)
        for word in processed_value:
            if inverted_index.get(word):
                if inverted_index[word].get(key):
                    inverted_index[word][key] = inverted_index[word][key] + 1
                else:
                    inverted_index[word][key] = 1
            else:
                inverted_index[word] = {key: 1}

    return inverted_index


def add_to_data(inverted_index, documents_data, doc_id, text):
    """
    this function updates two dictionaries with a new document
    :param inverted_index: a dictionary with the number of appearances for each word
    :param documents_data: a dictionary with the amount of words in eah document
    :param doc_id: number of ID for new document
    :param text: the content of the new document
    :return: updated inverted_index and document_data
    """
    processed_text = preprocessing(text)
    documents_data[doc_id] = len(processed_text)

    for word in processed_text:
        if inverted_index.get(word):
            if inverted_index[word].get(doc_id):
                inverted_index[word][doc_id] = inverted_index[word][doc_id] + 1
            else:
                inverted_index[word][doc_id] = 1
        else:
            inverted_index[word] = {doc_id: 1}

    return inverted_index, documents_data


def remove_from_data(inverted_index, documents_data, doc_id):
    """
    removes a specific document from the dictionaries(inverted_index and documents_data)
    :param inverted_index: a dictionary with the number of appearances for each word
    :param documents_data: a dictionary with the amount of words in eah document
    :param doc_id:  the ID of the document to be removed
    :return: updated dictionaries inverted_index and documents_data without the removed document
    """
    documents_data.pop(doc_id, None)

    for key in inverted_index:
        inner_dictionary = inverted_index[key]
        inner_dictionary.pop(doc_id, None)

    keys_to_delete = []
    for key in inverted_index:
        if inverted_index[key] == {}:
            keys_to_delete.append(key)

    for key in keys_to_delete:
        inverted_index.pop(key)

    return inverted_index, documents_data


###### Part C #######
def calculate_tf_idf(word, doc_id, inverted_index, documents_data):
    """
    this function calculates the TF-IDF value of the word in the document
    :param word:a word from a document
    :param doc_id: an existing document ID in the corpus
    :param inverted_index: a dictionary of amount of appearances in each document
    :param documents_data: a dictionary of amount of word in each document
    :return: the value of TF-IDF
    """

    docs_in_corpus = (len(documents_data.keys()))
    amount_of_docs_with_word = len(inverted_index[word].keys())
    IDF = math.log((docs_in_corpus / amount_of_docs_with_word), 2)

    total_words_in_doc = documents_data[doc_id]
    if inverted_index[word].get(doc_id):
        TF = inverted_index[word][doc_id] / total_words_in_doc
    else:
        TF = 0 / total_words_in_doc

    return round(TF * IDF, 3)


def get_scores_of_relevance_docs(query, inverted_index, documents_data):
    """
    this function calculates the relevance of a document based on the words inserted
    :param query: a list of words to be searched for
    :param inverted_index: a dictionary of amount of appearances in each document
    :param documents_data: a dictionary of amount of word in each document
    :return: a dictionary with a document ID and it's relevance score
    """
    score_of_relevance = {}
    for word in query:
        if inverted_index.get(word):
            for key in inverted_index[word]:
                calculation = calculate_tf_idf(word, key, inverted_index, documents_data)
                if score_of_relevance.get(key):
                    score_of_relevance[key] = round((score_of_relevance[key] + calculation), 3)
                else:
                    score_of_relevance[key] = calculation
        else:
            continue

    return score_of_relevance


###### Part D #######
def menu(corpus):
    """
    this function creates the user interface for the database
    :param corpus: a dictionary of documents
    :return: 
    """
    menu_inverted_index = create_inverted_index(corpus)
    menu_get_documents = get_documents_data(corpus)
    while True:
        choice = input('Choose an option from the menu:\n\t(1) Insert a query.\n\t(2) Add document to corpus.\n\t(3) Calculate TF-IDF Score for a word in a document.\n\t(4) Delete a document from the corpus.\n\t(5) Exit.\nYour choice: ')
        if choice == "1":
            query = str(input("Write your query here: "))

            query_choice = input('Choose the type of results you would like to retrieve:\n\t(A) All relevant documents.\n\t(B) The most relevant document.\n\t(C) Back to the main menu.\nYour choice: ')
            processed_query = preprocessing(query)
            score_dict = get_scores_of_relevance_docs(processed_query, menu_inverted_index, menu_get_documents)
            doc_id_lst = sorted(score_dict.keys())
            while True:
                if query_choice == "A":
                    for doc_id in score_dict:
                        print(f'{doc_id} : {score_dict[doc_id]}')
                    query_choice = input('Choose the type of results you would like to retrieve:\n\t(A) All relevant documents.\n\t(B) The most relevant document.\n\t(C) Back to the main menu.\nYour choice: ')

                elif query_choice == "B":

                    max_value = max(get_scores_of_relevance_docs(processed_query, menu_inverted_index, menu_get_documents).values())
                    for key in doc_id_lst:
                        value_in_key = get_scores_of_relevance_docs(processed_query, menu_inverted_index, menu_get_documents)[key]
                        if value_in_key == max_value:
                            print(f'The most relevant document is {key} with a score of {value_in_key}')
                    query_choice = input(
                        'Choose the type of results you would like to retrieve:\n\t(A) All relevant documents.\n\t(B) The most relevant document.\n\t(C) Back to the main menu.\nYour choice: ')

                elif query_choice == "C":
                    break

                else:
                    print(f'Invalid choice. Please select a valid option.')
                    query_choice = input(
                        'Choose the type of results you would like to retrieve:\n\t(A) All relevant documents.\n\t(B) The most relevant document.\n\t(C) Back to the main menu.\nYour choice: ')

        elif choice == "2":
            user_doc = int(input(f'Insert the document ID: '))
            while True:
                if menu_get_documents.get(user_doc):
                    print(f'The document ID {user_doc} is already in corpus.')
                    user_doc = int(input(f'Insert the document ID: '))
                else:
                    user_text = str(input(f'Insert the text of the document: '))
                    add_to_data(menu_inverted_index, menu_get_documents, user_doc, user_text)
                    print(f'Document {user_doc} was successfully added!')
                    break

        elif choice == "3":
            user_input_3 = int(input(f'Insert the document ID: '))
            while True:

                if menu_get_documents.get(user_input_3) is None:
                    print(f'The document ID {user_input_3} is not in corpus.')
                    user_input_3 = int(input(f'Insert the document ID: '))

                else:
                    user_word_3 = str(input(f'Insert a word: '))
                    processed_word_3 = preprocessing(user_word_3)[0]

                    if menu_inverted_index.get(processed_word_3) is None:
                        print(f'The word {processed_word_3} is not in corpus.')
                        processed_word_3 = str(preprocessing(user_word_3)[0])

                    else:
                        processed_word_3 = str(preprocessing(user_word_3)[0])
                        calculation = calculate_tf_idf(processed_word_3, user_input_3, menu_inverted_index,menu_get_documents)
                        print(f'TF-IDF of the word {processed_word_3} in document {user_input_3} is: {calculation}')
                        break

        elif choice == "4":
            while True:
                user_input_4 = int(input(f'Insert the document ID: '))
                if menu_get_documents.get(user_input_4) is None:
                    print(f'The document ID {user_input_4} is not in corpus.')

                else:
                    remove_from_data(menu_inverted_index, menu_get_documents, user_input_4)
                    print(f'Document {user_input_4} is successfully deleted.')
                    break

        elif choice == "5":
            return

        else:
            print(f'Invalid choice. Please select a valid option.')


if __name__ == '__main__':
    menu(corpus)