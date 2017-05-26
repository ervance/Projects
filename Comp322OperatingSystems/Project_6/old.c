#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <sys/types.h>






void displayVirtAddress(int address, int page_size, int total_address_size, FILE * wfp)
{
	//calculate and display address
	char str[255];
	int page_number = (address/page_size); 
	int offset = (address % page_size);
	snprintf(str, sizeof(str), "\n\nThe address %d contains:\npage number = %d\noffset = %d",address , page_number, offset);
	fputs(str, wfp);

}

int main(int argc, char *argv[])
{
	FILE * fp; //read from
	FILE * wfp;//write to
	int page_size = (int) pow(2.0, 12.0);
	int total_address_size = (int) pow(2.0, 32.0);
	
	int address;
	
	char line[255];
	//char * file = argv[1];
	char * file = "BACKING_STORE_256.dat"
	/*open the file*/
	fp = fopen(file, "r");
	//wfp = fopen("output.txt", "w+");

	if (fp == NULL || wfp == NULL){
		printf("Error opening file\n");
		return -1;
	}

	while(fgets(line,255, fp) != NULL){
		
		unsigned char value;
    	fseek(back_store_256, PAGE_SIZE * page_number, SEEK_SET);
    	fread(&value, sizeof(char), PAGE_SIZE, back_store_256);
    	printf("%s\n",value );
		//displayVirtAddress(address, page_size, total_address_size, wfp);

	}
	
	fclose(fp);
	fclose(wfp);

	return 0;

}