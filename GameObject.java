
public abstract class GameObject {
    private int x;
    private int y;
    boolean isAlive;
    boolean canMove;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
        isAlive = false;
        canMove = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
