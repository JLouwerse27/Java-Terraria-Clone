package src.code.analogue;

import src.code.Enums.Direction;

public class Battery extends AnalogueBreadBoardItem{
    public Battery(Direction dir, short x, short y, short z) {
        super(dir, x, y, z);
    }

    @Override
    protected void AnalogueSignal() {

    }

    /**
     * TO DO: INTO AND OUTOF DIRECTIONS
     * @param t
     */
    @Override
    public void signal(int t) {
//        TState digitalSignal = convertToDigital(out);
//        if(getDir() == Direction.RIGHT){
//
//        }else if(getDir() == Direction.LEFT){
//            Main.getBreadBoard().queueSignal(getDir(),digitalSignal,getX(),getY(),getZ(),t+1);
//        }else if(getDir() == Direction.UP){
//
//        }else if(getDir() == Direction.DOWN){
//
//        }
    }

    @Override
    public byte returnTile() {
        return 0;
    }
}
