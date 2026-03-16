package src.code;

import src.code.Digital.DigitalBreadBoardItem;
import src.code.Digital.SupaWire;
import src.code.Digital.TriStateBuffer;
import src.code.Enums.Direction;
import src.code.Enums.TState;
import src.code.Enums.TileByte;
import src.code.analogue.*;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.*;

import static src.code.Main.*;

/**
 * Class for gates made from a grid via user clicking on a tile grid.
 * Example, user clicking one square will turn that square into a wire.
 * Another example is an and circuit.
 * Another example is an led.
 */
public class BreadBoard {

    public final static Direction dR = Direction.RIGHT;
    public final static Direction dL = Direction.LEFT;
    public final static Direction dU = Direction.UP;
    public final static Direction dD = Direction.DOWN;
    public final static Direction dI = Direction.INTO;//Z-
    public final static Direction dO = Direction.OUTOF;//Z+
    public final static Direction dN = Direction.NONE;

    //short used for setting object ids in whole breadboard
    public short globalLatestID = -32768;

    //id to make teleport paths; i.e. from which teleport0 do i go from? and which teleport1 do i go to?
    //todo update
    public short bondedTeleportID = -1;//dummy value, the ID should NEVER be -1 or 0
    public short tempTeleportX = -1;
    public short tempTeleportY = -1;
    public short tempTeleportZ = -1;


    /**Empty is 0 in itemEnum*/
    private final static int EMPTY_TYPE = 0;

    private final short WIDTH;
    private final short HEIGHT;
    private final short ZHEIGHT;

    final byte TOP_Z;
    final byte BOTTOM_Z;

    final static short SIGNAL_ARRAY_LENGTH = 800;
    final static byte SIGNAL_ARRAY_D_PLACE = 0;
    final static byte SIGNAL_ARRAY_S_PLACE = 1;
    final static byte SIGNAL_ARRAY_X_PLACE = 2;
    final static byte SIGNAL_ARRAY_Y_PLACE = 3;
    final static byte SIGNAL_ARRAY_Z_PLACE = 4;
    final static byte SIGNAL_ARRAY_TICK_PLACE = 5;
    final static byte SIGNAL_ARRAY_OBJECT_PLACE = 6;//gives the object sending the signal
    final static byte SIGNAL_ARRAY_ID_PLACE = 7;//gives the id of the object
    final static byte MAX_TICKS_IN_THE_FUTURE = 127;
    final static int SIGNAL_CLEAR_RATE = 10;

    /**
     * THE width of the queueSignal array for items, which is the location fo the last input thing.
     * So if tick place is the last one and it's equal to 4, then this must be 4.
     */
    final static int SIGNAL_ARRAY_LAST_PLACE = SIGNAL_ARRAY_ID_PLACE;
    final static int SIGNAL_ARRAY_WIDTH = SIGNAL_ARRAY_LAST_PLACE + 1;

    final static String DEFAULT_KEYWORD = "DEFAULT";
    final static String EDITING_KEYWORD = "EDITING";
    final static String CUTTING_KEYWORD = "CUTTING";
    final static String COPYING_KEYWORD = "COPYING";
    final static String PASTING_KEYWORD = "PASTING";

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
    final static byte WIRE_DEAD_BYTE    = TileByte.WireDead.getSymbol();
    final static byte WIRE_OFF_BYTE     = TileByte.WireOff.getSymbol();
    final static byte WIRE_ON_BYTE      = TileByte.WireOn.getSymbol();
    final static byte DOUBLE_WIRE_BYTE  = TileByte.DoubleWire.getSymbol();
    final static byte RESISTOR_1_BYTE   = TileByte.Resistor1.getSymbol();
    final static byte RESISTOR_3_BYTE   = TileByte.Resistor3.getSymbol();
    final static byte RESISTOR_5_BYTE   = TileByte.Resistor5.getSymbol();
    final static byte RESISTOR_10_BYTE  = TileByte.Resistor10.getSymbol();
    final static byte RED_LED_ON_BYTE = TileByte.RedLEDOn.getSymbol();
    final static byte RED_LED_OFF_BYTE = TileByte.RedLEDOff.getSymbol();
    final static byte GREEN_LED_ON_BYTE = TileByte.GreenLEDOn.getSymbol();
    final static byte GREEN_LED_OFF_BYTE = TileByte.GreenLEDOff.getSymbol();
    final static byte BLUE_LED_ON_BYTE = TileByte.BlueLEDOn.getSymbol();
    final static byte BLUE_LED_OFF_BYTE = TileByte.BlueLEDOff.getSymbol();



    private String gamemode = DEFAULT_KEYWORD;
    public byte itemCursor  = 0;

    private boolean gatesAllowedToSignalOut = false;

    public List<BreadBoardItem> breadBoardItemsList = new ArrayList<>();
    /**
     * Clickable Breadboard Items List
     */
    public List<CBreadBoardItem> cBreadBoardItemsList = new ArrayList<>();

    private List<StoreBit> storeBitList = new ArrayList<>();

    public LinkedList<SupaWire> supaWireLinkedList = new LinkedList<>();

    public List<Gate> gates = new ArrayList<>();

    //300 possible teleportWires and 4 pieces of data for each
    public int[][] teleportWireInfoArray = new int[FileCreator.TOTAL_NUMBER_OF_TELEPORT_WIRES][FileCreator.INFO_PER_TELEPORT];

//    /**
//     * ===ARCHAIC JAVADOC===
//     * Okay, so there's three types of classes for our BreadBoard:
//     * Buttons,
//     * Wires,
//     * Gates (AND, OR, NOT, ETC.), and
//     * LEDs
//     *
//     * Problems::
//     * Wires (w):
//     * How will I "connect" them from one thing to another,
//     * How will I split wires,
//     *
//     * Gates (N, A, O):
//     * AND Gate
//     *
//     * NOT Gate
//     *
//     * OR Gate
//     *
//     *
//     */
//    //note: shortened (removed S and W and L
//    private byte[] itemEnumBytes = {
//            EMPTY_BYTE,
//            SWITCH_OFF_BYTE,
//            WIRE_OFF_BYTE,
//            DOUBLE_WIRE_BYTE,
//            NOT_BYTE,
//            OR_BYTE,
//            AND_BYTE,
//            LED_OFF_BYTE,
//            RESISTOR_1_BYTE,
//            RESISTOR_3_BYTE,
//            RESISTOR_5_BYTE,
//            RESISTOR_10_BYTE,
//            RED_LED_OFF_BYTE,
//            GREEN_LED_OFF_BYTE,
//            BLUE_LED_OFF_BYTE,
//            TileByte.AnalogueWire.getSymbol(),
//            TileByte.Collector.getSymbol(),
//            TileByte.Base.getSymbol(),
//            TileByte.Emitter.getSymbol(),
//            TileByte.TriStateBufferDisconnected.getSymbol(),
//            TileByte.SupaWire.getSymbol(),
//    };


    //full version
    //private String[] fullItemEnum = {"_","s", "w", "X", "N","O","A","l"};

    /**
     * May have to replace later, like changing it to an ArrayList
     */
    private Object[][] signalArray = new Object[SIGNAL_ARRAY_LENGTH][SIGNAL_ARRAY_WIDTH];//carries up to SIGNAL_ARRAY_LENGTH signals
    private Object[][] tempSignalArray = new Object[SIGNAL_ARRAY_LENGTH][SIGNAL_ARRAY_WIDTH];


    public void setSignalArrayToNull(){
        for (int i = 0; i < signalArray.length; i++) {
            signalArray[i] = null;
        }
    }

    //current breadboard stuff; different from default - I think.
    //private String[][][] breadboard;

    /**
     * 3D array which maps all the directions for the tiles in the world.
     */
    private Direction[][][] breadboardDirection;

    /**
     * 3D array which maps all the 2nd direction for the tiles in the world.
     */
    private Direction[][][] breadboardDirection2;

    /**
     * 3D array which maps all the tiles in the world.
     */
    private byte[][][] breadboardByte;
    //private Direction[][][] breadboardDirectionByte;
    //private Direction[][][] breadboardDirectionByte2;

    //archaic
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
     * =======Used for creating NEW items!!!!================
     * Converts given parameters into a new Breadboard Object
     * @param tb tile byte
     * @param d direction1
     * @param d2 direction2
     * @param x
     * @param y
     * @return proper BreadBoardItemm
     */
    private BreadBoardItem convertToTypeBytes(final byte tb, final Direction d, final Direction d2, final short x, final short y, final short z) {
        if (tb == SWITCH_OFF_BYTE) {
            return new Switch(TState.NEGATIVE, d, d2, x, y, z);
        } else if (tb == SWITCH_ON_BYTE) {
            return new Switch(TState.POSITIVE, d, d2, x, y, z);
        } else if (tb == WIRE_OFF_BYTE || tb == WIRE_ON_BYTE || tb == WIRE_DEAD_BYTE) {
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
        }else if (tb == TileByte.ButtonOff.getSymbol()) {
            return new Button(TState.NEGATIVE,d, x, y, z);
        }else if (tb == TileByte.ButtonOn.getSymbol()) {
            return new Button(TState.POSITIVE,d, x, y, z);
        }else if (tb == LED_ON_BYTE) {
            return new LED(TState.POSITIVE, d, x, y, z);
        } else if (tb == LED_OFF_BYTE) {
            return new LED(TState.NEGATIVE, d, x, y, z);
            // Resistors
        } else if (tb == RESISTOR_1_BYTE) {
            return new Resistor1(d, x, y, z);
        } else if (tb == RESISTOR_3_BYTE) {
            return new Resistor3(d, x, y, z);
        } else if (tb == RESISTOR_5_BYTE) {
            return new Resistor5(d, x, y, z);
        } else if (tb == RESISTOR_10_BYTE) {
            return new Resistor10(d, x, y, z);
        } else if (tb == TileByte.Resistor50.getSymbol()) {
            return new Resistor50(d, x, y, z);
        } else if (tb == TileByte.Resistor100.getSymbol()) {
            return new Resistor100(d, x, y, z);
        } else if (tb == RED_LED_ON_BYTE) {
            return new RedLED(TState.POSITIVE, d, x, y, z);
        } else if (tb == RED_LED_OFF_BYTE) {
            return new RedLED(TState.NEGATIVE, d, x, y, z);
        } else if (tb == GREEN_LED_ON_BYTE) {
            return new GreenLED(TState.POSITIVE, d, x, y, z);
        } else if (tb == GREEN_LED_OFF_BYTE) {
            return new GreenLED(TState.NEGATIVE, d, x, y, z);
        } else if (tb == BLUE_LED_ON_BYTE) {
            return new BlueLED(TState.POSITIVE, d, x, y, z);
        } else if (tb == BLUE_LED_OFF_BYTE) {
            return new BlueLED(TState.NEGATIVE, d, x, y, z);
        }else if (tb == TileByte.AnalogueWire.getSymbol()) {
            return new AnalogueWire(d, x, y, z);
        }else if (tb == TileByte.Collector.getSymbol()) {
            return new TCollector(d, x, y, z);
            // Resistors
        }else if (tb == TileByte.Base.getSymbol()) {
            return new TBase(d, x, y, z);
            // Resistors
        }else if (tb == TileByte.Emitter.getSymbol()) {
            return new TEmitter(d, x, y, z);
            // Resistors
        }else if (tb == TileByte.TriStateBufferDisconnected.getSymbol() ||
                  tb == TileByte.TriStateBufferConnected.getSymbol()) {
            return new TriStateBuffer(d, x, y, z);
            // Resistors
        }else if (tb == TileByte.SupaWire.getSymbol()) {
            return new SupaWire(this, d, d2, x, y, z);
        }else if (tb == TileByte.TwoByTwoLEDOff.getSymbol()){
            return new TwoByTwoLED(TState.NEGATIVE, d, x, y, z);
        }else if (tb == TileByte.TwoByTwoLEDOn.getSymbol()){
            return new TwoByTwoLED(TState.POSITIVE, d, x, y, z);
        }else if (tb == TileByte.ThreeByThreeLEDOff.getSymbol()){
            return new ThreeByThreeLED(TState.NEGATIVE, d, x, y, z);
        }else if (tb == TileByte.ThreeByThreeLEDOn.getSymbol()){
            return new ThreeByThreeLED(TState.POSITIVE, d, x, y, z);
        }else if (tb == TileByte.FourByFourLEDOff.getSymbol()){
            return new FourByFourLED(TState.NEGATIVE, d, x, y, z);
        }else if (tb == TileByte.FourByFourLEDOn.getSymbol()){
            return new FourByFourLED(TState.POSITIVE, d, x, y, z);
        }else if (tb == TileByte.Store0.getSymbol()){
            return new StoreBit(TState.NEGATIVE, d, x, y, z);
        }else if (tb == TileByte.Store1.getSymbol()){
            return new StoreBit(TState.POSITIVE, d, x, y, z);
        } else if (tb == TileByte.UpdatableWireDead.getSymbol()
                || tb == TileByte.UpdatableWireOn.getSymbol()
                || tb == TileByte.UpdatableWireOff.getSymbol()) {
            return new UpdatableWire(d, d2, x, y, z);
        } else if (tb == TileByte.TeleportWireDead.getSymbol()
                || tb == TileByte.TeleportWireOn.getSymbol()
                || tb == TileByte.TeleportWireOff.getSymbol()) {
            if(d.ordinal() == 1){
                return new TeleportWire1(d, d2, x, y, z,bondedTeleportID);
            }else {
                System.out.println("This teleportWire isn't implemented yet.");
            }
            return null;
        }else if (tb == EMPTY_BYTE) {
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
    private CBreadBoardItem convertToClickableTypeBytes(final byte tb, final Direction d, final Direction d2, final short x, final short y, final short z) {
        if (tb == SWITCH_OFF_BYTE) {
            return new Switch(TState.NEGATIVE, d, d2, x, y, z);
        } else if (tb == SWITCH_ON_BYTE) {
            return new Switch(TState.POSITIVE, d, d2, x, y, z);
        } if (tb == BUTTON_OFF_BYTE) {
            return new Button(TState.NEGATIVE, d, x, y, z);
        } else if (tb == BUTTON_ON_BYTE) {
            return new Button(TState.POSITIVE, d, x, y, z);
        }else {
            //System.out.println("cTCTB(): Unknown tile: " + tb);
            return null;
        }
    }


    /**
     * Returns the index of the current item within the breadBoardItemsList
     * if it matches the coordinates; otherwise -1
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
    private int getClickableBreadBoardItemIndexAtCoordinates(final short x, final short y, final short z) {
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
    public void changeBreadBoardBytes(final byte type, final Direction dir1,
                                    final Direction dir2, final short x, final short y, final short z){

        if(x < 0 || y < 0 || y > breadboardByte[z].length || x > breadboardByte[z][y].length){
            System.out.println("attempted to changebreadboardbytes from illegal location");
            return;
        }
        if(type == TileByte.Any.getSymbol()) {
            //any type, changes every click
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

//==============WARNING MAY NOT WORK ESPECIALLY OFF WIRE ==========================================
            for (byte index = 0; index < TileByte.values().length; index++) {
                if (breadboardByte[z][y][x] == index){
                    if (index + 1 >= TileByte.values().length) {
                        index = -1;
                    } else {
                        breadBoardItemsList.add(theOne,
                                convertToTypeBytes(index, dN,dN, x, y, z));
                    }
                    breadboardDirection[z][y][x] = dN;//call this before return, cuz of repaint in next fn
                    breadboardDirection2[z][y][x] = dN;
                    breadboardByte[z][y][x] = type;
                    return;
                }
            }
        }
        else if(type == TileByte.Empty.getSymbol()) {
            //empty
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
                if(breadBoardItemsList.get(index).getClass().equals(TeleportWire0.class)){
                    TeleportWire0 tw0 = (TeleportWire0) breadBoardItemsList.get(index);
                    for (int i = 0; i < teleportWireInfoArray[0].length; i++) {
                        teleportWireInfoArray[tw0.id * 2 + 1][i] = FileCreator.TELEPORT_INFO_DUMMY_VALUE;
                    }
                }else if(breadBoardItemsList.get(index).getClass().equals(TeleportWire1.class)){
                    TeleportWire1 tw1 = (TeleportWire1) breadBoardItemsList.get(index);
                    for (int i = 0; i < teleportWireInfoArray[0].length; i++) {
                        teleportWireInfoArray[tw1.id * 2][i] = FileCreator.TELEPORT_INFO_DUMMY_VALUE;
                    }
                }
                breadBoardItemsList.remove(index);

            }
            breadboardDirection[z][y][x] = dN;//call this before cuz of repaint in next fn
            breadboardDirection2[z][y][x] = dN;//call this before cuz of repaint in next fn
            breadboardByte[z][y][x] = type;

        }
        else {
            //anything else

            int theOne = 0;
            int theCOne = 0;

            //remove any "ClickableBreadBoardItems"
            if (getClickableBreadBoardItemIndexAtCoordinates(x, y, z) != -1){
                int index = getClickableBreadBoardItemIndexAtCoordinates(x, y, z);
                cBreadBoardItemsList.remove(index);
                theCOne = index;
            }else {
                theCOne = cBreadBoardItemsList.size();
            }
            //remove any general "BreadboardItems"
            if (getBreadBoardItemIndexAtCoordinates(x, y, z) != -1){
                int index = getBreadBoardItemIndexAtCoordinates(x, y, z);
                breadBoardItemsList.remove(index);
                theOne = index;
            }else {
                //if there is nothing to remove, that means the index should be at the end of the list.
                theOne = breadBoardItemsList.size();
            }

            if(type == TileByte.AnalogueWire.getSymbol()){

                if(placingWire){
//                    breadBoardItemsList.add(theOne,
//                            convertToTypeBytes(type, dir1, dir2, x, y, z));
//                    breadboardDirection[z][y][x] = dir1;//call this before cuz of repaint in next fn
//                    breadboardDirection2[z][y][x] = dir2;
                    convertToTypeBytes(type, dir1, dir2, x, y, z);
                    breadboardDirection[z][y][x] = dir1;//call this before cuz of repaint in next fn
                    breadboardDirection2[z][y][x] = dir2;
                    breadboardByte[z][y][x] = type;
                }else {
//                    breadBoardItemsList.add(theOne,
//                            convertToTypeBytes(type, dir1, dir2, x, y, z));
//                    breadboardDirection[z][y][x] = dir1;//call this before cuz of repaint in next fn
//                    breadboardDirection2[z][y][x] = dir2;
                    breadboardByte[z][y][x] = type;
                }


            }
            else {
                if(type != TileByte.TeleportWireDead.getSymbol()) {
                    breadBoardItemsList.add(theOne,
                            convertToTypeBytes(type, dir1, dir2, x, y, z));
                    if (convertToClickableTypeBytes(type, dir1, dir2, x, y, z) != null) {
                        cBreadBoardItemsList.add(theCOne, convertToClickableTypeBytes(type, dir1, dir2, x, y, z));
                    }
                }else {
                    if(!placingTeleportWire){//place teleportWire1 first
                        breadBoardItemsList.add(theOne,
                                convertToTypeBytes(type, Direction.fromSymbol((byte)1), dir2, x, y, z));
                        tempTeleportX = x;
                        tempTeleportY = y;
                        tempTeleportZ = z;
                    }else {//place teleportWire0 second
                        TeleportWire0 tw0 = new TeleportWire0(dir1,dir2,x,y,z,tempTeleportX,tempTeleportY,tempTeleportZ,bondedTeleportID);
                        breadBoardItemsList.add(theOne, tw0);
                        //we've bonded the two teleporters, so increase the ID!
                    }

                }

                breadboardDirection[z][y][x] = dir1;//call this before cuz of repaint in next fn
                breadboardDirection2[z][y][x] = dir2;
                breadboardByte[z][y][x] = type;


                if(type == TileByte.TwoByTwoLEDOff.getSymbol() || type == TileByte.TwoByTwoLEDOn.getSymbol() ||
                   type == TileByte.ThreeByThreeLEDOff.getSymbol() || type == TileByte.ThreeByThreeLEDOn.getSymbol() ||
                   type == TileByte.FourByFourLEDOff.getSymbol() || type == TileByte.FourByFourLEDOn.getSymbol()) {
                    final byte tempType;
                    if(type == TileByte.TwoByTwoLEDOff.getSymbol() || type == TileByte.ThreeByThreeLEDOff.getSymbol()
                    || type == TileByte.FourByFourLEDOff.getSymbol()){
                        tempType = TileByte.LEDOff.getSymbol();
                    }else {
                        tempType = TileByte.LEDOn.getSymbol();
                    }
                    breadboardDirection[z][y][x+1] = dir1;
                    breadboardDirection2[z][y][x+1] = dir2;
                    breadboardByte[z][y][x+1] = tempType;

                    breadboardDirection[z][y+1][x] = dir1;
                    breadboardDirection2[z][y+1][x] = dir2;
                    breadboardByte[z][y+1][x] = tempType;

                    breadboardDirection[z][y+1][x+1] = dir1;
                    breadboardDirection2[z][y+1][x+1] = dir2;
                    breadboardByte[z][y+1][x+1] = tempType;

                    //do the rest of the 3by3 if it's a 3by3
                    if(type == TileByte.ThreeByThreeLEDOff.getSymbol() || type == TileByte.ThreeByThreeLEDOn.getSymbol()
                    || type == TileByte.FourByFourLEDOff.getSymbol() || type == TileByte.FourByFourLEDOn.getSymbol()) {
                        breadboardDirection[z][y][x+2] = dir1;
                        breadboardDirection2[z][y][x+2] = dir2;
                        breadboardByte[z][y][x+2] = tempType;

                        breadboardDirection[z][y+1][x+2] = dir1;
                        breadboardDirection2[z][y+1][x+2] = dir2;
                        breadboardByte[z][y+1][x+2] = tempType;

                        breadboardDirection[z][y+2][x] = dir1;
                        breadboardDirection2[z][y+2][x] = dir2;
                        breadboardByte[z][y+2][x] = tempType;

                        breadboardDirection[z][y+2][x+1] = dir1;
                        breadboardDirection2[z][y+2][x+1] = dir2;
                        breadboardByte[z][y+2][x+1] = tempType;

                        breadboardDirection[z][y+2][x+2] = dir1;
                        breadboardDirection2[z][y+2][x+2] = dir2;
                        breadboardByte[z][y+2][x+2] = tempType;
                    }

                    //do the rest of the 4by4 if it's a 4by4
                    if(type == TileByte.FourByFourLEDOff.getSymbol() || type == TileByte.FourByFourLEDOn.getSymbol()) {
                        for (int i = 0; i < 4; i++) {
                            if(i < 3) {
                                breadboardDirection[z][y+i][x+3] = dir1;
                                breadboardDirection2[z][y+i][x+3] = dir2;
                                breadboardByte[z][y+i][x+3] = tempType;
                            }else {
                                for (int j = 0; j < 4; j++) {
                                    breadboardDirection[z][y+i][x+j] = dir1;
                                    breadboardDirection2[z][y+i][x+j] = dir2;
                                    breadboardByte[z][y+i][x+j] = tempType;
                                }
                            }
                        }
                    }

                    //same thing for 5by5, etc.

                }
            }
        }
    }

    /**
     * Function used to INITIALLY set the breadboard state
     * This is used so that there are no on wires initially
     * @param tiles
     * @param dir1
     * @param dir2
     */
    public void setBreadBoardStateByteInitial(
            byte[][][] tiles, Direction[][][] dir1, Direction[][][] dir2,
            byte[][] teleportWireData, short idSetNumber) {

        bondedTeleportID = idSetNumber;
        for (int i = 0; i < teleportWireData.length; i++) {
            for (int j = 0; j < teleportWireData[i].length; j++) {
                teleportWireInfoArray[i][j] = teleportWireData[i][j];
            }
        }

        System.out.println(Arrays.deepToString(teleportWireInfoArray));

        for (short z = 0; z < ZHEIGHT; z++) {
            for (short y = 0; y < HEIGHT; y++) {
                for (short x = 0; x < WIDTH; x++) {
                    byte tile = tiles[z][y][x];
                    Direction d1 = dir1[z][y][x];
                    Direction d2 = dir2[z][y][x];

//==================SET ON THINGS TO OFF INITIALLY==============================================
                    if(tile == TileByte.WireOn.getSymbol()) tile = TileByte.WireDead.getSymbol();
                    if(tile == TileByte.WireOff.getSymbol()) tile = TileByte.WireDead.getSymbol();
                    if(tile == TileByte.LEDOn.getSymbol()) tile = TileByte.LEDOff.getSymbol();
                    if(tile == TileByte.TwoByTwoLEDOn.getSymbol()) tile = TileByte.TwoByTwoLEDOff.getSymbol();
                    if(tile == TileByte.ThreeByThreeLEDOn.getSymbol()) tile = TileByte.ThreeByThreeLEDOff.getSymbol();
                    if(tile == TileByte.FourByFourLEDOn.getSymbol()) tile = TileByte.FourByFourLEDOff.getSymbol();
                    if(tile == TileByte.SwitchOn.getSymbol()) tile = TileByte.SwitchOff.getSymbol();
                    if(tile == TileByte.ButtonOn.getSymbol()) tile = TileByte.ButtonOff.getSymbol();
                    if(tile == TileByte.TriStateBufferConnected.getSymbol()) tile = TileByte.TriStateBufferDisconnected.getSymbol();
                    if(tile == TileByte.SupaWire.getSymbol()) tile = TileByte.WireDead.getSymbol();
                    if(tile == TileByte.UpdatableWireOn.getSymbol()) tile = TileByte.UpdatableWireDead.getSymbol();
                    if(tile == TileByte.UpdatableWireOff.getSymbol()) tile = TileByte.UpdatableWireDead.getSymbol();

                    breadboardByte[z][y][x] = tile;
                    //System.out.println("called setBBTB");
                    setBreadBoardDirectionTile(d1, x, y, z);
                    setBreadBoardDirection2Tile(d2, x, y, z);

                    addNewItemToBreadBoardItemsListInitiallyByte(x,y,z,d1,d2);

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

    /**
     * Function to get the id from a teleportWire already on the map
     * @param x position of tW
     * @param y position of tW
     * @param z position of tW
     * @return id of teleportWire
     */
    public short getIdFromTeleportArray(final short x, final short y, final short z) throws Exception {
        for (int i = 0; i < teleportWireInfoArray.length; i++) {
            short thisX = (short) ((teleportWireInfoArray[i][0]) * 128 + (teleportWireInfoArray[i][1]));
            short thisY = (short) ((teleportWireInfoArray[i][2]) * 128 + (teleportWireInfoArray[i][3]));
            short thisZ = (short) ((teleportWireInfoArray[i][4]) * 128 + (teleportWireInfoArray[i][5]));
            if(thisX == x && thisY == y && thisZ == z) {
                //return the ID
                return (short) ((teleportWireInfoArray[i][6]) * 128 + (teleportWireInfoArray[i][7]));
            }
        }
        //else return bogus number
        throw new Exception();
    }

    /**
     * Function to get the next block to teleport to based off the id
     * @param teleportNumber if the PREVIOUS block is the 0th block, 1st block etc.
     *                       teleportation goes from block 0, to 1, etc. up to around 7
     *                       (since this value is stored in the direction)
     * @return id of teleportWire
     */
    public short[] getPositionFromTeleportArray(final byte teleportNumber, final short id) throws Exception {
        //System.out.println("\npp" + Arrays.deepToString(teleportWireInfoArray));
        for (int i = 0; i < teleportWireInfoArray.length; i++) {
            short thisId = (short) ((teleportWireInfoArray[i][6]) * 128 + (teleportWireInfoArray[i][7]));
            if(thisId == id) {
                int x = (int) ((teleportWireInfoArray[i][0]) * 128 + (teleportWireInfoArray[i][1]));
                int y = (int) ((teleportWireInfoArray[i][2]) * 128 + (teleportWireInfoArray[i][3]));
                int z = (int) ((teleportWireInfoArray[i][4]) * 128 + (teleportWireInfoArray[i][5]));
                if(teleportNumber == 0) {
                    if(getBreadBoardItemOnBoardFromCoordinates(x,y,z).getClass().equals(TeleportWire1.class)) {
                        return new short[]{(short) x, (short) y, (short) z};
                    }
                }else {
                    System.out.println("stop searching for portal 2 bruv");
                }
            }
        }
        //else return bogus number
        throw new Exception();
    }


    /**
     * Function used to INITIALLY ADD THE STUFF TO THE BREADBOARD
     * since it's initial, all things should be dead; not negative, nor positive
     * @param x
     * @param y
     * @param z
     * @param d
     * @param d2
     */
    public void addNewItemToBreadBoardItemsListInitiallyByte(final short x, final short y, final short z, final Direction d, final Direction d2) {

        switch (TileByte.fromSymbol(breadboardByte[z][y][x])) {
            case ButtonOn, ButtonOff:
                breadBoardItemsList.add(new Button(TState.DEAD, d, x, y, z));
                cBreadBoardItemsList.add(new Button(TState.DEAD, d, x, y, z));
                break;
            case SwitchOn, SwitchOff:
                breadBoardItemsList.add(new Switch(TState.DEAD, d, d2, x, y, z));
                cBreadBoardItemsList.add(new Switch(TState.DEAD, d, d2, x, y, z));
                break;
            case LEDOff, LEDOn:
                breadBoardItemsList.add(new LED(TState.DEAD, d, x, y, z));
                break;
            case WireOff, WireOn, WireDead:
                breadBoardItemsList.add(new Wire(d, d2, x, y, z));
                break;
            case And:
                breadBoardItemsList.add(new And(d, x, y, z));
                break;
            case Not:
                breadBoardItemsList.add(new Not(d, x, y, z));
                break;
            case Or:
                breadBoardItemsList.add(new Or(d, x, y, z));
                break;
            case DoubleWire:
                breadBoardItemsList.add(new DoubleWire(d, d2, x, y, z));
                break;
            case Resistor1:
                breadBoardItemsList.add(new Resistor1(d, x, y, z));
                break;
            case Resistor3:
                breadBoardItemsList.add(new Resistor3(d, x, y, z));
                break;
            case Resistor5:
                breadBoardItemsList.add(new Resistor5(d, x, y, z));
                break;
            case Resistor10:
                breadBoardItemsList.add(new Resistor10(d, x, y, z));
                break;
            case Resistor50:
                breadBoardItemsList.add(new Resistor50(d, x, y, z));
                break;
            case Resistor100:
                breadBoardItemsList.add(new Resistor100(d, x, y, z));
                break;
            case Xor:
                breadBoardItemsList.add(new Xor(d, x, y, z));
                break;
            case RedLEDOff, RedLEDOn:
                breadBoardItemsList.add(new RedLED(TState.DEAD, d, x, y, z));
                break;
            case GreenLEDOff, GreenLEDOn:
                breadBoardItemsList.add(new GreenLED(TState.DEAD, d, x, y, z));
                break;
            case BlueLEDOff, BlueLEDOn:
                breadBoardItemsList.add(new BlueLED(TState.DEAD, d, x, y, z));
                break;
            case AnalogueWire:
                breadBoardItemsList.add(new AnalogueWire(d, x, y, z));
                break;
            case Collector:
                //add this if the separate items doesn't work
                //breadBoardItemsList.add(new Transistor(d, x, y, z));
                breadBoardItemsList.add(new TCollector(d, x, y, z));
                break;
            case Base:
                //add this if the separate items doesn't work
                //breadBoardItemsList.add(new Transistor(d, x, y, z));
                breadBoardItemsList.add(new TBase(d, x, y, z));
                break;
            case Emitter:
                //add this if the separate items doesn't work
                //breadBoardItemsList.add(new Transistor(d, x, y, z));
                breadBoardItemsList.add(new TEmitter(d, x, y, z));
                break;
            case TriStateBufferDisconnected, TriStateBufferConnected:
                breadBoardItemsList.add(new TriStateBuffer(d, x, y, z));
                break;
            case SupaWire:
                breadBoardItemsList.add(new SupaWire(this, d, d2, x, y, z));
                break;
            case TwoByTwoLEDOff, TwoByTwoLEDOn:
                breadBoardItemsList.add(new TwoByTwoLED(TState.DEAD, d, x, y, z));
                break;
            case ThreeByThreeLEDOff, ThreeByThreeLEDOn:
                breadBoardItemsList.add(new ThreeByThreeLED(TState.DEAD, d, x, y, z));
                break;
            case FourByFourLEDOff, FourByFourLEDOn:
                breadBoardItemsList.add(new FourByFourLED(TState.DEAD, d, x, y, z));
                break;
            case Store0://IMPORTANT THAT THESE ARE NOT DEAD! THESE ARE MEANT FOR MEMORY PURPOSES!
                //THINK OF IT LIKE A OPTICAL DISC DIVOT, IT CAN BE 1 OR 0 AND IT IS SAVED.
                StoreBit sb = new StoreBit(TState.NEGATIVE, d, x, y, z);
                breadBoardItemsList.add(sb);
                storeBitList.add(sb);
                break;
            case Store1:
                StoreBit sb1 = new StoreBit(TState.POSITIVE, d, x, y, z);
                breadBoardItemsList.add(sb1);
                storeBitList.add(sb1);
                break;
            case UpdatableWireDead,UpdatableWireOff,UpdatableWireOn:
                UpdatableWire uw = new UpdatableWire(d, d2, x, y, z);
                breadBoardItemsList.add(uw);//also check if the uWires array works!
                break;
            case TeleportWireDead,TeleportWireOff,TeleportWireOn:
                TeleportWire tw = null;
                short id = 0;
                try {
                    id = getIdFromTeleportArray(x,y,z);
                } catch (Exception e) {
                    System.out.println("trying to find tpwire at " +x+" "+y+" "+z);
                    throw new RuntimeException(e);
                }
                if(id == -127){
                    System.out.println("LOLOL");
                }else {
                    byte teleportNumber = (byte) breadboardDirection[z][y][x].ordinal();
                    System.out.println("id " + id + " tp " + teleportNumber);
                    if (teleportNumber == 0) {
                        try {
                            short nextX = getPositionFromTeleportArray(teleportNumber, id)[0];
                            short nextY = getPositionFromTeleportArray(teleportNumber, id)[1];
                            short nextZ = getPositionFromTeleportArray(teleportNumber, id)[2];
                            tw = new TeleportWire0(Direction.fromSymbol((byte)0), d2, x, y, z, nextX, nextY, nextZ, id);
                        }catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                    }else if(teleportNumber == 1) {
                        tw = new TeleportWire1(Direction.fromSymbol((byte)1), d2, x, y, z, id);
                    }
                    if (tw != null) {
                        breadBoardItemsList.add(tw);//also check if the uWires array works!
                    }
                }
                break;
            case Empty:
                break;
            default:
                System.out.println("ERROR LOADING TILES IN addNewItemToBreadBoardItemsListInitiallyByte");
                break;
        }

    }

    /**
     * Used if you don't want things to start off as dead, to "save" the previous state so to speak.
     * @param x
     * @param y
     * @param z
     * @param d
     * @param d2
     */
    public void addNewItemToBreadBoardItemsListInitiallyNotDead(final short x, final short y, final short z, final Direction d, final Direction d2) {

        switch (TileByte.fromSymbol(breadboardByte[z][y][x])) {
            case ButtonOn:
                breadBoardItemsList.add(new Button(TState.POSITIVE, d, x, y, z));
                cBreadBoardItemsList.add(new Button(TState.POSITIVE, d, x, y, z));
                break;
            case ButtonOff:
                breadBoardItemsList.add(new Button(TState.NEGATIVE, d, x, y, z));
                cBreadBoardItemsList.add(new Button(TState.NEGATIVE, d, x, y, z));
                break;
            case SwitchOn:
                breadBoardItemsList.add(new Switch(TState.POSITIVE, d, d2, x, y, z));
                cBreadBoardItemsList.add(new Switch(TState.POSITIVE, d, d2, x, y, z));
                break;
            case SwitchOff:
                breadBoardItemsList.add(new Switch(TState.NEGATIVE, d, d2, x, y, z));
                cBreadBoardItemsList.add(new Switch(TState.NEGATIVE, d, d2, x, y, z));
                break;
            case LEDOn:
                breadBoardItemsList.add(new LED(TState.POSITIVE, d, x, y, z));
                break;
            case LEDOff:
                breadBoardItemsList.add(new LED(TState.NEGATIVE, d, x, y, z));
                break;
            case WireOff, WireOn, WireDead:
                breadBoardItemsList.add(new Wire(d, d2, x, y, z));
                break;
            case And:
                breadBoardItemsList.add(new And(d, x, y, z));
                break;
            case Not:
                breadBoardItemsList.add(new Not(d, x, y, z));
                break;
            case Or:
                breadBoardItemsList.add(new Or(d, x, y, z));
                break;
            case DoubleWire:
                breadBoardItemsList.add(new DoubleWire(d, d2, x, y, z));
                break;
            case Resistor1:
                breadBoardItemsList.add(new Resistor1(d, x, y, z));
                break;
            case Resistor3:
                breadBoardItemsList.add(new Resistor3(d, x, y, z));
                break;
            case Resistor5:
                breadBoardItemsList.add(new Resistor5(d, x, y, z));
                break;
            case Resistor10:
                breadBoardItemsList.add(new Resistor10(d, x, y, z));
                break;
            case Resistor50:
                breadBoardItemsList.add(new Resistor50(d, x, y, z));
                break;
            case Resistor100:
                breadBoardItemsList.add(new Resistor100(d, x, y, z));
                break;
            case Xor:
                breadBoardItemsList.add(new Xor(d, x, y, z));
                break;
            case RedLEDOn:
                breadBoardItemsList.add(new RedLED(TState.POSITIVE, d, x, y, z));
                break;
            case RedLEDOff:
                breadBoardItemsList.add(new RedLED(TState.NEGATIVE, d, x, y, z));
                break;
            case GreenLEDOn:
                breadBoardItemsList.add(new GreenLED(TState.POSITIVE, d, x, y, z));
                break;
            case GreenLEDOff:
                breadBoardItemsList.add(new GreenLED(TState.NEGATIVE, d, x, y, z));
                break;
            case BlueLEDOn:
                breadBoardItemsList.add(new BlueLED(TState.POSITIVE, d, x, y, z));
                break;
            case BlueLEDOff:
                breadBoardItemsList.add(new BlueLED(TState.NEGATIVE, d, x, y, z));
                break;
            case AnalogueWire:
                breadBoardItemsList.add(new AnalogueWire(d, x, y, z));
                break;
            case Collector:
                breadBoardItemsList.add(new TCollector(d, x, y, z));
                break;
            case Base:
                breadBoardItemsList.add(new TBase(d, x, y, z));
                break;
            case Emitter:
                breadBoardItemsList.add(new TEmitter(d, x, y, z));
                break;
            case TriStateBufferDisconnected, TriStateBufferConnected:
                breadBoardItemsList.add(new TriStateBuffer(d, x, y, z));
                break;
            case SupaWire:
                breadBoardItemsList.add(new SupaWire(this, d, d2, x, y, z));
                break;
            case TwoByTwoLEDOn:
                breadBoardItemsList.add(new TwoByTwoLED(TState.POSITIVE, d, x, y, z));
                break;
            case TwoByTwoLEDOff:
                breadBoardItemsList.add(new TwoByTwoLED(TState.NEGATIVE, d, x, y, z));
                break;
            case ThreeByThreeLEDOn:
                breadBoardItemsList.add(new ThreeByThreeLED(TState.POSITIVE, d, x, y, z));
                break;
            case ThreeByThreeLEDOff:
                breadBoardItemsList.add(new ThreeByThreeLED(TState.NEGATIVE, d, x, y, z));
                break;
            case FourByFourLEDOn:
                breadBoardItemsList.add(new FourByFourLED(TState.POSITIVE, d, x, y, z));
                break;
            case FourByFourLEDOff:
                breadBoardItemsList.add(new FourByFourLED(TState.NEGATIVE, d, x, y, z));
                break;
            case Store0:
                breadBoardItemsList.add(new StoreBit(TState.NEGATIVE, d, x, y, z));
                break;
            case Store1:
                breadBoardItemsList.add(new StoreBit(TState.POSITIVE, d, x, y, z));
                break;
            case Empty:
                break;
            default:
                System.out.println("ERROR LOADING TILES IN addNewItemToBreadBoardItemsListInitiallyNotDead");
                break;
        }
    }



    public BreadBoard(final short width, final short height, final short zHeight) {
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
     * Function to initially output the store bits used as memory cells
     * So far it works.
     */
    public void outputStores() {

        for (StoreBit sb: storeBitList) {
            sb.signal(tickNumber);
        }
    }

    /**
     * Checks clicks and calls update breadboard
     * two different modes; first:
     * checks the click within the boundary of a tile within the Item list
     * @param x
     * @param y
     * @return whether the program should repaint or not
     */
    public void checkClick(final MouseEvent e,
                              final short x, final short y,
                              final short layer) {

        //System.out.println("checkClick at " + x + ", " + y);
        if(gamemode.equals(DEFAULT_KEYWORD))
        {
            for(CBreadBoardItem cbi:cBreadBoardItemsList)
            {
                //check if it is within the width and height of the clickableBreadBoardItem
                if(x >= cbi.getX() * MyGameScreen.tileWidth &&
                        x < cbi.getX() * MyGameScreen.tileWidth + MyGameScreen.tileWidth
                        && y >= cbi.getY() * MyGameScreen.tileHeight &&
                        y < cbi.getY() * MyGameScreen.tileHeight + MyGameScreen.tileHeight
                        && layer == cbi.getZ())
                {
                    cbi.set();
                    breadboardByte[cbi.getZ()][cbi.getY()][cbi.getX()] = cbi.returnTile();
                    return;
                }
            }
        }
        else if (gamemode.equals(EDITING_KEYWORD)) {
            if (!getCopying() && !getCutting()) {
                //System.out.println("mouse click:" + e.getButton());
                short tile_x = (short) (x / MyGameScreen.tileWidth);
                short tile_y = (short) (y / MyGameScreen.tileHeight);

                if (tile_x >= 0 && tile_x < MyGameScreen.xPixels
                        && tile_y >= 0 && tile_y < MyGameScreen.yPixels)
                {//within bounds
                    if (e.getButton() == MouseEvent.BUTTON3) {//right click
                        changeBreadBoardBytes(TileByte.Empty.getSymbol(), dN, dN, tile_x, tile_y, layer);//0 is empty space
                    }
                    else if (e.getButton() == MouseEvent.BUTTON1) {//left click
                        if(itemCursor == TileByte.AnalogueWire.getSymbol()){
                            if(placingWire){
                                //Main.placingWire = true;
                            }else{
                                //Main.placingWire = true;
                            }
                        }
                        else if(itemCursor == TileByte.Collector.getSymbol()){
                            placeTransistor(itemCursor, tile_x,tile_y,layer);
                        }else if(itemCursor == TileByte.TeleportWireDead.getSymbol()){
                            if(!placingTeleportWire) {
                                //place ending teleportWire1 first
                                bondedTeleportID++;
                                changeBreadBoardBytes(
                                        itemCursor,
                                        Direction.fromSymbol((byte)1),
                                        getTheSecondDirection(),
                                        tile_x, tile_y, layer);
                                placingTeleportWire = true;
                                TeleportWire1 tw = (TeleportWire1)getBreadBoardItemOnBoardFromCoordinates(tile_x, tile_y, layer);
                                tw.updateArray();
                            }else {
                                //place starting teleportWire0 last

                                changeBreadBoardBytes(
                                        itemCursor,
                                        Direction.fromSymbol((byte)0),
                                        getTheSecondDirection(),
                                        tile_x, tile_y, layer);
                                placingTeleportWire = false;
                                TeleportWire0 tw = (TeleportWire0)getBreadBoardItemOnBoardFromCoordinates(tile_x, tile_y, layer);
                                tw.updateArray();
                            }
                        }
                        else {//everything else

                            changeBreadBoardBytes(
                                    itemCursor,
                                    getTheDirection(),
                                    getTheSecondDirection(),
                                    tile_x, tile_y, layer);
                        }
                    }
                    else if (e.getButton() == MouseEvent.NOBUTTON) {
                        // dragging ======= MAY HAVE TO CHANGE ========
                        if(mouseClickNumber[0] == MouseEvent.BUTTON1)
                        {//drag left click
                            if(itemCursor != TileByte.Collector.getSymbol()) {
                                changeBreadBoardBytes(
                                        itemCursor,
                                        getTheDirection(),
                                        getTheSecondDirection(),
                                        tile_x, tile_y, layer);
                            }
                        }else if (mouseClickNumber[0] == MouseEvent.BUTTON3)
                        {//drag right click
                            //return changeBreadBoard(EMPTY_TYPE, dN, dN, tile_x, tile_y, layer);
                            changeBreadBoardBytes(TileByte.Empty.getSymbol(), dN, dN, tile_x, tile_y, layer);
                        }
                    }
                }
            }
        }else if (gamemode.equals(COPYING_KEYWORD) || gamemode.equals(CUTTING_KEYWORD))
        {//copying and pasting
            int sX = (int)((mouseX[0] - SCREEN_X_OFFSET + DEFAULT_SCREEN_X_OFFSET) / MyGameScreen.tileWidth);
            if(sX < SELECTION_BIAS_X) sX = 0 - SELECTION_BIAS_X;

            int sY = (int)((mouseY[0] - SCREEN_Y_OFFSET + DEFAULT_SCREEN_Y_OFFSET) / MyGameScreen.tileHeight);
            if(sY < SELECTION_BIAS_Y) sY = 0 - SELECTION_BIAS_Y;

            int eX = (int)((mouseX[1] - SCREEN_X_OFFSET + DEFAULT_SCREEN_X_OFFSET) / MyGameScreen.tileWidth);
            if(eX > MyGameScreen.xPixels) eX = MyGameScreen.xPixels;

            int eY = (int)((mouseY[1] - SCREEN_Y_OFFSET + DEFAULT_SCREEN_Y_OFFSET) / MyGameScreen.tileHeight);
            if(eY > MyGameScreen.yPixels) eY = MyGameScreen.yPixels;



            if(getAllLayers()) {
                System.out.println(
                        "selecting entities from: " + (sX + SELECTION_BIAS_X) + ", " + (sY + SELECTION_BIAS_Y) + ", " + BOTTOM_Z
                        + eX + ", " + eY + ", " + TOP_Z);
                selectEntities(sX+ SELECTION_BIAS_X, sY+ SELECTION_BIAS_Y, BOTTOM_Z, eX, eY, TOP_Z);
            }else {
                System.out.println(
                        "selecting entities from: " + (sX + SELECTION_BIAS_X) + ", " + (sY + SELECTION_BIAS_Y) + ", " + LOGIC_SCREEN_LAYER + " to "
                                + eX + ", " + eY + ", " + (LOGIC_SCREEN_LAYER + 1));
                selectEntities(sX+ SELECTION_BIAS_X, sY+ SELECTION_BIAS_Y, LOGIC_SCREEN_LAYER, eX, eY, LOGIC_SCREEN_LAYER + 1);

            }
//            if(gamemode.equals(CUTTING_KEYWORD)) {
//
//            }

        }
    }

    /**
     * Function used to edit the actual tiles in the breadboard array
     * @param itemCursor
     * @param tile_x
     * @param tile_y
     * @param layer
     */
    private void placeTransistor(final byte itemCursor, final short tile_x, final short tile_y, final short layer) {
        if(getTheDirection() == Direction.RIGHT || getTheDirection() == Direction.NONE) {
            //we want a transistor, made up of a base collector and emitter, going from left to right
            //made in reverse order for the variables inside the classes (i.e. tBase in TCollector)
            changeBreadBoardBytes(
                    (byte)(itemCursor+2),
                    Direction.RIGHT,
                    Direction.NONE,
                    (short)(tile_x+2), tile_y, layer);
            changeBreadBoardBytes(
                    (byte)(itemCursor+1),
                    Direction.RIGHT,
                    Direction.NONE,
                    (short)(tile_x+1), tile_y, layer);
            changeBreadBoardBytes(
                    itemCursor,
                    Direction.RIGHT,
                    Direction.NONE,
                    tile_x, tile_y, layer);


        }else if(getTheDirection() == Direction.LEFT) {
            //we want a transistor, made up of a base collector and emitter, going from right to left
            changeBreadBoardBytes(
                    (byte)(itemCursor+2),
                    Direction.LEFT,
                    Direction.NONE,
                    (short)(tile_x-2), tile_y, layer);
            changeBreadBoardBytes(
                    (byte)(itemCursor+1),
                    Direction.LEFT,
                    Direction.NONE,
                    (short)(tile_x-1), tile_y, layer);
            changeBreadBoardBytes(
                    itemCursor,
                    Direction.LEFT,
                    Direction.NONE,
                    tile_x, tile_y, layer);
        }else if(getTheDirection() == Direction.DOWN) {
            //we want a transistor, made up of a base collector and emitter, going from right to left
            changeBreadBoardBytes(
                    (byte)(itemCursor+2),
                    Direction.DOWN,
                    Direction.NONE,
                    tile_x, (short)(tile_y+2), layer);
            changeBreadBoardBytes(
                    (byte)(itemCursor+1),
                    Direction.DOWN,
                    Direction.NONE,
                    tile_x, (short)(tile_y+1), layer);
            changeBreadBoardBytes(
                    itemCursor,
                    Direction.DOWN,
                    Direction.NONE,
                    tile_x, tile_y, layer);
        }else if(getTheDirection() == Direction.UP) {
            //we want a transistor, made up of a base collector and emitter, going from right to left
            changeBreadBoardBytes(
                    (byte)(itemCursor+2),
                    Direction.UP,
                    Direction.NONE,
                    tile_x, (short)(tile_y-2), layer);
            changeBreadBoardBytes(
                    (byte)(itemCursor+1),
                    Direction.UP,
                    Direction.NONE,
                    tile_x, (short)(tile_y-1), layer);
            changeBreadBoardBytes(
                    itemCursor,
                    Direction.UP,
                    Direction.NONE,
                    tile_x, tile_y, layer);

        }
    }

    /**
     * Selects entities and tiles within a given space
     * Used within cutting and copying
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
        getMyGameScreen().tempCutCopyPasteBoardList.clear();
        getMyGameScreen().tempCutCopyPasteBoardDirection1List.clear();
        getMyGameScreen().tempCutCopyPasteBoardDirection2List.clear();
        //set the board to the actual breadboard pieces
        for(int i = 0; i < dZ; i++) {
            getMyGameScreen().tempCutCopyPasteBoardList.add(new ArrayList<>());
            getMyGameScreen().tempCutCopyPasteBoardDirection1List.add(new ArrayList<>());
            getMyGameScreen().tempCutCopyPasteBoardDirection2List.add(new ArrayList<>());

            for (int j = 0; j < dY; j++) {
                getMyGameScreen().tempCutCopyPasteBoardList.get(i).add(new ArrayList<>());
                getMyGameScreen().tempCutCopyPasteBoardDirection1List.get(i).add(new ArrayList<>());
                getMyGameScreen().tempCutCopyPasteBoardDirection2List.get(i).add(new ArrayList<>());

                for (int k = 0; k < dX; k++) {
                    tempCutCopyPasteBoard[i][j][k] = breadboardByte[i + sZ][j + sY][k + sX];
                    tempCutCopyPasteBoardDir1[i][j][k] = breadboardDirection[i + sZ][j + sY][k + sX];
                    tempCutCopyPasteBoardDir2[i][j][k] = breadboardDirection2[i + sZ][j + sY][k + sX];

                    getMyGameScreen().tempCutCopyPasteBoardList.get(i).get(j).add(tempCutCopyPasteBoard[i][j][k]);
                    getMyGameScreen().tempCutCopyPasteBoardDirection1List.get(i).get(j).add(tempCutCopyPasteBoardDir1[i][j][k]);
                    getMyGameScreen().tempCutCopyPasteBoardDirection2List.get(i).get(j).add(tempCutCopyPasteBoardDir2[i][j][k]);
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
    public void eraseRegion(short sX, short sY, short sZ, short eX, short eY, short eZ) {
        System.out.println("eraseRegion(): Erasing region: " + sX + "," + sY + "," + sZ + " to " + eX + "," + eY + "," + eZ);
        for (short z = sZ; z < eZ; z++) {
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
            }else {//rotate the second output
                if (breadboardByte[z][y][x] != -1) {

                    if (breadboardByte[z][y][x] == TileByte.DoubleWire.getSymbol()) {

                        DoubleWire dw = (DoubleWire) getBreadBoardItemOnBoardFromCoordinates(x,y, z);
                        int dirNum = breadboardDirection2[z][y][x].ordinal() + 1;
                        if (dirNum >= Direction.values().length) dirNum = 0;
                        breadboardDirection2[z][y][x] = Direction.values()[dirNum];
                        //to do: implement safety measure so that user cannot make d2 the opposite of d1
                        dw.setDir2(Direction.values()[dirNum]);

                    }else if (breadboardByte[z][y][x] == TileByte.WireOn.getSymbol()
                           || breadboardByte[z][y][x] == TileByte.WireOff.getSymbol()
                           || breadboardByte[z][y][x] == TileByte.WireDead.getSymbol())
                    {

                        Wire w = (Wire) getBreadBoardItemOnBoardFromCoordinates(x,y, z);
                        int dirNum = breadboardDirection2[z][y][x].ordinal() + 1;
                        if (dirNum >= Direction.values().length) dirNum = 0;
                        breadboardDirection2[z][y][x] = Direction.values()[dirNum];
                        //to do: implement safety measure so that user cannot make d2 the opposite of d1
                        w.setDir2(Direction.values()[dirNum]);

                    }else if (breadboardByte[z][y][x] == TileByte.SwitchOff.getSymbol()
                           || breadboardByte[z][y][x] == TileByte.SwitchOn.getSymbol()){
                        Switch s = (Switch) getBreadBoardItemOnBoardFromCoordinates(x,y, z);
                        int dirNum = breadboardDirection2[z][y][x].ordinal() + 1;
                        if (dirNum >= Direction.values().length) dirNum = 0;
                        breadboardDirection2[z][y][x] = Direction.values()[dirNum];
                        s.setDir2(Direction.values()[dirNum]);
                    }else if (breadboardByte[z][y][x] == TileByte.UpdatableWireOn.getSymbol()
                            || breadboardByte[z][y][x] == TileByte.UpdatableWireOff.getSymbol()
                            || breadboardByte[z][y][x] == TileByte.UpdatableWireDead.getSymbol())
                    {
                        UpdatableWire uw = (UpdatableWire) getBreadBoardItemOnBoardFromCoordinates(x,y, z);
                        int dirNum = breadboardDirection2[z][y][x].ordinal() + 1;
                        if (dirNum >= Direction.values().length) dirNum = 0;
                        breadboardDirection2[z][y][x] = Direction.values()[dirNum];
                        //to do: implement safety measure so that user cannot make d2 the opposite of d1
                        uw.setDir2(Direction.values()[dirNum]);
                    }else if (breadboardByte[z][y][x] == TileByte.TeleportWireOn.getSymbol()
                            || breadboardByte[z][y][x] == TileByte.TeleportWireOff.getSymbol()
                            || breadboardByte[z][y][x] == TileByte.TeleportWireDead.getSymbol())
                    {
                        TeleportWire tw = (TeleportWire) getBreadBoardItemOnBoardFromCoordinates(x,y, z);
                        int dirNum = breadboardDirection2[z][y][x].ordinal() + 1;
                        if (dirNum >= Direction.values().length) dirNum = 0;
                        breadboardDirection2[z][y][x] = Direction.values()[dirNum];
                        //to do: implement safety measure so that user cannot make d2 the opposite of d1
                        tw.setDir2(Direction.values()[dirNum]);
                    }
                    callPaint();
                }
            }
        }
    }

    /**
     * Function to move the screen using WASD or the arrows
     */
    public void takeCareOfWASD() {
        if (keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP]) {
            getMyGameScreen().yOffset += MyGameScreen.tileSize;
            SCREEN_Y_OFFSET = getMyGameScreen().yOffset;
            getMyGameScreen().repaint();
        }else if ((keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN])
                    && !keys[KeyEvent.VK_CONTROL] && !keys[KeyEvent.VK_SHIFT]) {
            getMyGameScreen().yOffset -= MyGameScreen.tileSize;
            SCREEN_Y_OFFSET = getMyGameScreen().yOffset;
            getMyGameScreen().repaint();
        }if (keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT]) {
            getMyGameScreen().xOffset -= MyGameScreen.tileSize;
            SCREEN_X_OFFSET = getMyGameScreen().xOffset;
            getMyGameScreen().repaint();
        }else if (keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT]) {
            getMyGameScreen().xOffset += MyGameScreen.tileSize;
            SCREEN_X_OFFSET = getMyGameScreen().xOffset;
            getMyGameScreen().repaint();
        }
        //not here because it will call every tick
        //Main.getMyGameScreen().repaint();
    }

    /**
     * Ticks
     */
    public void tick(final int tickNo) {

//=========================WARNING: HOKEY FIX!====================================
        //clear the gates array every SIGNAL_CLEAR_RATE ticks
        if(tickNo % SIGNAL_CLEAR_RATE == 0) {
            gates.clear();
        }

        short i = 0;
        Object[][] signalArrayOnCall = new Object[signalArray.length][];
        for (short j = 0; j < signalArray.length; j++) {
            if (signalArray[j] != null) {
                signalArrayOnCall[j] = Arrays.copyOf(signalArray[j], signalArray[j].length);
            }
        }
        //System.out.println("tick(): queueSignal at tick " + Arrays.deepToString(signalArrayOnCall));
        while (i < signalArrayOnCall.length) {
            if (signalArrayOnCall[i] != null) {
                Direction d = (Direction) signalArrayOnCall[i][SIGNAL_ARRAY_D_PLACE];
                TState s = (TState) signalArrayOnCall[i][SIGNAL_ARRAY_S_PLACE];
                short x = ((Integer) signalArrayOnCall[i][SIGNAL_ARRAY_X_PLACE]).shortValue();
                short y = ((Integer) signalArrayOnCall[i][SIGNAL_ARRAY_Y_PLACE]).shortValue();
                byte  z = ((Integer) signalArrayOnCall[i][SIGNAL_ARRAY_Z_PLACE]).byteValue();
                /**
                 * Tick that the queueSignal is supposed to be propagated
                 */
                int tickToBe = (int)signalArrayOnCall[i][SIGNAL_ARRAY_TICK_PLACE];
                byte type = (byte)signalArrayOnCall[i][SIGNAL_ARRAY_OBJECT_PLACE];
                short id = ((Integer) signalArrayOnCall[i][SIGNAL_ARRAY_ID_PLACE]).shortValue();

                if(type != TileByte.TeleportWireOn.getSymbol()
                && type != TileByte.TeleportWireOff.getSymbol()) {

                    propagateSignal(d, s, x, y, z, tickToBe, type);
                }else {
                    propagateTeleportSignal(d,s,x,y,z,tickToBe,type,id);
                }

//                System.out.println(
//                        "tick(): gonna propagate " + i +
//                        "th queueSignal from " +
//                        x + " " + y + " " + z +
//                        " on tick" + tickToBe);
                //System.out.println("tick(): called propagateSignal with the " + i + "th queueSignal");

            }
            i++;
        }

        gatesAllowedToSignalOut = true;
        //==================EXPERIMENTAL===========================
        if(tickNo % SIGNAL_CLEAR_RATE == 0) {
            signalGates(tickNo,true);
            if(tickNo % (SIGNAL_CLEAR_RATE * SIGNAL_CLEAR_RATE) == 0) {
                UpdatableWire.update(tickNo);
            }
        }else {
            signalGates(tickNo,false);
        }

        //clear fo real, THIS STEP IS NECESSARY DO NOT DELETE
        for (int ind = 0; ind < signalArray.length; ind++) {
//====================CHANGE "<=" TO "<" IF THERE ARE PROBLEMS ================================================
            if (signalArray[ind] != null && (int) signalArray[ind][SIGNAL_ARRAY_TICK_PLACE] <= tickNumber) {
                signalArray[ind] = null;
            }
        }
//        System.out.println("aft tick clear"+Arrays.deepToString(signalArray));

        int writeIndex = 0;
        for (int ind = 0; ind < signalArray.length; ind++) {
            if (signalArray[ind] != null && (int) signalArray[ind][SIGNAL_ARRAY_TICK_PLACE] >= tickNumber) {
                signalArray[writeIndex] = signalArray[ind];
                writeIndex++;
            }
        }
//        System.out.println("aft tick writeindex"+Arrays.deepToString(signalArray));// + "\n");

//========THIS STEP IS NECESSARY, IF IT ISN'T HERE THEN THERE ARE DUPLICATES OF EVERYTHING!=====
        for (int ind = writeIndex; ind < signalArray.length; ind++) {
            signalArray[ind] = null;
        }
//        System.out.println("aft tick null"+Arrays.deepToString(signalArray)+"\n");
    }

    /**
     * Calls Mains getMyFrame's object's repaint.
     */
    private void callPaint(){
        getMyFrame().repaint();
    }

    /**
     * same as breadboardByte[z][y][x] = tile;<br><br>
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


    private void setWiresAndLeds(final TState s, final short dx, final short dy, final short dz, final short x, final short y, final int z, final int t) {
        //System.out.println("called setWiresAndLeds going with dx " + dx + " dy " + dy + " dz " + dz + " going to x " + x + " y " + y + " z " + z);
        //String tile = breadboard[z][y][x];
        byte tile = breadboardByte[z][y][x];

        if (s.equals(TState.POSITIVE)) {
            if (tile == WIRE_OFF_BYTE || tile == WIRE_DEAD_BYTE){ //|| tile == WIRE_ON_BYTE){
                BreadBoardItem item = getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                if (item instanceof Wire wire) {
                    wire.acceptInputsAndSetOut(dx, dy, dz, TState.POSITIVE, t);
                } else {
                    System.out.println("Expected Wire at " + x + "," + y + "," + z + " but got null or different type.");
                }
            }
            else if (tile == TileByte.UpdatableWireOff.getSymbol() || tile == TileByte.UpdatableWireDead.getSymbol()){ //|| tile == WIRE_ON_BYTE){
                BreadBoardItem item = getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                if (item instanceof UpdatableWire uWire) {
                    uWire.acceptInputsAndSetOut(dx, dy, dz, TState.POSITIVE, t);
                } else {
                    System.out.println("Expected UpdatableWire at " + x + "," + y + "," + z + " but got null or different type.");
                }
            }
            else if (tile == TileByte.TeleportWireOff.getSymbol() || tile == TileByte.TeleportWireDead.getSymbol()){ //|| tile == WIRE_ON_BYTE){
                BreadBoardItem item = getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                if (item instanceof TeleportWire0 tWire) {
                    //System.out.println("calling tWrie.acceptInputsAndSetOut from setWiresAndLeds");
                    tWire.acceptInputsAndSetOut(dx, dy, dz, TState.POSITIVE, t);
                } else {
                    System.out.println("Expected TeleportWire0 at " + x + "," + y + "," + z + " but got null or different type.");
                }
            }
//            else if (tile == TileByte.AnalogueWire.getSymbol()){
//                BreadBoardItem item = getBreadBoardItemOnBoardFromCoordinates(x, y, z);
//                if (item instanceof AnalogueWire wire && wire.getOut() < 5) {
//                    wire.inputSignal(dx, dy, dz, 5, t);
//                } else {
//                    System.out.println("Expected AnalogueWire at " + x + "," + y + "," + z + " but got null or different type.");
//                }
//            }
            else if (tile == LED_OFF_BYTE) {
                LED led = (LED) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert led != null;
                led.setOut(TState.POSITIVE, t);
            }
            else if (tile == TileByte.TwoByTwoLEDOff.getSymbol()) {
                TwoByTwoLED tLed = (TwoByTwoLED) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert tLed != null;
                tLed.setOut(TState.POSITIVE, t);
            }
            else if (tile == TileByte.ThreeByThreeLEDOff.getSymbol()) {
                ThreeByThreeLED thLed = (ThreeByThreeLED) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert thLed != null;
                thLed.setOut(TState.POSITIVE, t);
            }
            else if (tile == TileByte.FourByFourLEDOff.getSymbol()) {
                FourByFourLED fLed = (FourByFourLED) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert fLed != null;
                fLed.setOut(TState.POSITIVE, t);
            }
            else if (tile == RED_LED_OFF_BYTE) {
                RedLED rled = (RedLED) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                if (rled == null) {
                    System.err.println("RedLED missing at " + x + "," + y + "," + z);
                    return;
                }
                int ran = (int) (Math.random() * 255);
                rled.setBrightness(ran);
                rled.setOut(TState.POSITIVE, t);
            }
            else if (tile == GREEN_LED_OFF_BYTE) {
                GreenLED gled = (GreenLED) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert gled != null;
                gled.setOut(TState.POSITIVE, t);
            }
            else if (tile == BLUE_LED_OFF_BYTE) {
                BlueLED bled = (BlueLED) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert bled != null;
                bled.setOut(TState.POSITIVE, t);
            }
            else if (tile == RESISTOR_1_BYTE) {
                Resistor1 r1 = (Resistor1) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert r1 != null;
                r1.setOut(dx, dy, dz, TState.POSITIVE, t);
            }
            else if (tile == RESISTOR_3_BYTE) {
                Resistor3 r3 = (Resistor3) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert r3 != null;
                r3.setOut(dx, dy, dz, TState.POSITIVE, t);
            }
            else if (tile == RESISTOR_5_BYTE) {
                Resistor5 r5 = (Resistor5) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert r5 != null;
                r5.setOut(dx, dy, dz, TState.POSITIVE, t);
            }
            else if (tile == RESISTOR_10_BYTE) {
                Resistor10 r10 = (Resistor10) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert r10 != null;
                r10.setOut(dx, dy, dz, TState.POSITIVE, t);
            }else if (tile == TileByte.Resistor50.getSymbol()) {
                Resistor50 r50 = (Resistor50) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert r50 != null;
                r50.setOut(dx, dy, dz, TState.POSITIVE, t);
            }
            else if (tile == TileByte.Resistor100.getSymbol()) {
                Resistor100 r100 = (Resistor100) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert r100 != null;
                r100.setOut(dx, dy, dz, TState.POSITIVE, t);
            }else if (tile == TileByte.Store0.getSymbol()) {
                StoreBit sb = (StoreBit) getBreadBoardItemOnBoardFromCoordinates(x, y ,z);
                assert sb != null;
                sb.setOut(TState.POSITIVE, t);
            }else {
                //--note: bug with RGB LEDs; this prints out 4x
//                System.out.println("setWiresAndLeds(): trying to turn on unknown wire or LED");
            }
        }
        else if(s.equals(TState.NEGATIVE)){
            if (tile == WIRE_ON_BYTE || tile == WIRE_DEAD_BYTE){ // || tile == WIRE_OFF_BYTE){// || tile == WIRE_OFF_BYTE) {
                Wire wire = (Wire) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert wire != null;
                wire.acceptInputsAndSetOut(dx, dy, dz, TState.NEGATIVE, t);
            }
            else if (tile == TileByte.UpdatableWireOn.getSymbol() || tile == TileByte.UpdatableWireDead.getSymbol()){ //|| tile == WIRE_ON_BYTE){
                BreadBoardItem item = getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                if (item instanceof UpdatableWire uWire) {
                    uWire.acceptInputsAndSetOut(dx, dy, dz, TState.NEGATIVE, t);
                } else {
                    System.out.println("Expected UpdatableWire at " + x + "," + y + "," + z + " but got null or different type.");
                }
            }
            else if (tile == TileByte.TeleportWireOn.getSymbol() || tile == TileByte.TeleportWireDead.getSymbol()){ //|| tile == WIRE_ON_BYTE){
                BreadBoardItem item = getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                if (item instanceof TeleportWire0 tWire) {
                    tWire.acceptInputsAndSetOut(dx, dy, dz, TState.NEGATIVE, t);
                } else {
                    System.out.println("Expected TeleportWire0 at " + x + "," + y + "," + z + " but got null or different type.");
                }
            }
//            else if (tile == TileByte.AnalogueWire.getSymbol()){
//                BreadBoardItem item = getBreadBoardItemOnBoardFromCoordinates(x, y, z);
//                if (item instanceof AnalogueWire wire && wire.getOut() >= 5) {
//                    wire.inputSignal(dx, dy, dz, 0, t);
//                } else {
//                    System.out.println("Expected AnalogueWire at " + x + "," + y + "," + z + " but got null or different type.");
//                }
//            }
            else if (tile == LED_ON_BYTE) {
                LED led = (LED) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert led != null;
                led.setOut(TState.NEGATIVE, t);
            }
            else if (tile == TileByte.TwoByTwoLEDOn.getSymbol()) {
                TwoByTwoLED tLed = (TwoByTwoLED) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert tLed != null;
                tLed.setOut(TState.NEGATIVE, t);
            }
            else if (tile == TileByte.ThreeByThreeLEDOn.getSymbol()) {
                ThreeByThreeLED thLed = (ThreeByThreeLED) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert thLed != null;
                thLed.setOut(TState.NEGATIVE, t);
            }
            else if (tile == TileByte.FourByFourLEDOn.getSymbol()) {
                FourByFourLED fLed = (FourByFourLED) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert fLed != null;
                fLed.setOut(TState.NEGATIVE, t);
            }
            else if (tile == RED_LED_ON_BYTE) {
                RedLED rled = (RedLED) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert rled != null;
                rled.setOut(TState.NEGATIVE, t);
            }
            else if (tile == GREEN_LED_ON_BYTE) {
                GreenLED gled = (GreenLED) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert gled != null;
                gled.setOut(TState.NEGATIVE, t);
            }
            else if (tile == BLUE_LED_ON_BYTE) {
                BlueLED bled = (BlueLED) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert bled != null;
                bled.setOut(TState.NEGATIVE, t);
            }
            else if (tile == RESISTOR_1_BYTE) {
                Resistor1 r1 = (Resistor1) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert r1 != null;
                r1.setOut(dx, dy, dz, TState.NEGATIVE, t);
            }
            else if (tile == RESISTOR_3_BYTE) {
                Resistor3 r3 = (Resistor3) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert r3 != null;
                r3.setOut(dx, dy, dz, TState.NEGATIVE, t);
            }
            else if (tile == RESISTOR_5_BYTE) {
                Resistor5 r5 = (Resistor5) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert r5 != null;
                r5.setOut(dx, dy, dz, TState.NEGATIVE, t);
            }
            else if (tile == RESISTOR_10_BYTE) {
                Resistor10 r10 = (Resistor10) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert r10 != null;
                r10.setOut(dx, dy, dz, TState.NEGATIVE, t);
            }else if (tile == TileByte.Resistor50.getSymbol()) {
                Resistor50 r50 = (Resistor50) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert r50 != null;
                r50.setOut(dx, dy, dz, TState.NEGATIVE, t);
            }
            else if (tile == TileByte.Resistor100.getSymbol()) {
                Resistor100 r100 = (Resistor100) getBreadBoardItemOnBoardFromCoordinates(x, y, z);
                assert r100 != null;
                r100.setOut(dx, dy, dz, TState.NEGATIVE, t);
            }else if (tile == TileByte.Store1.getSymbol()) {
                StoreBit sb = (StoreBit) getBreadBoardItemOnBoardFromCoordinates(x, y ,z);
                assert sb != null;
                sb.setOut(TState.NEGATIVE, t);
            } else {
                //--note: bug with RGB LEDs; this prints out 4x
//                System.out.println("setWiresAndLeds(): trying to turn off unknown wire or LED");
            }
        }
        //else tstate = dead {error}
    }


    /**
     * Sets gates and double wires
     * @param s queueSignal
     * @param dx
     * @param dy
     * @param x
     * @param y
     */
    public void setGates(final TState s, final short dx, final short dy, final short dz, final short x, final short y, final short z, final int t) {
        //String sBR = breadboard[z + dz][y + dy][x + dx]; // adjacent tile
        byte sBR = breadboardByte[z + dz][y + dy][x + dx]; // adjacent tile

        if (sBR == TileByte.Not.getSymbol()) {
            Not not = (Not) getBreadBoardItemOnBoardFromCoordinates(x + dx, y + dy, z + dz);
            assert not != null;
            if (not.setRightGate(s, dx, dy, dz)) {
                //todo make it only queueSignal at the end of a tick
                not.calculate();
                addToGateCalculationArray(not);
                //not.signal(t);
            }
        } else if (sBR == TileByte.And.getSymbol()) {
            And and = (And) getBreadBoardItemOnBoardFromCoordinates(x + dx, y + dy,z + dz);
            assert and != null;
            if (and.setRightGate(s, dx, dy, dz)) {
                and.calculate();
                addToGateCalculationArray(and);
                //and.signal(t);
            }
        } else if (sBR == TileByte.Or.getSymbol()) {
            Or or = (Or) getBreadBoardItemOnBoardFromCoordinates(x + dx, y + dy, z + dz);
            assert or != null;
            if (or.setRightGate(s, dx, dy, dz)) {
                or.calculate();
                addToGateCalculationArray(or);
                //or.signal(t);
            }
        } else if (sBR == TileByte.Xor.getSymbol()) {
            Xor xor = (Xor) getBreadBoardItemOnBoardFromCoordinates(x + dx, y + dy, z + dz);
            assert xor != null;
            if (xor.setRightGate(s, dx, dy, dz)) {
                xor.calculate();
                addToGateCalculationArray(xor);
                //xor.signal(t);
            }
        }else if (sBR == TileByte.TriStateBufferDisconnected.getSymbol()) {
            TriStateBuffer tsb = (TriStateBuffer) getBreadBoardItemOnBoardFromCoordinates(x + dx, y + dy, z + dz);
            assert tsb != null;
            if (tsb.setRightGate(s, dx, dy, dz)) {
                tsb.calculate();
                addToGateCalculationArray(tsb);
                //tsb.signal(t);
            }
        }else if (sBR == TileByte.TriStateBufferConnected.getSymbol()) {
            TriStateBuffer tsb = (TriStateBuffer) getBreadBoardItemOnBoardFromCoordinates(x + dx, y + dy, z + dz);
            assert tsb != null;
            if (tsb.setRightGate(s, dx, dy, dz)) {
                tsb.calculate();
                addToGateCalculationArray(tsb);
                //tsb.signal(t);
            }
        } else if (sBR == TileByte.DoubleWire.getSymbol()) {
            DoubleWire dw = (DoubleWire) getBreadBoardItemOnBoardFromCoordinates(x + dx, y + dy, z + dz);
            dw.setRightInput(s, dx, dy, dz, t);
        }
    }

    /**
     *
     * @param d direction
     * @param s state
     * @param x this x
     * @param y this y
     * @param z this z
     * @param t tick to propagate on
     * @return whether allowed to propagate
     */
    private int propagateSignal(final Direction d, final TState s, final short x, final short y, final short z, final int t, final byte type) {
        short sn1 = (short)-1;
        short s0 = (short)0;
        short s1 = (short)1;
        byte bn1 = (byte)-1;
        byte b0 = (byte)0;
        byte b1 = (byte)1;

        if(t == tickNumber) {
            //System.out.println("pS(): queueSignal " + s + " from " + x + " " + y + " on tick " + t);

            if(type == TileByte.ButtonOff.getSymbol()){
                //if(s == TState.NEGATIVE) {
                    Button b = (Button) cBreadBoardItemsList.get(getClickableBreadBoardItemIndexAtCoordinates(x, y, z));
                    //if the current low tick is not the furthest scheduled one, i.e. stale
                    if (t != b.scheduledLowTick) {
                        System.out.println("t not scheduled thing t is " + t + " scheduledtick is " + b.scheduledLowTick);
                        return 2;//idk what return does
                    }
                //}
            }


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
                                    //(exclude centre case)
                                    if(!(j == y && k == x)) {
                                        if (k == x + 1) {//the new block is to the right of the original
                                            setGates(s, s1, s0, b0, x, y, z, t);
                                            setWiresAndLeds(s,(short)(k-x), s0, b0, k, j, i, t);
                                        } else if (k == x - 1) {//left
                                            setGates(s, sn1, s0, b0, x, y, z, t);
                                            setWiresAndLeds(s, sn1, s0, b0, k, j, i, t);
                                        } else if (j == y + 1) {//below
                                            setGates(s, s0, s1, b0, x, y, z, t);
                                            setWiresAndLeds(s, s0, s1, b0, k, j, i, t);
                                        } else if (j == y - 1) {//above
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
        }else if(t < tickNumber) {
            //System.out.println("pS(): not called; queueSignal " + s + " from " + x + " " + y + " on tick " + t);
            return -1;
        }else {//greater than
            //System.out.println("pS(): not called; queueSignal " + s + " from " + x + " " + y + " on tick " + t);
            return 1;//and this, it allows future ones to be held
        }
    }

    /**
     * Function to propagate signals from teleports 0 through whatnot.
     * @param teleportNumber which place along the path we WERE on
     * @param s signal (on or off)
     * @param x the x position to teleport to (not past position)
     * @param y the y position to teleport to
     * @param z the z position to teleport to
     * @param t tick
     * @param id which path to teleport along
     */
    private void propagateTeleportSignal(final Direction teleportNumber, final TState s, final short x, final short y, final short z, final int t, final byte type, final short id) {
        TeleportWire tw = null;
        if(teleportNumber.ordinal() == 0){
            TeleportWire1 tw1 = (TeleportWire1)getBreadBoardItemOnBoardFromCoordinates(x, y , z);
            tw1.signal(s,t);
        }else if(teleportNumber.ordinal() == 1){
            //tw = (TeleportWire2)getBreadBoardItemOnBoardFromCoordinates(x, y , z);
        }else if(teleportNumber.ordinal() == 2){
            //tw = (TeleportWire3)getBreadBoardItemOnBoardFromCoordinates(x, y , z);
        }

    }

    /**
     * This function is going to eliminate all the intermediate wires and speed up wire propagation big time
     * @param voltage
     * @param destX
     * @param destY
     * @param destZ
     */
    public void newPropagateSignal(double voltage, int destX, int destY, int destZ) {

    }


    /**
     * Queues a queueSignal
     * @param d direction
     * @param s state (ON, OFF, DEAD)
     * @param x current x position
     * @param y current y position
     * @param z current z position
     * @param t tick to propagate on
     * @param type the BreadBoardItem sending the signal (useful for Buttons)
     */
    public void queueSignal(final Direction d, final TState s, final int x, final int y, final int z, final int t, final byte type, final int id) {
        //System.out.println("queueSignal(): trying to add queueSignal at " + x + " " + y + " " + z + " on tick " + t);

        if (t < tickNumber || t > tickNumber + MAX_TICKS_IN_THE_FUTURE) {
            System.out.println("queueSignal(): tick out of range.");
            return;
        }

        // Step 1: Check for empty spacing using writeIndex
        int writeIndex = 0;
        for (int i = 0; i < signalArray.length; i++) {
            if (signalArray[i] != null && (int) signalArray[i][SIGNAL_ARRAY_TICK_PLACE] >= tickNumber) {
                writeIndex++;
            }
        }
        //System.out.println("beginning"+Arrays.deepToString(signalArray));
        // Step 3: Insert new queueSignal
        if (writeIndex < signalArray.length) {
            signalArray[writeIndex] = new Object[]{d, s, x, y, z, t, type, id};

//            System.out.println("aft insertion"+Arrays.deepToString(signalArray));
//            System.out.println(
//                    "queueSignal(): " +
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
    }

    /**
     * =============================EXPERIMENTAL=========================================
     *
     * Function to optimize queueSignal propagation.
     * Should be called at the beginning of the program and everytime the editing button is
     * pressed.
     *
     * Function works by making a list of straight paths the queueSignal would take.
     * May need to consider paths when anything/everything is true and false, since
     * logic gates might change the path. Though maybe they don't, now that I think of it.
     */
    public void memorizeWireRoutes(int initialTick) {

        Path[] paths = new Path[300];
        int pathIndex = 0;

    //for (int index = 0; index < cBreadBoardItemsList.size(); index++) {

        paths[pathIndex] = new Path();
        paths[pathIndex].sX = cBreadBoardItemsList.get(0).getX();
        paths[pathIndex].sY = cBreadBoardItemsList.get(0).getY();

        Switch sw = (Switch) cBreadBoardItemsList.get(0);
        sw.set();
        sw.signal(initialTick);


        boolean finalSignal = false;
        while (!finalSignal) {
            int i = 0;
            while (i < signalArray.length) {
                if (signalArray[i] != null) {
                    Direction d = (Direction) signalArray[i][SIGNAL_ARRAY_D_PLACE];
                    TState s = (TState) signalArray[i][SIGNAL_ARRAY_S_PLACE];
                    short x = ((Integer) signalArray[i][SIGNAL_ARRAY_X_PLACE]).shortValue();
                    short y = ((Integer) signalArray[i][SIGNAL_ARRAY_Y_PLACE]).shortValue();
                    short z = ((Integer) signalArray[i][SIGNAL_ARRAY_Z_PLACE]).shortValue();
                    /**
                     * Tick that the queueSignal is supposed to be propagated
                     */
                    int tickToBe = (int) signalArray[i][SIGNAL_ARRAY_TICK_PLACE];
                    byte type = (byte) signalArray[i][SIGNAL_ARRAY_OBJECT_PLACE];

                    finalSignal = true;
                    if (d == Direction.NONE) {
                        if (!finalSignalFunction(d, s, tickToBe, x, y, z, -1, 0, 0) &&
                                !finalSignalFunction(d, s, tickToBe, x, y, z, +1, 0, 0) &&
                                !finalSignalFunction(d, s, tickToBe, x, y, z, 0, -1, 0) &&
                                !finalSignalFunction(d, s, tickToBe, x, y, z, 0, 1, 0) &&
                                !finalSignalFunction(d, s, tickToBe, x, y, z, 0, 0, -1) &&
                                !finalSignalFunction(d, s, tickToBe, x, y, z, 0, 0, 1)) {
                            finalSignal = false;
                        }
                    } else if (d == Direction.RIGHT) {
                        if (!finalSignalFunction(Direction.NONE, s, tickToBe, x, y, z, 1, 0, 0)) {
                            finalSignal = false;
                        }
                    }

                    System.out.println("final queueSignal " + finalSignal);
                    if (finalSignal) {
                        System.out.println(x + "," + y + "," + z);
                    }

                    int returnValue = propagateSignal(d, s, x, y, z, tickToBe, type);

                }
                i++;
            }
            tickNumber++;
        }
    }

    /**
     * used in the memorize wire routes fn
     * @param d
     * @param s
     * @param t
     * @param x
     * @param y
     * @param z
     * @param dx
     * @param dy
     * @param dz
     * @return
     */
    private boolean finalSignalFunction(Direction d, TState s, int t, int x, int y, int z, int dx, int dy, int dz){
        if(d == Direction.NONE) {
            if (getBreadBoardItemOnBoardFromCoordinates(x + dx, y + dy, z + dz) != null) {
                //checks if there's a wire which has the same signal, and
                //whether it's a future signal
                if (locateDigitalBreadBoardItemOnBoard(x + dx, y + dy, z + dz).getOut() == s) {
                    for (int j = 0; j < signalArray.length; j++) {
                        if (signalArray[j] != null &&
                                (int) signalArray[j][SIGNAL_ARRAY_X_PLACE] == x + dx &&
                                (int) signalArray[j][SIGNAL_ARRAY_Y_PLACE] == y + dy &&
                                (int) signalArray[j][SIGNAL_ARRAY_Z_PLACE] == z + dz &&
                                (int) signalArray[j][SIGNAL_ARRAY_TICK_PLACE] >= t) {
                            return false;
                        }
                    }
                }else {
                    //we found ones which need the signal
                    return false;
                }
            }else {
                //an empty space should be false
                return false;
            }
        }
        return true;
    }

    /**
     * Set all wires, LEDs Switches to their dead or off state.
     */
    public void setAllThingsToDead() {
        for (short z = 0; z < ZHEIGHT; z++) {
            for (short y = 0; y < HEIGHT; y++) {
                for (short x = 0; x < WIDTH; x++) {
                    byte tile = breadboardByte[z][y][x];

//==================SET ON THINGS TO OFF INITIALLY==============================================
                    if (tile == TileByte.WireOn.getSymbol()) tile = TileByte.WireDead.getSymbol();
                    if (tile == TileByte.WireOff.getSymbol()) tile = TileByte.WireDead.getSymbol();
                    if (tile == TileByte.LEDOn.getSymbol()) tile = TileByte.LEDOff.getSymbol();
                    if (tile == TileByte.TwoByTwoLEDOn.getSymbol()) tile = TileByte.TwoByTwoLEDOff.getSymbol();
                    if (tile == TileByte.ThreeByThreeLEDOn.getSymbol()) tile = TileByte.ThreeByThreeLEDOff.getSymbol();
                    if (tile == TileByte.SwitchOn.getSymbol()) tile = TileByte.SwitchOff.getSymbol();
                    if (tile == TileByte.ButtonOn.getSymbol()) tile = TileByte.ButtonOff.getSymbol();
                    if (tile == TileByte.TriStateBufferConnected.getSymbol())
                        tile = TileByte.TriStateBufferDisconnected.getSymbol();
                    if (tile == TileByte.UpdatableWireOn.getSymbol()) tile = TileByte.UpdatableWireDead.getSymbol();
                    if (tile == TileByte.UpdatableWireOff.getSymbol()) tile = TileByte.UpdatableWireDead.getSymbol();

                    breadboardByte[z][y][x] = tile;
                }
            }
        }
    }

    /**
     * A class for wire paths for optimization.
     */
    private class Path {
        int sX;
        int sY;
        int eX;
        int eY;
        int t;
    }


    /**
     * Button, extends cBreadBoardItem
     */
    private class Button extends CBreadBoardItem {

        private TState out  = TState.DEAD;
        private int scheduledLowTick = -1;//variable to cancel the negative, delayed, tick when there is a new positive one

        public Button(final TState out, final Direction dir, final short x, final short y, final short z) {
            super(dir,x,y,z);
            this.out = out;
        }

        public void set() {
            int delay = 15;
            scheduledLowTick = tickNumber + 1 + delay;
            out = TState.POSITIVE;
            this.signal(tickNumber + 1);

            out = TState.NEGATIVE;
            this.signal(scheduledLowTick);

        }

        public byte returnTile(){
            if(out.equals(TState.POSITIVE)){
                return TileByte.ButtonOn.getSymbol();
            }else {
                return TileByte.ButtonOff.getSymbol();
            }
            //tstate dead
        }

        public void signal(int t){
            BreadBoard.this.queueSignal(this.getDir(),out, this.getX(), this.getY(), this.getZ(), t, returnTile(),this.id);
        }

    }

    /**
     * Switch, extends cBreadBoardItem
     */
    private class Switch extends CBreadBoardItem {

        private TState out  = TState.DEAD;
        private Direction dir2 = Direction.NONE;

        public Switch(final TState out, final Direction dir, final Direction dir2, final short x, final short y, final short z) {
            super(dir,x,y,z);
            this.out = out;
            this.dir2 = dir2;
        }

        public void set() {
            out = TState.flip(out);
            this.signal(tickNumber + 1);
        }

        public byte returnTile() {
            if (out == TState.POSITIVE) {
                return TileByte.SwitchOn.getSymbol();
            } else {
                return TileByte.SwitchOff.getSymbol();
            }
        }

        public TState getOut(){
            return out;
        }

        public void signal(int t) {
//
            BreadBoard.this.queueSignal(this.getDir(),out, this.getX(), this.getY(), this.getZ(), t, returnTile(),id);

            if(this.getDir2() != Direction.NONE) {
                BreadBoard.this.queueSignal(this.getDir2(),out, this.getX(), this.getY(), this.getZ(), t, returnTile(),id);
            }

        }

        public void setDir2(final Direction dir2) {
            this.dir2 = dir2;
        }

        public Direction getDir2(){
            return dir2;
        }

    }

    /**
     * Useful function to locate the corresponding list item from the breadBoardItemsList
     * @param x
     * @param y
     * @return BreadBoardItem bi
     */
    public BreadBoardItem getBreadBoardItemOnBoardFromCoordinates(final int x, final int y, final int z) {
        for(BreadBoardItem bi: breadBoardItemsList) {
            if(bi.getX() == x && bi.getY() == y && bi.getZ() == z) {
                return bi;
            }
        }
        return null;
    }

    /**
     * Useful function to locate the corresponding list item from the breadBoardItemsList
     * @param x
     * @param y
     * @return BreadBoardItem bi
     */
    public DigitalBreadBoardItem locateDigitalBreadBoardItemOnBoard(final int x, final int y, final int z) {
        return (DigitalBreadBoardItem) getBreadBoardItemOnBoardFromCoordinates(x,y,z);
    }

    /**
     * Function used calculate gates and add them to the array <br>
     * Will not add a new gate if it's already in there.
     */
    public void addToGateCalculationArray(Gate gate){
        boolean foundDuplicate = false;
        for(Gate g:gates){
            if(g == gate){
                foundDuplicate = true;
            }
        }
        if(!foundDuplicate){
            gates.add(gate);
        }
    }

    /**
     * Function used to call the final calculation and signal on gates <br>
     * This way there aren't duplicate calls (and therefore duplicate outputs) for a
     * single gate.
     */
    public void signalGates(int tickNo, boolean calculate){
        //note - this signals EVERY gate EVERY tick
        if(calculate) {
            for (Gate g : gates) {
                g.calculate();
                g.signal(tickNo);

            }
        }else {
            for (Gate g : gates) {
                g.signal(tickNo);
            }
        }
    }



    /**
     * And gate
     */
    private class And extends Gate {

        public And(final Direction dir, final short x, final short y, final short z) {
            super(dir, x, y, z);
        }

        public void calculate() {
//            if(TState.and(A,B) || TState.and(A,C) || TState.and(B,C)
//            && A.ordinal() * B.ordinal() * C.ordinal() == 0){
//                //only checking for 2 way and -- that is why == 0 is there
//                out = TState.POSITIVE;
//            }
            if (A.value + B.value + C.value + D.value + E.value + F.value < 50
            && A.value + B.value + C.value + D.value + E.value + F.value >= 2) {
                //check that theres no negatives, and that we at least have two positives
                //-----DON'T do one gated ands, whats the point lol --------------------------
                out = TState.POSITIVE;
            }else if (TState.dead(A, B, C, D, E, F)){//might be able to take this out, but keeping it for safety
                out = TState.DEAD;
            }else if(A.value + B.value + C.value + D.value + E.value + F.value >= 1){
                out = TState.NEGATIVE;
            }
            signalsOutputAtCurrentTick++;
        }

        public void signal(final int tick_when_set){
            //BreadBoard.this.queueSignal(this.getDir(),out,getX(),getY(),tick_when_set+1);
            super.signal(tick_when_set);
        }

        @Override
        public byte returnTile() {
            return TileByte.And.getSymbol();
        }

    }

    /**
     * Not gate
     */
    private class Not extends Gate {

        public Not(final Direction dir, final short x, final short y, final short z) {
            super(dir, x, y, z);
        }

        public void calculate(){
            //this.out = !B; old
            if(A.value + B.value + C.value + D.value + E.value + F.value == TState.POSITIVE.value){
                //notting from A
                //if only one ordinal is one AKA positive, then output should be negative
                this.out = TState.NEGATIVE;
            }else if(A.value + B.value + C.value + D.value + E.value + F.value == TState.NEGATIVE.value){//notting from C
                //if only one value is 50 AKA "TState.NEGATIVE" and the others are "0",
                //AKA "TState.DEAD", then output should be positive
                this.out = TState.POSITIVE;
            }else if(A.value + B.value + C.value + D.value + E.value + F.value != TState.DEAD.value){//one dead input
                this.out = TState.NEGATIVE;
            }

        }

        public void signal(int tick_when_set) {
            super.signal(tick_when_set);
        }

        @Override
        public byte returnTile() {
            return TileByte.Not.getSymbol();
        }

    }

    /**
     * Or gate
     */
    private class Or extends Gate {

        public Or(final Direction dir, final short x, final short y, final short z) {
            super(dir, x, y, z);
        }

        public void calculate(){
            if(TState.or(A,B,C,D,E,F)){
                out = TState.POSITIVE;
            } else if(TState.dead(A,B,C,D,E,F)){//ALL are dead
                out = TState.DEAD;
            } else {
                out = TState.NEGATIVE;
            }
        }

        public byte returnTile(){
            return TileByte.Or.getSymbol();
        }

        public void signal(int tick_when_set) {
            //BreadBoard.this.queueSignal(this.getDir(),out,this.getX(),this.getY());
            super.signal(tick_when_set);
        }

    }

    /**
     * For now only implements Xor on A and C.
     */
    private class Xor extends Gate {

        public Xor(final Direction dir, final short x, final short y, final short z) {
            super(dir, x, y, z);
        }

        public void calculate(){
            if((A.value + B.value + C.value + D.value + E.value + F.value == 2) ||
                    (A.value + B.value + C.value + D.value + E.value + F.value == 100)){//xoring
                //if two values are "1" AKA "TState.POSITIVE" and the others are "0" and not 50,
                //or two values are "50" AKA "TState.NEGATIVE" and the others are "0" and not 1,
                this.out = TState.NEGATIVE;
            }else if(A.value + B.value + C.value + D.value + E.value + F.value == 51){//xoring
                //one negative and one postive adds up to 51
                this.out = TState.POSITIVE;
            }

            if(A.value + B.value + C.value + D.value + E.value + F.value < 0){//at least one dead wire
                this.out = TState.DEAD;
            }
        }

        public byte returnTile(){
            return TileByte.Xor.getSymbol();
        }

        public void signal(int tick_when_set) {
            //BreadBoard.this.queueSignal(this.getDir(),out,this.getX(),this.getY());
            super.signal(tick_when_set);
        }

    }

    /**
     * LED
     */
    private class LED extends DigitalBreadBoardItem {

        protected TState out = TState.DEAD;

        public LED(final TState out, final Direction dir, final short x, final short y, final short z) {
            super(dir, x, y, z);
            this.out = out;
        }

        /**
         * To do, implement into and outof logic
         * @param s
         * @param t
         */
        public void setOut(final TState s, int t) {
            int nx = getX();
            int ny = getY();
            int nz = getZ();
            if(s.equals(TState.POSITIVE)){
                this.out = TState.POSITIVE;
            } else if(s.equals(TState.DEAD)){
                this.out = TState.DEAD;
            }else {
                this.out = TState.NEGATIVE;
                if(getBreadBoardItemIndexAtCoordinates(nx+1,ny, nz)!=-1){
                    DigitalBreadBoardItem dBBI = (DigitalBreadBoardItem) breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny, nz));
                    //System.out.println((breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny)).getOut()));
                    if (dBBI.getOut().equals(TState.POSITIVE)
                    && ((breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny, nz)).getDir() == dL)
                    || (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny, nz)).getDir() == dN))) {
                        this.out = TState.POSITIVE;

                    }
                }
                if(getBreadBoardItemIndexAtCoordinates(nx-1,ny, nz)!=-1){
                    DigitalBreadBoardItem dBBI = (DigitalBreadBoardItem) breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx-1, ny, nz));
                    if (dBBI.getOut().equals(TState.POSITIVE)
                            && ((breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx-1, ny, nz)).getDir() == dR)
                            || (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx-1, ny, nz)).getDir() == dN))) {
                        this.out = TState.POSITIVE;
                    }
                }
                if(getBreadBoardItemIndexAtCoordinates(nx,ny-1, nz)!=-1){
                    DigitalBreadBoardItem dBBI = (DigitalBreadBoardItem) breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny-1, nz));
                    if (dBBI.getOut().equals(TState.POSITIVE)
                            && (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny-1, nz)).getDir() == dD
                            || breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny-1, nz)).getDir() == dN)) {
                        this.out = TState.POSITIVE;
                    }
                }
                if(getBreadBoardItemIndexAtCoordinates(nx,ny+1, nz)!=-1){
                    DigitalBreadBoardItem dBBI = (DigitalBreadBoardItem) breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny+1, nz));
                    if (dBBI.getOut().equals(TState.POSITIVE)
                            && (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny+1, nz)).getDir() == dU
                            || breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny+1, nz)).getDir() == dN)) {
                        this.out = TState.POSITIVE;
                    }
                }
//
            }
            signal(t);

        }

        public void signal(int tick_when_set) {

            breadboardByte[getZ()][getY()][getX()] = returnTile();

            getMyFrame().repaint();
        }

        private void delay() {
            //delay
        }

        public byte returnTile(){
            if(out.equals(TState.POSITIVE)){
                return TileByte.LEDOn.getSymbol();
            }else {
                return TileByte.LEDOff.getSymbol();
            }
        }
    }

    /**
     * TwoByTwoLED
     * A two pixel by two pixel LED.
     */
    private class TwoByTwoLED extends LED {

        protected TState out = TState.DEAD;
        LED b,c,d; //The rest of the LED tiles which make up the 2by2 LED


        public TwoByTwoLED(final TState out, final Direction dir, final short x, final short y, final short z) {
            super(out, dir, x, y, z);
            this.out = out;
            b = new LED(out,Direction.NONE,(short)(x+1),y,z);
            c = new LED(out,Direction.NONE,x,(short)(y+1),z);
            d = new LED(out,Direction.NONE,(short)(x+1),(short)(y+1),z);

        }

        /**
         * To do, implement into and outof logic
         * @param s
         * @param t
         */
        public void setOut(final TState s, int t) {
            int nx = getX();
            int ny = getY();
            int nz = getZ();
            if(s.equals(TState.POSITIVE)){
                this.out = TState.POSITIVE;
            } else if(s.equals(TState.DEAD)){
                this.out = TState.DEAD;
            }else {
                this.out = TState.NEGATIVE;
                if(getBreadBoardItemIndexAtCoordinates(nx+1,ny, nz)!=-1){
                    DigitalBreadBoardItem dBBI = (DigitalBreadBoardItem) breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny, nz));
                    //System.out.println((breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny)).getOut()));
                    if (dBBI.getOut().equals(TState.POSITIVE)
                            && ((breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny, nz)).getDir() == dL)
                            || (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny, nz)).getDir() == dN))) {
                        this.out = TState.POSITIVE;

                    }
                }
                if(getBreadBoardItemIndexAtCoordinates(nx-1,ny, nz)!=-1){
                    DigitalBreadBoardItem dBBI = (DigitalBreadBoardItem) breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx-1, ny, nz));
                    if (dBBI.getOut().equals(TState.POSITIVE)
                            && ((breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx-1, ny, nz)).getDir() == dR)
                            || (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx-1, ny, nz)).getDir() == dN))) {
                        this.out = TState.POSITIVE;
                    }
                }
                if(getBreadBoardItemIndexAtCoordinates(nx,ny-1, nz)!=-1){
                    DigitalBreadBoardItem dBBI = (DigitalBreadBoardItem) breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny-1, nz));
                    if (dBBI.getOut().equals(TState.POSITIVE)
                            && (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny-1, nz)).getDir() == dD
                            || breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny-1, nz)).getDir() == dN)) {
                        this.out = TState.POSITIVE;
                    }
                }
                if(getBreadBoardItemIndexAtCoordinates(nx,ny+1, nz)!=-1){
                    DigitalBreadBoardItem dBBI = (DigitalBreadBoardItem) breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny+1, nz));
                    if (dBBI.getOut().equals(TState.POSITIVE)
                            && (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny+1, nz)).getDir() == dU
                            || breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny+1, nz)).getDir() == dN)) {
                        this.out = TState.POSITIVE;
                    }
                }
//
            }
            signal(t);

        }

        public void signal(int tick_when_set) {

            breadboardByte[getZ()][getY()][getX()] = returnTile(1);
            breadboardByte[getZ()][getY()][getX()+1] = returnTile(0);
            breadboardByte[getZ()][getY()+1][getX()] = returnTile(0);
            breadboardByte[getZ()][getY()+1][getX()+1] = returnTile(0);

            getMyFrame().repaint();
        }

        private void delay() {
            //delay
        }

        public byte returnTile(final int ifTopLeft){
            if(ifTopLeft == 0) {
                if (out.equals(TState.POSITIVE)) {
                    return TileByte.LEDOn.getSymbol();
                } else {
                    return TileByte.LEDOff.getSymbol();
                }
            }else {
                if (out.equals(TState.POSITIVE)) {
                    return TileByte.TwoByTwoLEDOn.getSymbol();
                } else {
                    return TileByte.TwoByTwoLEDOff.getSymbol();
                }
            }
        }
    }

    /**
     * ThreeByThreeLED
     * A three pixel by three pixel LED.
     */
    private class ThreeByThreeLED extends LED {

        protected TState out = TState.DEAD;
        LED b,c,d,e,f,g,h,i; //The eight LED tiles which make up the rest of the 3by3 LED (top left corner is a 3by3led type)


        public ThreeByThreeLED(final TState out, final Direction dir, final short x, final short y, final short z) {
            super(out, dir, x, y, z);
            this.out = out;
            b = new LED(out, Direction.NONE, (short)(x+1),            y, z);
            c = new LED(out, Direction.NONE, (short)(x+2),            y, z);
            d = new LED(out, Direction.NONE,            x, (short)(y+1), z);
            e = new LED(out, Direction.NONE, (short)(x+1), (short)(y+1), z);
            f = new LED(out, Direction.NONE, (short)(x+2), (short)(y+1), z);
            g = new LED(out, Direction.NONE,            x, (short)(y+2), z);
            h = new LED(out, Direction.NONE, (short)(x+1), (short)(y+2), z);
            i = new LED(out, Direction.NONE, (short)(x+2), (short)(y+2), z);

        }

        /**
         * To do, implement into and outof logic
         * @param s
         * @param t
         */
        public void setOut(final TState s, int t) {
            int nx = getX();
            int ny = getY();
            int nz = getZ();
            if(s.equals(TState.POSITIVE)){
                this.out = TState.POSITIVE;
            } else if(s.equals(TState.DEAD)){
                this.out = TState.DEAD;
            }else {
                this.out = TState.NEGATIVE;
                if(getBreadBoardItemIndexAtCoordinates(nx+1,ny, nz)!=-1){
                    DigitalBreadBoardItem dBBI = (DigitalBreadBoardItem) breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny, nz));
                    //System.out.println((breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny)).getOut()));
                    if (dBBI.getOut().equals(TState.POSITIVE)
                            && ((breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny, nz)).getDir() == dL)
                            || (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny, nz)).getDir() == dN))) {
                        this.out = TState.POSITIVE;

                    }
                }
                if(getBreadBoardItemIndexAtCoordinates(nx-1,ny, nz)!=-1){
                    DigitalBreadBoardItem dBBI = (DigitalBreadBoardItem) breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx-1, ny, nz));
                    if (dBBI.getOut().equals(TState.POSITIVE)
                            && ((breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx-1, ny, nz)).getDir() == dR)
                            || (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx-1, ny, nz)).getDir() == dN))) {
                        this.out = TState.POSITIVE;
                    }
                }
                if(getBreadBoardItemIndexAtCoordinates(nx,ny-1, nz)!=-1){
                    DigitalBreadBoardItem dBBI = (DigitalBreadBoardItem) breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny-1, nz));
                    if (dBBI.getOut().equals(TState.POSITIVE)
                            && (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny-1, nz)).getDir() == dD
                            || breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny-1, nz)).getDir() == dN)) {
                        this.out = TState.POSITIVE;
                    }
                }
                if(getBreadBoardItemIndexAtCoordinates(nx,ny+1, nz)!=-1){
                    DigitalBreadBoardItem dBBI = (DigitalBreadBoardItem) breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny+1, nz));
                    if (dBBI.getOut().equals(TState.POSITIVE)
                            && (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny+1, nz)).getDir() == dU
                            || breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny+1, nz)).getDir() == dN)) {
                        this.out = TState.POSITIVE;
                    }
                }
//
            }
            signal(t);

        }

        public void signal(int tick_when_set) {

            breadboardByte[getZ()][getY()][getX()] = returnTile(1);
            breadboardByte[getZ()][getY()][getX()+1] = returnTile(0);
            breadboardByte[getZ()][getY()][getX()+2] = returnTile(0);
            breadboardByte[getZ()][getY()+1][getX()] = returnTile(0);
            breadboardByte[getZ()][getY()+1][getX()+1] = returnTile(0);
            breadboardByte[getZ()][getY()+1][getX()+2] = returnTile(0);
            breadboardByte[getZ()][getY()+2][getX()] = returnTile(0);
            breadboardByte[getZ()][getY()+2][getX()+1] = returnTile(0);
            breadboardByte[getZ()][getY()+2][getX()+2] = returnTile(0);

            getMyFrame().repaint();
        }

        private void delay() {
            //delay
        }

        //note: keep them as ledon and off, if you put 3by3 then it spawns a new one.
        public byte returnTile(final int ifTopLeft){
            if(ifTopLeft == 0) {
                if (out.equals(TState.POSITIVE)) {
                    return TileByte.LEDOn.getSymbol();
                } else {
                    return TileByte.LEDOff.getSymbol();
                }
            }else {
                if (out.equals(TState.POSITIVE)) {
                    return TileByte.ThreeByThreeLEDOn.getSymbol();
                } else {
                    return TileByte.ThreeByThreeLEDOff.getSymbol();
                }
            }
        }
    }


    /**
     * FourByFourLED
     * A three pixel by three pixel LED.
     */
    private class FourByFourLED extends LED {

        protected TState out = TState.DEAD;
        //LED b,c,d,e,f,g,h,i; //The eight LED tiles which make up the rest of the 3by3 LED (top left corner is a 3by3led type)


        public FourByFourLED(final TState out, final Direction dir, final short x, final short y, final short z) {
            super(out, dir, x, y, z);
            this.out = out;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if((i!=0)||(j!=0)) {
                        new LED(out, Direction.NONE, (short)(x+j), (short)(y+i), z);
                    }
                }
            }

        }

        /**
         * To do, implement into and outof logic
         * @param s
         * @param t
         */
        public void setOut(final TState s, int t) {
            int nx = getX();
            int ny = getY();
            int nz = getZ();
            if(s.equals(TState.POSITIVE)){
                this.out = TState.POSITIVE;
            } else if(s.equals(TState.DEAD)){
                this.out = TState.DEAD;
            }else {
                this.out = TState.NEGATIVE;
                if(getBreadBoardItemIndexAtCoordinates(nx+1,ny, nz)!=-1){
                    DigitalBreadBoardItem dBBI = (DigitalBreadBoardItem) breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny, nz));
                    //System.out.println((breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny)).getOut()));
                    if (dBBI.getOut().equals(TState.POSITIVE)
                            && ((breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny, nz)).getDir() == dL)
                            || (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny, nz)).getDir() == dN))) {
                        this.out = TState.POSITIVE;

                    }
                }
                if(getBreadBoardItemIndexAtCoordinates(nx-1,ny, nz)!=-1){
                    DigitalBreadBoardItem dBBI = (DigitalBreadBoardItem) breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx-1, ny, nz));
                    if (dBBI.getOut().equals(TState.POSITIVE)
                            && ((breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx-1, ny, nz)).getDir() == dR)
                            || (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx-1, ny, nz)).getDir() == dN))) {
                        this.out = TState.POSITIVE;
                    }
                }
                if(getBreadBoardItemIndexAtCoordinates(nx,ny-1, nz)!=-1){
                    DigitalBreadBoardItem dBBI = (DigitalBreadBoardItem) breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny-1, nz));
                    if (dBBI.getOut().equals(TState.POSITIVE)
                            && (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny-1, nz)).getDir() == dD
                            || breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny-1, nz)).getDir() == dN)) {
                        this.out = TState.POSITIVE;
                    }
                }
                if(getBreadBoardItemIndexAtCoordinates(nx,ny+1, nz)!=-1){
                    DigitalBreadBoardItem dBBI = (DigitalBreadBoardItem) breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny+1, nz));
                    if (dBBI.getOut().equals(TState.POSITIVE)
                            && (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny+1, nz)).getDir() == dU
                            || breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny+1, nz)).getDir() == dN)) {
                        this.out = TState.POSITIVE;
                    }
                }
//
            }
            signal(t);

        }

        public void signal(int tick_when_set) {

            breadboardByte[getZ()][getY()][getX()] = returnTile(1);

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if(i!=0 || j!=0){
                        breadboardByte[getZ()][getY()+i][getX()+j] = returnTile(0);
                    }
                }
            }

            getMyFrame().repaint();
        }

        private void delay() {
            //delay
        }

        //note: keep them as ledon and off, if you put 3by3 then it spawns a new one.
        public byte returnTile(final int ifTopLeft){
            if(ifTopLeft != 1) {
                if (out.equals(TState.POSITIVE)) {
                    return TileByte.LEDOn.getSymbol();
                } else {
                    return TileByte.LEDOff.getSymbol();
                }
            }else {
                if (out.equals(TState.POSITIVE)) {
                    return TileByte.FourByFourLEDOn.getSymbol();
                } else {
                    return TileByte.FourByFourLEDOff.getSymbol();
                }
            }
        }
    }

    private class StoreBit extends DigitalBreadBoardItem {
        protected TState out = TState.DEAD;

        public StoreBit(final TState out, final Direction dir, final short x, final short y, final short z) {
            super(dir, x, y, z);
            this.out = out;
        }

        public void setOut(final TState out, int t) {
            this.out = out;
            signal(t + 1);
        }

        @Override
        public void signal(int tick_when_set) {
            breadboardByte[getZ()][getY()][getX()] = returnTile();
            getMyFrame().repaint();
            BreadBoard.this.queueSignal(this.getDir(),out, this.getX(), this.getY(), this.getZ(), tick_when_set, returnTile(),id);
        }

        @Override
        public byte returnTile() {
            if(out.equals(TState.POSITIVE)){
                return TileByte.Store1.getSymbol();
            }else if(out.equals(TState.NEGATIVE)){
                return TileByte.Store0.getSymbol();
            }
            System.out.println("StoreBit at " +
                    this.getX() + ", " +
                    this.getY() + ", " +
                    this.getZ() + ", " +
                    "should not be dead");
            /*TODO remove magic number */
            return -1;
        }
    }

    private abstract class ColourLED extends LED {

        protected int brightness = 0;

        public ColourLED(TState out, Direction dir, short x, short y, short z) {
            super(out, dir, x, y, z);
        }

        public void setBrightness(final int brightness) {
            this.brightness = brightness;
        }

        public int getBrightness() {
            return brightness;
        }

    }

    public class RedLED extends ColourLED {

        public RedLED(TState out, Direction dir, short x, short y, short z) {
            super(out, dir, x, y, z);
        }

        public void signal(int tick_when_set) {
            breadboardByte[getZ()][getY()][getX()] = returnTile();

            getMyFrame().repaint();
        }

        public byte returnTile(){
            if(out.equals(TState.POSITIVE)){
                return TileByte.RedLEDOn.getSymbol();
            }else {
                return TileByte.RedLEDOff.getSymbol();
            }
        }
    }

    public class GreenLED extends ColourLED {

        public GreenLED(TState out, Direction dir, short x, short y, short z) {
            super(out, dir, x, y, z);
        }

        public void signal(int tick_when_set) {
            breadboardByte[getZ()][getY()][getX()] = returnTile();
            getMyFrame().repaint();
        }

        public byte returnTile(){
            if(out.equals(TState.POSITIVE)){
                return TileByte.GreenLEDOn.getSymbol();
            }else {
                return TileByte.GreenLEDOff.getSymbol();
            }
        }
    }

    public class BlueLED extends ColourLED {

        public BlueLED(TState out, Direction dir, short x, short y, short z) {
            super(out, dir, x, y, z);
        }

        public void signal(int tick_when_set) {
            breadboardByte[getZ()][getY()][getX()] = returnTile();
            getMyFrame().repaint();
        }

        public byte returnTile(){
            if(out.equals(TState.POSITIVE)){
                return TileByte.BlueLEDOn.getSymbol();
            }else {
                return TileByte.BlueLEDOff.getSymbol();
            }
        }
    }

    /**
     * A new class for wire, which acts as a cross between two wires,
     * setting one side will set the opposite side on (say South side to North side)
     * and it will be independent of the other side.
     */
    public class DoubleWire extends Wire {

        public DoubleWire(final Direction dir1, final Direction dir2, final short x, final short y, final short z) {
            super(dir1, dir2, x, y, z);
        }

        /**
         *
         * @param s
         * @param deltax
         * @param deltay
         * @param deltaz
         * @param t
         */
        public void setRightInput(final TState s, final short deltax, final short deltay, final short deltaz, final int t){
            //this doubleWire is to the right of the input (ex. wire)
            if(deltaz == 0) {
            //we only want vertical and horizontal checking if its on the same plane/level.
                if (deltax == 1) {
                    if (this.getDir() == Direction.RIGHT || this.getDir2() == Direction.RIGHT
                            || this.getDir() == Direction.NONE || this.getDir2() == Direction.NONE) {
                        signal(dR, s, t);
                    }
                } else if (deltax == -1) {//this gate is to the left of the input (ex. wire)
                    if (this.getDir() == dL || this.getDir2() == dL
                            || this.getDir() == Direction.NONE || this.getDir2() == Direction.NONE) {
                        signal(dL, s, t);
                    }
                } else if (deltay == 1) {//this gate is below the input (ex. wire)
                    if (this.getDir() == dD || this.getDir2() == dD
                            || this.getDir() == Direction.NONE || this.getDir2() == Direction.NONE) {
                        signal(dD, s, t);
                    }
                } else if (deltay == -1) {//this gate is above the input (ex. wire)
                    if (this.getDir() == dU || this.getDir2() == dU
                            || this.getDir() == Direction.NONE || this.getDir2() == Direction.NONE) {
                        signal(dU, s, t);
                    }
                }
            }else {
                if(deltaz == -1){
                    if (this.getDir() == dI || this.getDir2() == dI
                            || this.getDir() == Direction.NONE || this.getDir2() == Direction.NONE) {
                        signal(dI, s, t);
                    }
                }else if (deltaz == 1){
                    if (this.getDir() == dO || this.getDir2() == dO
                            || this.getDir() == Direction.NONE || this.getDir2() == Direction.NONE) {
                        signal(dO, s, t);
                    }
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
        public void signal(final Direction d, final TState s, final int t) {
            //BreadBoard.this.queueSignal(d,s,this.getX(),this.getY());
            BreadBoard.this.queueSignal(d, s, this.getX(),this.getY(), this.getZ(), t+1, returnTile(),id);
        }

        //public Direction getDir2(){
        //    return dir2;
        //}

        public byte returnTile(){
            return TileByte.DoubleWire.getSymbol();
        }

    }

    /**
     * (Digital) Wire. <br>
     * Used to propagate digital (0s and 1s) signals across the board.
     */
    public class Wire extends DigitalBreadBoardItem {

        //private TState out = TState.DEAD;
        private Direction dir2;
        //private short dx = 0;
        //private short dy = 0;
        //private short dz = 0;

        public Wire(final Direction dir, final Direction dir2, final short x, final short y, final short z) {
            super(dir, x, y, z);
            this.dir2 = dir2;
        }

        public void acceptInputsAndSetOut(final short dx, final short dy, final short dz, final TState out, final int t) {
            this.out = out;
            //check first if this signal is coming from the right input direction
            //ie if this wire is facing right, then this should not be taking input
            //from an object to the right of it, thus possibly creating an infinite loop
            //or an insane amount of signals; much havoc was caused because of this on May 29, 2025
            if((this.getDir() == dL && dx != 1) ||
            (this.getDir() == dR && dx != -1) ||
            (this.getDir() == dU && dy != 1) ||
            (this.getDir() == dD && dy != -1) ||
            (this.getDir() == dO && dz != -1) || //out of the screen
            (this.getDir() == dI && dz != 1) ||//into the screen
            this.getDir() == dN) {
                breadboardByte[getZ()][getY()][getX()] = returnTile();
                this.signal(t);
            }
        }

        public void setDir2(final Direction dir2) {
            this.dir2 = dir2;
        }

        public TState getOut(){
            return out;
        }

        /**
         * Like the signal in Switch, searches for other board members.
         */
        public void signal(final int t) {
            //if(Main.tick_true) {
            BreadBoard.this.queueSignal(this.getDir(), out, this.getX(), this.getY(), this.getZ(), t + 1, returnTile(),id);
            //may need to check for errors in the future
            if(this.getDir2() != this.getDir() && this.getDir2() != Direction.NONE){
                BreadBoard.this.queueSignal(this.getDir2(), out, this.getX(), this.getY(), this.getZ(), t + 1, returnTile(),id);
            }
                //System.out.println("wire.queueSignal(): at " + this.getX() + " " + this.getY()
                //+ " which will be called on tick " + (t + 1));
            //}
        }

        public Direction getDir2(){
            return dir2;
        }

        public byte returnTile(){
            if(out.equals(TState.POSITIVE)){
                return TileByte.WireOn.getSymbol();
            }else {
                return TileByte.WireOff.getSymbol();
            }
        }

    }

    /**
     * Wire whose "out" value gets output at a constant rate; also acts like a normal Wire
     * in the sense that it takes input from other wires.
     */
    public class UpdatableWire extends Wire {

        static List<UpdatableWire> uWires = new ArrayList<UpdatableWire>();

        public UpdatableWire(Direction dir, Direction dir2, short x, short y, short z) {
            super(dir, dir2, x, y, z);
            uWires.add(this);
        }

        /**
         * Update all UpdatableWires
         */
        public static void update(final int t) {
            for (UpdatableWire uw: uWires) {
                uw.signal(t);
            }
        }

        public byte returnTile(){
            if(out.equals(TState.POSITIVE)){
                return TileByte.UpdatableWireOn.getSymbol();
            }else {
                return TileByte.UpdatableWireOff.getSymbol();
            }
        }

    }

    public abstract class TeleportWire extends Wire {

        protected int inX, inY, inZ;
        protected int outX, outY, outZ;

        int id;

        public TeleportWire(Direction dir, Direction dir2, short x, short y, short z, final int id) {
            super(dir, dir2, x, y, z);
            this.id = id;

        }

        /**
         * Like the signal in Switch, searches for other board members.
         */
        public void signal(final int t) {

            System.out.println("signaling from " + this.getClass());

            //use the out positions rather than this.positions to reduce redundant calculations!!!
            BreadBoard.this.queueSignal(this.getDir(), out, outX, outY, outZ, t + 1, returnTile(), id);

//================== USES A WIRE RATHER THAN A TELEPORT WIRE!!!! ========================================
            if(this.getDir2() != Direction.NONE){
                BreadBoard.this.queueSignal(this.getDir2(), out, this.getX(), this.getY(), this.getZ(), t + 1, returnWireTile(),id);
            }

        }

        @Override
        public byte returnTile() {
            if(out.equals(TState.POSITIVE)){
                return TileByte.TeleportWireOn.getSymbol();
            }else {
                return TileByte.TeleportWireOff.getSymbol();
            }
        }

        //usefull for child classes
        protected byte returnWireTile() {
            return super.returnTile();
        }

        public abstract void updateArray();

    }

    /**
     * Starting teleportation location:
     * NOTE IMPLEMENTED IN REVERSE ORDER
     * I.E. USER WILL HAVE TO PUT TW1 BEFORE TW0
     * THIS REDUCES EXTRA FUNCTIONS AND ID MESSINESS.
     */
    public class TeleportWire0 extends TeleportWire {

        public TeleportWire0(Direction dir, Direction dir2, short x, short y, short z, short outX, short outY, short outZ, final int id) {
            super(dir, dir2, x, y, z, id);

            this.outX = outX;
            this.outY = outY;
            this.outZ = outZ;
        }

        public void updateArray(){
            int pos = (bondedTeleportID-1)*2+1;
            teleportWireInfoArray[pos][0] = x / 128;
            teleportWireInfoArray[pos][1] = x % 128;
            teleportWireInfoArray[pos][2] = y / 128;
            teleportWireInfoArray[pos][3] = y % 128;
            teleportWireInfoArray[pos][4] = z / 128;
            teleportWireInfoArray[pos][5] = z % 128;
            teleportWireInfoArray[pos][6] = bondedTeleportID / 128;
            teleportWireInfoArray[pos][7] = bondedTeleportID % 128;
        }

    }

    public class TeleportWire1 extends TeleportWire {

        public TeleportWire1(Direction dir, Direction dir2, short x, short y, short z, final int id) {
            super(dir, dir2, x, y, z, id);
            //use if tw1 is the last in teleport series
            outX = x;
            outY = y;
            outZ = z;
        }

        public void updateArray(){
            int pos = (bondedTeleportID-1)*2;
            teleportWireInfoArray[pos][0] = x / 128;
            teleportWireInfoArray[pos][1] = x % 128;
            teleportWireInfoArray[pos][2] = y / 128;
            teleportWireInfoArray[pos][3] = y % 128;
            teleportWireInfoArray[pos][4] = z / 128;
            teleportWireInfoArray[pos][5] = z % 128;
            teleportWireInfoArray[pos][6] = bondedTeleportID / 128;
            teleportWireInfoArray[pos][7] = bondedTeleportID % 128;
        }

        public void signal(final TState out, final int t) {
            this.out = out;
            breadboardByte[getZ()][getY()][getX()] = returnTile();
            System.out.println("signalling " + out + " at " + x + " " + y + " " + z + " from tpw1");
//================== USES A WIRE RATHER THAN A TELEPORT WIRE!!!! ========================================
            if(this.getDir2() != Direction.NONE){
                BreadBoard.this.queueSignal(this.getDir2(), out, x, y, z, t + 1, super.returnWireTile() ,id);
            }

        }

    }

    public abstract class Resistor extends DigitalBreadBoardItem {
        protected int delayTicks;
        protected TState out = TState.DEAD;

        public Resistor(Direction dir, int delayTicks, short x, short y, final short z) {
            super(dir, x, y, z);
            this.delayTicks = delayTicks;
        }

        public void setOut(final short dx, final short dy, final short dz, final TState out, final int t) {
            this.out = out;
            //check first if this queueSignal is coming from the right input direction
            //ie if this wire is facing right, then this should not be taking input
            //from an object to the right of it, thus possibly creating an infinite loop
            //or an insane amount of signals; much havoc was caused because of this on May 29, 2025
            if((this.getDir() == dL && dx != 1) ||
                    (this.getDir() == dR && dx != -1) ||
                    (this.getDir() == dU && dy != 1) ||
                    (this.getDir() == dD && dy != -1) ||
                    this.getDir() == dN) {
                //Main.getMyGameScreen().repaint();
                this.signal(t);
            }

        }

        /**
         * Like the queueSignal in Switch, searches for other board members.
         */
        @Override
        public void signal(final int t) {
            BreadBoard.this.queueSignal(this.getDir(), out, this.getX(), this.getY(), this.getZ(),t + 1 + delayTicks,returnTile(),id);
        }

        public int getDelayTicks() {
            return delayTicks;
        }
        public abstract byte returnTile();
    }

    public class Resistor1 extends Resistor {
        public Resistor1(Direction dir, short x, short y, short z) {
            super(dir, 1, x, y, z);
        }

//        @Override
//        public void setOut(final short dx, final short dy, final TState out, final int t){
//            super.setOut(dx, dy, out, t);
//            //setBreadBoardTile(RESISTOR_1_SYMBOL, getX(), getY());
//        }

        @Override
        public byte returnTile() {
            return TileByte.Resistor1.getSymbol();
        }
    }

    public class Resistor3 extends Resistor {
        public Resistor3(Direction dir, short x, short y, short z) {
            super(dir, 3, x, y, z);
        }

//        @Override
//        public void setOut(final short dx, final short dy, final TState out, final int t){
//            super.setOut(dx, dy, out, t);
//            //setBreadBoardTile(RESISTOR_1_SYMBOL, getX(), getY());
//        }

        @Override
        public byte returnTile() {
                return TileByte.Resistor3.getSymbol();
        }
    }

    public class Resistor5 extends Resistor {
        public Resistor5(Direction dir, short x, short y, final short z) {
            super(dir, 5, x, y, z);
        }

//        @Override
//        public void setOut(final short dx, final short dy, final TState out, final int t){
//            super.setOut(dx, dy, out, t);
//            //setBreadBoardTile(RESISTOR_1_SYMBOL, getX(), getY());
//        }

        @Override
        public byte returnTile() {
            return TileByte.Resistor5.getSymbol();
        }
    }

    public class Resistor10 extends Resistor {
        public Resistor10(Direction dir, final short x, final short y, final short z) {
            super(dir, 10, x, y, z);
        }

//        @Override
//        public void setOut(final short dx, final short dy, final TState out, final int t){
//            super.setOut(dx, dy, out, t);
//            //setBreadBoardTile(RESISTOR_1_SYMBOL, getX(), getY());
//        }

        @Override
        public byte returnTile() {
            return TileByte.Resistor10.getSymbol();
        }
    }

    public class Resistor50 extends Resistor {
        public Resistor50(Direction dir, final short x, final short y, final short z) {
            super(dir, 50, x, y, z);
        }

//        @Override
//        public void setOut(final short dx, final short dy, final TState out, final int t){
//            super.setOut(dx, dy, out, t);
//            //setBreadBoardTile(RESISTOR_1_SYMBOL, getX(), getY());
//        }

        @Override
        public byte returnTile() {
            return TileByte.Resistor10.getSymbol();
        }
    }

    public class Resistor100 extends Resistor {
        public Resistor100(Direction dir, final short x, final short y, final short z) {
            super(dir, 100, x, y, z);
        }

//        @Override
//        public void setOut(final short dx, final short dy, final TState out, final int t){
//            super.setOut(dx, dy, out, t);
//            //setBreadBoardTile(RESISTOR_1_SYMBOL, getX(), getY());
//        }

        @Override
        public byte returnTile() {
            return TileByte.Resistor10.getSymbol();
        }
    }


    public abstract class CBreadBoardItem extends DigitalBreadBoardItem {

        public CBreadBoardItem(Direction dir, short x, short y, final short z) {
            super(dir, x, y, z);
            //cBreadBoardItemsList.add(this);
        }

        public abstract void set();

        //public abstract void queueSignal(final boolean s);

    }


    //--Getters--


    public String getGamemode() {
        return gamemode;
    }

    /**
     * Returns breadboardByte
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
     * Returns the yuuuge 2-dimensional queueSignal array
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

    /**
     * DO NOT DELETE VERY IMPORTANT
     * SAVES THE BREADBOARD
     * @param w
     * @param h
     * @param d
     */
    public void saveTileBytes(final int w, final int h, final int d)
    {

        int length = FileCreator.X_BYTES + FileCreator.Y_BYTES + FileCreator.Z_BYTES + FileCreator.SUBLAYERS * w * h * d
                + teleportWireInfoArray.length * teleportWireInfoArray[0].length;

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
        //teleportWire info stuff at the end!

        for (int tw = 0; tw < teleportWireInfoArray.length; tw++) {

            for (int datum = 0; datum < teleportWireInfoArray[tw].length; datum++) {
                b[6+ FileCreator.SUBLAYERS * d * h * w + tw * teleportWireInfoArray[tw].length + datum]
                        = (byte)teleportWireInfoArray[tw][datum];
            }
        }
        try {
            FileCreator.saveToFileBytes(b, fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Arrays.deepToString(teleportWireInfoArray));
        System.out.println("Saved breadboard to " + fileName);
    }

    public boolean isGatesAllowedToSignalOut() {
        return gatesAllowedToSignalOut;
    }

    public void setGatesAllowedToSignalOut(boolean gatesAllowedToSignalOut) {
        this.gatesAllowedToSignalOut = gatesAllowedToSignalOut;
    }

}