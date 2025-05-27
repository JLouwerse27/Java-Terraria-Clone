import javax.swing.*;
import java.awt.event.MouseEvent;
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
    private final static Direction dN = Direction.NONE;

    /**Empty is 0 in itemEnum*/
    private final static int EMPTY_TYPE = 0;

    private final int WIDTH;
    private final int HEIGHT;

    final static String DEFAULT_KEYWORD = "DEFAULT";
    final static String EDITING_KEYWORD = "EDITING";
    final static String CUTTING_KEYWORD = "CUTTING";
    final static String COPYING_KEYWORD = "COPYING";
    final static String PASTING_KEYWORD = "PASTING";

    final static String EMPTY_SYMBOL      = "_";
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
            LED_OFF_SYMBOL
    };

    //full version
    //private String[] fullItemEnum = {"_","s", "w", "X", "N","O","A","l"};

    //current breadboard stuff; different from default - I think.
    private String[][] breadboard;
    private Direction[][] breadboardDirection;
    private Direction[][] breadboardDirection2;

    private String [][] defaultBreadboard = {
            {"_","_","w","w","w","w","w","w","w","w","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_"},
            {"s","w","w","_","_","_","_","w","_","w","w","w","w","w","w","w","w","l","_","l","w","w","w","_","_","_","_","_","_","_"},
            {"_","_","A","w","w","_","w","X","w","X","w","_","_","_","_","_","_","_","_","_","_","_","w","_","_","_","_","_","_","_"},
            {"s","w","w","_","O","w","O","A","_","w","w","w","w","w","w","w","w","w","w","w","w","w","X","w","w","w","w","_","_","_"},
            {"_","w","A","w","w","_","_","w","_","N","_","_","_","w","_","_","_","_","_","_","_","_","w","_","_","_","w","_","_","_"},
            {"s","X","w","w","w","w","w","w","w","A","A","w","w","X","w","w","_","_","_","_","_","_","w","_","_","_","w","_","_","_"},
            {"_","w","_","_","_","_","w","_","_","_","N","_","_","w","_","O","w","w","w","w","_","_","w","_","_","_","w","_","_","_"},
            {"_","w","w","w","w","w","X","w","w","w","w","_","_","w","_","w","_","_","_","w","_","_","w","_","_","_","w","_","_","_"},
            {"_","_","w","_","w","_","w","_","w","A","A","w","w","X","w","w","_","_","_","O","w","w","O","w","w","w","A","_","_","_"},
            {"_","_","A","w","X","w","w","_","_","N","N","_","_","w","_","w","_","_","_","w","_","_","_","_","_","_","w","_","_","_"},
            {"_","_","w","_","w","_","w","w","w","w","w","w","w","w","_","O","w","w","w","w","_","_","_","_","_","_","w","_","_","_"},
            {"_","_","w","_","w","_","_","_","w","_","_","w","_","_","_","w","_","_","_","_","_","_","_","_","_","_","w","_","_","_"},
            {"_","_","w","_","w","_","_","_","N","_","_","w","_","_","_","w","_","_","_","_","_","_","_","_","_","_","w","_","_","_"},
            {"_","_","w","_","w","_","_","_","W","W","A","w","_","_","_","w","_","_","_","_","_","_","_","_","_","_","w","_","_","_"},
            {"_","_","w","_","w","_","_","_","_","_","w","_","_","_","_","w","_","_","_","_","_","_","_","_","_","_","w","_","_","_"},
            {"_","_","w","_","w","w","w","w","N","W","A","w","w","w","w","w","_","_","_","_","_","_","_","_","_","_","w","_","_","_"},
            {"_","_","w","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","w","_","_","_"},
            {"_","_","w","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","w","_","_","_"},
            {"_","_","w","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","w","_","_","_"},
            {"_","_","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","_","_","_"},
            {"_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_"},
            {"_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_"},
            {"_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_"},
            {"_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_"},
            {"_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_"},
            {"_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_"},
            {"_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_"},
            {"_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_"},
            {"_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_"},
            {"_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_","_"}
    };
    private Direction [][] defaultBreadboardDirection = {
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dR,dN,dN,dN,dN,dN,dD,dN,dD,dR,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dR,dR,dD,dN,dN,dD,dN,dD,dU,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dR,dR,dU,dL,dN,dN,dR,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dU,dN,dN,dN,dN,dN,dN,dN},
            {dN,dD,dR,dR,dU,dN,dN,dU,dN,dD,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dR,dN,dR,dR,dN,dN,dN,dN,dR,dR,dN,dN,dR,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dD,dN,dN,dN,dN,dN,dN,dN,dN,dU,dN,dN,dN,dN,dR,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dR,dN,dN,dR,dU,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dR,dR,dN,dN,dR,dN,dN,dN,dN,dN,dR,dN,dN,dU,dN,dN,dN,dL,dN,dN,dN},
            {dN,dN,dD,dN,dL,dN,dN,dN,dN,dU,dU,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dU,dU,dN,dN,dN,dN,dR,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dD,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dD,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dR,dN,dR,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN}
    };
    private Direction [][] defaultBreadboardDirection2 = {
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dR,dN,dR,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dR,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dD,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dD,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dD,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dD,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dD,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN,dN}
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
        } else {
            return -1;
        }
    }


    private BreadBoardItem convertToType(final String ts, final Direction d, final Direction d2, int x, final int y){
        switch (ts) {
            case "s":
                return new Switch(false, d, x, y);
            case "S":
                return new Switch(true, d, x, y);
            case "w", "W":
                return new Wire(d, x, y);
            case "X":
                return new DoubleWire(d,d2,x,y);
            case "N":
                return new Not(d, x, y);
            case "A":
                return new And(d, x, y);
            case "O":
                return new Or(d, x, y);
            case "L":
                return new LED(true,d,x,y);
            case "l":
                return new LED(false,d,x,y);
            case EMPTY_SYMBOL:
                return null;
            default:
                System.out.println("Unknown tile: " + breadboard[y][x] + " at " + x + "," + y);
        }
        return null;
    }

    /**
     * Returns the index of the current item if it matches the coordinates
     * otherwise -1
     * @param x position
     * @param y position
     * @return index the item matches, or -1
     */
    public int getBreadBoardItemIndexAtCoordinates(final int x, final int y) {
        for (int i = 0; i < breadBoardItemsList.size(); i++) {
            if (breadBoardItemsList.get(i).getX() == x && breadBoardItemsList.get(i).getY() == y) {
                //System.out.println("got index " + i);
                return i;
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
    private int GetClickableBreadBoardItemAtCoordinates(final int x, final int y) {
        for (int i = 0; i < cBreadBoardItemsList.size(); i++) {
            if (cBreadBoardItemsList.get(i).getX() == x && cBreadBoardItemsList.get(i).getY() == y) {
                System.out.println("found clickable at " + i);
                return i;
            }
        }
        return -1;
    }

    public boolean changeBreadBoard(final int type, final Direction dir1,
                                    final Direction dir2, final int x, final int y){

        if(x < 0 || y < 0 || x > breadboard[y].length || y > breadboard.length){
            return false;
        }

        if(type == -1) {
            //first remove the existing object
            int theOne = 0;
            if (getBreadBoardItemIndexAtCoordinates(x, y) != -1) {
                int i = getBreadBoardItemIndexAtCoordinates(x, y);
                breadBoardItemsList.remove(i);
                theOne = i;
            }
            if (GetClickableBreadBoardItemAtCoordinates(x, y) != -1) {
                int i = GetClickableBreadBoardItemAtCoordinates(x, y);
                cBreadBoardItemsList.remove(i);
            }

            for (int i = 0; i < itemEnum.length; i++) {
                if (breadboard[y][x].toUpperCase().equals(itemEnum[i])
                        || breadboard[y][x].toLowerCase().equals(itemEnum[i])) {
                    if (i + 1 >= itemEnum.length) {
                        i = -1;
                    } else {
                        breadBoardItemsList.add(theOne,
                                convertToType(itemEnum[i + 1], dN,dN, x, y));
                    }
                    breadboardDirection[y][x] = dN;//call this before cuz of repaint in next fn
                    setBreadBoardTile(itemEnum[i + 1], x, y);
                    return true;
                }
            }
        }else if(type == 0) {//empty
            //first remove the existing object if it's not a wire
            int theOne = 0;
            if (getBreadBoardItemIndexAtCoordinates(x, y) != -1
                    && !breadboard[y][x].toLowerCase().equals(itemEnum[type])) {
                int i = getBreadBoardItemIndexAtCoordinates(x, y);
                breadBoardItemsList.remove(i);
                theOne = i;
            }
            breadboardDirection[y][x] = dN;//call this before cuz of repaint in next fn
            setBreadBoardTile(itemEnum[type], x, y);
        }else {//anything else
            //first remove the existing object if it's not a wire
            int theOne = 0;
            if (getBreadBoardItemIndexAtCoordinates(x, y) != -1
            && !breadboard[y][x].toLowerCase().equals(itemEnum[type])) {
                int i = getBreadBoardItemIndexAtCoordinates(x, y);
                breadBoardItemsList.remove(i);
                theOne = i;
            }
            breadBoardItemsList.add(theOne,
                    convertToType(itemEnum[type], dir1,dir2, x, y));
            breadboardDirection[y][x] = dir1;//call this before cuz of repaint in next fn
            breadboardDirection2[y][x] = dir2;
            setBreadBoardTile(itemEnum[type], x, y);
        }
        return true;//fix later for other cases
    }

    /**
     * Sets all the arrays, preferably from BreadBoardFileLoader.java
     * @param tiles
     * @param dir1
     * @param dir2
     */
    public void setBreadBoardState(String[][] tiles, Direction[][] dir1, Direction[][] dir2) {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                String tile = tiles[y][x];
                Direction d1 = dir1[y][x];
                Direction d2 = dir2[y][x];

                setBreadBoardTile(tile, x, y);
                setBreadBoardDirectionTile(d1, x, y);
                setBreadBoardDirection2Tile(d2, x, y);

                int type = convertToItemEnumOrdinal(tile);
                if(type != -1) {
                    changeBreadBoard(type, d1, d2, x, y);
                }else {
                    String raw = tiles[y][x];
                    System.out.printf("weoa: raw='%s' tile='%s' at (%d,%d)%n",
                            raw, tile, x, y);
                    System.out.println("length = " + raw.length() + " char codes: " + Arrays.toString(raw.toCharArray()));
                }
            }
        }
    }


    public BreadBoard(final int width, final int height) {
        WIDTH = width;
        HEIGHT = height;
        if(WIDTH != Main.DEFAULT_SCREEN_SIZE){
            breadboard = new String[width][height];
            breadboardDirection = new Direction[width][height];
            breadboardDirection2 = new Direction[width][height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {

                    breadboard[i][j] = EMPTY_SYMBOL;
                    breadboardDirection[i][j] = dN;
                    breadboardDirection2[i][j] = dN;

                    if(i<defaultBreadboard.length && j < defaultBreadboard[i].length){
                        breadboard[i][j] = defaultBreadboard[i][j];
                        breadboardDirection[i][j] = defaultBreadboardDirection[i][j];
                        breadboardDirection2[i][j] = defaultBreadboardDirection2[i][j];
                    }
                }
            }
        }else{
            breadboard = defaultBreadboard;
            breadboardDirection = defaultBreadboardDirection;
            breadboardDirection2 = defaultBreadboardDirection2;
        }
        //setTilesInMain(breadboard);
        for(int i = 0; i < breadboard.length; i++) {
            for(int j = 0; j < breadboard[i].length; j++) {
                Direction d = breadboardDirection[i][j];
                Direction d2 = breadboardDirection2[i][j];
                switch (breadboard[i][j]) {
                    case "B":
                        breadBoardItemsList.add(new Button(true, d, j, i));
                        break;
                    case "b":
                        breadBoardItemsList.add(new Button(false, d, j, i));
                        break;
                    case "S":
                        breadBoardItemsList.add(new Switch(true, d, j, i));
                        break;
                    case "s":
                        breadBoardItemsList.add(new Switch(false, d, j, i));
                        break;
                    case "L":
                        breadBoardItemsList.add(new LED(false, d, j, i));
                        setBreadBoardTile("l", j, i);//no LED should be on by default
                        break;
                    case "l":
                        breadBoardItemsList.add(new LED(false, d, j, i));
                        break;
                    case "w":
                        breadBoardItemsList.add(new Wire(d, j, i));
                        break;
                    case "A":
                        breadBoardItemsList.add(new And(d, j, i));
                        break;
                    case "N":
                        breadBoardItemsList.add(new Not(d, j, i));
                        break;
                    case "O":
                        breadBoardItemsList.add(new Or(d, j, i));
                        break;
                    case "W":
                        breadBoardItemsList.add(new Wire(d, j, i));
                        setBreadBoardTile("w", j, i);//no wire should be on by default
                        break;
                    case "X":
                        breadBoardItemsList.add(new DoubleWire(d, d2, j, i));
                        break;
                    case "_":
                        break;
                    default:
                        System.out.println("ERROR LOADING TILES IN BREADBOARD CONSTRUCTOR!");
                        break;
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
    public boolean checkClick(final MouseEvent e, int x, final int y) {
        System.out.println("checkClick at " + x + ", " + y);
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
                    setBreadBoardTile(cbi.returnTile(),cbi.getX(),cbi.getY());
                    return true;
                }
            }
        }else if (gamemode.equals(EDITING_KEYWORD))
        {
            if (!Main.getCopying()) {
                System.out.println("mouse click:" + e.getButton());
                int tile_x = x / MyGameScreen.tileWidth;
                int tile_y = y / MyGameScreen.tileHeight;

                if (tile_x >= 0 && tile_x < MyGameScreen.xPixels
                        && tile_y >= 0 && tile_y < MyGameScreen.yPixels)
                {
                    if (e.getButton() == MouseEvent.BUTTON3) {//right click
                        return changeBreadBoard(EMPTY_TYPE, dN, dN, tile_x, tile_y);//0 is empty space
                    } else if (e.getButton() == MouseEvent.BUTTON1) {
                        return changeBreadBoard(itemCursor, dN, dN, tile_x, tile_y);
                    }else if (e.getButton() == MouseEvent.NOBUTTON) { // drag ======= MAY HAVE TO CHANGE ========
                        if(Main.mouseClickNumber[0] == MouseEvent.BUTTON1) {//drag left click
                            return changeBreadBoard(itemCursor, dN, dN, tile_x, tile_y);
                        }else if (Main.mouseClickNumber[0] == MouseEvent.BUTTON3) {//drag left click
                            return changeBreadBoard(EMPTY_TYPE, dN, dN, tile_x, tile_y);
                        }
                    }
                } else
                {
                    return false;
                }
            }
        }
        else if (gamemode.equals(COPYING_KEYWORD) || gamemode.equals(CUTTING_KEYWORD))
        {//copying and pasting
            int sX = (int)(Main.mouseX[0] - Main.SCREEN_X_OFFSET) / MyGameScreen.tileWidth;
            if(sX < 0) sX = 0;

            int sY = (int)(Main.mouseY[0] - Main.SCREEN_Y_OFFSET) / MyGameScreen.tileHeight;
            if(sY < 0) sY = 0;

            int eX = (int)(Main.mouseX[1] - Main.SCREEN_X_OFFSET) / MyGameScreen.tileWidth;
            if(eX > MyGameScreen.xPixels) eX = MyGameScreen.xPixels;

            int eY = (int)(Main.mouseY[1] - Main.SCREEN_Y_OFFSET) / MyGameScreen.tileHeight;
            if(eY > MyGameScreen.yPixels) eY = MyGameScreen.yPixels;

            selectEntities(sX,sY,eX,eY);

            if(gamemode.equals(CUTTING_KEYWORD)) {
                eraseRegion(sX, sY, eX, eY);
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
    private void selectEntities(final int sX, final int sY, final int eX, final int eY)
    {
        if((sX > eX) || (sY > eY))
        {
            System.out.println("ERROR SELECTING ENTITIES; DON'T DRAG IN REVERSE");
            //could possibly change this in the future
            return;
        }else if(sX == eX || sY == eY)
        {//one of the lengths is zero
            return;
        }


        int dX = eX - sX;
        int dY = eY - sY;

        String[][] tempCutCopyPasteBoard = new String[dY][dX];
        Direction[][] tempCutCopyPasteBoardDir1 = new Direction[dY][dX];
        Direction[][] tempCutCopyPasteBoardDir2 = new Direction[dY][dX];

        System.out.println("temp board at: "
                + sX
                + ", " + sY
                + ", " + eX
                + ", " + eY
                + " with dimensions: "
                + tempCutCopyPasteBoard[0].length
                + ", "
                + tempCutCopyPasteBoard.length);

        Main.getMyGameScreen().tempCutCopyPasteBoardList.clear();
        Main.getMyGameScreen().tempCutCopyPasteBoardDirection1List.clear();
        Main.getMyGameScreen().tempCutCopyPasteBoardDirection2List.clear();
        //set the board to the actual breadboard pieces
        for (int i = 0; i < dY; i++)
        {
            for (int j = 0; j < dX; j++)
            {
                tempCutCopyPasteBoard[i][j] = breadboard[i+sY][j+sX];
                tempCutCopyPasteBoardDir1[i][j] = breadboardDirection[i+sY][j+sX];
                tempCutCopyPasteBoardDir2[i][j] = breadboardDirection2[i+sY][j+sX];
            }
            //add the section of the board arrays to the board lists in mgs
            Main.getMyGameScreen().tempCutCopyPasteBoardList.add(
                    new ArrayList<>(Arrays.asList(tempCutCopyPasteBoard[i])));
            Main.getMyGameScreen().tempCutCopyPasteBoardDirection1List.add(
                    new ArrayList<>(Arrays.asList(tempCutCopyPasteBoardDir1[i])));
            Main.getMyGameScreen().tempCutCopyPasteBoardDirection2List.add(
                    new ArrayList<>(Arrays.asList(tempCutCopyPasteBoardDir2[i])));
            //System.out.println(Main.getMyGameScreen().tempCutCopyPasteBoardList.get(i));
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
    private void eraseRegion(int sX, int sY, int eX, int eY) {
        for (int y = sY; y < eY; y++) {
            for (int x = sX; x < eX; x++) {
                changeBreadBoard(0, Direction.NONE, Direction.NONE, x, y); // sets to EMPTY
            }
        }
    }

    /**
     * Selects entity and tile and adds it to a temp array
     * @param x
     * @param y
     */
    private BreadBoardItem selectEntity(final int x, final int y)
    {
        if (getBreadBoardItemIndexAtCoordinates(x, y) != -1) {
            int i = getBreadBoardItemIndexAtCoordinates(x, y);
            return breadBoardItemsList.get(i);
        }else {
            return null;
        }
    }

    /**
     * Rotates an item on the breadboard; only sif in editing mode.
     * for now just rotates 90 deg to the right
     */
    public void rotateItem(final int layer, final int x, final int y) {
        if(gamemode.equals(EDITING_KEYWORD)) {
            if(layer == 0) {//rotate for everything
                int num = breadboardDirection[y][x].ordinal() + 1;//rotate by adding one to the ordinal
                if (num >= Direction.values().length) num = 0;//reset to first one if > length
                breadboardDirection[y][x] = Direction.values()[num];
                if (getBreadBoardItemIndexAtCoordinates(x, y) != -1) {
                    int i = getBreadBoardItemIndexAtCoordinates(x, y);
                    breadBoardItemsList.get(i).setDir(Direction.values()[num]);
                }

                callPaint();
            }else {//rotate the second output of DoubleWire
                if (getBreadBoardItemIndexAtCoordinates(x, y) != -1) {

                    if (locateBreadBoardItemOnBoard(x,y).returnTile().equals("X")) {

                        DoubleWire dw = (DoubleWire) locateBreadBoardItemOnBoard(x,y);
                        int dirNum = dw.getDir2().ordinal() + 1;
                        if (dirNum >= Direction.values().length) dirNum = 0;
                        breadboardDirection2[y][x] = Direction.values()[dirNum];
                        //to do: implement safety measure so that user cannot make d2 the opposite of d1
                        dw.setDir2(Direction.values()[dirNum]);

                    }
                    callPaint();
                }
            }
        }
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
    public void setBreadBoardTile(final String tile, final int x, final int y){
        breadboard[y][x] = tile;
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
    public void setBreadBoardDirectionTile(final Direction dir, final int x, final int y){
        breadboardDirection[y][x] = dir;
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
    public void setBreadBoardDirection2Tile(final Direction dir, final int x, final int y){
        breadboardDirection2[y][x] = dir;
        //Main.setTiles(SIZE, breadboardDirection); defunct
        //callPaint();
    }


    private void setWiresAndLeds(final boolean s, final int x, final int y) {
        String tile = breadboard[y][x];

        if (s) {
            if (tile.equals(WIRE_OFF_SYMBOL)) {
                Wire wire = (Wire) locateBreadBoardItemOnBoard(x, y);
                assert wire != null;
                wire.setOut(true);
            }
            if (tile.equals(LED_OFF_SYMBOL)) {
                LED led = (LED) locateBreadBoardItemOnBoard(x, y);
                assert led != null;
                led.setOut(true);
            }
        } else {
            if (tile.equals(WIRE_ON_SYMBOL)) {
                Wire wire = (Wire) locateBreadBoardItemOnBoard(x, y);
                assert wire != null;
                wire.setOut(false);
            }
            if (tile.equals(LED_ON_SYMBOL)) {
                LED led = (LED) locateBreadBoardItemOnBoard(x, y);
                assert led != null;
                led.setOut(false);
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
    public void setGates(final boolean s, final int dx, final int dy, final int x, final int y) {
        String sBR = breadboard[y + dy][x + dx]; // tile to the right

        if (sBR.equals(NOT_SYMBOL)) {
            Not not = (Not) locateBreadBoardItemOnBoard(x + dx, y + dy);
            assert not != null;
            if (not.setRightGate(s, dx, dy)) {
                not.calculate();
                not.signal();
            }
        } else if (sBR.equals(AND_SYMBOL)) {
            And and = (And) locateBreadBoardItemOnBoard(x + dx, y + dy);
            assert and != null;
            if (and.setRightGate(s, dx, dy)) {
                and.calculate();
                and.signal();
            }
        } else if (sBR.equals(OR_SYMBOL)) {
            Or or = (Or) locateBreadBoardItemOnBoard(x + dx, y + dy);
            assert or != null;
            if (or.setRightGate(s, dx, dy)) {
                or.calculate();
                or.signal();
            }
        } else if (sBR.equals(CROSSWIRE_SYMBOL)) {
            DoubleWire dw = (DoubleWire) locateBreadBoardItemOnBoard(x + dx, y + dy);
            dw.setRightInput(s, dx, dy);
        }
    }

    public void signal(final Direction d, final boolean s, final int x, final int y) {
        //System.out.println(" signal " + s + " from " + x + "," + y);
        if(d == Direction.NONE) {
            for (int i = y - 1; i <= y + 1; i++) {
                for (int j = x - 1; j <= x + 1; j++) {
                    if (i != -1 && j != -1
                            && i < HEIGHT && j < WIDTH
                            && !(i == y - 1 && j == x - 1)
                            && !(i == y + 1 && j == x - 1)
                            && !(i == y - 1 && j == x + 1)
                            && !(i == y + 1 && j == x + 1)
                            && !(i == y && j == x)) {

                        setWiresAndLeds(s, j, i);

                        if(j == x + 1){//gate is to the right of this block
                            setGates(s,1,0,x,y);
                        }else if(j == x - 1){//gate is to the left of this block
                            setGates(s,-1,0,x,y);
                        } else if (i == y + 1) {//gate is below this block
                            setGates(s,0,1,x,y);
                        }else if (i == y - 1) {//gate is above this block
                            setGates(s,0,-1,x,y);
                        }
                    }
                }
            }
        }else if (d == Direction.RIGHT) {
            if (x + 1 < WIDTH) {
                setWiresAndLeds(s, x + 1, y);
                setGates(s, 1, 0, x, y);
            }
        } else if (d == Direction.LEFT) {
            if (x - 1 >= 0) {
                setWiresAndLeds(s, x - 1, y);
                setGates(s, -1, 0, x, y);
            }
        }else if (d == Direction.DOWN) {
            if (y + 1 < HEIGHT) {
                setWiresAndLeds(s, x, y + 1);
                setGates(s, 0, 1, x, y);
            }
        } else if (d == Direction.UP) {
            if (y - 1 >= 0) {
                setWiresAndLeds(s, x, y - 1);
                setGates(s, 0, -1, x, y);
            }
        }
    }

    /**
     * Button, extends cBreadBoardItem
     */
    private class Button extends CBreadBoardItem {

        private boolean out  = false;

        public Button(boolean out, Direction dir, int x, int y) {
            super(dir,x,y);
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

        public void signal(final boolean s){
            //--
        }

    }

    /**
     * Switch, extends cBreadBoardItem
     */
    private class Switch extends CBreadBoardItem {

        private boolean out  = false;

        public Switch(boolean out, Direction dir, int x, int y) {
            super(dir,x,y);
            this.out = out;
        }

        public void set(final boolean b) {
            out = !out;
            this.signal(out);
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

        public void signal(final boolean s) {
            BreadBoard.this.signal(this.getDir(),s, this.getX(), this.getY());
        }

    }

    /**
     * Useful function to locate the corresponding list item from the breadBoardItemsList
     * @param x
     * @param y
     * @return BreadBoardItem bi
     */
    public BreadBoardItem locateBreadBoardItemOnBoard(final int x, final int y) {
        for(BreadBoardItem bi: breadBoardItemsList) {
            if(bi.getX() == x && bi.getY() == y) {
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

        public Gate(final Direction dir, final int x, final int y) {
            super(dir, x, y);
        }

        public abstract void setA(boolean on);
        public abstract void setB(boolean on);
        public abstract void setC(boolean on);


        public abstract void calculate();

        /**A is always 90deg to the left of output
         * B is always 180deg away from the output
         * C is always 90 deg to the right of output
         */
        public boolean setRightGate(final boolean s, final int deltax, final int deltay){
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

        public boolean getOut(){
            return out;
        }
    }

    /**
     * And gate
     */
    private class And extends Gate {

        public And(final Direction dir, final int x, final int y) {
            super(dir, x, y);
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

        public void signal(){
            BreadBoard.this.signal(this.getDir(),out,getX(),getY());
        }

    }

    /**
     * Not gate
     */
    private class Not extends Gate {

        public Not(final Direction dir, final int x, final int y) {
            super(dir, x, y);
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

        public void signal() {
            BreadBoard.this.signal(this.getDir(),out,this.getX(),this.getY());
        }

    }

    /**
     * Or gate
     */
    private class Or extends Gate {

        public Or(final Direction dir, final int x, final int y) {
            super(dir, x, y);
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

        public void signal() {
            BreadBoard.this.signal(this.getDir(),out,this.getX(),this.getY());
        }

    }

    /**
     * LED
     */
    private class LED extends BreadBoardItem {

        private boolean out = false;

        public LED(final boolean out, final Direction dir, final int x, final int y) {
            super(dir, x, y);
            this.out = out;
        }

        public void setOut(final boolean s) {
            boolean[] signal = new boolean[4];
            int nx = getX();
            int ny = getY();
            if(s){
                this.out = true;
            } else {
                this.out = false;
                if(getBreadBoardItemIndexAtCoordinates(nx+1,ny)!=-1){
                    //System.out.println((breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny)).getOut()));
                    if (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny)).getOut()
                    && ((breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny)).getDir() == dL)
                    || (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx+1, ny)).getDir() == dN))) {
                        this.out = true;

                    }
                }
                if(getBreadBoardItemIndexAtCoordinates(nx-1,ny)!=-1){
                    if (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx-1, ny)).getOut()
                            && ((breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx-1, ny)).getDir() == dR)
                            || (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx-1, ny)).getDir() == dN))) {
                        this.out = true;
                    }
                }
                if(getBreadBoardItemIndexAtCoordinates(nx,ny-1)!=-1){
                    if (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny-1)).getOut()
                            && (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny-1)).getDir() == dD
                            || breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny-1)).getDir() == dN)) {
                        this.out = true;
                    }
                }
                if(getBreadBoardItemIndexAtCoordinates(nx,ny+1)!=-1){
                    if (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny+1)).getOut()
                            && (breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny+1)).getDir() == dU
                            || breadBoardItemsList.get(getBreadBoardItemIndexAtCoordinates(nx, ny+1)).getDir() == dN)) {
                        this.out = true;
                    }
                }
//
            }
            if (this.out) {
                setBreadBoardTile("L", getX(), getY());
            } else {
                setBreadBoardTile("l", getX(), getY());
            }
            Main.getMyFrame().repaint();
        }

        private void delay() {
            //delay
        }

        public String returnTile(){
            if(out){
                return "L";
            }else {
                return "l";
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

        public DoubleWire(final Direction dir1, final Direction dir2, final int x, final int y) {
            super(dir1, x, y);
            this.dir2 = dir2;
        }

        public void setDir2(final Direction dir2) {
            this.dir2 = dir2;
        }

        public void setRightInput(final boolean s, final int deltax, final int deltay){
            //this doubleWire is to the right of the input (ex. wire)
            if(deltax == 1){
                if(this.getDir() == Direction.RIGHT || this.getDir2() == Direction.RIGHT) {
                    signal(dR, s);
                }
            }else if(deltax == -1){//this gate is to the left of the input (ex. wire)
                if(this.getDir() == dL || this.getDir2() == dL) {
                    signal(dL, s);
                }
            }else if(deltay == 1){//this gate is below the input (ex. wire)
                if(this.getDir() == dD || this.getDir2() == dD) {
                    signal(dD, s);
                }
            }else if(deltay == -1){//this gate is above the input (ex. wire)
                if(this.getDir() == dU || this.getDir2() == dU) {
                    signal(dU, s);
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
        public void signal(final Direction d, final boolean s) {
            BreadBoard.this.signal(d,s,this.getX(),this.getY());
        }

        public Direction getDir2(){
            return dir2;
        }

        public String returnTile(){
            return "X";
        }

    }

    /**
     * Wire
     */
    public class Wire extends BreadBoardItem {

        private boolean out = false;

        public Wire(final Direction dir, final int x, final int y) {
            super(dir, x, y);
        }

        public void setOut(final boolean out) {
            delay();
            this.out = out;
            if(out) {
                setBreadBoardTile("W", getX(), getY());
            }else {
                setBreadBoardTile("w", getX(), getY());
            }
            Main.getMyFrame().repaint();
            this.signal(out);
        }

        public boolean getOut(){
            return out;
        }

        private void delay() {
            //delay
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
        public void signal(final boolean s) {
            BreadBoard.this.signal(this.getDir(),s,this.getX(),this.getY());
        }

    }

    /**
     * Abstract Parent Class for the breadboard
     */
    public abstract class BreadBoardItem { //extends Thread {
        private int x = 0;
        private int y = 0;
        private boolean out = false;
        private Direction dir = Direction.RIGHT;

        public BreadBoardItem(final Direction dir, final int x, final int y) {
            this.dir = dir;
            this.x = x;
            this.y = y;
        }

        public int getX(){
            return x;
        }

        public int getY(){
            return y;
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
            return "_";
        }
    }

    public abstract class CBreadBoardItem extends BreadBoardItem {

        public CBreadBoardItem(Direction dir, int x, int y) {
            super(dir, x, y);
            cBreadBoardItemsList.add(this);
        }

        public abstract void set(final boolean on);

        public abstract void signal(final boolean s);

    }


    //--Getters--


    public String getGamemode() {
        return gamemode;
    }

    /**
     * Returns the default breadboard
     * @return breadboard array
     */
    public String[][] getBreadboard(){
        return breadboard;
    }

    /**
     * Returns the array of directions of items in the Breadboard
     * @return breadboardDirection array
     */
    public Direction[][] getBreadboardDirection(){
        return breadboardDirection;
    }

    /**
     * Returns the string array of directions of items in the Breadboard
     * can be direction one or two depending on the given array
     * @return symobl breadboardDirection array
     */
    public String[][] getBreadboardDirectionAsStringArray(final Direction[][] dA){
        String[][] arr = new String[dA.length][dA.length];
        for(int i = 0; i < dA.length; i++){
            for(int j = 0; j < dA[i].length; j++){
                arr[i][j] = dA[i][j].getSymbol();
            }
        }
        return arr;
    }


    /**
     * Returns the array of the second directions of items in the Breadboard
     * @return breadboardDirection2 array
     */
    public Direction[][] getBreadboardDirection2(){
        return breadboardDirection2;
    }

    /**
     * Returns the array of BreadBoard items
     * @return
     */
    public List<BreadBoardItem> getBreadBoardItemsList() {
        return breadBoardItemsList;
    }

    //--Setters--

    public void setGamemode(String gamemode) {
        this.gamemode = gamemode;
    }

    public void printTiles(final int w, final int h) {

        String s = "";
        s += w + "," + h + "\n";
        s += "TILES\n";
        s += convertTilesIntoArrayString(
                breadboard,
                "private String [][] defaultBreadboard =",
                w,
                h);
        s += "DIR1\n";
        s += convertTilesIntoArrayString(
                getBreadboardDirectionAsStringArray(
                        breadboardDirection),
                "private Direction [][] defaultBreadboardDirection =",
                w,
                h);
        s += "DIR2\n";
        s += convertTilesIntoArrayString(
                getBreadboardDirectionAsStringArray(
                        breadboardDirection2),
                "private Direction [][] defaultBreadboardDirection2 =",
                w,
                h);
        System.out.println(s);
    }

    private String convertTilesIntoArrayString(final String[][] array, final String name, final int numX, final int numY) {
        boolean isDirection = name.contains("Direction");
        String s = "";

        for (int i = 0; i < numX; i++) {
            for (int j = 0; j < numY; j++) {
                s += array[i][j];
                if(j != numX -1){
                    s += ",";
                }
            }

            s += "\n";
        }

        return s;
    }

}

