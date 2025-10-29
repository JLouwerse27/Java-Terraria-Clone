package src.code;

public enum TState {
    DEAD((byte)-255),
    POSITIVE((byte)1),
    NEGATIVE((byte)50),
    UNUSED((byte)0);//not actually a state, but for gates with unused nodes
    byte value;

    TState(byte value) {
        this.value = value;
    }
    /**
     * NOTE, THIS FUNCTION FLIPS A DEAD STATE TO POSITIVE
     * @param state
     * @return
     */
    public static TState flip(TState state) {
        if(state == NEGATIVE){
            return POSITIVE;
        }else if(state == POSITIVE){
            return NEGATIVE;
        }else if(state == DEAD){
            return POSITIVE;
        }
        return null;
    }

    public static boolean and(TState state1, TState state2){
        if(state1 == POSITIVE && state2 == POSITIVE){
            return true;
        }else {
            return false;
        }
    }

    public static boolean or(TState state1, TState state2){
        if(state1 == POSITIVE || state2 == POSITIVE){
            return true;
        }else {
            return false;
        }
    }

    public static boolean or(TState state1, TState state2, TState state3, TState state4, TState state5, TState state6){
        if(state1 == POSITIVE || state2 == POSITIVE || state3 == POSITIVE || state4 == POSITIVE || state5 == POSITIVE
        || state6 == POSITIVE){
            return true;
        }else {
            return false;
        }
    }

    /**
     * Returns dead if all are dead
     * @param state1
     * @param state2
     * @return
     */
    public static boolean dead(TState state1, TState state2){
        if(state1 == DEAD && state2 == DEAD){
            return true;
        }
        return false;
    }

    /**
     * Returns dead if all are dead
     * @param state1
     * @param state2
     * @return
     */
    public static boolean dead(TState state1, TState state2, TState state3, TState state4, TState state5, TState state6){
        if(state1 == DEAD && state2 == DEAD && state3 == DEAD && state4 == DEAD && state5 == DEAD && state6 == DEAD){
            return true;
        }
        return false;
    }

}
