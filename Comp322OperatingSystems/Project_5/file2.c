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
	int new_pid, range_check;
	int map_range = ((PID_MAX+1) - PID_MIN);

	//check to see if last has reached the end of the the pid map
	if (last == PID_MAX)
		last = PID_MIN; //set back to begining to look for open pids
	else
		new_pid = last;

	range_check = 0;
	//check for available pids
	while((range_check < map_range) && (pid_map[new_pid] == 1)){
		//find an open pid
		range_check++; //increment range checker
		new_pid++; //check if next pid is available

		//if we started in middle of the map
		//go back to the beggining and start checking
		if(new_pid > PID_MAX)
			new_pid = PID_MIN;
		//exits when new_pid finds an index that is = 0, so that PID can now
		//be used
	}//enb while
	
	if(range_check == map_range)
		//no available so return -1
		new_pid = -1;
	else{
		//last will not change if a new free
		//pid is found
		last = new_pid;
		pid_map[new_pid] = 1;
	}
	//return the available pid or -1
	return new_pid;
}//end allocate_pid
/*
 * Releases a pid
 */
void release_pid(int pid)
{
	pid_map[pid] = 0;

}//end release_pid