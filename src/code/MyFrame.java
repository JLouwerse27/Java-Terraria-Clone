package src.code;

import static src.code.Main.DEFAULT_SCREEN_X_OFFSET;
import static src.code.Main.DEFAULT_SCREEN_Y_OFFSET;
import static src.code.Main.SCREEN_X_OFFSET;
import static src.code.Main.SCREEN_Y_OFFSET;
import static src.code.Main.mouseX;
import static src.code.Main.mouseY;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.JFrame;

/**
 * MyFrame is a custom JFrame that includes a MyGameScreen component.
 * It provides methods to set and get the game screen.
 */
public class MyFrame extends JFrame {
    
    private MyGameScreen mgs;
    
    private Graphics2D g2d;

    /**
     * Default constructor for MyFrame.
     * Initializes the frame with default settings.
     */
    public MyFrame() {
        super();
        g2d = (Graphics2D) this.getGraphics();
    }

    /**
     * Constructs a MyFrame with the specified width and height.
     * 
     * @param width  The width of the frame.
     * @param height The height of the frame.
     */
    public MyFrame(int width, int height) {
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("MyFrame");
        
        setVisible(true);   
        g2d = (Graphics2D) this.getGraphics();
    }
    
    
    /**
     * Paints the mouse coordinates at the bottom right of the screen
     * Uses MyGameScreen.getGraphics, not Frame.getGraphics!
     */
    public void paintFPS(int fps) {
        int fontSize = (int) (mgs.originalTileSize / 1.5);
        g2d.setFont(new Font("Arial", Font.BOLD, fontSize));
        g2d.setColor(Color.WHITE);
        int x = mouseX[0] - SCREEN_X_OFFSET + DEFAULT_SCREEN_X_OFFSET;
        int y = mouseY[0] - SCREEN_Y_OFFSET + DEFAULT_SCREEN_Y_OFFSET;
        g2d.drawString("FPS: " + fps, WIDTH - 270, HEIGHT - 30);
    }
    
    /**
     * Sets the game screen for this frame.
     * 
     * @param mgs The MyGameScreen component to be added to the frame.
     */
    public void setGameScreen(MyGameScreen mgs) {
        this.getContentPane().add(mgs);
        this.mgs = mgs;
    }

    /**
     * Gets the game screen of this frame.
     * 
     * @return The MyGameScreen component of the frame.
     * @throws NullPointerException if no game screen has been added to the frame.
     */
    public MyGameScreen getGameScreen() throws NullPointerException {
        if (mgs == null) {
            throw new NullPointerException("No Game Screen added to Frame!");
        }
        return mgs;
    }
}
