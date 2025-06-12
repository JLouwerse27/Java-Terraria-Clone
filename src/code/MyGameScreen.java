package src.code;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import javax.swing.JComponent;
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
    static int WIDTH;
    static int HEIGHT;
    static int xPixels;
    static int yPixels;
    static int zPixels;
    static int tileSize = 40;
    static int tileWidth;
    static int tileHeight;
    static int originalTileSize = 40;

    int xOffset = 0;
    int yOffset = 0;

    private BreadBoard breadBoard;

    //quality of life things
    private final static Direction dR = Direction.RIGHT;
    private final static Direction dL = Direction.LEFT;
    private final static Direction dU = Direction.UP;
    private final static Direction dD = Direction.DOWN;
    private final static Direction dN = Direction.NONE;

    List<List<List<String>>> tempCutCopyPasteBoardList = new ArrayList<List<List<String>>>();
    List<List<List<Direction>>> tempCutCopyPasteBoardDirection1List = new ArrayList<List<List<Direction>>>();
    List<List<List<Direction>>> tempCutCopyPasteBoardDirection2List = new ArrayList<List<List<Direction>>>();

    String[][][] tempCutCopyPasteBoard;
    Direction[][][] tempCutCopyPasteBoardDirection1;
    Direction[][][] tempCutCopyPasteBoardDirection2;

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
    public MyGameScreen(final int numX, final int numY) {
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
    public MyGameScreen(final int numX, final int numY, final int numZ, final BreadBoard b) {
        WIDTH = 500;
        HEIGHT = 500;
        xPixels = numX;
        yPixels = numY;
        zPixels = numZ;
        tileWidth = tileSize;//WIDTH / numX;
        tileHeight = tileSize;//HEIGHT / numY;
        setSize(WIDTH, HEIGHT);

        tempCutCopyPasteBoard = new String[zPixels][yPixels][xPixels];
        tempCutCopyPasteBoardDirection1 = new Direction[zPixels][yPixels][xPixels];
        tempCutCopyPasteBoardDirection2 = new Direction[zPixels][yPixels][xPixels];

        this.breadBoard = b;
    }
    
    // Constructor initializing the game screen with specified dimensions and tile configuration
    public MyGameScreen(final int width, final int height, final int numX, final int numY) {
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

        if(Main.gameMode == Main.GATES_GAMEMODE_NUMBER) {
            //noinspection DuplicatedCode
            int fontSize = (int) (th / 1.5);
            g2d.setFont(new Font("Arial", Font.BOLD, fontSize));

            int layer = Main.LOGIC_SCREEN_LAYER;

            for (int j = 0; j < yPixels; j++) {
                for (int k = 0; k < xPixels; k++) {

                    //draw the individual tile
                    g2d.setColor(getColour(breadBoard.getBreadboard()[layer][j][k]));
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
                    if(breadBoard.getBreadboard()[layer][j][k].equals(TileString.CrossWire.getSymbol())) {
                        //BreadBoard.DoubleWire dw = (BreadBoard.DoubleWire)breadBoard.getBreadBoardItemsList().get(breadBoard.getBreadBoardItemIndexAtCoordinates(k,j));
                        BreadBoard.DoubleWire dw = (BreadBoard.DoubleWire)breadBoard.locateBreadBoardItemOnBoard(k,j,layer);
                        if(dw.getDir2() == dR) {
                            g2d.drawString(">", k * tw + xOffset, j * th + yOffset + th/2 + (int) (th / 1.5));
                        }else if(dw.getDir2() == dL) {
                            g2d.drawString("<", k * tw + xOffset, j * th + yOffset + th/2 + (int) (th / 1.5));
                        }else if(dw.getDir2() == dD) {
                            g2d.drawString("v", k * tw + xOffset, j * th + yOffset + th/2 + (int) (th / 1.5));
                        }else if(dw.getDir2() == dU) {
                            g2d.drawString("^", k * tw + xOffset, j * th + yOffset + th/2 + (int) (th / 1.5));
                        }
                    }
                }
            }

            paintSignalStuff();

            fontSize = (int) (originalTileSize / 1.5);//don't know if this should be here

//============== COPYING CUTTING AND PASTING OVERLAY (and mode strings) =======================================
            //-- draw a string to show if you're in default or editing mode ----
            if(breadBoard.getGamemode() == BreadBoard.DEFAULT_KEYWORD)
            {
                g2d.drawString("DEFAULT", WIDTH - 140, HEIGHT - 30);

            }else if(breadBoard.getGamemode() == BreadBoard.EDITING_KEYWORD)
            {
                g2d.drawString("EDITING", WIDTH - 140, HEIGHT - 30);
            }else if(breadBoard.getGamemode() == BreadBoard.COPYING_KEYWORD ||
                    breadBoard.getGamemode() == BreadBoard.CUTTING_KEYWORD )
            {
                if(Main.getCopying() || Main.getCutting())
                {
                    int dX = (int)(Main.mouseX[0] - Main.SCREEN_X_OFFSET);
                    int mX0 = (int) (((dX / tileWidth) * tileWidth));

                    int dy = (int)(Main.mouseY[0] - Main.SCREEN_Y_OFFSET);
                    int mY0 = (int) (((dy / tileHeight) * tileHeight));

                    if(Main.getCopying()) {
                        g2d.setColor(Color.CYAN);
                    }else if(Main.getCutting()) {
                        g2d.setColor(Color.RED);
                    }
                    for (int j = 0; j < tempCutCopyPasteBoardList.size(); j++){
                        for (int k = 0; k < tempCutCopyPasteBoardList.get(j).size(); k++){
                            //g2d.setColor(getColour(tempCutCopyPasteBoardList.get(j).get(k)));

                            g2d.fillRect((int)(k*tileWidth + Main.SCREEN_X_OFFSET + mX0),
                                    (int)(j*tileHeight + Main.SCREEN_Y_OFFSET + mY0), tileWidth, tileHeight);
                        }
                    }
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(5));
                    g2d.drawRect(Main.mouseX[0]+ Main.DEFAULT_SCREEN_X_OFFSET,
                            Main.mouseY[0]+ Main.DEFAULT_SCREEN_Y_OFFSET,
                            Main.mouseX[1]-Main.mouseX[0],
                            Main.mouseY[1]-Main.mouseY[0]);
                }
                g2d.setColor(Color.WHITE);
                if(Main.getCopying()) {
                    g2d.drawString("COPYING", WIDTH - 140, HEIGHT - 30);
                }else if(Main.getCutting()) {
                    g2d.drawString("CUTTING", WIDTH - 140, HEIGHT - 30);
                }
            }
        }else if(breadBoard.getGamemode() == BreadBoard.PASTING_KEYWORD)
        {
            g2d.setColor(Color.WHITE);
            g2d.drawString("PASTING", WIDTH - 140, HEIGHT - 30);
        }else
        {
            for (int j = 0; j < yPixels; j++)
            {
                for (int k = 0; k < xPixels; k++)
                {
                    g2d.setColor(getColour(Main.getTiles()[j][k]));
                    g2d.fillRect(k * tw, j * th, tw, th);
                }
            }
        }

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




    // Method to return the color corresponding to the given tile symbol
    public Color getColour(String c) {
        //TERRARIA
        if (c.equals(TileString.Wall.getSymbol())) {
            return Color.BLACK;
        } else if (c.equals(TileString.Player.getSymbol())) {
            return Color.RED;
        } else if (c.equals(TileString.Not.getSymbol())) {
            return Color.ORANGE;
        //BREADBOARD
        }else if (c.equals(TileString.Or.getSymbol())) {
            return Color.GREEN;
        }else if (c.equals(TileString.And.getSymbol())) {
            return Color.BLUE;
        }else if (c.equals(TileString.LEDOff.getSymbol())) {
            //return new Color(210, 210, 210);
            return Color.black;
        }else if (c.equals(TileString.LEDOn.getSymbol())) {
            return Color.WHITE;
        }else if (c.equals(TileString.ButtonOff.getSymbol())) {
            return Color.darkGray;//--
        }else if (c.equals(TileString.ButtonOn.getSymbol())) {
            return Color.lightGray;//--
        }else if (c.equals(TileString.SwitchOff.getSymbol())) {
            return Color.darkGray;
        }else if (c.equals(TileString.SwitchOn.getSymbol())) {
            return Color.lightGray;
        }else if (c.equals(TileString.WireOff.getSymbol())) {
            return new Color(150,0,0);
        }else if (c.equals(TileString.WireOn.getSymbol())) {
            return Color.RED;
        }else if (c.equals(TileString.CrossWire.getSymbol())) {
            return new Color(110,0,0);
        }else if (c.equals(TileString.Resistor1.getSymbol())) {
            return new Color(10,140,10);
        }else if (c.equals(TileString.Resistor3.getSymbol())) {
            return new Color(10,120,10);
        }else if (c.equals(TileString.Resistor5.getSymbol())) {
            return new Color(10,100,10);
        }else {
            return Color.GRAY;
        }
    }

    // Method to update the size of the game screen and recalculate the tile dimensions
    public void updateSize() {
        WIDTH = super.getWidth();
        HEIGHT = super.getHeight();
        super.setSize(WIDTH, HEIGHT);
    }
    //
    public void setTileSize(final int tw, final int th) {
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
