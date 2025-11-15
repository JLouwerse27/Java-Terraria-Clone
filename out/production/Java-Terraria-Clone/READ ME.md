```
Hello viewers of the jar file package

Video at https://youtu.be/-jwTEhw6rDQ

This is a tutorial of how to use my Java Logic Gate clone.
It is loosely inspired from Redstone.

So to start first extract the folder.
Then go to the jar, make sure you have Java 22 installed!
The read me is also helpful
Click load if you want to see this file, otherwise click create file

Press escape to pause the simulation
Press plus to go up a "level" and minus to go down
Press enter to speed up the simulation 
and shift enter to slow it down

Press WASD or arrow keys to move around
or drag while in DEFAULT mode


Press E to go into editing mode, this is how you edit the world
More of this in the "READ ME".PNG file
Press 1 to select switches
Remember this is kind of like minecraft
2 for wires
3 for double wire (cross path)
4 for not gate
5 for or gate
6 and gate
7 led

8
9
i
o
those are resistors

You can press [ and ] to cycle through your "inventory"


Press control and scroll to zoom in out

While in editing mode, press R in order to rotate blocks
All gates (and or not) must be rotated in order to work
i.e. they cannot have a blank direction (blank direction means output to every direction)

Grey things are switches
Bright red means wire ON
Darker red means wire OFF
Very dark red means wire DEAD (which is the default state)

Right click while in editing mode to remove a block

Ok dont put multiple inputs in a NOT gate or it might screw it up
A screwed up NOT gate works if you remove it and replace it


The thing at the right side is a debug, it will show all the signals being propogated at the current tick.

On level two is an example of the TLW thing from my COMP architecture class

	There are 8 instructions (its halfway complete right now) 
	and there are 4 inputs to control which one you want (it could have up to 16 instructions)

On level six, at the bottom right is my computer

	its pretty basic but it can:
 	 -add a number
	 -read and write 3 bit numbers
	thats about it right now.
	But YOU dear reader can make it however you want it to,
	all the necessary items are at your disposal

	To execute instructions 
	===note: wait for the most of signals to stop propogating before going on to the next step===
	1. press the instruction switch
	2. then press the switch near the middle to turn the machine on,
	3. then press the switch near the middle again to turn the machine off (like an off signal from a clock) 
        4. press the next instruction switch and repeat steps 2 and 3.

	Execute the first instruction to write 3 to register 1
	Execute the second instruction to write 5 to register 2
	Execute the third instruction to write 6 to r1
	Execute the sixth instruction to write 3 and 3 together which is 6
	actually nvm it was 7 + 7

	first 4 bits are the opcode
	second 4 are the first operand
	third 4 are the second operand

	you can edit the instructions (using wires or not gates) to use the computer as you want


-------------------------------------------------Updates--------------------------------------------------

Nov 4, 2025===Just added step through!==================
Press space to disable ticks.
Then press "." to step through ticks.
Press space again if you want to enable ticks as normal.
========================================================

Nov 5, 2025===Just added better editing!==================
Press "e" to enable editing.
The HUD will show the 
 -block type
 -direction 1
 -direction 2
Press c to change direction 1 for all future blocks placed
Press x to change direction 2 for all future blocks placed
Press e again if you want to go back to defualt
==========================================================

Nov 7, 2025===========Just added better cut copy paste!=========================
Press normal cut copy and paste commands for normal cut copy paste of one layer.
Press shift while doing cut or copy to do a cut/copy of all the layers.
For one layer cut and copy, paste can be done on any layer.
Multiple layer cut and copy will always be pasted from the first layer,
as you are cutting/copying from every layer.
================================================================================
----------------------------------------------------------------------------------------------------------

Ok that's about it!
Thank you for reading!
```