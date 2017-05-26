/* 
Promise of Originality
I promise that this source code file has, in it's entirety, been
written by myself and by no other person or persons. If at any time an
exact copy of this source code is found to be used by another person in
this exam, I understand that both myself and the student that submitted
the copy will receive a zero on this assignment.
*/

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <time.h>
#include <errno.h>
#include <sys/types.h>
#include <string.h>
#include <time.h>
#include <pwd.h>

#define SLEEP 10

int random_num_gen(){
  int random_number;
  time_t t;

  //initialize random number generator
  random_number = (int)(rand() % SLEEP + 1);

  return random_number;

}//end rand_num_gen


int main(int argc, char *argv[]) {
  	pid_t pid;
  	int status = 0;

  //variables to print id and time
    register struct passwd *pw;
    register uid_t uid;
    uid = geteuid ();
    pw = getpwuid (uid);
    time_t mytime;
    mytime = time(NULL);

    //Implement Step 1-5 starting from here
 
    //step 1 
    if (argc > 2) {
        fprintf(stderr, "Only excepts 1 arg.\n");
        exit(1);
    }
    if (argc < 2) {
        fprintf(stderr, "Incorrect use.\nMust use the argument ls.\n");
        exit(1);
    }

  	pid = fork();

    if (pid == 0)
    {//child process
      printf("CHILD Started\n");
      printf("Student Username: %s\n Finish time: %s\n", pw->pw_name, ctime(&mytime));
            //execute command
      int rn = random_num_gen();
    sleep(rn);
    printf("CHILD done sleeping.\n");
      
    }
    else if(pid > 0)
    {//parent process
        printf("PARENT started, now waiting for process ID#%d\n",pid);
        wait(NULL);

        printf("PARENT resumed. Child exits. Now PARENT executing...\n");
        if(execlp(argv[1], argv[1], NULL) < 0){
            //error when trying to execute command
            printf("Error: Command not recoginzed.\n");
            exit(0);
        }
    }
    else{
        printf("Fork failure\n");
        exit(1);
    }



  return 0;
}