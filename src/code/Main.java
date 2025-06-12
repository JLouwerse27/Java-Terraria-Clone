package src.code;

import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

// import punchcard.Assembler;


public class Main {
    
    // private static char[][] tiles;
    private static String[][] tiles;

    //frame
    private static MyFrame mf;
    private static MyGameScreen mgs;
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
    //static final String GATES_GAMEMODE_STRING = "";

    static final int DEFAULT_SCREEN_SIZE = 10;//ARCHAIC: TERRARIA AND SUCH
    static final int DEFAULT_SCREEN_Y_OFFSET = -32;
    static final int DEFAULT_SCREEN_X_OFFSET = -8;
    static final int DEFAULT_LOGIC_SCREEN_SIZE = 3;
    private static int LOGIC_SCREEN_WIDTH;
    private static int LOGIC_SCREEN_HEIGHT;
    private static int LOGIC_SCREEN_ZHEIGHT;

    static int LOGIC_SCREEN_LAYER;
    private static int DEFAULT_LOGIC_SCREEN_LAYER = 1;


    static final double SCREEN_ZOOM_COEFFICENT = 1.25;//zoom in coefficient

    static double SCREEN_Y_OFFSET = 0;
    static double SCREEN_X_OFFSET = 0;

    static double P_SCREEN_Y_OFFSET = SCREEN_Y_OFFSET;
    static double P_SCREEN_X_OFFSET = SCREEN_X_OFFSET;
    
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

    static final int[] mouseX = new int[2];
    static final int[] mouseY = new int[2];
    static final int[] mouseClickNumber = new int[2];

    static final boolean[] mouseScrollUp = {false};
    static final boolean[] mouseScrollDown = {false};

    static final boolean[] dragging = {false};
    static final boolean[] keys = new boolean[500];

    private static boolean cutting = false;
    private static boolean copying = false;
    private static boolean pasting = false;

    static int gameMode;
    static int numTiles;

    static final int DEFAULT_TICK_SPEED = 2000;
    static int tickNumber = 0;
    static boolean tick_true = false;
    /**
     * Speed at which the game runs at; in ms.
     */
    private static int TICK_SPEED = DEFAULT_TICK_SPEED;//in ms

    /**
     * The main entry point for the Java Terraria Clone application.
     * Initializes players, game objects, and the game frame.
     * Sets up key listeners for player movement and handles window resizing events.
     *
     * @param args Command line arguments
     */
    public static void main(final String[] args) {
        if(args != null && args.length > 0) {
            if (args[0].equals(Integer.toString(TERRARIA_GAMEMODE_NUMBER))) {
                numTiles = DEFAULT_SCREEN_SIZE;
                gameMode = TERRARIA_GAMEMODE_NUMBER;
                initTerrariaClone();
                System.out.println("Initialized Terraria Clone");
            } else if (args[0].equals(Integer.toString(PUNCHCARD_GAMEMODE_NUMBER))) {
                numTiles = PUNCHCARD_DISPLAY_WIDTH;
                gameMode = PUNCHCARD_GAMEMODE_NUMBER;
                initPunchCard();
                System.out.println("Initialized Punch Card");
            }
        }else {

            gameMode = GATES_GAMEMODE_NUMBER;
            initLogicGates();
            System.out.println("Initialized LogicGates");
        }

    }

    private static void initLogicGates() {
        int width;
        int height;
        int zHeight;

        //Path file = Paths.get("test_breadboard.txt");
        //Path file = Paths.get("save3.txt");
        //a file which attempts repeatedly add one in a loop (0, 1, 2, 3 ... 14, 15, 0)
        Path file = Paths.get("add1movement.txt");
        //Path file = Paths.get("s.txt");
        //Path file = Paths.get("small.txt"); //first 3d. 3by3by3
        try {
            width = BreadBoardFileLoader.dimensions(file)[0];
            height = BreadBoardFileLoader.dimensions(file)[1];
            zHeight = BreadBoardFileLoader.dimensions(file)[2];
            System.out.println("width " + width + " height " + height + " zHeight " + zHeight);
            LOGIC_SCREEN_WIDTH = width;
            LOGIC_SCREEN_HEIGHT = height;
            LOGIC_SCREEN_ZHEIGHT = zHeight;
            LOGIC_SCREEN_LAYER = DEFAULT_LOGIC_SCREEN_LAYER;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        tiles = new String[width][height];
//        initTiles(numTiles); ARCHAIC!!!!

        mf = new MyFrame();
        mf.setSize(500 + 16, 500 + 38);
        mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mf.setTitle("muh gates");

        b = new BreadBoard(width,height, zHeight);


        try {
            BreadBoardFileLoader.load(b,file);
            System.out.println("FILE LOADED " + file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mgs = new MyGameScreen(width, height, zHeight, b);
        mf.setGameScreen(mgs);
        mf.setVisible(true);

        Timer timer = new Timer(0, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //tick_true = true;
                tickNumber++;
                //System.out.println("tick " + tickNumber);
                b.tick(tickNumber); // run every 500ms
                mgs.repaint();
                //tick_true = false;
            }
        });
        timer.start();

        //int initialSpeed = TICK_SPEED;
        //if(TICK_SPEED != initialSpeed){

        //}

//        MouseHandler mh = new MouseHandler(mf, b);
//        MouseHandler.MouseListenerHandler mlh = mh.new MouseListenerHandler();

        //mf.addMouseListener(mlh);

        mf.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //checks if clicked and draws the screen accordingly
//                mouseX[0] = e.getX();
//                mouseY[0] = e.getY();

                if(b.checkClick(
                        e,
                        e.getX() - (int)SCREEN_X_OFFSET + DEFAULT_SCREEN_X_OFFSET,
                        e.getY() - (int)SCREEN_Y_OFFSET + DEFAULT_SCREEN_Y_OFFSET,
                        LOGIC_SCREEN_LAYER)
                ) {
                    mf.getContentPane().repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mouseClickNumber[0] = e.getButton();
                mouseX[0] = e.getX();
                mouseY[0] = e.getY();
                P_SCREEN_X_OFFSET = Main.SCREEN_X_OFFSET;
                P_SCREEN_Y_OFFSET = SCREEN_Y_OFFSET;
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                if(copying && b.getGamemode().equals(BreadBoard.COPYING_KEYWORD)){
                    copying = false;
                    b.setGamemode(BreadBoard.EDITING_KEYWORD);
                    mgs.repaint();
                    return;
                }
                if(cutting && b.getGamemode().equals(BreadBoard.CUTTING_KEYWORD)){
                    int sX = (int)(Main.mouseX[0] - Main.SCREEN_X_OFFSET) / MyGameScreen.tileWidth;
                    if(sX < 0) sX = 0;

                    int sY = (int)(Main.mouseY[0] - Main.SCREEN_Y_OFFSET) / MyGameScreen.tileHeight;
                    if(sY < 0) sY = 0;

                    int eX = (int)(Main.mouseX[1] - Main.SCREEN_X_OFFSET) / MyGameScreen.tileWidth;
                    if(eX > MyGameScreen.xPixels) eX = MyGameScreen.xPixels;

                    int eY = (int)(Main.mouseY[1] - Main.SCREEN_Y_OFFSET) / MyGameScreen.tileHeight;
                    if(eY > MyGameScreen.yPixels) eY = MyGameScreen.yPixels;
                    b.eraseRegion(sX, sY, b.BOTTOM_Z, eX, eY,b.TOP_Z);
                    cutting = false;
                    b.setGamemode(BreadBoard.EDITING_KEYWORD);
                    mgs.repaint();
                    return;
                }


                //paste();

//                if(dragging[0]) {
//                    SCREEN_X_OFFSET = -(mouseX[0] - e.getX()); //+ DEFAULT_SCREEN_X_OFFSET;
//                    SCREEN_Y_OFFSET = -(mouseY[0] - e.getY()); //+ DEFAULT_SCREEN_Y_OFFSET;
//                    mf.getGameScreen().xOffset = SCREEN_X_OFFSET; //+ DEFAULT_SCREEN_X_OFFSET;
//                    mf.getGameScreen().yOffset = SCREEN_Y_OFFSET; //+ DEFAULT_SCREEN_Y_OFFSET;
                    mf.getContentPane().repaint();
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
                }else {
                    mouseScrollUp[0] = false;
                    mouseScrollDown[0] = false;
                }
            }




        });

        mf.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if(!(copying || cutting))
                {//dragging without CTL+C
                    if(b.getGamemode().equals(BreadBoard.DEFAULT_KEYWORD)) {
                        SCREEN_X_OFFSET = -(mouseX[0] - e.getX()); //
                        SCREEN_Y_OFFSET = -(mouseY[0] - e.getY()); //+ DEFAULT_SCREEN_Y_OFFSET;
                        SCREEN_X_OFFSET += P_SCREEN_X_OFFSET;
                        SCREEN_Y_OFFSET += P_SCREEN_Y_OFFSET;

                        mf.getGameScreen().xOffset = (int) SCREEN_X_OFFSET;
                        mf.getGameScreen().yOffset = (int) SCREEN_Y_OFFSET;
                        mf.getContentPane().repaint();
                    }else if(b.getGamemode().equals(BreadBoard.EDITING_KEYWORD))
                    {
                        //System.out.println(e.getButton()); is 0 in this case, for all clicks
                        if(mouseClickNumber[0] == MouseEvent.BUTTON1) {//left button tapped and then dragged
                            if (b.checkClick(
                                    e,
                                    e.getX() - (int) SCREEN_X_OFFSET + DEFAULT_SCREEN_X_OFFSET,
                                    e.getY() - (int) SCREEN_Y_OFFSET + DEFAULT_SCREEN_Y_OFFSET,
                                    LOGIC_SCREEN_LAYER)) {

                                mf.getContentPane().repaint();
                            }
                        }else if(mouseClickNumber[0] == MouseEvent.BUTTON3) {//right button tapped and then dragged
                            if (b.checkClick(
                                    e,
                                    e.getX() - (int) SCREEN_X_OFFSET + DEFAULT_SCREEN_X_OFFSET,
                                    e.getY() - (int) SCREEN_Y_OFFSET + DEFAULT_SCREEN_Y_OFFSET,
                                    LOGIC_SCREEN_LAYER)) {

                                mf.getContentPane().repaint();
                            }
                        }
                    }
                }else
                {//dragging with CTL+C
                    if(b.getGamemode().equals(BreadBoard.EDITING_KEYWORD))
                    {//while editing

                        //b.setGamemode(BreadBoard.COPYING_KEYWORD);
                        mouseX[1] = e.getX();
                        mouseY[1] = e.getY();

                        b.checkClick(e,mouseX[1], mouseY[1],LOGIC_SCREEN_LAYER);
                        mgs.repaint();
                    }else if(b.getGamemode().equals(BreadBoard.COPYING_KEYWORD)
                    || b.getGamemode().equals(BreadBoard.CUTTING_KEYWORD)) {
                        mouseX[1] = e.getX();
                        mouseY[1] = e.getY();

                        b.checkClick(e,mouseX[1], mouseY[1],LOGIC_SCREEN_LAYER);
                        mgs.repaint();
                    }else if(b.getGamemode().equals(BreadBoard.DEFAULT_KEYWORD))
                    {
                        copying = false;
                        cutting = false;
                        pasting = false;
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
                if(keys[KeyEvent.VK_E])
                {
                    if(b.getGamemode().equals(BreadBoard.DEFAULT_KEYWORD))
                    {
                        b.setGamemode(BreadBoard.EDITING_KEYWORD);
                    }else if(b.getGamemode().equals(BreadBoard.EDITING_KEYWORD))
                    {
                        b.setGamemode(BreadBoard.DEFAULT_KEYWORD);
                    }
                    mf.getContentPane().repaint();
                } else if (keys[KeyEvent.VK_R])
                {//rotate with "R"
                    b.rotateItem(
                            0,
                            (mouseX[0]-(int)SCREEN_X_OFFSET+DEFAULT_SCREEN_X_OFFSET)/MyGameScreen.tileWidth,
                            (mouseY[0]-(int)SCREEN_Y_OFFSET+DEFAULT_SCREEN_Y_OFFSET)/MyGameScreen.tileHeight,
                            LOGIC_SCREEN_LAYER);
//                    if (keys[KeyEvent.VK_SHIFT]) {//rotate second dir with "shift + R"
//
//                    }
                } else if (keys[KeyEvent.VK_Q])
                {
                    b.rotateItem(
                            1,
                            (mouseX[0]-(int)SCREEN_X_OFFSET+DEFAULT_SCREEN_X_OFFSET)/MyGameScreen.tileWidth,
                            (mouseY[0]-(int)SCREEN_Y_OFFSET+DEFAULT_SCREEN_Y_OFFSET)/MyGameScreen.tileHeight,
                            LOGIC_SCREEN_LAYER);
                } else if(keys[KeyEvent.VK_S])
                {//print or "save"
                    b.printTiles(LOGIC_SCREEN_WIDTH,LOGIC_SCREEN_HEIGHT,LOGIC_SCREEN_ZHEIGHT);
                }else if(keys[KeyEvent.VK_CONTROL])
                {
                    if(!(keys[KeyEvent.VK_C] || keys[KeyEvent.VK_V] || keys[KeyEvent.VK_X]))
                    {//zoom in and out if not CTL+C or CTL + V
                        if (mouseScrollDown[0]) {
                            if (MyGameScreen.tileSize / SCREEN_ZOOM_COEFFICENT >= 4) {
                                double dx = (mouseX[0] + DEFAULT_SCREEN_X_OFFSET - SCREEN_X_OFFSET);
                                double dy = (mouseY[0] + DEFAULT_SCREEN_Y_OFFSET - SCREEN_Y_OFFSET);

                                int oldTileSize = MyGameScreen.tileSize;
                                MyGameScreen.tileSize = (int) (MyGameScreen.tileSize / SCREEN_ZOOM_COEFFICENT);

                                SCREEN_X_OFFSET = (mouseX[0] + DEFAULT_SCREEN_X_OFFSET
                                        - (dx * MyGameScreen.tileSize / oldTileSize));
                                SCREEN_Y_OFFSET = (mouseY[0] + DEFAULT_SCREEN_Y_OFFSET
                                        - (dy * MyGameScreen.tileSize / oldTileSize));

                                mgs.xOffset = (int) SCREEN_X_OFFSET;
                                mgs.yOffset = (int) SCREEN_Y_OFFSET;

                            }
                            mgs.setTileSize(MyGameScreen.tileSize, MyGameScreen.tileSize);
                            mgs.repaint();
                            mouseScrollDown[0] = false;
                        } else if (mouseScrollUp[0]) {
                            if (MyGameScreen.tileSize <= 70) {
                                double dx = (mouseX[0] + DEFAULT_SCREEN_X_OFFSET - SCREEN_X_OFFSET);
                                double dy = (mouseY[0] + DEFAULT_SCREEN_Y_OFFSET - SCREEN_Y_OFFSET);

                                int oldTileSize = MyGameScreen.tileSize;
                                MyGameScreen.tileSize = (int) (MyGameScreen.tileSize * SCREEN_ZOOM_COEFFICENT);

                                SCREEN_X_OFFSET = (mouseX[0] + DEFAULT_SCREEN_X_OFFSET
                                        - (dx * MyGameScreen.tileSize / oldTileSize));
                                SCREEN_Y_OFFSET = (mouseY[0] + DEFAULT_SCREEN_Y_OFFSET
                                        - (dy * MyGameScreen.tileSize / oldTileSize));

                                mgs.xOffset = (int) SCREEN_X_OFFSET;
                                mgs.yOffset = (int) SCREEN_Y_OFFSET;
                            }
                            mgs.setTileSize(MyGameScreen.tileSize, MyGameScreen.tileSize);
                            mgs.repaint();
                            mouseScrollUp[0] = false;
                        }
                    } else if (keys[KeyEvent.VK_C])
                    {
                        copying = true;
                        cutting = false;
                        pasting = false;
                        b.setGamemode(BreadBoard.COPYING_KEYWORD);
                        mgs.repaint();
                    }else if (keys[KeyEvent.VK_V])
                    {
                        copying = false;
                        cutting = false;
                        pasting = true;
                        b.setGamemode(BreadBoard.PASTING_KEYWORD);
                        paste();
                        mgs.repaint();
                    }else if (keys[KeyEvent.VK_X])
                    {
                        copying = false;
                        cutting = true;
                        pasting = false;
                        b.setGamemode(BreadBoard.CUTTING_KEYWORD);
                        mgs.repaint();
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
                }else if (e.getKeyCode() == KeyEvent.VK_8) {
                    b.itemCursor = 8;
                }else if (e.getKeyCode() == KeyEvent.VK_9) {
                    b.itemCursor = 9;
                }else if (e.getKeyCode() == KeyEvent.VK_I) {
                    b.itemCursor = 10;
                }else if (e.getKeyCode() == KeyEvent.VK_O) {
                    b.itemCursor = 11;
                }else if (e.getKeyCode() == KeyEvent.VK_EQUALS){
                    if(LOGIC_SCREEN_LAYER+1 <= b.TOP_Z) {
                        LOGIC_SCREEN_LAYER++;
                    }
                }else if (e.getKeyCode() == KeyEvent.VK_MINUS){
                    if(LOGIC_SCREEN_LAYER-1 >= b.BOTTOM_Z) {
                        LOGIC_SCREEN_LAYER--;
                    }
                }

                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    double speedup = 1.2;
                    if(!keys[KeyEvent.VK_SHIFT]) {
                        if((int)(TICK_SPEED / speedup) > 0) {
                            TICK_SPEED = (int) (TICK_SPEED / speedup);//lower number = faster speed
                            System.out.println(TICK_SPEED);
                            timer.setDelay(TICK_SPEED);
                        }
                    }else {
                        if(TICK_SPEED <= 4) speedup = 2;
                        if(TICK_SPEED * speedup <= 3000) {
                            TICK_SPEED = (int)(TICK_SPEED * speedup);
                            System.out.println(TICK_SPEED);
                            timer.setDelay(TICK_SPEED);
                        }
                    }
                }

//                if (e.getKeyCode() == KeyEvent.VK_W) {//dont work rn
//                    mgs.yOffset -= MyGameScreen.tileSize;
//                }else if (e.getKeyCode() == KeyEvent.VK_S) {
//                    mgs.yOffset += MyGameScreen.tileSize;
//                }if (e.getKeyCode() == KeyEvent.VK_A) {
//                    mgs.xOffset -= MyGameScreen.tileSize;
//                }else if (e.getKeyCode() == KeyEvent.VK_D) {
//                    mgs.xOffset += MyGameScreen.tileSize;
//                }
                mgs.repaint();

            }

            @Override
            public void keyReleased(KeyEvent e) {
                keys[e.getKeyCode()] = false;
                if(e.getKeyCode() == KeyEvent.VK_CONTROL && (b.getGamemode().equals(BreadBoard.CUTTING_KEYWORD))){

                    //b.eraseRegion(sX, sY, eX, eY);

                    //b.setGamemode(BreadBoard.EDITING_KEYWORD);
                    mgs.repaint();
                }
            }
        });

    }

    /**
     * Pastes the stuff from the clipboard onto the breadboard
     */
    private static void paste(){
        int sX = (int)(mouseX[0] - SCREEN_X_OFFSET) / MyGameScreen.tileWidth;
        if(sX < 0) sX = 0;

        int sY = (int)(mouseY[0] - SCREEN_Y_OFFSET) / MyGameScreen.tileHeight;
        if(sY < 0) sY = 0;

        if(pasting && b.getGamemode().equals(BreadBoard.PASTING_KEYWORD)){
            for(int i = 0; i < mgs.tempCutCopyPasteBoardList.size(); i++) {
                for (int j = 0; j < mgs.tempCutCopyPasteBoardList.getFirst().size(); j++) {
                    for (int k = 0; k < mgs.tempCutCopyPasteBoardList.getFirst().getFirst().size(); k++) {
                        b.setBreadBoardTile(mgs.tempCutCopyPasteBoard[i][j + sY][k + sX],k + sX, j + sY, i);
                        b.setBreadBoardDirectionTile(mgs.tempCutCopyPasteBoardDirection1[i][j + sY][k + sX],k + sX, j + sY, i);
                        b.setBreadBoardDirection2Tile(mgs.tempCutCopyPasteBoardDirection2[i][j + sY][k + sX],k + sX, j + sY, i);
                        b.changeBreadBoard(
                                b.convertToItemEnumOrdinal(mgs.tempCutCopyPasteBoard[i][j + sY][k + sX]),
                                mgs.tempCutCopyPasteBoardDirection1[i][j + sY][k + sX],
                                mgs.tempCutCopyPasteBoardDirection2[i][j + sY][k + sX],
                                k + sX,
                                j + sY,
                                i);

                    }
                }
            }
            mgs.repaint();
            b.setGamemode(BreadBoard.EDITING_KEYWORD);
            pasting = false;
        }
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

    /**
     * Should probably get rid of this
     * @param numTiles
     * @param nTiles
     */
    public static void setTiles(int numTiles, final String[][] nTiles) {
// ================= TO DO: add checking ==============
        tiles = nTiles;

    }

    /**
     * Returns the frame.
     * @return mf.
     */
    public static MyFrame getMyFrame() {
        return mf;
    }

    /**
     * Returns the gamescreen.
     * @return mgs.
     */
    public static MyGameScreen getMyGameScreen() {
        return mgs;
    }

    /**
     * Standard getter for the temporary copying variable
     * @return copying
     */
    public static boolean getCopying(){
        return copying;
    }

    /**
     * Standard getter for the temporary copying variable
     * @return copying
     */
    public static boolean getCutting(){
        return cutting;
    }

}
