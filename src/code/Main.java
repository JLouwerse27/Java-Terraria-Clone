package src.code;

import org.w3c.dom.ls.LSOutput;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

// import punchcard.Assembler;


public class Main implements Runnable{
    
    // private static char[][] tiles;
    private static String[][] tiles;

    //frame
    private static MyFrame mf;
    private static MyGameScreen mgs;
    private static BreadBoard b;
    private static Graphics repeatedGraphics;
    
    //new Thread stuff
    private Thread thread;
	private boolean running = false;
	private int frames = 0;
	private double tickspersecond = 8;
	//private double paintTicksPerSecond = 64;
	private double ns = 1_000_000_000 / tickspersecond;
    	
    //magic numbers
    static final int PUNCHCARD_DISPLAY_WIDTH = 32;

    static final int PAUSED_GAMEMODE = -1;
    static final int TERRARIA_GAMEMODE = 1;
    static final int PUNCHCARD_GAMEMODE = 2;
    static final int GATES_GAMEMODE = 3;
    static final int FILE_REQUEST = 50;
    //static final String GATES_GAMEMODE_STRING = "";

    static final int DEFAULT_SCREEN_SIZE = 10;//ARCHAIC: TERRARIA AND SUCH
    static final int MIN_SCREEN_PIXEL_SIZE = 1;
    static final int DEFAULT_SCREEN_Y_OFFSET = -32;
    static final int DEFAULT_SCREEN_X_OFFSET = -8;
    static final int DEFAULT_LOGIC_SCREEN_SIZE = 3;
    private static short LOGIC_SCREEN_WIDTH;
    private static short LOGIC_SCREEN_HEIGHT;
    private static byte LOGIC_SCREEN_ZHEIGHT;

    static byte LOGIC_SCREEN_LAYER;
    private static byte DEFAULT_LOGIC_SCREEN_LAYER = 0;


    static final double SCREEN_ZOOM_COEFFICENT = 1.25;//zoom in coefficient

    static int SCREEN_Y_OFFSET = 0;
    static int SCREEN_X_OFFSET = 0;

    static int P_SCREEN_Y_OFFSET = SCREEN_Y_OFFSET;
    static int P_SCREEN_X_OFFSET = SCREEN_X_OFFSET;
    
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
    /** full file name:<br>
     *  including src/saves/ and .bin
     */
    static String fileName = "";

    static final short[] mouseX = new short[2];
    static final short[] mouseY = new short[2];
    static final byte[] mouseClickNumber = new byte[2];

    static final boolean[] mouseScrollUp = {false};
    static final boolean[] mouseScrollDown = {false};

    static final boolean[] dragging = {false};
    static final boolean[] keys = new boolean[500];

    private static boolean cutting = false;
    private static boolean copying = false;
    private static boolean pasting = false;
    private static boolean savePressed = false;


    static int gameMode;
    static short numTiles;

    static final int DEFAULT_TICK_SPEED = 2000;
    static int tickNumber = 0;
    static boolean tick_true = false;
    /**
     * Speed at which the game runs at; in ms.
     */
    private static int TICK_SPEED = DEFAULT_TICK_SPEED;//in ms
    
    /**
     * Main object, inits logic gates
     */
    public Main() {

        mf = new MyFrame();
        mf.setSize(500 + 16, 500 + 38);
        mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mf.setTitle("muh gates");

        askforFile();
        //initLogicGates(this);
        System.out.println("Initialized LogicGates");
    }
    
    /**
     * The main entry point for the Java Terraria Clone application.
     * Initializes players, game objects, and the game frame.
     * Sets up key listeners for player movement and handles window resizing events.
     *
     * @param args Command line arguments
     */
    public static void main(final String[] args) {
        if(args != null && args.length > 0) {
            if (args[0].equals(Integer.toString(TERRARIA_GAMEMODE))) {
                numTiles = DEFAULT_SCREEN_SIZE;
                gameMode = TERRARIA_GAMEMODE;
                initTerrariaClone();
                System.out.println("Initialized Terraria Clone");
            } else if (args[0].equals(Integer.toString(PUNCHCARD_GAMEMODE))) {
                numTiles = PUNCHCARD_DISPLAY_WIDTH;
                gameMode = PUNCHCARD_GAMEMODE;
                initPunchCard();
                System.out.println("Initialized Punch Card");
            }
        }else {
        	gameMode = GATES_GAMEMODE;
        	Main m = new Main();            
        }

    }

    /**
     * A method within Main.java to start the thread and set "running" to true.
     */
    public synchronized void start() {
        System.out.println("called start");
		thread = new Thread(this);
		thread.start();
		running = true;
	}

    /**
     * A method within Main.java to join the thread and set "running" to false.
     */
	public synchronized void stop() {
        System.out.println("called stop");
		try {
			thread.join();//kills off thread
			running = false;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("OOPS");
		}
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		int lastTick = tickNumber;
		//double amountOfTicks = 240.0;
		double delta = 0;
		int deltaTick = 0;
		long timer = System.currentTimeMillis();

		while(running) {
			long now = System.nanoTime();
			int currentTick = tickNumber;
			delta += (now- lastTime) / ns; 
			deltaTick = currentTick - lastTick;
			lastTime = now;

			while(delta >= 1) {
				b.tick(tickNumber);
				tickNumber++;
				//if (tickNumber % 4 == 0) {
			        mgs.repaint(); // paint every 4 ticks
			    //}
				delta--;
			}
            
			if(running) {
				frames++;
			}
			if(System.currentTimeMillis() - timer >= 1000) {
				//checks frames every second
				timer += 1000;
				System.out.println("TICKS: "+deltaTick+" FPS: " + mgs.paintsPerSecond + " "+ frames);
				//mgs.paintFPS(frames);
				mgs.paintsPerSecond = 0;
				frames = 0;
				lastTick = tickNumber;
			}
		}
	}


    private void askforFile() {
        gameMode = FILE_REQUEST;

        JPanel masterPanel = new JPanel();
        masterPanel.setLayout(new BoxLayout(masterPanel, BoxLayout.Y_AXIS));
        JPanel topPanel = new JPanel();
        JLabel topLabel = new JLabel("LOAD OR CREATE FILE");
        topPanel.add(topLabel);
        masterPanel.add(topPanel);


        JPanel buttons = new JPanel();
        JButton load = new JButton("Load File");
        buttons.add(load);
        JButton create = new JButton("Create File");
        buttons.add(create);
        masterPanel.add(buttons);

        mf.add(masterPanel);

        mf.setVisible(true);

        ActionListener gListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                buttons.removeAll();
                masterPanel.remove(buttons);

                if(e.getSource() == load) {
                    topLabel.setText("<html>LOAD FILE; LEAVE EMPTY FOR DEFAULT FILE.<br>PRESS ENTER TO SUBMIT.</html>");
                    JPanel txtPanel = new JPanel();
                    JTextArea txt = new JTextArea(1, 20);
                    txtPanel.add(txt);
                    masterPanel.add(txtPanel);

                    txt.requestFocusInWindow();
                    masterPanel.revalidate();
                    masterPanel.repaint();


                    txt.addKeyListener(new KeyAdapter() {
                        public void keyPressed(KeyEvent e) {
                            if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                                e.consume();

                                if (!txt.getText().isBlank()) {
                                    int length = txt.getText().length();
                                    //if(txt.getText().contains("src/saves/"))
                                    final int length_of_file_directory = ("src/saves").length() + 1;
                                    int start = (txt.getText().startsWith("src/saves/"))
                                                ? txt.getText().lastIndexOf("src/saves/") + length_of_file_directory
                                                : 0;
                                    int end = (txt.getText().endsWith(".bin"))
                                                ? txt.getText().lastIndexOf(".bin")
                                                : txt.getText().length();
                                    fileName = txt.getText().substring(start, end);
                                    System.out.println("fileName is " + fileName);
                                    fileName = "src/saves/" + fileName + ".bin";
                                    System.out.println("fileName is " + fileName);

                                    Path p = Paths.get(fileName);
                                    if(Files.exists(p)) {
                                        txt.removeKeyListener(this);
                                        mf.remove(txt);
                                        mf.remove(masterPanel);
                                        initLogicGates(Main.this, fileName);
                                    }else {
                                        topLabel.setText("FILE INVALID, TRY AGAIN");
                                        txt.setText("");
                                        txt.requestFocusInWindow();
                                        masterPanel.revalidate();
                                        masterPanel.repaint();

                                    }
                                }else {

                                    fileName = "src/saves/500,500,15 2025-10-19 02-00-08.bin";
                                    if (Files.exists(Paths.get(fileName))) {
                                        mf.remove(txt);
                                        mf.remove(masterPanel);
                                        initLogicGates(Main.this, fileName);
                                    } else {
                                        topLabel.setText("FILE INVALID, TRY AGAIN");
                                    }
                                }

                            }
                        }
                    });
                }

                if(e.getSource() == create) {
                    topLabel.setText("<html>CREATE FILE<br>DO NOT PUT A DIRECTORY OR FILE TYPE</html>");

                    masterPanel.add(Box.createRigidArea(new Dimension(0, 10))); // spacing

                    // Name
                    JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JLabel nameLabel = new JLabel("Name:");
                    JTextField nameField = new JTextField(20);
                    namePanel.add(nameLabel);
                    namePanel.add(nameField);
                    masterPanel.add(namePanel);

                    // Width
                    JPanel widthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JLabel widthLabel = new JLabel("Width:");
                    JTextField widthField = new JTextField(10);
                    widthPanel.add(widthLabel);
                    widthPanel.add(widthField);
                    masterPanel.add(widthPanel);

                    // Height
                    JPanel heightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JLabel heightLabel = new JLabel("Height:");
                    JTextField heightField = new JTextField(10);
                    heightPanel.add(heightLabel);
                    heightPanel.add(heightField);
                    masterPanel.add(heightPanel);

                    // Depth
                    JPanel depthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JLabel depthLabel = new JLabel("Depth:");
                    JTextField depthField = new JTextField(10);
                    depthPanel.add(depthLabel);
                    depthPanel.add(depthField);
                    masterPanel.add(depthPanel);

                    // Create Button
                    JButton createButton = new JButton("Create File");
                    createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    masterPanel.add(Box.createRigidArea(new Dimension(0, 10))); // spacing
                    masterPanel.add(createButton);

                    createButton.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String widthText = widthField.getText().trim();
                            String heightText = heightField.getText().trim();
                            String depthText = depthField.getText().trim();
                            String nameText = nameField.getText().trim();

                            if(nameText.isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Please enter a name. \nDo not put a file directory or file type.");
                                return;
                            }

                            try {
                                int width = Integer.parseInt(widthText);
                                int height = Integer.parseInt(heightText);
                                int depth = Integer.parseInt(depthText);

                                // Optional: check for positive numbers
                                if(width <= 0 || height <= 0 || depth <= 0) {
                                    JOptionPane.showMessageDialog(null, "Width, Height, and Depth must be positive numbers.");
                                    return;
                                }

                                // Everything is valid, create the file
                                fileName = "src/saves/" + nameText + ".bin";
                                new FileCreator(width, height, depth, fileName);
                                mf.remove(masterPanel);
                                initLogicGates(Main.this, fileName);

                            } catch(NumberFormatException ex) {
                                // Inform the user without crashing
                                JOptionPane.showMessageDialog(null, "Width, Height, and Depth must be valid numbers.");
                            }
                        }
                    });

                }




                mf.setVisible(true);

            }
        };
        load.addActionListener(gListener);
        create.addActionListener(gListener);


    }
    
    
    private void initLogicGates(Main m, final String fileName) {
        gameMode = GATES_GAMEMODE;

        short width;
        short height;
        byte zHeight;
        //String fileName = "src/saves/4,4,1 2025-06-14 12-45-05.bin";

        //main one
        //String fileName = "src/saves/500,500,15 2025-10-19 02-00-08.bin";
        //new one with depth of 6 tiles
        String newFileName = "src/saves/bigUn.bin";
        //src/saves/10,10,3 2025-06-14 21-33-17.bin";
        //String fileName = "src/saves/medium_test.bin";
        //String fileName = "src/saves/b.bin";
        //String fileName = "src/saves/b.txt";
        //String fileName = "src/saves/400,400,3 2025-06-14 02-38-29.txt";
        //String fileName = "src/saves/add1movement.txt";

        //USE THIS IF YOU WANT TO CREATE A NEW FILE
        //FileCreator fs = new FileCreator(500,500,15, newFileName);



        //String fileName = askforFile();


        Path file = Paths.get(fileName);

        //Path file = Paths.get("add1movement.txt");

        //Path file = Paths.get("test_breadboard.txt");
        //Path file = Paths.get("save3.txt");
        //a file which attempts repeatedly add one in a loop (0, 1, 2, 3 ... 14, 15, 0)
        //Path file = Paths.get("s.txt");
        //Path file = Paths.get("small.txt"); //first 3d. 3by3by3

        BreadBoardFileLoader bfl = new BreadBoardFileLoader(file);

        try {
            short [] dim = bfl.dimensions();
            width = dim[0];
            height = dim[1];
            zHeight = (byte)dim[2];
            System.out.println("width " + width + " height " + height + " zHeight " + zHeight);
            LOGIC_SCREEN_WIDTH = width;
            LOGIC_SCREEN_HEIGHT = height;
            LOGIC_SCREEN_ZHEIGHT = zHeight;
            LOGIC_SCREEN_LAYER = DEFAULT_LOGIC_SCREEN_LAYER;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        mf = new MyFrame();
//        mf.setSize(500 + 16, 500 + 38);
//        mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        mf.setTitle("muh gates");

        b = new BreadBoard(width, height, zHeight);


        try {
            bfl.load(b);
            System.out.println("FILE LOADED " + file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mgs = new MyGameScreen(width, height, zHeight, b, m);
        mf.setGameScreen(mgs);
        mf.setVisible(true);

        //b.memorizeWireRoutes(0);

        mf.revalidate();
        mf.repaint();
        mf.requestFocusInWindow(); // restore focus to game screen
        addAllListeners(m);
        
//        Timer timer = new Timer(0, new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if(gameMode != PAUSED_GAMEMODE) {
//                    tickNumber++;
//                    b.tick(tickNumber);
//                }
//            }
//        });
//        timer.start();

        m.start();

    }

    private void addAllListeners(Main m) {
        //System.out.println("called addAllListeners");
        mf.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                //System.out.println("Window resized to: " + mf.getSize());
            	mf.getGameScreen().updateSize();
            	mgs.repaint();
            }
        });

        mf.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //checks if mouse is clicked and draws the screen accordingly
                if(gameMode != PAUSED_GAMEMODE) {
                    if (b.checkClick(
                            e,
                            (short) (e.getX() - (int) SCREEN_X_OFFSET + DEFAULT_SCREEN_X_OFFSET),
                            (short) (e.getY() - (int) SCREEN_Y_OFFSET + DEFAULT_SCREEN_Y_OFFSET),
                            (byte) LOGIC_SCREEN_LAYER)
                    ) {
                    	mgs.repaint();
                    }
                }



            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(gameMode != PAUSED_GAMEMODE) {
                    mouseClickNumber[0] = (byte) e.getButton();
                    mouseX[0] = (short) e.getX();
                    mouseY[0] = (short) e.getY();
                    P_SCREEN_X_OFFSET = Main.SCREEN_X_OFFSET;
                    P_SCREEN_Y_OFFSET = SCREEN_Y_OFFSET;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(gameMode != PAUSED_GAMEMODE) {
                    if (copying && b.getGamemode().equals(BreadBoard.COPYING_KEYWORD)) {
                        copying = false;
                        b.setGamemode(BreadBoard.DEFAULT_KEYWORD);
                        mgs.repaint();
                        return;
                    }
                    if (cutting && b.getGamemode().equals(BreadBoard.CUTTING_KEYWORD)) {
                        short sX = (short) ((Main.mouseX[0] - Main.SCREEN_X_OFFSET) / MyGameScreen.tileWidth);
                        if (sX < 0) sX = 0;

                        short sY = (short) ((Main.mouseY[0] - Main.SCREEN_Y_OFFSET) / MyGameScreen.tileHeight);
                        if (sY < 0) sY = 0;

                        short eX = (short) ((Main.mouseX[1] - Main.SCREEN_X_OFFSET) / MyGameScreen.tileWidth);
                        if (eX > MyGameScreen.xPixels) eX = MyGameScreen.xPixels;

                        short eY = (byte) ((Main.mouseY[1] - Main.SCREEN_Y_OFFSET) / MyGameScreen.tileHeight);
                        if (eY > MyGameScreen.yPixels) eY = MyGameScreen.yPixels;
                        b.eraseRegion(sX, sY, b.BOTTOM_Z, eX, eY, b.TOP_Z);
                        cutting = false;
                        b.setGamemode(BreadBoard.DEFAULT_KEYWORD);
                        mgs.repaint();
                        return;
                    }
                    mf.getContentPane().repaint();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

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
                if(gameMode != PAUSED_GAMEMODE) {
                    //DO NOT USE "!(cutting || copying)": these will be used for actual cutting and pasting
                    //i.e. when the mouse is being dragged to cut/copy
                    //This if must check whether I've not clicked CTL C or CTL X
                    if (!(b.getGamemode() == BreadBoard.COPYING_KEYWORD ||
                            b.getGamemode() == BreadBoard.CUTTING_KEYWORD)) {//dragging without CTL+C
                        if (b.getGamemode().equals(BreadBoard.DEFAULT_KEYWORD)) {
                            SCREEN_X_OFFSET = -(mouseX[0] - e.getX()); //
                            SCREEN_Y_OFFSET = -(mouseY[0] - e.getY()); //+ DEFAULT_SCREEN_Y_OFFSET;
                            SCREEN_X_OFFSET += P_SCREEN_X_OFFSET;
                            SCREEN_Y_OFFSET += P_SCREEN_Y_OFFSET;

                            mf.getGameScreen().xOffset = (int) SCREEN_X_OFFSET;
                            mf.getGameScreen().yOffset = (int) SCREEN_Y_OFFSET;
                            
                            mgs.repaint();
                        } else if (b.getGamemode().equals(BreadBoard.EDITING_KEYWORD)) {
                            //System.out.println(e.getButton()); is 0 in this case, for all clicks
                            if (mouseClickNumber[0] == MouseEvent.BUTTON1) {//left button tapped and then dragged
                                if (b.checkClick(
                                        e,
                                        (short) (e.getX() - (int) SCREEN_X_OFFSET + DEFAULT_SCREEN_X_OFFSET),
                                        (short) (e.getY() - (int) SCREEN_Y_OFFSET + DEFAULT_SCREEN_Y_OFFSET),
                                        (byte) LOGIC_SCREEN_LAYER)) {
                                	
                                    mgs.repaint();
                                }
                            } else if (mouseClickNumber[0] == MouseEvent.BUTTON3) {//right button tapped and then dragged
                                if (b.checkClick(
                                        e,
                                        (short) (e.getX() - (int) SCREEN_X_OFFSET + DEFAULT_SCREEN_X_OFFSET),
                                        (short) (e.getY() - (int) SCREEN_Y_OFFSET + DEFAULT_SCREEN_Y_OFFSET),
                                        LOGIC_SCREEN_LAYER)) {
                                	
                                	mgs.repaint();
                                }
                            }
                        }
                    } else {//dragging with CTL+C or CTL+X
                        //this first and last if may be irrelevant
                        if (b.getGamemode().equals(BreadBoard.EDITING_KEYWORD)) {//while editing

                            mouseX[1] = (short) e.getX();
                            mouseY[1] = (short) e.getY();

                            b.checkClick(e, mouseX[1], mouseY[1], LOGIC_SCREEN_LAYER);
                             
                            mgs.repaint();
                        } else if (b.getGamemode().equals(BreadBoard.COPYING_KEYWORD)
                                || b.getGamemode().equals(BreadBoard.CUTTING_KEYWORD)) {

                            if(b.getGamemode().equals(BreadBoard.COPYING_KEYWORD)){
                                copying = true;
                                cutting = false;
                            }else if(b.getGamemode().equals(BreadBoard.CUTTING_KEYWORD)){
                                cutting = true;
                                copying = false;
                            }

                            mouseX[1] = (short) e.getX();
                            mouseY[1] = (short) e.getY();

                            b.checkClick(e, mouseX[1], mouseY[1], LOGIC_SCREEN_LAYER);
                             
                            mgs.repaint();
                        } else if (b.getGamemode().equals(BreadBoard.DEFAULT_KEYWORD)) {
                            copying = false;
                            cutting = false;
                            pasting = false;
                        }
                    }
                }
//                dragging[0] = true;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX[0] = (short) e.getX();
                mouseY[0] = (short) e.getY();
            }
        });



        mf.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                keys[e.getKeyCode()] = true;
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if (gameMode == GATES_GAMEMODE) {
                        gameMode = PAUSED_GAMEMODE;
                        running = false;
                    } else {
                        System.out.println("pressed escape to resume");
                        m.start();
                        gameMode = GATES_GAMEMODE;
                    }
                }
                if (gameMode != PAUSED_GAMEMODE) {
                    if (keys[KeyEvent.VK_E]) {
                        if (b.getGamemode().equals(BreadBoard.DEFAULT_KEYWORD)) {
                            b.setGamemode(BreadBoard.EDITING_KEYWORD);
                        } else if (b.getGamemode().equals(BreadBoard.EDITING_KEYWORD)) {
                            b.setGamemode(BreadBoard.DEFAULT_KEYWORD);
                        }
                        mf.getContentPane().repaint();
                    } else if (keys[KeyEvent.VK_R]) {//rotate with "R"
                        b.rotateItem(
                                0,
                                (mouseX[0] - (int) SCREEN_X_OFFSET + DEFAULT_SCREEN_X_OFFSET) / MyGameScreen.tileWidth,
                                (mouseY[0] - (int) SCREEN_Y_OFFSET + DEFAULT_SCREEN_Y_OFFSET) / MyGameScreen.tileHeight,
                                LOGIC_SCREEN_LAYER);
//                    if (keys[KeyEvent.VK_SHIFT]) {//rotate second dir with "shift + R"
//
//                    }
                    } else if (keys[KeyEvent.VK_Q]) {
                        b.rotateItem(
                                1,
                                (mouseX[0] - (int) SCREEN_X_OFFSET + DEFAULT_SCREEN_X_OFFSET) / MyGameScreen.tileWidth,
                                (mouseY[0] - (int) SCREEN_Y_OFFSET + DEFAULT_SCREEN_Y_OFFSET) / MyGameScreen.tileHeight,
                                LOGIC_SCREEN_LAYER);
                    } else if (keys[KeyEvent.VK_L]){
//                        gameMode = FILE_REQUEST;
//                        running = false;

                    } else if (keys[KeyEvent.VK_CONTROL]) {
                        if (!(keys[KeyEvent.VK_C] || keys[KeyEvent.VK_V] || keys[KeyEvent.VK_X])) {//zoom in and out if not copying, cutting, or pasting
                            if (mouseScrollDown[0]) {
                                if (MyGameScreen.tileSize / SCREEN_ZOOM_COEFFICENT >= MIN_SCREEN_PIXEL_SIZE) {
                                    double dx = (mouseX[0] + DEFAULT_SCREEN_X_OFFSET - SCREEN_X_OFFSET);
                                    double dy = (mouseY[0] + DEFAULT_SCREEN_Y_OFFSET - SCREEN_Y_OFFSET);

                                    int oldTileSize = MyGameScreen.tileSize;
                                    MyGameScreen.tileSize = (byte) (MyGameScreen.tileSize / SCREEN_ZOOM_COEFFICENT);

                                    SCREEN_X_OFFSET = (int)(mouseX[0] + DEFAULT_SCREEN_X_OFFSET
                                            - (dx * MyGameScreen.tileSize / oldTileSize));
                                    SCREEN_Y_OFFSET = (int)(mouseY[0] + DEFAULT_SCREEN_Y_OFFSET
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
                                    MyGameScreen.tileSize = (byte) (MyGameScreen.tileSize * SCREEN_ZOOM_COEFFICENT);
                                    if (MyGameScreen.tileSize == oldTileSize) {
                                        MyGameScreen.tileSize++;
                                    }


                                    SCREEN_X_OFFSET = (int)(mouseX[0] + DEFAULT_SCREEN_X_OFFSET
                                            - (dx * (double) MyGameScreen.tileSize / (double) oldTileSize));
                                    SCREEN_Y_OFFSET = (int)(mouseY[0] + DEFAULT_SCREEN_Y_OFFSET
                                            - (dy * (double) MyGameScreen.tileSize / (double) oldTileSize));

                                    mgs.xOffset = (int) SCREEN_X_OFFSET;
                                    mgs.yOffset = (int) SCREEN_Y_OFFSET;
                                }
                                mgs.setTileSize(MyGameScreen.tileSize, MyGameScreen.tileSize);
                                
                                mgs.repaint();
                                mouseScrollUp[0] = false;
                            }
                        } else if (keys[KeyEvent.VK_C]) {
                            //copying = true; must be set while dragging
                            cutting = false;
                            pasting = false;
                            b.setGamemode(BreadBoard.COPYING_KEYWORD);
                            
                            mgs.repaint();
                        } else if (keys[KeyEvent.VK_V]) {
                            copying = false;
                            cutting = false;
                            pasting = true;//this should be set to true because we don't need to drag in order to paste
                            System.out.println("paste from main");
                            b.setGamemode(BreadBoard.PASTING_KEYWORD);
                            paste();
                            
                            mgs.repaint();
                        } else if (keys[KeyEvent.VK_X]) {
                            copying = false;
                            //cutting = true;
                            pasting = false;
                            b.setGamemode(BreadBoard.CUTTING_KEYWORD);
                            
                            mgs.repaint();
                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        b.itemCursor = 0;
                    } else if (e.getKeyCode() == KeyEvent.VK_0) {
                        b.itemCursor = 0;
                    } else if (e.getKeyCode() == KeyEvent.VK_1) {
                        b.itemCursor = 1;
                    } else if (e.getKeyCode() == KeyEvent.VK_2) {
                        b.itemCursor = 52;//2 is a negative wire, 52 is a dead wire
                    } else if (e.getKeyCode() == KeyEvent.VK_3) {
                        b.itemCursor = 3;
                    } else if (e.getKeyCode() == KeyEvent.VK_4) {
                        b.itemCursor = 4;
                    } else if (e.getKeyCode() == KeyEvent.VK_5) {
                        b.itemCursor = 5;
                    } else if (e.getKeyCode() == KeyEvent.VK_6) {
                        b.itemCursor = 6;
                    } else if (e.getKeyCode() == KeyEvent.VK_7) {
                        b.itemCursor = 7;
                    } else if (e.getKeyCode() == KeyEvent.VK_8) {
                        b.itemCursor = 8;
                    } else if (e.getKeyCode() == KeyEvent.VK_9) {
                        b.itemCursor = 9;
                    } else if (e.getKeyCode() == KeyEvent.VK_I) {
                        b.itemCursor = 10;
                    } else if (e.getKeyCode() == KeyEvent.VK_O) {
                        b.itemCursor = 11;
                    } else if (e.getKeyCode() == KeyEvent.VK_P) {
                        b.itemCursor = 17;//XOR
                    } else if (e.getKeyCode() == KeyEvent.VK_OPEN_BRACKET) {
                        //cycle down
                        if(b.itemCursor == 52){
                            b.itemCursor = 1;
                        }else if(b.itemCursor == 3){
                            b.itemCursor = 52;
                        }else if(b.itemCursor == 17) {
                            b.itemCursor = 11;
                        }else {
                            b.itemCursor = (b.itemCursor > 0) ? (byte) (b.itemCursor - 1) : b.itemCursor;//XOR
                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_CLOSE_BRACKET) {
                        //cycle up
                        if(b.itemCursor == 52){
                            b.itemCursor = 3;
                        }else if(b.itemCursor == 1){
                            b.itemCursor = 52;
                        }else if(b.itemCursor == 11) {
                            b.itemCursor = 17;
                        }else {
                            b.itemCursor = (b.itemCursor < 23) ? (byte) (b.itemCursor + 1) : b.itemCursor;//XOR
                        }
                    }else if (e.getKeyCode() == KeyEvent.VK_EQUALS) {
                        if (LOGIC_SCREEN_LAYER + 1 <= b.TOP_Z) {
                            LOGIC_SCREEN_LAYER++;
                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_MINUS) {
                        if (LOGIC_SCREEN_LAYER - 1 >= b.BOTTOM_Z) {
                            LOGIC_SCREEN_LAYER--;
                        }
                    }


                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        double speedup = 2;
                        if (keys[KeyEvent.VK_SHIFT]) {
                            if ((int) (tickspersecond / speedup) > 0.25) {//four seconds per tick
                                tickspersecond = (int) (tickspersecond / speedup);//lower number = slower speed
                                ns = 1_000_000_000 / tickspersecond;
                                System.out.println(tickspersecond);
                                //timer.setDelay(TICK_SPEED);
                            }
                        } else {
                            if (tickspersecond <= 4) speedup = 2;
                            if (tickspersecond * speedup <= 64000) {//64000 tps
                            	tickspersecond = (int) (tickspersecond * speedup);
                            	ns = 1_000_000_000 / tickspersecond;
                            	System.out.println(tickspersecond);
                                //timer.setDelay(TICK_SPEED);
                            }
                        }
                    }

                    //clear signal queue
                    if(e.getKeyCode() == KeyEvent.VK_BACK_SLASH){
                        b.setSignalArrayToNull();
                    }
                }
                //this happens regardless if game is paused or not
                if (keys[KeyEvent.VK_CONTROL] && keys[KeyEvent.VK_S]) {
                    if (!savePressed) {
                        savePressed = true; // prevent repeat

                        if (keys[KeyEvent.VK_SHIFT]) {
                            fileName = JOptionPane.showInputDialog("Enter name to save as.");
                            final int length_of_file_directory = ("src/saves").length() + 1;
                            int start = (fileName.startsWith("src/saves/"))
                                    ? fileName.lastIndexOf("src/saves/") + length_of_file_directory
                                    : 0;
                            int end = (fileName.endsWith(".bin"))
                                    ? fileName.lastIndexOf(".bin")
                                    : fileName.length();
                            fileName = fileName.substring(start, end);
                            fileName = "src/saves/" + fileName + ".bin";
                        }
                        b.saveTileBytes(LOGIC_SCREEN_WIDTH, LOGIC_SCREEN_HEIGHT, LOGIC_SCREEN_ZHEIGHT);
                        // reset after dialog (focus lost causes stuck keys)
                        keys[KeyEvent.VK_CONTROL] = false;
                        keys[KeyEvent.VK_S] = false;
                        keys[KeyEvent.VK_SHIFT] = false;
                    }
                } else {
                    savePressed = false; // reset when Ctrl+S released
                }

                 
                mgs.repaint();

            }

            @Override
            public void keyReleased(KeyEvent e) {
                //I think fn key is 524, so update keys to that number if you want the function key
                //its either that or f6
                keys[e.getKeyCode()] = false;
                //System.out.println("key " + e.getKeyCode() + " released");
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
        short sX = (short) ((mouseX[0] - SCREEN_X_OFFSET) / MyGameScreen.tileWidth);
        if(sX < 0) sX = 0;

        short sY = (short) ((mouseY[0] - SCREEN_Y_OFFSET) / MyGameScreen.tileHeight);
        if(sY < 0) sY = 0;

        if(pasting && b.getGamemode().equals(BreadBoard.PASTING_KEYWORD)){
            for(byte i = 0; i < mgs.tempCutCopyPasteBoardList.size(); i++) {
                for (short j = 0; j < mgs.tempCutCopyPasteBoardList.get(0).size(); j++) {
                    for (short k = 0; k < mgs.tempCutCopyPasteBoardList.get(0).get(0).size(); k++) {

                        //dont delete this three
//                        b.setBreadBoardTileByte(mgs.tempCutCopyPasteBoardList.get(i).get(j).get(k), (short) (k + sX), (short) (j + sY), i);
//                        b.setBreadBoardDirectionTile(mgs.cutCopyPasteBoardDirection1[i][j + sY][k + sX],k + sX, j + sY, i);
//                        b.setBreadBoardDirection2Tile(mgs.cutCopyPasteBoardDirection2[i][j + sY][k + sX],k + sX, j + sY, i);


//                        b.setBreadBoardTileByte(mgs.cutCopyPasteBoardBytes[i][j + sY][k + sX], (short) (k + sX), (short) (j + sY), i);
//                        b.setBreadBoardDirectionTile(mgs.cutCopyPasteBoardDirection1[i][j + sY][k + sX],k + sX, j + sY, i);
//                        b.setBreadBoardDirection2Tile(mgs.cutCopyPasteBoardDirection2[i][j + sY][k + sX],k + sX, j + sY, i);
//                        b.changeBreadBoard(
//                                b.convertToItemEnumOrdinal(mgs.tempCutCopyPasteBoard[i][j + sY][k + sX]),
//                                mgs.tempCutCopyPasteBoardDirection1[i][j + sY][k + sX],
//                                mgs.tempCutCopyPasteBoardDirection2[i][j + sY][k + sX],
//                                k + sX,
//                                j + sY,
//                                i);
//                        b.changeBreadBoardBytes(
//                                mgs.cutCopyPasteBoardBytes[i][j + sY][k + sX],
//                                mgs.cutCopyPasteBoardDirection1[i][j + sY][k + sX],
//                                mgs.cutCopyPasteBoardDirection2[i][j + sY][k + sX],
//                                (short) (k + sX),
//                                (short) (j + sY),
//                                i);
                        b.changeBreadBoardBytes(
                                mgs.tempCutCopyPasteBoardList.get(i).get(j).get(k),
                                mgs.tempCutCopyPasteBoardDirection1List.get(i).get(j).get(k),
                                mgs.tempCutCopyPasteBoardDirection2List.get(i).get(j).get(k),
                                (short) (k + sX),
                                (short) (j + sY),
                                i);

                    }
                }
            }
             
            mgs.repaint();
            b.setGamemode(BreadBoard.DEFAULT_KEYWORD);
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

    public static BreadBoard getBreadBoard() {
        return b;
    }
    
    public int getFrames() {
    	return frames;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
