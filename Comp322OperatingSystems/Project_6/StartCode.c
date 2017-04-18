#include <stdio.h>
#include <sys/mman.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdlib.h>
#include <string.h>

#define TLB_SIZE 32
#define PAGES 256
#define PAGE_MASK 255

#define PAGE_SIZE 256
#define OFFSET_BITS 8
#define OFFSET_MASK 255

#define MEMORY_SIZE PAGES * PAGE_SIZE

// Max number of characters per line of input file to read.
#define BUFFER_SIZE 10

#define BACKING_STORE_256 "BACKING_STORE_256.bin"

struct tlbentry {
    unsigned char logical;//virtual page number
    unsigned char physical;//pyscial frame number
};

// TLB is kept track of as a circular array, with the oldest element being overwritten once the TLB is full.
struct tlbentry tlb[TLB_SIZE];
// number of inserts into TLB that have been completed. Use as tlbindex % TLB_SIZE for the index of the next TLB line to use.
int tlbindex = 0;

// pagetable[logical_page] is the physical page number for logical page. Value is -1 if that logical page isn't yet in the table.
int pagetable[PAGES];

signed char main_memory[MEMORY_SIZE];

// Pointer to memory mapped backing file
signed char *backing;

//
FILE * input_fp;

int max(int a, int b)
{
    if (a > b)
        return a;
    return b;
}

/* Returns the physical address from TLB or -1 if not present. */
int search_tlb(unsigned char logical_page) {
    int i;
    for (i = max((tlbindex - TLB_SIZE), 0); i < tlbindex; i++) {
        struct tlbentry *entry = &tlb[i % TLB_SIZE];
        
        if (entry->logical == logical_page) {
            return entry->physical;
        }
    }
    
    return -1;
}

/* Adds the specified mapping to the TLB, replacing the oldest mapping (FIFO replacement). */
void add_to_tlb(unsigned char logical, unsigned char physical) {
    struct tlbentry *entry = &tlb[tlbindex % TLB_SIZE];
    tlbindex++;
    entry->logical = logical;
    entry->physical = physical;
}

int page_number(int address)
{
    //calculate page number
    int page_number = (address/PAGE_SIZE); 
    return page_number;
}


int pyscial_address(int page, int offset){
    int physcial_address = (page * PAGE_SIZE) + offset;
    return physcial_address;
}

int frame_offset(int logical_address){
    int offset = (logical_address % PAGE_SIZE);
    return offset;
}

int main(int argc, const char *argv[])
{
    if (argc != 3) {
        fprintf(stderr, "Usage ./vmm backingstore input\n");
        exit(1);
    }
    
    int i;
    // Fill page table entries with -1 for initially empty table.
    for(i = 0; i < PAGES; i++)
        pagetable[i] = -1;
    
    // Character buffer for reading lines of input file.
    int logical_address;
    char buffer[BUFFER_SIZE];
    const char * input_fn = argv[2]; //input filename
    const char * backing_fn = argv[1];//backing filename
    /*open the file*/
    input_fp = fopen(input_fn, "r"); 
    int backing_store = open(backing_fn, O_RDONLY);
    backing = mmap(0, MEMORY_SIZE, PROT_READ, MAP_PRIVATE, backing_store,0);


    if (input_fp == NULL || backing == NULL){
        printf("Error opening file\n");
        return -1;
    }
    
    
    //Statiistic Data    
    int hit = 0;
    int miss = 0;
    int total = 0;
    while (fgets(buffer,255, input_fp) != NULL) {
        sscanf(buffer, "%d", &logical_address);
        int logical_page = page_number(logical_address);
        int physical_frame = search_tlb(logical_page);
        unsigned char value;
        int physical_address;
        // TLB hit
        if (physical_frame != -1) {
            hit++;
            physical_address = (physical_frame*PAGE_SIZE) + frame_offset(logical_address);
            value = main_memory[physical_address];
            // TLB miss
        } else {
            miss++;
            // Page fault
            
            //add phsical frame to page table (techincally they should be the same)
            physical_frame = logical_page;
            pagetable[logical_page] = physical_frame;
            
            //add to the table
            add_to_tlb(logical_page, physical_frame);
            int offset = frame_offset(logical_address);
            //calculate the pysical address
            physical_address = (physical_frame*PAGE_SIZE) + offset;
            //get value from the backing store and put it in main memory
            memcpy(main_memory + physical_frame * PAGE_SIZE, backing + logical_page * PAGE_SIZE, PAGE_SIZE);
            value = main_memory[physical_address];
        }
        total++;
        printf("Virtual address: %d Physical address: %d Value: %d\n", logical_address, physical_address, value);
    }
    //Statistics
    double fault_rate, tbl_hit_rate;
    fault_rate = ((double)miss / (double)total) * 100.0;
    tbl_hit_rate = ((double)hit / (double)total)* 100.0;
    printf("Total address translated = %d\nPage Faults = %d\nPage Fault Rate = %.2f\nTLB Hits = %d\nTLB Hit Rate = %.2f\n",total, miss, fault_rate,hit,tbl_hit_rate);
    
    fclose(input_fp);
    return 0;
}
