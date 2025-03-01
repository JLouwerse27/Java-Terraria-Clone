public class Player extends AliveGameObject {

    private int x;
    private int y;
    private final int ID;
    private final int n1 = 1;
    private final int n2 = 2;

    public Player(final int x, final int y, final int id) {
        super(x, y);
        isAlive = true;
        canMove = true;
        this.ID = id;
    }

    public void move(Direction direction) {
        moving = true;
        while (isMoving()){
            
            switch (direction) {
                case UP:
                    y = Math.max(n1, y - n1);
                    x = Math.max(n1, x);
                    break;
                case DOWN:
                    y = Math.min(Main.numTiles - n2, y + n1);
                    x = Math.max(n1, x);
                    break;
                case LEFT:
                    x = Math.max(n1, x - n1);
                    y = Math.max(n1, y);
                    break;
                case RIGHT:
                    x = Math.min(Main.numTiles - n2, x + n1);
                    y = Math.max(n1, y);
                    break;
                default: //NO DIRECTION GIVEN
                    moving = false;
                    break;
            }
            moving = false;
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getId() {
        return ID;
    }

}
