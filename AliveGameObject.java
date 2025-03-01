public class AliveGameObject extends GameObject {
    boolean moving;
    boolean canThink;

    public AliveGameObject(int x, int y) {
        super(x, y);
        isAlive = true;
        canMove = true;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean canMove() {
        return canMove;
    }

    public boolean isMoving() {
        return moving;
    }
    
}