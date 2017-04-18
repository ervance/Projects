
/*
Eric Robert Vance
Comp 222 at 5:30pm
Assignment 3 - Error Detection and Correction
November 11, 2015
*/ 


#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>


// global variables 
int max_hamming_size = 0;
int parity;
char *hamming_code;



/***************************************************************/

void printHamCode(char * a, int size){
	//helper to print out the hamming code
	int i;
	for( i = 0; i < size; i++){
		printf("%c", a[i]);
	}
	printf("\n");

	return;
}//end printHamCode

/***************************************************************/

void parameters()
{
	//prompt for maximum hamming code length and for even/odd parity
	printf("\nEnter the maximum length: ");
	scanf("%d", &max_hamming_size);
	printf("\nEnter the parity (0 = even, 1 = odd): ");
	scanf("%d", &parity);

	//allocate memory for hamming string based on 
	//maximum length and size of a character element
	if (hamming_code != NULL){
		//free the old memorry space for new string	
		free(hamming_code);
	}
	
	//Add extra memory to the size in order to avoid fault
	hamming_code = (char*) malloc(sizeof(char) * max_hamming_size);

  return;
}//end parameters

/***************************************************************/
void checkHamming()
{
	//declare local vars
	int i, j, k;//loop variables
	int ham_size;//size of string
	int check_loc;//which check bit to start at
	int num_of_check_bits;//how many checkbits are in the code
	int correct_bit;//fixed bit
	int check_sum = 0;//initialize the sum
	int error_result = 0;//initialize the error
	

	//prompt for hamming code
	printf("\nEnter the Hamming code: ");
	scanf("%s", hamming_code);
	ham_size = strlen(hamming_code);
	
	//check to make sure code entered is not larger than max size.
	if (ham_size <= max_hamming_size){
	 	num_of_check_bits = (int)ceil(log2(ham_size));
	 	
	 	//outer loop: for each parity bit/check bit in the Hamming code
	 	for( i = 0; i < num_of_check_bits; i++) {
	 		//update check/parity bit location
	 		check_loc = (int)pow(2,i);	

	 		//update/reset the sum with the parity
	 		//each round a new sum needs to be obtained
	 		//from the current parity/check bit	 		
	 		check_sum = parity;

	    	//middle loop: for each starting bit of a sequence
	 		for( j = check_loc; j <= ham_size; j += check_loc){
				
	         	//inner loop: for each bit to be checked, check value 
	         	for( k = 0; k < check_loc && j <= ham_size; k++){
	         		
	         		check_sum = check_sum ^ ((int)hamming_code[ham_size-j] - '0');
	         		//j increases to help with skipping the appropriate 
	         		//bits for the current sequence
	         		j++;

	         	}//end inner loop

			}//end middle loop
	 		
	 		//update the error result with the sum of the check bits/parity bits
	 		error_result += check_sum * (int)pow(2,i);
	 		
	 	}//end outer loop

	 	
	 	//report error in hamming code based on result 
	 	//from parity bits or report no error if none
	 	if(error_result != 0){
	 		
	 		printf("\n\n*** There is an error in bit: %d", error_result);
	 		//There is an error, fix the error bit by flipping it
	 		correct_bit = (1 ^ ((int)hamming_code[ham_size - error_result] - '0'));
	 		//replace incorrect bit with the corrected bit
	 		hamming_code[ham_size - error_result] = '0' + correct_bit;
	 		printf("\n*** The corrected Hamming code is: ");
	 		//print out the corrected Hamming code
	 		printHamCode(hamming_code, ham_size);
	 	}//end if error check
	 	else {
	 		//no error found
	 		printf("\n\n*** There is no bit error");

	 	}//end else error check

	 }//end if size check
	 else{
	 	//user tried to enter a code larger than the maximum size
	 	printf("\n\n*** Invalid Entry - Exceeds Maximum 
	 		Code Length of %d\n", max_hamming_size);

	 }//end else size check

  return;

}//end checkHamming



/***************************************************************/
int main()
{
	//local variables
	int run = 1;
	int choice;
 	//run until user chooses "3"
	while(run != 0) {
		//until program exits, print menu, select choice via 
		//switch statement and call appropriate function
		printf("\n\nEric Vance\n");
		printf("\nError detection/correction:\n");
		printf("----------------------------\n");
		printf("1) Enter parameters\n");
		printf("2) Check Hamming code\n");
		printf("3) Exit\n");
		printf("\nEnter selection: ");
		scanf("%d", &choice);
		printf("\n");
  
		switch(choice){
			case 1:
				//enter parameters
				parameters();
				break;
			case 2:
				//check hamming code
				checkHamming();
				break;
			case 3:
				//exit
				run = 0;
				break;
				

		}//end switch
	}//end menu while
	
	//free the memory before exiting
	free(hamming_code);

  return;

}//end main
