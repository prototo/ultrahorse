import java.awt.Graphics;
import java.util.Random;

/**
 * Created by greg on 11/08/13.
 */
public class Entity {
    private Sprite sprite;
    private double x;
    private double y;
    private double moveSpeed;

    private Random r;

    public Entity(String ref, double x, double y) {
        this.sprite = SpriteStore.get().getSprite(ref);
        this.x = x;
        this.y = y;
        this.moveSpeed = 5;

        r = new Random();
    }

    public void draw(Graphics g) {
        sprite.draw(g, (int) x, (int) y);
    }

    public void move(boolean up, boolean down, boolean left, boolean right) {
        if (up) {
            this.y -= moveSpeed;
        }
        if (down) {
            this.y += moveSpeed;
        }
        if (left) {
            this.x -= moveSpeed;
        }
        if (right) {
            this.x += moveSpeed;
        }
    }
}
