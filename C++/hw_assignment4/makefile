
Dungeon: Entity.o Game.o Room.o main.o
	g++ Entity.o Game.o Room.o main.o -o Dungeon

Entity.o: Entity.cpp Entity.h
	g++ -c Entity.cpp

Game.o: Game.cpp Game.h Room.h Entity.h
	g++ -c Game.cpp 

Room.o: Room.cpp Room.h Entity.h
	g++ -c Room.cpp

main.o: main.cpp Game.h Room.h Entity.h
	g++ -c main.cpp

Clean:
	rm -f main.o Room.o Game.o Entity.o Dungeon