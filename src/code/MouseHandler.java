package src.code;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler {

    private final MyFrame mf;
    private final BreadBoard b;

    static double SCREEN_Y_OFFSET = Main.SCREEN_Y_OFFSET;
    static double SCREEN_X_OFFSET = Main.SCREEN_X_OFFSET;

    static double P_SCREEN_Y_OFFSET = SCREEN_Y_OFFSET;
    static double P_SCREEN_X_OFFSET = SCREEN_X_OFFSET;

    static final int DEFAULT_SCREEN_SIZE = 10;
    static final int DEFAULT_SCREEN_Y_OFFSET = Main.DEFAULT_SCREEN_Y_OFFSET;
    static final int DEFAULT_SCREEN_X_OFFSET = Main.DEFAULT_SCREEN_X_OFFSET;
    static final int DEFAULT_LOGIC_SCREEN_SIZE = Main.DEFAULT_LOGIC_SCREEN_SIZE;

    final int[] mouseX = new int[2];
    final int[] mouseY = new int[2];

    final boolean[] mouseScrollUp = {false};
    final boolean[] mouseScrollDown = {false};

    final boolean[] dragging = {false};
    boolean[] keys = new boolean[500];



    public MouseHandler(final MyFrame mf, final BreadBoard b){

        this.mf = mf;
        this.b = b;
    }

    class MouseListenerHandler implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            //checks if clicked and draws the screen accordingly
//                mouseX[0] = e.getX();
//                mouseY[0] = e.getY();

            if(b.checkClick(
                    e,
                    (short) (e.getX() - SCREEN_X_OFFSET + DEFAULT_SCREEN_X_OFFSET),
                    (short) (e.getY() - SCREEN_Y_OFFSET + DEFAULT_SCREEN_Y_OFFSET),
                    Main.LOGIC_SCREEN_LAYER)) {
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
    }


}
