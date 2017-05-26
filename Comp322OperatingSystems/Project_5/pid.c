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

	pthread_mutex_lock(&lock);
	//check to see if last has reached the end of the the pid map
	if (last > PID_MAX || last < PID_MIN)
		new_pid = PID_MIN; //set back to begining to look for open pids
	else
		new_pid = last;

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

	pthread_mutex_unlock(&lock);

	//return the available pid or -1
	return new_pid;

}//end allocate_pid

/*
 * Releases a pid
 */
void release_pid(int pid)
{
	pthread_mutex_lock(&lock);
	pid_map[pid] = 0;
	pthread_mutex_unlock(&lock);
	return;
}//end release_pid