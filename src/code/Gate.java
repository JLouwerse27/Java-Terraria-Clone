package src.code;




/**
 * Abstract gate class
 */
public abstract class Gate extends BreadBoardItem {

    TState out = TState.DEAD;
    TState A = TState.DEAD;
    TState B = TState.DEAD;
    TState C = TState.DEAD;

    public Gate(final Direction dir, final short x, final short y, final byte z) {
        super(dir, x, y, z);
    }

    public abstract void setA(TState on);
    public abstract void setB(TState on);
    public abstract void setC(TState on);


    public abstract void calculate();

    /**A is always 90deg to the left of output
     * B is always 180deg away from the output
     * C is always 90 deg to the right of output
     */
    public boolean setRightGate(final TState s, final short deltax, final short deltay, final byte deltaz){
        //this gate is to the right of the input (ex. wire)
        if(deltax == 1){
            if(this.getDir() == Direction.RIGHT) {
                setB(s);
                return true;
            } else if (this.getDir() == Direction.UP) {
                setA(s);
                return true;
            }else if (this.getDir() == Direction.DOWN) {
                setC(s);
                return true;
            }
            return false;
        }else if(deltax == -1){//this gate is to the left of the input (ex. wire)
            if(this.getDir() == Direction.LEFT) {
                setB(s);
                return true;
            } else if (this.getDir() == Direction.UP) {
                setC(s);
                return true;
            }else if (this.getDir() == Direction.DOWN) {
                setA(s);
                return true;
            }
            return false;
        }else if(deltay == 1){//this gate is below the input (ex. wire)
            if(this.getDir() == Direction.RIGHT) {
                setA(s);
                return true;
            } else if (this.getDir() == Direction.LEFT) {
                setC(s);
                return true;
            }else if (this.getDir() == Direction.DOWN) {
                setB(s);
                return true;
            }
            return false;
        }else if(deltay == -1){//this gate is above the input (ex. wire)
            if(this.getDir() == Direction.RIGHT) {
                setC(s);
                return true;
            } else if (this.getDir() == Direction.LEFT) {
                setA(s);
                return true;
            }else if (this.getDir() == Direction.UP) {
                setB(s);
                return true;
            }
            return false;
        }
        return false;
    }

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



