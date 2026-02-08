package src.code.analogue;

import src.code.Enums.Direction;
import src.code.Enums.TileByte;

public class AnalogueResistor extends AnalogueBreadBoardItem{

    protected double resistance = 0;

    public AnalogueResistor(Direction dir, short x, short y, short z) {
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
//============================CHANGE ME===============================================
        return TileByte.Any.getSymbol();
    }
}
