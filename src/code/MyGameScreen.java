package src.code;

import java.awt.*;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import javax.swing.JComponent;
import static src.code.Main.*;

/**
 * MyGameScreen is a custom JComponent that represents the game screen.
 * It handles the rendering of the game tiles based on the provided dimensions and tile information.
 * 
 * The class provides multiple constructors to initialize the game screen with different configurations.
 * It also includes methods to paint the game screen, determine tile colors, and update the screen size.
 * 
 * Fields:
 * - WIDTH: The width of the game screen.
 * - HEIGHT: The height of the game screen.
 * - xPixels: The number of horizontal tiles.
 * - yPixels: The number of vertical tiles.
 * - tileSize: The size of each tile (deprecated).
 * - tileWidth: The width of each tile.
 * - tileHeight: The height of each tile.
 * 
 * Constructors:
 * - MyGameScreen(): Initializes the game screen with default dimensions and tile configuration.
 * - MyGameScreen(int numX, int numY): Initializes the game screen with specified number of horizontal and vertical tiles.
 * - MyGameScreen(int width, int height, int numX, int numY): Initializes the game screen with specified dimensions and tile configuration.
 * 
 * Methods:
 * - paint(Graphics g): Renders the game screen by drawing the tiles based on their colors.
 * - getColour(String c): Returns the color corresponding to the given tile symbol.
 * - updateSize(): Updates the size of the game screen and recalculates the tile dimensions.
 * - setPixels(int numX, int numY): Sets the number of horizontal and vertical tiles (currently commented out).
 */
public class MyGameScreen extends JComponent {
    
    // Static fields for screen dimensions and tile configuration
    static short WIDTH;
    static short HEIGHT;
    static short xPixels;
    static short yPixels;
    static short zPixels;
    static byte tileSize = 40;
    static byte tileWidth;
    static byte tileHeight;
    static byte originalTileSize = 40;

    static final short MINIMAP_TILE_SIZE = 4;

    int xOffset = 0;
    int yOffset = 0;

    private BreadBoard breadBoard;

    //quality of life things
    private final static Direction dR = Direction.RIGHT;
    private final static Direction dL = Direction.LEFT;
    private final static Direction dU = Direction.UP;
    private final static Direction dD = Direction.DOWN;
    private final static Direction dN = Direction.NONE;
    private final static Direction dO = Direction.OUTOF;
    private final static Direction dI = Direction.INTO;

    List<List<List<Byte>>> tempCutCopyPasteBoardList = new ArrayList<List<List<Byte>>>();
    List<List<List<Direction>>> tempCutCopyPasteBoardDirection1List = new ArrayList<List<List<Direction>>>();
    List<List<List<Direction>>> tempCutCopyPasteBoardDirection2List = new ArrayList<List<List<Direction>>>();

    //String[][][] tempCutCopyPasteBoard;
    byte[][][] cutCopyPasteBoardBytes;
    Direction[][][] cutCopyPasteBoardDirection1;
    Direction[][][] cutCopyPasteBoardDirection2;


    private Graphics2D g2d;

    private Graphics ng;

    // Default constructor initializing the game screen with default dimensions and tile configuration
    public MyGameScreen() {
        WIDTH = 500;
        HEIGHT = 500;
        xPixels = 50;
        yPixels = 50;
        tileSize = 10;
        tileWidth = tileSize;
        tileHeight = tileSize;
        setSize(WIDTH, HEIGHT);
    }

    // Constructor initializing the game screen with specified number of horizontal and vertical tiles
    public MyGameScreen(final short numX, final short numY) {
        WIDTH = 500;
        HEIGHT = 500;
        xPixels = numX;
        yPixels = numY;
        tileWidth = tileSize;//WIDTH / numX;
        tileHeight = tileSize;//HEIGHT / numY;
        setSize(WIDTH, HEIGHT);
    }

    /** --For BreadBoard--
     * Constructor initializing the game screen with specified number of horizontal and vertical tiles
     * with a BreadBoard object
     * @param numX
     * @param numY
     * @param b BreadBoard
     */
    public MyGameScreen(final short numX, final short numY, final byte numZ, final BreadBoard b) {
        WIDTH = 500;
        HEIGHT = 500;
        xPixels = numX;
        yPixels = numY;
        zPixels = numZ;
        tileWidth = tileSize;//WIDTH / numX;
        tileHeight = tileSize;//HEIGHT / numY;
        setSize(WIDTH, HEIGHT);

        //tempCutCopyPasteBoard = new String[zPixels][yPixels][xPixels];
        cutCopyPasteBoardBytes = new byte[zPixels][yPixels][xPixels];
        cutCopyPasteBoardDirection1 = new Direction[zPixels][yPixels][xPixels];
        cutCopyPasteBoardDirection2 = new Direction[zPixels][yPixels][xPixels];

        this.breadBoard = b;
    }
    
    // Constructor initializing the game screen with specified dimensions and tile configuration
    public MyGameScreen(final short width, final short height, final short numX, final short numY) {
        WIDTH = width;
        HEIGHT = height;
        xPixels = numX;
        yPixels = numY;
        tileWidth = tileSize;//WIDTH / numX;
        tileHeight = tileSize;//HEIGHT / numY;
        setSize(width, height);
    }

    /**
     * Method to render the game screen by drawing the tiles based on their colors
     */
    @Override
    public void paint(Graphics g) {

        g2d = (Graphics2D) g;
        updateSize();

        int tw = (int) tileWidth;
        int th = (int) tileHeight;

        int bufferPixels = 1;

        // last block horizontal */
        int lbx = (int) (-SCREEN_X_OFFSET - DEFAULT_SCREEN_X_OFFSET + WIDTH) / tileSize + bufferPixels;
        // last block vertical */
        int lby = (int) (-SCREEN_Y_OFFSET - DEFAULT_SCREEN_Y_OFFSET + HEIGHT) / tileSize + bufferPixels;

        if(lbx > xPixels) lbx = xPixels;
        if(lby > yPixels) lby = yPixels;


        if(Main.gameMode == Main.GATES_GAMEMODE_NUMBER) {
            //noinspection DuplicatedCode
            int fontSize = (int) (th / 1.5);
            g2d.setFont(new Font("Arial", Font.BOLD, fontSize));

            //draw mouse coordinates on screen


            int layer = Main.LOGIC_SCREEN_LAYER;

            for (int j = 0; j < lby; j++) {
                for (int k = 0; k < lbx; k++) {

                    //draw the individual tile
                    g2d.setColor(getColour(breadBoard.getBreadboardByte()[layer][j][k]));
                    g2d.fillRect(k * tw + xOffset, j * th + yOffset, tw, th);

                    g2d.setColor(Color.WHITE);
                    //draw the direction
                    if(breadBoard.getBreadboardDirection()[layer][j][k].equals(Direction.RIGHT)) {
                        g2d.drawString(">", k * tw + xOffset, j * th + yOffset + th/2);
                    }else if(breadBoard.getBreadboardDirection()[layer][j][k].equals(Direction.LEFT)) {
                        g2d.drawString("<", k * tw + xOffset, j * th + yOffset + th/2);
                    }else if(breadBoard.getBreadboardDirection()[layer][j][k].equals(Direction.DOWN)) {
                        g2d.drawString("v", k * tw + xOffset, j * th + yOffset + th/2);
                    }else if(breadBoard.getBreadboardDirection()[layer][j][k].equals(Direction.UP)) {
                        g2d.drawString("^", k * tw + xOffset, j * th + yOffset + th/2);
                    }else if(breadBoard.getBreadboardDirection()[layer][j][k].equals(Direction.INTO)) {
                        g2d.drawString("O", k * tw + xOffset, j * th + yOffset + th/2);
                    }else if(breadBoard.getBreadboardDirection()[layer][j][k].equals(Direction.OUTOF)) {
                        g2d.drawString("+", k * tw + xOffset, j * th + yOffset + th/2);
                    }
                    if(breadBoard.getBreadboardByte()[layer][j][k] == (TileByte.DoubleWire).getSymbol()
                    || breadBoard.getBreadboardByte()[layer][j][k] == TileByte.WireOn.getSymbol()
                    || breadBoard.getBreadboardByte()[layer][j][k] == TileByte.WireOff.getSymbol()
                    && breadBoard.getBreadboardDirection2()[layer][j][k] != dN) {
                        //BreadBoard.DoubleWire dw = (BreadBoard.DoubleWire)breadBoard.getBreadBoardItemsList().get(breadBoard.getBreadBoardItemIndexAtCoordinates(k,j));
                        //BreadBoard.Wire w = (BreadBoard.Wire)breadBoard.locateBreadBoardItemOnBoard(k,j,layer);

                        if(breadBoard.getBreadboardDirection2()[layer][j][k] != dN) {
                            if (breadBoard.getBreadboardDirection2()[layer][j][k] == dR) {
                                g2d.drawString(">", k * tw + xOffset, j * th + yOffset + th / 2 + (int) (th / 1.5));
                            } else if (breadBoard.getBreadboardDirection2()[layer][j][k] == dL) {
                                g2d.drawString("<", k * tw + xOffset, j * th + yOffset + th / 2 + (int) (th / 1.5));
                            } else if (breadBoard.getBreadboardDirection2()[layer][j][k] == dD) {
                                g2d.drawString("v", k * tw + xOffset, j * th + yOffset + th / 2 + (int) (th / 1.5));
                            } else if (breadBoard.getBreadboardDirection2()[layer][j][k] == dU) {
                                g2d.drawString("^", k * tw + xOffset, j * th + yOffset + th / 2 + (int) (th / 1.5));
                            } else if (breadBoard.getBreadboardDirection2()[layer][j][k] == dI) {
                                g2d.drawString("O", k * tw + xOffset, j * th + yOffset + th / 2 + (int) (th / 1.5));
                            } else if (breadBoard.getBreadboardDirection2()[layer][j][k] == dO) {
                                g2d.drawString("^", k * tw + xOffset, j * th + yOffset + th / 2 + (int) (th / 1.5));
                            }
                        }
                    }
                }
            }
            paintSignalStuff();
            paintTempArrayStuff();

            fontSize = (int) (originalTileSize / 1.5);//don't know if this should be here
            paintMouseCoordinates();

//============== COPYING CUTTING AND PASTING OVERLAY (and mode strings) =======================================
            //-- draw a string to show if you're in default or editing mode ----
            if(breadBoard.getGamemode() == BreadBoard.DEFAULT_KEYWORD)
            {
                g2d.setColor(Color.WHITE);
                g2d.drawString("DEFAULT", WIDTH - 140, HEIGHT - 30);

            }else if(breadBoard.getGamemode() == BreadBoard.EDITING_KEYWORD)
            {
                g2d.setColor(Color.WHITE);
                g2d.drawString("EDITING", WIDTH - 140, HEIGHT - 30);
            }else if(breadBoard.getGamemode() == BreadBoard.COPYING_KEYWORD ||
                    breadBoard.getGamemode() == BreadBoard.CUTTING_KEYWORD ) //IF THE USER HAS CLICKED CTL C OR CTL V
            {
                if(Main.getCopying() || Main.getCutting()) //NOT REDUNDANT, THIS IS WHEN USER IS DRAGGING
                {
                    int dx = (mouseX[0] - xOffset + DEFAULT_SCREEN_X_OFFSET) /
                            (tw) * tw + xOffset;

                    int dy = (mouseY[0] - yOffset + DEFAULT_SCREEN_Y_OFFSET) /
                             (th) * th + yOffset;
//                    if (
//                        (double)((mouseY[0] - yOffset + DEFAULT_SCREEN_Y_OFFSET))
//                                - (Math.floor((double) (mouseY[0] - yOffset + DEFAULT_SCREEN_Y_OFFSET) /
//                                        (th)))
//                                * th > 0){
//                        dy+=th;
//                    }
                                    //mouseY[0] + DEFAULT_SCREEN_Y_OFFSET; //- yOffset % tileSize;//(int)((mouseY[0])/tileHeight)*tileHeight;

                    System.out.println("mouseY[0] = " + mouseY[0] + "mouseY[1] = " + mouseY[1] +" dy = " + dy);

                    if(Main.getCopying()) {
                        g2d.setColor(Color.CYAN);
                    }else if(Main.getCutting()) {
                        g2d.setColor(Color.RED);
                    }
                    if(tempCutCopyPasteBoardList != null
                            && tempCutCopyPasteBoardList.size() > 0
                            && tempCutCopyPasteBoardList.getFirst().size() > 0) {
                        for (int j = 0; j < tempCutCopyPasteBoardList.get(layer).size(); j++) {
                            for (int k = 0; k < tempCutCopyPasteBoardList.get(layer).get(j).size(); k++) {
                                //g2d.setColor(getColour(tempCutCopyPasteBoardList.get(j).get(k)));

                                g2d.fillRect(
                                        (int) (k * tw + dx),
                                        (int) (j * th + dy),
                                        tw,
                                        th);
                            }
                        }
                    }
                    g2d.setColor(Color.WHITE);
                    g2d.drawRect(mouseX[0]+ DEFAULT_SCREEN_X_OFFSET,
                            mouseY[0]+ DEFAULT_SCREEN_Y_OFFSET,
                            mouseX[1]-mouseX[0],
                            mouseY[1]-mouseY[0]);
                }
                g2d.setColor(Color.WHITE);
                if(Main.getCopying()) {
                    g2d.drawString("COPYING", WIDTH - 140, HEIGHT - 30);
                }else if(Main.getCutting()) {
                    g2d.drawString("CUTTING", WIDTH - 140, HEIGHT - 30);
                }
            }else if(breadBoard.getGamemode() == BreadBoard.PASTING_KEYWORD)
            {
                g2d.setColor(Color.WHITE);
                g2d.drawString("PASTING", WIDTH - 140, HEIGHT - 30);
            }
        }else //terraria and assembler
        {
            g2d.setColor(Color.BLACK);
            g2d.drawString("PAUSED", WIDTH - 140, HEIGHT - 30);
        }

    }

    /**
     * Paints the mouse coordinates at the bottom right of the screen
     */
    private void paintMouseCoordinates() {
        g2d.setColor(Color.WHITE);
        int x = mouseX[0] - SCREEN_X_OFFSET + DEFAULT_SCREEN_X_OFFSET;
        int y = mouseY[0] - SCREEN_Y_OFFSET + DEFAULT_SCREEN_Y_OFFSET;
        g2d.drawString(x / tileWidth + "," + y / tileHeight, WIDTH - 240, HEIGHT - 30);
    }


    public void paintSignalStuff() {
        Object[][] localCopy = new Object[breadBoard.getSignalArray().length][];
        for (int j = 0; j < breadBoard.getSignalArray().length; j++) {
            if (breadBoard.getSignalArray()[j] != null) {
                localCopy[j] = Arrays.copyOf(breadBoard.getSignalArray()[j], breadBoard.getSignalArray()[j].length);
            }
        }

        int fontSize = 20;
        g2d.setFont(new Font("Arial", Font.BOLD, fontSize));

        //if(!breadBoard.cleaningSignalArray) {

        for (int k = 0; k <= BreadBoard.SIGNAL_ARRAY_LAST_PLACE; k++) {
            String s = "";
            switch (k) {
                case 0:
                    s = "D";
                    break;
                case 1:
                    s = "E";
                    break;
                case 2:
                    s = "X";
                    break;
                case 3:
                    s = "Y";
                    break;
                case 4:
                    s = "Z";
                    break;
                case 5:
                    s = "T";
                    break;

                default:
                    System.out.println("MyGameScreen.paintSignalStuff(): whattayadoinghere");
            }
            g2d.setColor(Color.black);
            g2d.drawString(s,k * 63 + (WIDTH - 363), 21);
        }
        g2d.drawString("TICK: " + Main.tickNumber, -102 + (WIDTH - 363), 21);
        for (int j = 0; j < localCopy.length; j++) {
            if(localCopy[j] != null){
                //System.out.println(Arrays.toString(breadBoard.getSignalArray()[j]));
                for (int k = 0; k < breadBoard.getSignalArray()[j].length; k++) {
                    if(breadBoard.getSignalArray()[j][k] != null) {
                        g2d.setColor(Color.black);
                        g2d.drawRect(k * 63 + (WIDTH - 363), j * 21 + 21, 62, 20);

                        fontSize = 20;
                        g2d.setFont(new Font("Arial", Font.BOLD, fontSize));
                        g2d.drawString(breadBoard.getSignalArray()[j][k].toString(),
                                k * 63 + (WIDTH - 363),
                                j * 21 + 42);
                    }
                }
            }

            for (int k = 0; k <= BreadBoard.SIGNAL_ARRAY_LAST_PLACE; k++) {
                g2d.setColor(Color.black);
                g2d.drawRect(k * 63 + (WIDTH - 363), j * 21 + 21, 62, 20);
            }

            g2d.drawString(String.valueOf(j),
                    WIDTH - 389,
                    j * 21 + 41);
        }
        //}
    }

    /**
     * Paints what is copied or cut from the clipboard.
     */
    public void paintTempArrayStuff() {
        int layer = LOGIC_SCREEN_LAYER;
        if(tempCutCopyPasteBoardList.isEmpty()){
            return;
        }
//        for (int j = 0; j < cutCopyPasteBoardBytes[layer].length; j++){
//            for(int k = 0; k < cutCopyPasteBoardBytes[layer][j].length; k++){
        for (int i = 0; i < tempCutCopyPasteBoardList.size(); i++) {
            for (int j = 0; j < tempCutCopyPasteBoardList.getFirst().size(); j++) {
                for (int k = 0; k < tempCutCopyPasteBoardList.getFirst().getFirst().size(); k++) {
                    g2d.setColor(getColour(tempCutCopyPasteBoardList.get(i).get(j).get(k)));
                    g2d.fillRect(
                            WIDTH - 500 + k*MINIMAP_TILE_SIZE,
                            21 + i*(tempCutCopyPasteBoardList.getFirst().size()+MINIMAP_TILE_SIZE) + j*MINIMAP_TILE_SIZE,
                            MINIMAP_TILE_SIZE,
                            MINIMAP_TILE_SIZE);
                }
            }
        }

//                if(CutCopyPasteBoardBytes[layer][j][k] == null){
//                    return;
//                }
    }

    public Color getColour(byte c) {
        //TERRARIA
        if (c == TileByte.Not.getSymbol()) {
            return Color.ORANGE;
            //BREADBOARD
        }else if (c == TileByte.Or.getSymbol()) {
            return Color.GREEN;
        }else if (c == TileByte.And.getSymbol()) {
            return Color.BLUE;
        }else if (c == TileByte.LEDOff.getSymbol()) {
            //return new Color(210, 210, 210);
            return Color.black;
        }else if (c == TileByte.LEDOn.getSymbol()) {
            return Color.WHITE;
        }else if (c == TileByte.ButtonOff.getSymbol()) {
            return Color.darkGray;//--
        }else if (c == TileByte.ButtonOn.getSymbol()) {
            return Color.lightGray;//--
        }else if (c == TileByte.SwitchOff.getSymbol()) {
            return Color.darkGray;
        }else if (c == TileByte.SwitchOn.getSymbol()) {
            return Color.lightGray;
        }else if (c == TileByte.WireOff.getSymbol()) {
            return new Color(150,0,0);
        }else if (c == TileByte.WireOn.getSymbol()) {
            return Color.RED;
        }else if (c == TileByte.DoubleWire.getSymbol()) {
            return new Color(110,0,0);
        }else if (c == TileByte.Resistor1.getSymbol()) {
            return new Color(10,140,10);
        }else if (c == TileByte.Resistor3.getSymbol()) {
            return new Color(10,120,10);
        }else if (c == TileByte.Resistor5.getSymbol()) {
            return new Color(10,100,10);
        }else if (c == TileByte.Resistor10.getSymbol()) {
            return new Color(10,80,10);
        }else if (c == TileByte.Xor.getSymbol()) {
            return new Color(200,120,20);
        }else {
            //System.out.println("paint(): given c: " + c);
            return Color.GRAY;
        }
    }

    // Method to update the size of the game screen and recalculate the tile dimensions
    public void updateSize() {
        WIDTH = (short) super.getWidth();
        HEIGHT = (short) super.getHeight();
        super.setSize(WIDTH, HEIGHT);
    }
    //
    public void setTileSize(final byte tw, final byte th) {
        tileWidth = tw;
        tileHeight = th;
    }

    // Method to set the number of horizontal and vertical tiles (currently commented out)
    public void setPixels(int numX, int numY) {
        // xPixels = numX;
        // yPixels = numY;
        // tileSize = 10;
    }
}
