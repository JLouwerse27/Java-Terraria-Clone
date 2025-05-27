import java.awt.*;
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

    List<List<String>> tempCutCopyPasteBoardList = new ArrayList<List<String>>();
    List<List<Direction>> tempCutCopyPasteBoardDirection1List = new ArrayList<List<Direction>>();
    List<List<Direction>> tempCutCopyPasteBoardDirection2List = new ArrayList<List<Direction>>();
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
    public MyGameScreen(final int numX, final int numY, final BreadBoard b) {
        WIDTH = 500;
        HEIGHT = 500;
        xPixels = numX;
        yPixels = numY;
        tileWidth = tileSize;//WIDTH / numX;
        tileHeight = tileSize;//HEIGHT / numY;
        setSize(WIDTH, HEIGHT);
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

        Graphics2D g2d = (Graphics2D) g;
        updateSize();

        int tw = (int) tileWidth;
        int th = (int) tileHeight;

        if(Main.gameMode == Main.GATES_GAMEMODE_NUMBER) {
            //noinspection DuplicatedCode
            int fontSize = (int) (th / 1.5);
            g2d.setFont(new Font("Arial", Font.BOLD, fontSize));

            for (int i = 0; i < yPixels; i++) {
                for (int j = 0; j < xPixels; j++) {
                    g2d.setColor(getColour(breadBoard.getBreadboard()[i][j]));
                    g2d.fillRect(j * tw + xOffset, i * th + yOffset, tw, th);
                    g2d.setColor(Color.WHITE);
                    if(breadBoard.getBreadboardDirection()[i][j].equals(Direction.RIGHT)) {
                        g2d.drawString(">", j * tw + xOffset, i * th + yOffset + th/2);
                    }else if(breadBoard.getBreadboardDirection()[i][j].equals(Direction.LEFT)) {
                        g2d.drawString("<", j * tw + xOffset, i * th + yOffset + th/2);
                    }else if(breadBoard.getBreadboardDirection()[i][j].equals(Direction.DOWN)) {
                        g2d.drawString("v", j * tw + xOffset, i * th + yOffset + th/2);
                    }else if(breadBoard.getBreadboardDirection()[i][j].equals(Direction.UP)) {
                        g2d.drawString("^", j * tw + xOffset, i * th + yOffset + th/2);
                    }
                    if(breadBoard.getBreadboard()[i][j].equals("X")) {
                        //BreadBoard.DoubleWire dw = (BreadBoard.DoubleWire)breadBoard.getBreadBoardItemsList().get(breadBoard.getBreadBoardItemIndexAtCoordinates(j,i));
                        BreadBoard.DoubleWire dw = (BreadBoard.DoubleWire)breadBoard.locateBreadBoardItemOnBoard(j,i);
                        if(dw.getDir2() == dR) {
                            g2d.drawString(">", j * tw + xOffset, i * th + yOffset + th/2 + fontSize);
                        }else if(dw.getDir2() == dL) {
                            g2d.drawString("<", j * tw + xOffset, i * th + yOffset + th/2 + fontSize);
                        }else if(dw.getDir2() == dD) {
                            g2d.drawString("v", j * tw + xOffset, i * th + yOffset + th/2 + fontSize);
                        }else if(dw.getDir2() == dU) {
                            g2d.drawString("^", j * tw + xOffset, i * th + yOffset + th/2 + fontSize);
                        }
                    }
                }
            }
            fontSize = (int) (originalTileSize / 1.5);
            g2d.setFont(new Font("Arial", Font.BOLD, fontSize));

            //---draw a string to show if you're in default or editing mode---
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
                    for (int i = 0; i < tempCutCopyPasteBoardList.size(); i++){
                        for (int j = 0; j < tempCutCopyPasteBoardList.get(i).size(); j++){
                            //g2d.setColor(getColour(tempCutCopyPasteBoardList.get(i).get(j)));

                            g2d.fillRect((int)(j*tileWidth + Main.SCREEN_X_OFFSET + mX0),
                                    (int)(i*tileHeight + Main.SCREEN_Y_OFFSET + mY0), tileWidth, tileHeight);
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
            for (int i = 0; i < yPixels; i++)
            {
                for (int j = 0; j < xPixels; j++)
                {
                    g2d.setColor(getColour(Main.getTiles()[i][j]));
                    g2d.fillRect(j * tw, i * th, tw, th);
                }
            }
        }
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
