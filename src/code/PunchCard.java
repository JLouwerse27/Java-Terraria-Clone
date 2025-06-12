package src.code;

import java.util.*;
/*
----------OPERATIONS----------
NO-OP (no operation):
00 (OPCODE), 9 0's OR 1's

NORMAL (normal reading):
0001 (OPCODE), 3 SIGNALS ON/OFF, NEXT/STEP
Ex "11,000,000,111" sinmply lights up the bottom 3 and goes to the next instruction
Ex "11,000,000,111" would lights up the bottom 3 triggers the step signal

GO TO (goes to an instruction):
//IF (CONDITION BIT = 1), THEN IT CHECKS THE THREE PREVIOUS LINES FOR A variable, an operator and a value. IF THE CONDITION IS MET, THEN IT GOES TO the specified line
01 (OPCODE), REGULAR/RELATIVE, (POS/neg), (COND/NO COND),  6 BITS
01 (OPCODE), REGULAR/RELATIVE, (POS/neg), 9 BITS
01 (OPCODE), REGULAR/RELATIVE, (POS/neg), 9 BITS
Ex "01,0,0,000_000_1" would step to the next instruction
Ex "01,0,1,000_000_1" would step to the previous instruction, thus looping the program
Ex "01,0,1,000_000_0" would step to the next instruction and stop reading the punch card
Ex "01,0,0,000_000_0" would step to the previous instruction and stop reading the punch card
EX "10,1,0000_000_1" would step to the first instruction\



VARIABLE/OPERATIONS (variable):
01 (OPCODE), 2 BITS FOR TYPE, 2 BITS SUBTYPE (POINTER, SETTER, INTEGER, GETTER), 4 BITS FOR ADDRESS LOCATION (FOR POINTER AND CHANGER), 2 BITS FOR VALUES
01 (OPCODE), 00 TYPE = VARIABLES, 2 BITS SUBTYPE (POINTER, SETTER, INTEGER, GETTER), 4 BITS FOR ADDRESS LOCATION (FOR POINTER AND CHANGER), 2 BITS FOR VALUES
01 (OPCODE), 01 TYPE = MATH, 2 BITS SUBTYPE (ADD, SUB, MUL, DIV), 4 BITS FOR ADDRESS LOCATION (FOR POINTER AND CHANGER), 2 BITS FOR VALUES
01 (OPCODE), 10 TYPE = CONDITIONALS, 2 BITS SUBTYPE (GREATER, LESS, EQUAL, GETTER), 4 BITS FOR ADDRESS LOCATION (FOR POINTER AND CHANGER), 2 BITS FOR VALUES
01 (OPCODE), 11 TYPE = , LOGICAL OPERATORS (NOT, AND, OR, --), 4 BITS FOR ADDRESS LOCATION (FOR POINTER AND CHANGER), 2 BITS FOR VALUES



POINTER SIMPLY POINTS TO AN INTEGER, SETTER SETS AN INTEGER, INTEGER IS A NUMBER, GETTER GETS AN INTEGER
--------------------------------
*/
public class PunchCard {
	private List<Long> cardList = new ArrayList<Long>();
	int X = 0;//X is a don't care
	private int[][] card = 
			{	
				//display a full 32 by 32 black screen
				{0,0,0,0, 0,0,0, 0,0,0, 0,0, 0,0,0,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,0,0, 0,0,1, 0,0, 0,0,0,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,0,0, 0,1,0, 0,0, 0,0,0,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,0,0, 0,1,1, 0,0, 0,0,0,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,0,0, 1,0,0, 0,0, 0,0,0,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,0,0, 1,0,1, 0,0, 0,0,0,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,0,0, 1,1,0, 0,0, 0,1,0,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,0,0, 1,1,1, 0,0, 0,1,0,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				
				{0,0,0,0, 0,0,1, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,0,1, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,0,1, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,0,1, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,0,1, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,0,1, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,0,1, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,0,1, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				
				{0,0,0,0, 0,1,0, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,1,0, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,1,0, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,1,0, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,1,0, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,1,0, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,1,0, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,1,0, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				
				{0,0,0,0, 0,1,1, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,1,1, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,1,1, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,1,1, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,1,1, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,1,1, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,1,1, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 0,1,1, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 

				{0,0,0,0, 1,0,0, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,0,0, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,0,0, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,0,0, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,0,0, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,0,0, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,0,0, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,0,0, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				
				{0,0,0,0, 1,0,1, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,0,1, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,0,1, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,0,1, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,0,1, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,0,1, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,0,1, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,0,1, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL,1

				{0,0,0,0, 1,1,0, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,1,0, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,1,0, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,1,0, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,1,0, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,1,0, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,1,0, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,1,0, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				
				{0,0,0,0, 1,1,1, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,1,1, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,1,1, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,1,1, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,1,1, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,1,1, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,1,1, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,0, 1,1,1, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 

				//{0,1,0,0, 0,0,0,0, 0,0,0,0,0,0,1,1}, //VARIABLE
				//{1,0,0,0, 0,0, 0,0,0,0,0,0,0,0,0}, //GO TO

				//display a full 32 by 32 black screen
				{0,0,0,1, 0,0,0, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,0,0, 0,0,1, 0,0, 1,1,0,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,0,0, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,0,0, 0,1,1, 0,0, 1,1,0,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,0,0, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,0,0, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,0,0, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,0,0, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				
				{0,0,0,1, 0,0,1, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,0,1, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,0,1, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,0,1, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,0,1, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,0,1, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,0,1, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,0,1, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				
				{0,0,0,1, 0,1,0, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,1,0, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,1,0, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,1,0, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,1,0, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,1,0, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,1,0, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,1,0, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				
				{0,0,0,1, 0,1,1, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,1,1, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,1,1, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,1,1, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,1,1, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,1,1, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,1,1, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 0,1,1, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 

				{0,0,0,1, 1,0,0, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,0,0, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,0,0, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,0,0, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,0,0, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,0,0, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,0,0, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,0,0, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				
				{0,0,0,1, 1,0,1, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,0,1, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,0,1, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,0,1, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,0,1, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,0,1, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,0,1, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,0,1, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL,1

				{0,0,0,1, 1,1,0, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,1,0, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,1,0, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,1,0, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,1,0, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,1,0, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,1,0, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,1,0, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				
				{0,0,0,1, 1,1,1, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,1,1, 0,0,1, 0,0, 0,0,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,1,1, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,1,1, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,1,1, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,1,1, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,1,1, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,0,1, 1,1,1, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 

				//display a full 32 by 32 black screen
				{0,0,1,0, 0,0,0, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,0,0, 0,0,1, 0,0, 1,1,0,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,0,0, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,0,0, 0,1,1, 0,0, 1,1,0,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,0,0, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,0,0, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,0,0, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,0,0, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				
				{0,0,1,0, 0,0,1, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,0,1, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,0,1, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,0,1, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,0,1, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,0,1, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,0,1, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,0,1, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				
				{0,0,1,0, 0,1,0, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,1,0, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,1,0, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,1,0, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,1,0, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,1,0, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,1,0, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,1,0, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				
				{0,0,1,0, 0,1,1, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,1,1, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,1,1, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,1,1, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,1,1, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,1,1, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,1,1, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 0,1,1, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 

				{0,0,1,0, 1,0,0, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,0,0, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,0,0, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,0,0, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,0,0, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,0,0, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,0,0, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,0,0, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				
				{0,0,1,0, 1,0,1, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,0,1, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,0,1, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,0,1, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,0,1, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,0,1, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,0,1, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,0,1, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL,1

				{0,0,1,0, 1,1,0, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,1,0, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,1,0, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,1,0, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,1,0, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,1,0, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,1,0, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,1,0, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				
				{0,0,1,0, 1,1,1, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,1,1, 0,0,1, 0,0, 0,0,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,1,1, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,1,1, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,1,1, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,1,1, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,1,1, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,0, 1,1,1, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 

				//display a full 32 by 32 black screen
				{0,0,1,1, 0,0,0, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,0,0, 0,0,1, 0,0, 1,1,0,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,0,0, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,0,0, 0,1,1, 0,0, 1,1,0,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,0,0, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,0,0, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,0,0, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,0,0, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				
				{0,0,1,1, 0,0,1, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,0,1, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,0,1, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,0,1, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,0,1, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,0,1, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,0,1, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,0,1, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				
				{0,0,1,1, 0,1,0, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,1,0, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,1,0, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,1,0, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,1,0, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,1,0, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,1,0, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,1,0, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				
				{0,0,1,1, 0,1,1, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,1,1, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,1,1, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,1,1, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,1,1, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,1,1, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,1,1, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 0,1,1, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 

				{0,0,1,1, 1,0,0, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,0,0, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,0,0, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,0,0, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,0,0, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,0,0, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,0,0, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,0,0, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				
				{0,0,1,1, 1,0,1, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,0,1, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,0,1, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,0,1, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,0,1, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,0,1, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,0,1, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,0,1, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL,1

				{0,0,1,1, 1,1,0, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,1,0, 0,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,1,0, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,1,0, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,1,0, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,1,0, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,1,0, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,1,0, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				
				{0,0,1,1, 1,1,1, 0,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,1,1, 0,0,1, 0,0, 0,0,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,1,1, 0,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,1,1, 0,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,1,1, 1,0,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,1,1, 1,0,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,1,1, 1,1,0, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 
				{0,0,1,1, 1,1,1, 1,1,1, 0,0, 1,1,1,1},//DISPLAY, LINE, y, x, COLOUR, FULL, 



				{0,0,0,1, 0,0,0, 0,0,0, 0,0, 0,0,0,0},//DISPLAY TOP RIGHT: FULL
				{0,0,1,0, 0,0,0, 0,0,0, 0,0, 0,0,0,0},//DISPLAY SECOND: BLANK
				{0,0,1,1, 0,0,0, 0,0,0, 0,0, 0,0,0,0},

				// {0,0,1,1, 0,0,0,0},//DISPLAY THIRD: BLANK	
				// {1,0,1,1, 0,0,0,0},//DISPLAY THIRD: BLANK	
				// {0,1,0,0, 0,0,1,0},//
				// {0,0,0,0, 1,1,0,1},
				// {0,0,1,0, 1,1,0,1},
				// {0,0,1,1, 0,0,0,1},
				// {0,1,0,0, 0,0,0,1},
				// {0,0,0,0, X,X,X,X},//NO-OP
				// {0,0,0,0, 1,X,X,X},//NO-OP
				// {0,1, 0,0,0,0,1,1,1,0,0},//variable, value
				// {1,1, 0,1,1,0,1,1,0,0,0},//2top, 2mid, next
				// {1,1, 0,0,0,0,1,1,0,1,1},//2mid, 2bot,
				// {1,0, 1,0,0,0,0,0,0,0,1}//GO-TO line 1
			};
		private int[][] superMario = 
			{
				{1,1, 0,0,0,0,0,0,0,0,0},//BLANK,NEXT 
				{1,1, 1,1,1,0,0,0,0,0,0},//3TOP, NEXT
				{1,1, 1,1,0,1,1,0,0,0,0},//2TOP, 2MID, NEXT
				{0,0, X,X,X,X,X,X,X,X,X},//NO-OP
				{0,0, X,X,X,X,X,1,X,X,X},//NO-OP
				{0,1, 0,0,0, 0,1,1,1,0,0},//variable, type,value
				{1,1, 0,1,1,0,1,1,0,0,0},//2top, 2mid, next
				{1,1, 0,0,0,0,1,1,0,1,1},//2mid, 2bot,
				{1,0, 1,0,0,0,0,0,0,0,1}//GO-TO line 1
			};
	public PunchCard(){
		
	}
	public int[][] getCard() {
		return card;
	}
}
