#include <stdio.h>
#include <stdlib.h>

//final draft


//define structure of dynamic cache and initialize var
//node structure to act as a line in cache
struct node{
	int tag;
	int *lineBlock;
} *cache = NULL;

//global variables
//memory size, default 0 to prevent 
//read/write without perameters being set
int mem_size = 0;
int *memory;//array whos size is determined by the user
int cache_size;//cache size defined by user
int block_size;//block size defined by user
int num_of_lines;//number of lines in cache
int word_line;//what line the word is on in the block
int num_of_blocks;//number of blocks in the memory allocation



typedef struct node n;


/**********************************************************************/
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

}

void emptycache()
{
	//empty and prepare the new cache by setting it to default values

	//local variables
	int i;
	n temp_node;

	//default values
	temp_node.tag = -1;
	temp_node.lineBlock = NULL;

	for(i = 0; i < num_of_lines; i++){
		//load default line into cache
		cache[i] = temp_node;
	}//for

	return;

}

void populatememory(int size)
{
	//local variables
	int i;

	//loads memory with reverse values
	//size of memory at memory location 0
	//0 at last location in memory
	for(i = 0; i < size; i++){
		memory[i] = size - i;
	}//for

	return;

}

int isincache(int t_number, int l_of_cache)
{
	//check if block is in cache
	//return true value if it is, false if it is not

	//local variables
	int in_cache;
	
		if(t_number == cache[l_of_cache].tag){	
			//is in cache
			in_cache = 1;				
		}//if
		else{
			//is not in cache
			in_cache = 0;
		}//else

	return in_cache;

}//isincache

void loadintocache(int t_number, int m_location, int l_of_cache, int w_line)
{
	//load the contents into the cache
	
	//local variables
	int i;
	//m_location is the location being accessed
	int bot_range = m_location;
	//word number the word is located in the block
	int bot_word_line = w_line;
	n cache_line;

	
	free(cache[l_of_cache].lineBlock);
	cache_line.lineBlock = (int *) malloc(block_size * sizeof(int));
	cache_line.tag = t_number;


	//find the starting main memory address of the block
	while(bot_word_line != 0){
		bot_range--;
		bot_word_line--;
	}//while
	//load the blocks from the start of 
	//the block in memory to end of block in memory

	for (i = 0; i < block_size; i++){
		cache_line.lineBlock[i] = memory[bot_range];
		bot_range++;
	}//for

	cache[l_of_cache] = cache_line;

	return;

}//loadintocache

void freememory(){
	
	//local variables
	int i;

	for(i = 0; i < num_of_lines; i++){
		//free cache lineBlock memory
		free(cache[i].lineBlock);
	}
	//free cache and main memory
	free(cache);
	free(memory);

}//freememory


/**************************BEGIN OPTIONS*********************/

void option1()//set parameters
{
	
	// Prompt for main memory size, cache size, block size 
	printf("Enter main memory size (words): ");
	scanf("%d", &mem_size);
	printf("Enter cache size (words): ");
	scanf("%d", &cache_size);
	printf("Enter block size (words/block): ");
	scanf("%d", &block_size);

	//check for errors in parameters
	if(!ispoweroftwo(cache_size)){
		//not a power of 2
		printf("\n** Error - cache size not a power of 2!\n");
		//reinitialze memory size to 0 
		//parameters were not accepted
		mem_size = 0;
	}//if
	else if (block_size > cache_size){
		//block size larger than cache
		printf("\n** Error - block size larger than cache size!\n");
		//reinitialze memory size to 0 
		//parameters were not accepted
		mem_size = 0;
	}//else if
	else if(!ispoweroftwo(mem_size)) {
		//memory size not power of 2
		printf("\n** Error - memory size not a power of 2!\n");
		//reinitialze memory size to 0 
		//parameters were not accepted
		mem_size = 0;
	}
	else if(!ispoweroftwo(block_size)){
		//block size not a power of 2
		printf("\n** Error - block size not a power of 2!\n");
		//reinitialze memory size to 0 
		//parameters were not accepted
		mem_size = 0;
	}
	else if(block_size > mem_size){
		//block size larger than main memory
		printf("\n** Error - block size larger than main memory size!\n");
		//reinitialze memory size to 0 
		//parameters were not accepted
		mem_size = 0;
	}
	else {
		//input is valid, allocate and load virutal memory
		printf("\n** Data Accepted\n");
		
		//memory array, free old, allocate new
		free(memory);
		memory = (int *) malloc(mem_size * sizeof(int));
		
		//load data into memory
		populatememory(mem_size);
		
		//calculate number of lines in the cache
		num_of_lines = cache_size / block_size;
		
		//cache array, free old, allocate new
		free(cache);
		cache = (n *) malloc(num_of_lines * sizeof(n));

		//calculate number of blocks in memory
		num_of_blocks = mem_size / block_size;
		
		//set cache to default
		emptycache();

	}//else
	return;
}//option 1


/**********************************************************************/

void option2()//read from cache/memory
{
	//local variables
	
	int read_location;
	int tag_number;
	int cache_line_number;
	int block_number;
	

	//Prompt for read signal
	printf("Enter main memory address to read from: ");
	scanf("%d", &read_location);

	//block number of the word being accessed
	block_number = read_location / block_size;

	//tag number of the block being accessed
	tag_number = block_number / num_of_lines;
	
	//word number in the block
	word_line = (read_location % block_size);

	//line in cache to load the block
	cache_line_number = (block_number % num_of_lines);
	
	//check cache
	if (mem_size <= read_location){
		printf("\n\n** Error - memory location outside memory size\n\n");
	}
	else if (isincache(tag_number, cache_line_number)){
		
		//Hit, block in cache.  Display contents
		printf("\n\n** Block in Cache \n** Word %d of cache line %d with 
			tag %d contains value %d\n\n", word_line, cache_line_number, 
			tag_number, cache[cache_line_number].lineBlock[word_line]);
	}
	else {
		//Miss, not in cache
		printf("\n\n** Read Miss...First Load Block from Memory!\n");
		
		//Load location into cache
		loadintocache(tag_number, read_location, 
			cache_line_number, word_line);

		//Display contents
		printf("** Word %d of cache line %d 
			with tag %d contains value %d\n", 
			word_line, cache_line_number, tag_number, 
			cache[cache_line_number].lineBlock[word_line]);
	}

	return;
}

void option3()//write to cache
{
	///local variables
	int write_content;//word to write to cache
	int write_location;//location to write to memory
	int tag_number;//tag number of block writing to
	int cache_line_number;//line of cache access
	int block_number;//block of address location


	//Prompt for write
	printf("Enter main memory address to write to: ");
	scanf("%d", &write_location);
	printf("Enter value to write: ");
	scanf("%d", &write_content);
	
	//word number in the block = memory location modulo block size
	word_line = write_location % block_size;

	//block number of memory location = memory location 
	//divided by block size
	block_number = write_location / block_size;

	//tag number of the block = block number divided by number 
	//of lines in cache
	tag_number = block_number / num_of_lines;
	
	//line in cache to load block = blocks number modulo number 
	//of lines in cache
	cache_line_number = block_number % num_of_lines;
	
	
	
	if (mem_size <= write_location){
		printf("\n\n** Error - memory location outside memory size\n\n");
	}
	//check if data in the cache
	else if (isincache(tag_number, cache_line_number)){
		
		//hit, write through to memory
		memory[write_location] = write_content;
		
		//block is in the cache, update value in cache
		cache[cache_line_number].lineBlock[word_line] = write_content;
		
		//display contents
		printf("\n\n** Block in Cache \n** Word %d of cache line %d 
			with tag %d contains value %d\n\n", word_line, 
			cache_line_number, tag_number, 
			cache[cache_line_number].lineBlock[word_line]);
	}
	else {
		//miss, not in cache
		printf("\n\n** Write Miss...First Load Block from Memory!\n");

		//write through to memory
		memory[write_location] = write_content;
		
		//load block into cache
		loadintocache(tag_number, write_location, 
			cache_line_number, word_line);
		
		//load the data into cache block
		cache[cache_line_number].lineBlock[word_line] = write_content;

		//display contents
		printf("** Word %d of cache line %d with tag %d 
			contains value %d\n\n", word_line, 
			cache_line_number, tag_number, 
			cache[cache_line_number].lineBlock[word_line]);
	}


	return;
}



/**********************************************************************/
int main()
{
	//local variables
	int run = 1;
	int choice;
	//until user chooses "4", loop
	while(run != 0) {
	//print out menu list
		printf("\nEric Vance\n");
		printf("\nMain memory to Cache memory mapping:\n");
		printf("--------------------------------------\n");
		printf("1) Set parameters\n");
		printf("2) Read cache\n");
		printf("3) Write to cache\n");
		printf("4) Exit\n");
		printf("\nEnter selection: ");
		scanf("%d", &choice);
		printf("\n");
  
		switch(choice){
			case 1:
				option1();
				break;
			case 2:
				if (mem_size != 0) {
					option2();
				} else {
					printf("** Error - No memory initialized. 
						Parameters must be set first.\n");
				}
				break;
			case 3:
				if ( mem_size != 0 ) {
					option3();
				} else {
					printf("** Error - No memory initialized. 
						Parameters must be set first.\n");
				}
				break;
			case 4:
				run = 0;
				break;

		}//switch
	}//while
	
	//free the memory before exiting
	
	freememory();
	

  return;
}