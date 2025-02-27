import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class MyGameScreen extends JComponent{
    
    static int WIDTH;
    static int HEIGHT;
    static int xPixels;
    static int yPixels;
    static int TILE_SIZE;

    public MyGameScreen(){
        WIDTH = 500;
        HEIGHT = 500;
        xPixels = 50;
        yPixels = 50;
        TILE_SIZE = 10;
        setSize(WIDTH, HEIGHT);
        //setPixels(num, ABORT);
    }

    public MyGameScreen(final int numX, final int numY){
        WIDTH = 500;
        HEIGHT = 500;
        xPixels = numX;
        yPixels = numY;
        TILE_SIZE = WIDTH / numX;
        setSize(WIDTH, HEIGHT);
        //setPixels(num, ABORT);
    }
    
    public MyGameScreen(final int width, final int height,
                            final int numX, final int numY){
        WIDTH = width;
        HEIGHT = height;
        xPixels = numX;
        yPixels = numY;
        TILE_SIZE = width / numX;
        setSize(width, height);
        //setPixels(num, ABORT);
    }

    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        for(int i = 0; i < yPixels; i++){
            for(int j = 0; j < xPixels; j++){
                g2d.setColor(getColour(Main.getTiles()[i][j]));
                g2d.fillRect(j*TILE_SIZE, i*TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    public Color getColour(char c){
        if(c == '#'){
            return Color.BLACK;
        }else if(c == 'P'){
            return Color.RED;
        }else{
            return Color.GRAY;
        }
    }

    public void setPixels(int numX, int numY){
        //xPixels = numX;
        //yPixels = numY;
        //TILE_SIZE = 10;
    }
}
