						@Equals Bank
.equ SWI_Open, 0x66
.equ SWI_PrStr, 0x69
.equ SWI_PrChr, 0x00
.equ SWI_RdStr, 0x6a
.equ InputMode, 0
.equ OutputMode, 1
.equ EXIT, 0x11
.equ SWI_Close, 0x68

						@Loading the test.txt file into the CharArray to process

LDR R0, =InFileName
MOV R1, #InputMode
SWI SWI_Open
BCS InFileError			@If the CPS has a 1, an error occured on opening the file
LDR R1, =InFileHandle
STR R0, [R1]			@This step stores the result of opening the test.txt file form R0 at the file handle
LDR R0, =InFileHandle
LDR R0, [R0]
LDR R1, =CharArray
MOV R2, #1024
SWI SWI_RdStr			@Now CharArray should contain all the strings in the test.txt file

						@Load the the output64

LDR R0, =OutFileName	@load output file name
MOV R1, #OutputMode		@output mode
SWI SWI_Open			@OPen file for output
BCS OutFileError
LDR R1, =OutFileHandle  @Load output file handle
STR R0, [R1]			@Saves the file handle at the start of the OutFile address

						@Encrypt the Characters in CharArray
						@Start with gathering the first 3 char

LDR R1, =CharArray
LDR R3, =OutFileArray
READLOOP:

LDRB R4, [R1]			@First Char in R4
ADD R1, R1, #1			@Move the pointer to next Char
LDRB R5, [R1]			@Second Char in R5
CMP R5, #00
BEQ PADTWO			
ADD R1, R1, #1			@Move the pointer to next Char
LDRB R6, [R1]			@Thrid Char in R6
CMP R6, #00
BEQ PADONE
ADD R1, R1, #1			@Move the pointer to next Char


@Combine the Char
COMBINE:
MOV R2, R4				@Move First Char into R2
MOV R2, R2, LSL #8		@Shift the First Char left a Byte to make room for Second Char in R2
ORR R2, R2, R5			@First Char + Second Char
MOV R2, R2, LSL #8		@Shift for Thrid Char
ORR R2, R2, R6			@Thrid Char Added to R2, Now total of 3 Bytes (24 bits)


@Cut the 3Bytes into 4 peices of 6 bits and store in R6, R7, R8, R9
@Takes a LIFO style

AND R7, R2, #0x3f		@first chunk of 6bits stored in R7, Last Char Read
MOV R2, R2, LSR #6
AND R8, R2, #0x3f		@second chunk R8
MOV R2, R2, LSR #6
AND R9, R2, #0x3f		@thrid chunk R9
MOV R2, R2, LSR #6
AND R10, R2, #0x3f		@fourth chunk R10



						@Store the Char into the OutFileArray

LDR R5, =LTable
LDRB R0, [R5, R10]
STRB R0, [R3]			@Store char in the OutFileArray
ADD R3, R3, #1
LDRB R0, [R5, R9]
AND R0, R0, #0xff
STRB R0, [R3]			@Store char in the OutFileArray
ADD R3, R3, #1
LDRB R0, [R5, R8]
AND R0, R0, #0xff
STRB R0, [R3]			@Store char in the OutFileArray
ADD R3, R3, #1
LDRB R0, [R5, R7]
AND R0, R0, #0xff
STRB R0, [R3]			@Store char in the OutFileArray
ADD R3, R3, #1


LDRB R4, [R1]
CMP R4, #13
BEQ EXITPROGRAM
CMP R4, #10
BEQ EXITPROGRAM
CMP R4, #0
BGT READLOOP

EXITPROGRAM:
							@Process the outfile writing before exit\
LDR R0, =OutFileHandle
LDR R0, [R0]
LDR R1, =OutFileArray
SWI SWI_PrStr
ldr r0,=OutFileHandle
ldr r0,[r0]
swi SWI_Close				@Close the outfile

							@Close the infile
LDR r0,=InFileHandle
LDR r0,[r0]
SWI SWI_Close				

			
SWI EXIT					@Exit Program



							@PAD THE NON DIVISABLE BY 3 WORDS
PADTWO:
MOV R2, R4				@Move First Char into R2
MOV R2, R2, LSL #8
ORR R2, R2, #0x00
MOV R2, R2, LSL #8
ORR R2, R2, #0x00

MOV R2, R2, ROR #12	
AND R7, R2, #0x3f		@first chunk of 6bits stored in R7, Last Char Read  plust the spill over 000000
MOV R2, R2, LSR #6
AND R8, R2, #0x3f		@second chunk R8  Whatever value of the first char
						@Store the char and the spill over
LDR R5, =LTable
LDRB R0, [R5, R8]
STRB R0, [R3]			@Store char in the OutFileArray
ADD R3, R3, #1
LDRB R0, [R5, R7]
STRB R0, [R3]
ADD R3, R3, #1
MOV R0, #61
STRB R0, [R3]
ADD R3, R3, #1
STRB R0, [R3]
BAL EXITPROGRAM

PADONE:
MOV R2, R4				@Move First Char into R2
MOV R2, R2, LSL #8		@Shift the First Char left a Byte to make room for Second Char in R2
ORR R2, R2, R5			@First Char + Second Char
MOV R2, R2, LSL #8

MOV R2, R2, ROR #6
AND R7, R2, #0x3f		@first chunk of 6bits stored in R7, Last Char Read plust the spill over
MOV R2, R2, ROR #6
AND R8, R2, #0x3f		@second chunk R8
MOV R2, R2, ROR #6
AND R9, R2, #0x3f		@thrid chunk R9


LDR R5, =LTable
LDRB R0, [R5, R9]
STRB R0, [R3]			@Store char in the OutFileArray
ADD R3, R3, #1
LDRB R0, [R5, R8]
STRB R0, [R3]			@Store char in the OutFileArray
ADD R3, R3, #1
LDRB R0, [R5, R7]
STRB R0, [R3]
ADD R3, R3, #1
MOV R0, #61
STRB R0, [R3]
BAL EXITPROGRAM


							@PRINT SPACE CHAR THEN JUMP BACK TO NEXT WORD
SPACE:
MOV R2, R4					@Move Space char into R2
MOV R2, R2, LSL #8			@Shift it to the left to make room
AND R7, R2, #0x3f			@Moves Bottom half of the SPACE char into R7
MOV R2, R2, LSR #6		
AND R8, R2, #0x3f			@Moves Top half of the SPACE Char into R8
LDR R9, =LTable
LDRB R0, [R9, R8]
STRB R0, [R3]
ADD R3, R3, #1
LDRB R0, [R9, R7]
STRB R0, [R3]
ADD R3, R3, #1
B READLOOP


InFileError: 
MOV R0, #OutputMode
LDR R1, =InError
SWI SWI_PrStr
SWI EXIT

OutFileError:
MOV R0, #OutputMode
LDR R1, =OutError
SWI SWI_PrStr
SWI EXIT


.data
LTable: .ascii "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
InFileHandle: .word 0
OutFileHandle: .word 0
InFileName: .asciiz "input.txt"
OutFileName: .asciiz "output64.txt"
CharArray: .skip 1024
OutFileArray: .skip 2000
InError: .asciiz "Error with open input file operation\n"
OutError: .ascii "Error with output file operation\n"	