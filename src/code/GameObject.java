package src.code;

/**
 * Represents a generic game object with basic properties such as position, 
 * and flags indicating whether it is alive and can move.
 */
public abstract class GameObject {
    private int x;
    private int y;
    boolean isAlive;
    boolean canMove;

    /**
     * Constructs a new GameObject with the specified position.
     * 
     * @param x The initial x-coordinate of the game object.
     * @param y The initial y-coordinate of the game object.
     */
    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
        isAlive = false;
        canMove = false;
    }

    /**
     * Gets the x-coordinate of the game object.
     * 
     * @return The x-coordinate of the game object.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the game object.
     * 
     * @return The y-coordinate of the game object.
     */
    public int getY() {
        return y;
    }
}
