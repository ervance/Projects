
/*
Eric Robert Vance
Comp 222 at 5:30pm
Assignment 4 - Virtual address mapping
November 29, 2015
*/ 


#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>

// global variables

int main_mem_size;
int page_size;
int rep_policy;
int total_pysical_frames;
int available_frames;
int frame;
int pysical_frame;


//structure holding the virtual memory
struct vm_node{
	int virtual_page;
	int frame_number;
	int initalized;
} *virtualMem = NULL;

typedef struct vm_node vm;

/***************************************************************/
//helper functions

int physicalAddress(int virtual_address, int pys_frame, int vir_page)
{//calculate the physical address
	int PA;

	PA = ( pys_frame * page_size) + (virtual_address - (vir_page * page_size));

	return PA;
}//pysicalAddress end


int ispoweroftwo(int num)
{
	//local variables
	int power_of_two;
	
	//checks if num is a power of 2 by and'ing the bits
	if ( (num > 0) && ((num & (num - 1)) == 0) ){
		//is a power of 2
		power_of_two = 1;
	}//if
	else{
		//is not a power of 2
		power_of_two = 0;
	}//else

	return power_of_two;

}//ispoweroftwo end

void createEmptyVm()
{//create an empty virtual memory to manipulate 
	int i;
	vm page_node;

	page_node.virtual_page = 0;
	page_node.frame_number = 0;
	page_node.initalized = 0;

	for(i = 0; i < total_pysical_frames; i++){

		virtualMem[i] = page_node;

	}

}//end createEmptyVm

void findLast()
{//find last entry to begin shifting
	int i = 0;

	while(i < total_pysical_frames){

		if(virtualMem[i].initalized == 1){
			frame = i;
		}
		i++;
	}
	return;

}//end findLast

int frameToReplace() 
{//find the frame to replace
	int replace;

	findLast();
	replace = virtualMem[frame].frame_number;
		
	return replace;

}//end frameToReplace

void slideFrames()
{//shift frames over
	int i;
	
	for(i = frame; i > 0; i--){
		virtualMem[i] = virtualMem[i-1];
	}//end for
	return;

}//end slideFrames

int faultCheck(int va_page)
{//returns 1 if there is a fault;
	int fault = 1;
	frame = 0;
	//initalized acts as if it were null or not
	while (frame < total_pysical_frames && fault !=0 && virtualMem[frame].initalized != 0){

		if(virtualMem[frame].virtual_page == va_page){
			//there is no fault
			//found a page that is in there
			fault = 0;
			
		} else {
			//keep looking
			frame++;
		}

	}

	return fault;

}//end faultCheck

void update(int fault, int va_page)
{//updaete the virtual memory
	vm page_frame;
	
	if (fault > 0){
		//fault occured

		//initalize the frame
		page_frame.initalized = 1;
		page_frame.virtual_page = va_page;
		
		if (available_frames < total_pysical_frames){
			//virtual memory has empty pysical frames
			page_frame.frame_number = available_frames;
			available_frames++;
			slideFrames();
			virtualMem[0] = page_frame;
			pysical_frame = virtualMem[0].frame_number;
			
		}
		else {
			//does not have empty physcial frames
			page_frame.frame_number = frameToReplace();
			slideFrames();
			virtualMem[0] = page_frame;
			pysical_frame = virtualMem[0].frame_number;
				
		}
	}
	else {
		//there was no fault		
		//adjust according to replacement policy
		if(rep_policy == 0){
			//LRU
			page_frame = virtualMem[frame];
			slideFrames();
			virtualMem[0] = page_frame;
			pysical_frame = virtualMem[0].frame_number;
		}
		else{
			//FIFO
			pysical_frame = virtualMem[frame].frame_number;
		}	
		
	}

	return;

}//end update


/***************************************************************/

void parameters()
{
	printf("\n\nEnter main memory size (words): ");
	scanf("%d", &main_mem_size);
	printf("\n\nEnter page size (words/page): ");
	scanf("%d", &page_size);
	printf("\n\nEnter replacement policy (0=LRU, 1=FIFO): ");
	scanf("%d", &rep_policy);
	//if entries are not valid just return to main memory
	if (ispoweroftwo(main_mem_size) == 1 && ispoweroftwo(page_size) == 1) {
		//check to make sure the entries are valid.
		total_pysical_frames = main_mem_size / page_size;
		
		//available pysical frames
		available_frames = 0;
		//free memory if not null
		if(virtualMem != NULL)
			free(virtualMem);
		//allocate memory for virtual memory
		virtualMem = (vm *) malloc (sizeof(vm) * total_pysical_frames);
		//create empty virtual memory
		createEmptyVm();
	}


	return;

}//parameters end

/***************************************************************/

void mapVirtualAddress()
{
	//local variables
	int va, i;
	int pysical_address;
	int va_page;
	int fault;//fault > 0, a fault occured
	
	
	printf("\nEnter virtual memory address to access: ");
	scanf("%d", &va);

	//determine fault
	va_page = va / page_size;

	
	//determine if fault
	fault = faultCheck(va_page);
	
	//update virtual memory
	update(fault, va_page);
	
	//calculate pysical address
	pysical_address = physicalAddress(va, pysical_frame, va_page); 
	
	if ( fault > 0){
		//if fault occurs
		printf("\n\n\n\n*** Page fault!\n");
		for(i = total_pysical_frames-1; i >= 0; i--)
			if(virtualMem[i].initalized != 0)
				printf("\n*** VP %d --> PF %d \n", virtualMem[i].virtual_page, virtualMem[i].frame_number);
	}//if end
	else {
		//if fault does not occur
		printf("\n\n\n\n*** Virtual address %d maps to physical address %d\n", va, pysical_address);
		for(i = total_pysical_frames-1; i >= 0; i--)
			if(virtualMem[i].initalized != 0)
				printf("\n*** VP %d --> PF %d \n", virtualMem[i].virtual_page, virtualMem[i].frame_number);
	}//else end



  return;

}//mapVirtualAddress end


/***************************************************************/

int main()
{
	//local variables
	int run = 1;
	int choice;
 	//run until user chooses "3"
	while(run != 0) {
		//until program exits, print menu, select choice via 
		printf("\n\n\n\nEric Vance\n");
		printf("\nVirtual address mapping:\n");
		printf("\n--------------------------\n");
		printf("\n1) Enter parameters\n");
		printf("\n2) Map Virtual Address\n");
		printf("\n3) Quit\n");
		printf("\nEnter selection: ");
		scanf("%d", &choice);
		printf("\n");
  
		switch(choice){
			case 1:
				//enter parameters
				parameters();
				break;
			case 2:
				//map virtual memory
				mapVirtualAddress();
				break;
			case 3:
				//exit
				run = 0;
				break;
				

		}//end switch

	}//end menu while
	
	//free the virtual memory before exiting
	free(virtualMem);

  	return;

}//end main
