import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class Main {
    
    // private static char[][] tiles;
    private static String[][] tiles;
    
    //magic numbers
    static final int n10 = 10;
    static final int n50 = 50;
    static final String sP = "P ";
    static final String sWall = "[]";
    static final String sEmpty = "  ";
    static final String sDown = "DOWN";
    static final String sUp = "UP";
    static final String sLeft = "LEFT";
    static final String sRight = "RIGHT";
    static final String sW = "W";
    static final String sS = "S";
    static final String sA = "A";
    static final String sD = "D";


    static final int numTiles = n10;
    
    public static void main(String[] args) {

        Player p1 = new Player(1, 1, 1);
        Player p2 = new Player(3, 3, 2);
        Player p3 = new Player(7, 3, 3);

        // tiles = new char[numTiles][numTiles];
        tiles = new String[numTiles][numTiles];
        List<GameObject> gameObjects = new ArrayList<GameObject>();

        List<Player> players = new ArrayList<Player>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
//        initTiles(numTiles);
        initTiles(numTiles, players);

        MyFrame mf;
        mf = new MyFrame();
        mf.setSize(500 + 16, 500 + 38);
        mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mf.setTitle("muh frame");
               
        mf.setGameScreen(new MyGameScreen(numTiles, numTiles));
        mf.setVisible(true);


        mf.addKeyListener(new KeyListener() {
            //function to reduce code repetition
            boolean code(java.awt.event.KeyEvent e, String d) {
                switch (d) {
                    case sUp:
                        if(e.getKeyCode() == java.awt.event.KeyEvent.VK_UP)
                            return true;
                        break;
                    case sDown:
                        if(e.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN)
                            return true;
                        break;
                    case sLeft:
                        if(e.getKeyCode() == java.awt.event.KeyEvent.VK_LEFT)
                            return true;
                        break;
                    case sRight:
                        if(e.getKeyCode() == java.awt.event.KeyEvent.VK_RIGHT)
                            return true;
                        break;
                    case sW:   
                        if(e.getKeyCode() == java.awt.event.KeyEvent.VK_W)
                            return true;
                        break;
                    case sS:
                        if(e.getKeyCode() == java.awt.event.KeyEvent.VK_S)
                            return true;
                        break;
                    case sA:
                        if(e.getKeyCode() == java.awt.event.KeyEvent.VK_A)
                            return true;
                        break;
                    case sD:
                        if(e.getKeyCode() == java.awt.event.KeyEvent.VK_D)
                            return true;
                        break;
                    default:
                        break;
                }
                return false;
            }

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
                if(code(e, sUp) || code(e, sW)) {
                    p1.move(Direction.UP);
                    System.out.println(sUp + p1.getX() + p1.getY());
                }
                else if(code(e, sDown) || code(e, sS)) {
                    p1.move(Direction.DOWN);
                    System.out.println(sDown + p1.getX() + p1.getY());
                }
                else if(code(e, sLeft) || code(e, sA)) {
                    p1.move(Direction.LEFT);
                    System.out.println(sLeft + p1.getX() + p1.getY());
                }
                else if(code(e, sRight) || code(e, sD)) {
                    p1.move(Direction.RIGHT);
                    System.out.println(sRight + p1.getX() + p1.getY());
                }
                mf.getContentPane().repaint();
                setTiles(numTiles, players);
                System.out.println("Key Pressed");
            }
            
        });
        

        mf.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                System.out.println("Window resized to: " + mf.getSize());
                mf.getGameScreen().updateSize();
            }
        });
        
        //MyFrame mf = new MyFrame(500,300);

    }

    public static <T> void initTiles(int num, List<T> ps){
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                for (T player : ps) {
                    
                    if(j == ((Player) player).getX() && i == ((Player) player).getY()){
                        System.out.print(sP);// + ((Player) player).getId());
                        // tiles[i][j] = 'P';
                        tiles[i][j] = TileString.Player.getSymbol();
                    }
                    
                }
                if(tiles[i][j] != sP){
                    if(i == 0 || i == num-1 || j == 0 || j == num-1){
                        // tiles[i][j] = '#';
                        tiles[i][j] = sWall;
                    }else {
                        System.out.print("  ");
                        // tiles[i][j] = ' ';
                        tiles[i][j] = sEmpty;
                    }
                }
            }
        }
    }

    public static <T> void setTiles(int num, List<T> ps){
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                if(i == 0 || i == num-1 || j == 0 || j == num-1){
                    tiles[i][j] = TileString.Wall.getSymbol();
                }else {
                    tiles[i][j] = TileString.Empty.getSymbol();
                }
                for (T player : ps) {
                    if(j == ((Player) player).getX() && i == ((Player) player).getY()){
                        tiles[i][j] = TileString.Player.getSymbol();
                    }  
                }
                
            }
        }
        shouldPrintTiles(num, n10);
    }
        
    // public static void init2Players(int x1, int y1, int x2, int y2){
    //     Player p1 = new Player(x1, y1);
    //     Player p2 = new Player(x2, y2);
    // }

    private static void shouldPrintTiles(final int num, final int n) {
        if (num <= n) {
            printTiles(num);
        }
    }

    private static void printTiles(final int num) {
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                System.out.print(tiles[i][j]);
            }
            System.out.println("");
        }
    }

    public static String[][] getTiles(){
        return tiles;
    }

}
