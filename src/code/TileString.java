/**
 * The {@code TileString} enum represents different types of tiles in the game.
 * Each tile type is associated with a specific symbol.
 */
public enum TileString {
    //Terraria
    Player("P "),
    Wall("[]"),
    Empty("  "),

    //BreadBoard
    Not("N"),
    Or("O"),
    And("A"),
    LEDOff("l"),
    LEDOn("L"),
    ButtonOff("b"),
    ButtonOn("B"),
    SwitchOff("s"),
    SwitchOn("S"),
    WireOff("w"),
    WireOn("W"),
    CrossWire("X");

    private final String symbol;

    TileString(String symbol) {
        this.symbol = symbol;
    }

    //returns the symbol String
    public String getSymbol() {
        return symbol;
    }
}
