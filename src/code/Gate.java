package src.code;




/**
 * Abstract gate class
 */
public abstract class Gate extends BreadBoardItem {

    TState out = TState.UNUSED;
    TState A = TState.UNUSED;//wire is to the left -- gate is to the right
    TState B = TState.UNUSED;//~ down
    TState C = TState.UNUSED;//~ right
    TState D = TState.UNUSED;//~ up
    TState E = TState.UNUSED;//~ zBelow
    TState F = TState.UNUSED;//~ zAbove
    ////no F because there should never be six inputs!

    byte numberOfInputsTotal = 0;
    byte numberOfInputsOn = 0;

    public Gate(final Direction dir, final short x, final short y, final byte z) {
        super(dir, x, y, z);
    }

    public abstract void setA(TState on);
    public abstract void setB(TState on);
    public abstract void setC(TState on);
    public abstract void setD(TState on);
    public abstract void setE(TState on);
    public abstract void setF(TState on);

    public abstract void calculate();

    /**A is always 90deg to the left of output
     * B is always 180deg away from the output
     * C is always 90 deg to the right of output
     * D is 90 degrees down of input
     * E is 90 degrees up of input
     * if gate is pointing up or down, assume regular coordinates
     *
     * @return whether the calling function should proceed with calculations,
     * i.e. return false if you put an input where the output of a gate should be
     */
    public boolean setRightGate(final TState s, final short deltax, final short deltay, final byte deltaz){
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
        }else if(deltaz == 1){//this gate is zAbove the input
            if(this.getDir() != Direction.INTO){//A gate going INTO would be pointing back to the wire
                setE(s);
                return true;
            }
            return false;
        }else if(deltaz == -1){//this gate is zBelow the input
            if(this.getDir() != Direction.OUTOF){//A gate going OUTOF would be pointing back to the wire
                setF(s);
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     *
     * @param tick_when_set
     */
    public void signal(final int tick_when_set){
//            System.out.println("Gate.signal(): going " +
//                            this.getDir() + ", " +
//                    out + ", at" +
//                    this.getX() + " " +
//                    this.getY() + " " +
//                    this.getZ() + " on tick " +
//                    (tick_when_set+1));
        Main.getBreadBoard().signal(
                this.getDir(),
                out,
                this.getX(),
                this.getY(),
                this.getZ(),
                tick_when_set+1);
    }

    public TState getOut(){
        return out;
    }
}



