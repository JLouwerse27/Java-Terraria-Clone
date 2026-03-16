package src.code.analogue;

import src.code.Enums.Direction;
import src.code.Main;
import src.code.Enums.TState;
import src.code.Enums.TileByte;

import static src.code.BreadBoard.*;

/**
 * AnalogueWireStart will have an input and an output tile, and anything in between will be just drawn as a wire
 */
public class AnalogueWire extends AnalogueBreadBoardItem {

    private Direction dir2;

    /**
     * AnalogueWire[0] is previous wire, AnalogueWire[1] is next wire
     */
    private AnalogueWire[] linkedList = new AnalogueWire[2];

    /**
     *
     * @param dir
     * @param x
     * @param y
     * @param z
     * //@param firstMiddleLast set to 0 for first, 1 for middle, 2 for last
     */
    public AnalogueWire(Direction dir, short x, short y, short z) {
        super(dir, x, y, z);
//        if(firstMiddleLast == 0) {
//            //first link in list
//            linkedList[0] = this;
//        }else if(firstMiddleLast == 2) {
//            //last link in list
//            linkedList[1] = this;
//        }
    }

    @Override
    protected void AnalogueSignal() {

    }

    public void inputSignal(final short dx, final short dy, final short dz, final double out, final int t) {
        this.out = out;
        //check first if this queueSignal is coming from the right input direction
        //ie if this wire is facing right, then this should not be taking input
        //from an object to the right of it, thus possibly creating an infinite loop
        //or an insane amount of signals; much havoc was caused because of this on May 29, 2025
        if((this.getDir() == dL && dx != 1) ||
                (this.getDir() == dR && dx != -1) ||
                (this.getDir() == dU && dy != 1) ||
                (this.getDir() == dD && dy != -1) ||
                (this.getDir() == dO && dz != -1) || //out of the screen
                (this.getDir() == dI && dz != 1) ||//into the screen
                this.getDir() == dN) {
//            if (out >= 0.5) {
//                setBreadBoardTileByte(TileByte.AnalogueWireHigh.getSymbol(), getX(), getY(), getZ());
//            } else {
//                setBreadBoardTileByte(TileByte.AnalogueWireLow.getSymbol(), getX(), getY(), getZ());
//            }
//            this.dx = dx;
//            this.dy = dy;
//            this.dz = dz;
            this.signal(t);
        }
    }

    public void setDir2(final Direction dir2) {
        this.dir2 = dir2;
    }

    public byte returnTile(){
//        if(out >= 0.5){
//            return TileByte.AnalogueWireHigh.getSymbol();
//        }else {
//            return TileByte.AnalogueWireLow.getSymbol();
//        }
        return TileByte.AnalogueWire.getSymbol();
    }

    /**
     * Output the signal <br><br>
     * Like the queueSignal in Switch, searches for other board members.
     */
    public void signal(final int t) {

        TState digitalSignal = convertToDigital(out);

        Main.getBreadBoard().queueSignal(this.getDir(), digitalSignal, this.getX(), this.getY(), this.getZ(), t + 1, returnTile(),id);
        //may need to check for errors in the future
        if(this.getDir2() != this.getDir() && this.getDir2() != Direction.NONE){
            Main.getBreadBoard().queueSignal(this.getDir2(), digitalSignal, this.getX(), this.getY(), this.getZ(), t + 1, returnTile(),id);
        }
        //System.out.println("wire.queueSignal(): at " + this.getX() + " " + this.getY()
        //+ " which will be called on tick " + (t + 1));
        //}
    }

    public Direction getDir2(){
        return dir2;
    }



}
