package src.code.analogue;

import src.code.Direction;
import src.code.TileByte;

public class TEmitter extends AnalogueBreadBoardItem{
    public TEmitter(Direction dir, short x, short y, short z) {
        super(dir, x, y, z);
    }

    @Override
    public void signal(int t) {

    }

    @Override
    public byte returnTile() {
        return TileByte.Emitter.getSymbol();
    }

}
