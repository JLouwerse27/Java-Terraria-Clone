package src.code.analogue;

import src.code.*;
import src.code.Enums.Direction;
import src.code.Enums.TState;
import src.code.Enums.TileByte;

public class TEmitter extends AnalogueBreadBoardItem{

    protected TBase tBase;
    protected TCollector tCollector;

    public TEmitter(Direction dir, short x, short y, short z) {
        super(dir, x, y, z);
        if(dir == Direction.RIGHT){
            if(Main.getBreadBoard().getBreadboardByte()[z][y][x-1] == TileByte.Base.getSymbol()){
                int tBaseIndex = Main.getBreadBoard().getBreadBoardItemIndexAtCoordinates(x-1,y,z);
                tBase = (TBase)Main.getBreadBoard().getBreadBoardItemsList().get(tBaseIndex);
                int tCollectorIndex = Main.getBreadBoard().getBreadBoardItemIndexAtCoordinates(x-2,y,z);
                tCollector = (TCollector)Main.getBreadBoard().getBreadBoardItemsList().get(tCollectorIndex);
                //System.out.println(tBase);
            }
        }else if(dir == Direction.LEFT){
            if(Main.getBreadBoard().getBreadboardByte()[z][y][x+1] == TileByte.Base.getSymbol()){
                int tBaseIndex = Main.getBreadBoard().getBreadBoardItemIndexAtCoordinates(x+1,y,z);
                tBase = (TBase)Main.getBreadBoard().getBreadBoardItemsList().get(tBaseIndex);
                int tCollectorIndex = Main.getBreadBoard().getBreadBoardItemIndexAtCoordinates(x+2,y,z);
                tCollector = (TCollector)Main.getBreadBoard().getBreadBoardItemsList().get(tCollectorIndex);
                //System.out.println(tBase);
            }
        }else if(dir == Direction.DOWN){
            if(Main.getBreadBoard().getBreadboardByte()[z][y-1][x] == TileByte.Base.getSymbol()){
                int tBaseIndex = Main.getBreadBoard().getBreadBoardItemIndexAtCoordinates(x,y-1,z);
                tBase = (TBase)Main.getBreadBoard().getBreadBoardItemsList().get(tBaseIndex);
                int tCollectorIndex = Main.getBreadBoard().getBreadBoardItemIndexAtCoordinates(x,y-2,z);
                tCollector = (TCollector)Main.getBreadBoard().getBreadBoardItemsList().get(tCollectorIndex);
                //System.out.println(tBase);
            }
        }else if(dir == Direction.UP){
            if(Main.getBreadBoard().getBreadboardByte()[z][y+1][x] == TileByte.Base.getSymbol()){
                int tBaseIndex = Main.getBreadBoard().getBreadBoardItemIndexAtCoordinates(x,y+1,z);
                tBase = (TBase)Main.getBreadBoard().getBreadBoardItemsList().get(tBaseIndex);
                int tCollectorIndex = Main.getBreadBoard().getBreadBoardItemIndexAtCoordinates(x,y+2,z);
                tCollector = (TCollector)Main.getBreadBoard().getBreadBoardItemsList().get(tCollectorIndex);
                //System.out.println(tBase);
            }
        }
    }

    @Override
    protected void AnalogueSignal() {

    }

    public void calculate(final int t){
        if(tBase.getDigitalOut() == TState.POSITIVE &&
           tCollector.getDigitalOut() == TState.POSITIVE) {
            this.digitalOut = TState.POSITIVE;
            this.signal(t);
        }
    }

    @Override
    public void signal(int t) {
        Main.getBreadBoard().queueSignal(this.getDir(), digitalOut, this.getX(), this.getY(), this.getZ(), t + 1, returnTile(),id);
    }

    public TBase getTBase() {
        return tBase;
    }

    public void setTBase(TBase tBase) {
        this.tBase = tBase;
    }

    @Override
    public byte returnTile() {
        return TileByte.Emitter.getSymbol();
    }

}
