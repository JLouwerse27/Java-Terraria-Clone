import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class MyGameScreen extends JComponent{
    
    static int WIDTH;
    static int HEIGHT;
    static int xPixels;
    static int yPixels;
    // static double tileSize;
    // static double tileWidth;
    // static double tileHeight;
    static int tileSize;
    static int tileWidth;
    static int tileHeight;

    public MyGameScreen(){
        WIDTH = 500;
        HEIGHT = 500;
        xPixels = 50;
        yPixels = 50;
        tileSize = 10;
        tileWidth = tileSize;
        tileHeight = tileSize;
        setSize(WIDTH, HEIGHT);
        //setPixels(num, ABORT);
    }

    public MyGameScreen(final int numX, final int numY){
        WIDTH = 500;
        HEIGHT = 500;
        xPixels = numX;
        yPixels = numY;
        // tileWidth = (double) WIDTH / (double) numX;
        // tileHeight = (double) HEIGHT / (double) numY;
        tileWidth = WIDTH / numX;
        tileHeight = HEIGHT / numY;
        setSize(WIDTH, HEIGHT);
    }
    
    public MyGameScreen(final int width, final int height,
                            final int numX, final int numY){
        WIDTH = width;
        HEIGHT = height;
        xPixels = numX;
        yPixels = numY;
        tileWidth = WIDTH / numX;
        tileHeight = HEIGHT / numY;
        setSize(width, height);
    }

    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        updateSize();
        int tw;
        int th;
        tw = (int) tileWidth;
        th = (int) tileHeight;
        for(int i = 0; i < yPixels; i++){
            for(int j = 0; j < xPixels; j++){
                g2d.setColor(getColour(Main.getTiles()[i][j]));
                g2d.fillRect(j*tw, i*th, tw, th);
            }
        }
    }

    public Color getColour(String c){
        if(c == TileString.Wall.getSymbol()){
            return Color.BLACK;
        }else if(c == TileString.Player.getSymbol()){
            return Color.RED;
        }else{
            return Color.GRAY;
        }
    }

    public void updateSize(){
        WIDTH = super.getWidth();
        HEIGHT = super.getHeight();
        // tileWidth = (double) WIDTH / (double) xPixels; 
        // tileHeight = (double) HEIGHT / (double) yPixels;
        tileWidth = WIDTH / xPixels; 
        tileHeight = HEIGHT / yPixels;

        WIDTH = tileWidth * xPixels;
        HEIGHT = tileHeight * yPixels;
        super.setSize(WIDTH, HEIGHT);

    }

    public void setPixels(int numX, int numY){
        //xPixels = numX;
        //yPixels = numY;
        //tileSize = 10;
    }
}
