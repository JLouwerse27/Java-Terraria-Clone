import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class Main {
    
    private static char[][] tiles;
    
    //magic numbers
    static final int n10 = 10;
    static final int n50 = 50;

    
    public static void main(String[] args) {

        Player p1 = new Player(1, 1);

        final int numTiles = n10;
        

        tiles = new char[numTiles][numTiles];
        List<GameObject> gameObjects = new ArrayList<GameObject>();
        initTiles(numTiles);

        JFrame jf;
        jf = new JFrame();
        jf.setSize(500 + 16, 500 + 38);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setTitle("My Frame");
               
        jf.getContentPane().add(new MyGameScreen(numTiles, numTiles));
        jf.setVisible(true);

        jf.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                // TODO Auto-generated method stub
                if(e.getKeyCode() == java.awt.event.KeyEvent.VK_UP){
                    p1.move(Direction.UP);
                    System.out.println("UP" + p1.getX() + p1.getY());
                    
                }
                else if(e.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN){
                    p1.move(Direction.DOWN);
                    System.out.println("DOWN" + p1.getX() + p1.getY());
                    
                }
                else if(e.getKeyCode() == java.awt.event.KeyEvent.VK_LEFT){
                    p1.move(Direction.LEFT);
                    System.out.println("LEFT" + p1.getX() + p1.getY());
                }
                else if(e.getKeyCode() == java.awt.event.KeyEvent.VK_RIGHT){
                    p1.move(Direction.RIGHT);
                    System.out.println("RIGHT" + p1.getX() + p1.getY());
                }
                jf.getContentPane().repaint();
                setTiles(numTiles, p1, null);
                System.out.println("Key Pressed");
            }
            
        });
        
        //MyFrame mf = new MyFrame(500,300);

    }

    public static void initTiles(int num){
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                if(i == 0 || i == num-1 || j == 0 || j == num-1){
                    System.out.print("[]");
                    tiles[i][j] = '#';
                }else if(i == 1 && j == 1){
                    System.out.print("P ");
                    tiles[i][j] = 'P';
                }else {
                    System.out.print("  ");
                    tiles[i][j] = ' ';
                }
            }
            System.out.println("");
        }
    }

    public static void setTiles(int num, Player p1, Player p2){
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                if(i == 0 || i == num-1 || j == 0 || j == num-1){
                    System.out.print("[]");
                    tiles[i][j] = '#';
                }else if(j == p1.getX() && i == p1.getY()){
                    System.out.print("P1");
                    tiles[i][j] = 'P';
                }else {
                    System.out.print("  ");
                    tiles[i][j] = ' ';
                }
            }
            System.out.println("");
        }
    }


    public static char[][] getTiles(){
        return tiles;
    }

}
