public class Player extends GameObject {

    private int x;
    private int y;

    public Player(final int x, final int y) {
        super(x, y);
        isAlive = true;
        canMove = true;
    }

    public void move(Direction direction) {
        switch (direction) {
            case UP:
                y = Math.max(1, y - 1);
                x = Math.max(1, x);
                break;
            case DOWN:
                y = Math.min(Main.n10 - 2, y + 1);
                x = Math.max(1, x);
                break;
            case LEFT:
                x = Math.max(1, x - 1);
                y = Math.max(1, y);
                break;
            case RIGHT:
                x = Math.min(Main.n10 - 2, x + 1);
                y = Math.max(1, y);
                break;
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

}
