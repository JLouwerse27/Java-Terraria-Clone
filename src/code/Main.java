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
    private static BreadBoard b;


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
    static final int DEFAULT_LOGIC_SCREEN_SIZE = 100;

    static final double SCREEN_ZOOM_COEFFICENT = 1.25;//zoom in coefficient

    static double SCREEN_Y_OFFSET = 0;
    static double SCREEN_X_OFFSET = 0;

    static double P_SCREEN_Y_OFFSET = SCREEN_Y_OFFSET;
    static double P_SCREEN_X_OFFSET = SCREEN_X_OFFSET;

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

    private static boolean copyingAndPasting = false;

    static int numTiles;
    
    /**
     * The main entry point for the Java Terraria Clone application.
     * Initializes players, game objects, and the game frame.
     * Sets up key listeners for player movement and handles window resizing events.
     *
     * @param args Command line arguments
     */
    public static void main(final String[] args) {

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

            gameMode = GATES_GAMEMODE_NUMBER;
            initLogicGates();
            System.out.println("Initialized LogicGates");
        }

    }

    private static void initLogicGates() {
        numTiles = DEFAULT_LOGIC_SCREEN_SIZE;
        tiles = new String[numTiles][numTiles];
        initTiles(numTiles);

        mf = new MyFrame();
        mf.setSize(500 + 16, 500 + 38);
        mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mf.setTitle("muh gates");

        b = new BreadBoard(numTiles);
        MyGameScreen mgs = new MyGameScreen(numTiles, numTiles, b);
        mf.setGameScreen(mgs);
        mf.setVisible(true);


        final int[] mouseX = new int[2];
        final int[] mouseY = new int[2];

        final boolean[] mouseScrollUp = {false};
        final boolean[] mouseScrollDown = {false};

        final boolean[] dragging = {false};
        boolean[] keys = new boolean[500];

        MouseHandler mh = new MouseHandler(mf, b);
        MouseHandler.MouseListenerHandler mlh = mh.new MouseListenerHandler();

        //mf.addMouseListener(mlh);

        mf.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //checks if clicked and draws the screen accordingly
//                mouseX[0] = e.getX();
//                mouseY[0] = e.getY();

                if(b.checkClick(e,e.getX() - (int)SCREEN_X_OFFSET + DEFAULT_SCREEN_X_OFFSET,
                        e.getY() - (int)SCREEN_Y_OFFSET + DEFAULT_SCREEN_Y_OFFSET)) {
                    mf.getContentPane().repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mouseX[0] = e.getX();
                mouseY[0] = e.getY();
                P_SCREEN_X_OFFSET = Main.SCREEN_X_OFFSET;
                P_SCREEN_Y_OFFSET = SCREEN_Y_OFFSET;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
//                if(dragging[0]) {
//                    SCREEN_X_OFFSET = -(mouseX[0] - e.getX()); //+ DEFAULT_SCREEN_X_OFFSET;
//                    SCREEN_Y_OFFSET = -(mouseY[0] - e.getY()); //+ DEFAULT_SCREEN_Y_OFFSET;
//                    mf.getGameScreen().xOffset = SCREEN_X_OFFSET; //+ DEFAULT_SCREEN_X_OFFSET;
//                    mf.getGameScreen().yOffset = SCREEN_Y_OFFSET; //+ DEFAULT_SCREEN_Y_OFFSET;
//                    mf.getContentPane().repaint();
//                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
//                mouseX[0] = e.getX();
//                mouseY[0] = e.getY();
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });


        mf.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(e.getWheelRotation() > 0) {
                    mouseScrollDown[0] = true;
                    mouseScrollUp[0] = false;
                }else if(e.getWheelRotation() < 0) {
                    mouseScrollUp[0] = true;
                    mouseScrollDown[0] = false;
                }else if(e.getWheelRotation() == 0){
                    mouseScrollUp[0] = false;
                    mouseScrollDown[0] = false;
                }
            }




        });

        mf.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if(!keys[KeyEvent.VK_CONTROL]) {//dragging without control key

                    copyingAndPasting = false;

                    SCREEN_X_OFFSET = -(mouseX[0] - e.getX()); //
                    SCREEN_Y_OFFSET = -(mouseY[0] - e.getY()); //+ DEFAULT_SCREEN_Y_OFFSET;
                    SCREEN_X_OFFSET += P_SCREEN_X_OFFSET;
                    SCREEN_Y_OFFSET += P_SCREEN_Y_OFFSET;

                    mf.getGameScreen().xOffset = (int) SCREEN_X_OFFSET;
                    mf.getGameScreen().yOffset = (int) SCREEN_Y_OFFSET;
                    mf.getContentPane().repaint();

                }else {//dragging with control key
                    if(b.getGamemode().equals(BreadBoard.EDITING_KEYWORD)){//while editing
                        copyingAndPasting = true;
                    }else if(b.getGamemode().equals(BreadBoard.DEFAULT_KEYWORD)){
                        copyingAndPasting = false;
                    }
                }
//                dragging[0] = true;

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
                keys[e.getKeyCode()] = true;
                if(keys[KeyEvent.VK_ENTER] || keys[KeyEvent.VK_E]) {
                    if(b.getGamemode().equals(BreadBoard.DEFAULT_KEYWORD)) {
                        b.setGamemode(BreadBoard.EDITING_KEYWORD);
                    }else if(b.getGamemode().equals(BreadBoard.EDITING_KEYWORD)) {
                        b.setGamemode(BreadBoard.DEFAULT_KEYWORD);
                    }
                    mf.getContentPane().repaint();
                } else if (keys[KeyEvent.VK_R]) {//rotate with "R"
                    b.rotateItem(0,(mouseX[0]-(int)SCREEN_X_OFFSET+DEFAULT_SCREEN_X_OFFSET)/MyGameScreen.tileWidth,
                            (mouseY[0]-(int)SCREEN_Y_OFFSET+DEFAULT_SCREEN_Y_OFFSET)/MyGameScreen.tileHeight);
//                    if (keys[KeyEvent.VK_SHIFT]) {//rotate second dir with "shift + R"
//
//                    }
                } else if (keys[KeyEvent.VK_Q]) {
                    b.rotateItem(1,(mouseX[0]-(int)SCREEN_X_OFFSET+DEFAULT_SCREEN_X_OFFSET)/MyGameScreen.tileWidth,
                                (mouseY[0]-(int)SCREEN_Y_OFFSET+DEFAULT_SCREEN_Y_OFFSET)/MyGameScreen.tileHeight);
                } else if(keys[KeyEvent.VK_S]){//print or "save"
                    b.printTiles(DEFAULT_LOGIC_SCREEN_SIZE);
                }else if(keys[KeyEvent.VK_CONTROL]){//zoom in out

                    if(mouseScrollDown[0]){
                        if(MyGameScreen.tileSize / SCREEN_ZOOM_COEFFICENT >=4) {
                            double dx = (mouseX[0] + DEFAULT_SCREEN_X_OFFSET - SCREEN_X_OFFSET);
                            double dy = (mouseY[0]+ DEFAULT_SCREEN_Y_OFFSET - SCREEN_Y_OFFSET);

                            int oldTileSize = MyGameScreen.tileSize;
                            MyGameScreen.tileSize = (int)(MyGameScreen.tileSize / SCREEN_ZOOM_COEFFICENT);

                            SCREEN_X_OFFSET = (mouseX[0] + DEFAULT_SCREEN_X_OFFSET
                                    - (dx * MyGameScreen.tileSize / oldTileSize));
                            SCREEN_Y_OFFSET = (mouseY[0] + DEFAULT_SCREEN_Y_OFFSET
                                    - (dy * MyGameScreen.tileSize / oldTileSize));

                            mgs.xOffset = (int)SCREEN_X_OFFSET;
                            mgs.yOffset = (int)SCREEN_Y_OFFSET;

                        }
                        mgs.setTileSize(MyGameScreen.tileSize, MyGameScreen.tileSize);
                        mgs.repaint();
                        mouseScrollDown[0] = false;
                    }else if (mouseScrollUp[0]){
                        if(MyGameScreen.tileSize<=70) {
                            double dx = (mouseX[0] + DEFAULT_SCREEN_X_OFFSET - SCREEN_X_OFFSET);
                            double dy = (mouseY[0]+ DEFAULT_SCREEN_Y_OFFSET - SCREEN_Y_OFFSET);

                            int oldTileSize = MyGameScreen.tileSize;
                            MyGameScreen.tileSize = (int)(MyGameScreen.tileSize * SCREEN_ZOOM_COEFFICENT);

                            SCREEN_X_OFFSET = (mouseX[0] + DEFAULT_SCREEN_X_OFFSET
                                    - (dx * MyGameScreen.tileSize / oldTileSize));
                            SCREEN_Y_OFFSET = (mouseY[0] + DEFAULT_SCREEN_Y_OFFSET
                                    - (dy * MyGameScreen.tileSize / oldTileSize));

                            mgs.xOffset = (int)SCREEN_X_OFFSET;
                            mgs.yOffset = (int)SCREEN_Y_OFFSET;
                        }
                        mgs.setTileSize(MyGameScreen.tileSize, MyGameScreen.tileSize);
                        mgs.repaint();
                        mouseScrollUp[0] = false;
                    }

                }else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    b.itemCursor = -1;
                }else if (e.getKeyCode() == KeyEvent.VK_0) {
                    b.itemCursor = 0;
                }else if (e.getKeyCode() == KeyEvent.VK_1) {
                    b.itemCursor = 1;
                }else if (e.getKeyCode() == KeyEvent.VK_2) {
                    b.itemCursor = 2;
                }else if (e.getKeyCode() == KeyEvent.VK_3) {
                    b.itemCursor = 3;
                }else if (e.getKeyCode() == KeyEvent.VK_4) {
                    b.itemCursor = 4;
                }else if (e.getKeyCode() == KeyEvent.VK_5) {
                    b.itemCursor = 5;
                }else if (e.getKeyCode() == KeyEvent.VK_6) {
                    b.itemCursor = 6;
                }else if (e.getKeyCode() == KeyEvent.VK_7) {
                    b.itemCursor = 7;
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                keys[e.getKeyCode()] = false;
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
        //shouldPrintTiles(num, DEFAULT_SCREEN_SIZE);
    }

    /**
     * Prints the tiles if the number of tiles is less than or equal to the specified number.
     * @param num The number of tiles.
     * @param n The specified number.
     */
    private static void shouldPrintTiles(final int num, final int n) {
        if (num <= n) {
            //printTiles(num);
        }
    }

    /**
     * Prints the tiles, dirs and 2nd dirs to the console.
     * @param num The number of tiles.
     */


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

    /**
     * Standard getter for the temporary copyingAndPasting variable
     * @return copyingAndPasting
     */
    public static boolean getCopyingAndPasting(){
        return copyingAndPasting;
    }

}
