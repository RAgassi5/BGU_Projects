//
// Created by Roii Agassi on 20/01/2025.
//
#ifndef GAMEEXCEPTION_H
#define GAMEEXCEPTION_H
#include <exception>
#include <string>

//a class for exceptions thrown within the game
class GameException : public std::exception {
private:
    std::string ErrorMessage;
    public:
    //constructor for the game exception
    explicit GameException(const std::string& message) throw(): ErrorMessage(message) {};

    //destructor for GameException
    virtual ~GameException() throw() {};

    //override the what() function to determine what to print out to the user
   const char * what() const throw() {
        return ErrorMessage.c_str();
    }
};

//exception class for a memory problem
class MemoryException: public GameException {
    public:
    explicit MemoryException() throw(): GameException("Memory Problem") {};
    virtual ~MemoryException() throw() {};
};

//exception class for a file problem
class FileNotFoundException: public GameException {

    public:
    explicit FileNotFoundException() throw(): GameException("Invalid File. File does not exist.") {};
    virtual ~FileNotFoundException() throw() {};
};

//exception class for a room problem
class RoomException: public GameException {
    public:
    explicit RoomException() throw(): GameException("Invalid Room") {};
    virtual ~RoomException() throw() {};

};

//exception class for a value problem
class ValueException: public GameException {
    public:
    explicit ValueException()throw(): GameException("Invalid Value") {};
    virtual ~ValueException() throw() {};
};

//exception class for a player type problem
class PlayerException: public GameException {
    public:
    explicit PlayerException()throw(): GameException("Invalid Player Type") {} ;
    virtual ~PlayerException() throw() {};
};

#endif //GAMEEXCEPTION_H
