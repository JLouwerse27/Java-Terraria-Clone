package src.code;

/**
 * Represents a player in the game.
 * The player can move in four directions: UP, DOWN, LEFT, and RIGHT.
 * This class extends AliveGameObject and includes the player's position (x, y) and unique identifier (ID).
 */
public class Player extends AliveGameObject {

    private int x;
    private int y;
    private final int ID;
    private final int n1 = 1;
    private final int n2 = 2;

    /**
     * Constructs a new Player with the specified position and ID.
     * 
     * @param x  The initial x-coordinate of the player.
     * @param y  The initial y-coordinate of the player.
     * @param id The unique identifier for the player.
     */
    public Player(final int x, final int y, final int id) {
        super(x, y);
        isAlive = true;
        canMove = true;
        this.ID = id;
    }

    /**
     * Moves the player in the specified direction.
     * The player's position is updated based on the direction and boundaries of the game.
     * 
     * @param direction The direction in which to move the player.
     */
    public void move(Direction direction) {
        moving = true;
        while (isMoving()) {
            switch (direction) {
                case Direction.UP:
                    y = Math.max(n1, y - n1);
                    x = Math.max(n1, x);
                    break;
                case Direction.DOWN:
                    y = Math.min(Main.numTiles - n2, y + n1);
                    x = Math.max(n1, x);
                    break;
                case Direction.LEFT:
                    x = Math.max(n1, x - n1);
                    y = Math.max(n1, y);
                    break;
                case Direction.RIGHT:
                    x = Math.min(Main.numTiles - n2, x + n1);
                    y = Math.max(n1, y);
                    break;
                default: // NO DIRECTION GIVEN
                    moving = false;
                    break;
            }
            moving = false;
        }
    }

    /**
     * Gets the x-coordinate of the player.
     * 
     * @return The x-coordinate of the player.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Gets the y-coordinate of the player.
     * 
     * @return The y-coordinate of the player.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Gets the unique identifier of the player.
     * 
     * @return The unique identifier of the player.
     */
    public int getId() {
        return ID;
    }
}
