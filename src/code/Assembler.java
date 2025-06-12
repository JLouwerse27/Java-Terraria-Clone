package src.code;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
 * 
 */


/*
    MyAssemblerMadeInJavaAndNotepad
    @author Joseph Louwerse
    @date Mar 15, 2025
    @version 0.0.2
*/
public class Assembler {

    final PunchCard pc;
	int[][] card;
	private int lineIndex = 0;
	private boolean normalRead = true;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final int speedms = 10;

	private static final int OPCODE_FIRST_BIT = 0;
	private static final int OPCODE_LAST_BIT = 3;
    private static final int X_POS_FIRST_BIT = 4;
    private static final int X_POS_LAST_BIT = 6;
    private static final int Y_POS_FIRST_BIT = 7;
    private static final int Y_POS_LAST_BIT = 9;
    private static final int DISPLAY_FIRST_BIT = 12;
    private static final int DISPLAY_LAST_BIT = 15;

	private static final int SCREEN_BITS = 4;//bits used to display the screen

	private static final int FINAL_BIT = OPCODE_LAST_BIT + 12;
	private static final int OFF = 0;
	private static final int ON = 1;

    private static final int n0 = 0;
    private static final int n1 = 1;
    private static final int n2 = 2;
    private static final int n3 = 3;
    private static final int n4 = 4;

    private static int displayWidth;
    /* Make new punch card and read it */
    public Assembler(int s) {
        displayWidth = s;
        pc = new PunchCard();
		readPunchCard(pc);
        //startReadingPunchCard();
    }
    
     /*reads the punch card*/
    public synchronized void readPunchCard(final PunchCard pc) {
        card = pc.getCard();
		while(lineIndex < card.length){
            readOpCode();
        	//readLine(lineIndex);
			try {
				Thread.sleep(speedms); // Sleep for 333 milliseconds (3 times per second)
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
            if(normalRead){
                //lineIndex++;
            }
		};
        // for(int i = 0; i < card.length; i++){
		//     readLine(card[i]);
        //     try {
        //         Thread.sleep(333); // Sleep for 333 milliseconds (3 times per second)
        //     } catch (InterruptedException e) {
        //         Thread.currentThread().interrupt();
        //         break;
        //     }
        //     System.out.println("Finished reading line " + i);
        // }
    }



		/*
	----------READING TYPES----------
	NORMAL (normal reading):
	OPERATION/NO OP, 9 SIGNALS ON/OFF, NEXT/STEP
	Ex "1,000,000,111,1" sinmply lights up the bottom 3 and goes to the next instruction
	Ex "1,000,000,111,0" would lights up the bottom 3 triggers the step signal

	GO TO (goes to an instruction):
	REGULAR/RELATIVE, POS/neg, 9 BITS
	Ex "00,00_000_000_1" would step to the next instruction
	Ex "01,000_000_000_1" would step to the previous instruction, thus looping the program
	Ex "00,000_000_000_0" would step to the next instruction and stop reading the punch card
	eX	"01,00_000_000_0" would step to the previous instruction and stop reading the punch card
	EX "10,00_000_000_1" would step to the first instruction
	--------------------------------
	*/

    public int decimal(final int fb, final int lb) {
        int decimal = 0;
        for(int i = fb; i <= lb; i++){
            if(card[lineIndex][i] == ON)
                decimal += Math.pow(n2, lb - i);
        }
        return decimal;
    }

    public void readOpCode(){
        int decimal = decimal(OPCODE_FIRST_BIT, OPCODE_LAST_BIT);
        
        // System.out.println("Decimal: " + decimal);

        if(decimal == 0){
            System.out.println("DISPLAY TOPLEFT 32BY32 QUADRANT");
            displayLine(0,0,n0); 
        }else if (decimal == 1 && displayWidth % 32 == 0){   
            System.out.println("DISPLAY SECOND 32BY32 QUADRANT");
            displayLine(0,1*displayWidth/2,n1);
        }  else if (decimal == 2 && displayWidth % 32 == 0){   
            System.out.println("DISPLAY THIRD 32BY32 QUADRANT");
            displayLine(1*displayWidth/2,0,n2);
        }else if (decimal == 3 && displayWidth % 32 == 0){   
            System.out.println("DISPLAY FOURTH 32BY32 QUADRANT");
            displayLine(1*displayWidth/2,1*displayWidth/2,n3);
        }else if (decimal == 4 && displayWidth % 32 == 0){   
            System.out.println("VARIABLE");
            displayLine(0,0,n0);
        }else if (decimal == 14){
            System.out.println("END OF PROGRAM");
        }else if (decimal == 15){
            System.out.println("NO OP");
        }
        
        lineIndex++;
    }

    public void displayLine(int yoff, int xoff, int pos){
        int[] line = card[lineIndex];
        int y = decimal(OPCODE_LAST_BIT + n1, OPCODE_LAST_BIT + n3);
        int x = decimal(OPCODE_LAST_BIT + n4, OPCODE_LAST_BIT + 6);
        int colour = decimal(OPCODE_LAST_BIT + 7, OPCODE_LAST_BIT + 8);

        display(xoff,yoff,x, y, colour, pos);
    

        for(int j = OPCODE_LAST_BIT + n1; j <= FINAL_BIT; j++){
            if(j > OPCODE_LAST_BIT && j <= OPCODE_LAST_BIT + n3){
                //display(j, pos, line[j]);
            }
            // else {
			// 	boolean relative = false;
			// 	if(j == REG_REL_BIT){
            //         if(line[j] == OFF) {
			// 			//goes x instructions away
            //             System.out.println("relative go to");
			// 			relative = true;
            //         }else{
			// 			//goes to x instruction
			// 			System.out.println("set go to");
			// 			relative = false;
			// 		}
            //     }  
			// 	if(relative){
			// 		if(j == POS_NEG_BIT){
			// 			if(line[j] == ON){
			// 				System.out.println("positive go to");
			// 			}else{
			// 				System.out.println("negative go to");
			// 			}
			// 		}
			// 		int position = n0;
			// 		for(int k = n2; k < line.length; k++){
			// 			position += line[k] * Math.pow(n2, FINAL_BIT - k);
			// 		}
			// 		if(j == FINAL_BIT){
			// 			if (position == n0){
            //                 lineIndex = FINAL_BIT + n1;
            //             }else{
            //                 System.out.println("Go to line " + position);
            //             }
			// 		}
			// 	}else{

            //     }
			// 	lineIndex = n0;
			// }		
        }
        System.out.println("Finished reading line " + lineIndex);		
        // if(normalRead){
        //     lineIndex++;
        // } else normalRead = true;
    }

    /* Start reading the punch card at a fixed rate */
    private void startReadingPunchCard() {
        final Runnable reader = new Runnable() {
            public void run() {
                //readPunchCard(pc);
            }
        };
        scheduler.scheduleAtFixedRate(reader, 0, 333, TimeUnit.MILLISECONDS);
    }

    /** configures a DISPLAY_WIDTH by DISPLAY_WIDTH array display,
     can be set to on or off	*/
    public void display(int bigXoff, int bigYoff, int x, int y, int colour, int offset){
        x*=n2;
        y*=n2;

        x += bigXoff;
        y += bigYoff;

        for (int i = DISPLAY_FIRST_BIT; i <= DISPLAY_LAST_BIT; i++){
            
            int yoff = 0;
            int xoff = 0;
            if(i % n2 == n1){
                xoff = n1;
            }else {
                xoff = n0;
            }
            if(i - DISPLAY_FIRST_BIT >= n2) {
                yoff = n1;
            }else {
                yoff = n0;
            }
            int onOff = pc.getCard()[lineIndex][i];
            //System.out.println("Display " + pos + " is " + (onOff == 1 ? "ON" : "OFF"));
            if (onOff == ON) {
                Main.getTiles()[y+yoff][x+xoff] = TileString.Empty.getSymbol();
            } else {
                Main.getTiles()[y+yoff][x+xoff] = TileString.Wall.getSymbol();
            }
        }
        
        
		Main.getMyFrame().getContentPane().repaint();
    }

    public void not(boolean b){
        if(b){
            b = false;
        }else{
            b = true;
        }
    }

    // public static void main(String[] args){
    //     Assembler as = new Assembler(n4);
    //     System.out.println("Hello World!");
    // }
}