package src.code.analogue;

import src.code.BreadBoardItem;
import src.code.Enums.Direction;
import src.code.Enums.TState;

public abstract class AnalogueBreadBoardItem extends BreadBoardItem {
    /**
     * Analogue output variable
     */
    protected double out = 0.0;

    /**
     * Temporary variable to use while still using digital logic.
     */
    protected TState digitalOut = TState.DEAD;

    public AnalogueBreadBoardItem(Direction dir, short x, short y, short z) {
        super(dir, x, y, z);
    }

    protected TState convertToDigital(double t){
        return (t >= 5)? TState.POSITIVE : TState.NEGATIVE;
    }
    //public abstract void outputSignal(int t);

    /**
     * Provides a continuous signal (in theory).
     */
    protected abstract void AnalogueSignal();

    public TState getDigitalOut() {
        return digitalOut;
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
