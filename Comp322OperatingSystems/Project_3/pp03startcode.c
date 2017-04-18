#include <stdio.h>
#include <unistd.h>
#include <errno.h>
#include <stdlib.h>
#include <sys/types.h>
#include <string.h>


#define MAX_LINE		80 /* 80 chars per line, per command, should be enough. */
#define MAX_COMMANDS	5 /* size of history */

/** 
 * The setup function below will read in the next command line;
 * separate it into distinct arguments (using blanks as delimiters), 
 * and set the args array entries to point to the beginning of what
 * will become null-terminated, C-style strings. 
 */

int setup(char inputBuffer[], char *args[])
{
    int length,		/* # of characters in the command line */
	i,				/* loop index for accessing inputBuffer array */
	start,			/* index where beginning of next command parameter is */
	ct,				/* index of where to place the next parameter into args[] */
    
    ct = 0;
	
    /* read what the user enters on the command line */
	do {


		length = read(STDIN_FILENO,inputBuffer,MAX_LINE); 
	}
	while (inputBuffer[0] == '\n'); /* swallow newline characters */
	
    /**
	 *  0 is the system predefined file descriptor for stdin (standard input),
     *  which is the user's screen in this case. inputBuffer by itself is the
     *  same as &inputBuffer[0], i.e. the starting address of where to store
     *  the command that is read, and length holds the number of characters
     *  read in. inputBuffer is not a null terminated C-string. 
	 */
	
    start = -1;
    if (length == 0)
        exit(0);            /* ^d was entered, end of user command stream */
	
	/** 
	 * the <control><d> signal interrupted the read system call 
	 * if the process is in the read() system call, read returns -1
  	 * However, if this occurs, errno is set to EINTR. We can check this  value
  	 * and disregard the -1 value 
	 */
    if ( (length < 0) && (errno != EINTR) ) {
		perror("error reading the command");
		exit(-1);           /* terminate with error code of -1 */
    }
	
	/**
	 * Parse the contents of inputBuffer
	 */
	
    for (i=0;i<length;i++) { 
		/* examine every character in the inputBuffer, find special characters in the inputBuffer */

	}    /* end of for */
	
	args[ct] = NULL; /* just in case the input line was > 80 */
	
	return 1;
	
} /* end of setup routine */


int main(void)
{
	char inputBuffer[MAX_LINE]; 	/* buffer to hold the command entered */
	char *args[MAX_LINE/2 + 1];	/* command line (of 80) has max of 40 arguments */
	pid_t child;            		/* process id of the child process */
	int status;           		/* result from execvp system call*/
	int shouldrun = 1;
	
	int i, upper;
		
    while (shouldrun){            		/* Program terminates normally inside setup */

		shouldrun = setup(inputBuffer,args);       /* get next command */
			
		/* Deal with user's command from the new shell*/

        /**
         * Deal with user's command from the new shell:
         * (1) fork a child process
         * (2) read user input (partially done in setup())
         * (3) the child process will invoke execvp()
         */

         /* Do not forget to print out your information*/
    }
	
	return 0;
}
