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

int pid_map[PID_MAX+1]; //initialize the pid pool

int last;	// last pid in use


//File 2, pid.c

#include "pid.h"
#include <pthread.h>
#include <stdio.h>

/**
 * Allocates the pid map.
 */
int allocate_map(void) 
{
	int i;
	//set all flags to 0, indicating unused pid
	//starts at 300 and goes to 350
	for (i = PID_MIN; i < PID_MAX+1; i++)
		pid_map[i] = 0;
	
	last = PID_MIN;

	//returns 1 for successful creation
	return 1;
}//end allocate_map

/**
 * Allocates a pid
 */
int allocate_pid(void)
{
	int new_pid, i;
	int map_range = (PID_MAX - PID_MIN);

	//check to see if last has reached the end of the the pid map
	if (last > PID_MAX || last < PID_MIN)
		new_pid = PID_MIN; //set back to begining to look for open pids
	else
		new_pid = last;

	/******LOCK*******/
	pthread_mutex_lock(&lock);
	//check for available pids
	for(i = 0; (i < map_range) && (pid_map[new_pid] != 0); i++){
		//find an open pid
		new_pid++; //check if next pid is available
		//if we started in middle of the map
		//go back to the beggining and start checking
		if(new_pid < PID_MIN || new_pid > PID_MAX)
			new_pid = PID_MIN;
		//exits when new_pid finds an index that is = 0, so that PID can now
		//be used
	}//enb while

	//no available so return -1
	if(!(i < map_range))
		new_pid = -1;
	else {
		//last will not change if a new free
		//pid is found
		last = new_pid;
		pid_map[new_pid] = 1;
	}
	
	if(new_pid != -1)
			printf("Tread acquired pid %d.\n", new_pid);

	pthread_mutex_unlock(&lock);
	/*****UNLOCKED******/

	//return the available pid or -1
	return new_pid;

}//end allocate_pid

/*
 * Releases a pid
 */
void release_pid(int pid)
{
	/******LOCK*******/
	pthread_mutex_lock(&lock);

	printf("Tread releasing pid %d.\n", pid);
	pid_map[pid] = 0;

	pthread_mutex_unlock(&lock);
	/*****UNLOCKED******/
	
	return;
}//end release_pid


//File , test.c 
#include <pthread.h>
#include <unistd.h>
#include <stdio.h>
#include <time.h>
#include "pid.h"

#define NUM_THREADS 100
#define ITERATIONS	10
#define SLEEP		5
 
/**
 * mutex lock used when accessing data structure
 * to ensure there are no duplicate pid's in use.
 */


int random_num_gen(){
	int random_number;
	time_t t;

	//initialize random number generator
	random_number = (int)(rand() % SLEEP + 1);

	return random_number;

}//end rand_num_gen

void *allocator(void *param)
{
	int thread_num = *(int *)param + 1;
	int pid, i;
	
	for(i = 0; i < ITERATIONS; i++){
		
		//obtain a pid
		pid = allocate_pid();

		//sleep
		int rn = random_num_gen();
		
		sleep(rn);
		if(pid != -1){
			release_pid(pid); //release if have pid
		}
	}
	
	pthread_exit(0);

}//end allocator

int main(void)
{
	int i;
	pthread_t tids[NUM_THREADS];

	/* allocate the pid map */
	if (allocate_map() == -1)
		return -1;
	
	//initiate the lock
	if(pthread_mutex_init(&lock, NULL) != 0){
		printf("Failure to initialize lock.");
		return 1;
	}

		//create the threads
		for(i = 0; i < NUM_THREADS; i++)
			if(pthread_create(&tids[i], NULL, allocator, &i)){
				printf("Error creating thread %d", i);
				return 1;
			}
			
		//join threads
		for (i = 0; i < NUM_THREADS; i++)
			if(pthread_join(tids[i], NULL)){
        		printf("Error joining thread.\n");    
				return 1;
			}

	printf("***DONE***\n");

	return 0;
}