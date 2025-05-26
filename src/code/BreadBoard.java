import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
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

    private final int SIZE;

    final static String DEFAULT_KEYWORD = "DEFAULT";
    final static String EDITING_KEYWORD = "EDITING";

    private String gamemode = DEFAULT_KEYWORD;
    public int itemCursor = -1;



    private List<BreadBoardItem> breadBoardItemsList = new ArrayList<BreadBoardItem>();
    private List<CBreadBoardItem> cBreadBoardItemsList = new ArrayList<CBreadBoardItem>();




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
    private String[] itemEnum = {" ","s", "w", "X", "N","O","A","l"};
    //full version
    //private String[] fullItemEnum = {" ","s", "w", "X", "N","O","A","l"};

    //current breadboard stuff; different from default - I think.
    private String[][] breadboard;
    private Direction[][] breadboardDirection;
    private Direction[][] breadboardDirection2;

    private String [][] defaultBreadboard = {
            {" "," ","w","w","w","w","w","w","w","w"," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "},
            {"s","w","w"," "," "," "," ","w"," ","w","w","w","w","w","w","w","w","l"," ","l","w","w","w"," "," "," "," "," "," "," "},
            {" "," ","A","w","w"," ","w","X","w","X","w"," "," "," "," "," "," "," "," "," "," "," ","w"," "," "," "," "," "," "," "},
            {"s","w","w"," ","O","w","O","A"," ","w","w","w","w","w","w","w","w","w","w","w","w","w","X","w","w","w","w"," "," "," "},
            {" ","w","A","w","w"," "," ","w"," ","N"," "," "," ","w"," "," "," "," "," "," "," "," ","w"," "," "," ","w"," "," "," "},
            {"s","X","w","w","w","w","w","w","w","A","A","w","w","X","w","w"," "," "," "," "," "," ","w"," "," "," ","w"," "," "," "},
            {" ","w"," "," "," "," ","w"," "," "," ","N"," "," ","w"," ","O","w","w","w","w"," "," ","w"," "," "," ","w"," "," "," "},
            {" ","w","w","w","w","w","X","w","w","w","w"," "," ","w"," ","w"," "," "," ","w"," "," ","w"," "," "," ","w"," "," "," "},
            {" "," ","w"," ","w"," ","w"," ","w","A","A","w","w","X","w","w"," "," "," ","O","w","w","O","w","w","w","A"," "," "," "},
            {" "," ","A","w","X","w","w"," "," ","N","N"," "," ","w"," ","w"," "," "," ","w"," "," "," "," "," "," ","w"," "," "," "},
            {" "," ","w"," ","w"," ","w","w","w","w","w","w","w","w"," ","O","w","w","w","w"," "," "," "," "," "," ","w"," "," "," "},
            {" "," ","w"," ","w"," "," "," ","w"," "," ","w"," "," "," ","w"," "," "," "," "," "," "," "," "," "," ","w"," "," "," "},
            {" "," ","w"," ","w"," "," "," ","N"," "," ","w"," "," "," ","w"," "," "," "," "," "," "," "," "," "," ","w"," "," "," "},
            {" "," ","w"," ","w"," "," "," ","W","W","A","w"," "," "," ","w"," "," "," "," "," "," "," "," "," "," ","w"," "," "," "},
            {" "," ","w"," ","w"," "," "," "," "," ","w"," "," "," "," ","w"," "," "," "," "," "," "," "," "," "," ","w"," "," "," "},
            {" "," ","w"," ","w","w","w","w","N","W","A","w","w","w","w","w"," "," "," "," "," "," "," "," "," "," ","w"," "," "," "},
            {" "," ","w"," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," ","w"," "," "," "},
            {" "," ","w"," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," ","w"," "," "," "},
            {" "," ","w"," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," ","w"," "," "," "},
            {" "," ","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w"," "," "," "},
            {" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "}
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
        }else if(s.equals("n") || s.equals(" ")){
            return dN;
        }
        return null;
    }

    private BreadBoardItem convertToType(final String ts, final Direction d, int x, final int y){
        switch (ts) {
            case "s":
                return new Switch(false, d, x, y);
            case "S":
                return new Switch(true, d, x, y);
            case "w":
                return new Wire(d, x, y);
            case "W":
                return new Wire(d, x, y);
            case "X":
                return new DoubleWire(d,dN,x,y);
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
            case " ":
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

    public void clickAndChangeBreadBoard(final int type, final int x, final int y){
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
                                convertToType(itemEnum[i + 1], dN, x, y));
                    }
                    breadboardDirection[y][x] = dN;//call this before cuz of repaint in next fn
                    updateBreadBoard(itemEnum[i + 1], x, y);
                    return;
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
            updateBreadBoard(itemEnum[type], x, y);
        }else  {//wire
            //first remove the existing object if it's not a wire
            int theOne = 0;
            if (getBreadBoardItemIndexAtCoordinates(x, y) != -1
            && !breadboard[y][x].toLowerCase().equals(itemEnum[type])) {
                int i = getBreadBoardItemIndexAtCoordinates(x, y);
                breadBoardItemsList.remove(i);
                theOne = i;
            }
            breadBoardItemsList.add(theOne,
                    convertToType(itemEnum[type], dN, x, y));
            breadboardDirection[y][x] = dN;//call this before cuz of repaint in next fn
            updateBreadBoard(itemEnum[type], x, y);
        }
    }

    public BreadBoard(final int size) {
        SIZE = size;
        if(size != Main.DEFAULT_SCREEN_SIZE){
            breadboard = new String[size][size];
            breadboardDirection = new Direction[size][size];
            breadboardDirection2 = new Direction[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {

                    breadboard[i][j] = " ";
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
        setTilesInMain(breadboard);
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
                        updateBreadBoard("l", j, i);//no LED should be on by default
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
                        updateBreadBoard("w", j, i);//no wire should be on by default
                        break;
                    case "X":
                        breadBoardItemsList.add(new DoubleWire(d, d2, j, i));
                        break;
                    case " ":
                        break;
                    default:
                        System.out.println("ERROR LOADING TILES IN BREADBOARD CONSTRUCTOR!");
                        break;
                }
            }
        }
        //setTilesInMain();
    }

    private void setTilesInMain(final String[][] bb) {
        Main.setTiles(SIZE, bb);
    }

    /**
     * Checks clicks and calls update breadboard
     * two different modes; first:
     * checks the click within the boundary of a tile within the Item list
     * @param x
     * @param y
     * @return whether the program should repaint or not
     */
    public boolean checkClick(final MouseEvent e, int x, final int y) {

        if(gamemode.equals(DEFAULT_KEYWORD)) {
            for(CBreadBoardItem cbi:cBreadBoardItemsList){
                if(x >= cbi.getX() * MyGameScreen.tileWidth &&
                        x < cbi.getX() * MyGameScreen.tileWidth + MyGameScreen.tileWidth
                        && y >= cbi.getY() * MyGameScreen.tileHeight &&
                        y < cbi.getY() * MyGameScreen.tileHeight + MyGameScreen.tileHeight) {
                    cbi.set(true);//true is just a necessary thing, not actually used
                    updateBreadBoard(cbi.returnTile(),cbi.getX(),cbi.getY());
                    return true;
                }
            }
        }else if (gamemode.equals(EDITING_KEYWORD)) {
            System.out.println(e.getButton());
            int tile_x = x/MyGameScreen.tileWidth;
            int tile_y = y/MyGameScreen.tileHeight;
            if(e.getButton() == MouseEvent.BUTTON3){//right click
                clickAndChangeBreadBoard(EMPTY_TYPE,tile_x,tile_y);//0 is empty space
            }else if(e.getButton() == MouseEvent.BUTTON1){
                clickAndChangeBreadBoard(itemCursor, tile_x, tile_y);
            }
            return true;
        }

        return false;
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
     * then repaints
     * @param tile
     * @param x
     * @param y
     */
    private void updateBreadBoard(final String tile, final int x, final int y){
        breadboard[y][x] = tile;
        Main.setTiles(SIZE, breadboard);
        callPaint();
    }

    private void setWiresAndLeds(final boolean s, final int x, final int y) {
        if (s) {
            if (breadboard[y][x].equals("w")) {
                Wire wire = (Wire) locateBreadBoardItemOnBoard(x, y);
                assert wire != null;
                wire.setOut(s);
            }
            if (breadboard[y][x].equals("l")) {
                LED led = (LED) locateBreadBoardItemOnBoard(x, y);
                assert led != null;
                led.setOut(s);
            }
        } else {
            if (breadboard[y][x].equals("W")) {
                Wire wire = (Wire) locateBreadBoardItemOnBoard(x, y);
                assert wire != null;
                wire.setOut(s);
            }
            if (breadboard[y][x].equals("L")) {
                LED led = (LED) locateBreadBoardItemOnBoard(x, y);
                assert led != null;
                led.setOut(s);
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
    public void setGates(final boolean s, final int dx, final int dy, final int x, final int y){
        String sBR = breadboard[y+dy][x+dx];//string of the BreadBoard item to the right
        if (sBR.equals("N")) {
            Not not = (Not) locateBreadBoardItemOnBoard(x+dx, y+dy);
            assert not != null;
            if(not.setRightGate(s,dx,dy)) {
                not.calculate();
                not.signal();
            }
        }else if (sBR.equals("A")) {
            And and = (And) locateBreadBoardItemOnBoard(x+dx, y+dy);
            assert and != null;
            if(and.setRightGate(s,dx,dy)) {
                and.calculate();
                and.signal();
            }
        }else if (sBR.equals("O")) {
            Or or = (Or) locateBreadBoardItemOnBoard(x+dx, y+dy);
            assert or != null;
            if(or.setRightGate(s,dx,dy)) {
                or.calculate();
                or.signal();
            }
        }else if(sBR.equals("X")){
            DoubleWire dw = (DoubleWire) locateBreadBoardItemOnBoard(x+dx, y+dy);
            dw.setRightInput(s,dx,dy);
        }
    }

    public void signal(final Direction d, final boolean s, final int x, final int y) {
        //System.out.println(" signal " + s + " from " + x + "," + y);
        if(d == Direction.NONE) {
            for (int i = y - 1; i <= y + 1; i++) {
                for (int j = x - 1; j <= x + 1; j++) {
                    if (i != -1 && j != -1
                            && i < SIZE && j < SIZE
                            && !(i == y - 1 && j == x - 1)
                            && !(i == y + 1 && j == x - 1)
                            && !(i == y - 1 && j == x + 1)
                            && !(i == y + 1 && j == x + 1)
                            && !(i == y && j == x)) {

                        setWiresAndLeds(s, j, i);

                        if(j == x + 1){//gate is to the right of this block

                            setGates(s,1,0,x,y);
//                            if (breadboard[i][j].equals("A")) {
//                                And and = (And) locateBreadBoardItemOnBoard(j, i);
//                                assert and != null;
//                                and.setRightGate(s,1,0);
//                                and.calculate();
//                                and.signal();
//                            }else if (breadboard[i][j].equals("O")) {
//                                Or or = (Or) locateBreadBoardItemOnBoard(j, i);
//                                assert or != null;
//                                or.setRightGate(s,1,0);
//                                or.calculate();
//                                or.signal();
//                            }else if (breadboard[i][j].equals("N")) {
//                                Not not = (Not) locateBreadBoardItemOnBoard(j, i);
//                                assert not != null;
//                                if(not.setRightGate(s,1,0)) {
//                                    not.calculate();
//                                    not.signal();
//                                }
//                            }
                        }else if(j == x - 1){//gate is to the left of this block
                            setGates(s,-1,0,x,y);
//                            if (breadboard[i][j].equals("A")) {
//                                And and = (And) locateBreadBoardItemOnBoard(j, i);
//                                assert and != null;
//                                and.setRightGate(s,-1,0);
//                                and.calculate();
//                                and.signal();
//                            }else if (breadboard[i][j].equals("O")) {
//                                Or or = (Or) locateBreadBoardItemOnBoard(j, i);
//                                assert or != null;
//                                or.setRightGate(s,-1,0);
//                                or.calculate();
//                                or.signal();
//                            }else if (breadboard[i][j].equals("N")) {
//                                Not not = (Not) locateBreadBoardItemOnBoard(j, i);
//                                assert not != null;
//                                if(not.setRightGate(s,-1,0)) {
//                                    not.calculate();
//                                    not.signal();
//                                }
//                            }
                        } else if (i == y + 1) {//gate is below this block
                            setGates(s,0,1,x,y);
//                            if (breadboard[i][j].equals("A")) {
//                                And and = (And) locateBreadBoardItemOnBoard(j, i);
//                                assert and != null;
//                                and.setRightGate(s,0,1);
//                                and.calculate();
//                                and.signal();
//                            }else if (breadboard[i][j].equals("O")) {
//                                Or or = (Or) locateBreadBoardItemOnBoard(j, i);
//                                assert or != null;
//                                or.setRightGate(s,0,1);
//                                or.calculate();
//                                or.signal();
//                            }else if (breadboard[i][j].equals("N")) {
//                                Not not = (Not) locateBreadBoardItemOnBoard(j, i);
//                                assert not != null;
//                                not.setRightGate(s,0,1);
//                                not.calculate();
//                                not.signal();
//                            }
                        }else if (i == y - 1) {//gate is above this block
                            setGates(s,0,-1,x,y);
//                            if (breadboard[i][j].equals("A")) {
//                                And and = (And) locateBreadBoardItemOnBoard(j, i);
//                                assert and != null;
//                                and.setRightGate(s,0,-1);
//                                and.calculate();
//                                and.signal();
//                            }else if (breadboard[i][j].equals("O")) {
//                                Or or = (Or) locateBreadBoardItemOnBoard(j, i);
//                                assert or != null;
//                                or.setRightGate(s,0,-1);
//                                or.calculate();
//                                or.signal();
//                            }else if (breadboard[i][j].equals("N")) {
//                                Not not = (Not) locateBreadBoardItemOnBoard(j, i);
//                                assert not != null;
//                                not.setRightGate(s,0,-1);
//                                not.calculate();
//                                not.signal();
//                            }
                        }
                    }
                }
            }
        }else if (d == Direction.RIGHT){

            setWiresAndLeds(s,x+1,y);
            setGates(s,1,0,x,y);

        }else if (d == Direction.LEFT){
//            String sBR = breadboard[y][x-1];//string of the BreadBoard item to the left
            setWiresAndLeds(s,x-1,y);
            setGates(s,-1,0,x,y);
//            if (sBR.equals("N")) {
//                Not not = (Not) locateBreadBoardItemOnBoard(x-1, y);
//                assert not != null;
//                not.setRightGate(s,-1,0);
//                not.calculate();
//                not.signal();
//            }else if (sBR.equals("A")) {
//                And and = (And) locateBreadBoardItemOnBoard(x-1, y);
//                assert and != null;
//                and.setRightGate(s,-1,0);
//                and.calculate();
//                and.signal();
//            }else if (sBR.equals("O")) {
//                Or or = (Or) locateBreadBoardItemOnBoard(x-1, y);
//                assert or != null;
//                or.setRightGate(s,-1,0);
//                or.calculate();
//                or.signal();
//            }
        }else if (d == Direction.DOWN){
//            String sBR = breadboard[y+1][x];//string of the BreadBoard item below it
            setWiresAndLeds(s,x,y+1);
            setGates(s,0,1,x,y);
//            if (sBR.equals("N")) {
//                Not not = (Not) locateBreadBoardItemOnBoard(x, y+1);
//                assert not != null;
//                not.setRightGate(s,0,1);
//                not.calculate();
//                not.signal();
//            }else if (sBR.equals("A")) {
//                And and = (And) locateBreadBoardItemOnBoard(x, y+1);
//                assert and != null;
//                and.setRightGate(s,0,1);
//                and.calculate();
//                and.signal();
//            }else if (sBR.equals("O")) {
//                Or or = (Or) locateBreadBoardItemOnBoard(x, y+1);
//                assert or != null;
//                or.setRightGate(s,0,1);
//                or.calculate();
//                or.signal();
//            }
        }else if (d == Direction.UP){
//            String sBR = breadboard[y-1][x];//string of the BreadBoard item above
            setWiresAndLeds(s,x,y-1);
            setGates(s,0,-1,x,y);
//            if (sBR.equals("N")) {
//                Not not = (Not) locateBreadBoardItemOnBoard(x, y-1);
//                assert not != null;
//                not.setRightGate(s,0,-1);
//                not.calculate();
//                not.signal();
//            }else if (sBR.equals("A")) {
//                And and = (And) locateBreadBoardItemOnBoard(x, y-1);
//                assert and != null;
//                and.setRightGate(s,0,-1);
//                and.calculate();
//                and.signal();
//            }else if (sBR.equals("O")) {
//                Or or = (Or) locateBreadBoardItemOnBoard(x, y-1);
//                assert or != null;
//                or.setRightGate(s,0,-1);
//                or.calculate();
//                or.signal();
//            }
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
                return "B";
            }else {
                return "b";
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

        public String returnTile(){
            if(out){
                return "S";
            }else {
                return "s";
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
            return "O";
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
                updateBreadBoard("L", getX(), getY());
            } else {
                updateBreadBoard("l", getX(), getY());
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
                updateBreadBoard("W", getX(), getY());
            }else {
                updateBreadBoard("w", getX(), getY());
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
                return "W";
            }else {
                return "w";
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
            return " ";
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

    public void printTiles(final int num) {
        System.out.println(convertTilesIntoArrayString(
                breadboard,
                "private String [][] defaultBreadboard =",
                num));
        System.out.println(convertTilesIntoArrayString(
                getBreadboardDirectionAsStringArray(
                        breadboardDirection),
                "private Direction [][] defaultBreadboardDirection =",
                num));
        System.out.println(convertTilesIntoArrayString(
                getBreadboardDirectionAsStringArray(
                        breadboardDirection2),
                "private Direction [][] defaultBreadboardDirection2 =",
                num));
    }

    private String convertTilesIntoArrayString(final String[][] array, final String name, final int num){
        String s = name + " {";
        boolean isDirection = name.contains("Direction");
        for (int i = 0; i < num; i++) {
            s+="\n";
            s+= "{";
            for (int j = 0; j < num; j++) {

                if(!isDirection) {s += "\"";}
                s += array[i][j];
                if(!isDirection) {s += "\"";}
                s += ",";
                //System.out.print(tiles[i][j]);
                //System.out.print("\"");
                //System.out.print(",");
                //System.out.print("\"");
            }
            s = s.substring(0, s.length() - 1);
            s+="}";
            if(i != num - 1) {
                s+=",";
            }
        }
        s+="\n};";
        return s;
    }

}

