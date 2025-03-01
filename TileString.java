public enum TileString {
    Player(Main.sP),
    Wall(Main.sWall),
    Empty(Main.sEmpty);

    private final String symbol;

    TileString(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
