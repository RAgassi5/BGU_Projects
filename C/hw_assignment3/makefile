JerryBoree: HashTable.o Jerry.o JerryBoreeMain.o KeyValuePair.o LinkedList.o MultiValueHashTable.o
	gcc HashTable.o Jerry.o JerryBoreeMain.o KeyValuePair.o LinkedList.o MultiValueHashTable.o -o JerryBoree

HashTable.o: HashTable.c HashTable.h Defs.h LinkedList.h KeyValuePair.h
	gcc -c HashTable.c

Jerry.o: Jerry.c Jerry.h Defs.h
	gcc -c Jerry.c

JerryBoreeMain.o: JerryBoreeMain.c Defs.h Jerry.h KeyValuePair.h \
  HashTable.h MultiValueHashTable.h LinkedList.h
	gcc -c JerryBoreeMain.c

KeyValuePair.o: KeyValuePair.c KeyValuePair.h Defs.h
	gcc -c KeyValuePair.c

LinkedList.o: LinkedList.c LinkedList.h Defs.h
	gcc -c LinkedList.c

MultiValueHashTable.o: MultiValueHashTable.c MultiValueHashTable.h Defs.h \
  HashTable.h LinkedList.h KeyValuePair.h
	gcc -c MultiValueHashTable.c

Clean:
	rm -f HashTable.o Jerry.o JerryBoreeMain.o KeyValuePair.o LinkedList.o MultiValueHashTable.o JerryBoree