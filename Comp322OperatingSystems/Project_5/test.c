
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
	random_number = rand() % SLEEP + 1;
	return random_number;

}//end rand_num_gen

void *allocator(void *param)
{
	int thread_num = *(int *)param + 1;
	int pid;
	
	/******LOCK*******/
	
	//obtain a pid
	pid = allocate_pid();
	if(pid != -1)
		printf("Tread %d pid is %d.\n", thread_num, pid);
	
	
	
	/*****UNLOCKED******/

	//sleep
	int rn = random_num_gen();
	printf("%d, sleep for %d \n", pid, rn);
	sleep(rn);
	if(pid != -1)
		release_pid(pid); //release if have pid

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

	int count = 0;
	//while should be here
	while (count < ITERATIONS) {
		//create the threads
		for(i = 0; i < NUM_THREADS; i++)
			if(pthread_create(&tids[i], NULL, allocator, &i)){
				printf("Error creating thread %d", i);
				return 1;
			}
			

		for (i = 0; i < NUM_THREADS; i++)
			if(pthread_join(tids[i], NULL)){
        		printf("Error joining thread.\n");    
				return 1;
			}

		count++;
	}

	printf("***DONE***\n");

	return 0;
}