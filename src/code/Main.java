import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

// import punchcard.Assembler;

public class Main {
    
    // private static char[][] tiles;
    private static String[][] tiles;

    //frame
    private static MyFrame mf;
    
    //magic numbers
    static final int n3 = 3;
    static final int n4 = 4;
    static final int n10 = 10;
    static final int n50 = 50;
    static final int PUNCHCARD_DISPLAY_WIDTH = 32;

    static final int TERRARIA_GAMEMODE_NUMBER = 1;
    static final int PUNCHCARD_GAMEMODE_NUMBER = 2;
    static final int GATES_GAMEMODE_NUMBER = 3;

    static final int DEFAULT_SCREEN_SIZE = 10;
    static final int DEFAULT_SCREEN_Y_OFFSET = -32;
    static final int DEFAULT_SCREEN_X_OFFSET = -8;
    static int SCREEN_Y_OFFSET = DEFAULT_SCREEN_Y_OFFSET;
    static int SCREEN_X_OFFSET = DEFAULT_SCREEN_X_OFFSET;

    static int gameMode;
    // static final String sP = "P ";
    // static final String sWall = "[]";
    // static final String sEmpty = "  ";
    static final String sP = TileString.Player.getSymbol();
    static final String sWall = TileString.Wall.getSymbol();
    static final String sEmpty = TileString.Empty.getSymbol();
    static final String sDown = "DOWN";
    static final String sUp = "UP";
    static final String sLeft = "LEFT";
    static final String sRight = "RIGHT";
    static final String sW = "W";
    static final String sS = "S";
    static final String sA = "A";
    static final String sD = "D";


    static int numTiles;
    
    /**
     * The main entry point for the Java Terraria Clone application.
     * Initializes players, game objects, and the game frame.
     * Sets up key listeners for player movement and handles window resizing events.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {

        if(args[0].equals(Integer.toString(TERRARIA_GAMEMODE_NUMBER))) {
            numTiles = DEFAULT_SCREEN_SIZE;
            gameMode = TERRARIA_GAMEMODE_NUMBER;
            initTerrariaClone();
            System.out.println("Initialized Terraria Clone");
        } else if(args[0].equals(Integer.toString(PUNCHCARD_GAMEMODE_NUMBER))) {
            numTiles = PUNCHCARD_DISPLAY_WIDTH;
            gameMode = PUNCHCARD_GAMEMODE_NUMBER;
            initPunchCard();
            System.out.println("Initialized Punch Card");
        } else if(args[0].equals(Integer.toString(GATES_GAMEMODE_NUMBER))){
            numTiles = 30;
            gameMode = GATES_GAMEMODE_NUMBER;
            initLogicGates();
            System.out.println("Initialized LogicGates");
        }

    }

    private static void initLogicGates() {
        tiles = new String[numTiles][numTiles];
        initTiles(numTiles);

        mf = new MyFrame();
        mf.setSize(500 + 16, 500 + 38);
        mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mf.setTitle("muh gates");

        BreadBoard b = new BreadBoard(numTiles);

        mf.setGameScreen(new MyGameScreen(numTiles, numTiles, b));
        mf.setVisible(true);


        final int[] mouseX = new int[1];
        final int[] mouseY = new int[1];

        mf.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                //checks if clicked and draws the screen accordingly
                mouseX[0] = e.getX();
                mouseY[0] = e.getY();

                if(b.checkClick(e.getX() + SCREEN_X_OFFSET,e.getY() + SCREEN_Y_OFFSET)) {
                    mf.getContentPane().repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                mouseX[0] = e.getX();
                mouseY[0] = e.getY();
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        mf.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                //mf.getContentPane().repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX[0] = e.getX();
                mouseY[0] = e.getY();
            }
        });

        mf.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if(b.getGamemode().equals(BreadBoard.DEFAULT_KEYWORD)) {
                        b.setGamemode(BreadBoard.EDITING_KEYWORD);
                    }else if(b.getGamemode().equals(BreadBoard.EDITING_KEYWORD)) {
                        b.setGamemode(BreadBoard.DEFAULT_KEYWORD);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_R) {
                    b.rotateItem((mouseX[0]+SCREEN_X_OFFSET)/MyGameScreen.tileWidth,
                            (mouseY[0]+SCREEN_Y_OFFSET)/MyGameScreen.tileHeight);
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

    }

    public static void initPunchCard(){
                
        tiles = new String[numTiles][numTiles];
        initTiles(numTiles);

        mf = new MyFrame();
        mf.setSize(500 + 16, 500 + 38);
        mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mf.setTitle("muh punchcard");
        mf.setGameScreen(new MyGameScreen(numTiles, numTiles));
        mf.setVisible(true);
        Assembler a = new Assembler(numTiles);
        
    }

    public static void initTerrariaClone() {
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

/**
     * Initializes the tiles withnd walls.
     * @param num The number of tiles.
     */
    public static void initTiles(int num) {
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                    tiles[i][j] = TileString.Wall.getSymbol();
            }
        }
    }


    /**
     * Initializes the tiles with players and walls.
     * @param num The number of tiles.
     * @param ps The list of players.
     */
    public static <T> void initTiles(int num, List<T> ps) {
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                for (T player : ps) {
                    if (j == ((Player) player).getX() && i == ((Player) player).getY()) {
                        tiles[i][j] = TileString.Player.getSymbol();
                    }
                }
                if (tiles[i][j] != TileString.Player.getSymbol()) {
                    if (i == 0 || i == num - 1 || j == 0 || j == num - 1) {
                        tiles[i][j] = TileString.Wall.getSymbol();
                    } else {
                        tiles[i][j] = TileString.Empty.getSymbol();
                    }
                }
            }
        }
    }

    /**
     * Sets the tiles with players and walls.
     * @param num The number of tiles.
     * @param ps The list of players.
     */
    public static <T> void setTiles(int num, List<T> ps) {
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                if (i == 0 || i == num - 1 || j == 0 || j == num - 1) {
                    tiles[i][j] = TileString.Wall.getSymbol();
                } else {
                    tiles[i][j] = TileString.Empty.getSymbol();
                }
                for (T player : ps) {
                    if (j == ((Player) player).getX() && i == ((Player) player).getY()) {
                        tiles[i][j] = TileString.Player.getSymbol();
                    }
                }
            }
        }
        shouldPrintTiles(num, DEFAULT_SCREEN_SIZE);
    }

    /**
     * Prints the tiles if the number of tiles is less than or equal to the specified number.
     * @param num The number of tiles.
     * @param n The specified number.
     */
    private static void shouldPrintTiles(final int num, final int n) {
        if (num <= n) {
            printTiles(num);
        }
    }

    /**
     * Prints the tiles to the console.
     * @param num The number of tiles.
     */
    private static void printTiles(final int num) {
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                System.out.print(tiles[i][j]);
            }
            System.out.println("");
        }
    }

    /**
     * Returns the tiles.
     * @return The tiles.
     */
    public static String[][] getTiles() {
        return tiles;
    }

    public static void setTiles(int numTiles, final String[][] nTiles) {
// ================= TO DO: add checking ==============
        tiles = nTiles;

    }

    /**
     * Returns the frame.
     * @return The frame.
     */
    public static MyFrame getMyFrame() {
        return mf;
    }

}
