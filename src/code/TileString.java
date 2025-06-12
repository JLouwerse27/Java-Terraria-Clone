package src.code;

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
    Not("BN"),
    Or("BO"),
    And("BA"),
    LEDOff("Bl"),
    LEDOn("BL"),
    ButtonOff("Bb"),
    ButtonOn("BB"),
    SwitchOff("Bs"),
    SwitchOn("BS"),
    WireOff("Bw"),
    WireOn("BW"),
    CrossWire("BX"),
    Resistor1("R1"),
    Resistor3("R3"),
    Resistor5("R5"),
    Resistor10("R10");
    private final String symbol;

    TileString(String symbol) {
        this.symbol = symbol;
    }

    //returns the symbol String
    public String getSymbol() {
        return symbol;
    }
}
