package src.code.analogue;

import src.code.BreadBoardItem;
import src.code.Enums.Direction;

/**
 * Class which creates a Collector, base, and emitter, based off direction and coordinates.
 * Note this shouldn't extend BBI because then you'd have 4 BBI in total
 */
public class Transistor{

    /**
     * Contains 3 tiles: collector, base, emitter
     */
    public BreadBoardItem[] tiles = new BreadBoardItem[3];

    public Transistor(Direction dir, short x, short y, short z) {

        //create the next two transistor items: the base, and emitter
        if(dir == Direction.NONE || dir == Direction.RIGHT){
            new TCollector(Direction.DOWN,x,y,z);
            new TBase(Direction.DOWN,x,y,z);
            new TEmitter(Direction.DOWN,x,y,z);
        }else if(dir == Direction.LEFT){

        }else if(dir == Direction.UP){

        }else if(dir == Direction.DOWN){

        }
    }

}
