package src.code;

/**
 * Enum to represent the four cardinal directions.
 * <p>
 * This enum defines the four primary directions:
 * <ul>
 *   <li>{@link #UP} - Represents the upward direction.</li>
 *   <li>{@link #DOWN} - Represents the downward direction.</li>
 *   <li>{@link #LEFT} - Represents the leftward direction.</li>
 *   <li>{@link #RIGHT} - Represents the rightward direction.</li>
 * </ul>
 */
// Enum to represent the four cardinal directions
//public enum Direction {
//    UP("dU"),
//    DOWN("dD"),
//    LEFT("dL"),
//    RIGHT("dR"),
//    NONE("dN"),
//    INTO("dI"), //away from player
//    OUTOF("dO"); //to the player
//
//    private final String symbol;
//
//    //Direction(String symbol) {this.symbol = symbol;}
//    Direction(String symbol) {this.symbol = symbol;}
//
//    //returns the symbol String
//    public String getSymbol() {
//        return symbol;
//    }
//
//    public static Direction fromSymbol(String symbol) {
//        for (Direction d : values()) {
//            if (d.symbol.equals(symbol)) {
//                return d;
//            }
//        }
//        throw new IllegalArgumentException("Invalid direction symbol: " + symbol);
//    }
//}

// Enum to represent the four cardinal directions
public enum Direction {
    NONE((byte)0),
    UP((byte)1),
    RIGHT((byte)2),
    DOWN((byte)3),
    LEFT((byte)4),
    INTO((byte)5), //away from player
    OUTOF((byte)6); //to the player

    private final Byte symbol;

    //Direction(String symbol) {this.symbol = symbol;}
    Direction(Byte symbol) {this.symbol = symbol;}

    //returns the symbol String
    public Byte getSymbol() {
        return symbol;
    }

    public static Direction fromSymbol(Byte symbol) {
        for (Direction d : values()) {
            if (d.symbol.equals(symbol)) {
                return d;
            }
        }
        throw new IllegalArgumentException("Invalid direction symbol: " + symbol);
    }
}