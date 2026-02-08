package src.code.analogue;

import src.code.Enums.Direction;
import src.code.Enums.TileByte;

public class TCollector extends AnalogueBreadBoardItem{



    public TCollector(Direction dir, short x, short y, short z) {
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
        return TileByte.Collector.getSymbol();
    }
}
