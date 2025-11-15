package src.code;

/**
 * The {@code TileString} enum represents different types of tiles in the game.
 * Each tile type is associated with a specific symbol.
 */
public enum TileByte {
    // BreadBoard

    //====note====
    //update
    //in this file
    //   finalTypeExcludingWireDead
    //in BreadBoard.java:
    //   addNewItemToBreadBoardItemsListInitiallyByte
    //   itemEnumBytes[], and
    //   convertToTypeBytes
    //   setBreadBoardStateByteInitial if the tile can be turned to a different colour
    //   in BreadBoard everytime you add a TileByte that is able
    //   to be edited into the board
    //in MyGameScreen.java:
    //   getColour
    //whenever you add a new tile

    Any((byte) -1),//this might be stupid
    Empty((byte) 0),
    SwitchOff((byte) 1),
    WireOff((byte) 2),
    DoubleWire((byte) 3),
    Not((byte) 4),
    Or((byte) 5),
    And((byte) 6),
    LEDOff((byte) 7),
    Resistor1((byte) 8),
    Resistor3((byte) 9),
    Resistor5((byte) 10),
    Resistor10((byte) 11),
    SwitchOn((byte) 12),
    WireOn((byte) 13),
    LEDOn((byte) 14),
    ButtonOff((byte) 15),
    ButtonOn((byte) 16),
    Xor((byte) 17),
    RedLEDOn((byte) 18),
    RedLEDOff((byte) 19),
    GreenLEDOn((byte) 20),
    GreenLEDOff((byte) 21),
    BlueLEDOn((byte) 22),
    BlueLEDOff((byte) 23),
    AnalogueWire((byte) 24),
    Collector((byte) 25),
    Base((byte) 26),
    Emitter((byte) 27),
    TriStateBufferConnected((byte)28),
    TriStateBufferDisconnected((byte)29),
    WireDead((byte) 52);
    //Resistor((byte N);

    /**
     * Used in Main.java
     */
    public static final int finalTypeExcludingWireDead = 29;

    private final byte symbol;
    TileByte(byte symbol) {
        this.symbol = symbol;
    }

    //returns the symbol String
    public byte getSymbol() {
        return symbol;
    }

    public static TileByte fromSymbol(byte symbol) {
        for (TileByte tb : values()) {
            if (tb.symbol == symbol) {
                return tb;
            }
        }
        throw new IllegalArgumentException("Invalid TileByte symbol: " + symbol);
    }

}
