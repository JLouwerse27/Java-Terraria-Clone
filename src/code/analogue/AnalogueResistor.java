package src.code.analogue;

import src.code.Direction;
import src.code.TileByte;

public class AnalogueResistor extends AnalogueBreadBoardItem{

    protected double resistance = 0;

    public AnalogueResistor(Direction dir, short x, short y, short z) {
        super(dir, x, y, z);
    }


    @Override
    public void signal(int t) {

    }

    @Override
    public byte returnTile() {
//============================CHANGE ME===============================================
        return TileByte.Any.getSymbol();
    }
}
