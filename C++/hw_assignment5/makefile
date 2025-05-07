Dungeon: Dragon.o Entity.o Fighter.o Game.o Goblin.o Monster.o Player.o Room.o Sorcerer.o main.o
	g++ Dragon.o Entity.o Fighter.o Game.o Goblin.o Monster.o Player.o Room.o Sorcerer.o main.o -o Dungeon

Dragon.o: Dragon.cpp Dragon.h Monster.h Entity.h
	g++ -c Dragon.cpp Dragon.h Monster.h Entity.h

Entity.o: Entity.cpp Entity.h
	g++ -c Entity.cpp Entity.h

Fighter.o: Fighter.cpp Fighter.h Player.h Entity.h Monster.h
	g++ -c Fighter.cpp Fighter.h Player.h Entity.h Monster.h

Game.o: Game.cpp Game.h Player.h Entity.h Monster.h Room.h GameException.h Fighter.h Sorcerer.h
	g++ -c Game.cpp Game.h Player.h Entity.h Monster.h Room.h GameException.h Fighter.h Sorcerer.h

Goblin.o: Goblin.cpp Goblin.h Monster.h Entity.h
	g++ -c Goblin.cpp Goblin.h Monster.h Entity.h

Monster.o: Monster.cpp Monster.h Entity.h
	g++ -c Monster.cpp Monster.h Entity.h

Player.o: Player.cpp Player.h Entity.h Monster.h
	g++ -c Player.cpp Player.h Entity.h Monster.h

Room.o: Room.cpp Room.h Monster.h Entity.h GameException.h Dragon.h Goblin.h
	g++ -c Room.cpp Room.h Monster.h Entity.h GameException.h Dragon.h Goblin.h

Sorcerer.o: Sorcerer.cpp Sorcerer.h Player.h Entity.h Monster.h
	g++ -c Sorcerer.cpp Sorcerer.h Player.h Entity.h Monster.h

main.o: main.cpp Game.h Player.h Entity.h Monster.h Room.h GameException.h
	g++ -c main.cpp Game.h Player.h Entity.h Monster.h Room.h GameException.h

Clean:
	rm -f Dungeon Dragon.o Entity.o Fighter.o Game.o Goblin.o Monster.o Player.o Room.o Sorcerer.o main.o