package src.code;

/**
 * Represents a game object that is alive and can move.
 * This class extends GameObject and includes additional properties for movement and thinking capabilities.
 */
public class AliveGameObject extends GameObject {
    boolean moving;
    boolean canThink;

    /**
     * Constructs a new AliveGameObject with the specified position.
     * 
     * @param x The initial x-coordinate of the game object.
     * @param y The initial y-coordinate of the game object.
     */
    public AliveGameObject(int x, int y) {
        super(x, y);
        isAlive = true;
        canMove = true;
    }

    /**
     * Checks if the game object is alive.
     * 
     * @return true if the game object is alive, false otherwise.
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Checks if the game object can move.
     * 
     * @return true if the game object can move, false otherwise.
     */
    public boolean canMove() {
        return canMove;
    }

    /**
     * Checks if the game object is currently moving.
     * 
     * @return true if the game object is moving, false otherwise.
     */
    public boolean isMoving() {
        return moving;
    }
}