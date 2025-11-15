package src.code.analogue;

import src.code.BreadBoardItem;
import src.code.Direction;

public abstract class AnalogueBreadBoardItem extends BreadBoardItem {
    /**
     * Analogue output variable
     */
    protected double out = 0.0;

    public AnalogueBreadBoardItem(Direction dir, short x, short y, short z) {
        super(dir, x, y, z);
    }

    /**
     * Returns an analogue output,
     * maybe between 0 and 1, or possibly negative voltages
     * @return
     */
    public double getOut(){
        return out;
    }

}
