package src.code.Digital;

import src.code.BreadBoardItem;
import src.code.Enums.Direction;
import src.code.Enums.TState;

public abstract class DigitalBreadBoardItem extends BreadBoardItem {

    protected TState out = TState.DEAD;//-1 off, 0 dead, 1 on

    public DigitalBreadBoardItem(Direction dir, short x, short y, short z) {
        super(dir, x, y, z);
    }

    public TState getOut(){
        return out;
    }

}
