import java.awt.Image;
import java.util.HashSet;


public class Block {
    public int x;
    public int y;
    public int width;
    public int height;
    public Image image;

    public int startX;
    public int startY;
    public char direction = 'U';
    public int velocityX = 0;
    public int velocityY = 0;


    public boolean inHouse = false;
    public int houseExitDelay = 0;
    public boolean exitingHouse = false;

    public Block(Image image, int x, int y, int width, int height) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startX = x;
        this.startY = y;
    }

    public void updateDirection(char direction, HashSet<Block> walls, int tileSize) {
        char prevDirection = this.direction;
        this.direction = direction;
        updateVelocity(tileSize);
        this.x += this.velocityX;
        this.y += this.velocityY;
        for (Block wall : walls) {
            if (collidesWith(wall)) {
                this.x -= this.velocityX;
                this.y -= this.velocityY;
                this.direction = prevDirection;
                updateVelocity(tileSize);
            }
        }
    }

    public void updateVelocity(int tileSize) {
        if (this.direction == 'U') {
            this.velocityX = 0;
            this.velocityY = -tileSize / 4;
        } else if (this.direction == 'D') {
            this.velocityX = 0;
            this.velocityY = tileSize / 4;
        } else if (this.direction == 'L') {
            this.velocityX = -tileSize / 4;
            this.velocityY = 0;
        } else if (this.direction == 'R') {
            this.velocityX = tileSize / 4;
            this.velocityY = 0;
        }
    }

    public void reset() {
        this.x = this.startX;
        this.y = this.startY;
    }

    public boolean collidesWith(Block other) {
        return this.x < other.x + other.width &&
                this.x + this.width > other.x &&
                this.y < other.y + other.height &&
                this.y + this.height > other.y;
    }
}
