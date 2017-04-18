#include <stdio.h>
#include <stdlib.h>
#include <math.h>

/* NOTE: throughout the code, change the name of each procedure or function to something else in order to compile */
#define TEN_POW 1000000

/* declare global var's */
int num_classes;
int freq;
int *cpi;
float *inst_count;
float total_inst;



/************************************************/
/* define functions to calculate average cpi, cpu time, and mips */

float average_cpi()
{
/* declare local var's, calculate and return average cpi */
	int i;
	float avg_cpi;
	float sum_cpi_inst;
	
	sum_cpi_inst = 0;

/*The average CPI = summation [(CPI(cpi) * I (inst_count)] / Ic (total of inst_count)*/
	for(i = 0; i <num_classes; i++){
		sum_cpi_inst = sum_cpi_inst + (cpi[i] * inst_count[i]);
	}
	//This is converting it to millions
	sum_cpi_inst = sum_cpi_inst * TEN_POW;

	avg_cpi = sum_cpi_inst / total_inst;
	
	return avg_cpi;

}


float cpu_time()
{
/* declare local var's, calculate and return cpu time */
	float cycle_time;
	float cpu_time;
	float avg_cpi;

	avg_cpi = average_cpi();
	cycle_time = 1.0/freq;

	cpu_time = total_inst * avg_cpi * cycle_time;
	
	/* this is formating cpu_time to seconds*/
	cpu_time = cpu_time/TEN_POW;
	/*the 100 is to put the cpu_time into ms */
	cpu_time = cpu_time * 1000;

	
	return cpu_time;


}


float mips()
{
/* declare local var's, calculate and return mips */
	float mips_rate;
	float cpi;

	cpi = average_cpi();
	
	
	
	mips_rate = (freq * TEN_POW) / (cpi * TEN_POW);
	

	return mips_rate;



}


/********************************************************************/
void option1()
{
/* declare local var's */
	int i;

	free(cpi);
	free(inst_count);
	total_inst = 0;
/* Prompt for number of instruction classes and frequency of machine */  
	printf("Enter the number of instruction classes: ");
	scanf("%d", &num_classes);
	printf("Enter the frequency of the machine (MHz): ");
	scanf("%d", &freq);
/* allocate memory for dynamic arrays for cpi and instruction count */
	cpi = (int *) malloc(num_classes*sizeof(int));
	inst_count = (float *) malloc(num_classes*sizeof(float));
/* for each instruction class, prompt for CPI and instruction count */
	/* update total number of instructions and cycles */
	for(i = 1; i <= num_classes; i++) {
		printf("Enter CPI of class %d: ", i);
		scanf("%d", &cpi[i-1]);
		printf("Enter instruction count of class %d (millions): ", i);
		scanf("%f", &inst_count[i-1]);
	}//for
	/* calculate the total instructions and set it to the global variable */
	for(i = 0; i < num_classes; i++){
		total_inst = total_inst + inst_count[i];
		
	}
	total_inst = total_inst * TEN_POW;
	
}

/********************************************************************/
void option2()
{
/* declare local var's */
	int i;
/* print table of parameters inputed from OPTION #1, with each row containing the class number, the CPI of the class, and the instruction count of the class, following the sample output on the handout */
	
	printf("\n-------------------------\n");
	printf("|Class\t|CPI\t|Count\t|\n");
	printf("-------------------------\n");
	for(i = 0; i < num_classes; i++){
		printf("|%d\t|%d\t|%.0f\t|\n-------------------------\n",i + 1, cpi[i], inst_count[i]);
	}//for

}//option


/********************************************************************/
void option3()
{
/* declare local var's */
	int i;
/* print table of calculations, including the average CPI, the CPU time, and the MIPS */
	printf("\n-------------------------\n");
	printf("|Performance\t|Value\t|\n");
	printf("-------------------------\n");
	printf("|Average CPI\t|%.2f\t|\n", average_cpi());
	printf("-------------------------\n");
	printf("|CPU Time (ms)\t|%.2f\t|\n", cpu_time());
	printf("-------------------------\n");
	printf("|MIPS\t\t|%.2f\t|\n", mips());
	printf("-------------------------\n");
}


/********************************************************************/
int main()
{
/* declare local var's */
	//The while run = 1, it is representing true;
	int run = 1;
	int choice;
/* until user chooses "4", loop */
	while(run != 0) {
	/* print out menu list starting with your name*/
		printf("\nEric Vance\n")
		printf("\nPerformance assessment:\n");
		printf("-----------------------\n");
		printf("1) Enter parameters\n");
		printf("2) Print table of parameters\n");
		printf("3) Print table of performance\n");
		printf("4) Quit\n");
		printf("\nEnter selection: ");
		scanf("%d", &choice);
		printf("\n");
      /* prompt for selection & choose appropriate procedure using either a case statement of if-else if-else statements */
		switch(choice){
			case 1:
				option1();
				break;
			case 2:
				option2();
				break;
			case 3:
				option3();
				break;
			case 4:
				run = 0;
				break;

		}//switch
	}//while
	/* free the memory before exiting */
	free(cpi);
	free(inst_count);
}
