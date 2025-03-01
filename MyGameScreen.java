import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

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
    static int tileSize;
    static int tileWidth;
    static int tileHeight;

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
        tileWidth = WIDTH / numX;
        tileHeight = HEIGHT / numY;
        setSize(WIDTH, HEIGHT);
    }
    
    // Constructor initializing the game screen with specified dimensions and tile configuration
    public MyGameScreen(final int width, final int height, final int numX, final int numY) {
        WIDTH = width;
        HEIGHT = height;
        xPixels = numX;
        yPixels = numY;
        tileWidth = WIDTH / numX;
        tileHeight = HEIGHT / numY;
        setSize(width, height);
    }

    // Method to render the game screen by drawing the tiles based on their colors
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        updateSize();
        int tw = (int) tileWidth;
        int th = (int) tileHeight;
        for (int i = 0; i < yPixels; i++) {
            for (int j = 0; j < xPixels; j++) {
                g2d.setColor(getColour(Main.getTiles()[i][j]));
                g2d.fillRect(j * tw, i * th, tw, th);
            }
        }
    }

    // Method to return the color corresponding to the given tile symbol
    public Color getColour(String c) {
        if (c.equals(TileString.Wall.getSymbol())) {
            return Color.BLACK;
        } else if (c.equals(TileString.Player.getSymbol())) {
            return Color.RED;
        } else {
            return Color.GRAY;
        }
    }

    // Method to update the size of the game screen and recalculate the tile dimensions
    public void updateSize() {
        WIDTH = super.getWidth();
        HEIGHT = super.getHeight();
        tileWidth = WIDTH / xPixels;
        tileHeight = HEIGHT / yPixels;

        WIDTH = tileWidth * xPixels;
        HEIGHT = tileHeight * yPixels;
        super.setSize(WIDTH, HEIGHT);
    }


    // Method to set the number of horizontal and vertical tiles (currently commented out)
    public void setPixels(int numX, int numY) {
        // xPixels = numX;
        // yPixels = numY;
        // tileSize = 10;
    }
}
