package src.code;

public enum TState {
    DEAD,
    POSITIVE,
    NEGATIVE;

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

    public static boolean or(TState state1, TState state2, TState state3){
        if(state1 == POSITIVE || state2 == POSITIVE || state3 == POSITIVE){
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
    public static boolean dead(TState state1, TState state2, TState state3){
        if(state1 == DEAD && state2 == DEAD && state3 == DEAD){
            return true;
        }
        return false;
    }

}
