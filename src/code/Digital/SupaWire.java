package src.code.Digital;


import src.code.BreadBoard;
import src.code.Enums.Direction;

/**
 * Just place a start and end to the wire.
 */
public class SupaWire extends BreadBoard.Wire {
    //public static allWires =

    //address part of the node
    public SupaWire address = null;

    public SupaWire(BreadBoard board, Direction dir, Direction dir2, short x, short y, short z) {
        board.super(dir, dir2, x, y, z);
    }

    public void setAddress(){

    }

    @Override
    public void signal(int t) {
        if(address != null){

        }
    }

    @Override
    public byte returnTile() {
        return 0;
    }
}
