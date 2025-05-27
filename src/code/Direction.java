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
public enum Direction {
    UP("dU"),
    DOWN("dD"),
    LEFT("dL"),
    RIGHT("dR"),
    NONE("dN");

    private final String symbol;

    Direction(String symbol) {
        this.symbol = symbol;
    }

    //returns the symbol String
    public String getSymbol() {
        return symbol;
    }

    public static Direction fromSymbol(String symbol) {
        for (Direction d : values()) {
            if (d.symbol.equals(symbol)) {
                return d;
            }
        }
        throw new IllegalArgumentException("Invalid direction symbol: " + symbol);
    }
}