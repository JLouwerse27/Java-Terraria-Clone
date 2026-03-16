package src.code;

import src.code.Enums.Direction;

/**
 * Abstract Parent Class for the breadboard
 */
public abstract class BreadBoardItem { //extends Thread {
    protected short x = 0;
    protected short y = 0;
    protected short z = 1;//1 is middle in a 3 block tall array
    //putting this in DigitalBreadBoardItem
    //private TState out = TState.DEAD;//-1 off, 0 dead, 1 on
    protected Direction dir = Direction.RIGHT;

    protected short id;

    public BreadBoardItem(final Direction dir, final short x, final short y, final short z) {
        this.dir = dir;
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = Main.getBreadBoard().globalLatestID;
        Main.getBreadBoard().globalLatestID++;
    }

    public abstract void signal(int t);

    public short getX(){
        return x;
    }

    public short getY(){
        return y;
    }

    public short getZ() {
        return z;
    }

    public Direction getDir(){
        return dir;
    }

    //digital
//    public TState getOut(){
//        return out;
//    }

    public void setDir(final Direction dir){
        this.dir = dir;
    }

    public abstract byte returnTile();

}
