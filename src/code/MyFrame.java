package src.code;

import javax.swing.JFrame;

/**
 * MyFrame is a custom JFrame that includes a MyGameScreen component.
 * It provides methods to set and get the game screen.
 */
public class MyFrame extends JFrame {
    
    private MyGameScreen mgs;

    /**
     * Default constructor for MyFrame.
     * Initializes the frame with default settings.
     */
    public MyFrame() {
        super();
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
