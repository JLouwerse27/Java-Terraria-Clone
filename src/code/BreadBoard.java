package src.code;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for gates made from a grid via user clicking on a tile grid.
 * Example, user clicking one square will turn that square into a wire.
 * Another example is an and circuit.
 * Another example is an led.
 */
public class BreadBoard {

    private final static Boolean OFF = false;
    private final static Boolean ON = true;
    private final static Direction dR = Direction.RIGHT;
    private final static Direction dL = Direction.LEFT;
    private final static Direction dU = Direction.UP;
    private final static Direction dD = Direction.DOWN;
    private final static Direction dI = Direction.INTO;//Z-
    private final static Direction dO = Direction.OUTOF;//Z+
    private final static Direction dN = Direction.NONE;



    /**Empty is 0 in itemEnum*/
    private final static int EMPTY_TYPE = 0;

    private final int WIDTH;
    private final int HEIGHT;
    private final int ZHEIGHT;

    final int TOP_Z;
    final int BOTTOM_Z;

    final static int SIGNAL_ARRAY_LENGTH = 400;
    final static int SIGNAL_ARRAY_D_PLACE = 0;
    final static int SIGNAL_ARRAY_S_PLACE = 1;
    final static int SIGNAL_ARRAY_X_PLACE = 2;
    final static int SIGNAL_ARRAY_Y_PLACE = 3;
    final static int SIGNAL_ARRAY_Z_PLACE = 4;
    final static int SIGNAL_ARRAY_TICK_PLACE = 5;
    final static int MAX_TICKS_IN_THE_FUTURE = 100;
    /**
     * THE width of the signal array for items, which is the location fo the last input thing.
     * So if tick place is the last one and it's equal to 4, then this must be 4.
     */
    final static int SIGNAL_ARRAY_LAST_PLACE = SIGNAL_ARRAY_TICK_PLACE;
    final static int SIGNAL_ARRAY_WIDTH = SIGNAL_ARRAY_LAST_PLACE + 1;

    final static String DEFAULT_KEYWORD = "DEFAULT";
    final static String EDITING_KEYWORD = "EDITING";
    final static String CUTTING_KEYWORD = "CUTTING";
    final static String COPYING_KEYWORD = "COPYING";
    final static String PASTING_KEYWORD = "PASTING";

    final static String EMPTY_SYMBOL      = "__";
    final static String NOT_SYMBOL        = TileString.Not.getSymbol();
    final static String OR_SYMBOL         = TileString.Or.getSymbol();
    final static String AND_SYMBOL        = TileString.And.getSymbol();
    final static String LED_OFF_SYMBOL   = TileString.LEDOff.getSymbol();
    final static String LED_ON_SYMBOL    = TileString.LEDOn.getSymbol();
    final static String BUTTON_OFF_SYMBOL = TileString.ButtonOff.getSymbol();
    final static String BUTTON_ON_SYMBOL  = TileString.ButtonOn.getSymbol();
    final static String SWITCH_OFF_SYMBOL = TileString.SwitchOff.getSymbol();
    final static String SWITCH_ON_SYMBOL  = TileString.SwitchOn.getSymbol();
    final static String WIRE_OFF_SYMBOL   = TileString.WireOff.getSymbol();
    final static String WIRE_ON_SYMBOL    = TileString.WireOn.getSymbol();
    final static String CROSSWIRE_SYMBOL  = TileString.CrossWire.getSymbol();
    final static String RESISTOR_1_SYMBOL = TileString.Resistor1.getSymbol();
    final static String RESISTOR_3_SYMBOL = TileString.Resistor3.getSymbol();
    final static String RESISTOR_5_SYMBOL = TileString.Resistor5.getSymbol();
    final static String RESISTOR_10_SYMBOL = TileString.Resistor10.getSymbol();


    private String gamemode = DEFAULT_KEYWORD;
    public int itemCursor = -1;


    public List<BreadBoardItem> breadBoardItemsList = new ArrayList<BreadBoardItem>();
    public List<CBreadBoardItem> cBreadBoardItemsList = new ArrayList<CBreadBoardItem>();


    /**
     * Okay, so there's three types of classes for our BreadBoard:
     * Buttons,
     * Wires,
     * Gates (AND, OR, NOT, ETC.), and
     * LEDs
     *
     * Problems::
     * Wires (w):
     * How will I "connect" them from one thing to another,
     * How will I split wires,
     *
     * Gates (N, A, O):
     * AND Gate
     *
     * NOT Gate
     *
     * OR Gate
     *
     *
     */
    //note: shortened (removed S and W and L
    private String[] itemEnum = {
            EMPTY_SYMBOL,
            SWITCH_OFF_SYMBOL,
            WIRE_OFF_SYMBOL,
            CROSSWIRE_SYMBOL,
            NOT_SYMBOL,
            OR_SYMBOL,
            AND_SYMBOL,
            LED_OFF_SYMBOL,
            RESISTOR_1_SYMBOL,
            RESISTOR_3_SYMBOL,
            RESISTOR_5_SYMBOL,
            RESISTOR_10_SYMBOL
    };

    //full version
    //private String[] fullItemEnum = {"_","s", "w", "X", "N","O","A","l"};

    /**
     * May have to replace later, like changing it to an ArrayList
     */
    private Object[][] signalArray = new Object[SIGNAL_ARRAY_LENGTH][SIGNAL_ARRAY_WIDTH];//carries up to SIGNAL_ARRAY_LENGTH signals
    private Object[][] tempSignalArray = new Object[SIGNAL_ARRAY_LENGTH][SIGNAL_ARRAY_WIDTH];


    private void setSignalArrayToNull(){
        for (int i = 0; i < signalArray.length; i++) {
            signalArray[i] = null;
        }
    }

    //current breadboard stuff; different from default - I think.
    private String[][][] breadboard;
    private Direction[][][] breadboardDirection;
    private Direction[][][] breadboardDirection2;

    private String [][][] defaultBreadboard = {
            {//top
                {"__","__","__"},
                {"__","__","__"},
                {"__","__","__"}
            },
            {//middle
                {"Bw","__","Bl"},
                {"Bs","__","Bl"},
                {"Bw","__","Bl"}
            },
            {//bottom
                {"Bw","Bw","Bw"},
                {"__","__","Bw"},
                {"Bw","Bw","Bw"}
            }
    };
    private Direction [][][] defaultBreadboardDirection = {
            {
                {dN,dN,dN},
                {dN,dN,dN},
                {dN,dN,dN}
            },
            {
                {dR,dN,dN},
                {dN,dN,dN},
                {dR,dN,dN}
            },
            {
                {dR,dR,dD},
                {dN,dN,dN},
                {dR,dR,dU}
            }
    };
    private Direction [][][] defaultBreadboardDirection2 = {
        {
            {dN,dN,dN},
            {dN,dN,dN},
            {dN,dN,dN}
        },
        {
            {dN,dN,dN},
            {dN,dN,dN},
            {dN,dN,dN}
        },
        {
            {dN,dN,dN},
            {dN,dN,dN},
            {dN,dN,dN}
        }
    };

    public Direction convertToDirection(final String s){
        if(s.equals("r")){
            return dR;
        }else if(s.equals("l")){
            return dL;
        }else if(s.equals("u")){
            return dU;
        }else if(s.equals("d")){
            return dD;
        }else if(s.equals("n") || s.equals(EMPTY_SYMBOL)){
            return dN;
        }
        return null;
    }

    public int convertToItemEnumOrdinal(final String s) {
        String sym = s.trim();
        if (sym.equals(EMPTY_SYMBOL)) {
            return 0;
        } else if (sym.equals(SWITCH_OFF_SYMBOL) || sym.equals(SWITCH_ON_SYMBOL)) {
            return 1;
        } else if (sym.equals(WIRE_OFF_SYMBOL) || sym.equals(WIRE_ON_SYMBOL)) {
            return 2;
        } else if (sym.equals(CROSSWIRE_SYMBOL)) {
            return 3;
        } else if (sym.equals(NOT_SYMBOL)) {
            return 4;
        } else if (sym.equals(OR_SYMBOL)) {
            return 5;
        } else if (sym.equals(AND_SYMBOL)) {
            return 6;
        } else if (sym.equals(LED_OFF_SYMBOL) || sym.equals(LED_ON_SYMBOL)) {
            return 7;
        } else if (sym.equals(RESISTOR_1_SYMBOL)) {
            return 8;
        } else if (sym.equals(RESISTOR_3_SYMBOL)) {
            return 9;
        }  else {
            return -1;
        }
    }

    /**
     *
     * @param ts tile string
     * @param d direction1
     * @param d2 direction2
     * @param x
     * @param y
     * @return proper BreadBoardItemm
     */
    private BreadBoardItem convertToType(final String ts, final Direction d, final Direction d2, int x, final int y, final int z){
        if (ts.equals(SWITCH_OFF_SYMBOL)) {
            return new Switch(false, d, x, y, z);
        } else if (ts.equals(SWITCH_ON_SYMBOL)) {
            return new Switch(true, d, x, y, z);
        } else if (ts.equals(WIRE_OFF_SYMBOL) || ts.equals(WIRE_ON_SYMBOL)) {
            return new Wire(d, x, y, z);
        } else if (ts.equals(CROSSWIRE_SYMBOL)) {
            return new DoubleWire(d, d2, x, y, z);
        } else if (ts.equals(NOT_SYMBOL)) {
            return new Not(d, x, y, z);
        } else if (ts.equals(AND_SYMBOL)) {
            return new And(d, x, y, z);
        } else if (ts.equals(OR_SYMBOL)) {
            return new Or(d, x, y, z);
        } else if (ts.equals(LED_ON_SYMBOL)) {
            return new LED(true, d, x, y, z);
        } else if (ts.equals(LED_OFF_SYMBOL)) {
            return new LED(false, d, x, y, z);
// Resistors
        } else if (ts.equals(RESISTOR_1_SYMBOL)) {
            return new Resistor1(d, x, y, z);
        } else if (ts.equals(RESISTOR_3_SYMBOL)) {
            return new Resistor3(d, x, y, z);
        } else if (ts.equals(RESISTOR_5_SYMBOL)) {
            return new Resistor5(d, x, y, z);
        } else if (ts.equals(RESISTOR_10_SYMBOL)) {
            return new Resistor10(d, x, y, z);

        } else if (ts.equals(EMPTY_SYMBOL)) {
            return null;
        } else {
            System.out.println("Unknown tile: " + breadboard[z][y][x] + " at " + x + "," + y);
            return null;
        }
    }

    /**
     * Returns the index of the current item if it matches the coordinates
     * otherwise -1
     * @param x position
     * @param y position
     * @return index the item matches, or -1
     */
    public int getBreadBoardItemIndexAtCoordinates(final int x, final int y, final int z) {
        for (int index = 0; index < breadBoardItemsList.size(); index++) {
            if (breadBoardItemsList.get(index).getX() == x && breadBoardItemsList.get(index).getY() == y && breadBoardItemsList.get(index).getZ() == z) {
                //System.out.println("got index " + index);
                return index;
            }
        }
        return -1;
    }

    /**
     * Returns the index of the current item if it matches the coordinates
     * otherwise -1
     * @param x position
     * @param y position
     * @return index the item matches, or -1
     */
    private int GetClickableBreadBoardItemAtCoordinates(final int x, final int y, final int z) {
        for (int index = 0; index < cBreadBoardItemsList.size(); index++) {
            if (cBreadBoardItemsList.get(index).getX() == x
                    && cBreadBoardItemsList.get(index).getY() == y
                    && cBreadBoardItemsList.get(index).getZ() == z) {
                System.out.println("found clickable at " + index);
                return index;
            }
        }
        return -1;
    }

    public boolean changeBreadBoard(final int type, final Direction dir1,
                                    final Direction dir2, final int x, final int y, final int z){

        if(x < 0 || y < 0 || y > breadboard[z].length || x > breadboard[z][y].length){
            return false;
        }

        if(type == -1) {//any type, changes every click
            //first remove the existing object
            int theOne = 0;
            if (getBreadBoardItemIndexAtCoordinates(x, y, z) != -1) {
                int index = getBreadBoardItemIndexAtCoordinates(x, y, z);
                breadBoardItemsList.remove(index);
                theOne = index;
            }
            if (GetClickableBreadBoardItemAtCoordinates(x, y, z) != -1) {
                int index = GetClickableBreadBoardItemAtCoordinates(x, y, z);
                cBreadBoardItemsList.remove(index);
            }

            for (int index = 0; index < itemEnum.length; index++) {
                if (breadboard[z][y][x].toUpperCase().equals(itemEnum[index])
                        || breadboard[z][y][x].toLowerCase().equals(itemEnum[index])) {
                    if (index + 1 >= itemEnum.length) {
                        index = -1;
                    } else {
                        breadBoardItemsList.add(theOne,
                                convertToType(itemEnum[index + 1], dN,dN, x, y, z));
                    }
                    breadboardDirection[z][y][x] = dN;//call this before cuz of repaint in next fn
                    setBreadBoardTile(itemEnum[index + 1], x, y, z);
                    return true;
                }
            }
        }else if(type == 0) {//empty
            //first remove the existing object if it's not a wire
            int theOne = 0;
            if (getBreadBoardItemIndexAtCoordinates(x, y, z) != -1
                    && !breadboard[z][y][x].toLowerCase().equals(itemEnum[type])) {
                int index = getBreadBoardItemIndexAtCoordinates(x, y, z);
                breadBoardItemsList.remove(index);
                theOne = index;
            }
            breadboardDirection[z][y][x] = dN;//call this before cuz of repaint in next fn
            breadboardDirection2[z][y][x] = dN;//call this before cuz of repaint in next fn
            setBreadBoardTile(itemEnum[type], x, y, z);
        }else {//anything else
            //first remove the existing object if it's not a wire
            int theOne = 0;
            if (getBreadBoardItemIndexAtCoordinates(x, y, z) != -1
            && !breadboard[z][y][x].toLowerCase().equals(itemEnum[type])) {
                int index = getBreadBoardItemIndexAtCoordinates(x, y, z);
                breadBoardItemsList.remove(index);
                theOne = index;
            }
            breadBoardItemsList.add(theOne,
                    convertToType(itemEnum[type], dir1,dir2, x, y, z));
            //System.out.println("changeBreadBoard(): breadBoardItemesList has been added to");
            breadboardDirection[z][y][x] = dir1;//call this before cuz of repaint in next fn
            breadboardDirection2[z][y][x] = dir2;
            setBreadBoardTile(itemEnum[type], x, y, z);
        }
        return true;//fix later for other cases
    }

    /**
     * Sets all the arrays, preferably from BreadBoardFileLoader.java
     * @param tiles
     * @param dir1
     * @param dir2
     */
    public void setBreadBoardState(String[][][] tiles, Direction[][][] dir1, Direction[][][] dir2) {
        for (int z = 0; z < ZHEIGHT; z++) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    String tile = tiles[z][y][x];
                    Direction d1 = dir1[z][y][x];
                    Direction d2 = dir2[z][y][x];

                    setBreadBoardTile(tile, x, y, z);
                    setBreadBoardDirectionTile(d1, x, y, z);
                    setBreadBoardDirection2Tile(d2, x, y, z);

                    int type = convertToItemEnumOrdinal(tile);
                    if (type != -1) {
                        changeBreadBoard(type, d1, d2, x, y, z);
                    } else {
                        String raw = tiles[z][y][x];
                        System.out.printf("weoa: raw='%s' tile='%s' at (%d,%d,%d)%n",
                                raw, tile, x, y, z);
                        System.out.println("length = " + raw.length() + " char codes: " + Arrays.toString(raw.toCharArray()));
                    }
                }
            }
        }
    }


    public BreadBoard(final int width, final int height, final int zHeight) {
        WIDTH = width;
        HEIGHT = height;
        ZHEIGHT = zHeight;
        System.out.println("WIDTH " + WIDTH + ", HEIGHT " + HEIGHT + ", ZHEIGHT " + ZHEIGHT);
        TOP_Z = zHeight - 1;
        BOTTOM_Z = 0;
        if(WIDTH != Main.DEFAULT_SCREEN_SIZE && HEIGHT != Main.DEFAULT_SCREEN_SIZE && ZHEIGHT != Main.DEFAULT_SCREEN_SIZE){
            breadboard = new String[zHeight][height][width];
            breadboardDirection = new Direction[zHeight][height][width];
            breadboardDirection2 = new Direction[zHeight][height][width];
            for (int i = 0; i < zHeight; i++) {
                for (int j = 0; j < height; j++) {
                    for (int k = 0; k < width; k++) {

                        breadboard[i][j][k] = EMPTY_SYMBOL;
                        breadboardDirection[i][j][k] = dN;
                        breadboardDirection2[i][j][k] = dN;

                        if (i < defaultBreadboard.length && j < defaultBreadboard[i].length && k < defaultBreadboard[i][j].length) {
                            breadboard[i][j][k] = defaultBreadboard[i][j][k];
                            breadboardDirection[i][j][k] = defaultBreadboardDirection[i][j][k];
                            breadboardDirection2[i][j][k] = defaultBreadboardDirection2[i][j][k];
                        }
                    }
                }
            }
        }else{
            breadboard = defaultBreadboard;
            breadboardDirection = defaultBreadboardDirection;
            breadboardDirection2 = defaultBreadboardDirection2;
        }

        setSignalArrayToNull();

        //setTilesInMain(breadboard);
        for (int i = 0; i < breadboard.length; i++){
            for (int j = 0; j < breadboard[i].length; j++) {
                for (int k = 0; k < breadboard[i][j].length; k++) {
                    Direction d = breadboardDirection[i][j][k];
                    Direction d2 = breadboardDirection2[i][j][k];
                    switch (breadboard[i][j][k]) {
                        case "BB":
                            breadBoardItemsList.add(new Button(true, d, k, j, i));
                            break;
                        case "Bb":
                            breadBoardItemsList.add(new Button(false, d, k, j, i));
                            break;
                        case "BS":
                            breadBoardItemsList.add(new Switch(true, d, k, j, i));
                            break;
                        case "Bs":
                            breadBoardItemsList.add(new Switch(false, d, k, j, i));
                            break;
                        case "BL":
                            breadBoardItemsList.add(new LED(false, d, k, j, i));
                            setBreadBoardTile("Bl", k, j, i);//no LED should be on by default
                            break;
                        case "Bl":
                            breadBoardItemsList.add(new LED(false, d, k, j, i));
                            break;
                        case "Bw":
                            breadBoardItemsList.add(new Wire(d, k, j, i));
                            System.out.println("Placed wire at " + k + "," + j + "," + i);
                            break;
                        case "BA":
                            breadBoardItemsList.add(new And(d, k, j, i));
                            break;
                        case "BN":
                            breadBoardItemsList.add(new Not(d, k, j, i));
                            break;
                        case "BO":
                            breadBoardItemsList.add(new Or(d, k, j, i));
                            break;
                        case "BW":
                            breadBoardItemsList.add(new Wire(d, k, j, i));
                            setBreadBoardTile("Bw",k, j, i);//no wire should be on by default
                            break;
                        case "BX":
                            breadBoardItemsList.add(new DoubleWire(d, d2, k, j, i));
                            break;
                        case "R1":
                            breadBoardItemsList.add(new Resistor1(d, k, j, i));
                            break;
                        case "R3":
                            breadBoardItemsList.add(new Resistor3(d, k, j, i));
                            break;
                        case "R5":
                            breadBoardItemsList.add(new Resistor5(d, k, j, i));
                            break;
                        case "R10":
                            breadBoardItemsList.add(new Resistor10(d, k, j, i));
                            break;
                        case "__":
                            break;
                        default:
                            System.out.println("ERROR LOADING TILES IN BREADBOARD CONSTRUCTOR!");
                            break;
                    }
                }
            }
        }
        //setTilesInMain();
    }

//    private void setTilesInMain(final String[][] bb) {
//        Main.setTiles(WIDTH, bb);
//    }

    /**
     * Checks clicks and calls update breadboard
     * two different modes; first:
     * checks the click within the boundary of a tile within the Item list
     * @param x
     * @param y
     * @return whether the program should repaint or not
     */
    public boolean checkClick(final MouseEvent e, int x, final int y, final int layer) {
        //System.out.println("checkClick at " + x + ", " + y);
        if(gamemode.equals(DEFAULT_KEYWORD))
        {
            for(CBreadBoardItem cbi:cBreadBoardItemsList)
            {
                if(x >= cbi.getX() * MyGameScreen.tileWidth &&
                        x < cbi.getX() * MyGameScreen.tileWidth + MyGameScreen.tileWidth
                        && y >= cbi.getY() * MyGameScreen.tileHeight &&
                        y < cbi.getY() * MyGameScreen.tileHeight + MyGameScreen.tileHeight)
                {
                    cbi.set(true);//true is just a necessary thing, not actually used
                    setBreadBoardTile(cbi.returnTile(),cbi.getX(),cbi.getY(), cbi.getZ());
                    return true;
                }
            }
        }else if (gamemode.equals(EDITING_KEYWORD))
        {
            if (!Main.getCopying()) {
                //System.out.println("mouse click:" + e.getButton());
                int tile_x = x / MyGameScreen.tileWidth;
                int tile_y = y / MyGameScreen.tileHeight;

                if (tile_x >= 0 && tile_x < MyGameScreen.xPixels
                        && tile_y >= 0 && tile_y < MyGameScreen.yPixels)
                {
                    if (e.getButton() == MouseEvent.BUTTON3)
                    {//right click
                        return changeBreadBoard(EMPTY_TYPE, dN, dN, tile_x, tile_y, layer);//0 is empty space
                    } else if (e.getButton() == MouseEvent.BUTTON1)
                    {
                        return changeBreadBoard(itemCursor, dN, dN, tile_x, tile_y, layer);
                    }else if (e.getButton() == MouseEvent.NOBUTTON)
                    { // drag ======= MAY HAVE TO CHANGE ========
                        if(Main.mouseClickNumber[0] == MouseEvent.BUTTON1)
                        {//drag left click
                            return changeBreadBoard(itemCursor, dN, dN, tile_x, tile_y, layer);
                        }else if (Main.mouseClickNumber[0] == MouseEvent.BUTTON3)
                        {//drag left click
                            return changeBreadBoard(EMPTY_TYPE, dN, dN, tile_x, tile_y, layer);
                        }
                    }
                } else
                {
                    return false;
                }
            }
        }else if (gamemode.equals(COPYING_KEYWORD) || gamemode.equals(CUTTING_KEYWORD))
        {//copying and pasting
            int sX = (int)(Main.mouseX[0] - Main.SCREEN_X_OFFSET) / MyGameScreen.tileWidth;
            if(sX < 0) sX = 0;

            int sY = (int)(Main.mouseY[0] - Main.SCREEN_Y_OFFSET) / MyGameScreen.tileHeight;
            if(sY < 0) sY = 0;

            int eX = (int)(Main.mouseX[1] - Main.SCREEN_X_OFFSET) / MyGameScreen.tileWidth;
            if(eX > MyGameScreen.xPixels) eX = MyGameScreen.xPixels;

            int eY = (int)(Main.mouseY[1] - Main.SCREEN_Y_OFFSET) / MyGameScreen.tileHeight;
            if(eY > MyGameScreen.yPixels) eY = MyGameScreen.yPixels;

            selectEntities(sX,sY,BOTTOM_Z,eX,eY,TOP_Z);

            if(gamemode.equals(CUTTING_KEYWORD)) {

            }

        }

        return false;
    }

    /**
     * Selects entities and tiles within a given space
     * @param sX
     * @param sY
     * @param eX
     * @param eY
     */
    private void selectEntities(final int sX, final int sY, final int sZ, final int eX, final int eY, final int eZ)
    {
        //for now hardset sZ and eZ to the bottom and top of the world

        if((sX > eX) || (sY > eY) || (sZ > eZ))
        {
            System.out.println("ERROR SELECTING ENTITIES; DON'T DRAG IN REVERSE");
            //could possibly change this in the future
            return;
        }else if(sX == eX || sY == eY || sZ == eZ)
        {//one of the lengths is zero
            return;
        }


        int dX = eX - sX;
        int dY = eY - sY;
        int dZ = eZ - sZ;

        String[][][] tempCutCopyPasteBoard = new String[dZ][dY][dX];
        Direction[][][] tempCutCopyPasteBoardDir1 = new Direction[dZ][dY][dX];
        Direction[][][] tempCutCopyPasteBoardDir2 = new Direction[dZ][dY][dX];

//        System.out.println("temp board at: "
//                + sX
//                + ", " + sY
//                + ", " + eX
//                + ", " + eY
//                + " with dimensions: "
//                + tempCutCopyPasteBoard[0].length
//                + ", "
//                + tempCutCopyPasteBoard.length);

        Main.getMyGameScreen().tempCutCopyPasteBoardList.clear();
        Main.getMyGameScreen().tempCutCopyPasteBoardDirection1List.clear();
        Main.getMyGameScreen().tempCutCopyPasteBoardDirection2List.clear();
        //set the board to the actual breadboard pieces
        for(int i = 0; i < dZ; i++) {
            for (int j = 0; j < dY; j++) {
                for (int k = 0; k < dX; k++) {
                    tempCutCopyPasteBoard[i][j][k] = breadboard[i + sZ][j + sY][k + sX];
                    tempCutCopyPasteBoardDir1[i][j][k] = breadboardDirection[i + sZ][j + sY][k + sX];
                    tempCutCopyPasteBoardDir2[i][j][k] = breadboardDirection2[i + sZ][j + sY][k + sX];

//                    //add the section of the board arrays to the board lists in mgs
//                    Main.getMyGameScreen().tempCutCopyPasteBoardList.add(
//                            new ArrayList<>(new ArrayList<>(Arrays.asList(tempCutCopyPasteBoard[i][j][k]))));
//                    Main.getMyGameScreen().tempCutCopyPasteBoardDirection1List.add(
//                            new ArrayList<>(Arrays.asList(tempCutCopyPasteBoardDir1[i][j][k])));
//                    Main.getMyGameScreen().tempCutCopyPasteBoardDirection2List.add(
//                            new ArrayList<>(Arrays.asList(tempCutCopyPasteBoardDir2[i][j][k])));

                    Main.getMyGameScreen().tempCutCopyPasteBoard[i][j][k] = tempCutCopyPasteBoard[i][j][k];
                    Main.getMyGameScreen().tempCutCopyPasteBoardDirection1[i][j][k] = tempCutCopyPasteBoardDir1[i][j][k];
                    Main.getMyGameScreen().tempCutCopyPasteBoardDirection2[i][j][k] = tempCutCopyPasteBoardDir2[i][j][k];

                }
                //System.out.println(Main.getMyGameScreen().tempCutCopyPasteBoardList.get(i));
            }
        }

        //System.out.println(convertTilesIntoArrayString(tempCutCopyPasteBoard, "", dX, dY));

        //System.out.println(convertTilesIntoArrayString(Main.getMyGameScreen().tempCutCopyPasteBoardList, "", dX, dY));

    }

    /**
     * Sets everything within the region to empty tile
     * @param sX starting x
     * @param sY starting y
     * @param eX ending x
     * @param eY ending y
     */
    public void eraseRegion(int sX, int sY, int sZ, int eX, int eY, int eZ) {
        //System.out.println("called erase region");
        for (int z = sZ; z < eZ; z++) {
            for (int y = sY; y < eY; y++) {
                for (int x = sX; x < eX; x++) {
                    changeBreadBoard(EMPTY_TYPE, Direction.NONE, Direction.NONE, x, y, z); // sets to EMPTY
                }
            }
        }
    }

    /**
     * Selects entity and tile and adds it to a temp array
     * @param x
     * @param y
     */
    private BreadBoardItem selectEntity(final int x, final int y, final int z)
    {
        if (getBreadBoardItemIndexAtCoordinates(x, y, z) != -1) {
            int index = getBreadBoardItemIndexAtCoordinates(x, y, z);
            return breadBoardItemsList.get(index);
        }else {
            return null;
        }
    }

    /**
     * Rotates an item on the breadboard; only sif in editing mode.
     * for now just rotates 90 deg to the right
     */
    public void rotateItem(final int layer, final int x, final int y, final int z) {
        if(gamemode.equals(EDITING_KEYWORD)) {
            if(layer == 0) {//rotate for everything
                int num = breadboardDirection[z][y][x].ordinal() + 1;//rotate by adding one to the ordinal
                if (num >= Direction.values().length) num = 0;//reset to first one if > length
                breadboardDirection[z][y][x] = Direction.values()[num];
                if (getBreadBoardItemIndexAtCoordinates(x, y, z) != -1) {
                    int index = getBreadBoardItemIndexAtCoordinates(x, y , z);
                    breadBoardItemsList.get(index).setDir(Direction.values()[num]);
                }

                callPaint();
            }else {//rotate the second output of DoubleWire
                if (getBreadBoardItemIndexAtCoordinates(x, y, z) != -1) {

                    if (locateBreadBoardItemOnBoard(x,y, z).returnTile().equals(CROSSWIRE_SYMBOL)) {

                        DoubleWire dw = (DoubleWire) locateBreadBoardItemOnBoard(x,y, z);
                        int dirNum = dw.getDir2().ordinal() + 1;
                        if (dirNum >= Direction.values().length) dirNum = 0;
                        breadboardDirection2[z][y][x] = Direction.values()[dirNum];
                        //to do: implement safety measure so that user cannot make d2 the opposite of d1
                        dw.setDir2(Direction.values()[dirNum]);

                    }
                    callPaint();
                }
            }
        }
    }

    /**
     * Ticks
     */
    public void tick(final int tickNo) {
        int i = 0;
        Object[][] signalArrayOnCall = new Object[signalArray.length][];
        for (int j = 0; j < signalArray.length; j++) {
            if (signalArray[j] != null) {
                signalArrayOnCall[j] = Arrays.copyOf(signalArray[j], signalArray[j].length);
            }
        }
        //System.out.println("tick(): signal at tick " + Arrays.deepToString(signalArrayOnCall));
        while (i < signalArrayOnCall.length) {
            if (signalArrayOnCall[i] != null) {
                Direction d = (Direction) signalArrayOnCall[i][SIGNAL_ARRAY_D_PLACE];
                boolean s = (boolean) signalArrayOnCall[i][SIGNAL_ARRAY_S_PLACE];
                int x = (int) signalArrayOnCall[i][SIGNAL_ARRAY_X_PLACE];
                int y = (int) signalArrayOnCall[i][SIGNAL_ARRAY_Y_PLACE];
                int z = (int) signalArrayOnCall[i][SIGNAL_ARRAY_Z_PLACE];
                /**
                 * Tick that the signal is supposed to be propagated
                 */
                int tickToBe = (int)signalArrayOnCall[i][SIGNAL_ARRAY_TICK_PLACE];
//                System.out.println(
//                        "tick(): gonna propagate " + i +
//                        "th signal from " +
//                        x + " " + y + " " + z +
//                        " on tick" + tickToBe);
                //System.out.println("tick(): signal before calling pS() " + Arrays.deepToString(signalArrayOnCall));
                //System.out.println("tick(): gonna call pS() with the " + i + "th signal");
                propagateSignal(d, s, x, y, z, tickToBe);
                //System.out.println("tick(): called propogateSignal with the " + i + "th signal");

            }
            i++;
        }
        //cleanEmptyOrOldSignalsAtBeginningOfSignalArray(tickNo);
    }

    /**
     * Calls Mains getMyFrame's object's repaint.
     */
    private void callPaint(){
        Main.getMyFrame().repaint();
    }


    /**
     * Updates the breadBoard array by
     * setting the array cell at x and y to given tile string
     * calls main.setTiles
     * then repaints //commented out
     * @param tile
     * @param x
     * @param y
     */
    public void setBreadBoardTile(final String tile, final int x, final int y, final int z){
        breadboard[z][y][x] = tile;
        //Main.setTiles(SIZE, breadboard);
        //callPaint();
    }

    /**
     * Updates the breadBoardDirection array by
     * setting the array cell direction at x and y to given direction
     * calls main.setTiles //defunct
     * then repaints
     * @param x
     * @param y
     */
    public void setBreadBoardDirectionTile(final Direction dir, final int x, final int y, final int z){
        breadboardDirection[z][y][x] = dir;
        //Main.setTiles(SIZE, breadboardDirection); defunct
        //callPaint();
    }

    /**
     * Updates the breadBoardDirection2 array by
     * setting the array cell direction at x and y to given direction
     * calls main.setTiles //defunct
     * then repaints
     * @param x
     * @param y
     */
    public void setBreadBoardDirection2Tile(final Direction dir, final int x, final int y, final int z){
        breadboardDirection2[z][y][x] = dir;
        //Main.setTiles(SIZE, breadboardDirection); defunct
        //callPaint();
    }


    private void setWiresAndLeds(final boolean s, final int dx, final int dy, final int dz, final int x, final int y, final int z, final int t) {
        //System.out.println("called setWiresAndLeds going with dx " + dx + " dy " + dy + " dz " + dz + " going to x " + x + " y " + y + " z " + z);
        String tile = breadboard[z][y][x];

        if (s) {
            if (tile.equals(WIRE_OFF_SYMBOL)) {
                BreadBoardItem item = locateBreadBoardItemOnBoard(x, y, z);
                if (item instanceof Wire wire) {
                    wire.setOut(dx, dy, dz, true, t);
                } else {

                    System.out.println("Expected Wire at " + x + "," + y + "," + z + " but got null or different type.");
                }
            }else if (tile.equals(LED_OFF_SYMBOL)) {
                LED led = (LED) locateBreadBoardItemOnBoard(x, y, z);
                assert led != null;
                led.setOut(true,t);
            }else if(tile.equals(RESISTOR_1_SYMBOL)) {
                Resistor1 r1 = (Resistor1) locateBreadBoardItemOnBoard(x, y, z);
                assert r1 != null;
                r1.setOut(dx,dy,true, t);
            }else if(tile.equals(RESISTOR_3_SYMBOL)) {
                Resistor3 r3 = (Resistor3) locateBreadBoardItemOnBoard(x, y, z);
                assert r3 != null;
                r3.setOut(dx,dy,true, t);
            }else if(tile.equals(RESISTOR_5_SYMBOL)) {
                Resistor5 r5 = (Resistor5) locateBreadBoardItemOnBoard(x, y, z);
                assert r5 != null;
                r5.setOut(dx,dy,true, t);
            }else if(tile.equals(RESISTOR_10_SYMBOL)) {
                Resistor10 r10 = (Resistor10) locateBreadBoardItemOnBoard(x, y, z);
                assert r10 != null;
                r10.setOut(dx,dy,true, t);
            }
        } else {
            if (tile.equals(WIRE_ON_SYMBOL)) {
                Wire wire = (Wire) locateBreadBoardItemOnBoard(x, y, z);
                assert wire != null;
                wire.setOut(dx, dy,dz,false, t);
            }else if (tile.equals(LED_ON_SYMBOL)) {
                LED led = (LED) locateBreadBoardItemOnBoard(x, y, z);
                assert led != null;
                led.setOut(false,t);
            }else if(tile.equals(RESISTOR_1_SYMBOL)) {
                Resistor1 r1 = (Resistor1) locateBreadBoardItemOnBoard(x, y, z);
                assert r1 != null;
                r1.setOut(dx,dy,false, t);
            }else if(tile.equals(RESISTOR_3_SYMBOL)) {
                Resistor3 r3 = (Resistor3) locateBreadBoardItemOnBoard(x, y, z);
                assert r3 != null;
                r3.setOut(dx,dy,false, t);
            }else if(tile.equals(RESISTOR_5_SYMBOL)) {
                Resistor5 r5 = (Resistor5) locateBreadBoardItemOnBoard(x, y, z);
                assert r5 != null;
                r5.setOut(dx,dy,false, t);
            }else if(tile.equals(RESISTOR_10_SYMBOL)) {
                Resistor10 r10 = (Resistor10) locateBreadBoardItemOnBoard(x, y, z);
                assert r10 != null;
                r10.setOut(dx,dy,false, t);
            }
        }
    }


    /**
     * Sets gates and double wires
     * @param s signal
     * @param dx
     * @param dy
     * @param x
     * @param y
     */
    public void setGates(final boolean s, final int dx, final int dy, final int dz, final int x, final int y, final int z, final int t) {
        String sBR = breadboard[z + dz][y + dy][x + dx]; // adjacent tile

        if (sBR.equals(NOT_SYMBOL)) {
            Not not = (Not) locateBreadBoardItemOnBoard(x + dx, y + dy, z + dz);
            assert not != null;
            if (not.setRightGate(s, dx, dy, dz)) {
                not.calculate();
                not.signal(t);
            }
        } else if (sBR.equals(AND_SYMBOL)) {
            And and = (And) locateBreadBoardItemOnBoard(x + dx, y + dy,z + dz);
            assert and != null;
            if (and.setRightGate(s, dx, dy, dz)) {
                and.calculate();
                and.signal(t);
            }
        } else if (sBR.equals(OR_SYMBOL)) {
            Or or = (Or) locateBreadBoardItemOnBoard(x + dx, y + dy, z + dz);
            assert or != null;
            if (or.setRightGate(s, dx, dy, dz)) {
                or.calculate();
                or.signal(t);
            }
        } else if (sBR.equals(CROSSWIRE_SYMBOL)) {
            DoubleWire dw = (DoubleWire) locateBreadBoardItemOnBoard(x + dx, y + dy, z + dz);
            dw.setRightInput(s, dx, dy, t);
        }
    }

    /**
     * Just does the signal logic
     * @param d
     * @param s
     * @param x
     * @param y
     */
    private int propagateSignal(final Direction d, final boolean s, final int x, final int y, final int z, final int t) {
        if(t == Main.tickNumber) {
            //System.out.println("pS(): signal " + s + " from " + x + " " + y + " on tick " + t);
            if (d == Direction.NONE) {
                for (int i = z - 1; i <= z + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        for (int k = x - 1; k <= x + 1; k++) {
                            //might have to change to <= ZHEIGHT
                            if (i >= 0 && j >= 0 && k >= 0 && i < ZHEIGHT && j < HEIGHT && k < WIDTH &&
                                    //check for corner cases
                                    !(j == y - 1 && k == x - 1) &&
                                    !(j == y + 1 && k == x - 1) &&
                                    !(j == y - 1 && k == x + 1) &&
                                    !(j == y + 1 && k == x + 1)
                            ) {
                                if(i == z) {
                                    //check for centre case if i == z
                                    if(!(j == y && k == x)) {
                                        if (k == x + 1) {
                                            setGates(s, 1, 0, 0, x, y, z, t);
                                            setWiresAndLeds(s, 1, 0, 0, k, j, i, t);
                                        } else if (k == x - 1) {
                                            setGates(s, -1, 0, 0, x, y, z, t);
                                            setWiresAndLeds(s, -1, 0, 0, k, j, i, t);
                                        } else if (j == y + 1) {
                                            setGates(s, 0, 1, 0, x, y, z, t);
                                            setWiresAndLeds(s, 0, 1, 0, k, j, i, t);
                                        } else if (j == y - 1) {
                                            setGates(s, 0, -1, 0, x, y, z, t);
                                            setWiresAndLeds(s, 0, -1, 0, k, j, i, t);
                                        }
                                    }
                                }else {
                                    //the tile above or below the sending tile MUST be
                                    //exactly above or below it
                                    if(j == y && k == x) {
                                        //System.out.println("x " + x + " y " + y + " z " + z);
                                        //System.out.println("k " + k + " j " + j + " i " + i);
                                        setGates(s, k - x, j - y, i - z, x, y, z, t);
                                        setWiresAndLeds(s, k - x, j - y, i - z, k, j, i, t);
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (d == Direction.RIGHT && x + 1 < WIDTH) {
                setWiresAndLeds(s, 1, 0, 0,x + 1, y, z, t);
                setGates(s, 1, 0, 0, x, y, z, t);
            } else if (d == Direction.LEFT && x - 1 >= 0) {
                setWiresAndLeds(s, -1, 0, 0, x - 1, y, z, t);
                setGates(s, -1, 0, 0, x, y, z, t);
            } else if (d == Direction.DOWN && y + 1 < HEIGHT) {
                setWiresAndLeds(s, 0, 1, 0, x, y + 1, z, t);
                setGates(s, 0, 1, 0, x, y, z, t);
            } else if (d == Direction.UP && y - 1 >= 0) {
                setWiresAndLeds(s, 0, -1, 0, x, y - 1, z, t);
                setGates(s, 0, -1, 0, x, y, z, t);
            }
            return 0;//this saved me
        }else if(t < Main.tickNumber) {
            //System.out.println("pS(): not called; signal " + s + " from " + x + " " + y + " on tick " + t);
            return -1;
        }else {//greater than
            //System.out.println("pS(): not called; signal " + s + " from " + x + " " + y + " on tick " + t);
            return 1;//and this, it allows future ones to be held
        }
    }

//Defunct
//    private void cleanEmptyOrOldSignalsAtBeginningOfSignalArray(int currentTick) {
//        int writeIndex = 0;
//        for (int readIndex = 0; readIndex < signalArray.length; readIndex++) {
//            Object[] signal = signalArray[readIndex];
//            if (signal != null && (int) signal[4] >= currentTick) {
//                if (writeIndex != readIndex) {
//                    signalArray[writeIndex] = signal;
//                }
//                writeIndex++;
//            }
//        }
//        for (int i = writeIndex; i < signalArray.length; i++) {
//            signalArray[i] = null;
//        }
//    }

    /**
     * Queues a signal
     * @param d
     * @param s
     * @param x
     * @param y
     */
    public void signal(final Direction d, final boolean s, final int x, final int y, final int z, final int t) {
        //System.out.println("signal(): trying to add signal at " + x + " " + y + " " + z + " on tick " + t);

        if (t < Main.tickNumber || t > Main.tickNumber + MAX_TICKS_IN_THE_FUTURE) {
            System.out.println("signal(): tick out of range.");
            return;
        }

        // Step 1: Clean old/expired signals
        int writeIndex = 0;
        for (int i = 0; i < signalArray.length; i++) {
            if (signalArray[i] != null && (int) signalArray[i][SIGNAL_ARRAY_TICK_PLACE] >= Main.tickNumber) {
                signalArray[writeIndex++] = signalArray[i];
                //writeIndex = i + 1;
            }
        }

        // Step 2: Null out the rest
        for (int i = writeIndex; i < signalArray.length; i++) {
            signalArray[i] = null;
        }

        // Step 3: Insert new signal
        if (writeIndex < signalArray.length) {
            signalArray[writeIndex] = new Object[]{d, s, x, y, z, t};
//            System.out.println(
//                    "signal(): " +
//                    d + "," +
//                    s + "," +
//                    x + "," +
//                    y + "," +
//                    z + "," +
//                    t +
//                    " inserted at index " + writeIndex);
        } else {
            System.out.println("Signal queue overflow.");
        }

        Main.getMyGameScreen().repaint();
    }


    /**
     * Button, extends cBreadBoardItem
     */
    private class Button extends CBreadBoardItem {

        private boolean out  = false;

        public Button(final boolean out, final Direction dir, final int x, final int y, final int z) {
            super(dir,x,y,z);
            this.out = out;
        }

        public void set(boolean out) {
            this.out = out;
        }

        public String returnTile(){
            if(out){
                return TileString.ButtonOn.getSymbol();
            }else {
                return TileString.ButtonOff.getSymbol();
            }
        }

        public void signal(int t){
            //--
        }

    }

    /**
     * Switch, extends cBreadBoardItem
     */
    private class Switch extends CBreadBoardItem {

        private boolean out  = false;

        public Switch(boolean out, Direction dir, final int x, final int y, final int z) {
            super(dir,x,y,z);
            this.out = out;
        }

        public void set(final boolean b) {
            out = !out;
            this.signal(Main.tickNumber + 1);
        }

        public String returnTile() {
            if (out) {
                return SWITCH_ON_SYMBOL;
            } else {
                return SWITCH_OFF_SYMBOL;
            }
        }


        public boolean getOut(){
            return out;
        }

        public void signal(int t) {
//            if(Main.tick_true){
                BreadBoard.this.signal(this.getDir(),out, this.getX(), this.getY(), this.getZ(), t);
            //System.out.println("switch.signal(): at " + this.getX() + " " + this.getY() + " on tick" + Main.tickNumber);
//            }else {
//                putIntoSignalsArray(this.getDir(),out, this.getX(), this.getY());
//            }
        }

    }

    /**
     * Useful function to locate the corresponding list item from the breadBoardItemsList
     * @param x
     * @param y
     * @return BreadBoardItem bi
     */
    public BreadBoardItem locateBreadBoardItemOnBoard(final int x, final int y, final int z) {
        for(BreadBoardItem bi: breadBoardItemsList) {
            if(bi.getX() == x && bi.getY() == y && bi.getZ() == z) {
                return bi;
            }
        }
        return null;
    }

    /**
     * Abstract gate class
     */
    public abstract class Gate extends BreadBoardItem {

        boolean out = false;
        boolean A = false;
        boolean B = false;
        boolean C = false;

        public Gate(final Direction dir, final int x, final int y, final int z) {
            super(dir, x, y, z);
        }

        public abstract void setA(boolean on);
        public abstract void setB(boolean on);
        public abstract void setC(boolean on);


        public abstract void calculate();

        /**A is always 90deg to the left of output
         * B is always 180deg away from the output
         * C is always 90 deg to the right of output
         */
        public boolean setRightGate(final boolean s, final int deltax, final int deltay, final int deltaz){
            //this gate is to the right of the input (ex. wire)
            if(deltax == 1){
                if(this.getDir() == Direction.RIGHT) {
                    setB(s);
                    return true;
                } else if (this.getDir() == Direction.UP) {
                    setA(s);
                    return true;
                }else if (this.getDir() == Direction.DOWN) {
                    setC(s);
                    return true;
                }
                return false;
            }else if(deltax == -1){//this gate is to the left of the input (ex. wire)
                if(this.getDir() == Direction.LEFT) {
                    setB(s);
                    return true;
                } else if (this.getDir() == Direction.UP) {
                    setC(s);
                    return true;
                }else if (this.getDir() == Direction.DOWN) {
                    setA(s);
                    return true;
                }
                return false;
            }else if(deltay == 1){//this gate is below the input (ex. wire)
                if(this.getDir() == Direction.RIGHT) {
                    setA(s);
                    return true;
                } else if (this.getDir() == Direction.LEFT) {
                    setC(s);
                    return true;
                }else if (this.getDir() == Direction.DOWN) {
                    setB(s);
                    return true;
                }
                return false;
            }else if(deltay == -1){//this gate is above the input (ex. wire)
                if(this.getDir() == Direction.RIGHT) {
                    setC(s);
                    return true;
                } else if (this.getDir() == Direction.LEFT) {
                    setA(s);
                    return true;
                }else if (this.getDir() == Direction.UP) {
                    setB(s);
                    return true;
                }
                return false;
            }
            return false;
        }

        public void signal(final int tick_when_set){
//            System.out.println("Gate.signal(): going " +
//                            this.getDir() + ", " +
//                    out + ", at" +
//                    this.getX() + " " +
//                    this.getY() + " " +
//                    this.getZ() + " on tick " +
//                    (tick_when_set+1));
            BreadBoard.this.signal(
                    this.getDir(),
                    out,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    tick_when_set+1);
        }

        public boolean getOut(){
            return out;
        }
    }

    /**
     * And gate
     */
    private class And extends Gate {

        public And(final Direction dir, final int x, final int y, final int z) {
            super(dir, x, y, z);
        }

        public void setA(final boolean on) {
            this.A = on;
        }

        public void setB(final boolean on) {
            this.B = on;
        }

        public void setC(final boolean on) {
            this.C = on;
        }

        public void calculate(){
            if(A&&B || A&&C || B&&C){
                out = true;
            } else {
                out = false;
            }
        }

        public void signal(final int tick_when_set){
            //BreadBoard.this.signal(this.getDir(),out,getX(),getY(),tick_when_set+1);
            super.signal(tick_when_set);
        }

    }

    /**
     * Not gate
     */
    private class Not extends Gate {

        public Not(final Direction dir, final int x, final int y, final int z) {
            super(dir, x, y, z);
        }

        public void setA(final boolean on) {
            System.out.println("why you setting A in not!");
        }

        /**
         * B is always 180 deg from the output, so that is why
         * it is chosen in a not gate
         * @param on signal, true or false
         */
        @Override
        public void setB(boolean on) {
            this.B = on;
        }

        @Override
        public void setC(boolean on) {
            System.out.println("why you setting C in not!");
        }

        public void calculate(){
            this.out = !B;
        }

        public void signal(int tick_when_set) {
            super.signal(tick_when_set);
        }

    }

    /**
     * Or gate
     */
    private class Or extends Gate {

        public Or(final Direction dir, final int x, final int y, final int z) {
            super(dir, x, y, z);
        }

        public void setA(final boolean on) {
            this.A = on;
        }

        public void setB(final boolean on) {
            this.B = on;
        }

        public void setC(final boolean on) {
            this.C = on;
        }


        public void calculate(){
            if(A || B || C){
                out = true;
            } else {
                out = false;
            }
        }

        public String returnTile(){
            return OR_SYMBOL;
        }

        public void signal(int tick_when_set) {
            //BreadBoard.this.signal(this.getDir(),out,this.getX(),this.getY());
            super.signal(tick_when_set);
        }

    }

    /**
     * LED
     */
    private class LED extends BreadBoardItem {

        private boolean out = false;

        public LED(final boolean out, final Direction dir, final int x, final int y, final int z) {
            super(dir, x, y, z);
            this.out = out;
        }

        public void setOut(final boolean s, int t) {
            boolean[] signal = new boolean[4];
            int nx = getX();
            int ny = getY();
            int nz = getZ();
            if(s){
                this.out = true;
            } else {
                this.out = false;
                if(getBreadBoardItemIndexAtCoordinates(nx+1,ny, nz)!=-1){
                    //System.out.println((breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny)).getOut()));
                    if (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny, nz)).getOut()
                    && ((breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny, nz)).getDir() == dL)
                    || (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny, nz)).getDir() == dN))) {
                        this.out = true;

                    }
                }
                if(getBreadBoardItemIndexAtCoordinates(nx-1,ny, nz)!=-1){
                    if (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx-1, ny, nz)).getOut()
                            && ((breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx-1, ny, nz)).getDir() == dR)
                            || (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx-1, ny, nz)).getDir() == dN))) {
                        this.out = true;
                    }
                }
                if(getBreadBoardItemIndexAtCoordinates(nx,ny-1, nz)!=-1){
                    if (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny-1, nz)).getOut()
                            && (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny-1, nz)).getDir() == dD
                            || breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny-1, nz)).getDir() == dN)) {
                        this.out = true;
                    }
                }
                if(getBreadBoardItemIndexAtCoordinates(nx,ny+1, nz)!=-1){
                    if (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny+1, nz)).getOut()
                            && (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny+1, nz)).getDir() == dU
                            || breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny+1, nz)).getDir() == dN)) {
                        this.out = true;
                    }
                }
//
            }
            signal(t);

        }

        public void signal(int tick_when_set) {
            if (this.out) {
                setBreadBoardTile(LED_ON_SYMBOL, getX(), getY(), getZ());
            } else {
                setBreadBoardTile(LED_OFF_SYMBOL, getX(), getY(), getZ());
            }
            Main.getMyFrame().repaint();
        }

        private void delay() {
            //delay
        }

        public String returnTile(){
            if(out){
                return LED_ON_SYMBOL;
            }else {
                return LED_OFF_SYMBOL;
            }
        }
    }

    /**
     * A new class for wire, which acts as a cross between two wires,
     * setting one side will set the opposite side on (say South side to North side)
     * and it will be independent of the other side.
     */
    public class DoubleWire extends Wire {

        private Direction dir2;

        public DoubleWire(final Direction dir1, final Direction dir2, final int x, final int y, final int z) {
            super(dir1, x, y, z);
            this.dir2 = dir2;
        }

        public void setDir2(final Direction dir2) {
            this.dir2 = dir2;
        }

        public void setRightInput(final boolean s, final int deltax, final int deltay, final int t){
            //this doubleWire is to the right of the input (ex. wire)
            if(deltax == 1){
                if(this.getDir() == Direction.RIGHT || this.getDir2() == Direction.RIGHT) {
                    signal(dR, s, t);
                }
            }else if(deltax == -1){//this gate is to the left of the input (ex. wire)
                if(this.getDir() == dL || this.getDir2() == dL) {
                    signal(dL, s, t);
                }
            }else if(deltay == 1){//this gate is below the input (ex. wire)
                if(this.getDir() == dD || this.getDir2() == dD) {
                    signal(dD, s, t);
                }
            }else if(deltay == -1){//this gate is above the input (ex. wire)
                if(this.getDir() == dU || this.getDir2() == dU) {
                    signal(dU, s,t);
                }
            }
        }

        /**
         * Like the signal in Wire, searches for other board members,
         * but we can use it for both directions
         * @param d direction paramater, this reduces having to look up
         *          either dir1 or dir2
         * @param s signal: true or false
         */
        public void signal(final Direction d, final boolean s, final int t) {
            //BreadBoard.this.signal(d,s,this.getX(),this.getY());
            BreadBoard.this.signal(d, s, this.getX(),this.getY(), this.getZ(), t+1);
        }

        public Direction getDir2(){
            return dir2;
        }

        public String returnTile(){
            return CROSSWIRE_SYMBOL;
        }

    }

    /**
     * Wire
     */
    public class Wire extends BreadBoardItem {

        private boolean out = false;

        public Wire(final Direction dir, final int x, final int y, final int z) {
            super(dir, x, y, z);
        }

        public void setOut(final int dx, final int dy, final int dz, final boolean out, final int t) {
            this.out = out;
            //check first if this signal is coming from the right input direction
            //ie if this wire is facing right, then this should not be taking input
            //from an object to the right of it, thus possibly creating an infinite loop
            //or an insane amount of signals; much havoc was caused because of this on May 29, 2025
            if((this.getDir() == dL && dx != 1) ||
            (this.getDir() == dR && dx != -1) ||
            (this.getDir() == dU && dy != 1) ||
            (this.getDir() == dD && dy != -1) ||
            this.getDir() == dN) {
                if (out) {
                    setBreadBoardTile(WIRE_ON_SYMBOL, getX(), getY(), getZ());
                } else {
                    setBreadBoardTile(WIRE_OFF_SYMBOL, getX(), getY(), getZ());
                }
                Main.getMyGameScreen().repaint();
                this.signal(t);
            }
        }

        public boolean getOut(){
            return out;
        }

        public String returnTile(){
            if(out){
                return WIRE_ON_SYMBOL;
            }else {
                return WIRE_OFF_SYMBOL;
            }
        }

        /**
         * Like the signal in Switch, searches for other board members.
         */
        public void signal(final int t) {
            //if(Main.tick_true) {
                BreadBoard.this.signal(this.getDir(), out, this.getX(), this.getY(), this.getZ(), t + 1);
                //System.out.println("wire.signal(): at " + this.getX() + " " + this.getY()
                //+ " which will be called on tick " + (t + 1));
            //}
        }

    }

    public abstract class Resistor extends BreadBoardItem {
        protected int delayTicks;
        protected boolean out = false;

        public Resistor(Direction dir, int delayTicks, int x, int y, final int z) {
            super(dir, x, y, z);
            this.delayTicks = delayTicks;
        }

        public void setOut(final int dx, final int dy, final boolean out, final int t) {
            this.out = out;
            //check first if this signal is coming from the right input direction
            //ie if this wire is facing right, then this should not be taking input
            //from an object to the right of it, thus possibly creating an infinite loop
            //or an insane amount of signals; much havoc was caused because of this on May 29, 2025
            if((this.getDir() == dL && dx != 1) ||
                    (this.getDir() == dR && dx != -1) ||
                    (this.getDir() == dU && dy != 1) ||
                    (this.getDir() == dD && dy != -1) ||
                    this.getDir() == dN) {
                Main.getMyGameScreen().repaint();
                this.signal(t);
            }

        }

        /**
         * Like the signal in Switch, searches for other board members.
         */
        @Override
        public void signal(final int t) {
            System.out.println("Resistor.signal() delayTicks " + delayTicks);
            BreadBoard.this.signal(this.getDir(), out, this.getX(), this.getY(), this.getZ(),t + 1 + delayTicks);
        }

        public int getDelayTicks() {
            return delayTicks;
        }
        public abstract String returnTile();
    }

    public class Resistor1 extends Resistor {
        public Resistor1(Direction dir, int x, int y, int z) {
            super(dir, 1, x, y, z);
        }

        @Override
        public void setOut(final int dx, final int dy, final boolean out, final int t){
            super.setOut(dx, dy, out, t);
            //setBreadBoardTile(RESISTOR_1_SYMBOL, getX(), getY());
        }

//        /**
//         * Like the signal in Switch, searches for other board members.
//         */
//        @Override
//        public void signal(final int t) {
//            System.out.println("Resistor1.signal() delayTicks " + delayTicks);
//            BreadBoard.this.signal(this.getDir(), out, this.getX(), this.getY(), t + 1 + delayTicks);
//        }

        @Override
        public String returnTile() {
            return RESISTOR_1_SYMBOL;
        }
    }

    public class Resistor3 extends Resistor {
        public Resistor3(Direction dir, int x, int y, int z) {
            super(dir, 3, x, y, z);
        }

        @Override
        public void setOut(final int dx, final int dy, final boolean out, final int t){
            super.setOut(dx, dy, out, t);
            //setBreadBoardTile(RESISTOR_1_SYMBOL, getX(), getY());
        }

        @Override
        public String returnTile() {
            return RESISTOR_3_SYMBOL;
        }
    }

    public class Resistor5 extends Resistor {
        public Resistor5(Direction dir, int x, int y, final int z) {
            super(dir, 5, x, y, z);
        }

        @Override
        public void setOut(final int dx, final int dy, final boolean out, final int t){
            super.setOut(dx, dy, out, t);
            //setBreadBoardTile(RESISTOR_1_SYMBOL, getX(), getY());
        }

        @Override
        public String returnTile() {
            return "5";
        }
    }

    public class Resistor10 extends Resistor {
        public Resistor10(Direction dir, final int x, final int y, final int z) {
            super(dir, 10, x, y, z);
        }

        @Override
        public void setOut(final int dx, final int dy, final boolean out, final int t){
            super.setOut(dx, dy, out, t);
            //setBreadBoardTile(RESISTOR_1_SYMBOL, getX(), getY());
        }

        @Override
        public String returnTile() {
            return RESISTOR_5_SYMBOL;
        }
    }

    /**
     * Abstract Parent Class for the breadboard
     */
    public abstract class BreadBoardItem { //extends Thread {
        private int x = 0;
        private int y = 0;
        private int z = 1;//1 is middle in a 3 block tall array
        private boolean out = false;
        private Direction dir = Direction.RIGHT;

        public BreadBoardItem(final Direction dir, final int x, final int y, final int z) {
            this.dir = dir;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public abstract void signal(int t);

        public int getX(){
            return x;
        }

        public int getY(){
            return y;
        }

        public int getZ() {
            return z;
        }

        public Direction getDir(){
            return dir;
        }

        public boolean getOut(){
            return out;
        }

        public void setDir(final Direction dir){
            this.dir = dir;
        }

        public String returnTile(){
            return EMPTY_SYMBOL;
        }
    }

    public abstract class CBreadBoardItem extends BreadBoardItem {

        public CBreadBoardItem(Direction dir, int x, int y, final int z) {
            super(dir, x, y, z);
            cBreadBoardItemsList.add(this);
        }

        public abstract void set(final boolean on);

        //public abstract void signal(final boolean s);

    }


    //--Getters--


    public String getGamemode() {
        return gamemode;
    }

    /**
     * Returns the default breadboard
     * @return breadboard array
     */
    public String[][][] getBreadboard(){
        return breadboard;
    }

    /**
     * Returns the array of directions of items in the Breadboard
     * @return breadboardDirection array
     */
    public Direction[][][] getBreadboardDirection(){
        return breadboardDirection;
    }

    /**
     * Returns the string array of directions of items in the Breadboard at a given z
     * can be direction one or two depending on the given array
     * @return symobl breadboardDirection array
     */
    public String[][] get2DBreadboardDirectionAsStringArray(final Direction[][][] dA, final int z){
        //zyx
        String[][] arr = new String[dA[0].length][dA[0][0].length];
            for(int j = 0; j < dA[z].length; j++){
                for(int k = 0; k < dA[z][j].length; k++){
                    arr[j][k] = dA[z][j][k].getSymbol();
                }
            }

        return arr;
    }


    /**
     * Returns the string array of directions of items in the Breadboard
     * can be direction one or two depending on the given array
     * @return symobl breadboardDirection array
     */
    public String[][][] getBreadboardDirectionAsStringArray(final Direction[][][] dA){
        //zyx
        String[][][] arr = new String[dA.length][dA[0].length][dA[0][0].length];
        for(int i = 0; i < dA.length; i++){
            for(int j = 0; j < dA[i].length; j++){
                for(int k = 0; k < dA[i][j].length; k++){
                    arr[i][j][k] = dA[i][j][k].getSymbol();
                }
            }
        }
        return arr;
    }


    /**
     * Returns the array of the second directions of items in the Breadboard
     * @return breadboardDirection2 array
     */
    public Direction[][][] getBreadboardDirection2(){
        return breadboardDirection2;
    }

    /**
     * Returns the array of BreadBoard items
     * @return
     */
    public List<BreadBoardItem> getBreadBoardItemsList() {
        return breadBoardItemsList;
    }

    /**
     * Returns the yuuuge 2-dimensional signal array
     *
     * @return signalArray
     */
    public Object[][] getSignalArray(){
        return signalArray;
    }



    //--Setters--

    public void setGamemode(String gamemode) {
        this.gamemode = gamemode;
    }

    public void printTiles(final int x, final int y, final int z) {

        String s = "";
        s += x + "," + y + "," + z + "\n";
        for(int i = 0; i < z; i++) {
            s += "LAYER " + i + "\n";
            s += "TILES\n";
            s += convertTilesInto2DArrayString(
                    breadboard[i],
                    x,
                    y);
            s += "DIR1\n";
            s += convertTilesInto2DArrayString(
                    get2DBreadboardDirectionAsStringArray(breadboardDirection, i),
                    x,
                    y);
            s += "DIR2\n";
            s += convertTilesInto2DArrayString(
                    get2DBreadboardDirectionAsStringArray(breadboardDirection2, i),
                    x,
                    y);

        }
        System.out.println(s);
    }

    /**
     * 2D Array into 2D String
     * @param array
     * @param numX
     * @param numY
     * @return
     */
    private String convertTilesInto2DArrayString(final String[][] array, final int numX, final int numY) {
        String s = "";

        //String[] line = new String[numX];

        for (int j = 0; j < numY; j++) {
            for (int k = 0; k < numX; k++) {
                s += array[j][k];
                if(k != numX -1){
                    s += ",";
                }
            }
            s += "\n";
        }

        //s += "\n";


        return s;
    }

    private String convertTilesIntoArrayString(final String[][][] array, final int numX, final int numY, final int numZ) {
        String s = "";

        for (int i = 0; i < numZ; i++) {
            for (int j = 0; j < numY; j++) {
                for (int k = 0; k < numX; k++) {
                    s += array[i][j][k];
                    if(k != numX -1){
                        s += ",";
                    }

                }
                s += "\n";
            }

            s += "\n";
        }

        return s;
    }

}

