package src.code;


import src.code.Digital.DigitalBreadBoardItem;
import src.code.Enums.Direction;
import src.code.Enums.TState;

/**
 * Abstract gate class
 */
public abstract class Gate extends DigitalBreadBoardItem {

    protected TState out = TState.UNUSED;
    protected TState A = TState.UNUSED;//input is from the left -- gate is right
    protected TState B = TState.UNUSED;//input is from a block up -- gate is down
    protected TState C = TState.UNUSED;//input is from the right -- gate is left
    protected TState D = TState.UNUSED;//input is from a block down -- gate is up
    protected TState E = TState.UNUSED;//input is from zBelow -- gate is zAbove the input
    protected TState F = TState.UNUSED;//input is from zAbove -- gate is zBelow the input
    ////no F because there should never be six inputs!

    protected int signalsOutputAtCurrentTick = 0;
    /**There can be five possible inputs, so five possible states
     * in one tick for any given gate*/
    //protected int [] signals = new int[5];

    byte numberOfInputsTotal = 0;
    byte numberOfInputsOn = 0;

    public Gate(final Direction dir, final short x, final short y, final short z) {
        super(dir, x, y, z);
    }

    public void setA(final TState on) {
        this.A = on;
    }

    public void setB(final TState on) {
        this.B = on;
    }

    public void setC(final TState on) {
        this.C = on;
    }

    public void setD(final TState on) {
        this.D = on;
    }

    public void setE(final TState on) {
        this.E = on;
    }

    public void setF(final TState on) {
        this.F = on;
    }

    public abstract void calculate();

    /**
     * A is left side input
     * B is bottom side input
     * C is right side input
     * D is top side input
     * E is zBelow input
     * F is zAbove input
     *
     * @return whether the calling function should proceed with calculations,
     * i.e. return false if you put an input where the output of a gate should be
     */
    public boolean setRightGate(final TState s, final short deltax, final short deltay, final short deltaz){
        //this gate is to the right of the input (ex. wire)
        if(deltax == 1){
            if(this.getDir() != Direction.LEFT){//A gate going LEFT would be pointing back to the wire
                setA(s);
                return true;
            }
            return false;
        }else if(deltax == -1){//this gate is to the left of the input (ex. wire)
            if(this.getDir() != Direction.RIGHT){//A gate going RIGHT would be pointing back to the wire
                setC(s);
                return true;
            }
            return false;
        }else if(deltay == 1){//this gate is below the input (ex. wire)
            if(this.getDir() != Direction.UP){
                setB(s);
                return true;
            }
            return false;
        }else if(deltay == -1){//this gate is above the input (ex. wire)
            if(this.getDir() != Direction.DOWN){
                setD(s);
                return true;
            }
            return false;
        }else if(deltaz == 1){//the input is zBelow the gate; this gate is zAbove the input
            if(this.getDir() != Direction.INTO){//A gate going INTO would be pointing back to the wire
                setE(s);
                return true;
            }
            return false;
        }else if(deltaz == -1){//the input is zAbove the gate; this gate is zBelow the input
            if(this.getDir() != Direction.OUTOF){//A gate going OUTOF would be pointing back to the wire
                setF(s);
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Gates have multiple inputs coming in, usually at the same time, however it is calculated
     * one input at a time, thus giving multiple outputs even if the inputs come in simultaneously.
     * @param tick_when_set
     */
    public void signal(final int tick_when_set){
        //if(Main.getBreadBoard().isGatesAllowedToSignalOut()){
            Main.getBreadBoard().queueSignal(
                    this.getDir(),
                    out,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    tick_when_set+1,
                    returnTile(),id);
        //}else {
        //    signals[signalsOutputAtCurrentTick]
        //}
    }

    public TState getOut(){
        return out;
    }
}



