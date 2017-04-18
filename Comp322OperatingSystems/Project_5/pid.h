/*
	Start code for Programming Project 4.
	Create 4 files with proper file extension.
*/


// File 1, pid.h
// This header file is included in pid.c and test.c

#include <pthread.h>

#define PID_MIN  	300 
#define PID_MAX 	350

/* mutex lock for accessing pid_map */
pthread_mutex_t lock;

int pid_map[PID_MAX+1];

int last;	// last pid in use