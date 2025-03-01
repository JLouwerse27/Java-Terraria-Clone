/**
 * The {@code TileString} enum represents different types of tiles in the game.
 * Each tile type is associated with a specific symbol.
 */
public enum TileString {
    Player("P "),
    Wall("[]"),
    Empty("  ");

    private final String symbol;

    TileString(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
