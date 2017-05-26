//To compile, gcc -l pthread stats.c -o stats

#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>

/* the list of integers */
int *int_list;

/* the threads will set these values */
double average;
int maximum;
int minimum;

void *calculate_average(void *param)
{
    int i;
    int count = *(int *)param;
    //Implement the logic to calculate the average of values in list[].
    //make sure the list of numbers is not equal zero
    if (count != 0){
        //sum integers
        for (i = 0; i < count; i++)
            average = average + int_list[i];
        //divide by total number of ints
        average = average / count;
    }//end if
    pthread_exit(0);
}//end calculate_average

void *calculate_maximum(void *param)
{
    int i;
    int count = *(int *)param;
    //Implement the logic to find the biggest value in list[].
        
    //if the int_list is not empty
    //grab the first int to check for max
    if (count > 0)
        maximum = int_list[0];
    for (i = 1; i < count; i++)
    //change to max if the integer is bigger than the current
     //max
        if (int_list[i] > maximum)
            maximum = int_list[i];
    
    
    


    pthread_exit(0);
}//end caclulate_maximum

void *calculate_minimum(void *param)
{
    
    int i;
    int count = *(int *)param;
    //Implement the logic to find the smallest value in list[].
    
    //if the int_list is not empty grab the first int to check
    //for min
    if (count > 0)
        minimum = int_list[0];
        
    for (i = 1; i < count; i++)
    //change to min if the integer is smaller than the current min
        if (int_list[i] < minimum)
            minimum = int_list[i];  

    pthread_exit(0);
}//end calculate_minimum


int main(int argc, char *argv[])
{
    int i, argv_size;
    pthread_t avg_thread, max_thread, min_thread;
    
    argv_size = argc - 1;

    /* allocate memory to hold array of integers */
    int_list = (int *) malloc(argv_size * sizeof(int));
    
    /*convert the argv into integers */
    
    for (i = 0; i < argv_size; i++) 
    //loop through and convert the strings to integers
        sscanf(argv[i+1], "%d", &int_list[i]);
    
    

    //printf("%d", argv_size);
    /* create threads to calculate average, max and min by using pthread_create()*/
    if (pthread_create(&avg_thread, NULL, calculate_average, &argv_size))
    {//Print error and exit if thread creating fails
        printf("Error creating average thread.\n");
        exit(0);
    }//end if

    if (pthread_create(&max_thread, NULL, calculate_maximum, &argv_size))
    {//Print error and exit if thread creating fails
        printf("Error creating maximum thread.\n");
        exit(0);
    }//end if

    if (pthread_create(&min_thread, NULL, calculate_minimum, &argv_size))
    {//Print error and exit if thread creating fails
        printf("Error creating minimum thread.\n");
        exit(0);
    }//end if


    if(pthread_join(avg_thread, NULL))
        printf("Error joining Average thead.\n");
    if(pthread_join(max_thread, NULL))
        printf("error joining Maximum thread.\n");
    if(pthread_join(min_thread, NULL))
        printf("error joining Minimum thread.\n");


    //Output results
    printf("The Average is %.2f\n", average);
    printf("The Maximum is %d\n", maximum);
    printf("The Minimum is %d\n", minimum);

    free(int_list);

    return 0;
}



