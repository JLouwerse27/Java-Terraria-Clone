package src.code.Digital;

import src.code.*;
import src.code.Enums.Direction;
import src.code.Enums.TState;
import src.code.Enums.TileByte;

public class TriStateBuffer extends Gate {

    public TriStateBuffer(Direction dir, short x, short y, short z) {
        super(dir, x, y, z);
    }

    @Override
    public void calculate() {

        if(this.getDir() == Direction.NONE || this.getDir() == Direction.RIGHT){
            //top and left respectively
            if(B == TState.POSITIVE){
                out = A;
                Main.getBreadBoard().setBreadBoardTileByte(TileByte.TriStateBufferConnected.getSymbol(), getX(), getY(), getZ());
            }else {
                Main.getBreadBoard().setBreadBoardTileByte(TileByte.TriStateBufferDisconnected.getSymbol(), getX(), getY(), getZ());
            }
            //otherwise do nothing, the "switch" is off; no current flows -- no changes
        } else if(this.getDir() == Direction.LEFT){
            //top and right respectively
            if(B == TState.POSITIVE){
                out = C;
                Main.getBreadBoard().setBreadBoardTileByte(TileByte.TriStateBufferConnected.getSymbol(), getX(), getY(), getZ());
            }else {
                Main.getBreadBoard().setBreadBoardTileByte(TileByte.TriStateBufferDisconnected.getSymbol(), getX(), getY(), getZ());
            }
            //otherwise do nothing, the "switch" is off; no current flows -- no changes
        } else if(this.getDir() == Direction.UP){
            //right and bottom respectively
            if(C == TState.POSITIVE){
                out = D;
                Main.getBreadBoard().setBreadBoardTileByte(TileByte.TriStateBufferConnected.getSymbol(), getX(), getY(), getZ());
            }else {
                Main.getBreadBoard().setBreadBoardTileByte(TileByte.TriStateBufferDisconnected.getSymbol(), getX(), getY(), getZ());
            }
            //otherwise do nothing, the "switch" is off; no current flows -- no changes
        } else if(this.getDir() == Direction.DOWN){
            //right and top respectively
            if(C == TState.POSITIVE){
                out = B;
                Main.getBreadBoard().setBreadBoardTileByte(TileByte.TriStateBufferConnected.getSymbol(), getX(), getY(), getZ());
            }else {
                Main.getBreadBoard().setBreadBoardTileByte(TileByte.TriStateBufferDisconnected.getSymbol(), getX(), getY(), getZ());
            }
            //otherwise do nothing, the "switch" is off; no current flows -- no changes
        }

    }

    @Override
    public void signal(int tick_when_set) {
        //change to variable if buggy.
        if(Main.getBreadBoard().getBreadboardByte()[getZ()][getY()][getX()]
        == TileByte.TriStateBufferConnected.getSymbol()){
            super.signal(tick_when_set);
        }
    }

    @Override
    public byte returnTile() {
        if(B == TState.POSITIVE){
            return TileByte.TriStateBufferConnected.getSymbol();
        } else {
            return TileByte.TriStateBufferDisconnected.getSymbol();
        }

    }
}
