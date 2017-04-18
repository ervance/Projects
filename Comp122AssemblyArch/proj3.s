@Safe Project 3

.equ EXIT, 0x11
@----WaitTimerValues
.equ Sec8, 1000
.equ EmbestTimerMask, 0x7FFF
.equ Top15bitRange, 0x0000FFFF
.equ SWI_GetTicks, 0x6d

@----8 Segment Display-----
.equ SWI_LED, 0x200
.equ U, 0x6d
.equ L, 0x0d
.equ P, 0xc7
.equ C, 0x8d
.equ F, 0x87
.equ A, 0xe7
.equ E, 0x8f

@-------Blue Buttons-------
.equ SWI_CheckBlue, 0x203
.equ Blue_Button_00, 0x01
.equ Blue_Button_01, 0x02
.equ Blue_Button_02, 0x04
.equ Blue_Button_03, 0x08
.equ Blue_Button_04, 0x10
.equ Blue_Button_05, 0x20
.equ Blue_Button_06, 0x40
.equ Blue_Button_07, 0x80
.equ Blue_Button_08, 0x100
.equ Blue_Button_09, 0x200
.equ Blue_Button_10, 0x400
.equ Blue_Button_11, 0x800
.equ Blue_Button_12, 0x1000
.equ Blue_Button_13, 0x2000
.equ Blue_Button_14, 0x4000
.equ Blue_Button_15, 0x8000
.equ Blue_Button_16, 0x10000


@------Black Buttons ------
.equ SWI_CheckBlack, 0x202
.equ Black_Button_Right, 0x01
.equ Black_Button_Left, 0x02


						@R5 = 0,  This means cannot Lock because no codes exist in the data base
LDR R10, =SavedCodes
LDR R9, =CodeEntries
MOV R6, R10
MOV R1, #0
MOV R0, #U
SWI SWI_LED
BAL BlackLoop

CodeSavedRestartIdle:
LDR R10, =SavedCodes
LDR R9, =CodeEntries
MOV R0, #0x00
STRB R0, [R9]
MOV R0, #0xFF
STRB R0, [R6]
ADD R6, R6, #1			
MOV R1, #0
BAL EraseEntriesCode

BlackLoop:
MOV R0, #0
Black:
SWI SWI_CheckBlack
CMP R0, #0
BEQ Black
BlackButton:
CMP R0, #Black_Button_Right
BGT Lock
BAL RightBlackButton

BlueRestart:
LDR R9, =CodeEntries
BlueLoop:
MOV R0, #0
CheckBlueButton:
SWI SWI_CheckBlack
CMP R0, #0
BNE BlackButton
SWI SWI_CheckBlue
CMP R0, #0
BEQ CheckBlueButton

CMP R0, #Blue_Button_00
BEQ ZERO

CMP R0, #Blue_Button_01
BEQ ONE

CMP R0, #Blue_Button_02
BEQ TWO

CMP R0, #Blue_Button_03
BEQ THREE

CMP R0, #Blue_Button_04
BEQ FOUR

CMP R0, #Blue_Button_05
BEQ FIVE

CMP R0, #Blue_Button_06
BEQ SIX

CMP R0, #Blue_Button_07
BEQ SEVEN

CMP R0, #Blue_Button_08
BEQ EIGHT

CMP R0, #Blue_Button_09
BEQ NINE

CMP R0, #Blue_Button_10
BEQ TEN

CMP R0, #Blue_Button_11
BEQ ELEVEN

CMP R0, #Blue_Button_12
BEQ TWELVE

CMP R0, #Blue_Button_13
BEQ THIRTEEN

CMP R0, #Blue_Button_14
BEQ FOURTEEN

CMP R0, #Blue_Button_15
BEQ FIFTEEN

CMP R0, #Blue_Button_16
BEQ SIXTEEN

BAL BlueLoop

ZERO:
MOV R8, #48
CMP R5, #1
BEQ Confirmation
CMP R5, #2
BEQ ForgetConfirm
STRB R8, [R9]
ADD R9, R9, #1
BAL BlueLoop

ONE:
MOV R8, #49
CMP R5, #1
BEQ Confirmation
CMP R5, #2
BEQ ForgetConfirm
STRB R8, [R9]
ADD R9, R9, #1
BAL BlueLoop

TWO:
MOV R8, #50
CMP R5, #1
BEQ Confirmation
CMP R5, #2
BEQ ForgetConfirm
STRB R8, [R9]
ADD R9, R9, #1
BAL BlueLoop

THREE:
MOV R8, #51
CMP R5, #1
BEQ Confirmation
CMP R5, #2
BEQ ForgetConfirm
STRB R8, [R9]
ADD R9, R9, #1
BAL BlueLoop

FOUR:
MOV R8, #52
CMP R5, #1
BEQ Confirmation
CMP R5, #2
BEQ ForgetConfirm
STRB R8, [R9]
ADD R9, R9, #1
BAL BlueLoop

FIVE:
MOV R8, #53
CMP R5, #1
BEQ Confirmation
CMP R5, #2
BEQ ForgetConfirm
STRB R8, [R9]
ADD R9, R9, #1
BAL BlueLoop

SIX:
MOV R8, #54
CMP R5, #1
BEQ Confirmation
CMP R5, #2
BEQ ForgetConfirm
STRB R8, [R9]
ADD R9, R9, #1
BAL BlueLoop

SEVEN:
MOV R8, #55
CMP R5, #1
BEQ Confirmation
CMP R5, #2
BEQ ForgetConfirm
STRB R8, [R9]
ADD R9, R9, #1
BAL BlueLoop

EIGHT:
MOV R8, #56
CMP R5, #1
BEQ Confirmation
CMP R5, #2
BEQ ForgetConfirm
STRB R8, [R9]
ADD R9, R9, #1
BAL BlueLoop

NINE:
MOV R8, #57
CMP R5, #1
BEQ Confirmation
CMP R5, #2
BEQ ForgetConfirm
STRB R8, [R9]
ADD R9, R9, #1
BAL BlueLoop

TEN:
MOV R8, #61

CMP R5, #1
BEQ Confirmation
CMP R5, #2
BEQ ForgetConfirm
STRB R8, [R9]
ADD R9, R9, #1
BAL BlueLoop

ELEVEN:
MOV R8, #65


CMP R5, #1
BEQ Confirmation
CMP R5, #2
BEQ ForgetConfirm
STRB R8, [R9]
ADD R9, R9, #1
BAL BlueLoop

TWELVE:
MOV R8, #90

CMP R5, #1
BEQ Confirmation
CMP R5, #2
BEQ ForgetConfirm
STRB R8, [R9]
ADD R9, R9, #1
BAL BlueLoop

THIRTEEN:
MOV R8, #75

CMP R5, #1
BEQ Confirmation
CMP R5, #2
BEQ ForgetConfirm
STRB R8, [R9]
ADD R9, R9, #1
BAL BlueLoop

FOURTEEN:
MOV R8, #32

CMP R5, #1
BEQ Confirmation
CMP R5, #2
BEQ ForgetConfirm
STRB R8, [R9]
ADD R9, R9, #1
BAL BlueLoop

FIFTEEN:
MOV R8, #80

CMP R5, #1
BEQ Confirmation
CMP R5, #2
BEQ ForgetConfirm
STRB R8, [R9]
ADD R9, R9, #1
BAL BlueLoop

SIXTEEN:
MOV R8, #66

CMP R5, #1
BEQ Confirmation
CMP R5, #2
BEQ ForgetConfirm
STRB R8, [R9]
ADD R9, R9, #1
BAL BlueLoop



Program:						
CMP R5, #1						@If R5 = 1 the safe is programing and will need to be confirmed
BEQ ConfirmationEnd
MOV R4, #0
BAL CheckLearnCode

GoodLock:
LDR R9, =CodeEntries
MOV R5, #1						@The lock has four or more digits, so set R5 = 1 so that the code can be confirmed
BAL BlueLoop

Confirmation:						
LDRB R3, [R9]					@Using R3 here to help confirm, may conflict with light
ADD R9, R9, #1
CMP R8, R3
BNE Error
BAL BlueLoop
 
ConfirmationEnd:
CMP R5, #2
BEQ EraseCode
LDRB R3, [R9]
CMP R3, #0xFF
BGT Error
MOV R0, #A
SWI SWI_LED
MOV R5, #5						@When R5 = 5 the safe will be unlocked
BAL SaveCode

CheckLearnCode:
LDR R9, =CodeEntries
CheckFourEntriesLoop:
LDRB R8, [R9]
CMP R8, #0x00
BEQ CheckFour
ADD R4, R4, #1
ADD R9, R9, #1
BAL CheckFourEntriesLoop

CheckFour:
CMP R4, #4
BLT Error
MOV R0, #C
SWI SWI_LED
BAL GoodLock

ValidCheck:				@----CHECK IF LOOP IS VALID to unlock the safe
LDR R10, =SavedCodes
LDRB R7, [R10]			
LDR R9, =CodeEntries
BAL CheckLoop

ValidCheckLoop:
LDR R9, =CodeEntries
ADD R10, R10, #1
LDRB R7, [R10]

CheckLoop:
CMP R7, #0xFF
BEQ SaveCode
LDRB R8, [R9]
CMP R8, R7
BNE SkipSavedEntryLoop
ADD R9, R9, #1
ADD R10, R10, #1
LDRB R7, [R10]
BAL CheckLoop

CheckIfNeedToForget:
LDRB R0, [R10]				@I may be able to change this to just R7 and then could omit loading it just below in forgetcodecheck
CMP R0, #0x00
BEQ Program
BAL ForgetCodeCheck

ForgetCodeCheckWalk:
ADD R10, R10, #1
ForgetCodeCheck:			@Used to exit the forget skip save code loop
LDR R9, =CodeEntries
MOV R2, R10
LDRB R7, [R10]
LDRB R8, [R9]

ForgetCheckLoop:			
CMP R8, #0x00				@This is here if it passed through and the code entered is the same as in the one entered
BEQ ForgetCode				@If it hits thist part without skipping, that means that the entry matched a saved code so we need to now forget that code
CMP R8, R7
BNE ForgetSavedSkipEntryLoop
ADD R9, R9, #1
ADD R10, R10, #1
LDRB R7, [R10]
LDRB R8, [R9]
BAL ForgetCheckLoop


SkipSavedEntryLoop:
LDRB R7, [R10]
CMP R7, #0xFF				
BEQ ValidCheckLoop
CMP R7, #0x00				@if it reaches this then there are no more saved codes and no code matches it
BEQ Error
ADD R10, R10, #1		
BAL SkipSavedEntryLoop

ForgetSavedSkipEntryLoop:	@It came here because it it the end marker in saved codes and there was still a number in the entry.
LDRB R7, [R10, #1]			@may not be necissary
CMP R7, #0x00				@This means its reached the next Saved code-
BEQ ConfirmationLED
FSSkipEntryLoop:
LDRB R7, [R10]
CMP R7, #0xFF	
BEQ ForgetCodeCheckWalk		@if it reaches this then there are no more saved codes and no code matches it		
ADD R10, R10, #1			@ walk through the saved code
BAL FSSkipEntryLoop


SaveCode:
CMP R5, #7
BEQ Unlocked
LDR R9, =CodeEntries
SavedCodeLoop:
LDRB R8, [R9]				@---Loads first entry into R8
STRB R8, [R6]				@---Stores First entry into the last spot of the Saved codes
ADD R6, R6, #1				@---These next two walk forward in the savedcodes and codeentries
ADD R9, R9, #1
LDRB R8, [R9]				@---To check if the end marker has been reached for the codeentries
CMP R8, #0x00
BEQ CodeSavedRestartIdle	@---If it was it will break to the idle and reset otherwise continue this loop until it reaches this point.
BAL SavedCodeLoop

Lock:
LDR R10, =SavedCodes
LDRB R0, [R10]
CMP R0, #0x00
BEQ EraseEntriesCode		@Cause a branch if there are no codes that are programmed in
CMP R5, #5					
BLT Error					@Will branch if the the lock was in a different mode other than unlocked, ie it was learning/programming/doing something
CMP R5, #7
BEQ ValidCheck			 
MOV R0, #L
SWI SWI_LED
MOV R5, #7					@Setting R5 = 7 to lock the safe 
LDR R9, =CodeEntries		@Forgetting any Code entries bc it intterupted/Reseting to get ready for new entries
MOV R1, #0
BAL EraseEntriesCode



ForgetCode:					
LDR R9, =CodeEntries
MOV R5, #2
MOV R0, #F
SWI SWI_LED
BAL BlueLoop

ForgetConfirm:
LDRB R3, [R9]					
ADD R9, R9, #1
CMP R8, R3
BNE Error
BAL BlueLoop

EraseCode:
ADD R10, R10, #1
EraseCodeLoop:
LDRB R8, [R2]
CMP R8, #0x00
BEQ AcceptedErased
LDRB R7, [R10]
STRB R7, [R2]		
ADD R10, R10, #1
ADD R2, R2, #1
BAL EraseCodeLoop

AcceptedErased:
LDR R6, =SavedCodes
AcceptedErasedLoop:
LDRB R7, [R6]
CMP R7, #0x00
BEQ Accepted
ADD R6, R6, #1
BAL AcceptedErasedLoop

Accepted:
MOV R0, #A
SWI SWI_LED
MOV R5, #9
LDR R10, =SavedCodes
MOV R1, #0
BAL EraseEntriesCode


EraseEntriesCode:
LDR R9, =CodeEntries
MOV R0, #0x00
CMP R5, #5					
BLT Error
EraseEntriesLoop:
STRB R0, [R9]
ADD R9, R9, #1
LDRB R7, [R9]
CMP R7, #0x00
BEQ WaitUnlock
BAL EraseEntriesLoop


								@-TIMER Delay the Unlock-
WaitUnlock:
CMP R5, #7
BEQ SkipU
LDR r8,=Top15bitRange
LDR r7,=EmbestTimerMask
LDR r10,=Sec8
SWI SWI_GetTicks				@Get current time T1
MOV r1,r0						@ R1 is T1
AND r1,r1,r7					@ T1 in 15 bits
AcceptRepeatTillTime:

SWI SWI_GetTicks				@Get current time T2
MOV r2,r0						@ R2 is T2
AND r2,r2,r7					@ T2 in 15 bits
CMP r2,r1						@ is T2>T1?
BGE Acceptsimpletime
SUB r9,r8,r1					@ TIME= 32,676 - T1
ADD r9,r9,r2					@ + T2
BAL AcceptCheckInt
Acceptsimpletime:
SUB r9,r2,r1					@ TIME = T2-T1
AcceptCheckInt:
CMP r9,r10						
BLT AcceptRepeatTillTime
MOV R0, #U
SWI SWI_LED
MOV R5, #9
SkipU:
MOV R1, #0

LDR R10, =SavedCodes
BAL BlueRestart


RightBlackButton:
CMP R5, #7
BEQ Error			
ADD R1, R1, #1				@indicates how many times the right black button has been pushed one time
CMP R1, #1
BEQ BlueLoopProgramLED
CMP R1, #2
BEQ CheckIfNeedToForget
CMP R1, #3
BEQ ConfirmationEnd


BlueLoopProgramLED:
LDR R9, =CodeEntries		@this should get rid of anything that was typed in before the right button was hit to program.
LDRB R0, [R9]
MOV R0, #P
SWI SWI_LED
BAL BlueLoop

ConfirmationLED:
MOV R0, #C
SWI SWI_LED
BAL Program


Unlocked:
MOV R5, #9
MOV R1, #0
MOV R0, #U
SWI SWI_LED
LDR R10, =SavedCodes
BAL EraseEntriesCode			@was pointing to blue loop and i got rid of the part that was changing the first entry to 0xff


Error:							@This is to keep the Error showing on the LED until the Left button is pressed again to reset it.
MOV R0, #E
SWI SWI_LED
MOV R5, #6						@R5 = #6 will be in error state

								@TIMER before it locks
LDR r8,=Top15bitRange
LDR r7,=EmbestTimerMask
LDR r10,=Sec8
SWI SWI_GetTicks				@Get current time T1
MOV r1,r0						@ R1 is T1
AND r1,r1,r7					@ T1 in 15 bits
RepeatTillTime:

SWI SWI_GetTicks				@Get current time T2
MOV r2,r0						@ R2 is T2
AND r2,r2,r7					@ T2 in 15 bits
CMP r2,r1						@ is T2>T1?
BGE simpletime
SUB r9,r8,r1					@ TIME= 32,676 - T1
ADD r9,r9,r2					@ + T2
BAL CheckInt
simpletime:
SUB r9,r2,r1					@ TIME = T2-T1
CheckInt:
CMP r9,r10						@is TIME < interval?
BLT RepeatTillTime
BAL Lock



SWI EXIT


.data
SavedCodes: .skip 1024
CodeEntries: .skip 1024
