all: testpid

testpid: test.o pid.o
	gcc -pthread -o testpid test.o pid.o

pid.o: pid.c pid.h
	gcc -c pid.c

test.o: test.c pid.h
	gcc -pthread -c test.c
