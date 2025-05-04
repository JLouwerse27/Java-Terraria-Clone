import javax.swing.*;
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

    private final int SIZE;

    final static String DEFAULT_KEYWORD = "DEFAULT";
    final static String EDITING_KEYWORD = "EDITING";

    private String gamemode = DEFAULT_KEYWORD;

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
    private String[] itemEnum = {" ","s", "w", "N","O","A","l"};

    private String[][] breadboard;
    private Direction[][] breadboardDirection;

    private String [][] defaultBreadboard = {
            {" "," "," "," "," "," "," "," "," "," "},
            {" ","s","w","w","w","w","w","w"," "," "},
            {" "," ","A","l"," ","N","l","w"," "," "},
            {" ","s","w","w","w","A","O","A"," "," "},
            {" "," "," "," ","w","N","w","w"," "," "},
            {" "," "," "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "," "," "}
    };
    private Direction [][] defaultBreadboardDirection = {
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dR,dN,dR,dR,dN,dR,dD,dN,dN},
            {dN,dR,dR,dN,dN,dD,dN,dD,dN,dN},
            {dN,dN,dN,dR,dN,dR,dU,dL,dN,dN},
            {dN,dR,dN,dR,dR,dR,dR,dU,dN,dN},
            {dN,dD,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dD,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN},
            {dN,dN,dN,dN,dN,dN,dN,dN,dN,dN}
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
    private int getBreadBoardItemAtCoordinates(final int x, final int y) {
        for (int i = 0; i < breadBoardItemsList.size(); i++) {
            if (breadBoardItemsList.get(i).getX() == x && breadBoardItemsList.get(i).getY() == y) {
                System.out.println("found at " + i);
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
    private int getCBreadBoardItemAtCoordinates(final int x, final int y) {
        for (int i = 0; i < cBreadBoardItemsList.size(); i++) {
            if (cBreadBoardItemsList.get(i).getX() == x && cBreadBoardItemsList.get(i).getY() == y) {
                System.out.println("found clickable at " + i);
                return i;
            }
        }
        return -1;
    }

    public void clickAndChangeBreadBoard(final int x, final int y){
        System.out.println("cacbb called");
        //first remove the existing object
        int theOne = 0;
        if(getBreadBoardItemAtCoordinates(x,y) != -1){
            int i = getBreadBoardItemAtCoordinates(x,y);
            breadBoardItemsList.remove(i);
            theOne = i;
        }
        if(getCBreadBoardItemAtCoordinates(x,y) != -1){
            int i = getCBreadBoardItemAtCoordinates(x,y);
            cBreadBoardItemsList.remove(i);
        }

        for (int i = 0; i < itemEnum.length; i++) {
            if(breadboard[y][x].equals(itemEnum[i])){
                if(i+1 >= itemEnum.length) {
                    i = -1;
                }else {
                    breadBoardItemsList.add(theOne,
                            convertToType(itemEnum[i+1],dN,x,y));
                }
                updateBreadBoard(itemEnum[i+1],x,y);
                return;
            }
        }

    }

    public BreadBoard(final int size) {
        SIZE = size;
        if(size != Main.DEFAULT_SCREEN_SIZE){
            breadboard = new String[size][size];
            breadboardDirection = new Direction[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {

                    breadboard[i][j] = " ";
                    breadboardDirection[i][j] = dN;

                    if(i<defaultBreadboard.length && j < defaultBreadboard[i].length){
                        breadboard[i][j] = defaultBreadboard[i][j];
                        breadboardDirection[i][j] = defaultBreadboardDirection[i][j];
                    }
                }
            }
        }else{
            breadboard = defaultBreadboard;
            breadboardDirection = defaultBreadboardDirection;
        }
        setTilesInMain(breadboard);
        for(int i = 0; i < breadboard.length; i++) {
            for(int j = 0; j < breadboard[i].length; j++) {
                Direction d = breadboardDirection[i][j];
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
                        breadBoardItemsList.add(new LED(true, d, j, i));
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
                        updateBreadBoard("w", j, i);
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
    public boolean checkClick(final int x, final int y) {
        if(gamemode.equals(DEFAULT_KEYWORD)) {
            for(CBreadBoardItem cbi:cBreadBoardItemsList){
                if(x >= cbi.getX() * MyGameScreen.tileWidth &&
                        x < cbi.getX() * MyGameScreen.tileWidth + MyGameScreen.tileWidth
                        && y >= cbi.getY() * MyGameScreen.tileHeight &&
                        y < cbi.getY() * MyGameScreen.tileHeight + MyGameScreen.tileHeight) {
                    cbi.set(true);
                    updateBreadBoard(cbi.returnTile(),cbi.getX(),cbi.getY());
                    return true;
                }
            }
        }else if (gamemode.equals(EDITING_KEYWORD)) {
            int tile_x = x/MyGameScreen.tileWidth;
            int tile_y = y/MyGameScreen.tileHeight;
            System.out.println("x and y before clickandchange " + tile_x + "," + tile_y);
            clickAndChangeBreadBoard(tile_x,tile_y);
            return true;
        }

        return false;
    }

    /**
     * Rotates an item on the breadboard; only sif in editing mode.
     * for now just rotates 90 deg to the right
     */
    public void rotateItem(final int x, final int y) {
        if(gamemode.equals(EDITING_KEYWORD)) {
            int num = breadboardDirection[y][x].ordinal() + 1;
            if (num >= Direction.values().length) num = 0;
            breadboardDirection[y][x] = Direction.values()[num];
            if(getBreadBoardItemAtCoordinates(x,y) != -1){
                int i = getBreadBoardItemAtCoordinates(x,y);
                breadBoardItemsList.get(i).setDir(Direction.values()[num]);
            }

            callPaint();
        }
    }

    /**
     * Simply calls paint in main.
     */
    private void callPaint(){
        Main.getMyFrame().repaint();
    }


    /**
     * Updates the breadBoard array
     * @param tile
     * @param x
     * @param y
     */
    private void updateBreadBoard(final String tile, final int x, final int y){
        breadboard[y][x] = tile;
        Main.setTiles(SIZE, breadboard);
        callPaint();
    }

    public void signal(final Direction d, final boolean s, final int x, final int y) {
        //System.out.println("Signal " + s + " from " + x + "," + y);
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
                        if (s) {
                            if (breadboard[i][j].equals("w")) {
                                Wire wire = (Wire) locateItemOnBoard(j, i);
                                assert wire != null;
                                wire.signal(s);
                            }
                            if (breadboard[i][j].equals("l")) {
                                LED led = (LED) locateItemOnBoard(j, i);
                                assert led != null;
                                led.setOn(s);
                            }
                        } else {
                            if (breadboard[i][j].equals("W")) {
                                Wire wire = (Wire) locateItemOnBoard(j, i);
                                assert wire != null;
                                wire.signal(s);
                            }
                            if (breadboard[i][j].equals("L")) {
                                LED led = (LED) locateItemOnBoard(j, i);
                                assert led != null;
                                led.setOn(s);
                            }
                        }
                        if (breadboard[i][j].equals("N")) {
                            Not not = (Not) locateItemOnBoard(j, i);
                            assert not != null;
                            not.setB(s);
                            not.calculate();
                            not.signal();
                        }
                        if(j == x + 1){//gate is to the right of this block
                            if (breadboard[i][j].equals("A")) {
                                And and = (And) locateItemOnBoard(j, i);
                                assert and != null;
                                and.setRightGate(s,1,0);
                                and.calculate();
                                and.signal();
                            }else if (breadboard[i][j].equals("O")) {
                                Or or = (Or) locateItemOnBoard(j, i);
                                assert or != null;
                                or.setRightGate(s,1,0);
                                or.calculate();
                                or.signal();
                            }
                        } else if (i == y + 1) {//gate is below this block
                            if (breadboard[i][j].equals("A")) {
                                And and = (And) locateItemOnBoard(j, i);
                                assert and != null;
                                and.setRightGate(s,0,1);
                                and.calculate();
                                and.signal();
                            }else if (breadboard[i][j].equals("O")) {
                                Or or = (Or) locateItemOnBoard(j, i);
                                assert or != null;
                                or.setRightGate(s,0,1);
                                or.calculate();
                                or.signal();
                            }
                        }else if (i == y - 1) {//gate is above this block
                            if (breadboard[i][j].equals("A")) {
                                And and = (And) locateItemOnBoard(j, i);
                                assert and != null;
                                and.setRightGate(s,0,-1);
                                and.calculate();
                                and.signal();
                            }else if (breadboard[i][j].equals("O")) {
                                Or or = (Or) locateItemOnBoard(j, i);
                                assert or != null;
                                or.setRightGate(s,0,-1);
                                or.calculate();
                                or.signal();
                            }
                        }
                    }
                }
            }
        }else if (d == Direction.RIGHT){
            String sBR = breadboard[y][x+1];//string of the BreadBoard item to the right
            if (s) {
                if (sBR.equals("w")) {
                    Wire wire = (Wire) locateItemOnBoard(x+1, y);
                    assert wire != null;
                    wire.signal(s);
                }
                if (sBR.equals("l")) {
                    LED led = (LED) locateItemOnBoard(x+1, y);
                    assert led != null;
                    led.setOn(s);
                }
            } else {
                if (sBR.equals("W")) {
                    Wire wire = (Wire) locateItemOnBoard(x+1, y);
                    assert wire != null;
                    wire.signal(s);
                }
                if (sBR.equals("L")) {
                    LED led = (LED) locateItemOnBoard(x+1, y);
                    assert led != null;
                    led.setOn(s);
                }
            }
            if (sBR.equals("N")) {
                Not not = (Not) locateItemOnBoard(x+1, y);
                assert not != null;
                not.setB(s);
                not.calculate();
                not.signal();
            }else if (sBR.equals("A")) {
                And and = (And) locateItemOnBoard(x+1, y);
                assert and != null;
                and.setRightGate(s,1,0);
                and.calculate();
                and.signal();
            }else if (sBR.equals("O")) {
                Or or = (Or) locateItemOnBoard(x+1, y);
                assert or != null;
                or.setRightGate(s,1,0);
                or.calculate();
                or.signal();
            }
        }else if (d == Direction.LEFT){
            String sBR = breadboard[y][x-1];//string of the BreadBoard item to the left
            if (s) {
                if (sBR.equals("w")) {
                    Wire wire = (Wire) locateItemOnBoard(x-1, y);
                    assert wire != null;
                    wire.signal(s);
                }
                if (sBR.equals("l")) {
                    LED led = (LED) locateItemOnBoard(x-1, y);
                    assert led != null;
                    led.setOn(s);
                }
            } else {
                if (sBR.equals("W")) {
                    Wire wire = (Wire) locateItemOnBoard(x-1, y);
                    assert wire != null;
                    wire.signal(s);
                }
                if (sBR.equals("L")) {
                    LED led = (LED) locateItemOnBoard(x-1, y);
                    assert led != null;
                    led.setOn(s);
                }
            }
            if (sBR.equals("N")) {
                Not not = (Not) locateItemOnBoard(x-1, y);
                assert not != null;
                not.setB(s);
                not.calculate();
                not.signal();
            }else if (sBR.equals("A")) {
                And and = (And) locateItemOnBoard(x-1, y);
                assert and != null;
                and.setRightGate(s,-1,0);
                and.calculate();
                and.signal();
            }else if (sBR.equals("O")) {
                Or or = (Or) locateItemOnBoard(x-1, y);
                assert or != null;
                or.setRightGate(s,-1,0);
                or.calculate();
                or.signal();
            }
        }else if (d == Direction.DOWN){
            String sBR = breadboard[y+1][x];//string of the BreadBoard item below it
            if (s) {
                if (sBR.equals("w")) {
                    Wire wire = (Wire) locateItemOnBoard(x, y+1);
                    wire.signal(s);
                }
                if (sBR.equals("l")) {
                    LED led = (LED) locateItemOnBoard(x, y+1);
                    assert led != null;
                    led.setOn(s);
                }

            } else {
                if (sBR.equals("W")) {
                    Wire wire = (Wire) locateItemOnBoard(x, y+1);
                    wire.signal(s);
                }
                if (sBR.equals("L")) {
                    LED led = (LED) locateItemOnBoard(x, y+1);
                    assert led != null;
                    led.setOn(s);
                }
            }
            if (sBR.equals("N")) {
                Not not = (Not) locateItemOnBoard(x, y+1);
                assert not != null;
                not.setB(s);
                not.calculate();
                not.signal();
            }else if (sBR.equals("A")) {
                And and = (And) locateItemOnBoard(x, y+1);
                assert and != null;
                and.setRightGate(s,0,1);
                and.calculate();
                and.signal();
            }else if (sBR.equals("O")) {
                Or or = (Or) locateItemOnBoard(x, y+1);
                assert or != null;
                or.setRightGate(s,0,1);
                or.calculate();
                or.signal();
            }
        }else if (d == Direction.UP){
            String sBR = breadboard[y-1][x];//string of the BreadBoard item above
            if (s) {
                if (sBR.equals("w")) {
                    Wire wire = (Wire) locateItemOnBoard(x, y-1);
                    wire.signal(s);
                }
                if (sBR.equals("l")) {
                    LED led = (LED) locateItemOnBoard(x, y-1);
                    assert led != null;
                    led.setOn(s);
                }
            } else {
                if (sBR.equals("W")) {
                    Wire wire = (Wire) locateItemOnBoard(x, y-1);
                    wire.signal(s);
                }
                if (sBR.equals("L")) {
                    LED led = (LED) locateItemOnBoard(x, y-1);
                    assert led != null;
                    led.setOn(s);
                }
            }
            if (sBR.equals("N")) {
                Not not = (Not) locateItemOnBoard(x, y-1);
                assert not != null;
                not.setB(s);
                not.calculate();
                not.signal();
            }else if (sBR.equals("A")) {
                And and = (And) locateItemOnBoard(x, y-1);
                assert and != null;
                and.setRightGate(s,0,-1);
                and.calculate();
                and.signal();
            }else if (sBR.equals("O")) {
                Or or = (Or) locateItemOnBoard(x, y-1);
                assert or != null;
                or.setRightGate(s,0,-1);
                or.calculate();
                or.signal();
            }
        }
    }

    /**
     * Button, extends cBreadBoardItem
     */
    private class Button extends CBreadBoardItem {

        private boolean on  = false;

        public Button(boolean on, Direction dir, int x, int y) {
            super(dir,x,y);
            this.on = on;
        }

        public void set(boolean on) {
            this.on = on;
        }

        public String returnTile(){
            if(on){
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

        private boolean on  = false;

        public Switch(boolean on, Direction dir, int x, int y) {
            super(dir,x,y);
            this.on = on;
        }

        public void set(final boolean b) {
            on = !on;
            signal(on);
        }

        public String returnTile(){
            if(on){
                return "S";
            }else {
                return "s";
            }
        }

        public void signal(final boolean s) {
            BreadBoard.this.signal(this.getDir(),s, this.getX(), this.getY());
        }

    }

    /**
     * locates the corresponding list item from the breadboard
     * @param x
     * @param y
     * @return
     */
    private BreadBoardItem locateItemOnBoard(int x, int y) {
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
    private abstract class Gate extends BreadBoardItem {

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
        public void setRightGate(final boolean s, final int deltax, final int deltay){
            //this gate is to the right of the input (ex. wire)
            if(deltax == 1){
                if(this.getDir() == Direction.RIGHT) {
                    setB(s);
                } else if (this.getDir() == Direction.UP) {
                    setA(s);
                }else if (this.getDir() == Direction.DOWN) {
                    setC(s);
                }
            }else if(deltax == -1){//this gate is to the left of the input (ex. wire)
                if(this.getDir() == Direction.LEFT) {
                    setB(s);
                } else if (this.getDir() == Direction.UP) {
                    setC(s);
                }else if (this.getDir() == Direction.DOWN) {
                    setA(s);
                }
            }else if(deltay == 1){//this gate is below the input (ex. wire)
                if(this.getDir() == Direction.RIGHT) {
                    setA(s);
                } else if (this.getDir() == Direction.LEFT) {
                    setC(s);
                }else if (this.getDir() == Direction.DOWN) {
                    setB(s);
                }
            }else if(deltay == -1){//this gate is above the input (ex. wire)
                if(this.getDir() == Direction.RIGHT) {
                    setC(s);
                } else if (this.getDir() == Direction.LEFT) {
                    setA(s);
                }else if (this.getDir() == Direction.UP) {
                    setB(s);
                }
            }
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
            System.out.println("A is " + A);
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

        private boolean on = false;

        public LED(final boolean on, final Direction dir, final int x, final int y) {
            super(dir, x, y);
            this.on = on;
        }

        public void setOn(final boolean on) {
            delay();
            this.on = on;
            if(on) {
                updateBreadBoard("L", getX(), getY());
            }else {
                updateBreadBoard("l", getX(), getY());
            }
            Main.getMyFrame().repaint();
        }

        private void delay() {
            //delay
        }

        public String returnTile(){
            if(on){
                return "L";
            }else {
                return "l";
            }
        }

    }

    /**
     * Wire
     */
    private class Wire extends BreadBoardItem {

        private boolean on = false;

        public Wire(final Direction dir, final int x, final int y) {
            super(dir, x, y);
        }

        public void setOn(final boolean on) {
            delay();
            this.on = on;
            if(on) {
                updateBreadBoard("W", getX(), getY());
            }else {
                updateBreadBoard("w", getX(), getY());
            }
            Main.getMyFrame().repaint();
        }

        private void delay() {
            //delay
        }

        public String returnTile(){
            if(on){
                return "W";
            }else {
                return "w";
            }
        }

        /**
         * Like the signal in Switch, searches for other board members.
         * But first turns itself on/off.
         */
        public void signal(final boolean s) {
            setOn(s);
            BreadBoard.this.signal(this.getDir(),s,this.getX(),this.getY());
        }

    }

    /**
     * Abstract Parent Class for the breadboard
     */
    private abstract class BreadBoardItem { //extends Thread {
        private int x = 0;
        private int y = 0;
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

        public void setDir(final Direction dir){
            this.dir = dir;
        }

        public String returnTile(){
            return " ";
        }
    }

    private abstract class CBreadBoardItem extends BreadBoardItem {

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

    //--Setters--

    public void setGamemode(String gamemode) {
        this.gamemode = gamemode;
    }



}

