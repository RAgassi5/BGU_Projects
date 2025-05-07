#!/bin/bash

inputed_arguments=("$@")        #take all given arguments and place in an array


if (( ${#inputed_arguments[@]}<3 ))     #checks if there are less than 3 given arguments
    then
        echo >&2 "Number of parameters received : ${#inputed_arguments[@]}"
        echo "Usage : wordFinder.sh <valid file name> [More Files] ... <char> <length>"
        exit 1
else
    second_to_last="${inputed_arguments[${#inputed_arguments[@]}-2]}"   #the second to last argument
    last="${inputed_arguments[${#inputed_arguments[@]}-1]}"             #the last argument

    if [[ (${#second_to_last} -ne 1 || ! "$second_to_last" =~ ^[a-zA-Z0-9]+$) && ("$last" -gt 0 && "$last" =~ ^[0-9]+$) ]]      #checks if the second to last given arguemnt is valid, if not the script will terminate 
        then
            echo >&2 "Only one char needed : $second_to_last"
            echo "Usage : wordFinder.sh <valid file name> [More Files] ... <char> <length>"        ##check if the output is correct like in the pdf!!!
            exit 1
            
    
    elif [[ (${#second_to_last} -eq 1 && "$second_to_last" =~ ^[a-zA-Z0-9]+$) && ("$last" -le 0 || ! "$last" =~ ^[0-9]+$) ]]      #checks if the last given argument is valid, if not the script will terminate  
        then 
            echo >&2 "Not a positive number : $last"
            echo "Usage : wordFinder.sh <valid file name> [More Files] ... <char> <length>"
            exit 1
    
    elif [[ (${#second_to_last} -ne 1 || ! "$second_to_last" =~ ^[a-zA-Z0-9]+$) && ("$last" -le 0 || ! "$last" =~ ^[0-9]+$) ]]      #checks if the second to last and last given arguments are valid, if not the script will terminate 
        then
            echo >&2 "Only one char needed : $second_to_last"                           
            echo >&2 "Not a positive number : $last"
            echo "Usage : wordFinder.sh <valid file name> [More Files] ... <char> <length>"
            exit 1

    else                                                                        #we will reach this part of the code only if the arguments given meet the requirements
        exit_switch=0
        for ((i=0; i<${#inputed_arguments[@]}-2; i++))                          #for loop that checks wether the inputed files exist and are not directories
            do
            current_file="${inputed_arguments[i]}"
            if [[ ! -e "$current_file" || -d "$current_file" ]]
                then 
                exit_switch=1
                echo >&2 "File does not exist : $current_file"
            fi
        done

        if [[ exit_switch -eq 1 ]]                                          #if the exit_switch is 1, we have at least 1 problematic file - we print the proper usage and terminate the script
            then
            echo "Usage : wordFinder.sh <valid file name> [More Files] ... <char> <length>"
            exit 1

        fi
    fi    
fi        

touch wordSearch.txt                                                #creates an empty text file
lower_case=$(echo "${inputed_arguments[${#inputed_arguments[@]}-2]}" | tr '[:upper:]' '[:lower:]')   #tranlastes the given argument to a lower case
upper_case=$(echo "${inputed_arguments[${#inputed_arguments[@]}-2]}" | tr '[:lower:]' '[:upper:]')   #tranlastes the given argument to an upper case

for ((i=0; i<${#inputed_arguments[@]}-2; i++))                      #loop on all the words in the given files             
    do          
    current_file="${inputed_arguments[i]}"             
    for word in $(cat "$current_file" | tr -c 'a-zA-z0-9' ' ');        #takes the words of the current file and anything that is not part of a-z or A-Z is replaced with a space
        do
        if [[ ($word =~ ^$lower_case || $word =~ ^$upper_case) && ${#word} -ge $last ]]     #checks if the current word meets the argument's requirements
            then
            echo $(echo "$word" | tr '[:upper:]' '[:lower:]') >> wordSearch.txt             #the words that meet the requirements are outputted into the file we created
        fi
        done       
done

sort "wordSearch.txt" | uniq -c | sort -n | awk '{$1=$1}1'               #sort the chosen words by amount of apearances and then by alphabetical order, print out the chosen words
rm wordSearch.txt                                                     # "erasing our tracks" - delete our temporary text file that we created before           



