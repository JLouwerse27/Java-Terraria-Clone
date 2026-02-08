package src.code.analogue;

import src.code.Enums.Direction;
import src.code.Main;
import src.code.Enums.TileByte;

public class TBase extends AnalogueBreadBoardItem {

    protected TCollector tCollector;

    public TBase(Direction dir, short x, short y, short z) {
        super(dir, x, y, z);
        if(dir == Direction.RIGHT){
            if(Main.getBreadBoard().getBreadboardByte()[z][y][x-1] == TileByte.Base.getSymbol()){
                int tCollectorIndex = Main.getBreadBoard().getBreadBoardItemIndexAtCoordinates(x-1,y,z);
                tCollector = (TCollector)Main.getBreadBoard().getBreadBoardItemsList().get(tCollectorIndex);
                //System.out.println(tCollector);
            }
        }else if(dir == Direction.LEFT){
            if(Main.getBreadBoard().getBreadboardByte()[z][y][x+1] == TileByte.Base.getSymbol()){
                int tCollectorIndex = Main.getBreadBoard().getBreadBoardItemIndexAtCoordinates(x+1,y,z);
                tCollector = (TCollector)Main.getBreadBoard().getBreadBoardItemsList().get(tCollectorIndex);
                //System.out.println(tCollector);
            }
        }else if(dir == Direction.DOWN){
            if(Main.getBreadBoard().getBreadboardByte()[z][y-1][x] == TileByte.Base.getSymbol()){
                int tCollectorIndex = Main.getBreadBoard().getBreadBoardItemIndexAtCoordinates(x,y-1,z);
                tCollector = (TCollector)Main.getBreadBoard().getBreadBoardItemsList().get(tCollectorIndex);
                //System.out.println(tCollector);
            }
        }else if(dir == Direction.UP){
            if(Main.getBreadBoard().getBreadboardByte()[z][y+1][x] == TileByte.Base.getSymbol()){
                int tCollectorIndex = Main.getBreadBoard().getBreadBoardItemIndexAtCoordinates(x,y+1,z);
                tCollector = (TCollector)Main.getBreadBoard().getBreadBoardItemsList().get(tCollectorIndex);
                //System.out.println(tCollector);
            }
        }
    }

    @Override
    protected void AnalogueSignal() {

    }

    @Override
    public void signal(int t) {
        //note: tick place should be "t" not "t + 1" because all transistor logic should be done within
        //one tick
    }

    @Override
    public byte returnTile() {
        return TileByte.Base.getSymbol();
    }
}
