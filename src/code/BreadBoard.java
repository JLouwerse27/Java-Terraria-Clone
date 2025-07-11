package src.code;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static src.code.Main.keys;

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

    private final short WIDTH;
    private final short HEIGHT;
    private final byte ZHEIGHT;

    final byte TOP_Z;
    final byte BOTTOM_Z;

    final static short SIGNAL_ARRAY_LENGTH = 50;
    final static byte SIGNAL_ARRAY_D_PLACE = 0;
    final static byte SIGNAL_ARRAY_S_PLACE = 1;
    final static byte SIGNAL_ARRAY_X_PLACE = 2;
    final static byte SIGNAL_ARRAY_Y_PLACE = 3;
    final static byte SIGNAL_ARRAY_Z_PLACE = 4;
    final static byte SIGNAL_ARRAY_TICK_PLACE = 5;
    final static byte MAX_TICKS_IN_THE_FUTURE = 100;
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

//    final static String EMPTY_SYMBOL      = TileString.BreadBoardEmpty.getSymbol();
//    final static String NOT_SYMBOL        = TileString.Not.getSymbol();
//    final static String OR_SYMBOL         = TileString.Or.getSymbol();
//    final static String AND_SYMBOL        = TileString.And.getSymbol();
//    final static String LED_OFF_SYMBOL   = TileString.LEDOff.getSymbol();
//    final static String LED_ON_SYMBOL    = TileString.LEDOn.getSymbol();
//    final static String BUTTON_OFF_SYMBOL = TileString.ButtonOff.getSymbol();
//    final static String BUTTON_ON_SYMBOL  = TileString.ButtonOn.getSymbol();
//    final static String SWITCH_OFF_SYMBOL = TileString.SwitchOff.getSymbol();
//    final static String SWITCH_ON_SYMBOL  = TileString.SwitchOn.getSymbol();
//    final static String WIRE_OFF_SYMBOL   = TileString.WireOff.getSymbol();
//    final static String WIRE_ON_SYMBOL    = TileString.WireOn.getSymbol();
//    final static String CROSSWIRE_SYMBOL  = TileString.CrossWire.getSymbol();
//    final static String RESISTOR_1_SYMBOL = TileString.Resistor1.getSymbol();
//    final static String RESISTOR_3_SYMBOL = TileString.Resistor3.getSymbol();
//    final static String RESISTOR_5_SYMBOL = TileString.Resistor5.getSymbol();
//    final static String RESISTOR_10_SYMBOL = TileString.Resistor10.getSymbol();

    final static byte EMPTY_BYTE        = TileByte.Empty.getSymbol();
    final static byte NOT_BYTE          = TileByte.Not.getSymbol();
    final static byte OR_BYTE           = TileByte.Or.getSymbol();
    final static byte XOR_BYTE          = TileByte.Xor.getSymbol();
    final static byte AND_BYTE          = TileByte.And.getSymbol();
    final static byte LED_OFF_BYTE      = TileByte.LEDOff.getSymbol();
    final static byte LED_ON_BYTE       = TileByte.LEDOn.getSymbol();
    final static byte BUTTON_OFF_BYTE   = TileByte.ButtonOff.getSymbol();
    final static byte BUTTON_ON_BYTE    = TileByte.ButtonOn.getSymbol();
    final static byte SWITCH_OFF_BYTE   = TileByte.SwitchOff.getSymbol();
    final static byte SWITCH_ON_BYTE    = TileByte.SwitchOn.getSymbol();
    final static byte WIRE_OFF_BYTE     = TileByte.WireOff.getSymbol();
    final static byte WIRE_ON_BYTE      = TileByte.WireOn.getSymbol();
    final static byte DOUBLE_WIRE_BYTE    = TileByte.DoubleWire.getSymbol();
    final static byte RESISTOR_1_BYTE   = TileByte.Resistor1.getSymbol();
    final static byte RESISTOR_3_BYTE   = TileByte.Resistor3.getSymbol();
    final static byte RESISTOR_5_BYTE   = TileByte.Resistor5.getSymbol();
    final static byte RESISTOR_10_BYTE  = TileByte.Resistor10.getSymbol();


    private String gamemode = DEFAULT_KEYWORD;
    public byte itemCursor = 0;


    public List<BreadBoardItem> breadBoardItemsList = new ArrayList<BreadBoardItem>();
    /**
     * Clickable Breadboard Items List
     */
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
//    private String[] itemEnum = {
//            EMPTY_SYMBOL,
//            SWITCH_OFF_SYMBOL,
//            WIRE_OFF_SYMBOL,
//            CROSSWIRE_SYMBOL,
//            NOT_SYMBOL,
//            OR_SYMBOL,
//            AND_SYMBOL,
//            LED_OFF_SYMBOL,
//            RESISTOR_1_SYMBOL,
//            RESISTOR_3_SYMBOL,
//            RESISTOR_5_SYMBOL,
//            RESISTOR_10_SYMBOL
//    };

    //note: shortened (removed S and W and L
    private byte[] itemEnumBytes = {
            EMPTY_BYTE,
            SWITCH_OFF_BYTE,
            WIRE_OFF_BYTE,
            DOUBLE_WIRE_BYTE,
            NOT_BYTE,
            OR_BYTE,
            AND_BYTE,
            LED_OFF_BYTE,
            RESISTOR_1_BYTE,
            RESISTOR_3_BYTE,
            RESISTOR_5_BYTE,
            RESISTOR_10_BYTE
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
    //private String[][][] breadboard;
    private Direction[][][] breadboardDirection;
    private Direction[][][] breadboardDirection2;

    private byte[][][] breadboardByte;
    //private Direction[][][] breadboardDirectionByte;
    //private Direction[][][] breadboardDirectionByte2;

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
        }else if(s.equals("n") || s.equals(" ")){
            return dN;
        }
        return null;
    }

    public Direction convertToDirectionBytes(final byte s){
        return Direction.fromSymbol(s);
    }

    /**
     * Converts given parameters into a new Breadboard Object
     * @param tb tile byte
     * @param d direction1
     * @param d2 direction2
     * @param x
     * @param y
     * @return proper BreadBoardItemm
     */
    private BreadBoardItem convertToTypeBytes(final byte tb, final Direction d, final Direction d2, final short x, final short y, final byte z) {
        if (tb == SWITCH_OFF_BYTE) {
            return new Switch(false, d, x, y, z);
        } else if (tb == SWITCH_ON_BYTE) {
            return new Switch(true, d, x, y, z);
        } else if (tb == WIRE_OFF_BYTE || tb == WIRE_ON_BYTE) {
            return new Wire(d, d2, x, y, z);
        } else if (tb == DOUBLE_WIRE_BYTE) {
            return new DoubleWire(d, d2, x, y, z);
        } else if (tb == NOT_BYTE) {
            return new Not(d, x, y, z);
        } else if (tb == AND_BYTE) {
            return new And(d, x, y, z);
        } else if (tb == OR_BYTE) {
            return new Or(d, x, y, z);
        } else if (tb == XOR_BYTE) {
            return new Xor(d, x, y, z);
        } else if (tb == LED_ON_BYTE) {
            return new LED(true, d, x, y, z);
        } else if (tb == LED_OFF_BYTE) {
            return new LED(false, d, x, y, z);
            // Resistors
        } else if (tb == RESISTOR_1_BYTE) {
            return new Resistor1(d, x, y, z);
        } else if (tb == RESISTOR_3_BYTE) {
            return new Resistor3(d, x, y, z);
        } else if (tb == RESISTOR_5_BYTE) {
            return new Resistor5(d, x, y, z);
        } else if (tb == RESISTOR_10_BYTE) {
            return new Resistor10(d, x, y, z);
        } else if (tb == EMPTY_BYTE) {
            return null;
        } else {
            System.out.println("Unknown tile: " + tb);
            return null;
        }
    }

    /**
     * Converts given parameters into a new CBreadBoardItem Object
     * @param tb tile byte
     * @param d direction1
     * @param d2 direction2
     * @param x
     * @param y
     * @return proper BreadBoardItemm
     */
    private CBreadBoardItem convertToClickableTypeBytes(final byte tb, final Direction d, final Direction d2, final short x, final short y, final byte z) {
        if (tb == SWITCH_OFF_BYTE) {
            return new Switch(false, d, x, y, z);
        } else if (tb == SWITCH_ON_BYTE) {
            return new Switch(true, d, x, y, z);
        } if (tb == BUTTON_OFF_BYTE) {
            return new Button(false, d, x, y, z);
        } else if (tb == BUTTON_ON_BYTE) {
            return new Button(true, d, x, y, z);
        }else {
            System.out.println("cTCTB(): Unknown tile: " + tb);
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
        //System.out.println("size of bbil: " + breadBoardItemsList.size());
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
    private int getClickableBreadBoardItemIndexAtCoordinates(final short x, final short y, final byte z) {
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

    /**
     * Sets the corresponding item lists based off the type, directions, and coordinates
     * @param type
     * @param dir1
     * @param dir2
     * @param x
     * @param y
     * @param z
     * @return
     */
    public boolean changeBreadBoardBytes(final byte type, final Direction dir1,
                                    final Direction dir2, final short x, final short y, final byte z){

        if(x < 0 || y < 0 || y > breadboardByte[z].length || x > breadboardByte[z][y].length){
            return false;
        }

        if(type == TileByte.Any.getSymbol()) {//any type, changes every click
            //first remove the existing object
            int theOne = 0;

            if (getClickableBreadBoardItemIndexAtCoordinates(x, y, z) != -1) {
                int index = getClickableBreadBoardItemIndexAtCoordinates(x, y, z);
                cBreadBoardItemsList.remove(index);
            }

            if (getBreadBoardItemIndexAtCoordinates(x, y, z) != -1) {
                int index = getBreadBoardItemIndexAtCoordinates(x, y, z);
                breadBoardItemsList.remove(index);
                theOne = index;
            }


            for (byte index = 0; index < itemEnumBytes.length; index++) {
                if (breadboardByte[z][y][x] == index){
                        //|| breadboardByte[z][y][x].toLowerCase().equals(itemEnum[index])) { //HAVE TO CHECK FOR ON WIRES AND LEDS AND SWITCHES
                    if (index + 1 >= itemEnumBytes.length) {
                        index = -1;
                    } else {
                        breadBoardItemsList.add(theOne,
                                //convertToType(itemEnum[index + 1], dN,dN, x, y, z));
                                convertToTypeBytes(index, dN,dN, x, y, z));
                    }
                    breadboardDirection[z][y][x] = dN;//call this before return, cuz of repaint in next fn
                    breadboardDirection2[z][y][x] = dN;
                    setBreadBoardTileByte(type, x, y, z);
                    return true;
                }
            }
        }else if(type == TileByte.Empty.getSymbol()) {//empty
            //first remove the existing object if it's not a wire
            int theOne = 0;
            if (getClickableBreadBoardItemIndexAtCoordinates(x,y,z) != -1 && !(breadboardByte[z][y][x] == type))
            {
                int index = getClickableBreadBoardItemIndexAtCoordinates(x, y, z);
                cBreadBoardItemsList.remove(index);
            }
            if (getBreadBoardItemIndexAtCoordinates(x, y, z) != -1 && !(breadboardByte[z][y][x] == type))
            {
                int index = getBreadBoardItemIndexAtCoordinates(x, y, z);
                breadBoardItemsList.remove(index);
            }
            breadboardDirection[z][y][x] = dN;//call this before cuz of repaint in next fn
            breadboardDirection2[z][y][x] = dN;//call this before cuz of repaint in next fn
            //setBreadBoardTile(itemEnum[type], x, y, z);
            setBreadBoardTileByte(type, x, y, z);
        }else {//anything else
            //first remove the existing object if it's not a wire
            int theOne = 0;
            int theCOne = -1;

            //remove any "ClickableBreadBoardItems"
            if (getClickableBreadBoardItemIndexAtCoordinates(x, y, z) != -1){
                int index = getClickableBreadBoardItemIndexAtCoordinates(x, y, z);
                cBreadBoardItemsList.remove(index);
                theCOne = index;
            }
            //remove any general "BreadboardItems"
            if (getBreadBoardItemIndexAtCoordinates(x, y, z) != -1){
                int index = getBreadBoardItemIndexAtCoordinates(x, y, z);
                breadBoardItemsList.remove(index);
                theOne = index;
            }

            breadBoardItemsList.add(theOne,
                    //convertToType(itemEnum[type], dir1,dir2, x, y, z));
                    convertToTypeBytes(type, dir1,dir2, x, y, z));
            if(theCOne != -1 &&
                    convertToClickableTypeBytes(type, dir1,dir2, x, y, z) != null) {
                cBreadBoardItemsList.add(theCOne, convertToClickableTypeBytes(type, dir1,dir2, x, y, z));
            }
            //System.out.println("changeBreadBoard(): breadBoardItemesList has been added to");
            breadboardDirection[z][y][x] = dir1;//call this before cuz of repaint in next fn
            breadboardDirection2[z][y][x] = dir2;
            //setBreadBoardTile(itemEnum[type], x, y, z);
            setBreadBoardTileByte(type, x, y, z);
        }
        return true;//fix later for other cases
    }

    /**
     * Byte version of setBreadBoardState
     * @param tiles
     * @param dir1
     * @param dir2
     */
    public void setBreadBoardStateByte(byte[][][] tiles, Direction[][][] dir1, Direction[][][] dir2) {
        for (byte z = 0; z < ZHEIGHT; z++) {
            for (short y = 0; y < HEIGHT; y++) {
                for (short x = 0; x < WIDTH; x++) {
                    byte tile = tiles[z][y][x];
                    Direction d1 = dir1[z][y][x];
                    Direction d2 = dir2[z][y][x];

                    setBreadBoardTileByte(tile, x, y, z);
                    //System.out.println("called setBBTB");
                    setBreadBoardDirectionTile(d1, x, y, z);
                    setBreadBoardDirection2Tile(d2, x, y, z);

                    addNewItemToBreadBoardItemsListByte(x,y,z,d1,d2);

                    //int type = convertToItemEnumOrdinal(tile);
//                    if (tile != -1) {
//                        changeBreadBoardBytes(tile, d1, d2, x, y, z);
//                    } else {
//                        byte raw = tiles[z][y][x];
//                        System.out.printf("setBreadBoardState(): ERROR SETTING TILES raw='%s' tile='%s' at (%d,%d,%d)%n",
//                                raw, tile, x, y, z);
//                        //System.out.println("length = " + raw.length() + " char codes: " + Arrays.toString(raw.toCharArray()));
//                    }
                }
            }
        }
    }

    public void addNewItemToBreadBoardItemsListByte(final short x, final short y, final byte z, final Direction d, final Direction d2) {

        switch (TileByte.fromSymbol(breadboardByte[z][y][x])) {
            case TileByte.ButtonOn:
                breadBoardItemsList.add(new Button(true, d, x, y, z));
                break;
            case TileByte.ButtonOff:
                breadBoardItemsList.add(new Button(false, d, x, y, z));
                break;
            case TileByte.SwitchOn:
                breadBoardItemsList.add(new Switch(true, d, x, y, z));
                break;
            case TileByte.SwitchOff:
                breadBoardItemsList.add(new Switch(false, d, x, y, z));
                break;
            case TileByte.LEDOff:
                breadBoardItemsList.add(new LED(false, d, x, y, z));
                break;
            case TileByte.LEDOn:
                breadBoardItemsList.add(new LED(false, d, x, y, z));
                break;
            case TileByte.WireOff:
                breadBoardItemsList.add(new Wire(d, d2, x, y, z));
                //System.out.println("Placed wire at " + x + "," + y + "," + z);
                break;
            case TileByte.And:
                breadBoardItemsList.add(new And(d, x, y, z));
                break;
            case TileByte.Not:
                breadBoardItemsList.add(new Not(d, x, y, z));
                break;
            case TileByte.Or:
                breadBoardItemsList.add(new Or(d, x, y, z));
                break;
            case TileByte.WireOn:
                breadBoardItemsList.add(new Wire(d, d2, x, y, z));
                break;
            case TileByte.DoubleWire:
                breadBoardItemsList.add(new DoubleWire(d, d2, x, y, z));
                break;
            case TileByte.Resistor1:
                breadBoardItemsList.add(new Resistor1(d, x, y, z));
                break;
            case TileByte.Resistor3:
                breadBoardItemsList.add(new Resistor3(d, x, y, z));
                break;
            case TileByte.Resistor5:
                breadBoardItemsList.add(new Resistor5(d, x, y, z));
                break;
            case TileByte.Resistor10:
                breadBoardItemsList.add(new Resistor10(d, x, y, z));
                break;
            case TileByte.Xor:
                breadBoardItemsList.add(new Xor(d, x, y, z));
                break;
            case TileByte.Empty:
                break;
            default:
                System.out.println("ERROR LOADING TILES IN BREADBOARD CONSTRUCTOR!");
                break;
        }

    }


    public BreadBoard(final short width, final short height, final byte zHeight) {
        WIDTH = width;
        HEIGHT = height;
        ZHEIGHT = zHeight;
        System.out.println("Breadboard(): WIDTH " + WIDTH + ", HEIGHT " + HEIGHT + ", ZHEIGHT " + ZHEIGHT);
        TOP_Z = (byte) (zHeight - 1);
        BOTTOM_Z = 0;
        //if(WIDTH != Main.DEFAULT_SCREEN_SIZE && HEIGHT != Main.DEFAULT_SCREEN_SIZE && ZHEIGHT != Main.DEFAULT_SCREEN_SIZE){
        //breadboard = new String[zHeight][height][width];
        breadboardByte = new byte[zHeight][height][width];
        breadboardDirection = new Direction[zHeight][height][width];
        breadboardDirection2 = new Direction[zHeight][height][width];

         setSignalArrayToNull();
    }

    /**
     * Checks clicks and calls update breadboard
     * two different modes; first:
     * checks the click within the boundary of a tile within the Item list
     * @param x
     * @param y
     * @return whether the program should repaint or not
     */
    public boolean checkClick(final MouseEvent e, short x, final short y, final byte layer) {
        //System.out.println("checkClick at " + x + ", " + y);
        if(gamemode.equals(DEFAULT_KEYWORD))
        {
            for(CBreadBoardItem cbi:cBreadBoardItemsList)
            {
                //check if it is within the width and height of the clickableBreadBoardItem
                if(x >= cbi.getX() * MyGameScreen.tileWidth &&
                        x < cbi.getX() * MyGameScreen.tileWidth + MyGameScreen.tileWidth
                        && y >= cbi.getY() * MyGameScreen.tileHeight &&
                        y < cbi.getY() * MyGameScreen.tileHeight + MyGameScreen.tileHeight)
                {
                    cbi.set(true);//true is just a necessary thing, not actually used
                    //setBreadBoardTile(cbi.returnTile(),cbi.getX(),cbi.getY(), cbi.getZ());
                    setBreadBoardTileByte(cbi.returnTile(),cbi.getX(),cbi.getY(), cbi.getZ());
                    return true;
                }
            }
        }else if (gamemode.equals(EDITING_KEYWORD))
        {
            if (!Main.getCopying() && !Main.getCutting()) {
                //System.out.println("mouse click:" + e.getButton());
                short tile_x = (short) (x / MyGameScreen.tileWidth);
                short tile_y = (short) (y / MyGameScreen.tileHeight);

                if (tile_x >= 0 && tile_x < MyGameScreen.xPixels
                        && tile_y >= 0 && tile_y < MyGameScreen.yPixels)
                {
                    if (e.getButton() == MouseEvent.BUTTON3)
                    {//right click
                        //return changeBreadBoard(EMPTY_TYPE, dN, dN, tile_x, tile_y, layer);//0 is empty space
                        return changeBreadBoardBytes(TileByte.Empty.getSymbol(), dN, dN, tile_x, tile_y, layer);//0 is empty space
                    } else if (e.getButton() == MouseEvent.BUTTON1)
                    {//left click
//                        return changeBreadBoard(itemCursor, dN, dN, tile_x, tile_y, layer);
                        return changeBreadBoardBytes(itemCursor, dN, dN, tile_x, tile_y, layer);
                    }else if (e.getButton() == MouseEvent.NOBUTTON)
                    { // drag ======= MAY HAVE TO CHANGE ========
                        if(Main.mouseClickNumber[0] == MouseEvent.BUTTON1)
                        {//drag left click
//                            return changeBreadBoard(itemCursor, dN, dN, tile_x, tile_y, layer);
                            return changeBreadBoardBytes(itemCursor, dN, dN, tile_x, tile_y, layer);
                        }else if (Main.mouseClickNumber[0] == MouseEvent.BUTTON3)
                        {//drag right click
                            //return changeBreadBoard(EMPTY_TYPE, dN, dN, tile_x, tile_y, layer);
                            return changeBreadBoardBytes(TileByte.Empty.getSymbol(), dN, dN, tile_x, tile_y, layer);
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

            int sY = (int)(Main.mouseY[0] - Main.SCREEN_Y_OFFSET + Main.DEFAULT_SCREEN_Y_OFFSET) / MyGameScreen.tileHeight;
            if(sY < 0) sY = 0;

            int eX = (int)(Main.mouseX[1] - Main.SCREEN_X_OFFSET) / MyGameScreen.tileWidth;
            if(eX > MyGameScreen.xPixels) eX = MyGameScreen.xPixels;

            int eY = (int)(Main.mouseY[1] - Main.SCREEN_Y_OFFSET + Main.DEFAULT_SCREEN_Y_OFFSET) / MyGameScreen.tileHeight;
            if(eY > MyGameScreen.yPixels) eY = MyGameScreen.yPixels;

            System.out.println("selecting entities with: sX" + sX + ", sY" + sY + ", eX" + eX + ", eY" + eY);
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
            if(sZ == eZ){
                System.out.println("sZ == eZ");
            }
            if(sX == eX){
                System.out.println("sX == eX");
            }
            if(sY == eY){
                System.out.println("sY == eY");
            }
            //System.out.println("one of the lengths is zero");
            return;
        }


        int dX = eX - sX;
        int dY = eY - sY;
        int dZ = eZ - sZ;

//        String[][][] tempCutCopyPasteBoard = new String[dZ][dY][dX];
//        Direction[][][] tempCutCopyPasteBoardDir1 = new Direction[dZ][dY][dX];
//        Direction[][][] tempCutCopyPasteBoardDir2 = new Direction[dZ][dY][dX];

        byte[][][] tempCutCopyPasteBoard = new byte[dZ][dY][dX];
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

        //reset the temp lists in mgs
        Main.getMyGameScreen().tempCutCopyPasteBoardList.clear();
        Main.getMyGameScreen().tempCutCopyPasteBoardDirection1List.clear();
        Main.getMyGameScreen().tempCutCopyPasteBoardDirection2List.clear();
        //set the board to the actual breadboard pieces
        for(int i = 0; i < dZ; i++) {
            Main.getMyGameScreen().tempCutCopyPasteBoardList.add(new ArrayList<>());
            Main.getMyGameScreen().tempCutCopyPasteBoardDirection1List.add(new ArrayList<>());
            Main.getMyGameScreen().tempCutCopyPasteBoardDirection2List.add(new ArrayList<>());

            for (int j = 0; j < dY; j++) {
                Main.getMyGameScreen().tempCutCopyPasteBoardList.get(i).add(new ArrayList<>());
                Main.getMyGameScreen().tempCutCopyPasteBoardDirection1List.get(i).add(new ArrayList<>());
                Main.getMyGameScreen().tempCutCopyPasteBoardDirection2List.get(i).add(new ArrayList<>());

                for (int k = 0; k < dX; k++) {
                    tempCutCopyPasteBoard[i][j][k] = breadboardByte[i + sZ][j + sY][k + sX];
                    tempCutCopyPasteBoardDir1[i][j][k] = breadboardDirection[i + sZ][j + sY][k + sX];
                    tempCutCopyPasteBoardDir2[i][j][k] = breadboardDirection2[i + sZ][j + sY][k + sX];

                    Main.getMyGameScreen().tempCutCopyPasteBoardList.get(i).get(j).add(tempCutCopyPasteBoard[i][j][k]);
                    Main.getMyGameScreen().tempCutCopyPasteBoardDirection1List.get(i).get(j).add(tempCutCopyPasteBoardDir1[i][j][k]);
                    Main.getMyGameScreen().tempCutCopyPasteBoardDirection2List.get(i).get(j).add(tempCutCopyPasteBoardDir2[i][j][k]);
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
    public void eraseRegion(short sX, short sY, byte sZ, short eX, short eY, byte eZ) {
        //System.out.println("called erase region");
        for (byte z = sZ; z < eZ; z++) {
            for (short y = sY; y < eY; y++) {
                for (short x = sX; x < eX; x++) {
                    //changeBreadBoard(EMPTY_SYMBOL, Direction.NONE, Direction.NONE, x, y, z); // sets to EMPTY
                    changeBreadBoardBytes(TileByte.Empty.getSymbol(), Direction.NONE, Direction.NONE, x, y, z); // sets to EMPTY
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
    public void rotateItem(final int dirNumber, final int x, final int y, final int z) {
        if(gamemode.equals(EDITING_KEYWORD)) {
            if(dirNumber == 0) {//rotate dir1
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

                    if (locateBreadBoardItemOnBoard(x,y, z).returnTile() == TileByte.DoubleWire.getSymbol()) {

                        DoubleWire dw = (DoubleWire) locateBreadBoardItemOnBoard(x,y, z);
                        int dirNum = dw.getDir2().ordinal() + 1;
                        if (dirNum >= Direction.values().length) dirNum = 0;
                        breadboardDirection2[z][y][x] = Direction.values()[dirNum];
                        //to do: implement safety measure so that user cannot make d2 the opposite of d1
                        dw.setDir2(Direction.values()[dirNum]);

                    }else if (locateBreadBoardItemOnBoard(x,y, z).returnTile() == TileByte.WireOn.getSymbol()
                    || locateBreadBoardItemOnBoard(x,y, z).returnTile() == TileByte.WireOff.getSymbol()) {

                        Wire w = (Wire) locateBreadBoardItemOnBoard(x,y, z);
                        int dirNum = w.getDir2().ordinal() + 1;
                        if (dirNum >= Direction.values().length) dirNum = 0;
                        breadboardDirection2[z][y][x] = Direction.values()[dirNum];
                        //to do: implement safety measure so that user cannot make d2 the opposite of d1
                        w.setDir2(Direction.values()[dirNum]);

                    }
                    callPaint();
                }
            }
        }
    }

    public void takeCareOfWASD() {
        if (keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP]) {//dont work rn
            Main.getMyGameScreen().yOffset += MyGameScreen.tileSize;
            Main.SCREEN_Y_OFFSET = Main.getMyGameScreen().yOffset;
        }else if (keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN]) {
            Main.getMyGameScreen().yOffset -= MyGameScreen.tileSize;
            Main.SCREEN_Y_OFFSET = Main.getMyGameScreen().yOffset;
        }if (keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT]) {
            Main.getMyGameScreen().xOffset -= MyGameScreen.tileSize;
            Main.SCREEN_X_OFFSET = Main.getMyGameScreen().xOffset;
        }else if (keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT]) {
            Main.getMyGameScreen().xOffset += MyGameScreen.tileSize;
            Main.SCREEN_X_OFFSET = Main.getMyGameScreen().xOffset;
        }
        Main.getMyGameScreen().repaint();
    }

    /**
     * Ticks
     */
    public void tick(final int tickNo) {
        takeCareOfWASD();
        short i = 0;
        Object[][] signalArrayOnCall = new Object[signalArray.length][];
        for (short j = 0; j < signalArray.length; j++) {
            if (signalArray[j] != null) {
                signalArrayOnCall[j] = Arrays.copyOf(signalArray[j], signalArray[j].length);
            }
        }
        //System.out.println("tick(): signal at tick " + Arrays.deepToString(signalArrayOnCall));
        while (i < signalArrayOnCall.length) {
            if (signalArrayOnCall[i] != null) {
                Direction d = (Direction) signalArrayOnCall[i][SIGNAL_ARRAY_D_PLACE];
                boolean s = (boolean) signalArrayOnCall[i][SIGNAL_ARRAY_S_PLACE];
                short x = ((Integer) signalArrayOnCall[i][SIGNAL_ARRAY_X_PLACE]).shortValue();
                short y = ((Integer) signalArrayOnCall[i][SIGNAL_ARRAY_Y_PLACE]).shortValue();
                byte  z = ((Integer) signalArrayOnCall[i][SIGNAL_ARRAY_Z_PLACE]).byteValue();
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
     * setting the array cell at x and y to given tile byte
     * calls main.setTiles
     * then repaints //commented out
     * @param tile
     * @param x
     * @param y
     */
    public void setBreadBoardTileByte(final byte tile, final short x, final short y, final short z){
        if(tile != TileByte.Any.getSymbol()) {
            breadboardByte[z][y][x] = tile;
        }
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


    private void setWiresAndLeds(final boolean s, final short dx, final short dy, final byte dz, final short x, final short y, final int z, final int t) {
        //System.out.println("called setWiresAndLeds going with dx " + dx + " dy " + dy + " dz " + dz + " going to x " + x + " y " + y + " z " + z);
        //String tile = breadboard[z][y][x];
        byte tile = breadboardByte[z][y][x];

        if (s) {
            if (tile == WIRE_OFF_BYTE) {
                BreadBoardItem item = locateBreadBoardItemOnBoard(x, y, z);
                if (item instanceof Wire wire) {
                    wire.setOut(dx, dy, dz, true, t);
                } else {
                    System.out.println("Expected Wire at " + x + "," + y + "," + z + " but got null or different type.");
                }
            } else if (tile == LED_OFF_BYTE) {
                LED led = (LED) locateBreadBoardItemOnBoard(x, y, z);
                assert led != null;
                led.setOut(true, t);
            } else if (tile == RESISTOR_1_BYTE) {
                Resistor1 r1 = (Resistor1) locateBreadBoardItemOnBoard(x, y, z);
                assert r1 != null;
                r1.setOut(dx, dy, true, t);
            } else if (tile == RESISTOR_3_BYTE) {
                Resistor3 r3 = (Resistor3) locateBreadBoardItemOnBoard(x, y, z);
                assert r3 != null;
                r3.setOut(dx, dy, true, t);
            } else if (tile == RESISTOR_5_BYTE) {
                Resistor5 r5 = (Resistor5) locateBreadBoardItemOnBoard(x, y, z);
                assert r5 != null;
                r5.setOut(dx, dy, true, t);
            } else if (tile == RESISTOR_10_BYTE) {
                Resistor10 r10 = (Resistor10) locateBreadBoardItemOnBoard(x, y, z);
                assert r10 != null;
                r10.setOut(dx, dy, true, t);
            }
        } else {
            if (tile == WIRE_ON_BYTE) {
                Wire wire = (Wire) locateBreadBoardItemOnBoard(x, y, z);
                assert wire != null;
                wire.setOut(dx, dy, dz, false, t);
            } else if (tile == LED_ON_BYTE) {
                LED led = (LED) locateBreadBoardItemOnBoard(x, y, z);
                assert led != null;
                led.setOut(false, t);
            } else if (tile == RESISTOR_1_BYTE) {
                Resistor1 r1 = (Resistor1) locateBreadBoardItemOnBoard(x, y, z);
                assert r1 != null;
                r1.setOut(dx, dy, false, t);
            } else if (tile == RESISTOR_3_BYTE) {
                Resistor3 r3 = (Resistor3) locateBreadBoardItemOnBoard(x, y, z);
                assert r3 != null;
                r3.setOut(dx, dy, false, t);
            } else if (tile == RESISTOR_5_BYTE) {
                Resistor5 r5 = (Resistor5) locateBreadBoardItemOnBoard(x, y, z);
                assert r5 != null;
                r5.setOut(dx, dy, false, t);
            } else if (tile == RESISTOR_10_BYTE) {
                Resistor10 r10 = (Resistor10) locateBreadBoardItemOnBoard(x, y, z);
                assert r10 != null;
                r10.setOut(dx, dy, false, t);
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
    public void setGates(final boolean s, final short dx, final short dy, final byte dz, final short x, final short y, final byte z, final int t) {
        //String sBR = breadboard[z + dz][y + dy][x + dx]; // adjacent tile
        byte sBR = breadboardByte[z + dz][y + dy][x + dx]; // adjacent tile

        if (sBR == TileByte.Not.getSymbol()) {
            Not not = (Not) locateBreadBoardItemOnBoard(x + dx, y + dy, z + dz);
            assert not != null;
            if (not.setRightGate(s, dx, dy, dz)) {
                not.calculate();
                not.signal(t);
            }
        } else if (sBR == TileByte.And.getSymbol()) {
            And and = (And) locateBreadBoardItemOnBoard(x + dx, y + dy,z + dz);
            assert and != null;
            if (and.setRightGate(s, dx, dy, dz)) {
                and.calculate();
                and.signal(t);
            }
        } else if (sBR == TileByte.Or.getSymbol()) {
            Or or = (Or) locateBreadBoardItemOnBoard(x + dx, y + dy, z + dz);
            assert or != null;
            if (or.setRightGate(s, dx, dy, dz)) {
                or.calculate();
                or.signal(t);
            }
        } else if (sBR == TileByte.Xor.getSymbol()) {
            Xor xor = (Xor) locateBreadBoardItemOnBoard(x + dx, y + dy, z + dz);
            assert xor != null;
            if (xor.setRightGate(s, dx, dy, dz)) {
                xor.calculate();
                xor.signal(t);
            }
        } else if (sBR == TileByte.DoubleWire.getSymbol()) {
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
    private int propagateSignal(final Direction d, final boolean s, final short x, final short y, final byte z, final int t) {
        short sn1 = (short)-1;
        short s0 = (short)0;
        short s1 = (short)1;
        byte bn1 = (byte)-1;
        byte b0 = (byte)0;
        byte b1 = (byte)1;

        if(t == Main.tickNumber) {
            //System.out.println("pS(): signal " + s + " from " + x + " " + y + " on tick " + t);
            if (d == Direction.NONE) {
                for (byte i = (byte) (z - 1); i <= z + 1; i++) {
                    for (short j = (short) (y - 1); j <= y + 1; j++) {
                        for (short k = (short) (x - 1); k <= x + 1; k++) {
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
                                            setGates(s, s1, s0, b0, x, y, z, t);
                                            setWiresAndLeds(s, s1, s0, b0, k, j, i, t);
                                        } else if (k == x - 1) {
                                            setGates(s, sn1, s0, b0, x, y, z, t);
                                            setWiresAndLeds(s, sn1, s0, b0, k, j, i, t);
                                        } else if (j == y + 1) {
                                            setGates(s, s0, s1, b0, x, y, z, t);
                                            setWiresAndLeds(s, s0, s1, b0, k, j, i, t);
                                        } else if (j == y - 1) {
                                            setGates(s, s0, sn1, b0, x, y, z, t);
                                            setWiresAndLeds(s, s0, sn1, b0, k, j, i, t);
                                        }
                                    }
                                }else {
                                    //the tile above or below the sending tile MUST be
                                    //exactly above or below it
                                    if(j == y && k == x) {
                                        //System.out.println("x " + x + " y " + y + " z " + z);
                                        //System.out.println("k " + k + " j " + j + " i " + i);
                                        setGates(s, (short) (k - x), (short) (j - y), (byte) (i - z), x, y, z, t);
                                        setWiresAndLeds(s, (short) (k - x), (short) (j - y), (byte) (i - z), k, j, i, t);
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (d == Direction.RIGHT && x + 1 < WIDTH) {
                setWiresAndLeds(s, s1, s0, b0, (short) (x + 1), y, z, t);
                setGates(s, s1, s0, b0, x, y, z, t);
            } else if (d == Direction.LEFT && x - 1 >= 0) {
                setWiresAndLeds(s, sn1, s0, b0, (short) (x - 1), y, z, t);
                setGates(s, sn1, s0, b0, x, y, z, t);
            } else if (d == Direction.DOWN && y + 1 < HEIGHT) {
                setWiresAndLeds(s, s0, s1, b0, x, (short) (y + 1), z, t);
                setGates(s, s0, s1, b0, x, y, z, t);
            } else if (d == Direction.UP && y - 1 >= 0) {
                setWiresAndLeds(s, s0, sn1, b0, x, (short) (y - 1), z, t);
                setGates(s, s0, sn1, b0, x, y, z, t);
            } else if (d == Direction.INTO && z - 1 >= 0) {
                setWiresAndLeds(s, s0, s0, bn1, x, y, (short) (z - 1), t);
                setGates(s, s0, s0, bn1, x, y, z, t);
            } else if (d == Direction.OUTOF && z + 1 < ZHEIGHT) {
                setWiresAndLeds(s, s0, s0, b1, x, y, (short) (z + 1), t);
                setGates(s, s0, s0, b1, x, y, z, t);
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

        public Button(final boolean out, final Direction dir, final short x, final short y, final byte z) {
            super(dir,x,y,z);
            this.out = out;
        }

        public void set(boolean out) {
            this.out = out;
        }

        public byte returnTile(){
            if(out){
                return TileByte.ButtonOn.getSymbol();
            }else {
                return TileByte.ButtonOff.getSymbol();
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

        public Switch(boolean out, Direction dir, final short x, final short y, final byte z) {
            super(dir,x,y,z);
            this.out = out;
        }

        public void set(final boolean b) {
            out = !out;
            this.signal(Main.tickNumber + 1);
        }

        public byte returnTile() {
            if (out) {
                return TileByte.SwitchOn.getSymbol();
            } else {
                return TileByte.SwitchOff.getSymbol();
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

        public Gate(final Direction dir, final short x, final short y, final byte z) {
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
        public boolean setRightGate(final boolean s, final short deltax, final short deltay, final byte deltaz){
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

        public And(final Direction dir, final short x, final short y, final byte z) {
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

        public Not(final Direction dir, final short x, final short y, final byte z) {
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

        public Or(final Direction dir, final short x, final short y, final byte z) {
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

        public byte returnTile(){
            return TileByte.Or.getSymbol();
        }

        public void signal(int tick_when_set) {
            //BreadBoard.this.signal(this.getDir(),out,this.getX(),this.getY());
            super.signal(tick_when_set);
        }

    }

    private class Xor extends Gate {

        public Xor(final Direction dir, final short x, final short y, final byte z) {
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
            //THIS IS JUST A AND C, CHANGE IF NECESSARY
            if(xor(A,C)){
                out = true;
            } else {
                out = false;
            }
        }

        private boolean xor(boolean A, boolean B){
            if(A || B) {
                if(!(A && B)){
                    return true;
                }
            }
            return false;
        }

        public byte returnTile(){
            return TileByte.Xor.getSymbol();
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

        public LED(final boolean out, final Direction dir, final short x, final short y, final byte z) {
            super(dir, x, y, z);
            this.out = out;
        }

        public void setOut(final boolean s, int t) {
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
                setBreadBoardTileByte(TileByte.LEDOn.getSymbol(), getX(), getY(), getZ());
            } else {
                setBreadBoardTileByte(TileByte.LEDOff.getSymbol(), getX(), getY(), getZ());
            }
            Main.getMyFrame().repaint();
        }

        private void delay() {
            //delay
        }

        public byte returnTile(){
            if(out){
                return TileByte.LEDOn.getSymbol();
            }else {
                return TileByte.LEDOff.getSymbol();
            }
        }
    }

    /**
     * A new class for wire, which acts as a cross between two wires,
     * setting one side will set the opposite side on (say South side to North side)
     * and it will be independent of the other side.
     */
    public class DoubleWire extends Wire {

        public DoubleWire(final Direction dir1, final Direction dir2, final short x, final short y, final byte z) {
            super(dir1, dir2, x, y, z);
        }

        public void setRightInput(final boolean s, final short deltax, final short deltay, final int t){
            //this doubleWire is to the right of the input (ex. wire)
            if(deltax == 1){
                if(this.getDir() == Direction.RIGHT || this.getDir2() == Direction.RIGHT
                || this.getDir() == Direction.NONE || this.getDir2() == Direction.NONE) {
                    signal(dR, s, t);
                }
            }else if(deltax == -1){//this gate is to the left of the input (ex. wire)
                if(this.getDir() == dL || this.getDir2() == dL
                || this.getDir() == Direction.NONE || this.getDir2() == Direction.NONE) {
                    signal(dL, s, t);
                }
            }else if(deltay == 1){//this gate is below the input (ex. wire)
                if(this.getDir() == dD || this.getDir2() == dD
                || this.getDir() == Direction.NONE || this.getDir2() == Direction.NONE) {
                    signal(dD, s, t);
                }
            }else if(deltay == -1){//this gate is above the input (ex. wire)
                if(this.getDir() == dU || this.getDir2() == dU
                || this.getDir() == Direction.NONE || this.getDir2() == Direction.NONE) {
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

        //public Direction getDir2(){
        //    return dir2;
        //}

        public byte returnTile(){
            return TileByte.DoubleWire.getSymbol();
        }

    }

    /**
     * Wire
     */
    public class Wire extends BreadBoardItem {

        private boolean out = false;
        private Direction dir2;
        private short dx = 0;
        private short dy = 0;
        private byte dz = 0;

        public Wire(final Direction dir, final Direction dir2, final short x, final short y, final byte z) {
            super(dir, x, y, z);
            this.dir2 = dir2;
        }

        public void setOut(final short dx, final short dy, final byte dz, final boolean out, final int t) {
            this.out = out;
            //check first if this signal is coming from the right input direction
            //ie if this wire is facing right, then this should not be taking input
            //from an object to the right of it, thus possibly creating an infinite loop
            //or an insane amount of signals; much havoc was caused because of this on May 29, 2025
            if((this.getDir() == dL && dx != 1) ||
            (this.getDir() == dR && dx != -1) ||
            (this.getDir() == dU && dy != 1) ||
            (this.getDir() == dD && dy != -1) ||
            (this.getDir() == dO && dz != 1) || //out of the screen
            (this.getDir() == dI && dz != -1) ||//into the screen
            this.getDir() == dN) {
                if (out) {
                    setBreadBoardTileByte(TileByte.WireOn.getSymbol(), getX(), getY(), getZ());
                } else {
                    setBreadBoardTileByte(TileByte.WireOff.getSymbol(), getX(), getY(), getZ());
                }
                Main.getMyGameScreen().repaint();
                this.dx = dx;
                this.dy = dy;
                this.dz = dz;
                this.signal(t);
            }
        }

        public void setDir2(final Direction dir2) {
            this.dir2 = dir2;
        }

        public boolean getOut(){
            return out;
        }

        public byte returnTile(){
            if(out){
                return TileByte.WireOn.getSymbol();
            }else {
                return TileByte.WireOff.getSymbol();
            }
        }

        /**
         * Like the signal in Switch, searches for other board members.
         */
        public void signal(final int t) {
            //if(Main.tick_true) {
            BreadBoard.this.signal(this.getDir(), out, this.getX(), this.getY(), this.getZ(), t + 1);
            if(this.getDir2() != this.getDir() && this.getDir2() != Direction.NONE){
                BreadBoard.this.signal(this.getDir2(), out, this.getX(), this.getY(), this.getZ(), t + 1);
            }
                //System.out.println("wire.signal(): at " + this.getX() + " " + this.getY()
                //+ " which will be called on tick " + (t + 1));
            //}
        }

        public Direction getDir2(){
            return dir2;
        }

    }

    public abstract class Resistor extends BreadBoardItem {
        protected int delayTicks;
        protected boolean out = false;

        public Resistor(Direction dir, int delayTicks, short x, short y, final byte z) {
            super(dir, x, y, z);
            this.delayTicks = delayTicks;
        }

        public void setOut(final short dx, final short dy, final boolean out, final int t) {
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
            //System.out.println("Resistor.signal() delayTicks " + delayTicks);
            BreadBoard.this.signal(this.getDir(), out, this.getX(), this.getY(), this.getZ(),t + 1 + delayTicks);
        }

        public int getDelayTicks() {
            return delayTicks;
        }
        public abstract byte returnTile();
    }

    public class Resistor1 extends Resistor {
        public Resistor1(Direction dir, short x, short y, byte z) {
            super(dir, 1, x, y, z);
        }

        @Override
        public void setOut(final short dx, final short dy, final boolean out, final int t){
            super.setOut(dx, dy, out, t);
            //setBreadBoardTile(RESISTOR_1_SYMBOL, getX(), getY());
        }

        @Override
        public byte returnTile() {
            return TileByte.Resistor1.getSymbol();
        }
    }

    public class Resistor3 extends Resistor {
        public Resistor3(Direction dir, short x, short y, byte z) {
            super(dir, 3, x, y, z);
        }

        @Override
        public void setOut(final short dx, final short dy, final boolean out, final int t){
            super.setOut(dx, dy, out, t);
            //setBreadBoardTile(RESISTOR_1_SYMBOL, getX(), getY());
        }

        @Override
        public byte returnTile() {
                return TileByte.Resistor3.getSymbol();
        }
    }

    public class Resistor5 extends Resistor {
        public Resistor5(Direction dir, short x, short y, final byte z) {
            super(dir, 5, x, y, z);
        }

        @Override
        public void setOut(final short dx, final short dy, final boolean out, final int t){
            super.setOut(dx, dy, out, t);
            //setBreadBoardTile(RESISTOR_1_SYMBOL, getX(), getY());
        }

        @Override
        public byte returnTile() {
            return TileByte.Resistor5.getSymbol();
        }
    }

    public class Resistor10 extends Resistor {
        public Resistor10(Direction dir, final short x, final short y, final byte z) {
            super(dir, 10, x, y, z);
        }

        @Override
        public void setOut(final short dx, final short dy, final boolean out, final int t){
            super.setOut(dx, dy, out, t);
            //setBreadBoardTile(RESISTOR_1_SYMBOL, getX(), getY());
        }

        @Override
        public byte returnTile() {
            return TileByte.Resistor10.getSymbol();
        }
    }

    /**
     * Abstract Parent Class for the breadboard
     */
    public abstract class BreadBoardItem { //extends Thread {
        private short x = 0;
        private short y = 0;
        private short z = 1;//1 is middle in a 3 block tall array
        private boolean out = false;
        private Direction dir = Direction.RIGHT;

        public BreadBoardItem(final Direction dir, final short x, final short y, final short z) {
            this.dir = dir;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public abstract void signal(int t);

        public short getX(){
            return x;
        }

        public short getY(){
            return y;
        }

        public short getZ() {
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

        public byte returnTile(){
            return TileByte.Empty.getSymbol();
        }

    }

    public abstract class CBreadBoardItem extends BreadBoardItem {

        public CBreadBoardItem(Direction dir, short x, short y, final short z) {
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
     * Returns the default breadboardByte
     * @return breadboard array
     */
    public byte[][][] getBreadboardByte(){
        return breadboardByte;
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
    public byte[][] get2DBreadboardDirectionAsByteArray(final Direction[][][] dA, final int z){
        //zyx
        byte[][] arr = new byte[dA[0].length][dA[0][0].length];
        for(int j = 0; j < dA[z].length; j++){
            for(int k = 0; k < dA[z][j].length; k++){
                arr[j][k] = dA[z][j][k].getSymbol();
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

    public void printTilesBytes(final int w, final int h, final int d)
    {

        int length = FileCreator.X_BYTES + FileCreator.Y_BYTES + FileCreator.Z_BYTES + FileCreator.SUBLAYERS * w * h * d;

        byte[] b = new byte[length];
        //b += x + "," + y + "," + z + "\n";
        b[0] = (byte) (w / 128);
        b[1] = (byte) (w % 128);
        b[2] = (byte) (h / 128);
        b[3] = (byte) (h % 128);
        b[4] = (byte) (d / 128);
        b[5] = (byte) (d % 128);

        //b[] = [xx, yy, zz, all tiles, all dir1s, all dir2s]
        for (int i = 0; i < FileCreator.SUBLAYERS; i++) {
            for (int j = 0; j < d; j++) {
                if(i == 0){
                    for (int k = 0; k < h; k++) {
                        for (int l = 0; l < w; l++) {
                            b[6+ i * d * h * w + j * h * w + k * w + l] = breadboardByte[j][k][l];
                        }
                    }
                }else if (i == 1){
                    for (int k = 0; k < h; k++) {
                        for (int l = 0; l < w; l++) {
                            b[6+ i * d * h * w + j * h * w + k * w + l] = breadboardDirection[j][k][l].getSymbol();
                        }
                    }
                }else if (i == 2){
                    for (int k = 0; k < h; k++) {
                        for (int l = 0; l < w; l++) {
                            b[6+ i * d * h * w + j * h * w + k * w + l] = breadboardDirection2[j][k][l].getSymbol();
                        }
                    }
                }
            }
        }

        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String formattedDateTime = formatter.format(now);
        String saveName = "src/saves/" + w + "," + h + "," + d + " " + formattedDateTime + ".bin";

        try {
            FileCreator.saveToFileBytes(b, saveName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Saved breadboard to " + saveName + ".");
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
                s += ",";
            }
            s = s.substring(0, s.length() - 1);//remove the last ","
            s += "\n";
        }
        return s;
    }

}