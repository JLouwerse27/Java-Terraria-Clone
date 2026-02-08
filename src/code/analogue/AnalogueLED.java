package src.code.analogue;

import src.code.Enums.Direction;

public class AnalogueLED extends AnalogueBreadBoardItem{

    public AnalogueLED(Direction dir, short x, short y, short z) {
        super(dir, x, y, z);
    }

    @Override
    protected void AnalogueSignal() {

    }

    @Override
    public void signal(int t) {
        
    }

    @Override
    public byte returnTile() {
        return 0;
    }
}
