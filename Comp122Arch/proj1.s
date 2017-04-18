.equ Stdout, 1
.equ SWI_PrInt, 0x6b
.equ SWI_PrChr, 0x00
.equ NewLine, '\n'
.equ SWI_Exit, 0x11

MOV R5, #1
MOV R6, #5
MOV R7, #1
MOV R9, #1000

LOOPBACK:
CMP R5, R9
BGT JUMPOUT
CMP R7, R6
BLT PRINTI
MOV R7, #1
MOV R0, #NewLine
SWI SWI_PrChr
ADD R5, R5, #1
B LOOPBACK

PRINTI:
MOV R1, R5
MOV R0, #Stdout
SWI SWI_PrInt
MOV R1, #NewLine
SWI SWI_PrChr
ADD R5, R5, #1
ADD R7, R7, #1
B LOOPBACK

JUMPOUT:
SWI SWI_Exit